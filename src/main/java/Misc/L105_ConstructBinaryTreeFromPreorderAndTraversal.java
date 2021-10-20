package Misc;

import Utils.Helpers.TreeNode;

import static Utils.Helpers.log;

/*
 * Construct Binary Tree from Preorder and Inorder Traversal
 *
 * - Given two integer arrays preorder and inorder where preorder is the preorder traversal of a binary tree
 *   and inorder is the inorder traversal of the same tree, construct and return the binary tree.
 * */

public class L105_ConstructBinaryTreeFromPreorderAndTraversal {
    /*
     * 解法1：
     * -
     * */
    public static TreeNode buildTree(int[] preorder, int[] inorder) {
        return null;
    }

    public static void main(String[] args) {
        log(buildTree(new int[]{3, 9, 20, 15, 7}, new int[]{9, 3, 15, 20, 7}));
        /*
         * expects:    3
         *          /     \
         *         9      20
         *              /   \
         *            15     7
         * */

        log(buildTree(new int[]{-1}, new int[]{-1}));
        /*
         * expects:   -1
         * */
    }
}
