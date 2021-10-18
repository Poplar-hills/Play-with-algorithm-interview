package Misc;

import static Utils.Helpers.*;

/*
 * Count Good Nodes in Binary Tree
 *
 * - Given a binary tree root, a node X in the tree is named good if in the path from root to X there are no nodes
 *   with a value greater than X. Return the number of good nodes in the binary tree.
 * */

public class L1448_CountGoodNodesInBinaryTree {
    /*
     * 解法1：
     * */
    public static int goodNodes(TreeNode root) {
        return 0;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeDepthFirst(new Integer[]{3, 1, 4, 3, null, 1, 5});
        log(goodNodes(t1));
        /*
         * expects 4.
         *         3
         *       /   \
         *      1     4
         *    /     /  \
         *   3     1    5
         *
         * - Node 3、4、3、5 are good nodes.
         * - Root node 3 is always a good node.
         * - Node 4: (3,4) is the maximum value in the path starting from the root.
         * - Node 5: (3,4,5) is the maximum value in the path.
         * - Node 3: (3,1,3) is the maximum value in the path.
         * */

        TreeNode t2 = createBinaryTreeDepthFirst(new Integer[]{3, 3, null, 4, 2});
        log(goodNodes(t2));
        /*
         * expects 3.
         *         3
         *       /
         *      3
         *    /   \
         *   4     2
         *
         * - Node 3、3、4 are good nodes.
         * - Node 2: (3, 3, 2) is not good, because "3" is higher than it.
         * */

        TreeNode t3 = createBinaryTreeDepthFirst(new Integer[]{1});
        log(goodNodes(t3));
        /*
         * expects 1. Root is considered as good.
         * */
    }
}
