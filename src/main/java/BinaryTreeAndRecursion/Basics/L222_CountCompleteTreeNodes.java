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
 *
 * - 复习：
 *   1. 完全二叉树非最后一层的节点数为 2^h，最后一层的节点数为 1~2^h，其中 h 为层的深度；
 *   2. 完美二叉树的总结点数为 2^h - 1；
 * */

public class L222_CountCompleteTreeNodes {
    /*
     * 解法1：Recursion
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int countNodes(TreeNode root) {
        if (root == null) return 0;
        return 1 + countNodes(root.left) + countNodes(root.right);
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
     * 解法4：Recursion（解法1的优化版）
     * - 思路：不同于解法1-3，本解法利用了2个性质：
     *     1. 完美二叉树的节点总数：2^h - 1，其中 h 为树高；
     *     2. 完全二叉树左、右子树中至少有一个是完美二叉树（见 test cases）；
     *   根据性质2，可以在解法1的基础上进行优化：不直接递归子树，而是先计算树的左右深度，若左右深度相同则说明是完美二叉树，则可
     *   直接根据性质1得到节点数，若不相同则再继续遍历子树。
     * - 时间复杂度 O(logn^2)：∵ 左、右子树中至少有一个是完美二叉树，且完美的一边直接得到结果，复杂度为 O(1) ∴ 整个递归过程
     *   相当于单边递归（与 getLeftDepth 一样）∴ 复杂度为 O(logn)。而又因为每次递归中有 getLeftDepth、getRightDepth
     *   ∴ 整体复杂度为 O(logn^2)。
     * - 空间复杂度 O(logn)：∵ 完全二叉树也是平衡树，不会退化成链表。
     * */
    public static int countNodes4(TreeNode root) {
        if (root == null) return 0;
        int leftDepth = getLeftDepth(root);
        int rightDepth = getRightDepth(root);
        return (leftDepth == rightDepth)
            ? (1 << leftDepth) - 1                                   // 若是完美二叉树，则的总节点数 2^h - 1
            : 1 + countNodes4(root.left) + countNodes4(root.right);  // 若不完美则递归左右子树（其中至少有一个是完美的）
    }

    private static int getLeftDepth(TreeNode root) {
        if (root == null) return 0;
        return 1 + getLeftDepth(root.left);
    }

    private static int getRightDepth(TreeNode root) {
        if (root == null) return 0;
        return 1 + getRightDepth(root.right);
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

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, 4, 5});
        log(countNodes4(t2));
        /*
         * expects 5.
         *        1
         *       / \
         *      2   3
         *     / \
         *    4   5
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3});
        log(countNodes4(t3));
        /*
         * expects 3.
         *        1
         *       / \
         *      2   3
         * */
    }
}
