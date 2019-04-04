package Array;

import static Utils.Helpers.log;
import static Utils.Helpers.swap;

public class MoveZeroes {
    public static void moveZeros(Integer[] arr) {
        int i = 0; // zero index
        int j = 0; // non-zero index

        while (j < arr.length) {
            if (arr[i] == 0) {
                while (j < arr.length && arr[j] == 0)
                    j++;
                if (j < arr.length)
                    swap(arr, i, j);
            }
            i++;
        }
    }
    public static void main(String[] args) {
        Integer[] arr1 = new Integer[] {0, 1, 4, 0, 0, 0, 3, 8};
        moveZeros(arr1);
        log(arr1);

        Integer[] arr2 = new Integer[] {0, 0, 3, 8, 0};
        moveZeros(arr2);
        log(arr2);

        Integer[] arr3 = new Integer[] {0, 1, 4, 3, 8};
        moveZeros(arr3);
        log(arr3);
    }

}
