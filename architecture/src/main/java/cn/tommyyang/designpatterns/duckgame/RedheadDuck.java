package cn.tommyyang.designpatterns.duckgame;

import cn.tommyyang.designpatterns.duckgame.behavior.FlyBehavior;
import cn.tommyyang.designpatterns.duckgame.behavior.QuackBehavior;

/**
 * 红头鸭
 *
 * @Author : TommyYang
 * @Time : 2021-04-05 16:02
 * @Software: IntelliJ IDEA
 * @File : RedheadDuck.java
 */
public class RedheadDuck extends Duck {
    /**
     * 构造器
     */
    public RedheadDuck(FlyBehavior flyBehavior, QuackBehavior quackBehavior) {
        super(flyBehavior, quackBehavior);
    }

    /**
     * 具体实现鸭子外观
     */
    public void display() {
        System.out.println("红头");
    }
}
