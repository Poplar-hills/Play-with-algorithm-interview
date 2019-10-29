package BinaryTreeAndRecursion.LowestCommonAncestor;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import Utils.Helpers.TreeNode;

/*
 * Validate Binary Search Tree
 *
 * - Given a binary tree, determine if it is a valid binary search tree (BST).
 * - Assume a BST is defined as follows:
 *   1. The left subtree of a node contains only nodes with keys less than the node's key.
 *   2. The right subtree of a node contains only nodes with keys greater than the node's key.
 *   3. Both the left and right subtrees must also be binary search trees.
 * */

public class L98_ValidateBinarySearchTree {
    /*
     * 解法1：
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static boolean isValidBST(TreeNode root) {
        return true;
    }

    public static void main(String[] args) {
      TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{2, 1, 3});
      log(isValidBST(t1));
      /*
       * expects true.
       *     2
       *    / \
       *   1   3
       * */

      TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{5, 1, 4, null, null, 3, 6});
      log(isValidBST(t2));
      /*
       * expects false.
       *     5
       *    / \
       *   1   4
       *      / \
       *     3   6
       * */

    }
}
