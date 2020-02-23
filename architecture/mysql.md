# MySQL
MySQL 是我们日常开发中用到的最多的关系型数据库，该篇总结 MySQL 的常用知识点。

>**目录**
- [查询优化](#查询优化)

    - [优化之 EXPLAIN](#优化之EXPLAIN)
    - [优化访问](#优化数据访问)
    - [重构查询方式](#重构查询方式)
    
- [MySQL事物隔离级别与锁总结](#MySQL事物隔离级别与锁总结)

- [主从复制](#主从复制)

- [读写分离](#读写分离)

## 查询优化
### 优化之EXPLAIN
使用 EXPLAIN 可以帮助分析自己写的 SQL 语句，看看我们是否用到了索引。

#### 按以下两个 SQL 新建两张表
```sql

CREATE TABLE `demo` (
  `ID` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'demo name',
  `author` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'demo author',
  PRIMARY KEY (`ID`),
  KEY `IX_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `demo_details` (
  `ID` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `demoId` int(11) unsigned NOT NULL COMMENT 'demo id',
  `url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'demo author',
  PRIMARY KEY (`ID`),
  KEY `FIX_demoId_ID` (`demoId`),
  CONSTRAINT `demo_details_ibfk_1` FOREIGN KEY (`demoId`) REFERENCES `demo` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

```

#### 具体用法

```
mysql> EXPLAIN SELECT * FROM demo WHERE ID = 1\G

执行结果：

id: 1
select_type: SIMPLE
table: demo
partitions: NULL
type: const
possible_keys: PRIMARY
key: PRIMARY
key_len: 4
ref: const
rows: 1
filtered: 100.00
Extra: NULL
1 row in set, 1 warning (0.00 sec)

```

以上执行结果各行表示的含义：
- id: SELECT 查询的标识符，每个 SELECT 都会自动分配一个唯一的标识符
- select_type: SELECT 查询的类型
- table: 查询的是哪个表
- partitions: 匹配的分区
- type: join 类型
- possible_keys: 此次查询中可能选用的索引
- key: 此次查询中确切使用到的索引
- ref: 哪个字段或常数与 key 一起被使用
- rows: 显示此查询一共扫描了多少行，这个是一个估计值
- filtered: 表示此查询条件所过滤的数据的百分比
- extra: 额外的信息

#### select_type
`select_type` 表示了查询的类型, 它的常用取值有:
- SIMPLE： 表示此查询不包含 UNION 查询或子查询
- PRIMARY： 表示此查询是最外层的查询
- UNION： 表示此查询是 UNION 的第二或随后的查询
- DEPENDENT UNION： UNION 中的第二个或后面的查询语句, 取决于外面的查询
- UNION RESULT： UNION 的结果
- SUBQUERY： 子查询中的第一个 SELECT
- DEPENDENT SUBQUERY： 子查询中的第一个 SELECT, 取决于外面的查询. 即子查询依赖于外层查询的结果

最常见的应该是 SIMPLE，当我们的查询 SQL 里面没有 UNION 查询或者子查询的时候，那么通常就是 SIMPLE 类型。

#### type
`type` 字段比较重要，它提供了判断查询是否高效的重要依据。通过 type 字段，我们可以判断此次查询是**全表扫描**，还是**索引扫描**等。

`type` 常用取值有：
- system
    
    表中只有一条数据，这个类型是特殊的 const 类型。

- const
    
    针对主键或唯一索引的等值查询扫描，最多只返回一行数据，const 查询速度非常快，因为它仅仅读取一次即可。
    
- eq_ref
    
    此类型通常出现在多表的 join 查询，表示对于前表的每一个结果，都只能匹配到后表的一行结果，并且查询的比较操作通常是 =，查询效率较高。
    demo 如下：
    ```
  
    mysql>EXPLAIN select * from demo,demo_details where demo.ID = demo_details.demoId\G;
    *************************** 1. row ***************************
               id: 1
      select_type: SIMPLE
            table: demo_details
       partitions: NULL
             type: ALL
    possible_keys: FIX_demoId_ID
              key: NULL
          key_len: NULL
              ref: NULL
             rows: 1
         filtered: 100.00
            Extra: NULL
    *************************** 2. row ***************************
               id: 1
      select_type: SIMPLE
            table: demo
       partitions: NULL
             type: eq_ref
    possible_keys: PRIMARY
              key: PRIMARY
          key_len: 4
              ref: springdemo.demo_details.demoId
             rows: 1
         filtered: 100.00
            Extra: NULL
    2 rows in set, 1 warning (0.00 sec)
  
    ```
    
- ref
    
    此类型通常出现在多表的 join 查询，针对于非唯一或非主键索引，或者是使用了**最左前缀**规则索引的查询。
    
    ```
  
    mysql> EXPLAIN select * from demo where name='tommy'\G;
    *************************** 1. row ***************************
               id: 1
      select_type: SIMPLE
            table: demo
       partitions: NULL
             type: ref
    possible_keys: IX_name
              key: IX_name
          key_len: 258
              ref: const
             rows: 1
         filtered: 100.00
            Extra: NULL
    1 row in set, 1 warning (0.00 sec)

    ```
    
- range

    表示使用索引范围查询，通过索引字段范围获取表中部分数据记录，这个类型通常出现在 =、 <>、 >、 >=、 <、 <=、 IS NULL、 <=>、 BETWEEN、 IN 操作中。
    当 type 是 range 时，那么 EXPLAIN 输出的 ref 字段为 NULL, 并且 key_len 字段是此次查询中使用到的索引的最长的那个。
    demo 如下：
    ```
  
    mysql> EXPLAIN select * from demo where id between 1 and 2\G;
    *************************** 1. row ***************************
               id: 1
      select_type: SIMPLE
            table: demo
       partitions: NULL
             type: range
    possible_keys: PRIMARY
              key: PRIMARY
          key_len: 4
              ref: NULL
             rows: 2
         filtered: 100.00
            Extra: Using where
    1 row in set, 1 warning (0.00 sec)
 
    ```
    
- index
    
    表示全索引扫描(full index scan)，和 ALL 类型类似，只不过 ALL 类型是全表扫描，而 index 类型则仅仅扫描所有的索引，而不扫描数据。
    index 类型通常出现在：所要查询的数据直接在索引树中就可以获取到，而不需要扫描数据。当是这种情况时，Extra 字段 会显示 Using index。
    demo 如下：
    ```
  
    mysql> EXPLAIN select name from demo\G;
    *************************** 1. row ***************************
               id: 1
      select_type: SIMPLE
            table: demo
       partitions: NULL
             type: index
    possible_keys: NULL
              key: IX_name
          key_len: 258
              ref: NULL
             rows: 2
         filtered: 100.00
            Extra: Using index
    1 row in set, 1 warning (0.00 sec)

    ```
    
- ALL

    表示全表扫描，这个类型的查询是性能最差的查询之一。通常来说，我们的查询不应该出现 ALL 类型的查询，因为这样的查询在数据量大的情况下，对数据库的性能是巨大的灾难。
    如一个查询是 ALL 类型查询，那么一般来说可以对相应的字段添加索引来避免。
    demo 如下：
    ```
  
    mysql> EXPLAIN select * from demo\G;
    *************************** 1. row ***************************
               id: 1
      select_type: SIMPLE
            table: demo
       partitions: NULL
             type: ALL
    possible_keys: NULL
              key: NULL
          key_len: NULL
              ref: NULL
             rows: 2
         filtered: 100.00
            Extra: NULL
    1 row in set, 1 warning (0.00 sec)

    ```
    
#### type 类型的性能比较
通常来说, 不同的 type 类型的性能关系如下:
`ALL < index < range < index_merge < ref < eq_ref < const < system`

ALL 类型因为是全表扫描，因此在相同的查询条件下，它是速度最慢的。
而 index 类型的查询虽然不是全表扫描，但是它扫描了所有的索引，因此比 ALL 类型的稍快。
后面的几种类型都是利用了索引来查询数据，因此可以过滤部分或大部分数据，因此查询效率就比较高了。

#### possible_keys
`possible_key` 表示 MySQL 在查询时，可能使用到的索引。即使有些索引出现在 possible_key 中，但是并不表示此索引一定会被 MySQL 使用到。MySQL 在查询时具体使用到那些索引，与 key 和你写的 SQL 有关。

#### key
此字段表示 MySQL 在当前查询时所真正会使用到的索引。

#### key_len
表示查询优化器使用了索引的字节数。这个字段可以评估组合索引是否完全被使用，或只有最左部分字段被使用到。
key_len 的计算规则如下：

- 字符串
    - char(n)：n 字节长度
    - varchar(n)：如果是 utf8 编码，则是 3n + 2字节；如果是 utf8mb4 编码，则是 4n + 2 字节。

- 数值类型
    - TINYINT： 1字节
    - SMALLINT： 2字节
    - MEDIUMINT: 3字节
    - INT: 4字节
    - BIGINT: 8字节

- 时间类型
    - DATE：3字节
    - TIMESTAMP：4字节
    - DATETIME：8字节

- 字段属性
    NULL 属性 占用一个字节。如果一个字段是 NOT NULL 的，则没有此属性。
    
#### rows
rows 也是一个重要的字段。MySQL 查询优化器根据统计信息，估算 SQL 要查找到结果集需要扫描读取的数据行数。这个值非常直观显示 SQL 的效率好坏，原则上 rows 越少越好。

#### Extra
EXPLAIN 中的很多额外的信息会在 Extra 字段显示，常见的有以下几种内容：
- Using filesort

    当 Extra 中有 Using filesort 时，表示 MySQL 需额外的排序操作，不能通过索引顺序达到排序效果。一般有 Using filesort，都建议优化去掉，因为这样的查询 CPU 资源消耗大。
    demo 如下：
    ```
    
    mysql> EXPLAIN select * from demo order by name\G;
    *************************** 1. row ***************************
               id: 1
      select_type: SIMPLE
            table: demo
       partitions: NULL
             type: ALL
    possible_keys: NULL
              key: NULL
          key_len: NULL
              ref: NULL
             rows: 2
         filtered: 100.00
            Extra: Using filesort
    1 row in set, 1 warning (0.00 sec)  
  
    ```

- Using index

    **覆盖索引扫描**，表示查询在索引树中就可查找所需数据，不用扫描表数据文件，往往说明性能不错。
    demo 如下：
    ```
    
    mysql> EXPLAIN select * from demo order by id desc\G;
    *************************** 1. row ***************************
               id: 1
      select_type: SIMPLE
            table: demo
       partitions: NULL
             type: index
    possible_keys: NULL
              key: PRIMARY
          key_len: 4
              ref: NULL
             rows: 2
         filtered: 100.00
            Extra: Backward index scan
    1 row in set, 1 warning (0.00 sec)
  
    ```

- Using temporary

    查询有使用临时表，一般出现于排序，分组和多表 join 的情况，查询效率不高，建议优化。
    
- Using where

    列数据是从仅仅使用了索引中的信息而没有读取实际行的表返回的，这发生在对表的全部的请求列都是同一个索引的部分的时候，表示 MySQL 服务器将在存储引擎检索行后再进行过滤。
    
### 优化数据访问
#### 减少返回数据的数量
- 只返回必要的列：最好不要使用 SELECT * 语句。
- 只返回必要的行：使用 LIMIT 语句来限制返回的数据。

#### 减少服务器端扫描的行数
- 最有效的方式是使用索引来覆盖查询。
- 缓存重复查询的数据：使用缓存可以避免在数据库中进行查询，特别在要查询的数据经常被重复查询时，缓存带来的查询性能提升将会是非常明显的。

### 重构查询方式
#### 切分大查询
一个大查询如果一次性执行的话，可能一次锁住很多数据、占满整个事务日志、耗尽系统资源、阻塞很多小的但重要的查询。

```SQL

DELETE FROM messages WHERE create < DATE_SUB(NOW(), INTERVAL 12 MONTH);
rows_affected = 0
do {
    rows_affected = do_query(
    "DELETE FROM messages WHERE create  < DATE_SUB(NOW(), INTERVAL 12 MONTH) LIMIT 5000")
} while rows_affected > 0

```


#### 分解大连接查询
将一个大连接查询分解成对每一个表进行一次单表查询，然后在应用程序中进行关联，这样做的好处有：
- 让缓存更高效。对于连接查询，如果其中一个表发生变化，那么整个查询缓存就无法使用。而分解后的多个查询，即使其中一个表发生变化，对其它表的查询缓存依然可以使用。
- 分解成多个单表查询，这些单表查询的缓存结果更可能被其它查询使用到，从而减少冗余记录的查询。
- 减少锁竞争。（select *** for update）
- 在应用层进行连接，可以更容易对数据库进行拆分，从而更容易做到高性能和可伸缩。

## MySQL事物隔离级别与锁总结
锁是计算机协调多个进程或线程并发访问某一资源的机制。锁保证数据并发访问的一致性、有效性；锁冲突也是影响数据库并发访问性能的一个重要因素。锁是 MySQL 在服务器层和存储引擎层的并发控制。

### MySQL 事物隔离级别
说到 MySQL 的锁，先来了解一下 MySQL 的事物隔离级别。

#### 事务的四个重要特性 --- ACID 特性
- 原子性（Atomicity）
    
    事务开始后所有操作，要么全部做完，要么全部不做，不可能停滞在中间环节。事务执行过程中出错，会回滚到事务开始前的状态，所有的操作就像没有发生一样。
    
- 一致性（Consistency）

    指事务将数据库从一种状态转变为另一种一致的的状态。事务开始前和结束后，数据库的完整性约束没有被破坏。
    
- 隔离性（Isolation）
    
    要求每个读写事务的对象对其他事务的操作对象能互相分离，即该事务提交前对其他事务不可见。也可以理解为多个事务并发访问时，事务之间是隔离的，一个事务不应该影响其它事务运行结果。这指的是在并发环境中，当不同的事务同时操纵相同的数据时，每个事务都有各自完整的数据空间。由并发事务所做的修改必须与任何其他并发事务所做的修改隔离。 **Tips：MySQL 通过锁机制来保证事务的隔离性**。

- 持久性（Durability）
    事务一旦提交，则其结果就是永久性的。即使发生宕机的故障，数据库也能将数据恢复，也就是说事务完成后，事务对数据库的所有更新将被保存到数据库，不能回滚。这只是从事务本身的角度来保证，排除 RDBMS（关系型数据库管理系统，例如 Oracle、MySQL 等）本身发生的故障。**Tips：MySQL 使用 redo log 来保证事务的持久性**。

#### 事务的四种隔离级别
在数据库操作中，为了有效保证并发读取数据的正确性，提出的事务隔离级别。我们的数据库锁，也是为了构建这些隔离级别存在的。

|  隔离级别   |  脏读（Dirty Read） |  不可重复读（NonRepeatable Read） |  幻读（Phantom Read）|
|   :----:   |   :----:   |    :----:    |    :----:    |
| 未提交读（Read Uncommitted） |  可能  |  可能  |  可能  |
| 已提交读（Read Committed） |  不可能  |  可能  |  可能  |
| 可重复读（Repeated Read） |  不可能  |  不可能  |  可能  |
| 可串行化（Serializable） |  不可能  | 不可能  |  不可能  |

- 未提交读（Read Uncommitted）：允许脏读，也就是可能读取到其他会话中未提交事务修改的数据。
- 已提交读（Read Committed）：只能读取到已经提交的数据。Oracle等多数数据库默认都是该级别 (不重复读)。
- 可重复读（Repeated Read）：在同一个事务内的查询都是事务开始时刻一致的，InnoDB 默认级别。在 SQL 标准中，该隔离级别消除了不可重复读，但是还存在幻读。
- 可串行化（Serializable）：完全串行化的读，每次读都需要获得表级共享锁，读写相互都会阻塞。

通过上述描述可以看出，Read Uncommitted 这种级别，数据库一般都不会用，而且任何操作都不会加锁。


### 锁机制
InnoDB 实现了两种类型的行级锁：
**共享锁（也称为 S 锁）**：允许事务读取一行数据。
可以使用 SQL 语句 select * from tableName where … lock in share mode; 手动加 S 锁。

**独占锁（也称为 X 锁）**：允许事务删除或更新一行数据。
可以使用 SQL 语句 select * from tableName where … for update; 手动加 X 锁。

S 锁和 S 锁是兼容的，X 锁和其它锁都不兼容，举个例子，事务 T1 获取了一个行 r1 的 S 锁，另外事务 T2 可以立即获得行 r1 的 S 锁，此时 T1 和 T2 共同获得行 r1 的 S 锁，此种情况称为锁兼容，但是另外一个事务 T2 此时如果想获得行 r1 的 X 锁，则必须等待 T1 对行 r 锁的释放，此种情况也成为锁冲突。

为了实现多粒度的锁机制，InnoDB 还有两种内部使用的意向锁，由 InnoDB 自动添加，且都是表级别的锁。

**意向共享锁（IS）**：事务即将给表中的各个行设置共享锁，事务给数据行加 S 锁前必须获得该表的 IS 锁。
**意向排他锁（IX）**：事务即将给表中的各个行设置排他锁，事务给数据行加 X 锁前必须获得该表 IX 锁。

#### 锁粒度

#### 不同粒度锁的比较

### MylSAM 表锁
#### MylSAM 表级锁模式

#### MylSAM 加表锁方法

### 死锁案例分析
- show variables like 'innodb_deadlock_detect';
- show status like 'table_locks%';
- show status like 'innodb_row_lock%';
- show engine innodb status;

## 主从复制
- 数据分布
- 负载平衡（Load Balancing）
- 备份
- 高可用性（High Availability）和容错

### 相关命令
- show status 查看整个 mysql 状态，这个命令也可以看到 Seconds_Behind_Master。
- show master status 查看主库信息
- show slave status  查看从库信息
    
    查看参数 Seconds_Behind_Master 来看主从是否延迟。 
    - 0：该值为零，是我们极为渴望看到的情况，表示主从复制良好，可以认为lag不存在。
    - 正值：表示主从已经出现延时，数字越大表示从库落后主库越多。
    - 负值：几乎很少见，我只是听一些资深的DBA说见过，其实，这是一个BUG值，该参数是不支持负值的，也就是不应该出现。
    
- show processlist 查看数据库线程列表信息

### 原理

#### 基于语句的复制
在 MySQL5.0 及之前的版本只支持基于语句的复制。基于语句复制的模式下，主库会记录那些造成数据更改的事件，当备库读取并重放这些事件时，备库只是把主库上执行过的 SQL 再执行一遍。

**优点**
- 实现简单。
- 二进制日志里的事件更加的紧凑。（全部是需要执行的 SQL 语句）

**缺点**
- 执行语句的时间不同。（机器的 CPU 和内存可能很不一样）
- 还有一些动态数据，比如 `DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP`，还有一些使用函数的语句，比如包含 `CURRENT_USER` 的语句。
- 更新是串行的。(需要考虑锁带来的性能消耗)

#### 基于行的复制
在 MySQL5.1 开始支持基于行的复制，这种方式会将实际的数据记录到二进制日志中。

**优点**
- 可以正确的复制每一行。（不存在基于语句的复制出现的那种问题）

- 可以更高效地复制数据。（备库不用重放 MySQL 的事件，这个也是针对具体的 SQL，有的 SQL 可以提高效率，有的确会降低效率。）
    ```sql
    insert into tab1 (col1, col2, sum_col3) select col1, col2, sum(col1, col2) from tab2 group by col1, col2; # 基于行的复制只需要把插入结果记录下来
    
    update tab3 set col1 = 0; // 基于行的复制就要在二进制中记录全表的数据
    ```
- 有利于数据的恢复

**缺点**
- 无法判断数据库做了什么，因为不知道执行的 SQL。
- 针对上述全表数据更新的时候，效率会很低。

#### 复制文件
- mysql-bin.index
    
    该文件是 MySQL 用来识别具体的二进制 binlog 文件；该文件记录磁盘上 binlog 文件。
    
- mysql-relay-bin.index

    中继日志的索引文件。跟 mysql-bin.index 作用类似。
    
- master.info
    
    保存备库连接到主库所需要的信息，格式为纯文本。这个文件以文本的方式记录了复制用户的密码。故要注意该文件的权限。
    
- relay-log.info
    
    包含当前备库复制的二进制日志和中继日志的坐标（及备库复制到主库的具体位置）。
    
    
#### 发送复制事件到其它的备库
当设置 log_slave_updates 时，你可以让 slave 扮演其它 slave 的 master。此时，slave 把 SQL 线程执行的事件写进行自己的二进制日志（binary log），然后，它的 slave 可以获取这些事件并执行它。

<img src="https://blog.tommyyang.cn/img/architecture/master-slave.png">

#### 复制过滤器
复制过滤可以让你只复制服务器中的一部分数据，有两种复制过滤：在 master 上过滤二进制日志中的事件；在 slave 上过滤中继日志中的事件。

<img src="https://blog.tommyyang.cn/img/architecture/mysql-filter.png">

## 读写分离
基于主从复制架构，简单来说，就搞一个主库，挂多个从库，然后我们就单单只是写主库，然后主库会自动把数据给同步到从库上去。
读写分离可以提高系统的效率，特别是对于写少读多的系统，使用读写分离可以大大提高系统的效率。这也是从库会有多个的原因，读的时候可以做负载均衡（可以通过主健或者用户 id 等 hash 的方式，也可以使用 Round Robin 轮询算法；负载均衡算法有很多种，这里就不一一列举），让读请求分布到不同的从库上，提高读请求的效率。

## 持久化数据分析

数据InnoDB到磁盘需要经过

- InnoDB buffer pool， Redo log buffer。这个是InnoDB应用系统本身的缓冲。
- page cache /Buffer cache（可通过o_direct绕过）。这个是vfs层的缓冲。
- Inode cache/directory buffer。这个也是vfs层的缓冲。需要通过O_SYNC或者fsync()来刷新。
- Write-Back buffer。(可设置存储控制器参数绕过)
- Disk on-broad buffer。(可通过设置磁盘控制器参数绕过)




