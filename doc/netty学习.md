## Netty

### 简单介绍Java IO模型

1. BIO
    同步并阻塞（一个socket一个线程）
    通常适用于连接数目小并且固定的架构，这种方式对服务器资源要求比较高，并发局限于应用中，JDK1.4以前的唯一选择，程序简单易理解。
2. NIO
    同步非阻塞（选择器、多路复用）
    适用于连接数多且连接比较短的架构，比如聊天服务器，弹幕系统，服务器之间通讯等。
3. AIO
    异步非阻塞（等待操作系统完成之后通知程序）
    适用于连接数目多且连接比较长的架构，比如相册服务器，充分调用OS参与并发操作。

- netty架构图

  ![netty架构图](.\images\netty架构图.png)

netty是以反应堆模型(Reactor)写的框架.

取决于Reactor的数量和Handler线程数量的不同，Reactor模型有3个变种：

1. 单reactor 单线程
2. 单reactor 多线程
3. 主从reactor 多线程

Netty线程模型

Netty主要是基于主从Reactors多线程模型(如下图)做了一些修改，其中主从reactor多线程模型有多个reactor：

1. MainReactor负责客户端的连接请求，并将请求转交给SubReactor
2. SubReactor负责相应通道的IO读写请求
3. 非IO请求(具体逻辑处理)的任务则会直接进入写入队列，等到worker threads进行处理

这里引用Doug Lee大神的Reactor介绍：Scalable IO in Java里面关于主从Reactor多线程模型的图：

![Reactor模型图](.\images\Reactor模型.png)

特别说明的是：虽然Netty的线程模型基于主从Reactor多线程，借用了MainReactor和SubReactor的结构。但是实际实现上SubReactor和Worker线程在同一个线程池中

1. bossGroup线程池则只是在bind某个端口后，获得其中一个线程作为MainReactor，专门处理端口的Accept事件，每个端口对应一个boss线程
2. workerGroup线程池会被各个SubReactor和Worker线程充分利用

### 代码流程

#### 启动流程

```java
public class NettyServer {


    public static void main(String[] args) throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new MyInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(8081).sync(); // (7)

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
```

以上是netty官网上的代码，MyInitializer是自定义的初始化类，一般主要是给客户端连接后生成的socketChannel添加pipeline。

- 整体启动概述

> 以上代码主要做的事情有

1. 创建两个NioEventLoopGroup，作为bossGroup和workGroup，boss负责监听端口，当有连接时，把接收的socket注册到workGroup，让其工作。
2. 创建ServerBootstrap，这是启动引导类
3. 给ServerBootstrap绑定参数
4. 调用ServerBootstrap的bind方法，传入端口号，正式启动服务

在这之前，先说一下一般的java nio代码，netty其实也是这个流程走的

```java
public static void main(String[] args) throws IOException {
		// 核心选择器
        Selector selector = Selector.open();
    	// 服务端socket通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 设置为非阻塞，一定要设置，不然。。。
        serverSocketChannel.configureBlocking(false);
        // 绑定端口
        serverSocketChannel.bind(new InetSocketAddress(8080));
        // 注册到选择器中
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            if(selector.select(200) == 0){
                System.out.println("==");
                continue;
            }
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                if(key.isAcceptable()){
                    // TODO
                }
                if(key.isReadable()){
                    // TODO
                }
                if(key.isWritable() && key.isValid()){
                    // TODO
                }
                if(key.isConnectable()){
                    System.out.println("isConnectable = true");
                }
                iter.remove();
            }
        }
    }
```



##### **NioEventLoopGroup的创建**

