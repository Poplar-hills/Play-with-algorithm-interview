package BinaryTreeAndRecursion.S5_LowestCommonAncestor;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import java.util.Stack;

import Utils.Helpers.TreeNode;

/*
 * Lowest Common Ancestor of a Binary Search Tree
 *
 * - Given a binary search tree (BST), find the lowest common ancestor (LCA) of two given nodes in the BST.
 *
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
     * - 思路：在从上到下遍历 BST 的过程中，第一个使得 p 和 q 不在同一边的节点就是 p 和 q 的 LCA 节点。
     * - 时间复杂度 O(h)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
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
     * 解法2：Iteration (DFS) (解法1的非递归版)
     * - 时间复杂度 O(h)，空间复杂度 O(1)。
     * */
    public static TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) return null;
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            boolean bothInTheLeft = p.val < node.val && q.val < node.val;
            boolean bothInTheRight = p.val > node.val && q.val > node.val;

            if (bothInTheLeft && node.left != null)
                stack.push(node.left);
            else if (bothInTheRight && node.right != null)
                stack.push(node.right);
            else
                return node;
        }

        return null;
    }

    /*
     * 解法3：Iteration (DFS) (解法2的空间优化版)
     * - 思路：与解法2一致。
     * - 实现：该题本质上就是对 BST 的二分查找，而非树的遍历，即不需要在树上上下折返，只要一路往下走就能得到结果 ∴ 并不需要使用
     *   栈来记录前面的节点，只需一个指针指向当前节点即可 ∴ 可以去除对 Stack 的依赖。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static TreeNode lowestCommonAncestor3(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) return null;
        TreeNode curr = root;

        while (curr != null) {
            if (p.val < curr.val && q.val < curr.val)
                curr = curr.left;
            else if (p.val > curr.val && q.val > curr.val)
                curr = curr.right;
            else
                return curr;
        }

        return null;
    }


    public static void main(String[] args) {
        TreeNode t = createBinaryTreeBreadthFirst(new Integer[]{6, 2, 8, 0, 4, 7, 9, null, null, 3, 5});
        /*
         *           6
         *        /     \
         *       2       8
         *      / \     / \
         *     0   4   7   9
         *        / \
         *       3   5
         * */

        log(lowestCommonAncestor3(t, t.get(2), t.get(8)));   // expects 6. (The LCA of nodes 2 and 8 is 6.)
        log(lowestCommonAncestor3(t, t.get(3), t.get(7)));   // expects 6.
        log(lowestCommonAncestor3(t, t.get(2), t.get(4)));   // expects 2.
        log(lowestCommonAncestor3(t, t.get(0), t.get(5)));   // expects 2.
    }
}
