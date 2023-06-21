package HashTable.S4_SlidingWindow;

import java.util.Arrays;
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
     * 解法1：查找表 Set + 滑动窗口 + 窗口内基于 TreeSet 进行范围搜索
     * - 思路：💎 分析：题目中的2个限制条件：1是 index constrain；2是 value constrain。∴ 有2种方式来满足这2个条件：
     *   1. Index constrain first：先满足 index constrain，再在其基础上尝试满足条件2 —— 基于 L219_ContainsDuplicateII
     *      解法3的经验可知，可采用限制大小的滑动窗口来满足该条件，然后再在窗口中（满足条件1的元素中）寻找满足条件2的组合。
     *   2. Value constrain first：先满足 value constrain，再在其基础上尝试满足条件1 —— 基于 L1_TwoSum 解法2的经验可知，
     *      可以先对 nums 排序（同时保存索引信息），之后再用滑动窗口来获得一个满足条件2的窗口，最后再在该窗口种寻找满足条件1的组合。
     *   这两种方式中，2的实现复杂度很大：既要建立 indexedNums 数组，又要在根据窗口内的元素 sum 来控制窗口大小 ∴ 采用方式1。
     *   对于方式1来说，要满足条件2，就需要在给定的窗口内进行"范围搜索" —— 给定一个元素 num，判断窗口内是否有另一元素 num2，使得
     *   num2 ∈ [num-t, num+t]。对于该需求，若窗口内的元素有序，则很容易判断 —— 只需判断 num 在该有序窗口中的后一个元素
     *   ceiling(num) 是否 <= num+t，或其前一个元素 floor(num) 是否 >= num-t。至此，问题转化为如何让窗口内的元素有序，并能轻松
     *   获取某一元素的 ceiling、floor —— 这2点都可以通过 TreeSet 实现。
     * - 实现：
     *   1. 用固定大小的 Set 作为窗口，在 nums 上滑动从而获得满足条件1的元素；
     *   2. 用 TreeSet 让窗口内元素基于 value 有序。之后遍历窗口中的元素，判断是否存在另一元素 num2 ∈ [num-t, num+t]，即
     *      ceiling(num) 是否 <= num+t，或 floor(num) 是否 >= num-t；
     *   3. TreeSet 自带的 .ceiling(e) 查找的是 >= e 的最小元素；.floor(e) 查找的是 <= e 的最大元素；
     *   4. ∵ 代码中的 curr + t 对于大整数来说会整型溢出 ∴ 需要将 curr、t 转成 long（不过这种问题在面试中很少见）。
     * - 例子：nums=[4, 1, 2, 6, 4, 0], k=3, t=0：
     *              -                 - 初始窗口
     *              ----              - 扩展窗口，1的 ceiling 为4
     *              -------           - 扩展窗口，2的 ceiling 为4，floor 为1
     *              ----------        - 扩展窗口（达到最大宽度），6没有 ceiling，floor 为4
     *                 ----------     - 滑动窗口，4的 ceiling 和 floor 都为4，满足 4 <= 4+0 ∴ 找到解
     * - 💎 套路：之所以不能给整个 nums 排序，而只是让窗口内的元素在 Set 里有序，是因为若给 nums 排序，则窗口无法再在 nums 上
     *   滑动 ∴ 滑动窗口与整个数组排序通常不会一起使用，但在滑动窗口内排序来进行范围搜索（如本解法）是常见的。
     * - 时间复杂度 O(nlogk) ∵ TreeSet 的操作复杂度为 O(logn)，而 Set 中只存 k 个元素；空间复杂度 O(k)。
     * */
    public static boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        TreeSet<Long> window = new TreeSet<>();  // 类型声明必须是 TreeSet 而非 Set ∵ Set 接口上没有 ceiling, floor 方法

        for (int i = 0; i < nums.length; i++) {  // 使用 Set 作为窗口就不用声明 l、r 指针了，直接遍历元素即可
            // 1. 先在 window 中为 num 进行范围搜索，判断是否满足条件2
            long num = (long) nums[i], T = (long) t;
            Long ceiling = window.ceiling(num), floor = window.floor(num);
            if ((ceiling != null && ceiling <= num + T) || (floor != null && floor >= num - T))
                return true;

            // 2. 若不满足，则滑动窗口
            window.add(num);
            if (window.size() == k + 1)
                window.remove((long) nums[i - k]);  // ∵ TreeSet 里存的是 Long 型 ∴ 在增删改查时都要先将元素转成 long
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
