package cn.tommyyang.functioninterface;

/**
 * @Author : TommyYang
 * @Time : 2019-08-08 16:17
 * @Software: IntelliJ IDEA
 * @File : Demo.java
 */
public class Demo implements DefaultInterface {
    @Override
    public void test() {
        System.out.println("test");
    }

//    @Override
//    public String getName() {
//        return "demo";
//    }
}