```java
// 默认构造函数
public NioEventLoopGroup() {
        this(0);
 }
// 这里有个默认线程数
protected MultithreadEventLoopGroup(int nThreads, Executor executor, Object... args) {
    super(nThreads == 0 ? DEFAULT_EVENT_LOOP_THREADS : nThreads, executor, args);
}
// 默认为操作系统cpu核心数 *2
DEFAULT_EVENT_LOOP_THREADS = Math.max(1, SystemPropertyUtil.getInt(
                "io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
// 通过一系列的转调，最后调到父类的方法
protected MultithreadEventExecutorGroup(int nThreads, Executor executor,
                                        EventExecutorChooserFactory chooserFactory, Object... args) {
    if (nThreads <= 0) {
        throw new IllegalArgumentException(String.format("nThreads: %d (expected: > 0)", nThreads));
    }
	// 定义的线程池
    if (executor == null) {
        executor = new ThreadPerTaskExecutor(newDefaultThreadFactory());
    }
	// 创建一个线程数组
    children = new EventExecutor[nThreads];

    for (int i = 0; i < nThreads; i ++) {
        boolean success = false;
        try {
            // 代码往里点就是new 一个NioEventLoop，所以其实就是一个NioEventLoopGroup下有多个NioEventLoop
            children[i] = newChild(executor, args);
            success = true;
        } catch (Exception e) {
            // TODO: Think about if this is a good exception type
            throw new IllegalStateException("failed to create a child event loop", e);
        } finally {
            if (!success) {
                for (int j = 0; j < i; j ++) {
                    children[j].shutdownGracefully();
                }

                for (int j = 0; j < i; j ++) {
                    EventExecutor e = children[j];
                    try {
                        while (!e.isTerminated()) {
                            e.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
                        }
                    } catch (InterruptedException interrupted) {
                        // Let the caller handle the interruption.
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
    }

    chooser = chooserFactory.newChooser(children);
	...........
}


```

##### **bind方法**

```java
private ChannelFuture doBind(final SocketAddress localAddress) {
    	// 1.在这里初始化通道
        final ChannelFuture regFuture = initAndRegister();
        final Channel channel = regFuture.channel();
        if (regFuture.cause() != null) {
            return regFuture;
        }

        if (regFuture.isDone()) {
            // At this point we know that the registration was complete and successful.
            ChannelPromise promise = channel.newPromise();
            // 在这里实现端口绑定
            doBind0(regFuture, channel, localAddress, promise);
            return promise;
        } else {
            // Registration future is almost always fulfilled already, but just in case it's not.
            final PendingRegistrationPromise promise = new PendingRegistrationPromise(channel);
            regFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    Throwable cause = future.cause();
                    if (cause != null) {
                        // Registration on the EventLoop failed so fail the ChannelPromise directly to not cause an
                        // IllegalStateException once we try to access the EventLoop of the Channel.
                        promise.setFailure(cause);
                    } else {
                        // Registration was successful, so set the correct executor to use.
                        // See https://github.com/netty/netty/issues/2586
                        promise.registered();

                        doBind0(regFuture, channel, localAddress, promise);
                    }
                }
            });
            return promise;
        }
    }
```

##### 初始化通道

```java
final ChannelFuture initAndRegister() {
    Channel channel = null;
    try {
        // 主要是这段话
        channel = channelFactory.newChannel();
        init(channel);
    } catch (Throwable t) {
        .....
    }
    ChannelFuture regFuture = config().group().register(channel);
    if (regFuture.cause() != null) {
        if (channel.isRegistered()) {
            channel.close();
        } else {
            channel.unsafe().closeForcibly();
        }
    }
    return regFuture;
}
```

channelFactory.newChannel();

这其实就是一开始的配置，一开始有这么一个配置.channel(NioServerSocketChannel.class)，这边其实就是调用反射创建这个对象。

然后NioServerSocketChannel这个类的构造函数中，去初始化了通道。具体代码如下：

