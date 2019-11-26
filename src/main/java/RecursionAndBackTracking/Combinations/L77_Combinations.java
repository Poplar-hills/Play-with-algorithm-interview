package RecursionAndBackTracking.Combinations;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.List;

/*
 * Combinations
 *
 * - Given two integers n and k, return all possible combinations of k numbers out of 1 ... n.
 *
 * - 注：组合不关注顺序，即 [1,2] 和 [2,1] 是同一个组合。
 * */

public class L77_Combinations {
    /*
     * 解法1：
     * - 思路：与排列一样，组合的问题同样可以转化为树形问题求解，例如对于 n=4, k=2 来说，若列出所有的排列组合：
     *                                      []
     *            1/              2|                 3|             4\
     *          [1]               [2]                [3]              [4]
     *      2/  3|  4\        1/  3|  4\         1/  2|  4\       1/  2|  3\
     *   [1,2] [1,3] [1,4] [2,1] [2,3] [2,4] [3,1] [3,2] [3,4] [4,1] [4,2] [4,3]
     *
     *   但 ∵ 组合不关注顺序，[1,2] 和 [2,1] 等价 ∴ 对 [2] 的分支无需再从1开始，只考虑3、4即可，同理对 [3] 的分支只考虑4
     *   即可，而 [4] 不需要考虑任何分支 ∵ 已经在之前的分支中包含了：
     *                                      []
     *                     1/         2|         3|        4\
     *                 [1]            [2]        [3]       [4]
     *             2/  3|  4\       3/  4\       4|
     *          [1,2] [1,3] [1,4] [3,1] [2,4]   [3,4]
     *
     *   这样一来，每次递归中的遍历起点不再是1，而是由上层递归的起点 +1，例如 [] 的遍历范围是 [1..4]；[1] 的遍历范围是 [2..4]；
     *   [2] 的遍历范围是 [3..4]；[3] 的遍历范围是 [4]；而 [4] 的遍历范围就是空（∴ 也不会有解）。
     *
     * - 时间复杂度 O(n^k)，空间复杂度 O(k)。
     * */
    public static List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> res = new ArrayList<>();
        if (n == 0 || k == 0) return res;
        helper(n, k, 1, new ArrayList<>(), res);
        return res;
    }

    private static void helper(int n, int k, int i, List<Integer> list, List<List<Integer>> res) {
        if (list.size() == k) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int j = i; j <= n; j++) {  // 每次递归的遍历范围为 [i..n]
            list.add(j);
            helper(n, k, j + 1, list, res);
            list.remove(list.size() - 1);
        }
    }

    public static void main(String[] args) {
        log(combine(4, 2));  // expects [[2,4],[3,4],[2,3],[1,2],[1,3],[1,4]]
        log(combine(4, 1));  // expects [[1],[2],[3],[4]]
    }
}
