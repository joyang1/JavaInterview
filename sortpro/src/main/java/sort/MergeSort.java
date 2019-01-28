package sort;

import java.util.Arrays;

/**
 * Created by TommyYang on 2018/3/12.
 */
public class MergeSort implements IArraySort {

    @Override
    public int[] sort(int[] sourArr) throws Exception {
        // 对 sourceArr 进行拷贝，不改变参数内容
        int[] desArr = Arrays.copyOf(sourArr, sourArr.length);

        if (desArr.length < 2) {
            return desArr;
        }

        int middle = (int) Math.floor(desArr.length / 2);
        int[] left = Arrays.copyOfRange(desArr, 0, middle);
        int[] right = Arrays.copyOfRange(desArr, middle, desArr.length);

        return this.merge(sort(left), sort(right));
    }

    protected int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        int i = 0;
        while (left.length > 0 && right.length > 0) {
            if (left[0] <= right[0]) {
                result[i++] = left[0];
                left = Arrays.copyOfRange(left, 1, left.length);
            } else {
                result[i++] = right[0];
                right = Arrays.copyOfRange(right, 1, right.length);
            }
        }
        while (left.length > 0) {
            result[i++] = left[0];
            left = Arrays.copyOfRange(left, 1, left.length);
        }
        while (right.length > 0) {
            result[i++] = right[0];
            right = Arrays.copyOfRange(right, 1, right.length);
        }
        return result;
    }


}
