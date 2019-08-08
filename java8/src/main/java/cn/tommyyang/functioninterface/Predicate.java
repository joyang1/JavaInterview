package cn.tommyyang.functioninterface;

/**
 * @Author : TommyYang
 * @Time : 2019-08-08 15:05
 * @Software: IntelliJ IDEA
 * @File : Predicate.java
 */

@FunctionalInterface
public interface Predicate<T> {

    boolean test(T t);

    default Predicate<T> or(Predicate<? super T> other) {
        return t -> test(t) || other.test(t);
    }

    default Predicate<T> and(Predicate<? super T> other) {
        return t -> test(t) && other.test(t);
    }

    default Predicate<T> negate() {
        return t -> !test(t);
    }

}
