# MySQL
MySQL 是我们日常开发中用到的最多的关系型数据库，我们需要多了解其底层以及 MySQL 数据库相关锁类型的实现。

>**目录**
- [查询优化](#查询优化)

    - [优化之 EXPLAIN](#优化之EXPLAIN)
    
- [锁总结](#锁总结)

## 查询优化
### 优化之EXPLAIN
使用 EXPLAIN 可以帮助分析自己写的 SQL 语句，看看我们是否用到了索引。

#### 具体用法

```
mysql> EXPLAIN SELECT * FROM tbl_name WHERE FALSE\G

执行结果：

id: 1
select_type: SIMPLE
table: tbl_name
partitions: NULL
type: ALL
possible_keys: NULL
key: NULL
key_len: NULL
ref: NULL
rows: 1693
filtered: 19.00
Extra: Using where

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
    
- ref
    
    此类型通常出现在多表的 join 查询，针对于非唯一或非主键索引，或者是使用了**最左前缀**规则索引的查询。
    
- range

    表示使用索引范围查询，通过索引字段范围获取表中部分数据记录，这个类型通常出现在 =、 <>、 >、 >=、 <、 <=、 IS NULL、 <=>、 BETWEEN、 IN 操作中。
    当 type 是 range 时，那么 EXPLAIN 输出的 ref 字段为 NULL, 并且 key_len 字段是此次查询中使用到的索引的最长的那个。
    
- index
    
    表示全索引扫描(full index scan)，和 ALL 类型类似，只不过 ALL 类型是全表扫描，而 index 类型则仅仅扫描所有的索引，而不扫描数据。
    index 类型通常出现在：所要查询的数据直接在索引树中就可以获取到，而不需要扫描数据。当是这种情况时，Extra 字段 会显示 Using index。
    
- ALL

    表示全表扫描，这个类型的查询是性能最差的查询之一。通常来说，我们的查询不应该出现 ALL 类型的查询，因为这样的查询在数据量大的情况下，对数据库的性能是巨大的灾难。
    如一个查询是 ALL 类型查询，那么一般来说可以对相应的字段添加索引来避免。

    

## 锁总结
锁是计算机协调多个进程或线程并发访问某一资源的机制。锁保证数据并发访问的一致性、有效性；锁冲突也是影响数据库并发访问性能的一个重要因素。锁是 MySQL 在服务器层和存储引擎层的并发控制。

### 锁机制
#### 共享锁和排它锁

#### 锁粒度

#### 不同粒度锁的比较

### MylSAM 表锁
#### MylSAM 表级锁模式

#### MylSAM 加表锁方法
