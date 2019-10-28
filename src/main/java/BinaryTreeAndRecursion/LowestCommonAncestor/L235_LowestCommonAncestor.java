package BinaryTreeAndRecursion.LowestCommonAncestor;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import Utils.Helpers.TreeNode;

/*
 * Lowest Common Ancestor of a Binary Search Tree
 *
 * - Given a binary search tree (BST), find the lowest common ancestor (LCA) of two given nodes in the BST.
 * - Note:
 *   1. All of the nodes' values will be unique.
 *   2. p and q are different and both values will exist in the BST.
 * */

public class L235_LowestCommonAncestor {
    /*
     * 解法1：Recursion (DFS)
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null)
            return null;
        if (p.val < root.val && q.val < root.val)
            return lowestCommonAncestor(root.left, p, q);
        if (p.val > root.val && q.val > root.val)
            return lowestCommonAncestor(root.right, p, q);
        return root;
    }

    /*
     * 解法2：Iteration (DFS)
     * - 思路：
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {
        return null;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{6, 2, 8, 0, 4, 7, 9, null, null, 3, 5});
        /*
         *           6
         *        /     \
         *       2       8
         *      / \     / \
         *     0   4   7   9
         *        / \
         *       3   5
         * */

        log(lowestCommonAncestor(t1, new TreeNode(2), new TreeNode(8)));   // expects 6. (The LCA of nodes 2 and 8 is 6.)
        log(lowestCommonAncestor(t1, new TreeNode(3), new TreeNode(7)));   // expects 6.
        log(lowestCommonAncestor(t1, new TreeNode(2), new TreeNode(4)));   // expects 2.
        log(lowestCommonAncestor(t1, new TreeNode(0), new TreeNode(5)));   // expects 2.
    }
}
