# Spring-Gateway

## gateway解析路由配置

spring对于路由配置的解析，通过对于路由的断言和Filter解析，组装成路由。入口为RouteLocator接口。总体流程为

DispatcherHandler.handle(ServerWebExchange exchange) -> AbstractHandlerMapping.getHandler(ServerWebExchange exchange) -> RoutePredicateHandlerMapping.getHandlerInternal() -> RoutePredicateHandlerMapping.lookupRoute() -> RouteLocator.getRoutes()

并且在spring的自动配置中，默认的RouteLocator实现为CachingRouteLocator，代码片段为：GatewayAutoConfiguration

```java
@Bean
@Primary
@ConditionalOnMissingBean(name = "cachedCompositeRouteLocator")
// TODO: property to disable composite?
public RouteLocator cachedCompositeRouteLocator(List<RouteLocator> routeLocators) {
 return new CachingRouteLocator(
   new CompositeRouteLocator(Flux.fromIterable(routeLocators)));
}
```

CachingRouteLocator老千层饼了，里面包了一个CompositeRouteLocator，CompositeRouteLocator里面又包了其他的RouteLocator，在默认配置里，最里层的为RouteDefinitionRouteLocator

总结就是，CachingRouteLocator为最外层，用于缓存所有的路由信息。即，在初始化的时候调用里面的CompositeRouteLocator的getRoutes()方法，然后依次调用最里面的routeLocators的getRoutes()方法，获取所有路由，然后缓存在属性字段里，当路由刷新事件触发时（RefreshRoutesEvent），刷新缓存。

### 解析断言

spring的断言由断言工厂类创建，核心方法为RoutePredicateFactory中的applyAsync方法。

```java
default AsyncPredicate<ServerWebExchange> applyAsync(Consumer<C> consumer) {
 C config = newConfig();
 consumer.accept(config);
 beforeApply(config);
 return applyAsync(config);
}
```

主要配置实现在于config，拿QueryRoutePredicateFactory举例。

在QueryRoutePredicateFactory中有个内部Config类，基本每个工厂都有该类，用于配置参数。

QueryRoutePredicateFactory创建QueryRoutePredicate的代码在RouteDefinitionRouteLocator中。

从lookup入手，主要流程就是先构架config类，然后通过applyAsync创建AsyncPredicate。整体代码：

```java
@SuppressWarnings("unchecked")
private AsyncPredicate<ServerWebExchange> lookup(RouteDefinition route,
  PredicateDefinition predicate) {
 RoutePredicateFactory<Object> factory = this.predicates.get(predicate.getName());
 if (factory == null) {
  throw new IllegalArgumentException(
    "Unable to find RoutePredicateFactory with name "
      + predicate.getName());
 }
 if (logger.isDebugEnabled()) {
  logger.debug("RouteDefinition " + route.getId() + " applying "
    + predicate.getArgs() + " to " + predicate.getName());
 }

 // @formatter:off
 // 关键代码
 Object config = this.configurationService.with(factory)
   .name(predicate.getName())
   .properties(predicate.getArgs())
   .eventFunction((bound, properties) -> new PredicateArgsEvent(
     RouteDefinitionRouteLocator.this, route.getId(), properties))
   .bind();
 // @formatter:on

 return factory.applyAsync(config);
}
```

关键在于创建Config类，通过properties把配置文件中的解析的参数传入，然后通过bing方法创建Config。其中bind方法中为：

```java
public T bind() {
 validate();
 Assert.hasText(this.name, "name may not be empty");
 Assert.isTrue(this.properties != null || this.normalizedProperties != null,
   "properties and normalizedProperties both may not be null");

 if (this.normalizedProperties == null) {
     // 关键在于这个方法
  this.normalizedProperties = normalizeProperties();
 }

 T bound = doBind();

 if (this.eventFunction != null && this.service.publisher != null) {
  ApplicationEvent applicationEvent = this.eventFunction.apply(bound,
    this.normalizedProperties);
  this.service.publisher.publishEvent(applicationEvent);
 }

 return bound;
}
```

