package BinaryTreeAndRecursion.S1_Basics;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import java.util.LinkedList;
import java.util.Queue;

import Utils.Helpers.*;

/*
 * Minimum Depth of Binary Tree
 *
 * - Given a binary tree, find its minimum depth.
 * - The minimum depth is the number of nodes along the shortest path from the root to the nearest leaf node.
 * */

public class L111_MinimumDepthOfBinaryTree {
    /*
     * 解法1：DFS (Recursion)
     * - 思路：与 L104_MaximumDepthOfBinaryTree 一样，用 DFS 查找树的最小深度有两种思路：
     *   1. 从上到下遍历所有分支，从根节点开始向下层层传递节点的深度，在每找到一个叶子节点时就检查/更新最小深度；
     *   2. 从下到上层层递推每个节点的最小深度 —— 每个节点的最小深度 = min(左子树最小深度, 右子树最小深度) + 1。
     * - 实现：本解法采用思路1。
     * - 💎 总结：👆两种方向的 DFS 是非常好的思路归纳（类似的题 SEE: L113_PathSumII 解法1-3）。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    private static int minDepth;

    public static int minDepth(TreeNode root) {
        if (root == null) return 0;
        minDepth = Integer.MAX_VALUE;
        helper(root, 1);
        return minDepth;
    }

    private static void helper(TreeNode root, int level) {
        if (root == null) return;                     // 遇到空节点不处理
        if (root.left == null && root.right == null)  // 遇到叶子节点更新 minDepth
            minDepth = Math.min(minDepth, level);
        helper(root.left, level + 1);
        helper(root.right, level + 1);
    }

    /*
     * 解法2：DFS (Recursion)
     * - 思路：采用解法1中的思路2 —— 从下到上层层递推：每个节点的最小深度 = min(左子树最小深度, 右子树最小深度) + 1。
     *   但要注意，根据题中对 minimum depth 的定义可知，当一个节点只有左/右子树中的一边，而另一边为空时，该节点的最小深度
     *   并非是 min(0, 非空子树最小深度) + 1 = 1，而应该取决于非空子树那一边的最小深度（test case 2） ∴ 需要分情况讨论：
     *     1. 若左右子树同时存在，则该节点的最小深度 = min(左子树最小深度, 右子树最小深度) + 1；
     *     2. 若左右子树都不存在，则该节点的最小深度 = 1；
     *     3. 若左右子树只存在一边，则该节点的最小深度 = 非空子树的最小深度 + 1。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int minDepth2(TreeNode root) {
        if (root == null) return 0;
        if (root.left == null) return minDepth(root.right) + 1;
        if (root.right == null) return minDepth(root.left) + 1;
        return Math.min(minDepth(root.left), minDepth(root.right)) + 1;
    }

    /*
     * 解法3：BFS
     * - 思路：使用 BFS 对树进行层序遍历，找到的第一个叶子节点的深度即是整个树的最小深度。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int minDepth3(TreeNode root) {
        if (root == null) return 0;
        Queue<Pair<TreeNode, Integer>> q = new LinkedList<>();
        q.offer(new Pair<>(root, 1));

        while (!q.isEmpty()) {
            Pair<TreeNode, Integer> p = q.poll();
            TreeNode node = p.getKey();
            int depth = p.getValue();

            if (node.left == null && node.right == null)
                return depth;

            if (node.left != null)
                q.offer(new Pair<>(node.left, depth + 1));
            if (node.right != null)
                q.offer(new Pair<>(node.right, depth + 1));
        }

        throw new IllegalArgumentException("No solution");
    }

    /*
     * 解法4：BFS
     * - 思路：与解法3一致。
     * - 实现：采用一次性遍历一层节点的方式。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int minDepth4(TreeNode root) {
        if (root == null) return 0;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        int depth = 1;                 // 只要根节点不为 null 则最小树高就至少是1

        while (!q.isEmpty()) {
            int levelSize = q.size();
            while (levelSize-- > 0) {  // 将同一层的节点一次性消费完
                TreeNode node = q.poll();
                if (node.left == null && node.right == null) return depth;  // 若碰到叶子节点则找到了最小深度，提前结束遍历
                if (node.left != null) q.offer(node.left);
                if (node.right != null) q.offer(node.right);
            }
            depth++;                   // 消费完同一层节点还没有碰到叶子节点则给最小深度+1
        }

        return depth;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, null, 15, 7});
        log(minDepth2(t1));
        /*
         * expects 2.
         *      3
         *     / \
         *    9  20
         *      /  \
         *     15   7
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2});
        log(minDepth2(t2));
        /*
         * expects 2. (左右子树只有一边的情况)
         *      1
         *     /
         *    2
         * */
    }
}
