package BinaryTreeAndRecursion.DefineRecursiveProblem;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import Utils.Helpers.TreeNode;

/*
 * Sum Root to Leaf Numbers
 *
 * - Given a binary tree containing digits from 0-9 only, each root-to-leaf path could represent a number.
 *   For example the root-to-leaf path 1->2->3 represents the number 123. Find the total sum of all
 *   root-to-leaf numbers.
 * */

public class L129_SumRootToLeafNumbers {
    /*
     * 解法1：Recursion
     * - 思路：递归函数 f(n) 定义：返回以 n 为根的二叉树的所有 root-to-leaf numbers 之和。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int sumNumbers(TreeNode root) {
        return helper(root, 0);
    }

    private static int helper(TreeNode root, int pathNum) {
        if (root == null) return 0;
        pathNum = pathNum * 10 + root.val;
        if (root.left == null && root.right == null) return pathNum;
        return helper(root.left, pathNum) + helper(root.right, pathNum);
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3});
        log(sumNumbers(t1));
        /*
         * expects 25. (12 + 13)
         *        1
         *       / \
         *      2   3
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{4, 9, 0, 5, 1});
        log(sumNumbers(t2));
        /*
         * expects 1026. (495 + 491 + 40)
         *        4
         *       / \
         *      9   0
         *     / \
         *    5   1
         * */
    }
}
