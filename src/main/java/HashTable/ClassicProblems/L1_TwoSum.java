package HashTable.ClassicProblems;

import javafx.util.Pair;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static Utils.Helpers.log;

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
    * 解法1：排序后指针对撞
    * - 思路：若 nums 是有序数组，则通过指针对撞即可求得结果，因此可以先对 nums 排序。但这样引出另一个问题：排序后 nums 的索引会被
    *   打乱，无法直接使用，因此需要有一个数据结构在排序之前记录下元素和索引的对应关系。
    * - 时间复杂度 O(nlogn)，空间复杂度 O(n)。
    * */
    public static int[] twoSum(int[] nums, int target) {
        Pair<Integer, Integer>[] indexedNums = new Pair[nums.length];
        for (int i = 0; i < nums.length; i++)         // 时间 O(n)，空间 O(n)
            indexedNums[i] = new Pair<>(i, nums[i]);  // 使用 Pair 记录 <索引, 元素> 的对应关系

        Arrays.sort(indexedNums, Comparator.comparingInt(Pair::getValue));  // 用自定义比较器进行排序，时间 O(nlogn)

        int l = 0, r = indexedNums.length - 1;
        while (l < r) {                               // 指针对撞，直到找到和为 target 的两个元素，时间 O(n)
            Pair<Integer, Integer> lNum = indexedNums[l];
            Pair<Integer, Integer> rNum = indexedNums[r];
            if (lNum.getValue() + rNum.getValue() > target) r--;
            else if (lNum.getValue() + rNum.getValue() < target) l++;
            else return new int[]{lNum.getKey(), rNum.getKey()};  // 取得这两个元素在 nums 中的索引并返回
        }
        return null;
    }

    /*
     * 解法2：使用查找表
     * - 思路：将 nums 中的所有元素一次性放入 Map 中，之后遍历数组，若 target - nums[i] 存在于 Map 中，则找到了想要的结果。
     * - 问题：若一次性将所有元素都放入 Map 中，则对于 test cast 2 中 [2, 7, 15, 7] 这样的数组会有问题，因为其中有重复元素7，
     *   在加入 Map 时会产生覆盖，导致找不到正确的索引。
     * - 修正：不一次性将所有元素放入 Map，而是在每次要将元素 nums[i] 放入 Map 之前，先检查已放入 Map 中的元素是否有
     *   target - nums[i]，若有则说明找到了，没有则再继续放入。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int[] twoSum2(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();  // 存储 {元素值: 元素索引} 的映射
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement))
                return new int[]{map.get(complement), i};
            map.put(nums[i], i);
        }
        return null;
    }

    public static void main(String[] args) {
        log(twoSum(new int[]{2, 7, 15, 11}, 9));   // expects [0, 1]. (2+7)
        log(twoSum(new int[]{2, 7, 15, 7}, 14));   // expects [1, 3]. (7+7)
    }
}
