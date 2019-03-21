package cn.tommyyang.array;

/**
 * @author TommyYang on 2019-03-21
 */
public class TwoSortArrMerge {

    public static void main(String[] args) {
        int[] nums1 = new int[]{2,5,6, 0 , 0, 0};
        int[] nums2 = new int[]{1,2,3};

        merge(nums1, 3, nums2, 3);

        for(int i : nums1){
            System.out.println(i);
        }
    }

    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        int i = m - 1, j = n - 1, index = m + n - 1;
        while (i >= 0 || j >= 0){
            //i < 0; 说明 nums1已经填充完了,只剩下nums2,所以只需要把nums2填充到nums1对应的index上就行
            if(i < 0){
                nums1[index--] = nums2[j--];
                continue;
            }else if (j < 0){
                // j < 0; 说明nums2已经全部填充到了nums1,直接跳出循环就行了；
                break;
            }
            //将数据填充到具体的index
            if(nums1[i] >= nums2[j]){
                nums1[index--] = nums1[i--];
            } else {
                nums1[index--] = nums2[j--];
            }
        }
    }

}
