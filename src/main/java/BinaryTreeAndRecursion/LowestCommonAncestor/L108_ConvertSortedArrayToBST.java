package BinaryTreeAndRecursion.LowestCommonAncestor;

import static Utils.Helpers.printBinaryTreeBreadthFirst;

import Utils.Helpers.TreeNode;

/*
 * Delete Node in a BST
 *
 * - Given an array where elements are sorted in ascending order, convert it to a height-balanced BST.
 *
 * - For this problem, a height-balanced binary tree is defined as a binary tree in which the depth of the
 *   two subtrees of every node never differ by more than 1.
 * */

public class L108_ConvertSortedArrayToBST {
    /*
     * 解法1：Recursion + 二分查找
     * - 思路：∵ BST 中的元素天生就是以二分的形式排列的 ∴ 从有序数组建立 BST 的过程其实就是对数组不断进行二分查找，然后顺序
     *   添加到 BST 上。
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static TreeNode sortedArrayToBST(int[] nums) {
        if (nums == null || nums.length == 0) return null;
        return helper(nums, 0, nums.length - 1);
    }

    private static TreeNode helper(int[] nums, int l, int r) {
        if (l > r) return null;
        int mid = (r - l) / 2 + l;
        TreeNode node = new TreeNode(nums[mid]);
        node.left = helper(nums, l, mid - 1);
        node.right = helper(nums, mid + 1, r);
        return node;
    }

    public static void main(String[] args) {
      int[] arr1 = new int[]{-6, -4, -2, 0, 1, 3, 5};
      printBinaryTreeBreadthFirst(sortedArrayToBST(arr1));
      /*
       * expects [0,-4,3,-6,-2,1,5] or [-2,-4,3,-6,null,1,5,null,null,0] or others.
       *                   0                          -2
       *                 /   \                       /  \
       *               -4     3                    -4    3
       *               / \   / \                   /    / \
       *             -6  -2 1   5                -6    1   5
       *                                              /
       *                                             0
       * */
    }
}
