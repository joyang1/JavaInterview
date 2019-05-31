package cn.tommyyang.collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @Author : TommyYang
 * @Time : 2019-05-31 16:07
 * @Software: IntelliJ IDEA
 * @File : ListUtils.java
 */
public class ListUtils {

    private final static Logger logger = LoggerFactory.getLogger(ListUtils.class);

    public static List maxArrayList(int size) {
        List<String> strList = new ArrayList<String>(size);
        for (int i = 0; i < size; i++) {
            strList.add(i + "test");
        }
        return strList;
    }

    public static List maxList(int size) {
        List<String> strList = new LinkedList<String>();
        for (int i = 0; i < size; i++) {
            strList.add(i + "test");
        }
        return strList;
    }

    public static List testList(int size) {
        List<String> testList = new ArrayList<String>();
        for (int i = 0; i < size; i++) {
            testList.add(i + "test");
        }
        return testList;
    }

    public static Set testSet(int size) {
        Set<String> testSet = new HashSet<String>();
        for (int i = 0; i < size; i++) {
            testSet.add(i + "test");
        }
        return testSet;
    }

    public static void deWeightList(List<String> des, List<String> sourse) {
        if (sourse == null || sourse.size() <= 0) {
            return;
        }
        Iterator<String> listStr = sourse.iterator();
        while (listStr.hasNext()) {
            String item = listStr.next();
            if (des.contains(item)) {
                listStr.remove(); //考虑到list的删除效率慢，此种方法对于大数据集合来说不合适
            }
        }
        logger.info("after deWight list size: " + sourse.size());
    }

    public static void deWeightList(Set<String> des, List<String> sourse) {
        if (sourse == null || sourse.size() <= 0) {
            return;
        }
        Iterator<String> listStr = sourse.iterator();
        while (listStr.hasNext()) {
            String item = listStr.next();
            if (des.contains(item)) {
                listStr.remove(); //考虑到list的删除效率慢，此种方法对于大数据集合来说不合适
            }
        }
        logger.info("after deWight list size: " + sourse.size());
    }

    public static void deWeightListByNewList(Set<String> des, List<String> sourse) {
        if (sourse == null || sourse.size() <= 0) {
            return;
        }
        Iterator<String> listStr = sourse.iterator();
        List<String> existList = new ArrayList<String>();
        while (listStr.hasNext()) {
            String item = listStr.next();
            if(!des.contains(item)){
                //TODO 对去重后的数据进行逻辑操作，不一定要删除，可以换个思路（是否可以直接逻辑操作，不一定非要再把数据写进集合后，然后遍历集合在进行逻辑操作）
                existList.add(item); //改成添加进新的list，考虑到list的删除效率慢(非要得到删除后的集合的情况下，否则走else)
            }
        }
        sourse.clear();
        sourse = existList;
        logger.info("after deWight list size: " + sourse.size());
    }

    public static void deWeightListOther(List<String> des, List<String> sourse) {
        if (sourse == null || sourse.size() <= 0) {
            return;
        }
        for (int i = sourse.size() - 1; i >= 0; i--) {
            String str = sourse.get(i);
            for (String item : des) {
                if (str.equals(item)) {
                    sourse.remove(i);
                    break;
                }
            }
        }
        logger.info("after other deWight list size: " + sourse.size());
    }


}
