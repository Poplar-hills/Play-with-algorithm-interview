package BinaryTreeAndRecursion.S2_ExitCondition;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import java.util.LinkedList;
import java.util.Queue;

import Utils.Helpers.Pair;
import Utils.Helpers.TreeNode;

/*
 * Sum of Left Leaves
 *
 * - Find the sum of all left leaves in a given binary tree.
 * */

public class L404_SumOfLeftLeaves {
    /*
     * 解法1：BFS with mark
     * - 思路：
     *   1. 该题本质上是遍历树上节点，在碰到左叶子节点时累计节点值 ∴ 可在遍历二叉树的基础上修改访问节点的逻辑；
     *   2. 要累加的是左叶子节点 ∴ 很容易想到通过给每个节点打标记来标明一个节点是否是左子节点，然后再访问节点时判断是否同时满足
     *      叶子节点、左子节点这两个条件。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int sumOfLeftLeaves(TreeNode root) {
        if (root == null) return 0;
        int sum = 0;
        Queue<Pair<TreeNode, Boolean>> q = new LinkedList<>();
        q.offer(new Pair<>(root, false));  // 根节点不是左子节点

        while (!q.isEmpty()) {
            Pair<TreeNode, Boolean> pair = q.poll();
            TreeNode node = pair.getKey();
            boolean isLeftChild = pair.getValue();

            if (node.left == null && node.right == null && isLeftChild)
                sum += node.val;

            if (node.left != null)
                q.offer(new Pair<>(node.left, true));
            if (node.right != null)
                q.offer(new Pair<>(node.right, false));
        }

        return sum;
    }

    /*
     * 解法2：DFS with mark
     * - 思路：与解法1一致。
     * - 实现：采用 DFS 递归实现：
     *   1. 递归函数的语义：为每个节点计算以它为根的二叉树的左子节点值之和，从而由下到上最终累积出整棵树的左子节点值之和；
     *   2. 递归的退出条件：递归到底碰到 null 节点或叶子节点。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int sumOfLeftLeaves2(TreeNode root) {
        return helper2(root, false);
    }

    private static int helper2(TreeNode root, boolean isLeft) {
        if (root == null) return 0;
        if (root.left == null && root.right == null)
            return isLeft ? root.val : 0;
        return helper2(root.left, true) + helper2(root.right, false);
    }

    /*
     * 解法3：DFS
     * - 思路：解法1、2中对节点打标记是为了判断一个节点是否是左子节点。该解法中不对节点打标记，而是直接在父节点上进行判断：
     *     1. 若父节点有左子节点
     *     2. 且该左子节点是叶子节点
     *   若满足这两个条件则该左子节点即是左叶子节点，要累计其节点值，。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int sumOfLeftLeaves3(TreeNode root) {
        if (root == null) return 0;
        if (root.left != null && root.left.left == null && root.left.right == null)
            return root.left.val + sumOfLeftLeaves3(root.right);  // 访问节点（注意若有右子节点则要继续递归）
        return sumOfLeftLeaves3(root.left) + sumOfLeftLeaves3(root.right);
    }

    /*
     * 解法4：BFS
     * - 思路：与解法3一致。
     * - 实现：将 Queue 改为 Stack 即是相同思路下的 DFS 迭代实现。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int sumOfLeftLeaves4(TreeNode root) {
        if (root == null) return 0;
        int sum = 0;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            TreeNode node = q.poll();

            if (node.left != null) {
                if (node.left.left == null && node.left.right == null)
                    sum += node.left.val;  // 若是叶子节点则累计节点值
                else
                    q.offer(node.left);    // 若不是叶子节点则将其入队
            }

            if (node.right != null) q.offer(node.right);
        }

        return sum;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, null, null, 4, 5});
        log(sumOfLeftLeaves4(t1));
        /*
         * expects 6. (2 + 4)
         *       1
         *      / \
         *     2   3
         *        / \
         *       4   5
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, 4, null, null, 5});
        log(sumOfLeftLeaves4(t2));
        /*
         * expects 4. (4)
         *       1
         *      / \
         *     2   3
         *    /     \
         *   4       5
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, 4, null, 5, 6});
        log(sumOfLeftLeaves4(t3));
        /*
         * expects 9. (4 + 5)
         *       1
         *      / \
         *     2   3
         *    /   / \
         *   4   5   6
         * */

        TreeNode t4 = createBinaryTreeBreadthFirst(new Integer[]{-9, -3, 2, null, 4, 4, 0, -6, null, -5});
        log(sumOfLeftLeaves4(t4));
        /*
         * expects -11. (-6 + -5)
         *        -9
         *      /    \
         *    -3      2
         *      \    / \
         *      4   4   0
         *     /   /
         *    -6  -5
         * */

        TreeNode t5 = createBinaryTreeBreadthFirst(new Integer[]{1, null, 2});
        log(sumOfLeftLeaves4(t5));
        /*
         * expects 0. (没有左叶子节点的情况)
         *      1
         *       \
         *        2
         * */

        TreeNode t6 = createBinaryTreeBreadthFirst(new Integer[]{1});
        log(sumOfLeftLeaves4(t6));
        /*
         * expects 0. (根节点不算叶子节点 ∴ 该树没有左叶子节点)
         * */
    }
}
