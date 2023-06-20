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

    /**
     * 名称
     */
    private String name;

    /**
     * 飞行行为
     */
    private FlyBehavior flyBehavior;

    /**
     * 呱呱叫行为
     */
    private QuackBehavior quackBehavior;

    /**
     * 鸭子的叫声
     */
    public void performQuack() {
        this.quackBehavior.quack();
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
        this.flyBehavior.fly();
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
