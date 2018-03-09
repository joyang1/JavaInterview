package sort;

import java.util.Arrays;

/**
 * Created by TommyYang on 2018/3/9.
 */
public class InsertSort implements IArraySort {

    @Override
    public Integer[] sort(Integer[] sourArr) throws Exception {
        Integer[] desArr = Arrays.copyOf(sourArr, sourArr.length);

        for (Integer i = 1; i < desArr.length ; i++){
            Integer tmp = desArr[i];

            Integer j = i;
            while (j > 0 && tmp < desArr[j - 1]){
                desArr[j] = desArr[j - 1];
                j--;
            }
            if(j != i){
                desArr[j] = tmp;
            }
        }
        return desArr;
    }

}
