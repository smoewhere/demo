# Spring-Security

## 1 基本介绍

主要是包括了security的基本配置和基本依赖

### 1.1.  使用maven导入包

- 官网文档：https://docs.spring.io/spring-security/site/docs/5.4.1/reference/html5/#getting

有基于spring-boot的

```xml
<dependencies>
    <!-- ... other dependency elements ... -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
</dependencies>
```



不基于spring-boot

```xml
<!--导入security的依赖pom管理，确保版本依赖的包版本相同-->
<dependencyManagement>
    <dependencies>
        <!-- ... other dependency elements ... -->
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-bom</artifactId>
            <version>{spring-security-version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
<!--导入security的依赖-->
<dependencies>
    <!-- 在web中使用则导入web -->
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-config</artifactId>
    </dependency>
</dependencies>
```

### 1.2. Core — 核心jar包

以下的jar包是核心jar，如果要使用security，则必须依赖这些jar，它支持独立的应用程序，远程客户端，方法（服务层）安全性和JDBC用户配置。如果要使用其他功能，则需要导入其他相关jar包。

- `org.springframework.security.core`
- `org.springframework.security.access`
- `org.springframework.security.authentication`
- `org.springframework.security.provisioning`

关于其他可选功能jar包，详细请看官方文档;

https://docs.spring.io/spring-security/site/docs/5.4.1/reference/html5/#modules

## 2. 在servlet中使用

在传统的servlet-web项目中，spring-security使用了系列的Filter来拦截请求并验证。官方的大图：

![image-20201029144021512](.\images\filterchain.png)

以上单个http请求的流程，其中经过了多个Filter，spring-security就是其中一个Filter：

![image-20201029144255694](.\images\securityFilter.png)

其中DelegatingFilterProxy就是Spring设置的过滤器，这个是Spring在配置web的时候常用的过滤器，其作用就是可以先把过滤器注册到servlet容器中，然后在运行时，通过getFilter()方法获取真正的Filter。在这个过滤器中，真正的实现是FilterChainProxy。

具体如图所示：

![image-20201029144621197](.\images\securityChain.png)

可以看到，在FilterChainProxy中有多个SecurityFilterChain，根据配置的url获取需要拦截的FilterChain，然后执行SecurityFilterChain中的一系列过滤器，进行校验。Spring官方文档也给出了SecurityFilterChain中所有的Filter的顺序和类型，具体如下：

