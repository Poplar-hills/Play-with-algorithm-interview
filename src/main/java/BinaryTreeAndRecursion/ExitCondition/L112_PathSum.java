package BinaryTreeAndRecursion.ExitCondition;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import Utils.Helpers.TreeNode;

/*
 * Path Sum
 *
 * - Given a binary tree and a sum, determine if the tree has a root-to-leaf path such that adding up all the
 *   values along the path equals the given sum.
 * */

public class L112_PathSum {
    /*
     * 解法1：Recursion
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static boolean hasPathSum(TreeNode root, int sum) {
        if (root == null) return false;

        boolean isLeafNode = root.left == null && root.right == null;
        if (isLeafNode && sum == root.val) return true;
        if ((isLeafNode && sum != root.val) && (!isLeafNode && sum == 0)) return false;

        boolean foundInLeft = root.left != null && hasPathSum(root.left, sum - root.val);
        boolean foundInRight = root.right != null && hasPathSum(root.right, sum - root.val);
        return foundInLeft || foundInRight;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{5, 4, 8, 11, null, 13, 4, 7, 2, null, null, null, 1});
        log(hasPathSum(t1, 22));
        /*
         * expects true. (5 -> 4 -> 11 -> 2)
         *         5
         *        / \
         *       4   8
         *      /   / \
         *     11  13  4
         *    /  \      \
         *   7    2      1
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{5, 4, 8, 11, null, 13, 4});
        log(hasPathSum(t2, 9));
        /*
         * expects false. (5 -> 4 虽然和为9，但不是一条 root-to-leaf path)
         *         5
         *        / \
         *       4   8
         *      /   / \
         *     11  13  4
         * */
    }
}
