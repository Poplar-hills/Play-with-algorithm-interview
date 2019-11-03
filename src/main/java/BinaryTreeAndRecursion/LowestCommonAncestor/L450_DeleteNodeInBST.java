package BinaryTreeAndRecursion.LowestCommonAncestor;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.printBinaryTreeBreadthFirst;

import Utils.Helpers.TreeNode;

/*
 * Delete Node in a BST
 *
 * - Given a root node of a BST, delete the node with the given key in the BST. Return the root node
 *   (possibly updated) of the BST.
 *
 * - Basically, the deletion can be divided into two stages:
 *   1. Search for a node to remove.
 *   2. If the node is found, delete the node.
 * */

public class L450_DeleteNodeInBST {
    /*
     * 解法1：Recursion + Hibbard Deletion 方法
     * - 思路：思路与 Play-with-data-structure/BST/BST.java 中的 remove 方法一致。
     * - 时间复杂度 O(logn)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static TreeNode deleteNode(TreeNode root, int key) {
        if (root == null) return null;
        if (key < root.val)
            root.left = deleteNode(root.left, key);
        else if (key > root.val)
            root.right = deleteNode(root.right, key);
        else {
            if (root.left == null) return root.right;
            if (root.right == null) return root.left;

            TreeNode successor = getMin(root.right);   // 若左右子树都有，则使用 Hibbard Deletion 方法
            successor.right = removeMin(root.right);
            successor.left = root.left;
            root = successor;
        }
        return root;
    }

    private static TreeNode getMin(TreeNode node) {
        return (node.left == null) ? node : getMin(node.left);
    }

    private static TreeNode removeMin(TreeNode node) {
        if (node.left == null) return node.right;
        node.left = removeMin(node.left);
        return node;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{5, 3, 6, 2, null, null, 7});
        printBinaryTreeBreadthFirst(deleteNode(t1, 3));
        /*
         * expects [5,2,6,null,null,null,7]
         *       5                              5
         *      / \       After deleting       / \
         *     3   6     --------------->     2   6
         *    /     \       the node 3             \
         *   2       7                              7
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{5, 3, 6, null, 4, null, 7});
        printBinaryTreeBreadthFirst(deleteNode(t2, 3));
        /*
         * expects [5,4,6,null,null,null,7]
         *       5                              5
         *      / \       After deleting       / \
         *     3   6     --------------->     4   6
         *      \   \       the node 3             \
         *       4   7                              7
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{5, 3, 6, 2, 4, null, 7});
        printBinaryTreeBreadthFirst(deleteNode(t3, 3));
        /*
         * expects [5,4,6,2,null,null,7] or [5,2,6,null,4,null,7]
         *       5                              5                  5
         *      / \       After deleting       / \                / \
         *     3   6     --------------->     4   6      or      2   6
         *    / \   \       the node 3       /     \              \   \
         *   2   4   7                      2       7              4   7
         * */
    }
}
