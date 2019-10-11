package BinaryTreeAndRecursion.Basics;

import static Utils.Helpers.*;

/*
 * Same Tree
 *
 * - Given two binary trees, check if they are the same or not.
 * - Two binary trees are considered the same if they are identical in both structure and nodes.
 * */

public class L100_SameTree {
    /*
     * 解法1：Recursion
     * - 时间复杂度 O(n)。
     * - 空间复杂度：若树平衡，则空间复杂度达到最小 O(logn)；若树退化为链表，则空间复杂度达到最大 O(n)。
     * */
    public static boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) return true;
        if (p == null || q == null) return false;
        if (p.val != q.val) return false;
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }

    public static void main(String[] args) {
        TreeNode p1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3});
        TreeNode q1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3});
        log(isSameTree(p1, q1));
        /*
         * expects true.
         *      1         1
         *     / \       / \
         *    2   3     2   3
         * */

        TreeNode p2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2});
        TreeNode q2 = createBinaryTreeBreadthFirst(new Integer[]{1, null, 2});
        log(isSameTree(p2, q2));
        /*
         * expects false.
         *      1         1
         *     /           \
         *    2             2
         * */

        TreeNode p3 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 1});
        TreeNode q3 = createBinaryTreeBreadthFirst(new Integer[]{1, 1, 2});
        log(isSameTree(p3, q3));
        /*
         * expects false.
         *      1         1
         *     / \       / \
         *    2   1     1   2
         * */
    }
}
