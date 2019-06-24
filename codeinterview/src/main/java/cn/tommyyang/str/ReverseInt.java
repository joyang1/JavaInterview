package cn.tommyyang.str;

/**
 * @author TommyYang on 2019-04-08
 */
public class ReverseInt {

    public int reverse(int x) {
        long res = 0;
        while (x != 0){
            res = res * 10 + x % 10;
            res /= 10;
        }

        if (res > Integer.MAX_VALUE || res < Integer.MIN_VALUE){
            return 0;
        }

        return (int)res;
    }

}
