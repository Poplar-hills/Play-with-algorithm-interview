package BinaryTreeAndRecursion.Basics;

import static Utils.Helpers.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.function.Consumer;

import javafx.util.Pair;
import Utils.Helpers.TreeNode;

/*
 * Balanced Binary Tree
 *
 * - Given a binary tree, determine if it is height-balanced.
 * - For this problem, height-balanced means the depth of the two subtrees of every node never differ by more than 1.
 * */

public class L110_BalancedBinaryTree {
    /*
     * 解法1：Recursion
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static boolean isBalanced(TreeNode root) {
        return maxDepth(root) != -1;
    }

    private static int maxDepth(TreeNode root) {  // 计算左右子树的最大高度，若高度差 > 1，则返回 -1，否则返回最大高度
        if (root == null) return 0;
        int left = maxDepth(root.left);
        if (left == -1) return -1;
        int right = maxDepth(root.right);
        if (right == -1 || Math.abs(left - right) > 1) return -1;
        return 1 + Math.max(left, right);
    }

    /*
     * 解法2：Iteration (post-order traversal)
     * - 思路：要知道一棵树是否平衡，需要先知道其左右子树的最大高度，即先访问左右子节点，再访问父节点，这本质上就是二叉树的后续
     *   遍历。因此需要做的就是在后续遍历的基础上将访问每个节点的逻辑替换成计算树的最大高度的逻辑即可。
     * - 💎：该解法是二叉树后续遍历的典型应用（后续遍历的另一种方法 SEE: Play-with-data-structure/BST/BST.java）。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static boolean isBalanced2(TreeNode root) {
        if (root == null) return true;
        Map<TreeNode, Integer> map = new HashMap<>();  // 使用 map 记录每个节点的最大高度
        Stack<TreeNode> stack = new Stack<>();         // 后续遍历也是 DFS，因此使用 stack 结构进行辅助
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            boolean isLeafNode = node.left == null && node.right == null;
            boolean leftDone = map.containsKey(node.left);
            boolean rightDone = map.containsKey(node.right);
            boolean childrenDone = (leftDone && rightDone) || (node.left == null && rightDone) || (node.right == null && leftDone);

            if (isLeafNode || childrenDone) {  // 若是叶子节点，或左右子子树已经被访问过，则访问当前节点，并加入 map
                int leftDepth = map.getOrDefault(node.left, 0);
                int rightDepth = map.getOrDefault(node.right, 0);
                if (Math.abs(leftDepth - rightDepth) > 1) return false;
                map.put(node, 1 + Math.max(leftDepth, rightDepth));
            } else {                                        // 若左右子树中还有没计算过的，则继续递归
                stack.push(node);  // 将该节点再次入栈
                if (node.left != null) stack.push(node.left);
                if (node.right != null) stack.push(node.right);
            }
        }

        return true;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, null, null, 3, 3});
        log(isBalanced2(t1));
        /*
         * expects true.
         *        1
         *       / \
         *      2   2
         *         / \
         *        3   3
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, 3, null, 3, 3});
        log(isBalanced2(t2));
        /*
         * expects true.
         *        1
         *       / \
         *      2   2
         *     /   / \
         *    3   3   3
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, 3, 3, null, 3, 4, 4});
        log(isBalanced2(t3));
        /*
         * expects true. (注意这个是平衡树 ∵ 本题中平衡树的定义是任意节点的左右子树的最大高度的差 < 1)
         *           1
         *          / \
         *         2   2
         *        / \   \
         *       3   3   3
         *      / \
         *     4   4
         * */

        TreeNode t4 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, 3, 3, null, null, 4, 4});
        log(isBalanced2(t4));
        /*
         * expects false.
         *           1
         *          / \
         *         2   2
         *        / \
         *       3   3
         *      / \
         *     4   4
         * */

        TreeNode t5 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, 3, null, null, 3, 4, null, null, 4});
        log(isBalanced2(t5));
        /*
         * expects false.
         *           1
         *          / \
         *         2   2
         *        /     \
         *       3       3
         *      /         \
         *     4           4
         * */
    }
}
