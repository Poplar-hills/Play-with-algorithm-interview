package BinaryTreeAndRecursion.LowestCommonAncestor;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import Utils.Helpers.TreeNode;

/*
 * Validate Binary Search Tree
 *
 * - Given a binary tree, determine if it is a valid binary search tree (BST).
 *
 * - Assume a BST is defined as follows:
 *   1. The left subtree of a node contains only nodes with keys less than the node's key.
 *   2. The right subtree of a node contains only nodes with keys greater than the node's key.
 *   3. Both the left and right subtrees must also be binary search trees.
 * */

public class L98_ValidateBinarySearchTree {
    /*
     * 解法1：Recursion
     * - 思路：根据题中给出的 BST 设计递归函数：
     *     1. 父节点 > 左子树中的所有节点；
     *     2. 父节点 < 右子树中的所有节点；
     *     3. 左、右子树都是 BST。
     *   因此递归函数可以定义为：检查当前节点是否 > 其左子树上的所有节点，且 < 其右子树上的所有节点。
     * - 时间复杂度 O(nlogn)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static boolean isValidBST(TreeNode root) {
        if (root == null) return true;
        if (!isMaxInBST(root, root.left) || !isMinInBST(root, root.right)) return false;
        return isValidBST(root.left) && isValidBST(root.right);
    }

    private static boolean isMaxInBST(TreeNode node, TreeNode root) {  // 检查 node 是否 > root 上的所有节点
        if (root == null) return true;
        if (node.val <= root.val) return false;
        return isMaxInBST(node, root.right);  // 若 node > 右子树上的所有节点则说明 node > root 上的所有节点
    }                                         // （右子树是否是 BST 交给 isValidBST 去检查）

    private static boolean isMinInBST(TreeNode node, TreeNode root) {  // 检查 node 是否 < root 上的所有节点
        if (root == null) return true;
        if (node.val >= root.val) return false;
        return isMinInBST(node, root.left);  // 若 node < 左子树上的所有节点则说明 node < root 上的所有节点
    }                                        // （左子树是否是 BST 交给 isValidBST 去检查）

    /*
     * 解法2：Recursion
     * - 思路：另一种思路是使用上下界来检查每层节点是否合法，例如下面这棵树：
     *                5
     *              /   \
     *             3     7
     *            / \   / \
     *           2   4 8   6
     *     - 节点3是合法的 ∵ 它 < 5；      - 节点7是合法的 ∵ 它 > 5；
     *     - 节点2是合法的 ∵ 它 < 3；      - 节点8是非法的 ∵ 它应该 < 7 且 > 5；
     *     - 节点4是合法的 ∵ 3 < 它 < 5；  - 节点6是非法的 ∵ 它应该 > 7；
     *   可见每个节点值的上、下界是由上层及上上层节点决定的，这可以通过给递归函数传递上、下界参数来实现。而每层的递归函数可以定义
     *   为：当前节点是否 > 下界，且 < 上界。
     * - 时间复杂度 O(nlogn)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static boolean isValidBST2(TreeNode root) {
        return withinBounds(root, null, null);
    }

    private static boolean withinBounds(TreeNode root, Integer lower, Integer upper) {
        if (root == null) return true;
        if (lower != null && root.val <= lower) return false;
        if (upper != null && root.val >= upper) return false;
        return withinBounds(root.left, lower, root.val) && withinBounds(root.right, root.val, upper);
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{2, 1, 3});
        log(isValidBST2(t1));
        /*
         * expects true.
         *     2
         *    / \
         *   1   3
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{5, 1, 4, null, null, 3, 6});
        log(isValidBST2(t2));
        /*
         * expects false.
         *     5
         *    / \
         *   1   4
         *      / \
         *     3   6
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{1, 1});
        log(isValidBST2(t3));
        /*
         * expects false. (等值节点的情况)
         *     1
         *    /
         *   1
         * */

        TreeNode t4 = createBinaryTreeBreadthFirst(new Integer[]{10, 5, 15, null, null, 6, 20});
        log(isValidBST2(t4));
        /*
         * expects false. (跨层级的情况：6 < 10)
         *     10
         *    /  \
         *   5   15
         *      /  \
         *     6   20
         * */
    }
}
