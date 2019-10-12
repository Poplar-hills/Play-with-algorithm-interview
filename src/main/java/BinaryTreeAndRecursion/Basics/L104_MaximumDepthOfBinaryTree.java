package BinaryTreeAndRecursion.Basics;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javafx.util.Pair;

/*
 * Maximum Depth of Binary Tree
 *
 * - Given a binary tree, find its maximum depth.
 * */

public class L104_MaximumDepthOfBinaryTree {
    /*
     * 解法1：Recursion
     * - 时间复杂度 O(n)；
     * - 空间复杂度：最好情况（平衡树）时为 O(logn)；最坏情况（退化为链表）时为 O(n)。
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

        int count = 0;
        Queue<Pair<TreeNode, Integer>> q = new LinkedList<>();
        q.offer(new Pair<>(root, 1));

        while (!q.isEmpty()) {
            Pair<TreeNode, Integer> pair = q.poll();
            TreeNode node = pair.getKey();
            int level = pair.getValue();

            count = Math.max(count, level);
            if (node.left != null) q.offer(new Pair<>(node.left, level + 1));
            if (node.right != null) q.offer(new Pair<>(node.right, level + 1));
        }

        return count;
    }

    public static void main(String[] args) {
        TreeNode t = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, null, 15, 7});
        log(maxDepth3(t));
        /*
         *  expects 3.
         *      3
         *     / \
         *    9  20
         *      /  \
         *     15   7
         * */
    }
}
