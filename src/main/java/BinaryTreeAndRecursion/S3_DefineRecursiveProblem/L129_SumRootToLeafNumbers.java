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
     *            4                   f(4,0)
     *           / \                  ↙    ↘
     *          9   0    --->     f(9,4)  f(0,4)    - sum += 40
     *         / \                ↙    ↘
     *        5   1          f(5,49)  f(1,49)       - sum += 495; sum += 491;
     *
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    private static int sum;

    public static int sumNumbers(TreeNode root) {
        sum = 0;
        helper(root, 0);
        return sum;
    }

    private static void helper(TreeNode root, int pathNum) {
        if (root == null) return;
        pathNum = pathNum * 10 + root.val;

        if (root.left == null && root.right == null) {
            sum += pathNum;
            return;
        }
        helper(root.left, pathNum);
        helper(root.right, pathNum);
    }

    /*
     * 解法2：DFS (Recursion) (解法1的简化版)
     * - 思路：与解法1一致，也是从上到下在每个节点上累积当前 path 的 pathNum。
     * - 实现：与解法1不同，该解法不使用外部变量记录，而是让每个递归函数 f(n, num) 都返回以 num 为基数、以 n 为根的二叉树的
     *   sum of root to leaf numbers，即每个递归函数是一个完整的子问题，从而最终从下到上递推出原问题的解：
     *            4                  f(4,0)                    1026
     *           / \                 ↙    ↘                   ↗    ↖
     *          9   0    --->    f(9,4)  f(0,4)    --->     986     40
     *         / \               ↙    ↘                    ↗   ↖
     *        5   1         f(5,49)  f(1,49)             495   491
     *
     * - 💎 总结：相比解法1，该解法更加函数式：
     *   1. ∵ 没有外部遍历 ∴ 无副作用；
     *   2. ∵ 递归函数的语义中描述了返回值 ∴ 每个递归函数都是原问题的一个完整的子问题，是“真递归”。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int sumNumbers2(TreeNode root) {
        return helper2(root, 0);
    }

    private static int helper2(TreeNode root, int pathNum) {  // 返回以 pathNum 为基数、以 root 为根的二叉树的 root-to-leaf numbers 之和
        if (root == null) return 0;
        pathNum = pathNum * 10 + root.val;
        if (root.left == null && root.right == null) return pathNum;
        return helper2(root.left, pathNum) + helper2(root.right, pathNum);
    }

    /*
     * 解法3：DFS (Iteration)
     * - 思路：与解法1、2一致，都是将当前路径的 pathNum 带在每个节点上，一层层往下传递。
     * - 实现：只需将 Stack 替换为 Queue 就得到了 BFS 解法。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int sumNumbers3(TreeNode root) {
        if (root == null) return 0;
        int sum = 0;
        Stack<Pair<TreeNode, Integer>> stack = new Stack<>();  // 存储 <节点, 当前节点的 pathSum>
        stack.push(new Pair<>(root, 0));

        while (!stack.isEmpty()) {
            Pair<TreeNode, Integer> pair = stack.pop();
            TreeNode node = pair.getKey();
            int pathNum = pair.getValue() * 10 + node.val;

            if (node.left == null && node.right == null)
                sum += pathNum;
            if (node.left != null)
                stack.push(new Pair<>(node.left, pathNum));
            if (node.right != null)
                stack.push(new Pair<>(node.right, pathNum));
        }

        return sum;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3});
        log(sumNumbers3(t1));
        /*
         * expects 25. (12 + 13)
         *        1
         *       / \
         *      2   3
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{4, 9, 0, 5, 1});
        log(sumNumbers3(t2));
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
