package BinaryTreeAndRecursion.LowestCommonAncestor;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import Utils.Helpers.TreeNode;

/*
 * Lowest Common Ancestor of a Binary Tree
 *
 * - Given a binary tree, find the lowest common ancestor (LCA) of two given nodes in the tree.
 *
 * - Note:
 *   1. All of the nodes' values will be unique.
 *   2. p and q are different and both values will exist in the binary tree.
 * */

public class L236_LCAOfBinaryTree {
    /*
     * 解法1：Recursion (DFS)
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null)
            return null;
        if (contains(root.left, p) && contains(root.left, q))
            return lowestCommonAncestor(root.left, p, q);
        if (contains(root.right, p) && contains(root.right, q))
            return lowestCommonAncestor(root.right, p, q);
        return root;
    }

    private static boolean contains(TreeNode root, TreeNode node) {  // O(2^logn)
        if (root == null) return false;
        if (root.val == node.val) return true;
        return contains(root.left, node) || contains(root.right, node);
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{3, 5, 1, 6, 2, 0, 8, null, null, 7, 4});
        /*
         *           3
         *        /     \
         *       5       1
         *      / \     / \
         *     6   2   0   8
         *        / \
         *       7   4
         * */

        log(lowestCommonAncestor(t1, new TreeNode(5), new TreeNode(1)));  // expects 3. (The LCA of nodes 5 and 1 is 3.)
        log(lowestCommonAncestor(t1, new TreeNode(7), new TreeNode(0)));  // expects 3.
        log(lowestCommonAncestor(t1, new TreeNode(5), new TreeNode(4)));  // expects 5.
        log(lowestCommonAncestor(t1, new TreeNode(4), new TreeNode(6)));  // expects 5.
    }
}
