package LookUp;

import java.util.*;

import static Utils.Helpers.log;

/*
 * Intersection of Two Arrays
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
    public static int[] intersection(int[] nums1, int[] nums2) {  // 解法1：双 set，时间复杂度 O(n)，空间复杂度 O(n)
        Set<Integer> set = new HashSet<>();
        Set<Integer> common = new HashSet<>();

        for (int n : nums1)  // 时间复杂度 O(n)，空间复杂度 O(n)
            set.add(n);
        for (int m : nums2)  // 时间复杂度 O(n)，空间复杂度 O(n)
            if (set.contains(m))
                common.add(m);

        int i = 0;
        int[] res = new int[common.size()];
        for (int n : common) res[i++] = n;  // 时间复杂度 O(n)，空间复杂度 O(n)
        return res;
    }

    public static int[] intersection2(int[] nums1, int[] nums2) {  // 解法2：使用 set 的 retainAll 方法，O(n)
        Set<Integer> set1 = new HashSet<>();
        Set<Integer> set2 = new HashSet<>();

        for (int n : nums1)
            set1.add(n);
        for (int m : nums2)
            set2.add(m);

        set1.retainAll(set2);

        int i = 0;
        int[] res = new int[set1.size()];
        for (int n : set1) res[i++] = n;
        return res;
    }

    public static int[] intersection3(int[] nums1, int[] nums2) {  // 解法3：二分查找，O(nlogn)
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
        log(intersection(new int[] {1, 2, 2, 1}, new int[] {2, 2}));        // expects [2]
        log(intersection(new int[] {4, 9, 5}, new int[] {9, 4, 9, 8, 4}));  // expects [4, 9]

        log(intersection2(new int[] {1, 2, 2, 1}, new int[] {2, 2}));
        log(intersection2(new int[] {4, 9, 5}, new int[] {9, 4, 9, 8, 4}));

        log(intersection3(new int[] {1, 2, 2, 1}, new int[] {2, 2}));
        log(intersection3(new int[] {4, 9, 5}, new int[] {9, 4, 9, 8, 4}));
    }
}