import sort.BubbleSort;
import sort.CountingSort;
import sort.IArraySort;
import sort.InsertSort;

/**
 * Created by TommyYang on 2018/3/9.
 */
public class Run {

    public static void main(String[] args) {
        Integer[] arr = {2, 2, -3, 5, 4, 5, 3, 3};
        IArraySort bubbleSort = new BubbleSort();
        IArraySort countingSort = new CountingSort();
        IArraySort insertSort = new InsertSort();
        try {
            //Integer[] desArr = bubbleSort.sort(arr);
            //Integer[] desArr = countingSort.sort(arr);
            Integer[] desArr = insertSort.sort(arr);
            System.out.println("冒泡排序结果:");
            for (Integer i : desArr) {
                System.out.print(i);
                System.out.print("\t");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
