package BinaryTreeAndRecursion.DefineRecursiveProblem;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import java.util.Stack;

import Utils.Helpers.TreeNode;
import javafx.util.Pair;

/*
 * Sum Root to Leaf Numbers
 *
 * - Given a binary tree containing digits from 0-9 only, each root-to-leaf path could represent a number.
 *   For example the root-to-leaf path 1->2->3 represents the number 123. Find the total sum of all
 *   root-to-leaf numbers.
 * */

public class L129_SumRootToLeafNumbers {
    /*
     * 解法1：Recursion (DFS)
     * - 思路：递归函数 f(n) 定义：返回以 n 为根的二叉树的所有 root-to-leaf numbers 之和。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int sumNumbers(TreeNode root) {
        return helper(root, 0);
    }

    private static int helper(TreeNode root, int pathNum) {
        if (root == null) return 0;
        pathNum = pathNum * 10 + root.val;
        if (root.left == null && root.right == null) return pathNum;
        return helper(root.left, pathNum) + helper(root.right, pathNum);
    }

    /*
     * 解法2：Iteration (DFS)
     * - 思路：使用 Stack 实现深度优先遍历，向下遍历的同时累加 pathSum。
     * - 同理：只需将 Stack 替换为 Queue 就得到了 BFS 解法。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int sumNumbers2(TreeNode root) {
        if (root == null) return 0;
        int sum = 0;
        Stack<Pair<TreeNode, Integer>> stack = new Stack<>();
        stack.push(new Pair<>(root, root.val));

        while (!stack.isEmpty()) {
            Pair<TreeNode, Integer> pair = stack.pop();
            TreeNode node = pair.getKey();
            int pathSum = pair.getValue();

            if (node.left == null && node.right == null) {
                sum += pathSum;
                continue;
            }

            if (node.left != null) {
                int leftPathSum = pathSum * 10 + node.left.val;
                stack.push(new Pair<>(node.left, leftPathSum));
            }
            if (node.right != null) {
                int rightPathSum = pathSum * 10 + node.right.val;
                stack.push(new Pair<>(node.right, rightPathSum));
            }
        }

        return sum;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3});
        log(sumNumbers2(t1));
        /*
         * expects 25. (12 + 13)
         *        1
         *       / \
         *      2   3
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{4, 9, 0, 5, 1});
        log(sumNumbers2(t2));
        /*
         * expects 1026. (495 + 491 + 40)
         *        4
         *       / \
         *      9   0
         *     / \
         *    5   1
         * */
    }
}
