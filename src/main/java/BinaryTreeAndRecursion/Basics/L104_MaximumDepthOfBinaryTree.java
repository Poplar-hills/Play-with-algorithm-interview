package BinaryTreeAndRecursion.Basics;

import static Utils.Helpers.*;

/*
 * Maximum Depth of Binary Tree
 *
 * - Given a binary tree, find its maximum depth.
 * */

public class L104_MaximumDepthOfBinaryTree {
    public static int maxDepth(TreeNode root) {
        if (root == null) return 0;
        return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
    }

    public static void main(String[] args) {
        TreeNode t = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, null, 15, 7});
        log(maxDepth(t));
        /*
         *  expects 3
         *      3
         *     / \
         *    9  20
         *      /  \
         *     15   7
         * */
    }
}
