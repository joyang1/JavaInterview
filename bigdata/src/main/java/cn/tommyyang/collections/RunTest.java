package cn.tommyyang.collections;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author : TommyYang
 * @Time : 2019-05-31 16:09
 * @Software: IntelliJ IDEA
 * @File : RunTest.java
 */
public class RunTest {

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");

        List<String> maxArrayList = ListUtils.maxArrayList(25000000);
//        Set<String> maxSet = new HashSet<String>(maxArrayList);
//        maxSet.addAll(maxArrayList);

        List<String> testList = ListUtils.testList(20000);
        Set<String> testSet = ListUtils.testSet(1500000);

        //遍历过程中去重
        System.out.println("start test foreach list directly, start time: " + sdf.format(new Date()));

        for (String item: maxArrayList) {
            if(!testSet.contains(item)){
                //TODO
            }
        }

        System.out.println("end test foreach list directly, end time: " + sdf.format(new Date()) + "\n");


        //List结合List去重
        System.out.println("start test list, start time: " + sdf.format(new Date()));

        ListUtils.deWeightList(testList, maxArrayList);

        System.out.println("end test list, end time: " + sdf.format(new Date()) + "\n");

        maxArrayList.clear();
        maxArrayList = ListUtils.maxArrayList(25000000);

        //List结合Set去重
        System.out.println("start test set, start time: " + sdf.format(new Date()));

        ListUtils.deWeightList(testSet, maxArrayList);

        System.out.println("end test set, end time: " + sdf.format(new Date()));

        maxArrayList.clear();
        maxArrayList = ListUtils.maxArrayList(25000000);
        //List结合Set去重(不是直接对list进行删除，而是组装新list，考虑到list删除效率低)
        System.out.println("start test set, start time: " + sdf.format(new Date()));

        ListUtils.deWeightListByNewList(testSet, maxArrayList);

        System.out.println("end test set, end time: " + sdf.format(new Date()));


    }



}
