package BinaryTreeAndRecursion.Basics;

import static Utils.Helpers.*;

import java.util.Stack;

import javafx.util.Pair;
import Utils.Helpers.TreeNode;

/*
 * Balanced Binary Tree
 *
 * - Given a binary tree, determine if it is height-balanced.
 * - For this problem, height-balanced means the depth of the two subtrees of every node never differ by more than 1.
 * */

public class L110_BalancedBinaryTree {
    /*
     * 解法1：Recursion
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static boolean isBalanced(TreeNode root) {
        return maxDepth(root) != -1;
    }

    private static int maxDepth(TreeNode root) {  // 计算左右子树的最大高度，若高度差 > 1，则返回 -1，否则返回最大高度
        if (root == null) return 0;
        int left = maxDepth(root.left);
        if (left == -1) return -1;
        int right = maxDepth(root.right);
        if (right == -1 || Math.abs(left - right) > 1) return -1;
        return 1 + Math.max(left, right);
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, null, null, 3, 3});
        log(isBalanced2(t1));
        /*
         * expects true.
         *        1
         *       / \
         *      2   2
         *         / \
         *        3   3
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, 3, null, 3, 3});
        log(isBalanced2(t2));
        /*
         * expects true.
         *        1
         *       / \
         *      2   2
         *     /   / \
         *    3   3   3
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, 3, 3, null, 3, 4, 4});
        log(isBalanced2(t3));
        /*
         * expects true. (注意这个是平衡树 ∵ 本题中平衡树的定义是任意节点的左右子树的最大高度的差 < 1)
         *           1
         *          / \
         *         2   2
         *        / \   \
         *       3   3   3
         *      / \
         *     4   4
         * */

        TreeNode t4 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, 3, 3, null, null, 4, 4});
        log(isBalanced2(t4));
        /*
         * expects false.
         *           1
         *          / \
         *         2   2
         *        / \
         *       3   3
         *      / \
         *     4   4
         * */

        TreeNode t5 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, 3, null, null, 3, 4, null, null, 4});
        log(isBalanced2(t5));
        /*
         * expects false.
         *           1
         *          / \
         *         2   2
         *        /     \
         *       3       3
         *      /         \
         *     4           4
         * */
    }
}
