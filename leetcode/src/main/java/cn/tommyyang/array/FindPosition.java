package cn.tommyyang.array;

/**
 * @author TommyYang on 2019-03-27
 */
public class FindPosition {

    public int searchInsert(int[] nums, int target) {
        if (nums.length == 0 || nums[0] >= target){
            return 0;
        }
        for (int i = 0; i < nums.length; i++){
            if (nums[i] >= target){
                return i;
            }
        }

        return nums.length;

    }

}
