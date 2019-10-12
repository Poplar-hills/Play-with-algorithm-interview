package BinaryTreeAndRecursion.Basics;

import static Utils.Helpers.*;

import Utils.Helpers.TreeNode;

/*
 * Symmetric Tree
 *
 * - Given a binary tree, check whether it is a mirror of itself (ie, symmetric around its center, both in
 *   structure and node values).
 * */

public class L101_SymmetricTree {
    /*
     * 解法1：Recursion
     * - 思路：若一棵树是对称的，则其左右子树应互为镜像；若两棵树互为镜像，则：
     *     1. 他们的根节点值相同；
     *     2. 树1的左子树和树2的右子树互为镜像；
     *     3. 树1的右子树和树2的左子树互为镜像；
     *   这是个递归定义 ∴ 可以很自然的用递归求解，递归函数的语义就是求两棵树是否互为镜像。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     */
    public static boolean isSymmetric(TreeNode root) {
        return isMirror(root, root);  // 递归入口需要特殊处理一下，以符合递归函数的语义
    }

    private static boolean isMirror(TreeNode t1, TreeNode t2) {  // 检查两棵树是否互为镜像
        if (t1 == null && t2 == null) return true;
        if (t1 == null || t2 == null) return false;
        return t1.val == t2.val && isMirror(t1.left, t2.right) && isMirror(t1.right, t2.left);
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, 3, 4, 4, 3});
        log(isSymmetric(t1));
        /*
         * expects true.
         *        1
         *       / \
         *      2   2
         *     / \ / \
         *    3  4 4  3
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, 3, 4, 3, 4});
        log(isSymmetric(t2));
        /*
         * expects false.
         *        1
         *       / \
         *      2   2
         *     / \ / \
         *    3  4 3  4
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, null, 3, null, 3});
        log(isSymmetric(t3));
        /*
         * expects false.
         *        1
         *       / \
         *      2   2
         *       \   \
         *        3   3
         * */

        TreeNode t4 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, 3, null, null, 3});
        log(isSymmetric(t4));
        /*
         * expects true.
         *        1
         *       / \
         *      2   2
         *     /     \
         *    3       3
         * */
    }
}
