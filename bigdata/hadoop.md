# Hadoop 架构

## Hadoop 组成
- HDFS
- MapReduce
- Yarn (2.0 版本引入)

## HDFS

## MapReduce

## Yarn


## 例子
我们在 Hive 执行一个 select 操作需要经历的步骤。
1. 通过 HiveServer2 得到 hdfs 文件目录；
2. 向 yarn 集群提交我们的 Map Reduce 程序； 
3. yarn 集群中的 Resourcemanger 和 nodemanager 进行通信，根据集群情况，分配 Container 给集群的某个 Nodemanager，Nodemanager 启动 Container； 
4. Resource Manager 将 MapReduce Applicationmaster 分发到刚才 Nodemanager 启动的 container，然后自身在 container 中启动。 
5. MapRedeuce ApplicationMaster 启动之后，立马向 Resource manager 注册，并为 MapReduce 申请资源。 
6. MapReduce ApplicationMaster 申请到容器之后立马和 NodeManager 通信，将用户的 MapReduce 程序分发到对应的 Container 中运行，运行的是 Map 进行和 Reduce 进程。 
7. Map 和 Reduce 进程执行期间与 MapReduce ApplicationMaster  进行通信，汇报自身的运行状态，如果运行结束，MapReduce ApplicationMaster ApplicationMaster  会向 ResourceManager 注销并释放所有的容器资源。 
8. 最后返回 Reduce 的所有结果。 