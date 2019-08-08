package cn.tommyyang.streamapi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @Author : TommyYang
 * @Time : 2019-08-06 16:56
 * @Software: IntelliJ IDEA
 * @File : StreamAPI.java
 */
public class StreamAPI {

    private static List<Integer> addItems;
    private static Stream<Integer> stream;
    private static Stream<Integer> parallelStream;

    static {
        addItems = new ArrayList<>(10);
        for (int i = 1; i <= 10; i++) {
            addItems.add(i);
        }
        stream = addItems.stream();
        parallelStream = addItems.parallelStream();
    }

    public static void main(String[] args) {
        testDisplay();
//        testAdd();
    }

    public static void testAdd() {
        long t1 = System.currentTimeMillis();
        streamAdd();
        long t2 = System.currentTimeMillis();
        parallelStreamAdd();
        long t3 = System.currentTimeMillis();
        defaultAdd();
        long t4 = System.currentTimeMillis();

        System.out.println("streamAdd time used:" + (t2 - t1));
        System.out.println("parallelStreamAdd time used:" + (t3 - t2));
        System.out.println("defaultAdd time used:" + (t4 - t3));
    }

    public static void testDisplay() {
        long t1 = System.currentTimeMillis();
        streamDisplay();
        long t2 = System.currentTimeMillis();
        parallelStreamDisplay();
        long t3 = System.currentTimeMillis();
        defaultDisplay();
        long t4 = System.currentTimeMillis();

        System.out.println("stream time used:" + (t2 - t1));
        System.out.println("parallelStream time used:" + (t3 - t2));
        System.out.println("default time used:" + (t4 - t3));
    }

    public static void streamAdd() {
        CountService countService = new CountService();
        stream.forEach(countService::atomicCount);

        System.out.println(countService.getCount());
    }

    public static void parallelStreamAdd() {
        CountService countService = new CountService();
        parallelStream.forEach(countService::atomicCount);

        System.out.println(countService.getCount());
    }

    public static void defaultAdd() {
        CountService countService = new CountService();
        for (int i : addItems) {
            countService.atomicCount(i);
        }

        System.out.println(countService.getCount());
    }

    public static void streamDisplay() {
//       stream.forEach(System.out::println);
        stream.forEach(item -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(item);
        });

        System.out.println("stream display end ------");
    }

    public static void parallelStreamDisplay() {
        parallelStream.forEach(item -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(item);
        });

        System.out.println("parallel stream display end ------");
    }

    public static void defaultDisplay() {
        for (int i : addItems) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(i);
        }

        System.out.println("end--------");
    }

}
