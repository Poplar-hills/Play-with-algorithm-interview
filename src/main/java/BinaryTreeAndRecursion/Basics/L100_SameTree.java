package BinaryTreeAndRecursion.Basics;

import static Utils.Helpers.*;

/*
 * Same Tree
 *
 * - Given two binary trees, check if they are the same or not.
 * - Two binary trees are considered the same if they are identical in both structure and nodes.
 * */

public class L100_SameTree {
    public static boolean isSameTree(TreeNode p, TreeNode q) {
        return true;
    }

    public static void main(String[] args) {
        TreeNode p1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3});
        TreeNode q1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3});
        log(isSameTree(p1, q1));  // expects true.

        TreeNode p2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2});
        TreeNode q2 = createBinaryTreeBreadthFirst(new Integer[]{1, null, 2});
        log(isSameTree(p2, q2));  // expects false.

        TreeNode p3 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 1});
        TreeNode q3 = createBinaryTreeBreadthFirst(new Integer[]{1, 1, 2});
        log(isSameTree(p3, q3));  // expects false.

    }
}
