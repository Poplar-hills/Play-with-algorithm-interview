package BinaryTreeAndRecursion.S1_Basics;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/*
 * Maximum Depth of Binary Tree
 *
 * - Given a binary tree, find its maximum depth.
 * */

public class L104_MaximumDepthOfBinaryTree {
    /*
     * 解法1：Recursion (DFS)
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int maxDepth(TreeNode root) {
        if (root == null) return 0;
        return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
    }

    /*
     * 解法2：Iteration (BFS)
     * - 思路：逻辑与 L102_BinaryTreeLevelOrderTraversal 解法1一致。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int maxDepth2(TreeNode root) {
        if (root == null) return 0;
        List<List<TreeNode>> res = new ArrayList<>();
        Queue<Pair<TreeNode, Integer>> q = new LinkedList<>();
        q.offer(new Pair<>(root, 0));

        while (!q.isEmpty()) {
            Pair<TreeNode, Integer> pair = q.poll();
            TreeNode node = pair.getKey();
            int level = pair.getValue();

            if (level == res.size())
                res.add(new ArrayList<>());

            List<TreeNode> list = res.get(level);
            list.add(node);

            if (node.left != null) q.offer(new Pair<>(node.left, level + 1));
            if (node.right != null) q.offer(new Pair<>(node.right, level + 1));
        }

        return res.size();
    }

    /*
     * 解法3：Iteration (BFS)（解法2的简化版）
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int maxDepth3(TreeNode root) {
        if (root == null) return 0;
        int depth = 0;
        Queue<Pair<TreeNode, Integer>> q = new LinkedList<>();
        q.offer(new Pair<>(root, 1));

        while (!q.isEmpty()) {
            Pair<TreeNode, Integer> pair = q.poll();
            TreeNode node = pair.getKey();
            int level = pair.getValue();

            depth = Math.max(depth, level);
            if (node.left != null) q.offer(new Pair<>(node.left, level + 1));
            if (node.right != null) q.offer(new Pair<>(node.right, level + 1));
        }

        return depth;
    }

    /*
     * 解法4：Iteration (BFS)（解法3的简化版）
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int maxDepth4(TreeNode root) {
        if (root == null) return 0;
        int depth = 0;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            int qSize = q.size();
            while (qSize-- > 0) {  // 一次性将 q 中同一层的节点都消费完
                TreeNode node = q.poll();
                if (node.left != null) q.offer(node.left);
                if (node.right != null) q.offer(node.right);
            }
            depth++;               // 消费完一层的节点后就可以让 depth++
        }

        return depth;
    }

    /*
     * 解法5：Iteration (DFS)
     * - 思路：∵ 深度优先遍历 ∴ 无法采用解法4的方式一次性将一层节点全部遍历完 ∴ 只能采用解法3的方式将层级信息携带在节点上。
     *   跟解法3的区别只是采用了 Stack。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int maxDepth5(TreeNode root) {
        if (root == null) return 0;

        int depth = 0;
        Stack<Pair<TreeNode, Integer>> stack = new Stack<>();
        stack.push(new Pair<>(root, 1));

        while (!stack.isEmpty()) {
            Pair<TreeNode, Integer> pair = stack.pop();
            TreeNode node = pair.getKey();
            int level = pair.getValue();

            depth = Math.max(depth, level);
            if (node.left != null) stack.push(new Pair<>(node.left, level + 1));
            if (node.right != null) stack.push(new Pair<>(node.right, level + 1));
        }

        return depth;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, null, 15, 7});
        log(maxDepth5(t1));
        /*
         * expects 3.
         *      3
         *     / \
         *    9  20
         *      /  \
         *     15   7
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, 4, null, null, 5});
        log(maxDepth5(t2));
        /*
         * expects 3.
         *      1
         *     / \
         *    2   3
         *   /     \
         *  4       5
         * */
    }
}
