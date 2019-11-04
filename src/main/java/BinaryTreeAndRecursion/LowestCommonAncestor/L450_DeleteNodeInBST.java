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
            TreeNode successor = getMin(root.right);  // 若左右子树都有，则使用 Hibbard Deletion 方法
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

    /*
     * 解法2：Iteration + Hibbard Deletion 方法
     * - 思路：解法1的非递归版，总体思路是：1. 先找到以待删除节点为根的子树；2. 删除其父节点。具体移动过程比较复杂，要画图来辅助思考。
     * - 👉 总结：二叉树操作的非递归实现通常都需要拿到：1. 待操作节点；2. 待操作节点的父节点。
     * - 时间复杂度 O(logn)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static TreeNode deleteNode2(TreeNode root, int key) {
        TreeNode prev = null, curr = root;

        while (curr != null && curr.val != key) {  // 找到待删除节点，及其父节点
            prev = curr;
            if (key < curr.val) curr = curr.left;
            else if (key > curr.val) curr = curr.right;
        }

        if (prev == null) return deleteRootNode(curr);            // 待删除节点就是二叉树的根节点的情况
        if (prev.left == curr) prev.left = deleteRootNode(curr);  // 待删除节点是 prev.left 的情况
        else prev.right = deleteRootNode(curr);                   // 待删除节点是 prev.right 的情况
        return root;
    }

    private static TreeNode deleteRootNode(TreeNode root) {  // 从以 root 为根的二叉树中删除根节点，并返回以 succssor 为根的二叉树
        if (root == null) return null;
        if (root.left == null) return root.right;  // 没有子树或只有右子树
        if (root.right == null) return root.left;  // 没有子树或只有左子树

        TreeNode prev = null, succ = root.right;  // 若左右子树都有，则去右子树中找到最小节点 successor 节点，及其父节点
        while (succ.left != null) {
            prev = succ;
            succ = succ.left;
        }

        if (root.right == succ) return succ;  // 若 successor 就是右子树的根节点的情况（再没有左子树）
        prev.left = succ.right;               // 在移动 successor 之前需要先保留其右子树（移动到父节点上，接替 successor 的位置）
        succ.right = root.right;              // 再让 successor 接替根节点
        return succ;                          // 返回新的根节点
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{5, 3, 6, 2, null, null, 7});
        printBinaryTreeBreadthFirst(deleteNode2(t1, 3));
        /*
         * expects [5,2,6,null,null,null,7]
         *       5                              5
         *      / \       After deleting       / \
         *     3   6     --------------->     2   6
         *    /     \       the node 3             \
         *   2       7                              7
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{5, 3, 6, null, 4, null, 7});
        printBinaryTreeBreadthFirst(deleteNode2(t2, 3));
        /*
         * expects [5,4,6,null,null,null,7]
         *       5                              5
         *      / \       After deleting       / \
         *     3   6     --------------->     4   6
         *      \   \       the node 3             \
         *       4   7                              7
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{5, 3, 6, 2, 4, null, 7});
        printBinaryTreeBreadthFirst(deleteNode2(t3, 3));
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
