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
     * - 思路：递归的终止条件很容易写成 if (root == null) return sum == 0; 但这样 test case 3 过不去。因此递归终止条件
     *   需要用叶子节点来判断
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static boolean hasPathSum(TreeNode root, int sum) {
        if (root == null) return false;
        if (root.left == null && root.right == null) return sum == root.val;
        return hasPathSum(root.left, sum - root.val) || hasPathSum(root.right, sum - root.val);
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

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, -2, -3, 1, 3, -2, null, -1});
        log(hasPathSum(t2, -1));
        /*
         * expects true. (1 -> -2 -> 1 -> -1)
         *          1
         *        /   \
         *      -2    -3
         *      / \   /
         *     1  3  -2
         *    /
         *   -1
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{5, 4, 8, 11, null, 13, 4});
        log(hasPathSum(t3, 9));
        /*
         * expects false. (注意：5 -> 4 虽然和为9，但不是一条 root-to-leaf path)
         *         5
         *        / \
         *       4   8
         *      /   / \
         *     11  13  4
         * */

        TreeNode t4 = createBinaryTreeBreadthFirst(new Integer[]{});
        log(hasPathSum(t4, 1));
        /*
         * expects false.
         * */
    }
}
