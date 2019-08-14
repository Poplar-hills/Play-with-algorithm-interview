package DP.StateTransition;

import static Utils.Helpers.*;

/*
* House Robber III
*
* - 基本与 L198_HouseRobber 中的条件一样，只是本题中的街道为一棵二叉树，问在该街道中，在不触发警报的情况下，最多能盗取多少财产。
*   其本质上是在问如何在二叉树上选择不相邻的节点，使节点值之和最大。
* */

public class L337_HouseRobberIII {
    /*
    * 解法1：
    * - 思路：
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static int rob(TreeNode root) {
      return 0;
    }

    public static void main(String[] args) {
        /*
        *      3
        *     / \
        *    2   3
        *     \   \
        *      3   1
        * */
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{3, 2, 3, null, 3, null, 1});
        log(rob(t1));  // expects 7. (3 + 3 + 1)

        /*
        *        3
        *       / \
        *      4   5
        *     / \   \
        *    1   3   1
        * */
        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{3, 4, 5, 1, 3, null, 1});
        log(rob(t2));  // expects 9. (4 + 5)
    }
}
