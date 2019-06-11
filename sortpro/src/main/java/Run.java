import sort.*;

/**
 * Created by TommyYang on 2018/3/9.
 */
public class Run {

    public static void main(String[] args) {
        int[] arr = new int[]{2, 2, 23, 5, 4, 5, 3, 3};
        IArraySort bubbleSort = new BubbleSort();
        IArraySort countingSort = new CountingSort();
        IArraySort insertSort = new InsertSort();
        IArraySort selectionSort = new SelectionSort();
        IArraySort shellSort = new ShellSort();
        IArraySort mergeSort = new MergeSort();
        IArraySort quickSort = new QuickSort();
        IArraySort quickSort1 = new QuickSort1();
        IArraySort heapSort = new HeapSort();
        IArraySort bucketSort = new BucketSort();
        IArraySort radixSort = new RadixSort();
        try {
            //int[] desArr = bubbleSort.sort(arr);
            //int[] desArr = countingSort.sort(arr);
            //int[] desArr = insertSort.sort(arr);
            //int[] desArr = selectionSort.sort(arr);
            //int[] desArr = shellSort.sort(arr);
            //int[] desArr = mergeSort.sort(arr);
            //int[] desArr = quickSort.sort(arr);
            //int[] desArr = heapSort.sort(arr);
            //int[] desArr = bucketSort.sort(arr);
            int[] desArr = quickSort1.sort(arr);
            System.out.println("排序结果:");
            for (Integer i : desArr) {
                System.out.print(i);
                System.out.print("\t");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
