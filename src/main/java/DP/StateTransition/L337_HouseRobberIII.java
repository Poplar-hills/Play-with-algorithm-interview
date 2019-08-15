package DP.StateTransition;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javafx.util.Pair;

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
        if (root == null) return 0;
        int[] res = tryToRob(root);
        return Math.max(res[0], res[1]);
    }

    private static int[] tryToRob(TreeNode node) {
        int[] res = new int[2];
        int[] leftRes = new int[2];
        int[] rightRes = new int[2];

        if (node.left != null)
            leftRes = tryToRob(node.left);
        if (node.right != null)
            rightRes = tryToRob(node.right);

        res[0] = node.val + leftRes[1] + rightRes[1];
        res[1] = maxOfN(leftRes[0] + rightRes[0], leftRes[1] + rightRes[0], leftRes[0] + rightRes[1], leftRes[1] + rightRes[1]);

        return res;
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
        log(rob(t1));  // expects 7. (3 + 3 + 1)

        /*
        *        1
        *       / \
        *      5   5
        *     / \   \
        *    1   1   1
        * */
        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 5, 5, 1, 1, null, 1});
        log(rob(t2));  // expects 10. (5 + 5)

        /*
        *        1
        *       / \
        *      1   5
        *     / \   \
        *    5   5   1
        * */
        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{1, 1, 5, 5, 5, null, 1});
        log(rob(t3));  // expects 15. (5 + 5 + 5)

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
        log(rob(t4));  // expects 10. (5 + 5)
    }
}
