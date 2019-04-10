package Array;

import static Utils.Helpers.log;

/*
* - 对数组去重，使得数组中的每个元素最多保留两个
* - 解法的思路与 L26_RemoveDuplicates 一致，只是判断条件不同。
* - 解法时间复杂度为 O(n)，空间复杂度为 O(1)。
* */

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
