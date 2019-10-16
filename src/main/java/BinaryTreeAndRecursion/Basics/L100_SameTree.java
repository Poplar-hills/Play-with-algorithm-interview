package BinaryTreeAndRecursion.Basics;

import static Utils.Helpers.*;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import javafx.util.Pair;

/*
 * Same Tree
 *
 * - Given two binary trees, check if they are the same or not.
 * - Two binary trees are considered the same if they are identical in both structure and nodes.
 * */

public class L100_SameTree {
    /*
     * 解法1：Recursion
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) return true;
        if (p == null || q == null) return false;
        if (p.val != q.val) return false;
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }

    /*
     * 解法2：Iteration (BFS)
     * - 时间复杂度 O(n)；
     * - 空间复杂度 O(n)，∵ q 中同时最多容纳 n/2 个节点（即完美二叉树的最后一行）∴ 是 O(n) 级别。
     * */
    public static boolean isSameTree2(TreeNode p, TreeNode q) {
        if (p == null && q == null) return true;
        Queue<Pair<TreeNode, TreeNode>> queue = new LinkedList<>();
        queue.offer(new Pair<>(p, q));

        while (!queue.isEmpty()) {
            Pair<TreeNode, TreeNode> pair = queue.poll();
            TreeNode n1 = pair.getKey();
            TreeNode n2 = pair.getValue();
            if (n1 == null && n2 == null) continue;
            if (n1 == null || n2 == null || n1.val != n2.val) return false;
            queue.offer(new Pair<>(n1.left, n2.left));
            queue.offer(new Pair<>(n1.right, n2.right));
        }

        return true;
    }

    /*
     * 解法3：Iteration (DFS)
     * - 思路：与解法2的逻辑一致，与 L226_InvertBinaryTree 解法3的思路一致。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean isSameTree3(TreeNode p, TreeNode q) {
        if (p == null && q == null) return true;
        Stack<Pair<TreeNode, TreeNode>> stack = new Stack<>();
        stack.push(new Pair<>(p, q));

        while (!stack.isEmpty()) {
            Pair<TreeNode, TreeNode> pair = stack.pop();
            TreeNode n1 = pair.getKey();
            TreeNode n2 = pair.getValue();
            if (n1 == null && n2 == null) continue;
            if (n1 == null || n2 == null || n1.val != n2.val) return false;
            stack.push(new Pair<>(n1.left, n2.left));
            stack.push(new Pair<>(n1.right, n2.right));
        }

        return true;
    }

    public static void main(String[] args) {
        TreeNode p1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3});
        TreeNode q1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3});
        log(isSameTree3(p1, q1));
        /*
         * expects true.
         *      1         1
         *     / \
         *
         * return true;  / \
         *    2   3     2   3
         * */

        TreeNode p2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2});
        TreeNode q2 = createBinaryTreeBreadthFirst(new Integer[]{1, null, 2});
        log(isSameTree3(p2, q2));
        /*
         * expects false.
         *      1         1
         *     /
         *
         * return true;    \
         *    2             2
         * */

        TreeNode p3 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 1});
        TreeNode q3 = createBinaryTreeBreadthFirst(new Integer[]{1, 1, 2});
        log(isSameTree3(p3, q3));
        /*
         * expects false.
         *      1         1
         *     / \       / \
         *    2   1     1   2
         * */
    }
}
