# Hadoop 架构

## Hadoop 组成
- HDFS
- MapReduce
- Yarn (2.0 版本引入)

## HDFS

## MapReduce
MapReduce是一种用于大规模数据处理的编程模型和计算框架。它被广泛应用于分布式系统中，旨在提供高效、可扩展和可靠的数据处理解决方案。

MapReduce的核心思想是将大规模数据集分成小的数据块，然后并行处理这些数据块。这个过程包括两个主要阶段：Map阶段和Reduce阶段。

在Map阶段，数据集被划分成多个小的输入块，每个输入块由一个Map任务处理。Map任务将输入块转换成键值对的集合。键值对的生成是通过应用用户自定义的Map函数来实现的。这个函数将输入数据转换成一个或多个键值对，其中键表示数据的特定属性，值则包含与该键相关联的信息。

在Reduce阶段，Map任务生成的键值对集合被分组和排序，然后传递给Reduce任务进行处理。Reduce任务的数量通常与计算集群中的处理节点数量相匹配。Reduce任务将相同键的键值对进行合并、计算和聚合操作，生成最终的输出结果。Reduce函数也是用户自定义的，根据需要执行各种操作，例如求和、计数、平均值等。

MapReduce的优点在于它的并行性和可伸缩性。通过将数据划分成小块并将其分发给多个处理节点进行并行处理，MapReduce可以有效地处理大规模数据集，提高处理速度和性能。此外，它提供了自动处理故障和容错的机制，保证了计算的可靠性。

MapReduce在分布式系统中得到了广泛应用。它是Apache Hadoop框架的核心组件之一，被用于处理大数据和进行复杂的数据分析任务。MapReduce还可以在其他领域中发挥作用，如搜索引擎、机器学习和图形处理等。

总而言之，MapReduce是一种强大的编程模型和计算框架，通过将大规模数据集划分成小块并在分布式环境中并行处理，提供了高效、可扩展和可靠的数据处理解决方案。它在大数据领域的应用前景广阔，并在多个领域中发挥着重要的作用。

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
