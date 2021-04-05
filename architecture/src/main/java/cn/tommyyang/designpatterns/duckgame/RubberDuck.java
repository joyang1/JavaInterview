package cn.tommyyang.designpatterns.duckgame;

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
     * 鸭子的叫声
     */
    @Override
    public void quack() {
        System.out.println("吱吱叫");
    }

    /**
     * 具体实现鸭子外观
     */
    public void display() {
        System.out.println("橡皮鸭");
    }
}
