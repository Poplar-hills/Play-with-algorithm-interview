package BinaryTreeAndRecursion.Basics;

import static Utils.Helpers.*;

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
     * - 思路：
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static boolean isBalanced(TreeNode root) {
        return height(root) != -1;
    }

    private static int height(TreeNode root) {  // 若 root 是平衡树则返回树高，否则返回 -1
        if (root == null) return 0;
        int left = height(root.left);
        if (left == -1) return -1;
        int right = height(root.right);
        if (right == -1 || Math.abs(left - right) > 1) return -1;
        return 1 + Math.max(left, right);
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, null, 15, 7});
        log(isBalanced2(t1));
        /*
         * expects true.
         *        3
         *       / \
         *      9  20
         *         / \
         *        15  7
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, 4, null, 15, 7});
        log(isBalanced2(t2));
        /*
         * expects true.
         *        3
         *       / \
         *      9  20
         *     /   / \
         *    4   15  7
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, 3, 3, null, 7, 4, 4});
        log(isBalanced2(t3));
        /*
         * expects false.
         *           1
         *          / \
         *         2   2
         *        / \   \
         *       3   3   7
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
