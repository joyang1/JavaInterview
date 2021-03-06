package cn.tommyyang.designpatterns.duckgame;

import cn.tommyyang.designpatterns.duckgame.behavior.FlyBehavior;
import cn.tommyyang.designpatterns.duckgame.behavior.QuackBehavior;

/**
 * 橡皮鸭
 *
 * @Author : TommyYang
 * @Time : 2021-04-05 16:17
 * @Software: IntelliJ IDEA
 * @File : RubberDuck.java
 */
public class RubberDuck extends Duck {
    /**
     * 构造器
     */
    public RubberDuck(FlyBehavior flyBehavior, QuackBehavior quackBehavior) {
        super(flyBehavior, quackBehavior);
    }

    /**
     * 具体实现鸭子外观
     */
    public void display() {
        System.out.println("橡皮鸭");
    }
}
