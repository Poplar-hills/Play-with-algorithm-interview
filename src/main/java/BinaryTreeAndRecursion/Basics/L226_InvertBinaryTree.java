package BinaryTreeAndRecursion.Basics;

import static Utils.Helpers.*;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/*
 * Invert Binary Tree
 * */

public class L226_InvertBinaryTree {
    /*
     * 解法1：Recursion
     * - 思路：观察 test cases 可知，要反转一棵二叉树实际上就要为树上的每个节点交换左右子树，例如：
     *               4                    4                    4
     *             /   \                /   \                /   \
     *            2     7    ----->    2     7    ----->    7     2
     *           / \   / \            / \   / \            / \   / \
     *          1   3 6   9          3   1 9   6          9   6 3   1
     *   上面过程中：1.先分别交换2的左右子树1、3，和7的左右子树6、9；
     *             2.再交换4的左右子树2、7，得到最终结果。
     *   ∴ 可见 Invert Binary Tree 是一个自然的递归操作，可用递归方式求解。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 n 为节点数，h 为树高。
     * */
    public static TreeNode invertTree(TreeNode root) {
        if (root == null) return null;
        TreeNode temp = root.left;
        root.left = invertTree(root.right);
        root.right = invertTree(temp);
        return root;
    }

    /*
     * 解法2：Iteration (BFS)
     * - 时间复杂度 O(n)，其中 n 为节点数。
     * - 空间复杂度 O(n)，∵ q 中同时最多容纳 n/2 个节点（即完美二叉树的最后一行）∴ 是 O(n) 级别。
     * */
    public static TreeNode invertTree2(TreeNode root) {
        if (root == null) return null;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            TreeNode node = q.poll();
            TreeNode temp = node.left;
            node.left = node.right;
            node.right = temp;
            if (node.left != null) q.offer(node.left);
            if (node.right != null) q.offer(node.right);
        }

        return root;
    }

    /*
     * 解法3：Iteration (DFS)
     * - 思路：逻辑与解法2几乎完全一致，只是数据结构改为了 Stack。可见使用 Queue/Stack 决定了访问节点的顺序，即 BFS/DFS。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static TreeNode invertTree3(TreeNode root) {
        if (root == null) return null;
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            TreeNode temp = node.left;
            node.left = node.right;
            node.right = temp;
            if (node.left != null) stack.push(node.left);
            if (node.right != null) stack.push(node.right);
        }

        return root;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{4, 2, 7, 1, 3, 6, 9});
        printBinaryTreeBreadthFirst(invertTree(t1));
        /*
         * expects [4, 7, 2, 9, 6, 3, 1].
         *         4                  4
         *       /   \              /   \
         *      2     7    --->    7     2
         *     / \   / \          / \   / \
         *    1   3 6   9        9   6 3   1
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2});
        printBinaryTreeBreadthFirst(invertTree(t2));
        /*
         * expects [1, null, 2].
         *      1              1
         *     /      --->      \
         *    2                  2
         * */
    }
}
