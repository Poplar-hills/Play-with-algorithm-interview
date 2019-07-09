package HashTable.HashTableAndSlidingWindow;

import java.util.TreeSet;

import static Utils.Helpers.log;

/*
* Contains Duplicate III
*
* - 对于整型数组 nums 和整数 k，是否存在索引 i 和 j，使得：
*   1. nums[i] 与 nums[j] 的差不超过整数 t；
*   2. i 和 j 之间的差不超过整数 k。
*
* - 分析可知本题中的两个限制条件：
*   1. |i - j| <= k
*   2. |nums[i] - nums[j]| <= t
*
* */

public class L220_ContainsDuplicateIII {
    /*
    * 解法1：
    * - 分析：本题中的两个需求：1. |i - j| <= k； 2. |nums[i] - nums[j]| <= t。
    * - 思路：仍然可以采用 L219_ContainsDuplicateII 中解法2的实现方式，即 Set 作查找表 + 滑动窗口：
    *   - 对于需求1：用 Set 作为 window，只保存 k 个元素，将差距超过 k 的元素从 Set 中移除；
    *   - 对于需求2：需要判断窗口中是否有元素 nums[j] 在 [nums[i]-t, nums[i]+t] 范围内。
    *     - 对于该需求，若 nums 有序，则做这样的判断非常容易 —— 只需判断在 nums 中 nums[i] 的 ceil 是否 <= nums[i]+t，
    *       或 nums[i] 的 floor 是否 >= nums[i]-t。而 nums 的有序性可以通过 TreeSet 实现。
    * - 时间复杂度 O(nlogk)，因为 TreeSet 的操作复杂度为 O(logn)，而 set 中只存 k 个元素；空间复杂度 O(k)。
    * - 注意：因为代码中存在 curr + t，对于第四个 test case 的大整数来说会造成整型溢出。因此需要将 curr 和 t 转成 long
    *   （不过这种问题在面试中很少见）。
    * */
    public static boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        TreeSet<Long> set = new TreeSet<>();  // set 的类型声明必须是 TreeSet 而不能是 Set，否则后面无法调用 ceiling 方法（Set 接口上没有 ceiling 方法）
        for (int i = 0; i < nums.length; i++) {
            long curr = (long) nums[i];
            Long ceiling = set.ceiling(curr), floor = set.floor(curr);  // 获取 curr 在有序的 nums 中的前后元素

            boolean containsDuplicate = (ceiling != null && ceiling <= curr + (long) t) ||
                    (floor != null && floor >= curr - (long) t);
            if (containsDuplicate) return true;

            set.add(curr);
            if (set.size() == k + 1)
                set.remove((long) nums[i - k]);  // 注意这里也要转换成 long
        }
        return false;
    }

    public static void main(String[] args) {
        log(containsNearbyAlmostDuplicate(new int[] {1, 2, 3, 1}, 3, 0));        // expects true
        log(containsNearbyAlmostDuplicate(new int[] {1, 0, 1, 1}, 1, 2));        // expects true
        log(containsNearbyAlmostDuplicate(new int[] {1, 5, 9, 1, 5, 9}, 2, 3));  // expects false
    }
}
