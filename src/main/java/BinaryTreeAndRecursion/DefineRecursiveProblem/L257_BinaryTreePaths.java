package BinaryTreeAndRecursion.ExitCondition;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import java.util.List;

import Utils.Helpers.TreeNode;

/*
 * Binary Tree Paths
 *
 * - Given a binary tree, return all root-to-leaf paths.
 * */

public class L257_BinaryTreePaths {
    /*
     * 解法1：Recursion
     * -
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static List<String> binaryTreePaths(TreeNode root) {

    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, null, 5});
        log(binaryTreePaths(t1));
        /*
         * expects ["1->2->5", "1->3"].
         *       1
         *     /   \
         *    2     3
         *     \
         *      5
         * */
    }
}
