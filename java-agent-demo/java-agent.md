# JAVA-AGENT

参考： https://www.cnblogs.com/rickiyang/p/11368932.html

### Javaagent 是什么？

Javaagent是java命令的一个参数。参数 javaagent 可以用于指定一个 jar 包，并且对该 java 包有2个要求：

1. 这个 jar 包的 MANIFEST.MF 文件必须指定 Premain-Class 项。
2. Premain-Class 指定的那个类必须实现 premain() 方法。

premain 方法，从字面上理解，就是运行在 main 函数之前的的类。当Java 虚拟机启动时，在执行 main 函数之前，JVM 会先运行`-javaagent`所指定 jar 包内 Premain-Class 这个类的 premain 方法 。

在命令行输入 `java`可以看到相应的参数，其中有 和 java agent相关的：

```shell
Copy-agentlib:<libname>[=<选项>] 加载本机代理库 <libname>, 例如 -agentlib:hprof
	另请参阅 -agentlib:jdwp=help 和 -agentlib:hprof=help
-agentpath:<pathname>[=<选项>]
	按完整路径名加载本机代理库
-javaagent:<jarpath>[=<选项>]
	加载 Java 编程语言代理, 请参阅 java.lang.instrument
```

在上面`-javaagent`参数中提到了参阅`java.lang.instrument`，这是在`rt.jar` 中定义的一个包，该路径下有两个重要的类：

- ClassFileTransformer
- Instrumentation

该包提供了一些工具帮助开发人员在 Java 程序运行时，动态修改系统中的 Class 类型。其中，使用该软件包的一个关键组件就是 Javaagent。从名字上看，似乎是个 Java 代理之类的，而实际上，他的功能更像是一个Class 类型的转换器，他可以在运行时接受重新外部请求，对Class类型进行修改。

从本质上讲，Java Agent 是一个遵循一组严格约定的常规 Java 类。 上面说到 javaagent命令要求指定的类中必须要有premain()方法，并且对premain方法的签名也有要求，签名必须满足以下两种格式：

```java
public static void premain(String agentArgs, Instrumentation inst)
    
public static void premain(String agentArgs)
```

JVM 会优先加载 带 `Instrumentation` 签名的方法，加载成功忽略第二种，如果第一种没有，则加载第二种方法。这个逻辑在sun.instrument.InstrumentationImpl 类中。

**JVM启动后动态Instrument**

上面介绍的Instrumentation是在 JDK 1.5中提供的，开发者只能在main加载之前添加手脚，在 Java SE 6 的 Instrumentation 当中，提供了一个新的代理操作方法：agentmain，可以在 main 函数开始运行之后再运行。

跟`premain`函数一样， 开发者可以编写一个含有`agentmain`函数的 Java 类：

```java
//采用attach机制，被代理的目标程序VM有可能很早之前已经启动，当然其所有类已经被加载完成，这个时候需要借助Instrumentation#retransformClasses(Class<?>... classes)让对应的类可以重新转换，从而激活重新转换的类执行ClassFileTransformer列表中的回调
public static void agentmain (String agentArgs, Instrumentation inst)

public static void agentmain (String agentArgs)
```

同样，agentmain 方法中带Instrumentation参数的方法也比不带优先级更高。开发者必须在 manifest 文件里面设置“Agent-Class”来指定包含 agentmain 函数的类。

在Java6 以后实现启动后加载的新实现是Attach api。Attach API 很简单，只有 2 个主要的类，都在 `com.sun.tools.attach` 包里面。



### 使用 javaagent 需要几个步骤：

1. 定义一个 MANIFEST.MF 文件，必须包含 Premain-Class 选项，通常也会加入Can-Redefine-Classes 和 Can-Retransform-Classes 选项。
2. 创建一个Premain-Class 指定的类，类中包含 premain 方法，方法逻辑由用户自己确定。
3. 将 premain 的类和 MANIFEST.MF 文件打成 jar 包。
4. 使用参数 -javaagent: jar包路径 启动要代理的方法。

### 项目说明

该项目下有3个文件夹，其中：

1. java-agent是测试启动前加载agent的jar包。其作用是打印加载的类名。
2. agent-main是测试启动后动态attach的jar包。作用是动态修改方法体内容。
3. agent-test是测试代码。

#### 使用说明

要测试java-agent，在启动测试代码Main.java时，添加vm参数-javaagent:....\java-agent-1.0.0.jar。

-javaagent参数是可以存在多个的，当有多个存在时，按照定义的顺序执行。

要测试agent-main的功能，先启动Main，然后运行AgentMainTest。AgentMainTest会attach到目标虚拟机，然后动态加载agent.jar，修改内存中的Main.class的定义。

#### 动态修改方法原理

动态修改方法是通过动态加载agent.jar，然后添加自定义的ClassFileTransformer，并且触发Instrumentation.retransformClasses或者Instrumentation.redefineClasses方法，辅助以ASM等修改字节码的技术，修改方法体内容，重新编译成字节码。

#### 动态修改的限制

动态修改只对新运行的方法或者新生成的对象生效。即：

如果一个类已经老的字节码创建，通过动态修改，修改了属性的默认值，只对新new的对象生效。

如果一个方法已经在执行，修改了方法体里的内容不会生效，只有下次再调用才会生效。因为java中方法的执行对应到虚拟机字节码就是invokevirtual等指令的执行，如果已经被调用了，方法的信息以及被压入栈了，不会被新修改的内容影响。如果是新调用，则会把新的方法体指令压入栈。