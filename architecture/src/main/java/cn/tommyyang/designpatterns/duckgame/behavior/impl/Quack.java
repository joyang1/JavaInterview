package cn.tommyyang.designpatterns.duckgame.behavior.impl;

import cn.tommyyang.designpatterns.duckgame.behavior.QuackBehavior;

/**
 * @Author : TommyYang
 * @Time : 2021-04-09 22:29
 * @Software: IntelliJ IDEA
 * @File : Quack.java
 */
public class Quack implements QuackBehavior {
    /**
     * 实现 quack 方式
     */
    @Override
    public void quack() {
        System.out.println("呱呱叫");
    }
}
