package Array.S1_TwoPointerSwapOrAssignment;

import static Utils.Helpers.log;
import static Utils.Helpers.swap;

/*
 * Remove Duplicates II
 *
 * - Given a sorted array, remove the duplicates in-place such that duplicates appeared at most twice and
 *   return the new length (为有序数组去重，每个元素最多出现两次).
 * */

public class L80_RemoveDuplicatesII {
    /*
     * 解法1：双指针赋值
     * - 思路：与 L26_RemoveDuplicates 一致，只是判断条件不同，L26 的判断条件是 arr[i] == arr[i-1]，两边都是用 i 取值，
     *   而该解法中需要使用 arr[i] == arr[v-2] 比较：
     *     1, 1, 1, 2, 2, 3
     *           vi            - arr[i]==arr[v-2], v stays, i++
     *     1, 1, 1, 2, 2, 3
     *           v  i          - arr[i]!=arr[v-2], v!=i, arr[v]=arr[i], v++, i++
     *     1, 1, 2, 2, 2, 3
     *              v  i       - arr[i]!=arr[v-2], v!=i, arr[v]=arr[i], v++, i++
     *     1, 1, 2, 2, 2, 3
     *                 v  i    - arr[i]!=arr[v-2], v!=i, arr[v]=arr[i], v++, i++
     * - 实现：
     *   - 注意这里比较的是 arr[i] 与 arr[v-2]，而非 arr[i] 与 arr[i-2]，这是 ∵ 若要将 arr[i] 复制到 arr[v] 上，则
     *     需要保证 arr[i] != arr[v-2]，否则复制后仍会出现三个 duplicates 的情况。
     *   - L26_RemoveDuplicates 的实现稍微难懂，而本实现中的逻辑更符合直觉。
     *   - 👉 与上上个元素比较是在有序数组中判断一个元素是否连续出现3次的方法。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int removeDuplicates(int[] arr) {
        if (arr == null) return 0;
        if (arr.length <= 2) return arr.length;

        int nextValidIdx = 2;
        for (int i = 2; i < arr.length; i++) {    // 从第3个元素开始遍历
            if (arr[i] == arr[nextValidIdx - 2])  // 注意比较的是 arr[i] 与 arr[v-2]，而非 arr[i] 与 arr[i-2]
                continue;           // 若发现有元素连续出现3次，则只让 i++，nextValidIdx 不变
            if (nextValidIdx != i)
                arr[nextValidIdx] = arr[i];
            nextValidIdx++;
        }
        return nextValidIdx;
    }

    /*
     * 解法2：双指针 + Swap
     * - 思路：与解法1类似，只是由直接赋值改为了 swap。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int removeDuplicates2(int[] arr) {
        if (arr == null) return 0;
        if (arr.length <= 2) return arr.length;

        int nextValidIdx = 2;
        for (int i = 2; i < arr.length; i++) {
            if (arr[i] == arr[nextValidIdx - 2])
                continue;
            if (i != nextValidIdx)
                swap(arr, i, nextValidIdx);  // 与解法1唯一区别就是改成了 swap
            nextValidIdx++;
        }
        return nextValidIdx;
    }

    /*
     * 解法3：双指针 + Swap
     * - 思路：与解法2一致。
     * - 实现：与解法1、2的不同是判断条件不再比较 arr[i] 与 arr[nextValidIdx]，而是判断条件的两边都改用 i 取值。但这样一来，
     *   [1,1,1,2,2,3] 在 swap 之后会变成 [1,1,2,1,2,3] ∴ 不能只判断 arr[i] == arr[i-2]，而是必须判断
     *   arr[i], arr[i-1], arr[i-2] 3个元素都相等才行。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int removeDuplicates3(int[] arr) {
        if (arr == null) return 0;
        if (arr.length <= 2) return arr.length;

        int nextValidIdx = 2;
        for (int i = 2; i < arr.length; i++) {
            if (arr[i] == arr[i - 1] && arr[i - 1] == arr[i - 2])
                continue;
            if (i != nextValidIdx)
                swap(arr, i, nextValidIdx);
            nextValidIdx++;
        }
        return nextValidIdx;
    }

    public static void main(String[] args) {
        int[] arr1 = new int[]{1, 1, 1, 2, 2, 3};
        log(removeDuplicates(arr1));  // expects 5
        log(arr1);                    // expects [1, 1, 2, 2, 3, ..] (It doesn't matter what you leave beyond the returned length.)

        int[] arr2 = new int[]{2, 2, 3, 3};
        log(removeDuplicates(arr2));  // expects 4
        log(arr2);                    // expects [2, 2, 3, 3]
    }
}
