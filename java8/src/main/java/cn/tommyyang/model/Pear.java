package cn.tommyyang.model;

/**
 * @Author : TommyYang
 * @Time : 2019-08-08 15:28
 * @Software: IntelliJ IDEA
 * @File : Pear.java
 */
public class Pear {

    private String color;
    private int weight;

    public Pear(String color, int weight) {
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
        return "Pear{" +
                "color='" + color + '\'' +
                ", weight=" + weight +
                '}';
    }
}
