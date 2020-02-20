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
     * 解法1：查找表 Map + 滑动窗口
     * - 思路：题中说"i 和 j 之差不超过 k"，可理解为有一个长度为 k 的窗口在数组上滑动，若窗口内存在重复元素即可返回 true。
     * - 实现：窗口采用双指针实现，“是否存在重复元素”的问题由 Map 来回答。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean containsNearbyDuplicate(int[] nums, int k) {
        if (nums == null || nums.length < 2 || k <= 0)
            return false;

        Map<Integer, Integer> freq = new HashMap<>();
        int l = 0, r = 0;

        while (r < nums.length && r - l <= k)      // 先将窗口长度扩展到 k
            freq.merge(nums[r++], 1, Integer::sum);

        for (int f : freq.values())                // 查看此时窗口内是否有重复元素
            if (f > 1)
                return true;

        while (r < nums.length) {                  // 开始让窗口以固定长度滑动
            int left = nums[l], right = nums[r];
            freq.merge(left, -1, Integer::sum);    // 将左边界的元素排除
            freq.merge(right, 1, Integer::sum);    // 将右边界的元素纳入
            if (freq.get(right) > 1) return true;  // 只需检查刚进入窗口的元素是否为重复即可
            l++; r++;
        }

        return false;
    }

    /*
     * 解法2：查找表 Set + 滑动窗口
     * - 思路：与解法1一致。
     * - 实现：要实现窗口其实不需要双指针，查找表本身就可以当做窗口，本解法中使用 Set：
     *     1. 作为滑动窗口：Set 中只保存 k 个元素，即索引在 [i-k+1, i] 范围内的元素；
     *     2. 来回答"有没有"的问题：检查当前元素是否存在于 Set 中。
     * - 时间复杂度 O(n)，空间复杂度 O(k)。
     * */
    public static boolean containsNearbyDuplicate2(int[] nums, int k) {
        Set<Integer> set = new HashSet<>();  // 用查找表作为窗口（最多存 k 个元素）

        for (int i = 0; i < nums.length; i++) {
            if (set.contains(nums[i])) return true;
            set.add(nums[i]);
            if (set.size() == k + 1)         // Set 中元素个数达到 k+1 之前只添加不删除
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
