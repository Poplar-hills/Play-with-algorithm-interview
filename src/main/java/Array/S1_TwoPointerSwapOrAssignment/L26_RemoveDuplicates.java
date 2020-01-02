package Array.S1_TwoPointerSwapOrAssignment;

import static Utils.Helpers.log;

/*
 * Remove Duplicates
 *
 * - Given a sorted array, remove the duplicates in-place such that each element appear only once and return the new length.
 *
 * - 限制条件：
 *   1. Do not allocate extra space.
 *   2. 结果可以是将 remove 掉的元素放在数组尾部，不需要硬删除。
 *   3. 除了被 remove 掉的元素之外，其他元素的相对顺序不能变。
 * */

public class L26_RemoveDuplicates {
    /*
     * 解法1：
     * - 思路：从前往后遍历，在检测到之前没见过的元素时，将其复制到前面，让数组头部形成不重复的元素序列（后面的元素是什么不用管）。
     *   思路与 L283_MoveZeros, L27_RemoveElement 的解法1一致，只是判断条件不同。
     * - 实现：关键点是要维护一个用于存放下一个非重复元素的索引，不管对数组的遍历进行到哪，该索引之前的位置上存放的都是非重复的元素。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int removeDuplicates(int[] arr) {
        int nextValidIdx = 1;              // 存放下一个非重复元素的位置索引
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] != arr[i - 1]) {    // ∵ 有序数组中重复元素都挨在一起 ∴ 可这样判断当前元素是否重复
                if (i != nextValidIdx)     // 若至今还没发现重复元素则不需要复制（test case 2 中在第二个4以前都不需要复制元素）
                    arr[nextValidIdx] = arr[i];
                nextValidIdx++;
            }
        }
        return nextValidIdx;
    }

    public static void main(String[] args) {
        int[] arr1 = new int[]{1, 1, 2};
        log(removeDuplicates(arr1));  // expects 2
        log(arr1);                    // expects [1, 2, 2] (It doesn't matter what's left beyond the returned length)

        int[] arr2 = new int[]{1, 2, 3, 4, 4, 5};
        log(removeDuplicates(arr2));  // expects 5
        log(arr2);                    // expects [1, 2, 3, 4, 5, 5]

        int[] arr3 = new int[]{0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        log(removeDuplicates(arr3));  // expects 5
        log(arr3);                    // expects [0, 1, 2, 3, 4, 2, 2, 3, 3, 4]
    }
}
