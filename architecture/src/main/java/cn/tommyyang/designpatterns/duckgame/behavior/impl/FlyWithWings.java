package cn.tommyyang.designpatterns.duckgame.behavior.impl;

import cn.tommyyang.designpatterns.duckgame.behavior.FlyBehavior;

/**
 * 通过翅膀飞行实现类
 *
 * @Author : TommyYang
 * @Time : 2021-04-07 23:21
 * @Software: IntelliJ IDEA
 * @File : FlyWithWings.java
 */
public class FlyWithWings implements FlyBehavior {
    /**
     * 实现飞行行为
     */
    public void fly() {
        System.out.println("fly with wings");
    }
}
