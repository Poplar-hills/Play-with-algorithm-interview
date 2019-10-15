package BinaryTreeAndRecursion.Basics;

import static Utils.Helpers.*;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import Utils.Helpers.TreeNode;

/*
 * Count Complete Tree Nodes
 *
 * - Given a complete binary tree (完全二叉树), count the number of nodes.
 * - Definition of a complete binary tree: Every level, except possibly the last, is completely filled, and
 *   all nodes in the last level are as far left as possible (注：堆使用的就是完全二叉树).
 * - 完全二叉树第 h 层的节点数 ∈ [1, 2^h]。
 * */

public class L222_CountCompleteTreeNodes {
    /*
     * 解法1：Recursion
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int countNodes(TreeNode root) {
        if (root == null) return 0;
        return helper(root, 0);
    }

    private static int helper(TreeNode node, int count) {
        if (node == null) return count;
        return 1 + helper(node.left, count) + helper(node.right, count);
    }

    /*
     * 解法2：Iteration (BFS)
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int countNodes2(TreeNode root) {
        if (root == null) return 0;
        int count = 0;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            TreeNode node = q.poll();
            count++;
            if (node.left != null) q.offer(node.left);
            if (node.right != null) q.offer(node.right);
        }

        return count;
    }

    /*
     * 解法3：Iteration (DFS)
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int countNodes3(TreeNode root) {
        if (root == null) return 0;
        int count = 0;
        Stack<TreeNode> stack = new Stack<>();
        stack.add(root);

        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            count++;
            if (node.left != null) stack.add(node.left);
            if (node.right != null) stack.add(node.right);
        }

        return count;
    }

    /*
     * 解法4：
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int countNodes4(TreeNode root) {
        if (root == null) return 0;
        int count = 0;


        return count;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, 4, 5, 6});
        log(countNodes4(t1));
        /*
         * expects 6.
         *        1
         *       / \
         *      2   3
         *     / \ /
         *    4  5 6
         * */
    }
}
