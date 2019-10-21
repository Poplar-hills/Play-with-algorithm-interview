package BinaryTreeAndRecursion.DefineRecursiveProblem;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import java.util.List;

import Utils.Helpers.TreeNode;

/*
 * Path Sum II
 *
 * - Given a binary tree and a sum, find all root-to-leaf paths where each path's sum equals the given sum.
 * */

public class L113_PathSumII {
    /*
     * 解法1：Recursion
     * - 思路：递归函数 f(n) 定义：
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static List<List<Integer>> pathSum(TreeNode root, int sum) {

    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{5, 4, 8, 11, null, 13, 4, 7, 2, null, null, 5, 1});
        log(pathSum(t1, 22));
        /*
         * expects [[5,4,11,2], [5,8,4,5]].
         *        5
         *       / \
         *      4   8
         *     /   / \
         *    11  13  4
         *   /  \    / \
         *  7    2  5   1
         * */
    }
}
