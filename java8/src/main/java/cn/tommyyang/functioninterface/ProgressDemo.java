package cn.tommyyang.functioninterface;

import cn.tommyyang.model.Apple;
import cn.tommyyang.model.Fruit;
import cn.tommyyang.model.Pear;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author : TommyYang
 * @Time : 2019-08-08 14:43
 * @Software: IntelliJ IDEA
 * @File : ProgressDemo.java
 */
public class ProgressDemo {

    public static List<Apple> processApple(List<Apple> appleList, ApplePredicate p) {
        List<Apple> res = new ArrayList<>();
        for (Apple apple : appleList) {
            if (p.test(apple)) {
                res.add(apple);
            }
        }
        return res;
    }

    private static final List<Apple> apples;
    private static final List<Pear> pears;

    static {
        apples = Arrays.asList(new Apple("red", 150),
                new Apple("green", 250), new Apple("red", 350),
                new Apple("red", 300));
        pears = Arrays.asList(new Pear("red", 150),
                new Pear("green", 250), new Pear("red", 350),
                new Pear("red", 300));
    }

    public static void main(String[] args) {

//        test1();

//        test2();

//        test3();

        test3_();

//        test4();

//        test5();

    }

    // lambada
    public static void test1() {

        List<Apple> appleList1 = processApple(apples, new ApplePredicate() {
            @Override
            public boolean test(Apple apple) {
                return apple.getWeight() >= 300 && apple.getColor().equals("red");
            }
        });

        ApplePredicate predicate = (Apple apple) -> apple.getWeight() >= 300 && apple.getColor().equals("red");
        List<Apple> appleList2 = processApple(apples, predicate);
//        List<Apple> appleList2 = processApple(apples, apple -> apple.getWeight() >= 300 && apple.getColor().equals("red"));
        appleList2.stream().forEach(System.out::println);
    }

    // function interface
    public static void  test2() {

        Predicate<Apple> predicate1 = (Apple apple) -> apple.getWeight() >= 300 && apple.getColor().equals("red");
        Predicate<Pear> predicate2 = (Pear pear) -> pear.getWeight() >= 300 && pear.getColor().equals("red");

        new Fruit<Apple>().process(apples, predicate1).stream().forEach(System.out::println);
        new Fruit<Pear>().process(pears, predicate2).stream().forEach(System.out::println);
    }

    // default method
    public static void  test3_() {
        System.out.println(new Demo().getName());
    }

    // default method
    public static void test3() {
        Predicate<Apple> predicate1 = (Apple apple) -> apple.getWeight() >= 300 || apple.getColor().equals("red");
        Predicate<Pear> predicate2 = (Pear pear) -> pear.getWeight() >= 300 || pear.getColor().equals("red");

        Predicate<Apple> predicate3 = predicate1.negate();
        Predicate<Pear> predicate4 = predicate2.negate();

        new Fruit<Apple>().process(apples, predicate3).stream().forEach(System.out::println);
        new Fruit<Pear>().process(pears, predicate4).stream().forEach(System.out::println);

    }

    // stream api
    public static void test4() {
        apples.stream().filter(apple -> apple.getWeight() >= 300 && apple.getColor().equals("red")).
                forEach(System.out::println);

        java.util.function.Predicate<Apple> predicate1 = (Apple apple) -> apple.getWeight() >= 300 || apple.getColor().equals("red");

        apples.stream().filter(predicate1).forEach(System.out::println);
        apples.stream().filter(predicate1.negate()).forEach(System.out::println);
    }

    // group by
    public static void test5() {
        Map<String, Long> maps = apples.stream().
                collect(Collectors.groupingBy(Apple::getColor, Collectors.counting()));

        maps.entrySet().stream().forEach(System.out::println);
    }

}
