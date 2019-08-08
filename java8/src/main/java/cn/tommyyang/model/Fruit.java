package cn.tommyyang.model;

import cn.tommyyang.functioninterface.Predicate;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : TommyYang
 * @Time : 2019-08-08 15:32
 * @Software: IntelliJ IDEA
 * @File : Fruit.java
 */

public class Fruit<T> {

    public List<T> process(List<T> ts, Predicate p) {
        List<T> res = new ArrayList<>();
        for (T t : ts) {
            if (p.test(t)) {
                res.add(t);
            }
        }
        return res;
    }

}
