package BinaryTreeAndRecursion.Basics;

import static Utils.Helpers.*;

import java.util.LinkedList;
import java.util.Queue;

/*
 * Invert Binary Tree
 * */

public class L226_InvertBinaryTree {
    /*
     * 解法1：Recursion
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 n 为节点数，h 为树高。
     * */
    public static TreeNode invertTree(TreeNode root) {
        if (root == null) return null;
        TreeNode left = root.left;
        TreeNode right = root.right;
        root.left = invertTree(right);
        root.right = invertTree(left);
        return root;
    }

    /*
     * 解法2：Iterative (BFS)
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 n 为节点数，h 为树高。
     * */
    public static TreeNode invertTree2(TreeNode root) {
        if (root == null) return null;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            TreeNode node = q.poll();
            TreeNode temp = node.left;
            node.left = node.right;
            node.right = temp;
            if (node.right != null) q.offer(node.right);
            if (node.left != null) q.offer(node.left);
        }

        return root;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{4, 2, 7, 1, 3, 6, 9});
        printBinaryTreeBreadthFirst(invertTree2(t1));
        /*
         * expects [4, 7, 2, 9, 6, 3, 1].
         *         4                  4
         *       /   \              /   \
         *      2     7    --->    7     2
         *     / \   / \          / \   / \
         *    1   3 6   9        9   6 3   1
         * */
    }
}
