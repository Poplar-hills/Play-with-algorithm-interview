package BinaryTreeAndRecursion.LowestCommonAncestor;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import java.util.Stack;

import Utils.Helpers.TreeNode;

/*
 * Lowest Common Ancestor of a Binary Search Tree
 *
 * - Given a binary search tree (BST), find the lowest common ancestor (LCA) of two given nodes in the BST.
 * - Note:
 *   1. All of the nodes' values will be unique.
 *   2. p and q are different and both values will exist in the BST.
 *
 * - Follow-up: 若 Note 中的第2条不成立（即 p 或 q 可能不在树上），则需再写一个 contains(root, node) 方法，并在递归开始
 *   前检查 p 和 q 是否都在树上。
 * */

public class L235_LCAOfBST {
    /*
     * 解法1：Recursion (DFS)
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null)
            return null;
        if (p.val < root.val && q.val < root.val)
            return lowestCommonAncestor(root.left, p, q);
        if (p.val > root.val && q.val > root.val)
            return lowestCommonAncestor(root.right, p, q);
        return root;
    }

    /*
     * 解法2：Iteration (DFS)
     * - 思路：
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) return null;
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            if (p.val < node.val && q.val < node.val && node.left != null) stack.push(node.left);
            else if (p.val > node.val && q.val > node.val && node.right != null) stack.push(node.right);
            else return node;
        }

        return null;
    }

    /*
     * 解法3：Iteration (DFS)（解法2的简化版，不借助辅助数据结构）
     * - 思路：该题本质上就是一个简单的二分查找，若 BST 上某个节点使得 p 和 q 不再该节点同一边，则说明该节点就是 LCA 节点。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static TreeNode lowestCommonAncestor3(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) return null;
        TreeNode node = root;

        while (node != null) {
            if (p.val < node.val && q.val < node.val) node = node.left;
            else if (p.val > node.val && q.val > node.val) node = node.right;
            else return node;
        }

        return null;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{6, 2, 8, 0, 4, 7, 9, null, null, 3, 5});
        /*
         *           6
         *        /     \
         *       2       8
         *      / \     / \
         *     0   4   7   9
         *        / \
         *       3   5
         * */

        log(lowestCommonAncestor2(t1, new TreeNode(2), new TreeNode(8)));   // expects 6. (The LCA of nodes 2 and 8 is 6.)
        log(lowestCommonAncestor2(t1, new TreeNode(3), new TreeNode(7)));   // expects 6.
        log(lowestCommonAncestor2(t1, new TreeNode(2), new TreeNode(4)));   // expects 2.
        log(lowestCommonAncestor2(t1, new TreeNode(0), new TreeNode(5)));   // expects 2.
    }
}
