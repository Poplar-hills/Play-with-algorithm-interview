package Array;

import static Utils.Helpers.log;
import static Utils.Helpers.swap;

/*
* - 题目中说明了 remove 的结果可以是将 remove 掉的元素放在数组尾部，不需要硬删除。
* - 两个解法的思路与 L283_MoveZeros 中的解法一致。
* - 两个解法时间复杂度为 O(n)，空间复杂度为 O(1)。
* */

public class L27_RemoveElement {
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
        int[] arr1 = new int[] {0, 1, 2, 2, 3, 0, 4, 2};
        log(removeElement(arr1, 2));
        log(arr1);  // expects [0, 1, 3, 0, 4, ...]

        int[] arr2 = new int[] {0, 1, 2, 0, 0, 0, 3, 4};
        log(removeElement2(arr2, 0));
        log(arr2);  // expects [1, 2, 3, 4, ...]
    }
}
