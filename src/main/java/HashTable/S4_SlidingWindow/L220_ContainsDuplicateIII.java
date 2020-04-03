package HashTable.S4_SlidingWindow;

import java.util.TreeSet;

import static Utils.Helpers.log;

/*
 * Contains Duplicate III
 *
 * - 对于整型数组 nums 和整数 k，是否存在索引 i 和 j，使得：
 *   1. nums[i] 与 nums[j] 的差不超过整数 t；
 *   2. i 和 j 之间的差不超过整数 k。
 *
 * - 公式化题中的两个条件：
 *   1. |nums[i] - nums[j]| <= t
 *   2. |i - j| <= k
 * */

public class L220_ContainsDuplicateIII {
    /*
     * 解法1：查找表 Set + 滑动窗口
     * - 思路：大体思路仍可采用 L219_ContainsDuplicateII 解法2，即 Set 作查找表 + 滑动窗口：
     *   - 对于需求1：用 Set 作为窗口，固定大小，只保存 k 个元素，在 nums 上滑动；
     *   - 对于需求2：换一种表达方式就是，判断窗口中是否存在元素 nums[j] ∈ [nums[i]-t, nums[i]+t]。
     *     > 对于该需求，若窗口内的元素有序，则非常容易判断，只需判断 nums[i] 在有序窗口中的后一个元素（ceiling）是否 <= nums[i]+t，
     *       或前一个元素（floor）是否 >= nums[i]-t。
     *     > 例如对于 nums=[4, 1, 2, 6, 4, 0], k=3, t=0 来说，
     *                     -
     *                     ----              - 1的 ceiling 为4
     *                     -------           - 2的 ceiling 为4，floor 为1
     *                     ----------        - 6的 ceiling 没有，floor 为4
     *                       -----------     - 4的 ceiling 和 floor 都为4，且 <= 4+0 ∴ return true
     * - 实现：
     *   1. 窗口内元素的有序性可以通过 TreeSet 实现。
     *   2. TreeSet 自带的 ceiling 查找的是 >= 目标元素的最小元素；floor 查找的是 <= 目标元素的最大元素。
     *   3. ∵ 代码中的 curr + t 对于大整数来说会整型溢出 ∴ 需要将 curr、t 转成 long（不过这种问题在面试中很少见）。
     *
     * - 💎 套路：之所以不能给整个 nums 排序，而只是让窗口内的元素在 Set 里有序，是因为若给 nums 排序则窗口无法再在 nums 上
     *   滑动 ∴ 滑动窗口与整个数组排序通常不会一起使用，但在滑动窗口内排序来进行范围搜索（如本解法）是常见的。
     *
     * - 时间复杂度 O(nlogk) ∵ TreeSet 的操作复杂度为 O(logn)，而 Set 中只存 k 个元素；空间复杂度 O(k)。
     * */
    public static boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        TreeSet<Long> set = new TreeSet<>();      // 类型声明必须是 TreeSet 而不能是 Set，否则后面无法调用 ceiling, floor 方法
                                                  // （Set 接口上没有 ceiling, floor 方法）
        for (int i = 0; i < nums.length; i++) {
            long curr = (long) nums[i];
            Long ceiling = set.ceiling(curr), floor = set.floor(curr);  // 获取 curr 在窗口内的 ceiling, floor

            boolean containsDuplicate = (ceiling != null && ceiling <= curr + (long) t)
                || (floor != null && floor >= curr - (long) t);
            if (containsDuplicate) return true;

            set.add(curr);
            if (set.size() == k + 1)
                set.remove((long) nums[i - k]);  // ∵ TreeMap 里存的是 Long 型 ∴ 在增删改查时都要先将元素转成 long
        }

        return false;
    }

    public static void main(String[] args) {
        log(containsNearbyAlmostDuplicate(new int[]{1, 2, 3, 1}, 3, 0));
        // expects true. (i=0,j=3)

        log(containsNearbyAlmostDuplicate(new int[]{1, 0, 1, 1}, 1, 2));
        // expects true. (i=0,j=1 or i=1,j=2 or i=2,j=3)

        log(containsNearbyAlmostDuplicate(new int[]{1, 5, 9, 1, 5, 9}, 2, 3));
        // expects false.
    }
}
