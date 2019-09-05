# Hive 函数
相信大家对 Hive 都不陌生，那么大家肯定用过 Hive 里面各种各样的函数。
可能大家都会使用这些函数，但是没有自己动手去写过 Hive 里面的函数。
下面主要来介绍一下 Hive 里面的各种函数。


## 依赖
开发 Hive UDF 之前，我们需要引入一个 jar，这个 jar 就是 hive-exec，里面定义了各种我们自定义的 UDF 函数的
类型：UDF、 UDAF、 UDTF、 GenericUDF 等。

```xml

 <dependency>
    <groupId>org.apache.hive</groupId>
    <artifactId>hive-exec</artifactId>
    <version>2.1.1</version>
 </dependency>

```


## UDF(User-Defined-Function)
UDF：用户自定义函数，也是实现起来最简单的一种函数。
支持的就是**一进一出**类型的函数。

如何实现？

- 继承 UDF 类。

- 重写 evaluate 方法。

- 将该 java 文件打包成 jar。

- 在 beeline（hive 的一种终端）中输入如下命令：
    
    - 0: jdbc:hive2://localhost:10000> add jar /data/tommyyang/HiveUDF.jar;
    
    - 0: jdbc:hive2://localhost:10000> create temporary function ip2loc as 'cn.tommyyang.IPToLocation';
    
    - 0: jdbc:hive2://localhost:10000> select ip2loc("118.28.1.1");
    
    - 0: jdbc:hive2://localhost:10000> drop temporary function ip2loc;

具体代码实现：

```java

@Description(
        name = "ip2loc",
        value = "_FUNC_(str) - covert ip to location",
        extended = "Example:\n" +
                "  > SELECT ip2loc(ip) FROM ips;\n" +
                "  [中国,上海]"
)
public class IPToLocation extends UDF {
    
    private static final InputStream stream = IPToLocation.class.getClassLoader().getResourceAsStream("ipipfree.ipdb");
    private static City db = null;

    static {
        try {
            db = new City(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> evaluate(Text s) throws IOException, IPFormatException {
        ArrayList<String> allTexts = new ArrayList<>();
        if (s != null) {
            CityInfo info = db.findInfo(s.toString(), "CN");
            allTexts.add(info.getCountryName());
            allTexts.add(info.getRegionName());
            allTexts.add(info.getCityName());
        }
        return allTexts;
    }

}

```

@Description 为该 UDF 的描述，你可以通过命令 **desc function ip2loc**查看具体的描述；如下图：
<img src="https://blog.tommyyang.cn/img/bigdata/hive/hive-udf-1.png">

## UDAF(User-Defined-Aggregation-Function)

## UDTF(User-Defined-Table-Generating-Function)
