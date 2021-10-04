package HashTable.S4_SlidingWindow;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static Utils.Helpers.log;

/*
 * Contains Duplicate III
 *
 * - Given an integer array nums and two integers k and t, return true if there are two distinct indices i and j
 *   in the array such that:
 *     1. abs(i - j) <= k
 *     2. abs(nums[i] - nums[j]) <= t
 * */

public class L220_ContainsDuplicateIII {
    /*
     * 解法1：查找表 Set + 滑动窗口 + 窗口内范围搜索
     * - 思路：大体思路仍可采用 L219_ContainsDuplicateII 解法2，即 Set 作查找表 + 滑动窗口：
     *   - 对于题目需求1：用 Set 作为窗口，固定大小，只保存 k 个元素，在 nums 上滑动；
     *   - 对于题目需求2：换一种表达方式就是，给定元素 num，判断是否窗口中是否存在另一元素 num2 ∈ [num-t, num+t]。
     *     > 对于该需求，若窗口内的元素有序，则非常容易判断，只需判断 num 在有序窗口中的后一个元素 ceiling(nums) 是否 <= num+t，
     *       或其前一个元素 floor(num) 是否 >= num-t。
     *     > 例如对于 nums=[4, 1, 2, 6, 4, 0], k=3, t=0 来说，
     *                     -                 - 初始窗口
     *                     ----              - 扩展窗口，1的 ceiling 为4
     *                     -------           - 扩展窗口，2的 ceiling 为4，floor 为1
     *                     ----------        - 扩展窗口（达到最大宽度），6没有 ceiling，floor 为4
     *                        ----------     - 滑动窗口，4的 ceiling 和 floor 都为4，满足 4 <= 4+0 ∴ 找到解
     * - 实现：
     *   1. 窗口内元素的有序性可以通过 TreeSet 实现。
     *   2. TreeSet 自带的 .ceiling(e) 查找的是 >= e 的最小元素；.floor(e) 查找的是 <= e 的最大元素。
     *   3. ∵ 代码中的 curr + t 对于大整数来说会整型溢出 ∴ 需要将 curr、t 转成 long（不过这种问题在面试中很少见）。
     *
     * - 💎 套路：之所以不能给整个 nums 排序，而只是让窗口内的元素在 Set 里有序，是因为若给 nums 排序，则窗口无法再在 nums 上
     *   滑动 ∴ 滑动窗口与整个数组排序通常不会一起使用，但在滑动窗口内排序来进行范围搜索（如本解法）是常见的。
     *
     * - 时间复杂度 O(nlogk) ∵ TreeSet 的操作复杂度为 O(logn)，而 Set 中只存 k 个元素；空间复杂度 O(k)。
     * */
    public static boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        TreeSet<Long> window = new TreeSet<>();  // 类型声明必须是 TreeSet 而非 Set ∵ Set 接口上没有 ceiling, floor 方法

        for (int i = 0; i < nums.length; i++) {  // 使用 Set 作为窗口就不用声明 l、r 指针了，直接逐个遍历元素即可
            long num = (long) nums[i], T = (long) t;
            Long ceiling = window.ceiling(num), floor = window.floor(num);  // 获取 num 在窗口内的 ceiling, floor

            if ((ceiling != null && ceiling <= num + T) || (floor != null && floor >= num - T))
                return true;

            window.add(num);
            if (window.size() == k + 1)
                window.remove((long) nums[i - k]);  // ∵ TreeMap 里存的是 Long 型 ∴ 在增删改查时都要先将元素转成 long
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
