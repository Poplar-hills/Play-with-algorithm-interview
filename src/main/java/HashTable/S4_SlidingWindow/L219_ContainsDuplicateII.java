package HashTable.S4_SlidingWindow;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static Utils.Helpers.log;

/*
 * Contains Duplicate II
 *
 * - 对于整型数组 nums 和整数 k，是否存在索引 i 和 j，使得 nums[i] == nums[j]，且 i 和 j 之差不超过 k。
 * */

public class L219_ContainsDuplicateII {
    /*
     * 解法1：数组滑动窗口 + Set
     * - 思路：题中说"i 和 j 之差不超过 k"，可理解为有一个长度为 k 的窗口在数组上滑动，若窗口内存在重复元素即可返回 true。
     * - 实现：窗口采用双指针实现，“是否存在重复元素”的问题由 Set 来回答。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean containsNearbyDuplicate(int[] nums, int k) {
        if (nums == null || nums.length < 2 || k == 0) return false;
        Set<Integer> window = new HashSet<>();
        int l = 0, r = 0;                              // 窗口大小 [l,r)，r 指向待进入窗口的下一个元素

        for (; r < nums.length && r < k; r++) {        // 先将窗口长度扩展到 k（注意 nums.length < k 的情况）
            if (window.contains(nums[r])) return true; // 该过程中若发现重复，则直接返回 true
            window.add(nums[r]);
        }

        while (r < nums.length) {                      // 开始让窗口以固定长度滑动
            if (window.contains(nums[r])) return true;
            window.add(nums[r++]);                     // 将左边界的元素排除
            window.remove(nums[l++]);                  // 将右边界的元素纳入
        }

        return false;
    }

    /*
     * 解法2：查找表 Set + 滑动窗口
     * - 思路：与解法1略有不同，不再是窗口在 nums 上滑动，而是遍历 nums，将元素放到 Set 中尝试（即没有 l,r 指针滑动操作）。
     *   1. Set 中最多只保存 k 个元素，即索引在 [i-k+1, i] 范围内的元素；
     *   2. 通过将各元素放入 Set 中来回答"有没有"的问题：。
     * - 时间复杂度 O(n)，空间复杂度 O(k)。
     * */
    public static boolean containsNearbyDuplicate2(int[] nums, int k) {
        Set<Integer> set = new HashSet<>();  // 用查找表作为窗口（最多存 k 个元素）

        for (int i = 0; i < nums.length; i++) {
            if (set.contains(nums[i])) return true;
            set.add(nums[i]);
            if (set.size() == k + 1)         // Set 中元素个数达到 k+1 之前只添加不删除（巧妙之处）
                set.remove(nums[i - k]);
        }

        return false;
    }

    /*
     * 解法3：查找表 Map
     * - 思路：∵ 要找的是索引差在 k 之内的重复元素 ∴ 可以利用 Map.put() 方法，在插入元素时，若已存在则返回其之前的索引，
     *   并判断与当前的索引之差是否小于 k。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean containsNearbyDuplicate3(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            Integer index = map.put(nums[i], i);  // 若 map 中之前已有 key 为 nums[i] 的 entry 则会返回其 value，否则返回 null
            if (index != null && i - index <= k)
                return true;
        }
        return false;
    }

    public static void main(String[] args) {
        log(containsNearbyDuplicate(new int[]{1, 0, 1, 1}, 1));        // expects true
        log(containsNearbyDuplicate(new int[]{4, 1, 2, 3, 1}, 3));     // expects true
        log(containsNearbyDuplicate(new int[]{1, 2, 3, 1, 2, 3}, 2));  // expects false
        log(containsNearbyDuplicate(new int[]{1}, 1));                 // expects false
    }
}
