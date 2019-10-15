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
        return false;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, null, 15, 7});
        log(isBalanced(t1));
        /*
         * expects true.
         *        3
         *       / \
         *      9  20
         *         / \
         *        15  7
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, 3, 3, null, null, 4, 4});
        log(isBalanced(t2));
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
    }
}
