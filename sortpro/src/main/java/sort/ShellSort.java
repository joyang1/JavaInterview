package sort;

import java.util.Arrays;

/**
 * Created by TommyYang on 2018/3/12.
 */
public class ShellSort implements IArraySort {

    @Override
    public int[] sort(int[] sourArr) throws Exception {
        // 对 sourceArr 进行拷贝，不改变参数内容
        int[] desArr = Arrays.copyOf(sourArr, sourArr.length);
        int gap = 1;
        while (gap < desArr.length / 4){ //动态定义间隔序列
            gap = gap * 4 + 1;
        }

        while (gap > 0){
            for (int i = gap; i < desArr.length; i++){
                int tmp = desArr[i];
                int j = i - gap;
                while (j >= 0 && desArr[j] > tmp){
                    desArr[j + gap] = desArr[j];
                    j -= gap;
                }
                desArr[j + gap] = tmp;
            }
            gap = (int)Math.floor(gap / 3);
        }

        return desArr;
    }

}
