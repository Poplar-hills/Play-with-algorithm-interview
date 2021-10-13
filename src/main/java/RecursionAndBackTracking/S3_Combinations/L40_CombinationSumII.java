package RecursionAndBackTracking.S3_Combinations;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Combination Sum II
 *
 * - Given a collection of numbers and a target number, find all unique combinations in the numbers
 *   where the nums sum to the target.
 *
 * - Notes:
 *   1. Each number may only be used once in the combination.
 *   2. All numbers (including target) will be positive integers.
 *   3. The solution set must not contain duplicate combinations.
 *
 * - L39_CombinationSum：nums 中的所有元素都是唯一的，且可以使用无限多次，但结果集中不能有重复解；
 * - L40_CombinationSumII：nums 中的元素可能重复，且都只能使用一次，但结果集中不能有重复解。
 * */

public class L40_CombinationSumII {
    /*
     * 解法1：Recursion + Backtracking
     * - 思路：将组合问题转化为树形问题，采用回溯搜索求解。
     * - 💎 实现：L39_CombinationSum 中的限制是不能产生重复解，而该题中除了该限制之外还多了不能重复使用 num 的限制。因此：
     *   a). 为了不重复使用元素，有2种方法：
     *     1. 使用 boolean[] used 来排除使用过的元素（L46_Permutations 解法2、L47_PermutationsII 解法4、5）；
     *     2. 在每层分支时不回头，让下层递归在遍历 nums 时的起始位置+1（本题本解法）；
     *   b). 为了不产生重复解，有2种方法：
     *     1. 在最后用 Set 对解去重（复杂度高）；
     *     2. 使用 inner Set 在遍历元素进行分支时跳过相同的元素（L47_PermutationsII 解法4）。
     *     3. 提前对 nums 排序，在遍历元素进行分支时跳过相同的元素（L47_PermutationsII 解法5、本题本解法）。
     *   例如，对于 nums=[2,5,2,1,2], target=5 来说，排序后的 nums=[1,2,2,2,5]，于是有：
     *                                  5
     *                   1/      2/    2|    2\     5\    - 遍历 nums[0..)
     *                   4       3      ×     ×      0    - 跳过重复元素（第二个2）
     *             2/ 2| 2| 5\  2|                        - 跳过0号元素，遍历 nums[1..) ∵ nums 中的元素都只能使用一次
     *             2   ×  ×  ×   1
     *            2|         2/ 2| 5\
     *             0         ×   ×   ×
     * - 时间复杂度 << O(n^n)，空间复杂度 O(target)。
     * */
    public static List<List<Integer>> combinationSum(int[] nums, int target) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length == 0) return res;
        Arrays.sort(nums);
        backtrack(nums, target, 0, new ArrayList<>(), res);
        return res;
    }

    private static void backtrack(int[] nums, int target, int i, List<Integer> list, List<List<Integer>> res) {
        if (target == 0) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int j = i; j < nums.length && target >= nums[j]; j++) {
            if (j > i && nums[j] == nums[j - 1]) continue;  // 跳过重复元素（注意是 j>i，而非 j>0 ∵ j=i 是本层第一个遍历到的元素 ∴ j>i 表示从第二个元素开始）
            list.add(nums[j]);
            backtrack(nums, target - nums[j], j + 1, list, res);  // j 号元素在本层已使用过 ∴ 下层递归从第 j+1 号元素开始遍历（这是与 L39 不同之处）
            list.remove(list.size() - 1);
        }
    }

    public static void main(String[] args) {
        log(combinationSum(new int[]{2, 5, 2, 1, 2}, 5));         // expects [[1,2,2], [5]]
        log(combinationSum(new int[]{10, 1, 2, 7, 6, 1, 5}, 8));  // expects [[1,7], [1,2,5], [2,6], [1,1,6]]
    }
}
