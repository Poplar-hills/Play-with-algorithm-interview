package BinaryTreeAndRecursion.Basics;

import static Utils.Helpers.*;

/*
 * Count Complete Tree Nodes
 *
 * - Given a complete binary tree (完全二叉树), count the number of nodes.
 * - Definition of a complete binary tree: Every level, except possibly the last, is completely filled, and
 *   all nodes in the last level are as far left as possible.
 * */

public class L222_CountCompleteTreeNodes {
    /*
     * 解法1：Recursion
     * - 思路：
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int countNodes(TreeNode root) {
        return 0;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, 4, 5, 6});
        log(countNodes(t1));
        /*
         * expects 6.
         *        1
         *       / \
         *      2   3
         *     / \ /
         *    4  5 6
         * */
    }
}
