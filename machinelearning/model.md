# 相关模型介绍
## 分类模型

## 决策树
决策树（Decision Tree）算法是一种基本的分类与回归方法，是最经常使用的数据挖掘算法之一。我们这章节只讨论用于分类的决策树。

决策树模型呈树形结构，在分类问题中，表示基于特征对实例进行分类的过程。它可以认为是 if-then 规则的集合，也可以认为是定义在特征空间与类空间上的条件概率分布。

决策树学习通常包括 3 个步骤: 特征选择、决策树的生成和决策树的修剪。

### GBDT
GBDT: Gradient Boost Decision Tree。DT－Decision Tree决策树，GB是Gradient Boosting，是一种学习策略，GBDT的含义就是用Gradient Boosting的策略训练出来的DT模型。可以处理二分类问题。
