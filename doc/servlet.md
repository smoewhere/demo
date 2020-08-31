# Servlet

servlet三大组件：listener，filter，servlet。执行顺序为 listener -> filter -> servlet 



## Filter

在servlet 3.0之前，需要手动在web.xml中申明filter，filter执行的顺序按照filter-mapping定义的先后顺序。

servlet 3.0之后可以通过@WebFilter注解，tomcat会扫描有此注解的类，添加为filter，其顺序按照filter的名字排序，不能显式的排序。

```java
@WebFilter(filterName = "customer", urlPatterns = {"/*"},
    initParams = @WebInitParam(name = "name", value = "co1"))
public class CustomerFilter implements Filter {

  private static final Logger log = LoggerFactory.getLogger(CustomerFilter.class);

  private static final AtomicInteger nu = new AtomicInteger(1);

  private String name;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    String name = filterConfig.getInitParameter("name");
    if (StringUtils.isNotBlank(name)) {
      this.name = name;
    } else {
      this.name = "co" + nu.getAndIncrement();
    }
    log.info("[CustomerFilter.init] init name is {}!", this.name);
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    log.info("[CustomerFilter.doFilter] doFilter! name is {}", this.name);
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
    log.info("[CustomerFilter.destroy] destroy!");
  }
}
```

另：在ServletContainerInitializer中指定的类也可以添加filter。具体可以查看spring的SpringServletContainerInitializer。

在onStartup方法中会传入ServletContext，可以添加各种组件。

```java
public interface ServletContainerInitializer {
    
    public void onStartup(Set<Class<?>> c, ServletContext ctx)
        throws ServletException; 
}
```

需要注意的是：

- 在ServletContext中添加的filter，其执行顺序一定是在@WebFilter之后的，不管组件名字如何定义，都后于@WebFilter定义的filter。
- 如果在ServletContext中添加filter时配置了isMatchAfter属性为false（即先于所有filter），则执行顺序优先于其他。
- isMatchAfter为false的filter的顺序按照添加顺序执行。

