package RecursionAndBackTracking.S3_Combinations;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.List;

/*
 * Combinations
 *
 * - Given two integers n and k, return all possible combinations of k numbers out of 1...n.
 *
 * - 注：组合不关注顺序，即 [1,2] 和 [2,1] 是同一个组合。
 * */

public class L77_Combinations {
    /*
     * 解法1：Recursion + Backtracking
     * - 思路：与排列一样，组合的问题同样可以转化为树形问题求解，例如对于 n=4, k=2 来说，若列出所有的排列组合：
     *                                        []
     *              1/                2/              3\              4\
     *            [1]               [2]                [3]              [4]
     *        2/  3|  4\        1/  3|  4\         1/  2|  4\       1/  2|  3\
     *     [1,2] [1,3] [1,4] [2,1] [2,3] [2,4] [3,1] [3,2] [3,4] [4,1] [4,2] [4,3]
     *
     *   但 ∵ 组合不关注顺序，[1,2] 和 [2,1] 等价 ∴ 对 [2] 的分支无需再从1开始，只考虑3、4即可，同理对 [3] 的分支只考虑4
     *   即可，而 [4] 不需要考虑任何分支 ∵ 已经在之前的分支中包含了：
     *                                      []
     *                     1/          2/       3\       4\
     *                 [1]            [2]        [3]       [4]
     *             2/  3|  4\       3/  4\       4|
     *          [1,2] [1,3] [1,4] [3,1] [2,4]   [3,4]
     *
     *   这样一来，每次递归中的遍历起点不再是1，而是由上层递归的起点+1，例如 [] 的遍历范围是 [1..4]；[1] 的遍历范围是 [2..4]；
     *   [2] 的遍历范围是 [3..4]；[3] 的遍历范围是 [4]；而 [4] 的遍历范围就是空（∴ 不会有解）。
     *
     *   而另一种思考方式是：
     *                              从[1..4]中取2个
     *              1/             2/           3\          4\
     *       从[2..4]中取1个   从[3..4]中取1个  从[4]中取1个  从[]中取1个
     *        2/   3|   4\       3/  4\          4|
     *      [1,2] [1,3] [1,4]  [2,3] [2,4]      [3,4]
     *
     *   可见该过程本身就是递归的 —— 将大问题拆解成小问题。
     *
     * - 时间复杂度 O(n^k)：解释同 L46_Permutations 解法3 —— 树一共 k 层，每个节点最多有 n 个分支 ∴ 是 O(n^k)。
     * - 空间复杂度 O(k)。
     * */
    public static List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> res = new ArrayList<>();
        if (n <= 0 || k <= 0 || k > n) return res;
        backtrack(n, k, 1, new ArrayList<>(), res);  // i 从1开始
        return res;
    }

    private static void backtrack(int n, int k, int i, List<Integer> list, List<List<Integer>> res) {
        if (list.size() == k) {         // 或 i > k 也可以作为递归到底的条件
            res.add(new ArrayList<>(list));
            return;
        }
        for (int j = i; j <= n; j++) {  // 每次递归的遍历范围为 [i..n]
            list.add(j);
            backtrack(n, k, j + 1, list, res);
            list.remove(list.size() - 1);
        }
    }

    /*
     * 解法2：Recursion + Backtracking + 剪枝
     * - 思路：解法1中，[4] 不会再有分支，是一条死胡同，但我们在上一层递归中仍然对 [] 尝试了分支4的操作，这是不必要的 ∴ 可以
     *   通过对回溯进行“剪枝”来优化，即通过限制每次遍历的范围来“剪掉”没有解的分支。具体来说 ∵ k 是一个解所需的元素个数，而
     *   list.size() 是一个解已有的元素个数 ∴ k-list.size() 就是一个解还需的元素个数，而该个数 <= [j..n] 中的元素个数
     *   才能保证分支 j 上有解 ∴ k - list.size() <= n - j + 1，即 j <= n - (k - list.size()) + 1。
     * - 时间复杂度 O(n^k)，空间复杂度 O(k)。
     * */
    public static List<List<Integer>> combine2(int n, int k) {
        List<List<Integer>> res = new ArrayList<>();
        if (n <= 0 || k <= 0 || k > n) return res;
        backtrack2(n, k, 1, new ArrayList<>(), res);
        return res;
    }

    private static void backtrack2(int n, int k, int i, List<Integer> list, List<List<Integer>> res) {
        if (list.size() == k) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int j = i; j <= n - (k - list.size()) + 1; j++) {  // 通过限制 j 的上限进行剪枝
            list.add(j);
            backtrack2(n, k, j + 1, list, res);
            list.remove(list.size() - 1);
        }
    }

    public static void main(String[] args) {
        log(combine(4, 2));  // expects [[2,4],[3,4],[2,3],[1,2],[1,3],[1,4]]
        log(combine(4, 1));  // expects [[1],[2],[3],[4]]
    }
}
