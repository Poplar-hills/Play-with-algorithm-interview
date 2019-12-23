package BinaryTreeAndRecursion.S1_Basics;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

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
     * 解法1：Recursion (DFS)
     * - 思路：先考虑求一个节点最小深度的所有可能的情况：
     *   1. 若左右子树都没有，则该节点的最小深度为1；
     *   2. 若左右子树都有，则该节点的最小深度由两边深度更小的一边决定；
     *   3. 若只有左子树或只有右子树，则该节点的最小深度由有子树的一边决定。
     *   注意，情况3是最容易忽略的情况，若 test case 2 中不考虑该情况则会得到错误结果1；
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int minDepth(TreeNode root) {
        if (root == null) return 0;
        if (root.left == null) return 1 + minDepth(root.right);  // 若左右子树只有一边时，最小深度由有子树的一边决定
        if (root.right == null) return 1 + minDepth(root.left);
        return 1 + Math.min(minDepth(root.left), minDepth(root.right));
    }

    /*
     * 解法2：Iteration (BFS, Level-order Traversal)
     * - 思路：对树进行层序遍历的过程中，若碰到叶子节点，则该叶子节点所在的等深度就是该二叉树的最小深度。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。该解法统计性能应比解法1好 ∵ 遇到叶子节点会立即结束遍历，而解法1会遍历到底后再返回。
     * */
    public static int minDepth2(TreeNode root) {
        if (root == null) return 0;
        int depth = 1;  // 只要根节点不为 null 则最小树高就至少是1
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            int levelSize = q.size();
            while (--levelSize >= 0) {  // 将同一层的节点一次性消费完
                TreeNode node = q.poll();
                if (node.left == null && node.right == null) return depth;  // 若碰到叶子节点则找到了最小深度，提前结束遍历
                if (node.left != null) q.offer(node.left);
                if (node.right != null) q.offer(node.right);
            }
            depth++;                    // 消费完同一层节点还没有碰到叶子节点则给最小深度+1
        }

        return depth;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, null, 15, 7});
        log(minDepth(t1));
        /*
         * expects 2.
         *      3
         *     / \
         *    9  20
         *      /  \
         *     15   7
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2});
        log(minDepth(t2));
        /*
         * expects 2. (左右子树只有一边的情况)
         *      1
         *     /
         *    2
         * */
    }
}
