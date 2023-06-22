package cn.tommyyang.designpatterns.duckgame.behavior.impl;

import cn.tommyyang.designpatterns.duckgame.behavior.QuackBehavior;

/**
 * @Author : TommyYang
 * @Time : 2021-04-09 22:30
 * @Software: IntelliJ IDEA
 * @File : Squeak.java
 */
public class Squeak implements QuackBehavior {
    /**
     * 实现 quack 方式
     */
    @Override
    public void quack() {
        System.out.println("吱吱叫");
    }
}
