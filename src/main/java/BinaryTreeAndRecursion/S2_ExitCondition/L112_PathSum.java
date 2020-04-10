package BinaryTreeAndRecursion.S2_ExitCondition;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.*;

import java.util.LinkedList;
import java.util.Queue;
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
     * 解法1：DFS (Recursion)
     * - 注意：递归过程中，若在非叶子节点上有 sum == 0，不能就此返回 true，因为这不是一条 root-to-leaf path。所以递归终止条
     *   件不能写成 if (root == null) return sum == 0; 还需加入该节点是否是叶子节点的判断才行。
     * - 👉 递归终止条件较复杂的还有 L111 的解法1，可以顺便看一下。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static boolean hasPathSum(TreeNode root, int sum) {
        if (root == null) return false;
        if (root.left == null && root.right == null)
            return sum == root.val;
        return hasPathSum(root.left, sum - root.val) || hasPathSum(root.right, sum - root.val);
    }

    /*
     * 解法2：DFS (Iteration)
     * - 思路：与解法1一致，都是在访问节点时检查该节点是否是能使剩余 sum 得0的叶子节点，若是则说明找到解。
     * - 实现：采用两个 Stack 分别记录节点和当前路径的剩余 sum。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean hasPathSum2(TreeNode root, int sum) {
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

    /*
     * 解法3：BFS (Iteration)
     * - 思路：与解法1、2一致。
     * - 实现：采用 BFS 的迭代实现，使用 Queue 同时记录节点和剩余 sum。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean hasPathSum3(TreeNode root, int sum) {
        if (root == null) return false;
        Queue<Pair<TreeNode, Integer>> q = new LinkedList<>();
        q.offer(new Pair<>(root, sum));

        while (!q.isEmpty()) {
            Pair<TreeNode, Integer> pair = q.poll();
            TreeNode node = pair.getKey();
            int remainingSum = pair.getValue();

            if (node.left == null && node.right == null && remainingSum == node.val)
                return true;

            if (node.left != null)
                q.offer(new Pair<>(node.left, remainingSum - node.val));
            if (node.right != null)
                q.offer(new Pair<>(node.right, remainingSum - node.val));
        }

        return false;
    }

    /*
     * 解法4：BFS (Recursion)
     * - 思路：与解法1、2、3一致。
     * - 实现：采用 BFS 的递归实现，递归对象是 queue，递归退出条件是找到解或 q 中元素耗尽。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean hasPathSum4(TreeNode root, int sum) {
        if (root == null) return false;
        Queue<Pair<TreeNode, Integer>> q = new LinkedList<>();
        q.offer(new Pair<>(root, sum));
        return helper4(q);
    }

    private static boolean helper4(Queue<Pair<TreeNode, Integer>> q) {
        if (q.isEmpty()) return false;  // 若 q 中节点已经耗尽则说明无解

        Pair<TreeNode, Integer> pair = q.poll();
        TreeNode node = pair.getKey();
        int remindingSum = pair.getValue();

        if (node.left == null && node.right == null && remindingSum == node.val)
            return true;                // 若叶子节点符合条件则返回 true，否则也不返回 false，而是继续递归，从 q 中出队
                                        // 下一个节点（若这里返回 false 则会提前退出递归，导致无法遍历 q 中剩下的节点）
        if (node.left != null) q.offer(new Pair<>(node.left, remindingSum - node.val));
        if (node.right != null) q.offer(new Pair<>(node.right, remindingSum - node.val));
        return helper4(q);
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{5, 4, 8, 11, null, 13, 4, 7, 2, null, null, null, 1});
        log(hasPathSum4(t1, 22));
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
        log(hasPathSum4(t2, -1));
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
        log(hasPathSum4(t3, 9));
        /*
         * expects false. (注意：5 -> 4 虽然和为9，但不是一条 root-to-leaf path)
         *         5
         *        / \
         *       4   8
         *      /   / \
         *     11  13  4
         * */

        TreeNode t4 = createBinaryTreeBreadthFirst(new Integer[]{});
        log(hasPathSum4(t4, 1));
        /*
         * expects false.
         * */
    }
}