- ChannelProcessingFilter
- WebAsyncManagerIntegrationFilter
- SecurityContextPersistenceFilter
- HeaderWriterFilter
- CorsFilter
- CsrfFilter
- LogoutFilter
- OAuth2AuthorizationRequestRedirectFilter
- Saml2WebSsoAuthenticationRequestFilter
- X509AuthenticationFilter
- AbstractPreAuthenticatedProcessingFilter
- CasAuthenticationFilter
- OAuth2LoginAuthenticationFilter
- Saml2WebSsoAuthenticationFilter
- [`UsernamePasswordAuthenticationFilter`](https://docs.spring.io/spring-security/site/docs/5.4.1/reference/html5/#servlet-authentication-usernamepasswordauthenticationfilter)
- OpenIDAuthenticationFilter
- DefaultLoginPageGeneratingFilter
- DefaultLogoutPageGeneratingFilter
- ConcurrentSessionFilter
- [`DigestAuthenticationFilter`](https://docs.spring.io/spring-security/site/docs/5.4.1/reference/html5/#servlet-authentication-digest)
- BearerTokenAuthenticationFilter
- [`BasicAuthenticationFilter`](https://docs.spring.io/spring-security/site/docs/5.4.1/reference/html5/#servlet-authentication-basic)
- RequestCacheAwareFilter
- SecurityContextHolderAwareRequestFilter
- JaasApiIntegrationFilter
- RememberMeAuthenticationFilter
- AnonymousAuthenticationFilter
- OAuth2AuthorizationCodeGrantFilter
- SessionManagementFilter
- [`ExceptionTranslationFilter`](https://docs.spring.io/spring-security/site/docs/5.4.1/reference/html5/#servlet-exceptiontranslationfilter)
- [`FilterSecurityInterceptor`](https://docs.spring.io/spring-security/site/docs/5.4.1/reference/html5/#servlet-authorization-filtersecurityinterceptor)
- SwitchUserFilter

在后面的内容对这些拦截器做出具体说明。接下来看具体的配置。本文是基于Spring-Boot来使用SpringSecurity。

### 2.1.  在SpringBoot中自动配置

```xml
<!--导入spring-start-->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

导入jar包之后就开启了自动配置，对于spring的自动配置，其实就是扫描META-INF\spring.factories文件，获取所有的自动配置类，进行配置。在spring-autoconfig下有这些类，不是全部，但是Servlet相关的大概这些：

```java
org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration,\
org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration,\
org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration,\
```

从上往下开始

#### 2.1.1 SecurityAutoConfiguration

顾名思义，自动配置Security的类，代码如下：

```java
@Configuration(proxyBeanMethods = false)
// 如果类路径存在DefaultAuthenticationEventPublisher
@ConditionalOnClass(DefaultAuthenticationEventPublisher.class)
// 配置文件自动映射
@EnableConfigurationProperties(SecurityProperties.class)
// 导入3个配置类
@Import({ SpringBootWebSecurityConfiguration.class, WebSecurityEnablerConfiguration.class,
		SecurityDataConfiguration.class })
public class SecurityAutoConfiguration {

    // 如果容器中不存在AuthenticationEventPublisher类，则生成一个
	@Bean
	@ConditionalOnMissingBean(AuthenticationEventPublisher.class)
	public DefaultAuthenticationEventPublisher authenticationEventPublisher(ApplicationEventPublisher publisher) {
		return new DefaultAuthenticationEventPublisher(publisher);
	}

}
```

- SpringBootWebSecurityConfiguration：

```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(WebSecurityConfigurerAdapter.class)
@ConditionalOnMissingBean(WebSecurityConfigurerAdapter.class)
@ConditionalOnWebApplication(type = Type.SERVLET)
public class SpringBootWebSecurityConfiguration {

	@Configuration(proxyBeanMethods = false)
	@Order(SecurityProperties.BASIC_AUTH_ORDER)
	static class DefaultConfigurerAdapter extends WebSecurityConfigurerAdapter {

	}

}
```

在这个类中，默认定义了一个DefaultConfigurerAdapter，并且在注释中有一个@ConditionalOnMissingBean(WebSecurityConfigurerAdapter.class)，也就是说如果没有自定义WebSecurityConfigurerAdapter的话，就会按照这个默认的来配置，否则就按照自定义的来。

- WebSecurityEnablerConfiguration

开启自动配置的注解

```java
@Configuration(proxyBeanMethods = false)
// 容器中是否存在WebSecurityConfigurerAdapter的bean
@ConditionalOnBean(WebSecurityConfigurerAdapter.class)
@ConditionalOnMissingBean(name = BeanIds.SPRING_SECURITY_FILTER_CHAIN)
// 当前环境是servlet环境
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableWebSecurity
public class WebSecurityEnablerConfiguration {

}
```

代码很简单，如果容器中存在WebSecurityConfigurerAdapter的bean，可以自定义的也可以是默认的，则使用@EnableWebSecurity注解开启自动配置。

```java
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Documented
@Import({ WebSecurityConfiguration.class,
		SpringWebMvcImportSelector.class,
		OAuth2ImportSelector.class })
// 一些全局的配置：ObjectPostProcessorConfiguration、AuthenticationConfiguration
@EnableGlobalAuthentication
@Configuration
public @interface EnableWebSecurity {

	/**
	 * Controls debugging support for Spring Security. Default is false.
	 * @return if true, enables debug support with Spring Security
	 */
	boolean debug() default false;
}
```

@EnableWebSecurity 真正开启自动配置的代码。里面导入了三个配置类

- WebSecurityConfiguration		配置security相关
- SpringWebMvcImportSelector  配置mvc相关
- OAuth2ImportSelector              配置OAuth2相关

主要讲WebSecurityConfiguration	这个类。配置比较多。

```java
// Object的后置增强处理，在ObjectPostProcessorConfiguration进行了配置，主要用于一些new出来的类，而且实现了spring一些Awear之类的接口，进行后置的增强
	@Autowired(required = false)
	private ObjectPostProcessor<Object> objectObjectPostProcessor;

	// 一些监听器，SmartApplicationListener，用来处理SmartApplicationListener支持的事件
	@Bean
	public static DelegatingApplicationListener delegatingApplicationListener() {
		return new DelegatingApplicationListener();
	}

	// security的异常处理
	@Bean
	@DependsOn(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME)
	public SecurityExpressionHandler<FilterInvocation> webSecurityExpressionHandler() {
		return webSecurity.getExpressionHandler();
	}

	// springSecurityFilterChain，这个就是大图中的FilterChainProxy，真正的过滤器链
	@Bean(name = AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME)
	public Filter springSecurityFilterChain() throws Exception {
		boolean hasConfigurers = webSecurityConfigurers != null
				&& !webSecurityConfigurers.isEmpty();
		if (!hasConfigurers) {
			WebSecurityConfigurerAdapter adapter = objectObjectPostProcessor
					.postProcess(new WebSecurityConfigurerAdapter() {
					});
			webSecurity.apply(adapter);
		}
		return webSecurity.build();
	}
	// 一些其他的类
	@Bean
	@DependsOn(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME)
	public WebInvocationPrivilegeEvaluator privilegeEvaluator() {
		return webSecurity.getPrivilegeEvaluator();
	}

	// 用于扫描全部的WebSecurityConfigurerAdapter。可以配置多个，这样就有多个springSecurityFilterChain
	@Autowired(required = false)
	public void setFilterChainProxySecurityConfigurer(
			ObjectPostProcessor<Object> objectPostProcessor,
			@Value("#{@autowiredWebSecurityConfigurersIgnoreParents.getWebSecurityConfigurers()}") List<SecurityConfigurer<Filter, WebSecurity>> webSecurityConfigurers)
			throws Exception {
		webSecurity = objectPostProcessor
				.postProcess(new WebSecurity(objectPostProcessor));
		.............
            // 把所有的配置类塞入webSecurity
         for (SecurityConfigurer<Filter, WebSecurity> webSecurityConfigurer : webSecurityConfigurers) {
			webSecurity.apply(webSecurityConfigurer);
		}
		this.webSecurityConfigurers = webSecurityConfigurers;
	}

	// 这个类就是上面这个方法的@autowiredWebSecurityConfigurersIgnoreParents.getWebSecurityConfigurers()。
	//在getWebSecurityConfigurers中获取所有容器中存在的WebSecurityConfigurerAdapter
	@Bean
	public static AutowiredWebSecurityConfigurersIgnoreParents autowiredWebSecurityConfigurersIgnoreParents(
			ConfigurableListableBeanFactory beanFactory) {
		return new AutowiredWebSecurityConfigurersIgnoreParents(beanFactory);
	}
```

总体顺序就是：

1. 全局配置AutowireBeanFactoryObjectPostProcessor增强器

2. AutowiredWebSecurityConfigurersIgnoreParents扫描所有的WebSecurityConfigurerAdapter，

3. setFilterChainProxySecurityConfigurer方法把所有的WebSecurityConfigurerAdapter注入WebSecurity中
4. springSecurityFilterChain通过build方法产生FilterChainProxy

- springSecurityFilterChain

重点关注springSecurityFilterChain中的build

```java
	@Bean(name = AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME)
	public Filter springSecurityFilterChain() throws Exception {
        // 这里有加了一重验证，判断是否有webSecurityConfigurers，没有就新建一个
		boolean hasConfigurers = webSecurityConfigurers != null
				&& !webSecurityConfigurers.isEmpty();
		if (!hasConfigurers) {
			WebSecurityConfigurerAdapter adapter = objectObjectPostProcessor
					.postProcess(new WebSecurityConfigurerAdapter() {
					});
			webSecurity.apply(adapter);
		}
		return webSecurity.build();
	}
```

先上一张uml类图

![image-20201029155907944](.\images\webSecurity.png)

SecurityBuilder是顶级接口，基本上security相关的所有builder都是和他有关，他只有一个方法

```java
public interface SecurityBuilder<O> {
	O build() throws Exception;
}
```

看看webSecurity.build();做了啥

```java
public final O build() throws Exception {
		if (this.building.compareAndSet(false, true)) {
			this.object = doBuild();
			return this.object;
		}
		throw new AlreadyBuiltException("This object has already been built");
	}
// 首先调用了AbstractSecurityBuilder中的build方法，然后调用了AbstractConfiguredSecurityBuilder中的doBuild方法
	@Override
	protected final O doBuild() throws Exception {
		synchronized (configurers) {
			buildState = BuildState.INITIALIZING;

			beforeInit();
			init();

			buildState = BuildState.CONFIGURING;

			beforeConfigure();
			configure();

			buildState = BuildState.BUILDING;

			O result = performBuild();

			buildState = BuildState.BUILT;

			return result;
		}
	}
```

 doBuild方法就是真正的配置了，先后有5个动作

1. beforeInit
2.  init()
3. beforeConfigure()
4. configure()
5. performBuild()

init方法：

```java
private void init() throws Exception {
		Collection<SecurityConfigurer<O, B>> configurers = getConfigurers();

		for (SecurityConfigurer<O, B> configurer : configurers) {
			configurer.init((B) this);
		}

		for (SecurityConfigurer<O, B> configurer : configurersAddedInInitializing) {
			configurer.init((B) this);
		}
	}
```

从这里就可以看到了，之前扫描出来的WebSecurityConfigurerAdapter被一个个拿出来，然后进行init。

接下来看WebSecurityConfigurerAdapter.init()方法

```java
public void init(final WebSecurity web) throws Exception {
		final HttpSecurity http = getHttp();
		web.addSecurityFilterChainBuilder(http).postBuildAction(() -> {
			FilterSecurityInterceptor securityInterceptor = http
					.getSharedObject(FilterSecurityInterceptor.class);
			web.securityInterceptor(securityInterceptor);
		});
	}

protected final HttpSecurity getHttp() throws Exception {
		if (http != null) {
			return http;
		}

		AuthenticationEventPublisher eventPublisher = getAuthenticationEventPublisher();
		localConfigureAuthenticationBldr.authenticationEventPublisher(eventPublisher);

		AuthenticationManager authenticationManager = authenticationManager();
		authenticationBuilder.parentAuthenticationManager(authenticationManager);
		Map<Class<?>, Object> sharedObjects = createSharedObjects();

		http = new HttpSecurity(objectPostProcessor, authenticationBuilder,
				sharedObjects);
		if (!disableDefaults) {
			// @formatter:off
			http
				.csrf().and()
				.addFilter(new WebAsyncManagerIntegrationFilter())
				.exceptionHandling().and()
				.headers().and()
				.sessionManagement().and()
				.securityContext().and()
				.requestCache().and()
				.anonymous().and()
				.servletApi().and()
				.apply(new DefaultLoginPageConfigurer<>()).and()
				.logout();
			// @formatter:on
			ClassLoader classLoader = this.context.getClassLoader();
			List<AbstractHttpConfigurer> defaultHttpConfigurers =
					SpringFactoriesLoader.loadFactories(AbstractHttpConfigurer.class, classLoader);

			for (AbstractHttpConfigurer configurer : defaultHttpConfigurers) {
				http.apply(configurer);
			}
		}
		configure(http);
		return http;
	}
```

这里可以看到，在getHttp()中配置了所有的过滤器，然后在最后添加了一个FilterSecurityInterceptor。

最后就是performBuild()方法

```java
@Override
	protected Filter performBuild() throws Exception {
		Assert.state(
				!securityFilterChainBuilders.isEmpty(),
				() -> "At least one SecurityBuilder<? extends SecurityFilterChain> needs to be specified. "
						+ "Typically this done by adding a @Configuration that extends WebSecurityConfigurerAdapter. "
						+ "More advanced users can invoke "
						+ WebSecurity.class.getSimpleName()
						+ ".addSecurityFilterChainBuilder directly");
		int chainSize = ignoredRequests.size() + securityFilterChainBuilders.size();
		List<SecurityFilterChain> securityFilterChains = new ArrayList<>(
				chainSize);
		for (RequestMatcher ignoredRequest : ignoredRequests) {
			securityFilterChains.add(new DefaultSecurityFilterChain(ignoredRequest));
		}
		for (SecurityBuilder<? extends SecurityFilterChain> securityFilterChainBuilder : securityFilterChainBuilders) {
			securityFilterChains.add(securityFilterChainBuilder.build());
		}
		FilterChainProxy filterChainProxy = new FilterChainProxy(securityFilterChains);
		if (httpFirewall != null) {
			filterChainProxy.setFirewall(httpFirewall);
		}
		filterChainProxy.afterPropertiesSet();

		Filter result = filterChainProxy;
		if (debugEnabled) {
			logger.warn("\n\n"
					+ "********************************************************************\n"
					+ "**********        Security debugging is enabled.       *************\n"
					+ "**********    This may include sensitive information.  *************\n"
					+ "**********      Do not use in a production system!     *************\n"
					+ "********************************************************************\n\n");
			result = new DebugFilter(filterChainProxy);
		}
		postBuildAction.run();
		return result;
	}
```

这里可以看到，对每个securityFilterChainBuilders，其实就是HttpSecurity。因为在WebSecurity的init方法中，调用addSecurityFilterChainBuilder添加了进来。

接下去就是HttpSecurity的配置

![image-20201029162714867](.\images\HttpSecurity.png)

和WebSecurity差不多，所以也是那套流程，在里面有很多个FilterConfigurtion，通过FilterConfigurtion的configure方法，添加Filter。

到这里整个过滤器链就已经配置好了，但是还没加到容器中啊，这里就涉及到其他配置类。继续往下看

#### 2.1.2  UserDetailsServiceAutoConfiguration

该配置类和oauth2有关

#### 2.1.3 SecurityFilterAutoConfiguration

```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = Type.SERVLET)
@EnableConfigurationProperties(SecurityProperties.class)
@ConditionalOnClass({ AbstractSecurityWebApplicationInitializer.class, SessionCreationPolicy.class })
@AutoConfigureAfter(SecurityAutoConfiguration.class)
public class SecurityFilterAutoConfiguration {

	private static final String DEFAULT_FILTER_NAME = AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME;

	@Bean
	@ConditionalOnBean(name = DEFAULT_FILTER_NAME)
	public DelegatingFilterProxyRegistrationBean securityFilterChainRegistration(
			SecurityProperties securityProperties) {
		DelegatingFilterProxyRegistrationBean registration = new DelegatingFilterProxyRegistrationBean(
				DEFAULT_FILTER_NAME);
		registration.setOrder(securityProperties.getFilter().getOrder());
		registration.setDispatcherTypes(getDispatcherTypes(securityProperties));
		return registration;
	}

	private EnumSet<DispatcherType> getDispatcherTypes(SecurityProperties securityProperties) {
		if (securityProperties.getFilter().getDispatcherTypes() == null) {
			return null;
		}
		return securityProperties.getFilter().getDispatcherTypes().stream()
				.map((type) -> DispatcherType.valueOf(type.name()))
				.collect(Collectors.toCollection(() -> EnumSet.noneOf(DispatcherType.class)));
	}

}
```

代码一目了然，在SecurityAutoConfiguration配置完成之后，通过DelegatingFilterProxyRegistrationBean方法，注册一个Filter到容器里，并且设置Order为Integer.MIN_VALUE。即这个过滤器在第一位。

这样就完成了Filter的自动配置。剩下的细节，比如HttpSecurity是如何配置的，在下文中继续。