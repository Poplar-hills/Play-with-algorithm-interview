package HashTable.HashTableAndSlidingWindow;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import static Utils.Helpers.log;

/*
* Contains Duplicate III
*
* - 对于整型数组 nums 和整数 k，是否存在索引 i 和 j，使得 nums[i] 与 nums[j] 的差距不超过给定的整数 t，
*   且 i 和 j 之间的差不超过给定的整数 k。
*
* - 分析可知本题中的两个限制条件：
*   1. |i - j| <= k
*   2. |nums[i] - nums[j]| <= t
*
* - 思路：仍然可以采用 L219_ContainsDuplicateII 中解法2的实现方式，即用 Set 作查找表 + 滑动窗口。具体来说：
*   - 对于限制1来说，与 L219_ContainsDuplicateII 的解法2一样（用 Set 作为 window，将差距超过 k 的元素从 Set 中移除）；
*   - 对于限制2来说，需要判断窗口中是否存在 nums[j] 使得 nums[i]-t <= nums[j] <= nums[i]+t。若 nums 有序，则进行这样的判断会
*     非常容易 —— 只需判断元素 nums[i] 在 nums 中的 ceil 是否小于 nums[i] + t，或 floor 是否大于 nums[i] - t。
* */

public class L220_ContainsDuplicateIII {
    /*
    * 解法1：
    * - 时间复杂度 O(nlogk)，空间复杂度 O(k)，因为 TreeSet 的操作复杂度为 O(logn)，而 set 中只存 k 个元素。
    * - 注意：因为代码中存在 next + t，对于第四个 test case 的大整数来说会造成整型溢出。因此需要将 next 和 t 转成 long
    *   （不过这种问题在面试中很少见）。
    * */
    public static boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        TreeSet<Long> set = new TreeSet<>();  // set 的类型声明必须是 TreeSet 而不能是 Set，否则后面 ceiling 方法取不到（Set 接口上没有 ceiling 方法）
        for (int i = 0; i < nums.length; i++) {
            long next = (long) nums[i];
            Long ceiling = set.ceiling(next), floor = set.floor(next);
            boolean containsAlmostDuplicate = (ceiling != null && ceiling <= next + (long) t) ||
                    (floor != null && floor >= next - (long) t);
            if (containsAlmostDuplicate) return true;
            set.add(next);
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
