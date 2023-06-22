package cn.tommyyang.designpatterns.duckgame.behavior.impl;

import cn.tommyyang.designpatterns.duckgame.behavior.QuackBehavior;

/**
 * @Author : TommyYang
 * @Time : 2021-04-09 22:31
 * @Software: IntelliJ IDEA
 * @File : MuteQuack.java
 */
public class MuteQuack implements QuackBehavior {
    /**
     * 实现 quack 方式
     * 不能叫
     */
    @Override
    public void quack() {
        // nothing
        System.out.println("no quack");
    }
}
