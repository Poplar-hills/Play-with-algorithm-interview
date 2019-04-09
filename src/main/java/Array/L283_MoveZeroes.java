package Array;

import java.util.ArrayList;
import java.util.List;

import static Utils.Helpers.log;
import static Utils.Helpers.swap;

public class L283_MoveZeroes {
    public static void moveZerosV1(int[] arr) {
        int[] aux = new int[arr.length];

        int j = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != 0)
                aux[j++] = arr[i];
        }

        for (int i = 0; i < arr.length; i++)
            arr[i] = aux[i];
    }

    public static void moveZerosV2(int[] arr) {
//        int i = 0;  // zero element index
//        int j = 0;  // non-zero element index
//        while (j < arr.length) {
//            if (arr[i] == 0) {
//                while (j < arr.length && arr[j] == 0)
//                    j++;
//                if (j < arr.length)
//                    swap(arr, i, j);
//            }
//            i++;
//        }

        int i = 0, firstZeroIdx = -1, nonZeroIdx = -1;
        while (i < arr.length) {
            if (arr[i] == 0)
            i++;
        }
    }

    /*
    *    0, 1, 4, 0, 0, 0, 3, 8   init
    * nf
    *    0, 1, 4, 0, 0, 0, 3, 8   i = 0
    * n  f
    *    0, 1, 4, 0, 0, 0, 3, 8   i = 1
    *    f  n
    *    1, 0, 4, 0, 0, 0, 3, 8
    *    n  f
    *    1, 0, 4, 0, 0, 0, 3, 8   i = 2
    *       f  n
    *    1, 4, 0, 0, 0, 0, 3, 8
    *       n  f
    *    1, 4, 0, 0, 0, 0, 3, 8   i = 3
    *       n  f
    *    ...
    *    1, 4, 0, 0, 0, 0, 3, 8   i = 5
    *       n  f
    *    1, 4, 0, 0, 0, 0, 3, 8   i = 6
    *          f           n
    *    1, 4, 3, 0, 0, 0, 0, 8
    *          n  f
    *    1, 4, 3, 0, 0, 0, 0, 8   i = 7
    *             f           n
    *    1, 4, 3, 8, 0, 0, 0, 0
    *             n  f
    * */

    public static void main(String[] args) {
        int[] arr1 = new int[] {0, 1, 4, 0, 0, 0, 3, 8};
        moveZerosV1(arr1);
        log(arr1);

        int[] arr2 = new int[] {0, 1};
        moveZerosV1(arr2);
        log(arr2);

        int[] arr3 = new int[] {1, 0};
        moveZerosV1(arr3);
        log(arr3);
    }

}
