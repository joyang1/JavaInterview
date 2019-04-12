package sort;

/**
 * @author TommyYang on 2019-04-12
 */
public class QuickSort1 {

    public void quickSort(int[] arr, int left, int right) {

        if(left >= right){
            return;
        }

        int partition = partition(arr, left, right);

        quickSort(arr, left, partition - 1);
        quickSort(arr, partition + 1, right);

    }


    public int partition(int[] arr, int i, int j) {
        int pivot = arr[i];
        while (i < j) {
            while (i < j && arr[j] > pivot)
                j--;
            if (i < j) {
                arr[i] = arr[j];
                i++;
            }

            while (i < j && arr[i] < pivot)
                i++;

            if (i < j) {
                arr[j] = arr[i];
                j--;
            }
        }

        arr[i] = pivot;
        return i;

    }

}
