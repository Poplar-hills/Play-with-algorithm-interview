package HashTable.HashTableAndSlidingWindow;

import java.util.HashMap;
import java.util.Map;

import static Utils.Helpers.log;

/*
* Contains Duplicate II
*
* - 对于整型数组 nums 和整数 k，是否存在索引 i 和 j，使得 nums[i] == nums[j]，且 i 和 j 之间的差不超过 k。
* */

public class L219_ContainsDuplicateII {
    /*
    * 解法1：
    * - 思路：查找表 + 滑动窗口。
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
     * 解法2：
     * - 思路：查找表（利用 Java 里 Map 的 put 方法）
     *   - Map 中存储元素 -> 索引的映射。
     *   - put 方法返回该 key 更新之前的 value，若之前 key 不存在则返回 null。利用这一点，在每次取到新元素时，用 put 存储该元素，
     *     并查询上次存储该元素的索引，若索引存且与新元素索引之差小于 k，则 return true。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean containsNearbyDuplicate2(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            Integer index = map.put(nums[i], i);
            if (index != null && i - index <= k)
                return true;
        }
        return false;
    }

    public static void main(String[] args) {
        log(containsNearbyDuplicate2(new int[] {1, 0, 1, 1}, 1));        // expects true
        log(containsNearbyDuplicate2(new int[] {4, 1, 2, 3, 1}, 3));     // expects true
        log(containsNearbyDuplicate2(new int[] {1, 2, 3, 1, 2, 3}, 2));  // expects false
        log(containsNearbyDuplicate2(new int[] {1}, 1));                 // expects false
        log(containsNearbyDuplicate2(new int[] {99, 99}, 2));            // expects true
    }
}
