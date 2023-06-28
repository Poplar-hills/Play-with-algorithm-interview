package RecursionAndBackTracking.S3_Combinations;

import static Utils.Helpers.*;

import java.util.*;

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
 *
 * - 💎 注意：排列与组合中对于"重复解"的定义：
 *   - 在 L46_Permutations、L47_PermutationsII 中也要求不能有重复解，但具体是指不能有重复的排列，而 [1,2,1] 与 [2,1,1]
 *     就属于不同的排列 ∴ 不是重复解；
 *   - 而在 L39_CombinationSum、L40_CombinationSumII 中，不重复的解是指不能有重复的组合，而 [1,2,1] 与 [2,1,1] 就属于
 *     重复的组合 ∴ 要避免。
 * */

public class L40_CombinationSumII {
    /*
     * 解法1：Recursion + Backtracking
     * - 思路：将组合问题转化为树形问题，采用回溯搜索求解。
     * - 💎 实现：L39_CombinationSum 中的限制是不能产生重复解，而该题中除了该限制之外还多了不能重复使用 num 的限制。因此：
     *   a). 为了不重复使用元素，有2种方法：
     *     1. 使用 boolean[] used 来排除使用过的元素（L46_Permutations 解法2、L47_PermutationsII 解法4、5）；
     *     2. 在每层分支时不回头，让下层递归在遍历 nums 时的起始位置+1（本题本解法）；
     *   b). 为了不产生重复解（重复的组合），有2种方法：
     *     1. 在最后用 Set 对解去重（复杂度高）；
     *     2. 提前对 nums 排序，在遍历元素进行分支时跳过相同的元素（L47_PermutationsII 解法5、本题本解法）。
     * - 例：对于 nums=[2,5,2,1,2], target=5 来说，排序后的 nums=[1,2,2,2,5]，于是有：
     *                              5
     *            1/         2/    2|    2\     5\    - 遍历 nums[0..)
     *            4          3      ×     ×      0    - 跳过重复元素（第二、三个2）
     *      2/ 2| 2| 5\  2/ 2| 5\                     - 对4分支时跳过0号元素，对3分支时跳过0、1号元素（∵ nums 中的元素都只能使用一次）
     *      2   ×  ×  ×  1   ×  ×
     *     2|         2/2|5\
     *      0         ×  ×  ×
     * - 💎 注意：类似 L47_PermutationsII 解法4中使用 inner Set 在遍历元素进行分支时跳过相同的元素的方式能避免重复的排列，
     *   但仍会产出重复的组合 ∴ 只能用于 permutation 的题目，而不能用于 combination 的题目中。
     *                              5
     *              2/       5/    2|    1\     2\
     *              3        0      ×     4      ×   - 通过 Set 去重，5节点的第二、三个2分支被剪枝
     *        5/ 2/ 1\ 2\                2|
     *        ×  1   2  ×                 2          - 通过 Set 去重，3节点的第二个2分支被剪枝（到这里一切正常）
     *         1/2\ 2|
     *         0  ×  0                               - 但到这里会得到2个解：[2,2,1]、[2,1,2]，属于重复的组合 ∴ 该解法有误
     * - 💎 总结：对于"不能产生重复解"的要求：
     *   - 在 permutation 的题目中可使用 inner Set 去重的方式；
     *   - 在 combination 的题目中需预先 sort + 在遍历元素进行分支时跳过相同的元素的方式。
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
            if (j > i && nums[j] == nums[j - 1]) continue;  // 跳过重复元素（j>i 表示从第二个元素开始判断，而 j=i 是本层第一个遍历到的元素）
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
