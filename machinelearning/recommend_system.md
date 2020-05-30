# 推荐系统整理
该篇主要对推荐系统进行一些整理。

## 推荐系统涉及到的一些模块
核心模块有用户、Item（包括文章、视频、商品、音乐、电影等）、用户实时画像、Item 特征等。
核心服务有召回服务、推荐服务、排序服务等。

如下图：
![相关模块](https://blog.tommyyang.cn/img/architecture/recommend-module.png)

比较重要就是要去了解 NLP（自然语言处理），基于 NLP 的特征分析。基于模型的排序服务，比如决策树、FM 模型、FFM 模型、双线性 FFM 模型、DNN、Wide&Deep 等。排序模型个人觉得讲的比较好的一篇文章，[推荐排序模型](https://www.infoq.cn/article/vKoKh_ZDXcWRh8fLSsRp)。

## 推荐模型时序图
![时序图](https://blog.tommyyang.cn/img/architecture/recommend-time.png)

- 1.2.3 和 1.2.4 应该是在一步同时异步执行的
- 1.3 获取文章title、content、发布时间、作者等相关信息
- 1.2.3 里面包括 label 画像召回、LDA(Topic) 画像召回、用户与作者亲密度召回(我理解为基于社交关系的召回，这一类文章加上推荐理由效果会更好)

tips: 为什么 Doc2vec 画像单独弄成一个微服务，因为 vector 是一个 300 维的 float 类型的数组，计算量特别大；所以做成 T+1 模式，提前计算好，然后索引到文件中。（使用的是开源的 Annoy 进行文件索引）。

## 实时画像时序图
![实时画像时序图](https://blog.tommyyang.cn/img/architecture/recommend-profile-time.png)

- 流处理框架使用当下比较流行的 Flink。
- Flink 支持批量和流处理两种模式。
- 批量计算时只是对接数据源不一样，数据仓库大多数为 Hive。

## 数据分析
通过之前做过的一些视频、文章推荐举例。

- 多策略召回。
- Rank 模型训练特征由离线转为实时。
- 特征精细化，比如由文章总体 CTR 变为每个策略下的 CTR；不同分类下文章的时效性不一样，所以 CTR 计算周期不一样，比如有的文章在一开始 CTR 特别高，但随着时间变化，慢慢降低，但是相比其它的一些文章，还是很高，这时候，就需要调整 CTR 计算周期。
- 分析重要的特征加入到训练模型，比如对文章来说：CTR、CDR；统计用户在每个分类下的 CTR、CDR，可以更加具体的描述出用户对不同分类的喜欢程度。

## 名词介绍
- CTR：Click-Through-Rate，点击通过率。
- CDR：Completion-reaDing-Rate，阅读完成率。

## 文章推荐
- [从 FFM 到 DeepFFM，推荐排序模型到底哪家强？](https://www.infoq.cn/article/vKoKh_ZDXcWRh8fLSsRp)
- [NLP](https://easyai.tech/ai-definition/nlp/)
- [LDA](https://www.hankcs.com/nlp/lda-java-introduction-and-implementation.html)









