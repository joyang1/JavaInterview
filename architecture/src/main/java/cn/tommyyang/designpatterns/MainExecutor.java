package cn.tommyyang.designpatterns;

import cn.tommyyang.designpatterns.duckgame.Duck;
import cn.tommyyang.designpatterns.duckgame.RubberDuck;
import cn.tommyyang.designpatterns.duckgame.WoodDuck;
import cn.tommyyang.designpatterns.duckgame.behavior.impl.FlyWithNoWay;
import cn.tommyyang.designpatterns.duckgame.behavior.impl.MuteQuack;
import cn.tommyyang.designpatterns.duckgame.behavior.impl.Squeak;

/**
 * 鸭子游戏执行器
 *
 * @Author : TommyYang
 * @Time : 2021-04-05 16:00
 * @Software: IntelliJ IDEA
 * @File : MainExecutor.java
 */
public class MainExecutor {

    public static void main(String[] args) {

        // 实现不能飞行的橡皮鸭
        RubberDuck rubberDuck = new RubberDuck(new FlyWithNoWay(), new Squeak());
        execute(rubberDuck);

        // 实现不能飞也不能叫的橡皮鸭
        WoodDuck woodDuck = new WoodDuck(new FlyWithNoWay(), new MuteQuack());
        execute(woodDuck);

        // 后续各种鸭子实现，根据业务方的需求，设置其行为即可，行为可扩展，可复用
        // 这就是"策略模式"，各种策略实现好，组合使用即可
    }


    /**
     * 鸭子游戏开始运行
     */
    private static void execute(Duck duck) {
        duck.display();
        duck.performQuack();
        duck.performFly();
    }
}
