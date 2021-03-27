package cn.tommyyang.model;

/**
 * @Author : TommyYang
 * @Time : 2019-08-08 14:44
 * @Software: IntelliJ IDEA
 * @File : Apple.java
 */
public class Apple {

    private String color;
    private int weight;

    public Apple(String color, int weight) {
        this.color = color;
        this.weight = weight;
    }

    public String getColor() {
        return color;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Apple{" +
                "color='" + color + '\'' +
                ", weight=" + weight +
                '}';
    }
}
