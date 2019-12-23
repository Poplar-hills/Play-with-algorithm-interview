package HashTable.S2_ClassicHashTable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static Utils.Helpers.*;

/*
 * Two Sum
 *
 * - 对于整型数组 nums，返回其中两个不同元素（值相同可以）的索引 i 和 j 使得 nums[i] + nums[j] = target。
 * - You may assume that each input would have exactly one solution.
 *
 * - 与 3Sum、4Sum 不同之处在于 3Sum、4Sum 要返回的是元素值，而 TwoSum 要返回的是元素索引。
 * */

public class L1_TwoSum {
    /*
     * 解法1：Brute Force
     * - 思路：暴力破解的思路就是用 nums 中的每一个元素与 nums 中的所有其他元素逐一配对，看他们的和是否等于 target。
     * - 时间复杂度 O(n^2)，空间复杂度 O(1)。
     * */
    public static int[] twoSum(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++)
            for (int j = i + 1; j < nums.length; j++)
                if (nums[j] == target - nums[i])
                    return new int[] {i, j};
        throw new IllegalArgumentException("No two sum solution");
    }

    /*
     * 解法2：Sort + 指针对撞
     * - 思路：设想若 nums 是有序数组，则可以通过指针对撞的单次遍历即可求得结果：例如对于 nums=[2, 7, 15, 7], target=14
     *   来说，nums 排序后: [2, 7, 7, 15]
     *                     l        r     - nums[l] + nums[r] > target ∴ r--
     *                     l     r        - nums[l] + nums[r] < target ∴ l++
     *                        l  r        - nums[l] + nums[r] == target
     *   但有一个问题：一旦对 nums 排序，其索引会被打乱而无法再次使用 ∴ 需要有一个数据结构在排序之前记录下元素和索引的对应关系。
     *   而又因为 nums 中可能有重复元素 ∴ 无法采用 map 记录这种对应关系，
     * - 时间复杂度 O(nlogn)，空间复杂度 O(n)。
     * */
    public static int[] twoSum2(int[] nums, int target) {
        Pair<Integer, Integer>[] indexedNums = new Pair[nums.length];
        for (int i = 0; i < nums.length; i++)
            indexedNums[i] = new Pair<>(i, nums[i]);  // 让 indexedNums 中的每个元素记录 <索引, 元素>

        Arrays.sort(indexedNums, Comparator.comparingInt(Pair::getValue));  // O(nlogn)

        int l = 0, r = indexedNums.length - 1;
        while (l < r) {                               // 指针对撞，直到找到和为 target 的两个元素
            Pair<Integer, Integer> lNum = indexedNums[l];
            Pair<Integer, Integer> rNum = indexedNums[r];
            if (lNum.getValue() + rNum.getValue() > target) r--;
            else if (lNum.getValue() + rNum.getValue() < target) l++;
            else return new int[]{lNum.getKey(), rNum.getKey()};  // 取这两个元素在 nums 中的索引并返回
        }
        throw new IllegalArgumentException("No two sum solution");
    }

    /*
     * 解法3：Memoization (Two-pass)
     * - 思路：另一类思路是用 map 来查找每个元素的 complement —— 先构建一个 map 将 nums 中的元素存储成 <元素, 索引> 的形式，
     *   然后在遍历 nums 的过程中查找 nums[i] 的 complement 是否存在于 map 中，若存在则找到了结果。
     * - 注意：∵ test case 3 中存在 target - nums[0] = nums[0]（即 nums[0] 的 complement 还是 nums[0]）的情况，而本
     *   题要找的是“两个不同元素” ∴ 该情况不是有效解 ∴ 需要在查找 complement 时加入判断排除这种情况才行。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int[] twoSum3(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < nums.length; i++)  // 将 nums 中的所有元素一次性放入 Map 中（若有重复元素则直接覆盖，如 test case 2）
            map.put(nums[i], i);

        for (int i = 0; i < nums.length; i++) {  // 再次遍历数组
            int complement = target - nums[i];
            if (map.containsKey(complement) && map.get(complement) != i)  // 检查索引确保 complement 不等于 nums[i] 自身
                return new int[] {i, map.get(complement)};
        }
        throw new IllegalArgumentException("No two sum solution");
    }

    /*
     * 解法4：Memoization (One-pass) (解法2的时间优化版)
     * - 思路：在解法3的基础上进行优化，若 nums 中存在 nums[i] + complement == target，则在遍历 nums 的过程中一定会先后
     *   遇到 nums[i] 和 complement ∴ 我们不需要像解法3中那样一次性将 nums 的所有元素放入 map，一边检查 nums[i] 的
     *   complement 是否已经存在于 map 中，只需一边向 map 中插入 nums[i] 即可。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int[] twoSum4(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();  // 存储 <元素, 元素索引>

        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement))          // 先检查 complement 是否已存在
                return new int[] {map.get(complement), i};
            map.put(nums[i], i);                      // 再插入 nums[i]
        }
        throw new IllegalArgumentException("No two sum solution");
    }

    public static void main(String[] args) {
        log(twoSum2(new int[]{2, 7, 15, 11}, 9));   // expects [0, 1]. (2 + 7)
        log(twoSum2(new int[]{2, 7, 15, 7}, 14));   // expects [1, 3]. (7 + 7)
        log(twoSum2(new int[]{3, 2, 4}, 6));        // expects [1, 2]. (2 + 4)
    }
}
