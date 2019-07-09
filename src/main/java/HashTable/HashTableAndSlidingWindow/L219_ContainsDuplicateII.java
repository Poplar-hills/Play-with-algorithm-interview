package HashTable.HashTableAndSlidingWindow;

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
    * 解法1：查找表 + 滑动窗口
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static boolean containsNearbyDuplicate(int[] nums, int k) {
        if (nums == null || nums.length < 2 || k <= 0)
            return false;

        Map<Integer, Integer> freq = new HashMap<>();
        int l = 0, r = 0;

        // 先将窗口拉宽到 k 的长度
        for (; r < nums.length && r - l <= k; r++)  // 注意 r 的限制（SEE test case 4）
            freq.put(nums[r], freq.getOrDefault(nums[r], 0) + 1);
        for (int f : freq.values())  // 查看此时窗口内是否有 duplicate
            if (f > 1)
                return true;

        // 开始让窗口滑动
        while (r < nums.length) {
            int oldE = nums[l], newE = nums[r];
            freq.put(oldE, freq.get(oldE) - 1);
            freq.put(newE, freq.getOrDefault(newE, 0) + 1);
            if (freq.get(newE) > 1) return true;  // 只需检查新进入的元素在窗口内是否有 duplicate 即可
            l++; r++;
        }

        return false;
    }

    /*
     * 解法2：查找表 + 滑动窗口
     * - 思路：题中说"i 和 j 之差不超过 k"，可理解为有一个长度为 k 的窗口在数组上滑动，若窗口内存在两个值相同的元素即可返回 true。
     *   既可以作为滑动窗口，又能回答"有没有"的问题的数据结构就是查找表了，具体来说，可以采用 Set：
     *   1. 作为滑动窗口：Set 中只保存 k 个元素，即索引在 [i-k+1, i] 范围内的元素；
     *   2. 回答"有没有"的问题：当遍历到新元素时检查它是否已存在与 Set 中（说明元素值相等）。
     * - 时间复杂度 O(n)，空间复杂度 O(k)。
     * */
    public static boolean containsNearbyDuplicate2(int[] nums, int k) {
        Set<Integer> set = new HashSet<>();  // 查找表 & 窗口（最多存 k 个元素）
        for (int i = 0; i < nums.length; i++) {
            if (set.contains(nums[i])) return true;
            set.add(nums[i]);
            if (set.size() == k + 1)  // Set 中元素个数达到 k+1 之前只添加不删除
                set.remove(nums[i - k]);
        }
        return false;
    }

    /*
     * 解法3：查找表 Map
     * - 思路：
     *   - 本题的关键点是重复元素的索引要在 k 之内，因此可以建立 Map<存储元素, 索引> 的映射。
     *   - 再利用 Map.put() 方法的特性 -- 在更新某个 key 的 value 时，若该 key 已存在于 Map 中则返回其 value，若不存在则返回 null
     *     并直接添加。利用这一点，在遍历到处理新元素时，用 put 查询上次存储的该值的索引，若索引存且与新元素索引之差小于 k，则返回 true。
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
        log(containsNearbyDuplicate3(new int[] {1, 0, 1, 1}, 1));        // expects true
        log(containsNearbyDuplicate3(new int[] {4, 1, 2, 3, 1}, 3));     // expects true
        log(containsNearbyDuplicate3(new int[] {1, 2, 3, 1, 2, 3}, 2));  // expects false
        log(containsNearbyDuplicate3(new int[] {1}, 1));                 // expects false
        log(containsNearbyDuplicate3(new int[] {99, 99}, 2));            // expects true
    }
}
