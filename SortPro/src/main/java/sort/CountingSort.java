package sort;

/**
 * Created by TommyYang on 2018/3/9.
 */

import java.util.Arrays;

/**
 * 计数排序
 */
public class CountingSort implements IArraySort {

    @Override
    public Integer[] sort(Integer[] sourceArr) throws Exception {
        // 对 arr 进行拷贝，不改变参数内容
        Integer[] arr = Arrays.copyOf(sourceArr, sourceArr.length);

        Integer maxValue = getMaxValue(arr);

        return countingSort(arr, maxValue);
    }

    private Integer[] countingSort(Integer[] arr, Integer maxValue) {
        Integer bucketLen = maxValue + 1;
        int[] bucket = new int[bucketLen];

        for (Integer value : arr) {
            bucket[value]++;
        }

        Integer sortedIndex = 0;
        for (Integer j = 0; j < bucketLen; j++) {
            while (bucket[j] > 0) {
                arr[sortedIndex++] = j;
                bucket[j]--;
            }
        }
        return arr;
    }

    private Integer getMaxValue(Integer[] arr) {
        Integer maxValue = arr[0];
        for (Integer value : arr) {
            if (maxValue < value) {
                maxValue = value;
            }
        }
        return maxValue;
    }
}
