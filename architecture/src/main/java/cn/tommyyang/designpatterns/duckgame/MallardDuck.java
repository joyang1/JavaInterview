package cn.tommyyang.designpatterns.duckgame;

/**
 * 野鸭类
 *
 * @Author : TommyYang
 * @Time : 2021-04-05 16:00
 * @Software: IntelliJ IDEA
 * @File : MallardDuck.java
 */
public class MallardDuck extends Duck {
    /**
     * 具体实现鸭子外观
     */
    public void display() {
        System.out.println("绿头");
    }
}
