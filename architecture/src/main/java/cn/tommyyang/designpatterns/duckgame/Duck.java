package cn.tommyyang.designpatterns.duckgame;

/**
 * 鸭子超类（抽象类）
 *
 * @Author : TommyYang
 * @Time : 2021-04-05 15:57
 * @Software: IntelliJ IDEA
 * @File : Duck.java
 */
public abstract class Duck {

    private String name;

    /**
     * 鸭子的叫声
     */
    public void quack() {
        System.out.println("呱呱叫");
    }

    /**
     * 鸭子游泳
     */
    public void swim() {
        System.out.println("游泳");
    }

    /**
     * 鸭子飞起来了
     */
    public void fly() {
        System.out.println("飞起来了");
    }

    /**
     * 鸭子外观都不相同，所以设计为抽象方法
     */
    public abstract void display();
}
