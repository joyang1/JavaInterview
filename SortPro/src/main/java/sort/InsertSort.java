package sort;

import java.util.Arrays;

/**
 * Created by TommyYang on 2018/3/9.
 */
public class InsertSort implements IArraySort {

    @Override
    public int[] sort(int[] sourArr) throws Exception {
        // 对 sourceArr 进行拷贝，不改变参数内容
        int[] desArr = Arrays.copyOf(sourArr, sourArr.length);

        for (int i = 1; i < desArr.length ; i++){
            int tmp = desArr[i];

            int j = i;
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