```java
// ReflectiveChannelFactory.java ,反射创建
@Override
public T newChannel() {
    try {
        return constructor.newInstance();
    } catch (Throwable t) {
        throw new ChannelException("Unable to create Channel from class " + constructor.getDeclaringClass(), t);
    }
}
// 初始化通道
/**
 * Create a new instance
 */
public NioServerSocketChannel() {
    this(newSocket(DEFAULT_SELECTOR_PROVIDER));
}
// provider
private static final SelectorProvider DEFAULT_SELECTOR_PROVIDER = SelectorProvider.provider();

private static ServerSocketChannel newSocket(SelectorProvider provider) {
    try {
        // 就是这边打开了通道
        return provider.openServerSocketChannel();
    } catch (IOException e) {
        throw new ChannelException(
            "Failed to open a server socket.", e);
    }
}
// 设置为非阻塞
public NioServerSocketChannel(ServerSocketChannel channel) {
        super(null, channel, SelectionKey.OP_ACCEPT);
        config = new NioServerSocketChannelConfig(this, javaChannel().socket());
}
// 父类方法
protected AbstractNioChannel(Channel parent, SelectableChannel ch, int readInterestOp) {
        super(parent);
        this.ch = ch;
        // 这里设置为ACCEPT，这里加了一层包装
        this.readInterestOp = readInterestOp;
        try {
            // 这里设置了非阻塞
            ch.configureBlocking(false);
        } catch (IOException e) {
            try {
                ch.close();
            } catch (IOException e2) {
                logger.warn(
                            "Failed to close a partially initialized socket.", e2);
            }

            throw new ChannelException("Failed to enter non-blocking mode.", e);
        }
    }
```

接着是  init(channel); 方法

```java
void init(Channel channel) {
    // 设置一些参数
    setChannelOptions(channel, options0().entrySet().toArray(EMPTY_OPTION_ARRAY), logger);
    setAttributes(channel, attrs0().entrySet().toArray(EMPTY_ATTRIBUTE_ARRAY));
    // 获取pipeline
    ChannelPipeline p = channel.pipeline();
    // 这个childGroup就是workGroup
    final EventLoopGroup currentChildGroup = childGroup;
    final ChannelHandler currentChildHandler = childHandler;
    final Entry<ChannelOption<?>, Object>[] currentChildOptions =
        childOptions.entrySet().toArray(EMPTY_OPTION_ARRAY);
    final Entry<AttributeKey<?>, Object>[] currentChildAttrs = childAttrs.entrySet().toArray(EMPTY_ATTRIBUTE_ARRAY);
	// 这段比较重要，netty往服务端的通道中，给pipeline加了一个handler，当这个通道初始化完成之后，
    // pipeline中继续添加一个ServerBootstrapAcceptor的handler。这个handler比较重要
    p.addLast(new ChannelInitializer<Channel>() {
        @Override
        public void initChannel(final Channel ch) {
            final ChannelPipeline pipeline = ch.pipeline();
            ChannelHandler handler = config.handler();
            if (handler != null) {
                pipeline.addLast(handler);
            }

            ch.eventLoop().execute(new Runnable() {
                @Override
                public void run() {
                    pipeline.addLast(new ServerBootstrapAcceptor(
                        ch, currentChildGroup, currentChildHandler, currentChildOptions, currentChildAttrs));
                }
            });
        }
    });
}
```

##### 注册通道到EventLoopGroup

