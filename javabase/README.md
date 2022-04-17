# Java 基础知识

## 基础
### Calendar类
添加天数：<font color="red">DAY_OF_MONTH、DAY_OF_YEAR、DAY_OF_WEEK、DAY_OF_WEEK_IN_MONTH</font>的区别

就单纯的add操作结果都一样，因为都是将日期+1, 不管是月的日期中加1还是年的日期中加1。
强行解释区别如下：
`DAY_OF_MONTH` 的主要作用是get(DAY_OF_MONTH)，用来获得这一天在是这个月的第多少天。
`DAY_OF_YEAR` 的主要作用get(DAY_OF_YEAR)，用来获得这一天在是这个年的第多少天。
同样，`DAY_OF_WEEK`，用来获得当前日期是一周的第几天；`DAY_OF_WEEK_IN_MONTH`，用来获取 day 所在的周是这个月的第几周


## [Java8 篇](/java8)

## [异常篇](exception.md)

