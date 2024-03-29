package Array.S1_TwoPointerSwapOrAssignment;

import static Utils.Helpers.log;
import static Utils.Helpers.swap;

/*
 * Remove Element
 *
 * - Given an array and a value, remove all instances of that value in-place and return the new length.
 *
 * - 限制条件：
 *   1. Do not allocate extra space.
 *   2. 结果可以是将 remove 掉的元素放在数组尾部，不需要硬删除。
 *   3. 除了被 remove 掉的元素之外，其他元素的相对顺序不能变。
 * */

public class L27_RemoveElement {
    /*
     * 解法1：双指针赋值
     * - 思路：与 L26_RemoveDuplicates、L283_MoveZeros 的思路一致，从前往后遍历，跳过不符合条件的元素，将符合条件的元素
     *   复制到数组前面。
     * - 时间复杂度为 O(n)，空间复杂度为 O(1)。
     * */
    public static int removeElement(int[] arr, int val) {
        int nextValidIdx = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != val) {
                if (i != nextValidIdx)
                    arr[nextValidIdx] = arr[i];
                nextValidIdx++;
            }
        }
        return nextValidIdx;
    }

    /*
     * 解法2：双指针 + Swap
     * - 思路：与解法1的唯一区别是，使用 swap 代替解法一中的赋值，将不等于 val 的元素 swap 到数组前面。
     * - 时间复杂度为 O(n)，空间复杂度为 O(1)。
     * */
    public static int removeElement2(int[] arr, int val) {
        int nextValidIdx = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != val) {
                if (i != nextValidIdx)
                    swap(arr, i, nextValidIdx);
                nextValidIdx++;
            }
        }
        return nextValidIdx;
    }

    public static void main(String[] args) {
        int[] arr1 = new int[]{2, 3, 2, 2, 3};  // 去除数组中的所有3
        log(removeElement(arr1, 3));        // expects 3
        log(arr1);                              // expects [2, 2, 2, 2, 3]

        int[] arr2 = new int[]{0, 1, 2, 0, 0, 0, 3, 4};  // 去除数组中的所有0
        log(removeElement(arr2, 0));        // expects 4
        log(arr2);                              // expects [1, 2, 3, 4, 0, 0, 3, 4]
    }
}
