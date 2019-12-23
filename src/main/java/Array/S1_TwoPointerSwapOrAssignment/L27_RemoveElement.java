package Array.S1_TwoPointerSwapOrAssignment;

import static Utils.Helpers.log;
import static Utils.Helpers.swap;

/*
* Remove Element
*
* - Given an array nums and a value val, remove all instances of that value in-place and return the new length.
*
* - 限制条件：
*   1. Do not allocate extra space.
*   2. 结果可以是将 remove 掉的元素放在数组尾部，不需要硬删除。
*   3. 除了被 remove 掉的元素之外，其他元素的相对顺序不能变。
* */

public class L27_RemoveElement {
    /*
    * 解法1：
    * - 思路：与 L26_RemoveDuplicates、L283_MoveZeros 的思路一致，都是把从前往后遍历，跳过不符合条件的元素，将符合条件的元素
    *   复制到数组前面。
    * - 时间复杂度为 O(n)，空间复杂度为 O(1)。
    * */
    public static int removeElement(int[] arr, int val) {  // 解法1：two indexes
        int j = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != val) {
                if (i != j)
                    arr[j] = arr[i];
                j++;
            }
        }
        return j;
    }

    /*
    * 解法2：
    * - 思路：不同于解法1中将符合条件的元素复制到数组前面，该解法将符合条件的元素 swap 到数组前面。
    * - 时间复杂度为 O(n)，空间复杂度为 O(1)。
    * */
    public static int removeElement2(int[] arr, int val) {  // 解法2：把要删除的元素换到数组末尾去
        int j = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != val) {
                if (i != j)
                    swap(arr, i, j);
                j++;
            }
        }
        return j;
    }

    public static void main(String[] args) {
        int[] arr1 = new int[]{3, 2, 2, 3};  // 去除数组中的所有3
        log(removeElement(arr1, 3));    // expects 2
        log(arr1);                           // expects [2, 2, ...]

        int[] arr2 = new int[] {0, 1, 2, 0, 0, 0, 3, 4};  // 去除数组中的所有0
        log(removeElement(arr2, 0));   // expects 4
        log(arr2);                          // expects [1, 2, 3, 4, ...]
    }
}
