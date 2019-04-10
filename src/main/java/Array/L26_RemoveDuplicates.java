package Array;

import static Utils.Helpers.log;

/*
* - 题目中说明了 remove 的结果可以是将 remove 掉的元素放在数组尾部，不需要硬删除。
* - 解法的思路与 L283_MoveZeros, L27_RemoveElement 一致，只是判断条件不同。
* - 解法时间复杂度为 O(n)，空间复杂度为 O(1)。
* */

public class L26_RemoveDuplicates {
    public static int removeDuplicates(int[] arr) {
        int j = 1;
        for (int i = 1; i < arr.length; i++) {  // 索引 i 和 j 都从1开始
            if (arr[i] != arr[i - 1]) {  // 因为有序数组中重复元素都挨在一起，因此可以这样判断当前元素是否是重复元素
                if (i != j)
                    arr[j] = arr[i];
                j++;
            }
        }
        return j;
    }

    public static void main(String[] args) {
        int[] arr1 = new int[] {1, 2, 2, 3, 4, 5, 5, 5, 6};
        log(removeDuplicates(arr1));
        log(arr1);  // expects [1, 2, 3, 4, 5, 6 ...]
    }
}
