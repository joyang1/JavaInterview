package cn.tommyyang.array;


import java.util.HashSet;
import java.util.Set;

/**
 * @author TommyYang on 2019-05-19
 */
//求两个数组的交集
public class InterSection {

    //暂时没想到更好的解法，感觉解法效率有待提高。
    public int[] intersection(int[] nums1, int[] nums2) {

        if(nums1 == null || nums2 == null){
            return new int[]{};
        }

        Set<Integer> set = new HashSet<Integer>(nums1.length);
        for (int i : nums1){
            set.add(i);
        }

        Set<Integer> res = new HashSet<Integer>(nums2.length);
        for (int i : nums2){
            if (set.contains(i)){
                res.add(i);
            }
        }

        int[] resArr = new int[res.size()];

        int index = 0;
        for(int i : res){
            resArr[index++] = i;
        }

        return resArr;
    }

}
