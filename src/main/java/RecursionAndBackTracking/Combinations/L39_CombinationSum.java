package RecursionAndBackTracking.Combinations;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * Combination Sum
 *
 * - Given a set of unique candidate numbers and a target number, find all the unique combinations in the
 *   candidate numbers that sums to the target number.
 *
 * - Notes:
 *   1. The same repeated number may be chosen from candidates unlimited number of times.
 *   2. All numbers (including target) will be positive integers.
 *   3. The solution set must not contain duplicate combinations.
 * */

public class L39_CombinationSum {
    /*
     * 解法1：Recursion + Backtracking + Set
     * - 思路：尝试用 candidates 中的每一个元素对 target 进行递归分解，其中：
     *     1. 若 target 减去一个 candidate 之后等于0则找到一个解；
     *     2. candidate 必须 <= target 才可以相减。
     *                              8
     *               2/            3|            5\
     *               6              5              3
     *         2/   3|   5\    2/  3|  5\        2/ 3\
     *         4     3    1    3    2    0       1   0     - 找到 [3,5]、[5,3]
     *       2/ 3\ 2/ 3\     2/ 3\ 2|
     *       2   1 1   0     1   0  0                      - 找到 [2,3,3]、[3,2,3]、[3,3,2]
     *      2|
     *       0                                             - 找到 [2,2,2,2]
     *   但 ∵ 组合不关注顺序（如 [3,5] 和 [5,3] 等价）∴ 需对找到的解进行去重，在找到解时先对其中的元素进行排序，再用 Set 去重。
     * - 时间复杂度 O(n^n)，空间复杂度 O(???)。
     * */
    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        Set<List<Integer>> set = new HashSet<>();
        if (candidates == null || candidates.length == 0) return new ArrayList<>();
        helper(candidates, target, new ArrayList<>(), set);
        return new ArrayList<>(set);
    }

    private static void helper(int[] candidates, int target, List<Integer> list, Set<List<Integer>> set) {
        if (target == 0) {
            List<Integer> newList = new ArrayList<>(list);
            newList.sort((a, b) -> a - b);  // 先对解中元素排序
            set.add(newList);               // 再通过 Set 去重
            return;
        }
        for (int c : candidates) {
            if (target < c) continue;
            list.add(c);
            helper(candidates, target - c, list, set);
            list.remove(list.size() - 1);
        }
    }

    /*
     * 解法2：Recursion + Backtracking +
     * - 思路：解法1是在找到所有解之后再进行去重，而更优的做法是根本不产生重复的解，即需要对树进行剪枝以避免进入产生重复解的分支。
     *   根据该思路，可以采用 L77_Combinations 解法1中的方式 —— 让每个节点在遍历 candidates 时不回头，只遍历 >= 当前分支
     *   的 candidates ∴ 解法1中的树会被剪成这样：
     *                            8
     *               2/          3|          5\
     *               6            5            3     - 5节点不再考虑分支2，只考虑 >= 3 的分支；3节点不再考虑分支2、3，且5的分支无效 ∴ 无解
     *         2/   3|   5\    3/  5\
     *         4     3    1    2    0                - 3节点不再考虑分支2，只考虑 >= 3 的分支；2节点不在的考虑分支2；找到 [3,5]
     *       2/ 3\  3|
     *       2   1   0                               - 找到 [2,3,3]
     *      2|
     *       0                                       - 找到 [2,2,2,2]
     *   注意，这种方式依赖于 candidates 有序 ∴ 需要在最开始对其进行排序。
     * - 时间复杂度 << O(n^n)，空间复杂度 O(target)。
     * */
    public static List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        if (candidates == null || candidates.length == 0) return res;
        Arrays.sort(candidates);
        helper2(candidates, target, 0, new ArrayList<>(), res);
        return res;
    }

    private static void helper2(int[] candidates, int target, int i, List<Integer> list, List<List<Integer>> res) {
        if (target == 0) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int j = i; j < candidates.length && target >= candidates[j]; j++) {
            list.add(candidates[j]);
            helper2(candidates, target - candidates[j], j, list, res);
            list.remove(list.size() - 1);
        }
    }

    public static void main(String[] args) {
        log(combinationSum2(new int[]{2, 3, 6, 7}, 7));  // expects [[7], [2,2,3]]
        log(combinationSum2(new int[]{2, 7, 3, 6}, 7));  // expects [[7], [2,2,3]]
        log(combinationSum2(new int[]{2, 3, 5}, 8));     // expects [[2,2,2,2], [2,3,3], [3,5]]
        log(combinationSum2(new int[]{3}, 8));           // expects []
    }
}
