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
    public Integer[] sort(Integer[] sourArr) throws Exception {
        //对arr进行拷贝，不改变参数的内容
        Integer[] desArr = Arrays.copyOf(sourArr, sourArr.length);

        for (Integer i = 0; i < desArr.length ; i++){
            //设置一个标志，用来判断该数组是否已经有序，提高效率
            Boolean flag = Boolean.TRUE;

            //该处一定要是desArr.length - i - 1，切记不能是desArr.length - i
            for(Integer j = 0; j < desArr.length - i - 1; j ++){
                if(desArr[j] > desArr[j+1]){
                    Integer temp = desArr[j];
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
