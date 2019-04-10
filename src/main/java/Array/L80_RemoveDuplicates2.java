package Array;

import static Utils.Helpers.log;

public class L80_RemoveDuplicates2 {
    public static int removeDuplicates(int[] arr) {
        int j = 2;
        for (int i = 2; i < arr.length; i++)
            if (arr[i] > arr[j - 2])
                arr[j++] = arr[i];
        return j;
    }

    public static void main(String[] args) {
        int[] arr1 = new int[] {1, 1, 1, 2, 2, 3};
        log(removeDuplicates(arr1));
        log(arr1);  // expects [1, 1, 2, 2, 3, ...]
    }
}
