package BinaryTreeAndRecursion.Basics;

import static Utils.Helpers.*;

import java.util.LinkedList;
import java.util.Queue;

import Utils.Helpers.TreeNode;

/*
 * Minimum Depth of Binary Tree
 *
 * - Given a binary tree, find its minimum depth.
 * - The minimum depth is the number of nodes along the shortest path from the root to the nearest leaf node.
 * */

public class L111_MinimumDepthOfBinaryTree {
    /*
     * 解法1：Recursion
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 n 为节点数，h 为树高。
     * */
    public static int minDepth(TreeNode root) {
        if (root == null) return 0;
        if (root.left == null) return 1 + minDepth(root.right);
        if (root.right == null) return 1 + minDepth(root.left);
        return 1 + Math.min(minDepth(root.left), minDepth(root.right));
    }

    /*
     * 解法2：BFS (Level-order Traversal)
     * - 思路：对树进行层序遍历的过程中，若碰到叶子节点则提前结束遍历，返回树高，此时的树高即是 minimum depth。
     * - 时间复杂度 O(n)，空间复杂度 O(n)，其中 n 为节点数。该解法统计性能应比解法1更快 ∵ 遇到叶子节点后就会结束遍历，而解法1
     *   会遍历到底后再返回。
     * */
    public static int minDepth2(TreeNode root) {
        if (root == null) return 0;

        int depth = 1;  // 根节点不为 null 则最小树高就是1
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            int levelSize = q.size();
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = q.poll();
                if (node.left == null && node.right == null) return depth;  // 若碰到叶子节点则提前结束遍历，返回树高
                if (node.left != null) q.offer(node.left);
                if (node.right != null) q.offer(node.right);
            }
            depth++;
        }

        return depth;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, null, 15, 7});
        log(minDepth2(t1));
        /*
         *  expects 2
         *      3
         *     / \
         *    9  20
         *      /  \
         *     15   7
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2});
        log(minDepth2(t2));
        /*
         *  expects 2
         *      1
         *     /
         *    2
         * */
    }
}
