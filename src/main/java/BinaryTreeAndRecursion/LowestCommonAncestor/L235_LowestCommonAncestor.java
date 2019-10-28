package BinaryTreeAndRecursion.LowestCommonAncestor;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import Utils.Helpers.TreeNode;

/*
 * Lowest Common Ancestor of a Binary Search Tree
 *
 * - Given a binary search tree (BST), find the lowest common ancestor (LCA) of two given nodes in the BST.
 * */

public class L235_LowestCommonAncestor {
    /*
     * 解法1：Recursion (DFS)
     * - 思路：递归函数定义：判断节点 p 和 q 是否同时在以 root 为根的 BST 上，若是返回 root 节点，否则返回 null。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || contains(root, p) || contains(root, q))
            return null;
        if (p.val < root.val && q.val < root.val)
            return lowestCommonAncestor(root.left, p, q);
        if (p.val > root.val && q.val > root.val)
            return lowestCommonAncestor(root.right, p, q);
        return root;
    }

    private static boolean contains(TreeNode root, TreeNode node) {
        if (root == null) return false;
        if (node.val < root.val) return contains(root.left, node);
        if (node.val > root.val) return contains(root.right, node);
        return true;
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
        log(lowestCommonAncestor(t1, new TreeNode(-1), new TreeNode(5)));  // expects null.
    }
}
