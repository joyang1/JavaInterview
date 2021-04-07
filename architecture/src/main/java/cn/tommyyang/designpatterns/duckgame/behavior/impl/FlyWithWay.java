package cn.tommyyang.designpatterns.duckgame.behavior.impl;

import cn.tommyyang.designpatterns.duckgame.behavior.FlyBehavior;

/**
 * 实现飞行方法
 *
 * @Author : TommyYang
 * @Time : 2021-04-07 23:23
 * @Software: IntelliJ IDEA
 * @File : FlyWithWay.java
 */
public class FlyWithWay implements FlyBehavior {
    /**
     * 实现飞行行为
     */
    public void fly() {
        System.out.println("fly with way");
    }
}
