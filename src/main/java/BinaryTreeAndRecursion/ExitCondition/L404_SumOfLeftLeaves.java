package BinaryTreeAndRecursion.ExitCondition;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import Utils.Helpers.TreeNode;

/*
 * Sum of Left Leaves
 *
 * - Find the sum of all left leaves in a given binary tree.
 * */

public class L404_SumOfLeftLeaves {
    /*
     * 解法1：Recursion
     * - 💎 心得：在写代码之前要先想清楚解法，而在想解法之前要先找到题目的特点。
     * - 思路：1. 该题本质就是遍历树上节点，若碰到左子节点则累计其节点值。∴ 可先写出遍历二叉树的代码，再在上面修改访问节点的逻辑。
     *        2. 访问节点的逻辑需根据题目的特点进行设计：首先，“左子节点”一定是：1.叶子节点；2.某个节点的左子节点。∴ 访问节点
     *           的逻辑需要以这两点为条件。
     *        3. 在此基础上，若遍历采用递归，则还需思考递归函数的返回值是什么。∵ 要累计节点值 ∴ 递归函数可以就返回节点值到上层进行累计。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int sumOfLeftLeaves(TreeNode root) {
        if (root == null) return 0;
        if (root.left != null && root.left.left == null && root.left.right == null)  // 访问节点的条件
            return root.left.val + sumOfLeftLeaves(root.right);                      // 访问节点（注意若有右子节点则要继续递归）
        return sumOfLeftLeaves(root.left) + sumOfLeftLeaves(root.right);
    }

    /*
     * 解法2：Iteration (BFS, level-order traversal)
     * - 思路：非递归的遍历其实是更 intuitive 一些 —— 遍历所有节点，若碰到左叶子节点，若是则计入 sum。
     * - 实现：将 Queue 改为 Stack 即是相同思路下的 DFS 实现。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int sumOfLeftLeaves2(TreeNode root) {
        if (root == null) return 0;
        int sum = 0;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            TreeNode node = q.poll();
            if (node.left != null) {        // 在准备入队左子节点时检测其是否同时是叶子节点
                if (node.left.left == null && node.left.right == null)
                    sum += node.left.val;
                q.offer(node.left);         // 即使是左叶子节点也要入队 ∵ 还可能有右子树
            }
            if (node.right != null) q.offer(node.right);
        }

        return sum;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, null, null, 4, 5});
        log(sumOfLeftLeaves2(t1));
        /*
         * expects 6. (2 + 4)
         *       1
         *      / \
         *     2   3
         *        / \
         *       4   5
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, 4, null, null, 5});
        log(sumOfLeftLeaves2(t2));
        /*
         * expects 4. (4)
         *       1
         *      / \
         *     2   3
         *    /     \
         *   4       5
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, 4, null, 5, 6});
        log(sumOfLeftLeaves2(t3));
        /*
         * expects 9. (4 + 5)
         *       1
         *      / \
         *     2   3
         *    /   / \
         *   4   5   6
         * */

        TreeNode t4 = createBinaryTreeBreadthFirst(new Integer[]{-9, -3, 2, null, 4, 4, 0, -6, null, -5});
        log(sumOfLeftLeaves2(t4));
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
        log(sumOfLeftLeaves2(t5));
        /*
         * expects 0. (没有左叶子节点的情况)
         *      1
         *       \
         *        2
         * */

        TreeNode t6 = createBinaryTreeBreadthFirst(new Integer[]{1});
        log(sumOfLeftLeaves2(t6));
        /*
         * expects 0. (根节点不算叶子节点 ∴ 该树没有左叶子节点)
         * */
    }
}
