# JavaWeb 中 POJO、BO、VO、DO、DTO、DAO、PO 详细介绍
## PO
**PO**（持久对象）是 Persistent Object 的缩写，用于表示数据库中的一条记录映射成 Java 对象。PO 仅仅用于表示数据，没有任何数据操作。通常遵守 Java Bean 规范，拥有 getter/setter 方法。

## BO
**BO**（业务对象）是 Business Object 的缩写，用于表示一个业务对象，可以进行 PO 与 VO/DTO 之间的转换。BO 通常位于业务层，要区别于直接对外提供服务的服务层；BO 提供了基本业务单元的基本业务操作，在设计上属于被服务层业务流程调用的对象，一个业务流程可能需要调用多个 BO 来完成。也可以理解我们开发中的 Service 对象。

## DO
**DO**（领域对象）是 Domain Object 的缩写。个人觉得可以代替 PO，用于表示数据库对象，

## VO
**VO** 是 Value Object 的缩写，用来表示一个与前端进行交互的 Java 对象。通常拥有 Java Bean 的规范，拥有 getter/setter 方法。

## DTO
**DTO**（数据传输对象） 是 Data Transfer Object 的缩写，用于表示一个数据传输对象。DTO 通常用于不同服务或服务不同分层之间的数据传输。DTO 与 VO 概念类似，并且通常情况下字段基本一致。但 DTO 与 VO 又有一些不同，这个不同之处主要实在设计理念上，比如 API 服务需要使用的 DTO 就可能与 VO 存在差异。通常拥有 Java Bean 的规范，拥有 getter/setter 方法。

## DAO
**DAO**（数据访问对象） 是 Data Access Object 的缩写，用来表示一个数据访问的对象。使用 DAO 访问数据库，包括 CRUD 等操作，与 PO 一起使用。DAO 一般在持久层，完全封装数据库操作，对外暴露的方法使得上层应用不需要关注数据库相关的任何信息。

## POJO
**POJO** 是 Plain Ordinary Java Object 的缩写。是 PO、DO、VO、DTO 的统称。不会将一个对象命名为以 POJO 结尾。


**注意**：为什么需要定义这些 Object 对象呢？

个人觉得是因为每一层负责每一层的工作，然后也可以减少传输数据量的大小和保护数据库结构不外泄。如果你所有地方都使用 PO 对象，那么前端就很轻易的知道了你的数据库结构，同时有些数据根本不用传到前端的（可以节省传输数据量的大小），比如我们一些数据库里面表示是否删除、更新时间等这些字段，是不需要让前端知道的。