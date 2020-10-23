# ELK日志平台搭建

ELK即Elasticsearch , Logstash, Kibana，其中

- Elasticsearch ：负责日志索引的建立，方便快速查询
- Logstash：负责收集日志，可以从各种渠道收集日志，然后交给Elasticsearch 集中管理（这只是其中一部分功能，Logstash的功能就是数据的收集、过滤、转化，最后输出）
- Kibana：可视化的日志查看UI。

官网下载地址：<a id="link">https://www.elastic.co/cn/downloads/?elektra=home&storm=hero</a>

## ELK架构

ELK的架构有多种，比较灵活。

1. 最简单的一种就是，直接通过Logstash收集日志，各个应用把日志发送到Logstash，然后交由Elasticsearch 。

架构图如下：（// TODO）



2. 第二种就是通过消息队列等中间件采集数据，然后发送给Logstash。这样可以解决由于Logstash故障导致的消息丢失。

架构图：（//TODO）



3. 第三种是官方推荐的，通过Filebeat收集日志，传输到Logstash处理

## 简单架构搭建流程

<a href="https://www.elastic.co/cn/downloads/?elektra=home&storm=hero">先从官网下载</a>

首先安装Elasticsearch ，官网都有教程

1. 下载然后解压
2. 运行`bin/elasticsearch` (or `bin\elasticsearch.bat` on Windows)
3. 运行`curl http://localhost:9200/` or `Invoke-RestMethod http://localhost:9200` with PowerShell获取信息

需要修改的配置文件/config/elasticsearch.yml

```yaml
#注释打开
cluster.name: my-application
#注释打开
node.name: node-1
#打开注释，删除原有的node-2
cluster.initial_master_nodes: ["node-1"]
#添加一行，elasticsearch 7之后默认打开xpack，但是只能用30天，只用基础版的，免费
xpack.license.self_generated.type: basic


```

然后安装kibana

1. 下载解压
2. 编辑配置文件
3. 运行bin/kibana
4. 默认端口号为5061

配置文件：config/kibana.yml

```yaml
#kibana的绑定地址，默认是localhost，即拒绝远程连接，配置为网卡地址
server.host: "192.168.10.102"
#kibana服务名称。如果使用的反向代理，则需要配置路径前缀，因为里面的js都是绝对路径
server.basePath: "/kibana"
#设置为中文
i18n.locale: "zh-CN"
```

安装Logstash

1. 下载解压
2. 修改配置文件
3. 运行启动  `bin/logstash -f logstash.conf`

./logstash.bat --path.settings ../ -f ..\config\logstash-log4j2.conf --config.test_and_exit 测试配置文件是否正确

配置文件详细文档：https://www.elastic.co/guide/en/logstash/current/configuration.html

```tex
# Sample Logstash configuration for creating a simple
# Beats -> Logstash -> Elasticsearch pipeline.

input {
  gelf {
        port => 4567
        codec => json {
			charset => "UTF-8"
		}
    }
}

filter {
	if [server] {
		mutate {
			remove_field => ["server"]
		}
	}
	if [@version] {
		mutate {
			remove_field => ["@version"]
		}
	}
	if [project] {
		mutate {
			lowercase => ["project"]
		}
	} else {
		mutate {
			add_field => { "project" => "nobody" }
		}
	}
	if [server.fqdn] {
		mutate {
			lowercase => ["server.fqdn"]
		}
	} else {
		mutate {
				add_field => { "server.fqdn" => "nobody" }
			}
	}
}

output { 
	elasticsearch {
		hosts => ["127.0.0.1:9200"]
		index => "%{project}-%{server.fqdn}-%{+YYYY.MM.dd}"
	}
    stdout {}
}



```

其中，input是表示数据输入，可以从多种渠道输入，filter则是中间数据解析，output为输出

input -> filter -> output

```shell
bin/logstash-plugin list 					   ## 列出所有插件
bin/logstash-plugin list --verbose 		    	## 列出所有插件和版本信息
bin/logstash-plugin list '*namefragment*'  		## 列出所有符合pattern的所有插件
bin/logstash-plugin list --group output 	    ## 列出所有output分组下的所有插件（所有分组为input, filter, codec, output）
```