normalizeProperties方法为ConfigurableBuilder.normalizeProperties()

```java
@Override
protected Map<String, Object> normalizeProperties() {
 if (this.service.beanFactory != null) {
  // 关键方法为normalize
  return this.configurable.shortcutType().normalize(this.properties,
    this.configurable, this.service.parser, this.service.beanFactory);
 }
 return super.normalizeProperties();
}
```

这里从configurable（就是QueryRoutePredicateFactory自己）获取shortcutType，默认是ShortcutConfigurable.shortcutType()(DEFAULT)。然后在normalize方法中，获取key和value，然后存入map中。

```java
DEFAULT {
 @Override
 public Map<String, Object> normalize(Map<String, String> args,
   ShortcutConfigurable shortcutConf, SpelExpressionParser parser,
   BeanFactory beanFactory) {
  Map<String, Object> map = new HashMap<>();
  int entryIdx = 0;
  for (Map.Entry<String, String> entry : args.entrySet()) {
   String key = normalizeKey(entry.getKey(), entryIdx, shortcutConf,
     args);
   Object value = getValue(parser, beanFactory, entry.getValue());

   map.put(key, value);
   entryIdx++;
  }
  return map;
 }
}
// 其中的normalizeKey方法
static String normalizeKey(String key, int entryIdx, ShortcutConfigurable argHints,
			Map<String, String> args) {
		// RoutePredicateFactory has name hints and this has a fake key name
		// replace with the matching key hint
		if (key.startsWith(NameUtils.GENERATED_NAME_PREFIX)
				&& !argHints.shortcutFieldOrder().isEmpty() && entryIdx < args.size()
				&& entryIdx < argHints.shortcutFieldOrder().size()) {
			key = argHints.shortcutFieldOrder().get(entryIdx);
		}
		return key;
	}
```

这里，如果是用精简语句自动生成的key，会以GENERATED_NAME_PREFIX开头，并且从shortcutFieldOrder方法中获取参数名称，QueryRoutePredicateFactory中的参数名称为：

```java
/**
 * Param key.
 */
public static final String PARAM_KEY = "param";

/**
 * Regexp key.
 */
public static final String REGEXP_KEY = "regexp";

public QueryRoutePredicateFactory() {
 super(Config.class);
}

@Override
public List<String> shortcutFieldOrder() {
 return Arrays.asList(PARAM_KEY, REGEXP_KEY);
}
// 内部config
@Validated
	public static class Config {

		@NotEmpty
		private String param;

		private String regexp;

		public String getParam() {
			return param;
		}

		public Config setParam(String param) {
			this.param = param;
			return this;
		}

		public String getRegexp() {
			return regexp;
		}

		public Config setRegexp(String regexp) {
			this.regexp = regexp;
			return this;
		}

	}
```

其实就是config的属性名。

获取值的方法可以从spel解析（如果是#{}包围），也可以是直接的值。接下来的bind方法，其实就是把这个解析好的key-value的map作为propertySources，利用spring的binder创建对象并赋值

```java
public static <T> T bindOrCreate(Bindable<T> bindable, Map<String, Object> properties,
  String configurationPropertyName, Validator validator,
  ConversionService conversionService) {
 // see ConfigurationPropertiesBinder from spring boot for this definition.
 BindHandler handler = new IgnoreTopLevelConverterNotFoundBindHandler();

 if (validator != null) { // TODO: list of validators?
  handler = new ValidationBindHandler(handler, validator);
 }

 List<ConfigurationPropertySource> propertySources = Collections
   .singletonList(new MapConfigurationPropertySource(properties));

 return new Binder(propertySources, null, conversionService)
   .bindOrCreate(configurationPropertyName, bindable, handler);
}
```

