package BinaryTreeAndRecursion.LowestCommonAncestor;

import static Utils.Helpers.log;

import Utils.Helpers.TreeNode;

/*
 * Delete Node in a BST
 *
 * - Given an array where elements are sorted in ascending order, convert it to a height balanced BST.
 * - For this problem, a height-balanced binary tree is defined as a binary tree in which the depth of the
 *   two subtrees of every node never differ by more than 1.
 * */

public class L108_ConvertSortedArrayToBST {
    /*
     * 解法1：
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static TreeNode sortedArrayToBST(int[] nums) {
        return null;
    }

    public static void main(String[] args) {
      int[] arr1 = new int[]{-10, -3, 0, 5, 9};
      log(sortedArrayToBST(arr1));
      /*
       * One possible answer is [0,-3,9,-10,null,5]:
       *        0
       *       / \
       *     -3   9
       *     /   /
       *   -10  5
       * */
    }
}
