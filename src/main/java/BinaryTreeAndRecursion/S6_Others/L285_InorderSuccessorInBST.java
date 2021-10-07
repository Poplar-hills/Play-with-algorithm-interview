package BinaryTreeAndRecursion.S6_Others;

import Utils.Helpers.TreeNode;

import static Utils.Helpers.*;

/*
 * Inorder Successor in BST
 *
 * - Given a binary search tree and a node in it, find the in-order successor of that node in the BST.
 *   The successor of a node p is the node with the smallest key greater than p.val.
 *
 * - Note:
 *   1. If the given node has no in-order successor in the tree, return null.
 *   2. It's guaranteed that the values of the tree are unique.
 * */


public class L285_InorderSuccessorInBST {

    private static TreeNode inorderSuccessor(TreeNode root, TreeNode p) {
        return null;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{2, 1, 3});
        TreeNode successor1 = inorderSuccessor(t1, t1.get(1));
        log(successor1.getVal());
        /*
         * expects 2
         *       2
         *     /   \
         *    1     3
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{5, 3, 6, 2, 4, null, null, 1});
        TreeNode successor2 = inorderSuccessor(t2, t2.get(6));
        log(successor2.getVal());
        /*
         * expects null. (There no in-order successor of the node 6)
         *           5
         *         /   \
         *        3     6
         *      /  \
         *     2   4
         *   /
         *  1
         * */
    }
}
