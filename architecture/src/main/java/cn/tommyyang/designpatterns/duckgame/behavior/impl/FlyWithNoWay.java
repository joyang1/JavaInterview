package cn.tommyyang.designpatterns.duckgame.behavior.impl;

import cn.tommyyang.designpatterns.duckgame.behavior.FlyBehavior;

/**
 * 不能飞行行为定义
 *
 * @Author : TommyYang
 * @Time : 2021-04-10 22:16
 * @Software: IntelliJ IDEA
 * @File : FlyWithNoWay.java
 */
public class FlyWithNoWay implements FlyBehavior {
    /**
     * 不能飞行行为实现
     */
    public void fly() {
        System.out.println("no way to fly");
    }
}
