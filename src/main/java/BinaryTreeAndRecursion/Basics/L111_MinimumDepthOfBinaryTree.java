package BinaryTreeAndRecursion.Basics;

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
     * 解法1：Recursion
     * - 注意：列出单个节点的所有情况：
     *   1. 若左右子树都没有，则该节点上的最小深度为1；
     *   2. 若左右子树只有一边，则该节点上的最小深度由有子树的一边决定；
     *   3. 若左右子树两遍都有，则该节点上的最小深度由两边深度更小的一边决定；
     *   ∴ 递归的终止条件不能光是 root == null，否则情况2（即 test case 2）会挂。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int minDepth(TreeNode root) {
        if (root == null) return 0;
        if (root.left == null) return 1 + minDepth(root.right);  // 若左右子树只有一边时，最小深度由有子树的一边决定
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
        log(minDepth(t1));
        /*
         *  expects 2.
         *      3
         *     / \
         *    9  20
         *      /  \
         *     15   7
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2});
        log(minDepth(t2));
        /*
         *  expects 2. (左右子树只有一边的情况)
         *      1
         *     /
         *    2
         * */
    }
}
