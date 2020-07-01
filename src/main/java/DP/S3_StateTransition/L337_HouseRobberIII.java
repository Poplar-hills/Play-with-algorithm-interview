package DP.S3_StateTransition;

import static Utils.Helpers.*;

import java.util.HashMap;
import java.util.Map;

import Utils.Helpers.TreeNode;

/*
 * House Robber III
 *
 * - 与 L198_HouseRobber 中的条件一样，只是街道为一棵二叉树，问在该街道中，在不触发警报的情况下，最多能盗取多少财产。
 * - 本质：如何在二叉树上选择不相邻的节点，使节点值之和最大。
 * */

public class L337_HouseRobberIII {
    /*
     * 错误解：二维问题转化为一维问题
     * - 思路：最开始想到本题和 L198_HouseRobber 的区别就是二叉树是二维的，而数组是一维的。若能将本题转化为在一维数组上求解则可
     *   直接利用 L198 的解法。而要在二叉树上选择不相邻的节点其实就是选择不相邻的行，使这些行的所有节点之和最大。比如：
     *          3
     *         / \      对每行节点求和
     *        2   2    ------------->  [3, 4, 4]  （从而将问题转化为了类似 L198 的一维数组问题）
     *         \   \
     *          3   1
     *   然而这种方式对于 test case 3 来说不成立，某行中的节点并非要么全偷要么全不偷，而是可能偷左不偷右，或者相反亦可。
     * */

    /*
     * 解法1：DFS + Recursion
     * - 思路：该问题在二叉树上同样具有最优子结构性质 ∴ 可以用 bottom-up 的方式分析：先考虑叶子节点 —— 根据抢或不抢有2种收益。
     *   再考虑其父节点 —— 若抢父节点，则不能抢子节点；若不抢父节点，则父节点上的最大收益为2个子树各自的最大收获之和。以这样的
     *   方式一直推导到根节点就得到了最终解：
     *           1                              12/15
     *          / \    推导每个节点的2种收益       ↗    ↖      取根节点的最大收获
     *         1   5    ---------------->    1/10    5/1    -------------->   15
     *        / \   \                       ↗   ↖       ↖
     *       5   5   1                    5/0   5/0     1/0
     *   对左下角的5来说：若偷则收获为5，否则为0；对其父节点1来说：若偷则最大收获 0+0+1=1，否则为 5+5=10；对根节点1来说：若偷则
     *   最大收获为 1+10+1=12，若不偷则最大收获为 max(1,10) + max(5,1) = 15，因此最终解为15。由此可见：
     *     f(i) = max(y(i), n(i))，其中：
     *       - y(i) = i.val + n(i.l) + n(i.r)；
     *       - n(i) = max(y(i.l), n(i.l)) + max(y(i.r), n(i.r))。
     * - 时间复杂度 O(n)，空间复杂度 O(logn)，n 为节点个数。
     * */
    public static int rob(TreeNode root) {
        int[] res = tryToRob(root);
        return Math.max(res[0], res[1]);
    }

    private static int[] tryToRob(TreeNode node) {
        int[] res = new int[2];
        if (node == null) return res;

        int[] left = tryToRob(node.left);
        int[] right = tryToRob(node.right);

        res[0] = node.val + left[1] + right[1];  // res[0] 记录若偷该节点时的最大收获
        res[1] = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);  // res[1] 记录若不偷该节点时的最大收获

        return res;
    }

    /*
     * 解法2：DP
     * - 思路：∵ 每个节点都有偷/不偷2种选择 ∴ 有：
     *   - 定义子问题：f(i) 表示“以节点 i 为根的二叉树上能抢到的最大收益”；
     *   - 递推表达式：f(i) = max(y(i), n(i))，其中：
     *     - y(i) = i.val + n(i.left) + n(i.right)
     *     - n(i) = max(f(i.left), f(i.right))
     * - 实现：通过给 f 添加 boolean 参数的方式来实现递推表达式中 y、n 两个函数。
     * - 时间复杂度 O(n)，空间复杂度 O(logn)。
     * */
    public static int rob2(TreeNode root) {
        return Math.max(helper2(root, true), helper2(root, false));
    }

    private static int helper2(TreeNode root, boolean shouldRob) {
        if (root == null) return 0;
        return shouldRob
            ? root.val + helper2(root.left, false) + helper2(root.right, false)
            : rob2(root.left) + rob2(root.right);
    }

	/*
     * 解法3：双路 DFS + Recursion
     * - 思路：与解法2一致。
     * - 实现：根据解法2中的 boolean 参数的值将 helper2 方法分成了两个方法 robHouse 和 skipHouse
     * - 时间复杂度 O(n)，空间复杂度 O(logn)。
     * */
    public static int rob3(TreeNode root) {
        if (root == null) return 0;
        return Math.max(robHouse(root), skipHouse(root));
    }

    public static int robHouse(TreeNode node) {
        if (node == null) return 0;
        return node.val + skipHouse(node.left) + skipHouse(node.right);
    }

    public static int skipHouse(TreeNode node) {
        if (node == null) return 0;
        return rob3(node.left) + rob3(node.right);
    }

    /*
     * 解法4：DP + Memoization
     * - 思路：不同于解法2，本解法更 straight-forward，但也更冗长。加入的 Memoization 是以 TreeNode 为 key，因此需要在
     *   TreeNode 类上 @override hashCode 和 equals 方法。
     * - 时间复杂度 O(n)，空间复杂度 O(logn)。
     * */
    public static int rob4(TreeNode root) {
        if (root == null) return 0;
        return helper4(root, new HashMap<>());  // ∵ 二叉树节点个数未知 ∴ 使用 map 实现 memoization
    }

    private static int helper4(TreeNode node, Map<TreeNode, Integer> memo) {
        if (node == null) return 0;
        if (node.left == null && node.right == null) return node.val;
        if (memo.containsKey(node)) return memo.get(node);

        int sum1 = node.val;    // 偷该节点时的最大收获
        if (node.left != null)
            sum1 += helper4(node.left.left, memo) + helper4(node.left.right, memo);
        if (node.right != null)
            sum1 += helper4(node.right.left, memo) + helper4(node.right.right, memo);

        int sum2 = helper4(node.left, memo) + helper4(node.right, memo);  // 不偷该节点时的最大收获

        int sum = Math.max(sum1, sum2);  // 得到子问题解
        memo.put(node, sum);

        return sum;
	}

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{3, 2, 2, null, 3, null, 1});
        log(rob2(t1));
        /*
         *      3
         *     / \
         *    2   2
         *     \   \
         *      3   1
         *
         *  expects 7. (3 + 3 + 1)
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 5, 5, 1, 1, null, 1});
        log(rob2(t2));
        /*
         *        1
         *       / \
         *      5   5
         *     / \   \
         *    1   1   1
         *
         *  expects 10. (5 + 5)
         * */

        TreeNode t4 = createBinaryTreeBreadthFirst(new Integer[]{5, 1, null, 1, null, 5});
        log(rob2(t4));
        /*
         *          5
         *         /
         *        1
         *       /
         *      1
         *     /
         *    5
         *
         *  expects 10. (5 + 5)
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{1, 1, 5, 5, 5, null, 1});
        log(rob2(t3));
        /*
         *        1
         *       / \
         *      1   5
         *     / \   \
         *    5   5   1
         *
         *  expects 15. (5 + 5 + 5)
         * */
    }
}
