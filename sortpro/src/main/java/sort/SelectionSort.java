package sort;

import java.util.Arrays;

/**
 * Created by TommyYang on 2018/3/12.
 */
public class SelectionSort implements IArraySort {

    @Override
    public int[] sort(int[] sourArr) throws Exception {
        // 对 sourceArr 进行拷贝，不改变参数内容
        int[] desArr = Arrays.copyOf(sourArr, sourArr.length);

        //此处记住要-1， 需进行n-1轮比较
        for (int i = 0; i < desArr.length - 1; i++){
            int min = i;
            //每轮需进行n-i次比较
            for (int j = i+1; j < desArr.length; j++){
                if(desArr[j] < desArr[min]){
                    min = j; //每轮找到最小数位置的下标
                }
            }

            // 将找到的最小值和i位置所在的值进行交换
            if(min != i){
                int tmp = desArr[i];
                desArr[i] = desArr[min];
                desArr[min] = tmp;
            }
        }

        return desArr;
    }

}