```java
// 这个方法其实就是把通道注册到EventLoopGroup中
ChannelFuture regFuture = config().group().register(channel);
// 往里
@Override
public ChannelFuture register(Channel channel) {
    return next().register(channel);
}
// 还记得一开始一个EventLoopGroup中初始化了多个EventLoop么，这边就是每次注册的时候，通过轮询的方式，一个个往下。
// 注册核心方法
@Override
public final void register(EventLoop eventLoop, final ChannelPromise promise) {
    ObjectUtil.checkNotNull(eventLoop, "eventLoop");
    if (isRegistered()) {
        promise.setFailure(new IllegalStateException("registered to an event loop already"));
        return;
    }
    if (!isCompatible(eventLoop)) {
        promise.setFailure(
            new IllegalStateException("incompatible event loop type: " + eventLoop.getClass().getName()));
        return;
    }

    AbstractChannel.this.eventLoop = eventLoop;
	// 一开始的时候这边的eventLoop中的thread变量为null，所以是false
    if (eventLoop.inEventLoop()) {
        register0(promise);
    } else {
        try {
            // 然后就调用到了这边，点进去就是启动一个线程
            eventLoop.execute(new Runnable() {
                @Override
                public void run() {
                    register0(promise);
                }
            });
        } catch (Throwable t) {
            logger.warn(
                "Force-closing a channel whose registration task was not accepted by an event loop: {}",
                AbstractChannel.this, t);
            closeForcibly();
            closeFuture.setClosed();
            safeSetFailure(promise, t);
        }
    }
}
// execute
private void execute(Runnable task, boolean immediate) {
    boolean inEventLoop = inEventLoop();
    // 往任务队列中添加一个任务
    addTask(task);
    // 一开始是null
    if (!inEventLoop) {
        // 所以进来了启动一个线程，这个动作就是类似我们以前，写一个死循环，然后监听事件，netty就是在这里开始的死循环，并且在这个方法里面，把thread赋值为当前线程，所以的代码进这里就只是单纯添加任务，不会再去启动线程了
        startThread();
        if (isShutdown()) {
            boolean reject = false;
            try {
                if (removeTask(task)) {
                    reject = true;
                }
            } catch (UnsupportedOperationException e) {
                // The task queue does not support removal so the best thing we can do is to just move on and
                // hope we will be able to pick-up the task before its completely terminated.
                // In worst case we will log on termination.
            }
            if (reject) {
                reject();
            }
        }
    }

    if (!addTaskWakesUp && immediate) {
        wakeup(inEventLoop);
    }
}
```

到这里为止，netty就已经启动成功了，忘记说注册通道到选择器了。。就在register0方法中。

#### 执行流程

之前是启动成功监听端口了，那么有一个客户端连接进来之后，代码流程是怎样的呢？

在NioEventLoop（这个是真正干活的）的死循环中

##### 监听事件

