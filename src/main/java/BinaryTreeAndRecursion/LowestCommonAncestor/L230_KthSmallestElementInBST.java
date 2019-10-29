package BinaryTreeAndRecursion.LowestCommonAncestor;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import Utils.Helpers.TreeNode;

/*
 * Delete Node in a BST
 *
 * - Given a binary search tree, write a function kthSmallest to find the kth smallest element in it.
 * - Note: You may assume k is always valid, 1 ≤ k ≤ BST's total elements.
 *
 * - Follow up: What if the BST is modified (insert/delete operations) often and you need to find the kth
 *   smallest frequently? How would you optimize the kthSmallest routine?
 * */

public class L230_KthSmallestElementInBST {
    /*
     * 解法1：
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static int kthSmallest(TreeNode root, int k) {
        return 0;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{5, 3, 6, 2, 4, null, 7});
        log(kthSmallest(t1, 1));
        /*
         * expects 1.
         *       3
         *      / \
         *     1   4
         *      \
         *       2
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{5, 3, 6, 2, 4, null, null, 1});
        log(kthSmallest(t2, 3));
        /*
         * expects 3.
         *          5
         *         / \
         *        3   6
         *       / \
         *      2   4
         *     /
         *    1
         * */
    }
}
