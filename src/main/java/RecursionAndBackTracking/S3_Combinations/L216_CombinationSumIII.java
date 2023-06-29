package RecursionAndBackTracking.S3_Combinations;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.List;

/*
 * Combination Sum III
 *
 * - Find all possible combinations of k numbers that add up to a number n, given that only numbers from 1 to
 *   9 can be used and each combination should be a unique set of numbers. (从 1-9 中选出 k 个数字，使他们的和为 n)
 *
 * - Notes:
 *   1. All numbers will be positive integers.
 *   2. The solution set must not contain duplicate combinations.
 * */

public class L216_CombinationSumIII {
    /*
     * 解法1：Recursion + Backtracking
     * - 思路：采用回溯搜索求解，例如对于 k=3, n=7 来说：
     *                                      7                      - k=3
     *               1/           2/       3|    4\  5\  6\  7\
     *               6            5         4     3   2   1   0    - k=2
     *        2/ 3/ 4| 5\ 6\  3/ 4| 5\     4|
     *        4  3   2  1  0  2   1  0      0                      - k=1
     *     3/ 4\
     *     1   0                                                   - k=0, 其中 [1,2,4] 使得 n=0 ∴ 是有效解
     * - 时间复杂度 O(C(9,k))：从 1-9 中选出 k 个数，即 C(9,k) = A(9,k) / k! = (9! / (9-k)!) / k! = 9! / (k! * (9-k)!)；
     * - 空间复杂度 O(k)：递归深度为 k。
     * */
    public static List<List<Integer>> combinationSum(int k, int n) {
        List<List<Integer>> res = new ArrayList<>();
        if (k <= 0 || n <= 0) return res;
        backtrack(k, n, 1, new ArrayList<>(), res);
        return res;
    }

    private static void backtrack(int k, int n, int i, List<Integer> list, List<List<Integer>> res) {
        if (n == 0 && k == 0) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int j = i; j <= 9 && j <= n; j++) {  // 在 j ∈ [1,9] 的基础上进一步限制 j ∈ [i,n]
            list.add(j);
            backtrack(k - 1, n - j, j + 1, list, res);
            list.remove(list.size() - 1);
        }
    }

    /*
     * 解法2：Recursion + Backtracking (解法1的优化版)
     * - 思路：与解法1一致。
     * - 实现：在解法1的基础上进行进一步剪枝 —— 例如对于 k=3, n=7 来说：
     *                                      7                      - k=3
     *               1/           2/       3|    4\  5\  6\  7\
     *               6            5         4     3   2   1   0    - k=2
     *        2/ 3/ 4| 5\ 6\  3/ 4| 5\     4|
     *        4  3   2  1  0  2   1  0      0                      - k=1
     *     3/ 4\
     *     1   0                                                   - k=0
     *   当位于节点7，k=3 时，只有1、2是有效分支，而：
     *   - 分支3再往下走会提前让 n 减到0，此时 k 还未减到0 ∴ 无法满足“选出 k 个数字”的条件 ∴ 可知只有
     *     分支值 <= 节点值 / k 的才是有效分支。
     *   - 分支4-7根本无法再往下走 ∵ 没有分支能让 n ≥ 0。
     * - 💎 经验：backtracking 的题目要多考虑是否有其他剪枝的可能性，若题目能用很标准的 backtracking 实现则尤其要注意这点。
     * - 时间复杂度 O(C(9,k))，空间复杂度 O(k)。
     * */
    public static List<List<Integer>> combinationSum2(int k, int n) {
        List<List<Integer>> res = new ArrayList<>();
        if (k <= 0 || n <= 0) return res;
        backtrack2(k, n, 1, new ArrayList<>(), res);
        return res;
    }

    private static void backtrack2(int k, int n, int i, List<Integer> list, List<List<Integer>> res) {
        if (k == 0 && n == 0) {
            res.add(new ArrayList<>(list));
            return;
        }
        if (k == 0 || n == 0) return;  // 例如解法1图中左下角的节点1处 k=0，但 n 还不为0，此时可以提前剪枝
        for (int j = i; j <= 9 && j <= n / k; j++) {  // 在 j ∈ [1,9] 的基础上进一步限制 j ∈ [i,n/k]
            list.add(j);
            backtrack2(k - 1, n - j, j + 1, list, res);
            list.remove(list.size() - 1);
        }
    }

    public static void main(String[] args) {
        log(combinationSum2(2, 5));   // expects [[1,4], [2,3]]
        log(combinationSum2(3, 7));   // expects [[1,2,4]]
        log(combinationSum2(3, 9));   // expects [[1,2,6], [1,3,5], [2,3,4]]
        log(combinationSum2(2, 18));  // expects []
    }
}
