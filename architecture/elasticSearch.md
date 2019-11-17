# ElasticSearch

ElasticSearch 技术相关介绍。

`目录`
[too many dynamic script rejected](#Too Many Dynamic Script Rejected)

## Too Many Dynamic Script Rejected

最近工程中会使用到ElasticSearch(以下统称ES),就是将一些统计结果(点击量:click_count,曝光量:impr_count,点击曝光比:ctr=click_count/impr_count)写入到ES,会用到ES的dynamic script去实时修改ctr。然后就遇到了too many dynamic script rejected的问题。

### 问题解决过程
#### 获取EsClient的源码

``` java
public static synchronized TransportClient getInstance() throws UnknownHostException {
        if(transportClient == null){
            String clusterName = DataSourceUtils.getProperteValue("es.cluster", "es");
            String host = DataSourceUtils.getProperteValue("es.host","localhost");
            Integer port = Integer.parseInt(DataSourceUtils.getProperteValue("es.port","9300"));
            Settings settings = Settings.builder()
                    .put("cluster.name", clusterName)
                    .put("client.transport.sniff", true)
                    .build();
            transportClient = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
        }
        return transportClient;
    }
```

#### 贴上有问题的源码(dynamic script update ctr)

``` java
TransportClient esClient = EsClient.getInstance();
UpdateRequest updateRequest = new UpdateRequest("notes2", "note", "2");
String script = String.format("ctx._source.click_count=%d;ctx._source.impr_count=%d;ctx._source.ctr=(double)ctx._source.click_count/ctx._source.impr_count", 15, 120);
updateRequest.script(new Script(script));
updateRequest.retryOnConflict(3);
esClient.update(updateRequest).get();
```

用以上code去update ctr, 然后发现log中出现了too many dynamic script rejected,导致ctr更新失败。

#### 寻找原因

在ES官网上查找Script相关的文档说明,在Script Parameters栏目发现以下参数

`lang`

Specifies the language the script is written in. Defaults to painless.
指定编写脚本的语言,默认为painless。

`source, id`

Specifies the source of the script. An inline script is specified source. A stored script is specified id and is retrieved from the cluster state.
指定脚本的来源。inline类型的脚本是指定source。stored类型的脚本是指定source的id,通过id从ES集群上检索对应的source。

`params`

Specifies any named parameters that are passed into the script as variables.
指定作为变量传递到脚本的任何参数。

然后在这个下面的一段话,很重要，就是解决我们问题的关键信息

The first time Elasticsearch sees a new script, it compiles it and stores the compiled version in a cache. Compilation can be a heavy process.

If you need to pass variables into the script, you should pass them in as named params instead of hard-coding values into the script itself. For example, if you want to be able to multiply a field value by different multipliers, don’t hard-code the multiplier into the script:
> "source": "doc['my_field'] * 2"

Instead, pass it in as a named parameter:

> "source": "doc['my_field'] * multiplier",
  "params": {
    "multiplier": 2
  }

通过以上code我们可以得知 对于dynamic script我们应该使用params参数来传递参数,而不是把参数拼接在script中,这样就不需要ES就会在第一次的时候把script编译后保存在缓存中,而不是每次都会去编译。

`方法一` 使用inline type script

``` java
TransportClient esClient = EsClient.getInstance();
UpdateRequest updateRequest = new UpdateRequest("notes2", "note", "2");
Map<String, Object> params = new HashMap() {
    {
        put("click_count", 120);
        put("impr_count", 5);
    }
};

String code = "ctx._source.click_count=params.click_count;ctx._source.impr_count=params.impr_count;ctx._source.ctr=(double)ctx._source.click_count/ctx._source.impr_count*100";
Script script = new Script(ScriptType.INLINE, "painless", code, params);
updateRequest.script(script);
esClient.update(updateRequest).get();
```

`方法二` 使用stored type script

``` java
TransportClient esClient = EsClient.getInstance();
UpdateRequest updateRequest = new UpdateRequest("notes2", "note", "2");
Map<String, Object> params = new HashMap() {
    {
        put("click_count", 120);
        put("impr_count", 5);
    }
};

//String code = "ctx._source.read_time=params.read_time;ctx._source.read_num=params.read_num;ctx._source.avg_read_time=(double)ctx._source.read_time/ctx._source.read_num";
Script script = new Script(ScriptType.STORED, "painless", "ctr_calc", params);
updateRequest.script(script);
esClient.update(updateRequest).get();
```

使用方法二stored script的时候 需要在kibana里面将script存储到ES集群里面

`方法如下`

``` java
POST _scripts/ctr_calc
{
  "script": {
    "lang": "painless",
    "source": "ctx._source.click_count=params.click_count;ctx._source.impr_count=params.impr_count;ctx._source.ctr=(double)ctx._source.click_count/ctx._source.impr_count*100"
  }
}
```

#### 结果

通过以上两种方法,都可以解决too many dynamic script rejected的问题。
