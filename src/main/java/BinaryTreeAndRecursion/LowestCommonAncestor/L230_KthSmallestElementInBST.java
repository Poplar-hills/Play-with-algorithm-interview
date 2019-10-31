package BinaryTreeAndRecursion.LowestCommonAncestor;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import java.util.ArrayList;
import java.util.List;

import Utils.Helpers.TreeNode;

/*
 * Kth Smallest Element in a BST
 *
 * - Given a binary search tree, write a function to find the kth smallest element in it.
 *
 * - Note: You may assume k is always valid, 1 ≤ k ≤ BST's total elements.
 *
 * - Follow up: What if the BST is modified (insert/delete operations) often and you need to find the kth
 *   smallest frequently? How would you optimize the kthSmallest routine?
 * */

public class L230_KthSmallestElementInBST {
    /*
     * 解法1：Recursion（中序遍历）
     * - 思路：利用“BST 的中序遍历是从小到大遍历”这一性质
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static int kthSmallest(TreeNode root, int k) {
        List<Integer> arr = new ArrayList<>();
        inorder(root, arr);
        return arr.get(k - 1);
    }

    private static void inorder(TreeNode node, List<Integer> arr) {  // Q: Integer 作为参数不是传引用？？？
        if (node == null) return;
        inorder(node.left, arr);
        arr.add(node.val);
        inorder(node.right, arr);
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{3, 1, 4, null, 2});
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