```java
protected void run() {
    int selectCnt = 0;
    for (;;) {
        try {
            int strategy;
            try {
                // 判断选择器中是否有事件
                strategy = selectStrategy.calculateStrategy(selectNowSupplier, hasTasks());
                switch (strategy) {
                    case SelectStrategy.CONTINUE:
                        continue;

                    case SelectStrategy.BUSY_WAIT:
                        // fall-through to SELECT since the busy-wait is not supported with NIO

                    case SelectStrategy.SELECT:
                        long curDeadlineNanos = nextScheduledTaskDeadlineNanos();
                        if (curDeadlineNanos == -1L) {
                            curDeadlineNanos = NONE; // nothing on the calendar
                        }
                        nextWakeupNanos.set(curDeadlineNanos);
                        try {
                            if (!hasTasks()) {
                                // 这个就是nio的select方法咯
                                strategy = select(curDeadlineNanos);
                            }
                        } finally {
                            // This update is just to help block unnecessary selector wakeups
                            // so use of lazySet is ok (no race condition)
                            nextWakeupNanos.lazySet(AWAKE);
                        }
                        // fall through
                    default:
                }
            } catch (IOException e) {
                // If we receive an IOException here its because the Selector is messed up. Let's rebuild
                // the selector and retry. https://github.com/netty/netty/issues/8566
                rebuildSelector0();
                selectCnt = 0;
                handleLoopException(e);
                continue;
            }

            selectCnt++;
            cancelledKeys = 0;
            needsToSelectAgain = false;
            // 这个是百分比，处理io事件的百分比，默认50，如果是100，就先做完io，再做其他
            final int ioRatio = this.ioRatio;
            boolean ranTasks;
            if (ioRatio == 100) {
                try {
                    if (strategy > 0) {
                        processSelectedKeys();
                    }
                } finally {
                    // Ensure we always run tasks.
                    ranTasks = runAllTasks();
                }
            } else if (strategy > 0) {
                final long ioStartTime = System.nanoTime();
                try {
                    // 这个就是主要处理selectKey的方法了，对不同的信号做不同的处理
                    processSelectedKeys();
                } finally {
                    // Ensure we always run tasks.
                    final long ioTime = System.nanoTime() - ioStartTime;
                    ranTasks = runAllTasks(ioTime * (100 - ioRatio) / ioRatio);
                }
            } else {
                ranTasks = runAllTasks(0); // This will run the minimum number of tasks
            }

            if (ranTasks || strategy > 0) {
                if (selectCnt > MIN_PREMATURE_SELECTOR_RETURNS && logger.isDebugEnabled()) {
                    logger.debug("Selector.select() returned prematurely {} times in a row for Selector {}.",
                                 selectCnt - 1, selector);
                }
                selectCnt = 0;
            } else if (unexpectedSelectorWakeup(selectCnt)) { // Unexpected wakeup (unusual case)
                selectCnt = 0;
            }
        } catch (CancelledKeyException e) {
            // Harmless exception - log anyway
            if (logger.isDebugEnabled()) {
                logger.debug(CancelledKeyException.class.getSimpleName() + " raised by a Selector {} - JDK bug?",
                             selector, e);
            }
        } catch (Throwable t) {
            handleLoopException(t);
        }
        // Always handle shutdown even if the loop processing threw an exception.
        try {
            if (isShuttingDown()) {
                closeAll();
                if (confirmShutdown()) {
                    return;
                }
            }
        } catch (Throwable t) {
            handleLoopException(t);
        }
    }
}
```

##### 处理selectKey

```java
// 这里有两种方法，说明一下，netty默认对SelectorImpl中的selectedKeys的数据结构做了优化，jdk中是HashSet的结构，netty直接继承AbstractSet新建了一个类，优化了时间复杂度。这个优化是用反射实现的，如果优化失败，就用原生的。
// 具体看NioEventLoop.java 中	构造函数的openSelector()
private void processSelectedKeys() {
    if (selectedKeys != null) {
        processSelectedKeysOptimized();
    } else {
        processSelectedKeysPlain(selector.selectedKeys());
    }
}
// 接下来就是熟悉的遍历selectedKey，根据状态做不同操作
private void processSelectedKeysOptimized() {
    for (int i = 0; i < selectedKeys.size; ++i) {
        final SelectionKey k = selectedKeys.keys[i];
        // null out entry in the array to allow to have it GC'ed once the Channel close
        // See https://github.com/netty/netty/issues/2363
        selectedKeys.keys[i] = null;

        final Object a = k.attachment();

        if (a instanceof AbstractNioChannel) {
            processSelectedKey(k, (AbstractNioChannel) a);
        } else {
            @SuppressWarnings("unchecked")
            NioTask<SelectableChannel> task = (NioTask<SelectableChannel>) a;
            processSelectedKey(k, task);
        }

        if (needsToSelectAgain) {
            // null out entries in the array to allow to have it GC'ed once the Channel close
            // See https://github.com/netty/netty/issues/2363
            selectedKeys.reset(i + 1);

            selectAgain();
            i = -1;
        }
    }
}
```

##### 处理接收事件

当收到请求时的处理流程如下：

