package cn.tommyyang.designpatterns.duckgame;

import cn.tommyyang.designpatterns.duckgame.behavior.FlyBehavior;
import cn.tommyyang.designpatterns.duckgame.behavior.QuackBehavior;

/**
 * 鸭子超类（抽象类）
 *
 * @Author : TommyYang
 * @Time : 2021-04-05 15:57
 * @Software: IntelliJ IDEA
 * @File : Duck.java
 */
public abstract class Duck {

    public Duck(FlyBehavior flyBehavior, QuackBehavior quackBehavior) {
        this.flyBehavior = flyBehavior;
        this.quackBehavior = quackBehavior;
    }

    private String name;

    private FlyBehavior flyBehavior;

    private QuackBehavior quackBehavior;

    /**
     * 鸭子的叫声
     */
    public void quack() {
        System.out.println("嘎嘎叫");
    }

    /**
     * 鸭子游泳
     */
    public void swim() {
        System.out.println("游泳");
    }


    /**
     * 执行飞行行为
     */
    public void performFly() {

    }

    /**
     * 鸭子外观都不相同，所以设计为抽象方法
     */
    public abstract void display();

    public void setFlyBehavior(FlyBehavior flyBehavior) {
        this.flyBehavior = flyBehavior;
    }

    public void setQuackBehavior(QuackBehavior quackBehavior) {
        this.quackBehavior = quackBehavior;
    }
}
