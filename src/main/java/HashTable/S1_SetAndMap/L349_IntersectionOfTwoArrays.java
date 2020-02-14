package HashTable.S1_SetAndMap;

import java.util.*;

import static Utils.Helpers.log;

/*
 * Intersection of Two Arrays
 *
 * - Given two arrays, write a function to compute their intersection.
 * - 注意：返回的数组应该是去重过的，SEE test case 1。
 *
 * - set 底层实现可以有3种：
 *            普通数组   顺序数组   平衡二分搜索树   哈希表
 *   - 插入     O(1)      O(n)       O(logn)      O(1)
 *   - 查找     O(n)      O(logn)    O(logn)      O(1)
 *   - 删除     O(n)      O(n)       O(logn)      O(1)
 *
 *   因此 set 各个操作的最低复杂度是 O(1)。
 * */

public class L349_IntersectionOfTwoArrays {
    /*
     * 解法1：双 Set
     * - 时间复杂度 O(m+n)，空间复杂度 O(m+n)。
     * */
    public static int[] intersection(int[] nums1, int[] nums2) {
        Set<Integer> set = new HashSet<>();
        Set<Integer> common = new HashSet<>();

        for (int n : nums1) set.add(n);
        for (int m : nums2)
            if (set.contains(m))
                common.add(m);

        int i = 0;
        int[] res = new int[common.size()];
        for (int n : common) res[i++] = n;  // 将 Set 中的元素逐个放入 array

        return res;
    }

    /*
     * 解法2：set.retainAll()
     * - 思路：与解法1一致。
     * - 关联：两个 Set 的常用操作：
     *     1. Union: set1.addAll(set2);
     *     2. Intersection: set1.retainAll(set2);
     *     3. Difference: set1.removeAll(set2);
     * - 对比：在 JS 中的 intersection：
     *     let set1 = new Set(nums1), set2 = new Set(nums2)
     *     return new Set([...set1].filter(x => set2.has(x)))
     * - 时间复杂度 O(m+n)，空间复杂度 O(m+n)。
     * */
    public static int[] intersection2(int[] nums1, int[] nums2) {
        Set<Integer> set1 = new HashSet<>();
        Set<Integer> set2 = new HashSet<>();

        for (int n : nums1) set1.add(n);
        for (int n : nums2) set2.add(n);
        set1.retainAll(set2);

        int i = 0;
        int[] res = new int[set1.size()];
        for (int n : set1) res[i++] = n;  // 将 Set 中的元素逐个放入 array

        return res;
    }

    /*
     * 解法3：二分查找
     * - 时间复杂度 O(nlogn)。
     * */
    public static int[] intersection3(int[] nums1, int[] nums2) {
        Set<Integer> set = new HashSet<>();
        Arrays.sort(nums2);   // 要先排序才能对 nums2 进行二分查找，O(nlogn)。若 nums2 本身是有序的，则整个算法复杂度降为 O(logn)

        for (int n : nums1)   // 再进行 nums1.length 次 O(log(nums2.length)) 的查找
            if (binarySearch(nums2, 0, nums2.length - 1, n))
                set.add(n);

        int i = 0;
        int[] res = new int[set.size()];
        for (int n : set) res[i++] = n;
        return res;
    }

    private static boolean binarySearch(int[] arr, int l, int r, int n) {
        if (l > r) return false;
        int mid = (r - l) / 2 + l;
        if (n > arr[mid])
            return binarySearch(arr, mid + 1, r, n);
        else if (n < arr[mid])
            return binarySearch(arr, l, mid - 1, n);
        return true;
    }

    public static void main(String[] args) {
        log(intersection(new int[]{1, 2, 2, 1}, new int[]{2, 2}));        // expects [2]. 注意返回的数组应该是去重过的
        log(intersection(new int[]{4, 9, 5}, new int[]{9, 4, 9, 8, 4}));  // expects [4, 9]
    }
}