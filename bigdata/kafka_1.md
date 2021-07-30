## kafka的几个重要概念

- `Broker`：消息中间件处理节点，一个 Kafka 节点就是一个 broker，多个 broker 可以组成一个 Kafka 集群；
- `Topic`：一类消息，例如 note impression 日志、 click 日志等都可以以 topic 的形式存在，Kafka 集群能够同时负责多个 topic 的分发；
- `Partition`：topic 物理上的分组，一个 topic 可以分为多个 partition，每个 partition 是一个有序的队；
- `segment`：每个 partition 又由多个segment file组成；
- `offset`：每个 partition 都由一系列有序的、不可变的消息组成，这些消息被连续的追加到 partition 中。partition 中的每个消息都有一个连续的序列号叫做 offset，用于 partition 唯一标识一条消息；
- `message`：这个算是 kafka 文件中最小的存储单位，即是 one commit log。

kafka 的 message 是以 topic 为基本单位，不同 topic 之间是相互独立的。
每个 topic 又可分为几个不同的 partition，每个 partition 存储一部分 message。
同一个 topic 的不同 partition 可以分布在不同的 broker 上，这正是分布式的设计。
partition 是以文件夹的形式存储在 broker 机器上。
topic 与 partition 的关系如下：
<img src = "https://github.com/joyang1/tommy.github.io/blob/gh-pages/img/bigdata/kafka/topic.png">

## Partition中的文件
### segment file
对于一个 partition，里面又有很多大小相等的 segment 数据文件（这个文件的具体大小可以在config/server.properties中进行设置），这种特性可以方便 old segment file的快速删除。

下面介绍一下 partition 中的 segment file的组成：
1. segment file 组成：由2部分组成，分别为 index file和 data file，这两个文件是一一对应的，后缀”.index”和”.log”分别表示索引文件和数据文件；数值最大为64位 long 大小，19位数字字符长度，没有数字用0填充；
2. 自0.10.0.1开始的kafka segment file组成：多了一部分，还有 .timeindex 索引文件，基于时间的索引文件；目前支持的时间戳类型有两种： CreateTime 和 LogAppendTime 前者表示 producer 创建这条消息的时间；后者表示 broker 接收到这条消息的时间(严格来说，是 leader broker 将这条消息写入到 log 的时间)
3. segment file 命名规则：partition 的第一个 segment 从0开始，后续每个 segment 文件名为上一个 segment 文件最后一条消息的  offset, ofsset 的数值最大为64位（long 类型），20位数字字符长度，没有数字用0填充。如下图所示：
老版本 segment file 结构: 
<img src = "https://blog.tommyyang.cn/img/bigdata/kafka/segment.png">
新版本 segment file 结构:
<img src = "https://blog.tommyyang.cn/img/bigdata/kafka/new_segment.png">

4. 关于 segment file 中 index 索引文件与 data 文件对应关系图，这里我们借用网上的一个图片，如下所示：
<img src = "https://blog.tommyyang.cn/img/bigdata/kafka/index.png">

5. segment的索引文件中存储着大量的元数据，数据文件中存储着大量消息，索引文件中的元数据指向对应数据文件中的 message 的物理偏移地址。以索引文件中的6，1407为例，在数据文件中表示第6个 message（在全局 partition 表示第368775个 message），以及该消息的物理偏移地址为1407。

6. Kafka message 结构如下图:
<img src = "https://blog.tommyyang.cn/img/bigdata/kafka/message.png">

`注`：.index文件的第一个数是 message 相对 offset，后面的数字是该消息的物理偏移地址。


### 为什么分成多个segment file
1. 问题: 如果 topic 的 partition 中只有一个文件会怎么样？
- 新数据加在文件的末尾(调用内部方法)，不论文件多大，该操作的时间复杂度都是O(1)；这个没问题。
- 查找某个 offset 的时候，是顺序查找。想想，如果文件很大的话，查找的效率就会很低；这个是要解决的。

2. Kakfa 是如何解决上述问题的呢
通过上述分析发现，如果 Kafka 只有一个文件的话，插入新数据的效率是没问题的；只是在查找的时候，效率很低。

`解决办法`

`方法一` 数据文件分段

将大文件分成多个小的文件，便于维护。 由 offset 的起始位置来命名，则通过二分查找可以确定一个待查找的 offset
属于在那个小文件里面。

`方法二` 为数据文件建立索引文件

即为上图中可以看到的 .index 文件， 数据分段虽然解决了一个大文件的问题，但是找到具体的小文件后，还是要通过
顺序扫描的方式才能找到对应 offset 的 message。 为了提高顺序扫描的效率，kafka 为每个分段后的文件建立了一个
与数据文件名一样的，扩展名为 .index 的文件。

**索引文件的内部结构**

索引文件包含两个部分（均为4个字节），分别为 offset 和 position。

- 相对 offset: 由于数据文件分段以后，除了第一个数据以外，每个数据文件的起始 offset 不为0，相对 offset 表示
该 message 相对于其所属数据文件中最小的 offset 的大小，即在每个文件中都是从1开始。 然后查找具体 offset 的时候，
只需要通过二分查找找到具体的在哪个文件中，然后再用 offset - 文件名对应的数值， 即可确定在文件中的相对 offset。
存储相对 offset 的好处是*可以减小索引文件占用的空间*。

- position: 表示该条 message 在数据文件中的绝对位置， 只要打开数据文件并移动文件指针到这个 position 就可以
读取到对应的 message 数据了。

3. 通过 offset 查找 message
分为两个步骤：

- 通过`二分查找`文件列表，快速定位到具体的.index和.log文件； 即找到小于或等于给定 offset 的最大的 offset 文件。

- 通过 segment file 查找具体的 message，找到具体的文件后，先定位到 .index 文件中的 元数据物理位置 position，
即 .log 文件中的物理偏移地址。 然后在通过顺序查找到具体的 offset。

上述最后还需要顺序查找具体的 offset， 而不是直接通过 position 定位到具体的 message，是因为 segment index file
没有为数据文件中的每条 message 建立索引，而是采用`稀疏索引`的存储方式，每隔一定字节的数据建立一条索引，这样可以减小
索引文件，通过 map 可以直接进行内存操作， 稀疏索引为数据文件的每个对应 message 设置一个元数据指针， 这样比稠密索引
节省了存储空间，但是查找时需要消耗更多的时间，其实最后通过 .index 文件中 position 无法直接定位到 没有建立索引
的 message， 而是需要通过顺序查找，再继续在 .log 文件中继续往下查找。

### 总结

Kafka 高效文件存储设计特点：

- 将 topic 中一个 partition 大文件分割成多个小文件段，这样更容易定期清除和删除已经消费完的文件，减少磁盘占用。
- 通过索引可以快速定位到 message 和 确定 response 的最大大小。
- 通过 index 元数据全部映射到 memory，可以避免 segment file的 IO 磁盘操作。
- 通过索引文件稀疏存储，可以大幅降低 index 文件元数据占用空间大小。 


### 参考

- [kafka-fs-design-theory](https://tech.meituan.com/2015/01/13/kafka-fs-design-theory.html)
- [Kafka 存储](http://matt33.com/2016/03/08/kafka-store/) 


