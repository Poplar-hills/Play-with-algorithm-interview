package BinaryTreeAndRecursion.Basics;

import static Utils.Helpers.*;

/*
 * Symmetric Tree
 *
 * - Given a binary tree, check whether it is a mirror of itself (ie, symmetric around its center).
 * */

public class L101_SymmetricTree {
    /*
     * 解法1：Recursion
     * -
     * */
    public static boolean isSymmetric(TreeNode root) {
        return false;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, 3, 4, 4, 3});
        log(isSymmetric(t1));
        /*
         * expects true.
         *        1
         *       / \
         *      2   2
         *     / \ / \
         *    3  4 4  3
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, null, 3, null, 3});
        log(isSymmetric(t2));
        /*
         * expects false.
         *        1
         *       / \
         *      2   2
         *       \   \
         *       3    3
         * */
    }
}
