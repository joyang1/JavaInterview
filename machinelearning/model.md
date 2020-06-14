# 相关模型介绍
# 分类模型

## 决策树
决策树（Decision Tree）算法是一种基本的分类与回归方法，是最经常使用的数据挖掘算法之一。我们这章节只讨论用于分类的决策树。

决策树模型呈树形结构，在分类问题中，表示基于特征对实例进行分类的过程。它可以认为是 if-then 规则的集合，也可以认为是定义在特征空间与类空间上的条件概率分布。

决策树学习通常包括 3 个步骤: 特征选择、决策树的生成和决策树的修剪。

### 决策树场景
一个叫做 "二十个问题" 的游戏，游戏的规则很简单: 参与游戏的一方在脑海中想某个事物，其他参与者向他提问，只允许提 20 个问题，问题的答案也只能用对或错回答。问问题的人通过推断分解，逐步缩小待猜测事物的范围，最后得到游戏的答案。

**一个邮件分类系统，大致工作流程如下**:
![邮件分类](https://blog.tommyyang.cn/img/ml/model/dt_email.png)

- 首先检测发送邮件域名地址。如果地址为 myEmployer.com, 则将其放在分类 "无聊时需要阅读的邮件"中。
- 如果邮件不是来自这个域名，则检测邮件内容里是否包含单词 "曲棍球" , 如果包含则将邮件归类到 "需要及时处理的朋友邮件"。 
- 如果不包含则将邮件归类到 "无需阅读的垃圾邮件" 。

**假如你了解足球，让你预测世界杯 32 个球队哪支球队是冠军**：
![预测夺冠](https://blog.tommyyang.cn/img/ml/model/dt_32_champion.png)

- 在 1-16 么？在
- 在 9-16 么？在
- 在 13-16 么？ 在
- 在 15 -16 么？ 在
- 是 15 么？ 不是

答案：16 号球队夺冠。

`tips`
最多通过 5 次机会就猜测出哪只球队是冠军球队。这个 5 代表的是 5 bit(比特)，相当于是 2^5=32，通过 5 bit 可以描述出这个信息量。
如果是 64 支球队，那么需要 6 bit 来描述这个信息量。

### 信息熵(香农熵)
香农通过世界杯预测冠军的问题提出了信息熵，故信息熵也叫香农熵，信息熵用来度量信息量；信息量的度量等于不确定性的多少。
通过上述问题描述，相信读者已经了解了信息量的比特数和所有可能情况的对数函数 log 有关。（log32 = 5, log64 = 6）。

当然，如果是你对足球比较了解，通过历届球队的实力比较了解，以及世界杯参赛球队球星也比较了解。你可能会对一些球队能力的强弱进行分组，
因为你了解的信息量会多一些，所以猜测的不确定性就会小一些，从而你可能不需要 5 次就可以猜中。故香农提出了如下公式来计算信息熵：
H(x) = -(P1 * logP1 + P2 * logP2 + P3 * logP3 + ... + P32 * logP32)，
其中P1、P2、P3...P32 分别代表 32 支球队夺冠的概率。

### 信息增益
信息增益 = 信息熵 - 条件熵。

条件熵：在你得知一个信息(条件)后，得出结论还需要多少信息量。

信息增益越大，说明你得到的这个信息就越重要。

### 决策树定义
分类决策树模型是一种描述对实例进行分类的树形结构。决策树由结点（node）和有向边（directed edge）组成。结点有两种类型: 内部结点（internal node）和叶结点（leaf node）。内部结点表示一个特征或属性(features)，叶结点表示一个类(labels)。

用决策树对需要测试的实例进行分类: 从根节点开始，对实例的某一特征进行测试，根据测试结果，将实例分配到其子结点；这时，每一个子结点对应着该特征的一个取值。如此递归地对实例进行测试并分配，直至达到叶结点。最后将实例分配到叶结点的类中。

### 决策树工作原理
如何构造一个决策树?

使用 create_branch() 方法，伪代码如下所示:
```

def createBranch():
'''
此处运用了迭代的思想。 感兴趣可以搜索 迭代 recursion， 甚至是 dynamic programing。
'''
    检测数据集中的所有数据的分类标签是否相同:
        If so return 类标签
        Else:
            寻找划分数据集的最好特征（划分之后信息熵最小，也就是信息增益最大的特征）
            划分数据集
            创建分支节点
                for 每个划分的子集
                    调用函数 createBranch （创建分支的函数）并增加返回结果到分支节点中
            return 分支节点

```

### 决策树开发流程
- 收集数据: 可以使用任何方法。
- 准备数据: 树构造算法 (这里使用的是ID3算法，只适用于标称型数据，这就是为什么数值型数据必须离散化。 还有其他的树构造算法，比如CART)。
- 分析数据: 可以使用任何方法，构造树完成之后，我们应该检查图形是否符合预期。
- 训练算法: 构造树的数据结构。
- 测试算法: 使用训练好的树计算错误率。
- 使用算法: 此步骤可以适用于任何监督学习任务，而使用决策树可以更好地理解数据的内在含义。

### 决策树算法特点
- 优点: 计算复杂度不高，输出结果易于理解，数据有缺失也能跑，可以处理不相关特征。
- 缺点: 容易过拟合。
- 适用数据类型: 数值型和标称型。

### 决策树案例分析
#### 实例一 鱼类非鱼类问题
决策树如下：
![预测夺冠](https://blog.tommyyang.cn/img/ml/model/dt_fish.png)

##### 项目概述
根据以下 2 个特征，将动物分成两类: 鱼类和非鱼类。

特征:
- 不浮出水面是否可以生存。
- 是否有脚蹼(du 第三声)。

##### 构造数据集
``` python
# 构造数据集
def create_data_set():
    """
    :return: data_set 数据集, labels 标签数组
    """
    data_set = [
        [1, 1, 'yes'],
        [1, 1, 'yes'],
        [1, 0, 'no'],
        [0, 1, 'no'],
        [0, 1, 'no'],
        [0, 0, 'no']
    ]

    labels = ['no surfacing', 'flippers']

    return data_set, labels
```

##### 计算香农熵(经验熵)shannonEnt
```python

# 计算香农熵(经验熵)shannonEnt
def calc_shannon_ent(data_set):
    """

    :param data_set: 数据集
    :return: shannon_ent 香农熵

    """

    # 计算 list 的长度，表示计算参与训练的数据量
    num_entries = len(data_set)
    # 计算分类标签 label 出现的次数
    label_counts = {}

    for feat_vec in data_set:
        # 存储当前实例的标签存储，即每一行数据的最后一个数据代表的是标签
        current_label = feat_vec[-1]
        # 为所有可能的分类创建字典，如果当前值不存在，则扩展字典并将当前值加入字典。每个键值都记录了当前类别出现的次数。
        if current_label not in label_counts.keys():
            label_counts[current_label] = 0
        label_counts[current_label] += 1

    # 对于 label 标签的占比，求出 label 标签的香农熵
    shannon_ent = 0.0
    for key in label_counts:
        # 使用所有类标签的发生频率计算类别出现的概率
        prob = float(label_counts[key]) / num_entries
        # 计算香农熵
        shannon_ent -= prob * log(prob, 2)

    return shannon_ent

```

##### 将指定特征值等于 value 的行剩下列作为子数据集
``` python
# 将指定特征值等于 value 的行剩下列作为子数据集
def split_data_set(data_set, index, value):
    """
    (通过遍历 data_set 数据集，求出 index 对应的 column 列的值为 value 的行)
    就是依据 index 列进行分类，如果index列的数据等于 value 的时候，就要将 index 划分到我们创建的新的数据集中

    :param data_set: 数据集                 待划分的数据集
    :param index: 每一行的 index 列          划分数据集的特征
    :param value: index 列对应的 value 值    需要返回的特征的值
    :return:
        ret_data_set 指定特征值等于 value 的行剩下列(不包含值等于该 value 的列)作为子数据集
    """
    ret_data_set = []
    for feat_vec in data_set:
        # index 列为 value 的数据集[该数据集需要排除 index 列]
        # 判断 index 列的值是否为 value
        if feat_vec[index] == value:
            # chop out index used for splitting
            # [:index]表示前 index 行，即若 index 为 2，就是取 feat_vec 的前 2 行
            reduced_feat_vec = feat_vec[:index]
            '''
            请百度查询一下:  extend和append的区别
            music_media.append(object) 向列表中添加一个对象object
            music_media.extend(sequence) 把一个序列seq的内容添加到列表中 (跟 += 在list运用类似， music_media += sequence)
            1、使用append的时候，是将object看作一个对象，整体打包添加到music_media对象中。
            2、使用extend的时候，是将sequence看作一个序列，将这个序列和music_media序列合并，并放在其后面。
            music_media = []
            music_media.extend([1,2,3])
            print music_media
            #结果: 
            #[1, 2, 3]

            music_media.append([4,5,6])
            print music_media
            #结果: 
            #[1, 2, 3, [4, 5, 6]]

            music_media.extend([7,8,9])
            print music_media
            #结果: 
            #[1, 2, 3, [4, 5, 6], 7, 8, 9]
            '''
            # 取 index+1 行开始，后面的所有行数据
            reduced_feat_vec.extend(feat_vec[index + 1:])

            # 收集结果值 index 列为 value 的行[该行需要排除 index 列]
            ret_data_set.append(reduced_feat_vec)
    return ret_data_set
```

##### 选择信息增益最大的特征列
```python

def choose_best_feature_to_split(data_set):
    """
    :param data_set: 数据集
    :return:
        best_feature 最优的特征列
    """
    # 求第一行有多少列的 Feature, 最后一列是 label 列
    num_features = len(data_set[0]) - 1
    # 数据集的原始信息熵
    base_entropy = calc_shannon_ent(data_set)
    # 最优的信息增益值, 和最优的 feature 编号
    best_info_gain, best_feature = 0.0, -1

    # iterate over all the features
    for i in range(num_features):
        # create a list of all the examples of this feature
        # 获取对应的 feature 下的所有数据
        feat_list = [example[i] for example in data_set]
        # get a set of unique values
        # 获取剔重后的集合，使用set对list数据进行去重
        unique_vals = set(feat_list)
        print("unique_vals:", unique_vals)
        # 创建一个临时的信息熵
        new_entropy = 0.0
        # 遍历某一列的 value 集合，计算该列的信息熵
        # 遍历当前特征中的所有唯一属性值，对每个唯一属性值划分一次数据集，计算数据集的新熵值，并对所有唯一特征值得到的熵求和。
        for val in unique_vals:
            sub_data_set = split_data_set(data_set, i, val)
            # 计算概率
            prob = len(sub_data_set) / float(len(data_set))
            print("prob:", prob)
            # 计算信息熵
            new_entropy += prob * calc_shannon_ent(sub_data_set)
            print("new_entropy:", new_entropy)
        # gain[信息增益]: 划分数据集前后的信息变化， 获取信息熵最大的值
        # 信息增益是熵的减少或者是数据无序度的减少。最后，比较所有特征中的信息增益，返回最好特征划分的索引值。
        info_gain: float = base_entropy - new_entropy
        print('info_gain=', info_gain, 'best_feature=', i, base_entropy, new_entropy)
        if info_gain > best_info_gain:
            best_info_gain = info_gain
            best_feature = i
    return best_feature

```

##### 构建决策树
``` python

def create_tree(data_set, labels):
    """
    :param data_set: 数据集
    :param labels: label 标签
    :return: 返回决策树
    """
    class_list = [example[-1] for example in data_set]
    # 如果数据集的最后一列的第一个值出现的次数=整个集合的数量，也就说只有一个类别，就只直接返回结果就行
    # 第一个停止条件: 所有的类标签完全相同，则直接返回该类标签。
    # count() 函数是统计括号中的值在list中出现的次数
    if class_list.count(class_list[0]) == len(class_list):
        return class_list[0]
    # 如果数据集只有 1 列，那么最初出现 label 次数最多的一类，作为结果
    # 第二个停止条件: 使用完了所有特征，仍然不能将数据集划分成仅包含唯一类别的分组。
    if len(data_set[0]) == 1:
        return majority_cnt(class_list)
    # 选择最优的列，得到最优列对应的label含义
    best_feat = choose_best_feature_to_split(data_set)
    # 获取label的名称
    best_feat_label = labels[best_feat]
    # 初始化myTree
    my_tree = {best_feat_label: {}}
    # 注: labels列表是可变对象，在PYTHON函数中作为参数时传址引用，能够被全局修改
    # 所以这行代码导致函数外的同名变量被删除了元素，造成例句无法执行，提示'no surfacing' is not in list
    del (labels[best_feat])
    # 取出最优列，然后它的branch做分类
    feat_vals = [example[best_feat] for example in data_set]
    unique_vals = set(feat_vals)
    for val in unique_vals:
        # 求出剩余的标签label
        sub_labels = labels[:]
        # 遍历当前选择特征包含的所有属性值，在每个数据集划分上递归调用函数create_tree()
        my_tree[best_feat_label][val] = create_tree(split_data_set(data_set, best_feat, val), sub_labels)
        # print('myTree', val, my_tree)
    return my_tree

```

##### 通过输入的特征，预测分类
``` python

def classify(input_tree, feat_labels, test_vec):
    """
    给输入的节点，进行分类
    :param input_tree: 决策树模型
    :param feat_labels: feature标签对应的名称
    :param test_vec: 测试输入的数据
    :return:
        class_label 分类的结果值，需要映射 label 才能知道名称
    """
    # 获取 tree 的根节点对于的 key 值
    first_str = list(input_tree.keys())[0]
    # 通过key得到根节点对应的value
    second_dict = input_tree[first_str]
    # 判断根节点名称获取根节点在label中的先后顺序，这样就知道输入的testVec怎么开始对照树来做分类
    feat_index = feat_labels.index(first_str)
    # 测试数据，找到根节点对应的label位置，也就知道从输入的数据的第几位来开始分类
    key = test_vec[feat_index]
    feat_val = second_dict[key]
    print('+++', first_str, 'xxx', second_dict, '---', key, '>>>', feat_val)
    # 判断分枝是否结束: 判断 feat_val 是否是 dict 类型
    if isinstance(feat_val, dict):
        class_label = classify(feat_val, feat_labels, test_vec)
    else:
        class_label = feat_val
    return class_label

```

##### 选择出现次数最多的一个结果
``` python

def majority_cnt(class_list):
    """
    选择出现次数最多的一个结果
    :param class_list: label 列的集合
    :return: best_feature 最优的特征列
    """
    # -----------majorityCnt的第一种方式 start------------------------------------
    class_count = {}
    for vote in class_list:
        if vote not in class_count.keys():
            class_count[vote] = 0
        class_count[vote] += 1
    # 倒叙排列classCount得到一个字典集合，然后取出第一个就是结果（yes/no），即出现次数最多的结果
    sorted_class_count = sorted(class_count.iteritems(), key=operator.itemgetter(1), reverse=True)
    print('sortedClassCount:', sorted_class_count)
    return sorted_class_count[0][0]
    # -----------majorityCnt的第一种方式 end------------------------------------

    # # -----------majorityCnt的第二种方式 start------------------------------------
    # major_label = Counter(classList).most_common(1)[0]
    # return major_label
    # # -----------majorityCnt的第二种方式 end------------------------------------

```



### GBDT
GBDT: Gradient Boost Decision Tree。DT－Decision Tree决策树，GB是Gradient Boosting，是一种学习策略，GBDT的含义就是用Gradient Boosting的策略训练出来的DT模型。可以处理二分类问题。
