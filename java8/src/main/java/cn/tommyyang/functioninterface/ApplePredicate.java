package cn.tommyyang.functioninterface;

import cn.tommyyang.model.Apple;

/**
 * @Author : TommyYang
 * @Time : 2019-08-08 14:44
 * @Software: IntelliJ IDEA
 * @File : ApplePredicate.java
 */

public interface ApplePredicate {
    boolean test (Apple apple);
}
