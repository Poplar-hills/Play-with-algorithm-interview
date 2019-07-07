package Array.TwoPointerSwapOrAssignment;

import static Utils.Helpers.log;
import static Utils.Helpers.swap;

/*
* Remove Duplicates
*
* - Given a sorted array, remove the duplicates in-place such that each element appear only once and return the new length.
*
* - 限制条件：
*   1. Do not allocate extra space.
*   2. 结果可以是将 remove 掉的元素放在数组尾部，不需要硬删除。
*   3. 除了被 remove 掉的元素之外，其他元素的相对顺序不能变。
*
* - 解法的思路与 L283_MoveZeros, L27_RemoveElement 一致，只是判断条件不同。
* - 解法时间复杂度为 O(n)，空间复杂度为 O(1)。
* */

public class L26_RemoveDuplicates {
    /*
    * 解法1：
    * - 思路：从前往后遍历，在检测到之前没见过的元素时，将其复制到前面，让数组头部形成不重复的元素序列（后面的元素是什么不用管）。
    * - 解法时间复杂度为 O(n)，空间复杂度为 O(1)。
    * */
    public static int removeDuplicates(int[] arr) {
        int j = 1;                              // j 指向数组头部下一个待赋值的位置
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] != arr[i - 1]) {         // ∵ 有序数组中重复元素都挨在一起 ∴ 可以这样判断当前元素是否是重复元素
                if (i != j)
                    arr[j] = arr[i];
                j++;
            }
        }
        return j;
    }

    public static void main(String[] args) {
        int[] arr1 = new int[]{1, 1, 2};
        log(removeDuplicates(arr1));  // expects 2
        log(arr1);                    // expects [1, 2 ...] (It doesn't matter what's left beyond the returned length)

        int[] arr2 = new int[]{0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        log(removeDuplicates(arr2));  // expects 5
        log(arr2);                    // expects [0, 1, 2, 3, 4 ...]
    }
}
