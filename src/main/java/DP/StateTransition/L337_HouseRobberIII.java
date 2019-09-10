package DP.StateTransition;

import static Utils.Helpers.*;

import java.util.HashMap;

import Utils.Helpers.TreeNode;

/*
* House Robber III
*
* - 基本与 L198_HouseRobber 中的条件一样，只是本题中的街道为一棵二叉树，问在该街道中，在不触发警报的情况下，最多能盗取多少财产。
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
    * 解法1：
    * - 思路：该问题在二叉树上同样具有最优子结构性质 ∴ 可以以 bottom-up 的方式分析：先思考一个节点，对于每一个节点都可以偷或
    *   不偷，因此有2种所得。而对其父节点来说：1.若偷父节点，则不能偷子节点；2.若不偷父节点，则最大所得为2个子节点各自的最大所得
    *   之和。以这样的方式一直推导到根节点记得到最终解。
    *           1                             12/15
    *          / \    推导每个节点的2种所得      /    \      取根节点的最大所得
    *         1   5    ---------------->    1/10   5/1    -------------->   15
    *        / \   \                       /   \     \
    *       5   5   1                    5/0   5/0   1/0
    *   对左下角的5来说：若偷则所得为5，否则为0；对其父节点1来说：若偷则最大所得1，否则为10；对根节点1来说：若偷则最大所得为
    *   1+10+1 = 12，若不偷则最大所得为 max(1,10) + max(5,1) = 15，因此最终解为15。
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

        res[0] = node.val + left[1] + right[1];  // res[0] 记录若偷该节点得到的最大收获
        res[1] = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);  // res[1] 记录若不偷该节点得到的最大收获

        return res;
    }

    /*
    * 解法2：Memoization
    * - 思路：∵ 每个节点都有偷/不偷2种选择 ∴ 有：
    *   - 定义子问题：f(i) 表示“以节点 i 为根的二叉树上能获得的最大收获”；
    *   - 状态转移方程：f(i) = max(nums[i] + f(!i.l) + f(!i.r), f(i.l) + f(i.r))，其中 ! 表示不偷某个节点上的房子；
    *     解释：nums[i] + f(!i.l) + f(!i.r) 是偷节点 i 时的最大收获；f(i.l) + f(i.r) 是不偷节点 i 时的最大收获；
    * - 实现：要实现状态转移方程中的“!”，可以通过给给 f 添加 boolean 参数实现。
    * - 时间复杂度 O(n)，空间复杂度 O(logn)。
    * */
    public static int rob2(TreeNode root) {
        if (root == null) return 0;
        return helper2(root, true);  // 相当于 Math.max(helper2(root, true), helper2(root, false));
    }

    private static int helper2(TreeNode node, boolean included) {  // 求以节点 node 为根的二叉树上能获得的最大收获
        if (node == null) return 0;
        int res = helper2(node.left, true) + helper2(node.right, true);  // 不偷该节点时的最大收获
        if (included) {                                                  // 考虑该节点并不意味着一定要偷该节点
            int steal = node.val + helper2(node.left, false) + helper2(node.right, false);  // 偷该节点时的最大收获
            res = Math.max(res, steal);  // 最大收获 = max(偷该节点时的最大收获, 不偷该节点时的最大收获)
        }
        return res;
    }

	/*
    * 解法3：非常聪明的解法
    * - 思路：同样是基于每个节点可以偷或不偷进行递归。
    * - 时间复杂度 O(n)，空间复杂度 O(logn)。
    * */
    public static int rob3(TreeNode root) {
        if (root == null) return 0;
        return Math.max(robInclude(root), robExclude(root));
    }

    public static int robInclude(TreeNode node) {
        if (node == null) return 0;
        return node.val + robExclude(node.left) + robExclude(node.right);
    }

    public static int robExclude(TreeNode node) {
        if (node == null) return 0;
        return rob2(node.left) + rob2(node.right);
    }

    public static void main(String[] args) {
        /*
        *      3
        *     / \
        *    2   2
        *     \   \
        *      3   1
        * */
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{3, 2, 2, null, 3, null, 1});
        log(rob2(t1));  // expects 7. (3 + 3 + 1)

        /*
        *        1
        *       / \
        *      5   5
        *     / \   \
        *    1   1   1
        * */
        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 5, 5, 1, 1, null, 1});
        log(rob2(t2));  // expects 10. (5 + 5)

        /*
        *          5
        *         /
        *        1
        *       /
        *      1
        *     /
        *    5
        * */
        TreeNode t4 = createBinaryTreeBreadthFirst(new Integer[]{5, 1, null, 1, null, 5});
        log(rob2(t4));  // expects 10. (5 + 5)

        /*
        *        1
        *       / \
        *      1   5
        *     / \   \
        *    5   5   1
        * */
        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{1, 1, 5, 5, 5, null, 1});
        log(rob2(t3));  // expects 15. (5 + 5 + 5)
    }
}
