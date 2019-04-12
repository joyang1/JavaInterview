package search;

/**
 * @author TommyYang on 2019-04-12
 */
public class BinarySearch {

    //循环实现
    public static int binarySearch(int[] arr, int target){
        int low = 0, high = arr.length - 1;

        while (low <= high){
            int mid = low + (high - low) / 2;
            if (arr[mid] < target) low = mid + 1;
            else if (arr[mid] > target) high = mid - 1;
            else return mid;
        }

        return -1;
    }

    //递归实现
    public static int binarySearchRecur(int[] arr, int target, int low, int high){

        if (low > high){
            return -1;
        }

        int mid = low + (high - low) / 2;

        if(arr[mid] < target){
            return binarySearchRecur(arr, target, mid + 1, high);
        } else if(arr[mid] > target){
            return binarySearchRecur(arr, target, low, mid - 1);
        } else {
            return mid;
        }
    }

}
