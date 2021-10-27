# GCS
A multi-server group chat system based on netty.
<br/>
### 一个基于Java netty的多人聊天室。
1：自定义协议解决TCP沾包问题。<br/>
2：基于NIO IO多路复用，支持超高连接数。<br/>
3：服务端通过zookeeper进行服务注册，支持服务端拓展增加服务能力。<br/>
4：客户端通过自身信息hash决定与哪个主机建立netty长连接。<br/>
5：服务端宕机，客户端自动接入其他健康主机。
***
## Getting Start
### zookeeper部署
- docker部署单机zookeeper

1. 拉取zk镜像&emsp;&emsp;&emsp;指令：docker pull zookeeper:3.4.14
2. 查看镜像id&emsp;&emsp;&emsp;指令：docker images
3. 拉起容器&emsp;&emsp;&emsp;&emsp;指令：docker run -d -p 2181:2181 --name b-zookeeper --restart always {imageId}
4. 查看容器id&emsp;&emsp;&emsp;指令：docker ps -a
5. 进入容器&emsp;&emsp;&emsp;&emsp;指令：docker exec -it {containerId} /bin/bash
6. 起注册中心&emsp;&emsp;&emsp;指令：./bin/zkCli.sh

### 客户端启动
v1: 注册中心ip:port 字符串 (zk集群则字符串之间用","连接即可)<br/>
v2: clientName 字符串<br/>
com.polyu.gcs.client.Client.start(v1, v2)<br/>
### 服务端启动
v1: 服务端ip:port 字符串<br/>
v2: 注册中心ip:port 字符串<br/>
com.polyu.gcs.server.Server.start(v1, v2)<br/>
