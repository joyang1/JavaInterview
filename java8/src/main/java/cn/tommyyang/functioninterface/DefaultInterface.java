package cn.tommyyang.functioninterface;

/**
 * @Author : TommyYang
 * @Time : 2019-08-08 16:16
 * @Software: IntelliJ IDEA
 * @File : DefaultInterface.java
 */
public interface DefaultInterface {

    void test();

    default String getName() {
        return "default";
    }
}
