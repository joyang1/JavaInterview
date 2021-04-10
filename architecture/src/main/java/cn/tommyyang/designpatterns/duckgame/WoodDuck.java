package cn.tommyyang.designpatterns.duckgame;

import cn.tommyyang.designpatterns.duckgame.behavior.FlyBehavior;
import cn.tommyyang.designpatterns.duckgame.behavior.QuackBehavior;

/**
 * @Author : TommyYang
 * @Time : 2021-04-10 22:23
 * @Software: IntelliJ IDEA
 * @File : WoodDuck.java
 */
public class WoodDuck extends Duck {
    /**
     * 构造器
     */
    public WoodDuck(FlyBehavior flyBehavior, QuackBehavior quackBehavior) {
        super(flyBehavior, quackBehavior);
    }

    /**
     * 木头鸭外观
     */
    public void display() {
        System.out.println("木头鸭");
    }
}
