package cn.tommyyang.string;

/**
 * @Author : TommyYang
 * @Time : 2019-06-04 11:12
 * @Software: IntelliJ IDEA
 * @File : StringTest.java
 */
public class StringTest {

    public static void main(String[] args) {
        String a = "fsff";
        a.replace('s', '_');

        String b = new String("fsff");
        b.replace('s', '_');

        StringBuilder sb = new StringBuilder("fsff");
        sb.replace(1, 2, "_").toString().replace('_', 's');
        System.out.println(a);
        System.out.println(b);
        System.out.println(sb.toString());
        //通过以上例子发现 String 是不可变类， StringBuilder 是可变类
    }

}
