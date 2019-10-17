package BinaryTreeAndRecursion.ExitCondition;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import java.util.Stack;

import Utils.Helpers.TreeNode;
import javafx.util.Pair;

/*
 * Path Sum
 *
 * - Given a binary tree and a sum, determine if the tree has a root-to-leaf path such that adding up all the
 *   values along the path equals the given sum.
 * */

public class L112_PathSum {
    /*
     * 解法1：Recursion
     * - 注意点：
     *   1. 递归的终止条件很容易写成 if (root == null) return sum == 0; 但这样 test case 3 过不去，因此递归终止条件需要
     *      用叶子节点来判断。
     *   2. 递归过程中，若碰到非叶子节点上 sum == 0 的情况，不能就此返回 false，因为该路径后面可能还有+1再-1的情况，如 test case 2。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static boolean hasPathSum(TreeNode root, int sum) {
        if (root == null) return false;
        if (root.left == null && root.right == null) return sum == root.val;
        return hasPathSum(root.left, sum - root.val) || hasPathSum(root.right, sum - root.val);
    }

    /*
     * 解法2：Iteration
     * - 思路：若采用非递归的解法，首先可以确定应使用 DFS ∴ 采用 stack 作为辅助数据结构。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean hasPathSum2(TreeNode root, int sum) {
        if (root == null) return false;
        Stack<Pair<TreeNode, Integer>> stack = new Stack<>();
        stack.push(new Pair<>(root, sum));

        while (!stack.isEmpty()) {
            Pair<TreeNode, Integer> pair = stack.pop();
            TreeNode node = pair.getKey();
            int remainingSum = pair.getValue();

            if (node.left == null && node.right == null && remainingSum == node.val)
                return true;

            if (node.left != null)
                stack.push(new Pair<>(node.left, remainingSum - node.val));
            if (node.right != null)
                stack.push(new Pair<>(node.right, remainingSum - node.val));
        }

        return false;
    }

    /*
     * 解法3：Iteration
     * - 思路：与解法2完全一致，只是采用两个 stack 实现。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean hasPathSum3(TreeNode root, int sum) {
        if (root == null) return false;
        Stack<TreeNode> s1 = new Stack<>();  // node stack
        Stack<Integer> s2 = new Stack<>();   // sum stack
        s1.push(root);
        s2.push(sum);

        while (!s1.isEmpty()) {
            TreeNode node = s1.pop();
            int remainingSum = s2.pop();

            if (node.left == null && node.right == null && remainingSum == node.val)
                return true;

            if (node.left != null) {
                s1.push(node.left);
                s2.push(remainingSum - node.val);
            }
            if (node.right != null) {
                s1.push(node.right);
                s2.push(remainingSum - node.val);
            }
        }

        return false;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{5, 4, 8, 11, null, 13, 4, 7, 2, null, null, null, 1});
        log(hasPathSum3(t1, 22));
        /*
         * expects true. (5 -> 4 -> 11 -> 2)
         *         5
         *        / \
         *       4   8
         *      /   / \
         *     11  13  4
         *    /  \      \
         *   7    2      1
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, -2, -3, 1, 3, -2, null, -1});
        log(hasPathSum3(t2, -1));
        /*
         * expects true. (1 -> -2 -> 1 -> -1)
         *          1
         *        /   \
         *      -2    -3
         *      / \   /
         *     1  3  -2
         *    /
         *   -1
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{5, 4, 8, 11, null, 13, 4});
        log(hasPathSum3(t3, 9));
        /*
         * expects false. (注意：5 -> 4 虽然和为9，但不是一条 root-to-leaf path)
         *         5
         *        / \
         *       4   8
         *      /   / \
         *     11  13  4
         * */

        TreeNode t4 = createBinaryTreeBreadthFirst(new Integer[]{});
        log(hasPathSum3(t4, 1));
        /*
         * expects false.
         * */
    }
}
