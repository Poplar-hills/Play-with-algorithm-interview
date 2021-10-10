package HashTable.S2_ClassicHashTable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static Utils.Helpers.*;

/*
 * Two Sum
 *
 * - 对于整型数组 nums，返回其中两个不同元素（值相同可以）的索引 i、j 使得 nums[i] + nums[j] = target。
 * - You may assume that each input would have exactly one solution.
 *
 * - 与 3Sum、4Sum 不同之处在于 3Sum、4Sum 要返回的是元素值，而 TwoSum 要返回的是元素索引。
 *
 * - 💎 套路总结：2Sum 问题的三种经典思路：1. 指针对撞；2. Binary Search；3. 查找表。
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
                if (nums[i] + nums[j] == target)
                    return new int[]{i, j};

        throw new IllegalArgumentException("No two sum solution");
    }

    /*
     * 解法2：Sort + 指针对撞
     * - 思路：设想若 nums 是有序数组，则可以通过指针对撞的单次遍历即可求得结果：例如对于 nums=[2, 7, 15, 7], target=14
     *   来说，nums 排序后: [2, 7, 7, 15]
     *                     l        r     - nums[l] + nums[r] > target ∴ r--
     *                     l     r        - nums[l] + nums[r] < target ∴ l++
     *                        l  r        - nums[l] + nums[r] == target
     *   但有一个问题：一旦对 nums 排序，其索原有引会丢失 ∴ 需要一个数据结构在排序之前记录元素和索引的对应关系。而又因为 nums
     *   中可能有重复元素 ∴ 无法采用 Map 记录这种对应关系。换一个思路，不使用外部数据结构，而是将索引与元素以 pair 的方式存在
     *   数组中，这样后面的代码就无需再使用 nums，只用该数组即可。
     * - 时间复杂度 O(nlogn)，空间复杂度 O(n)。
     * */
    public static int[] twoSum2(int[] nums, int target) {
        Pair<Integer, Integer>[] indexedNums = new Pair[nums.length];  // 构建记录 <索引, 元素> 的数组
        for (int i = 0; i < nums.length; i++)
            indexedNums[i] = new Pair<>(i, nums[i]);

        Arrays.sort(indexedNums, Comparator.comparingInt(Pair::getValue));  // 根据元素对 pair 进行排序

        int l = 0, r = indexedNums.length - 1;
        while (l < r) {                         // 开始指针对撞
            Pair<Integer, Integer> lNum = indexedNums[l], rNum = indexedNums[r];
            int sum = lNum.getValue() + rNum.getValue();
            if (sum > target) r--;
            else if (sum < target) l++;
            else return new int[]{lNum.getKey(), rNum.getKey()};  // 返回两个元素在 nums 中的索引
        }

        throw new IllegalArgumentException("No solution");
    }

    /*
     * 解法3：Sort + Binary Search
     * - 思路：在遍历过程中为每个 nums[i] 在 nums(i..] 区间内搜索一个 complement，使得 nums[i] + complement = target。
     *   搜索过程使用二分查找 ∴ 需要先对 nums 排序，而又 ∵ 解法2的分析，若对 nums 排序会丢失原有索引 ∴ 需要构建一个保持了
     *   索引和元素的数组 indexedNums，再对 indexedNums 进行排序、遍历、搜索。
     * - 实现：本解法中不再使用 Pair，直接使用数组 [i, num] 记录每个索引元素对。
     * - 时间复杂度 O(n + nlogn + nlogn)，最终为 O(nlogn)，但实际上是 3-pass；空间复杂度 O(n)。
     * */
    public static int[] twoSum3(int[] nums, int target) {
        int[][] indexedNums = new int[nums.length][];
        for (int i = 0; i < nums.length; i++)
            indexedNums[i] = new int[]{i, nums[i]};  // 二维数组记录索引与元素的对应关系：[[索引0, 元素0], [索引1, 元素1], ...]

        Arrays.sort(indexedNums, (a, b) -> a[1] - b[1]);  // 另一种写法：Comparator.comparingInt(x -> x[1])

        for (int i = 0; i < indexedNums.length; i++) {
            int complement = target - indexedNums[i][1];
            int index = binarySearch(indexedNums, i + 1, nums.length - 1, complement);
            if (index != -1) return new int[]{indexedNums[i][0], indexedNums[index][0]};
        }

        throw new IllegalArgumentException("No solution");
    }

    private static int binarySearch(int[][] nums, int l, int r, int target) {
        if (l > r) return -1;
        int mid = (r - l) / 2 + l;  // 不直接 (r + l) / 2 是为了避免整型溢出
        if (target < nums[mid][1]) return binarySearch(nums, l, mid - 1, target);
        if (target > nums[mid][1]) return binarySearch(nums, mid + 1, r, target);
        return mid;
    }

    /*
     * 解法4：查找表 (two-pass)
     * - 思路：另一类思路是用 Map 来查找每个元素的 complement —— 先构建一个索引 Map<元素, 索引>，然后在遍历 nums 的过程
     *   中查找 nums[i] 的 complement 是否存在于 Map 中，若存在则返回其对应索引。
     * - 注意：∵ test case 3 中存在 target - nums[0] = nums[0] 的情况，而本题要找的是“两个不同元素” ∴ 需要在查找
     *   complement 时判断并排除这种情况。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int[] twoSum4(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++)  // 将 nums 中的所有元素一次性放入 Map 中（若有重复元素则直接覆盖，如 test case 2）
            map.put(nums[i], i);

        for (int i = 0; i < nums.length; i++) {  // 再次遍历数组
            int complement = target - nums[i];
            if (map.containsKey(complement) && map.get(complement) != i)  // 检查索引确保 complement 不等于 nums[i] 自身
                return new int[]{i, map.get(complement)};
        }

        throw new IllegalArgumentException("No two sum solution");
    }

    /*
     * 解法5：查找表 (one-pass，解法4的时间优化版)
     * - 思路：在解法4的基础上进行优化，在遍历到 i 时，若 nums 中存在一个 complement 使得 nums[i] + complement == target，
     *   则在遍历 i 之后的元素时一定会遇到该 complement ∴ 无需像解法3中那样一次性将 nums 的所有元素放入 map，再去挨个检查
     *   map 中是否有 nums[i] 的 complement，只需在一遍遍历过程中一边检查 complement 一边插入元素即可：
     *       例如：nums=[4, -1, -3, 7]，target=6：
     *                  ↑               - complement=2，map 中没有 ∴ 插入 map, map={4:0}
     *                      ↑           - complement=7，map 中没有 ∴ 插入 map, map={4:0, -1:1}
     *                          ↑       - complement=9，map 中没有 ∴ 插入 map, map={4:0, -1:1, -3:2}
     *                             ↑    - complement=-1，map 中有 ∴ 返回找到的解
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int[] twoSum5(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();  // Map<元素, 索引>

        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement))  // 先检查 complement 是否已存在
                return new int[]{map.get(complement), i};
            map.put(nums[i], i);              // 再插入 nums[i]
        }

        throw new IllegalArgumentException("No solution");
    }

    public static void main(String[] args) {
        log(twoSum4(new int[]{2, 7, 15, 11}, 9));   // expects [0, 1]. (2 + 7)
        log(twoSum4(new int[]{3, 3, 4}, 6));        // expects [0, 1]. (3 + 3)
        log(twoSum4(new int[]{3, 2, 4}, 6));        // expects [1, 2]. (2 + 4)
        log(twoSum4(new int[]{4, -1, -3, 7}, 6));   // expects [1, 3]. (-1 + 7)
    }
}
