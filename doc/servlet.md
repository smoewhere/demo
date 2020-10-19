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

## 关于HttpServletRequest中输入流的问题

HttpServletRequest中获取内容共用三种方法：

1. request.getParameter()
2. request.getReader()
3. request.getInputStream()

- request.getParameter()

从url路径中或者body体中获取参数。参数的形式为key=value&key=value。这里读取的方式有两种，如果Content-type为application/x-www-form-urlencoded则会从body体中读取。而且如果只是单纯的调用request.getParameter()，并且在url和body中都有同名参数，则只从url中读取。只有调用request.getParameterValues()才会获取body中

- request.getReader()

获取body中的数据，输入流为包装好的字符串输入流

- request.getInputStream()

获取body数据，输入流为字节流

### 三种方法的互相影响

除了从url中获取参数外，其余的从body体中获取数据都是对流的读取，而且tomcat中的流默认是CoyoteInputStream，读取完就到流末尾了，不能多次读取。因此引出一下问题。

1. 如果Content-type为application/x-www-form-urlencoded，并且执行过request.getParameter()方法，则流已经到末尾，再次调用request.getReader()、request.getInputStream()读取的输入流为null，反之亦然，即这两类方法不能在同一个request中互相存在
2. 如果Content-type不为application/x-www-form-urlencoded,则tomcat中的request.getParameter()并不会对body体进行读取，这时候可以调用request.getReader()、request.getInputStream()获取输入流，这两类方法互不影响。
3. request.getReader()、request.getInputStream()只能执行一次。

### 解决输入流只能读取一次的方法

输入流只能读取一次的原因是默认的输入流不能多次读取，所以要解决的话就是把输入流变成可以多次读取的流。

具体方法为：

在一次request请求中添加一个Filter，最好是放在调用链的首位，然后对request请求使用HttpServletRequestWrapper进行包装。

> NOTE: 本来是在包装的构造函数中进行数据读取，存到类中。但是这样会导致输入流被读取一次，调用request.getParameter()则不能获取body中的参数。所以在getInputStream()方法中进行包装。**需要注意的是如果调用过request.getParameter()，那么在此调用getInputStream()必然为空。**

```java
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.8.28 15:22
 */
public class RepeatAbleRequestWrapper extends HttpServletRequestWrapper {

  private ByteArrayInputStream inputStream;

  private static final int BUFFER_SIZE = 4096;

  private byte[] requestBody;

  private boolean isRead = false;

  private final HttpServletRequest request;

  /**
   * Constructs a request object wrapping the given request.
   *
   * @param request 请求
   * @throws IllegalArgumentException if the request is null
   */
  public RepeatAbleRequestWrapper(HttpServletRequest request) throws IOException {
    super(request);
    this.request = request;
    //copyBody(request.getInputStream());
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {
    if (!isRead) {
      copyBody(request.getInputStream());
      isRead = true;
    }
    return new RepeatAbleInputStream(requestBody);
  }


  private int copyBody(InputStream in) throws IOException {
    if (in == null) {
      requestBody = new byte[0];
      return 0;
    }
    if (in.markSupported()) {
      in.reset();
    }
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(BUFFER_SIZE);
    int byteCount = 0;
    byte[] buffer = new byte[BUFFER_SIZE];
    int len = 0;
    while ((len = in.read(buffer)) != -1) {
      outputStream.write(buffer, 0, len);
      byteCount += len;
    }
    outputStream.flush();
    requestBody = outputStream.toByteArray();
    return byteCount;
  }

  private static class RepeatAbleInputStream extends ServletInputStream {

    private final InputStream delegate;

    protected RepeatAbleInputStream(byte[] buffer) {
      delegate = new ByteArrayInputStream(buffer);
    }

    @Override
    public boolean isFinished() {
      return false;
    }

    @Override
    public boolean isReady() {
      return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
      throw new UnsupportedOperationException();
    }

    @Override
    public int read() throws IOException {
      return this.delegate.read();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
      return this.delegate.read(b, off, len);
    }

    @Override
    public int read(byte[] b) throws IOException {
      return this.delegate.read(b);
    }

    @Override
    public long skip(long n) throws IOException {
      return this.delegate.skip(n);
    }

    @Override
    public int available() throws IOException {
      return this.delegate.available();
    }

    @Override
    public void close() throws IOException {
      this.delegate.close();
    }

    @Override
    public synchronized void mark(int readlimit) {
      this.delegate.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
      this.delegate.reset();
    }

    @Override
    public boolean markSupported() {
      return this.delegate.markSupported();
    }
  }
}
```



