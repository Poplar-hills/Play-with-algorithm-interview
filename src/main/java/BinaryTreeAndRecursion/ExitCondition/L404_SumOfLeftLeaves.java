package BinaryTreeAndRecursion.ExitCondition;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import Utils.Helpers.TreeNode;

/*
 * Sum of Left Leaves
 *
 * - Find the sum of all left leaves in a given binary tree.
 * */

public class L404_SumOfLeftLeaves {
    /*
     * 解法1：Recursion
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int sumOfLeftLeaves(TreeNode root) {
        return 0;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, null, 15, 7});
        log(sumOfLeftLeaves(t1));
        /*
         * expects 24.
         *       3
         *      / \
         *     9  20
         *       /  \
         *      15   7
         * */
    }
}