```java
if ((readyOps & (SelectionKey.OP_READ | SelectionKey.OP_ACCEPT)) != 0 || readyOps == 0) {
    unsafe.read();
}
// 
public void read() {
    assert eventLoop().inEventLoop();
    final ChannelConfig config = config();
    // 到目前为止，还是在ServerSocketChannel中的，所以这边获取的参数都是Server的
    final ChannelPipeline pipeline = pipeline();
    final RecvByteBufAllocator.Handle allocHandle = unsafe().recvBufAllocHandle();
    allocHandle.reset(config);

    boolean closed = false;
    Throwable exception = null;
    try {
        try {
            do {
                // 这个方法里，就是获取连接了
                int localRead = doReadMessages(readBuf);
                if (localRead == 0) {
                    break;
                }
                if (localRead < 0) {
                    closed = true;
                    break;
                }

                allocHandle.incMessagesRead(localRead);
            } while (allocHandle.continueReading());
        } catch (Throwable t) {
            exception = t;
        }

        int size = readBuf.size();
        for (int i = 0; i < size; i ++) {
            readPending = false;
            // 执行pipeline中的fireChannelRead方法，在创建pipeline时，netty就加了头和尾事件，所以都是从头开始执行，头部一般就是调用下一个事件的方法，然后就到了一开始添加的ServerBootstrapAcceptor的handler方法么，这里就执行过去了
            pipeline.fireChannelRead(readBuf.get(i));
        }
        readBuf.clear();
        allocHandle.readComplete();
        pipeline.fireChannelReadComplete();

        if (exception != null) {
            closed = closeOnReadError(exception);

            pipeline.fireExceptionCaught(exception);
        }

        if (closed) {
            inputShutdown = true;
            if (isOpen()) {
                close(voidPromise());
            }
        }
    } finally {
        if (!readPending && !config.isAutoRead()) {
            removeReadOp();
        }
    }
}
```

doMessages方法：

```java
@Override
protected int doReadMessages(List<Object> buf) throws Exception {
    // 就是这里用accept接收的
    SocketChannel ch = SocketUtils.accept(javaChannel());
    try {
        if (ch != null) {
            buf.add(new NioSocketChannel(this, ch));
            return 1;
        }
    } catch (Throwable t) {
        logger.warn("Failed to create a new channel from an accepted socket.", t);

        try {
            ch.close();
        } catch (Throwable t2) {
            logger.warn("Failed to close a socket.", t2);
        }
    }

    return 0;
}
```

ServerBootstrapAcceptor中的channelRead方法

```java
public void channelRead(ChannelHandlerContext ctx, Object msg) {
    final Channel child = (Channel) msg;
	// 这里就是往子线程中添加pipeline，childHandler为一开始自定义的MyInitializer类
    child.pipeline().addLast(childHandler);

    setChannelOptions(child, childOptions, logger);
    setAttributes(child, childAttrs);

    try {
        // 然后注册到eventLoop中，和serverSocketChannel的注册一样，也是轮询每一个eventLoop，注册进去，然后因为一开始thread为null，就会去启动线程。netty就是通过线程是否启动去做的
        childGroup.register(child).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    forceClose(child, future.cause());
                }
            }
        });
    } catch (Throwable t) {
        forceClose(child, t);
    }
}
```

到这里，基本就结束了。。。

### 注意事项

1. 每一次请求初始化的时候，都会调用自定义的ChannelInitializer.initChannel方法，所以每次pipeline中除了头尾是同一个对象，其他的都是不同的，pipeline是和channel绑定的。
2. nio中事件的就绪条件：
   1. `OP_ACCEPT`就绪条件：当收到一个客户端的连接请求时，该操作就绪。这是`ServerSocketChannel`上唯一有效的操作。
   2. `OP_CONNECT`就绪条件：只有客户端`SocketChannel`会注册该操作，当客户端调用`SocketChannel.connect()`时，该操作会就绪。
   3. `OP_READ`就绪条件：该操作对客户端和服务端的`SocketChannel`都有效，当OS的读缓冲区中有数据可读时，该操作就绪。
   4. `OP_WRITE`就绪条件：该操作对客户端和服务端的`SocketChannel`都有效，当OS的写缓冲区中有空闲的空间时(大部分时候都有)，该操作就绪。