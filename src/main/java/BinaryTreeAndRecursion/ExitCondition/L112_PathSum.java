package BinaryTreeAndRecursion.ExitCondition;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.*;

import java.util.Stack;

import Utils.Helpers.TreeNode;

/*
 * Path Sum
 *
 * - Given a binary tree and a sum, determine if the tree has a root-to-leaf path such that adding up all the
 *   values along the path equals the given sum.
 * */

public class L112_PathSum {
    /*
     * 解法1：Recursion (DFS)
     * - 注意：递归过程中，若在非叶子节点上有 sum == 0，不能就此返回 true，因为这不是一条 root-to-leaf path。所以递归终止条
     *   件不能写成 if (root == null) return sum == 0; 还需加入该节点是否是叶子节点的判断才行。
     * - 👉 递归终止条件较复杂的还有 L111 的解法1，可以顺便看一下。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static boolean hasPathSum(TreeNode root, int sum) {
        if (root == null) return false;
        if (root.left == null && root.right == null) return sum == root.val;
        return hasPathSum(root.left, sum - root.val) || hasPathSum(root.right, sum - root.val);
    }

    /*
     * 解法2：Iteration (DFS)
     * - 思路：stack 中除了记录节点用于遍历之外，还需记录路径的剩余 sum。遍历过程中若碰到能使剩余 sum 得0的叶子节点，则说明找到目标路径。
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
     * 解法3：Iteration (DFS)
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
        log(hasPathSum(t1, 22));
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
        log(hasPathSum(t2, -1));
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
        log(hasPathSum(t3, 9));
        /*
         * expects false. (注意：5 -> 4 虽然和为9，但不是一条 root-to-leaf path)
         *         5
         *        / \
         *       4   8
         *      /   / \
         *     11  13  4
         * */

        TreeNode t4 = createBinaryTreeBreadthFirst(new Integer[]{});
        log(hasPathSum(t4, 1));
        /*
         * expects false.
         * */
    }
}
