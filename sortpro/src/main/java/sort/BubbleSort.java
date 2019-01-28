package sort;

import java.util.Arrays;

/**
 * Created by TommyYang on 2018/3/9.
 */
/**
 * 冒泡排序
 */
public class BubbleSort implements IArraySort {

    @Override
    public int[] sort(int[] sourArr) throws Exception {
        // 对 sourceArr 进行拷贝，不改变参数内容
        int[] desArr = Arrays.copyOf(sourArr, sourArr.length);

        for (int i = 0; i < desArr.length ; i++){
            //设置一个标志，用来判断该数组是否已经有序，提高效率
            Boolean flag = Boolean.TRUE;

            //该处一定要是desArr.length - i - 1，切记不能是desArr.length - i
            for(int j = 0; j < desArr.length - i - 1; j ++){
                if(desArr[j] > desArr[j+1]){
                    int temp = desArr[j];
                    desArr[j] = desArr[j+1];
                    desArr[j+1] = temp;
                    flag = false;
                }
            }
            if(flag){
                break;
            }
        }
        return desArr;
    }

}
