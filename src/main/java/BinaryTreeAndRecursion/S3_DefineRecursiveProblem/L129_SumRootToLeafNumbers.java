package BinaryTreeAndRecursion.S3_DefineRecursiveProblem;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.*;

import java.util.Stack;

import Utils.Helpers.TreeNode;

/*
 * Sum Root to Leaf Numbers
 *
 * - Given a binary tree containing digits from 0-9 only, each root-to-leaf path could represent a number.
 *   For example the root-to-leaf path 1->2->3 represents the number 123. Find the total sum of all
 *   root-to-leaf numbers.
 * */

public class L129_SumRootToLeafNumbers {
    /*
     * 解法1：DFS (Recursion)
     * - 思路：从根节点开始从上到下逐层累积当前 path 的 pathNum，当到达叶子节点时加到外部的 sum 上去。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    private static int sum;

    public static int sumNumbers(TreeNode root) {
        sum = 0;
        helper(root, root.val);
        return sum;
    }

    private static void helper(TreeNode root, int pathNum) {
        if (root == null) return;
        if (root.left == null && root.right == null) {
            sum += pathNum;
            return;
        }
        helper(root.left, pathNum * 10 + root.left.val);
        helper(root.right, pathNum * 10 + root.right.val);
    }

    /*
     * 解法2：Recursion (DFS)
     * - 思路：从根节点开始逐层累积当前 path 的 pathNum，直到叶子节点开始逐层返回，具体返回的是左、右子树的所有 pathNum 之和。
     *   根据该思路，需将每层累积的 pathNum 当做参数传递给下层递归函数。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int sumNumbers2(TreeNode root) {
        return helper2(root, 0);
    }

    private static int helper2(TreeNode root, int pathNum) {  // 返回以 root 为根的二叉树的所有 pathNum 之和
        if (root == null) return 0;
        pathNum = pathNum * 10 + root.val;
        if (root.left == null && root.right == null) return pathNum;
        return helper2(root.left, pathNum) + helper2(root.right, pathNum);
    }

    /*
     * 解法3：Iteration (DFS) (解法2的非递归版)
     * - 同理：只需将 Stack 替换为 Queue 就得到了 BFS 解法。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int sumNumbers3(TreeNode root) {
        if (root == null) return 0;
        int sum = 0;
        Stack<Pair<TreeNode, Integer>> stack = new Stack<>();  // 存储 <节点, 当前节点的 pathSum>
        stack.push(new Pair<>(root, root.val));

        while (!stack.isEmpty()) {
            Pair<TreeNode, Integer> pair = stack.pop();
            TreeNode node = pair.getKey();
            int pathNum = pair.getValue();

            if (node.left == null && node.right == null)
                sum += pathNum;
            if (node.left != null)
                stack.push(new Pair<>(node.left, pathNum * 10 + node.left.val));
            if (node.right != null)
                stack.push(new Pair<>(node.right, pathNum * 10 + node.right.val));
        }

        return sum;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3});
        log(sumNumbers(t1));
        /*
         * expects 25. (12 + 13)
         *        1
         *       / \
         *      2   3
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{4, 9, 0, 5, 1});
        log(sumNumbers(t2));
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
