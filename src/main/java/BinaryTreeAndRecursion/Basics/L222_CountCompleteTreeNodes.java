package BinaryTreeAndRecursion.Basics;

import static Utils.Helpers.*;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import Utils.Helpers.TreeNode;

/*
 * Count Complete Tree Nodes
 *
 * - Given a complete binary tree (完全二叉树), count the number of nodes.
 * - Definition of a complete binary tree: Every level, except possibly the last, is completely filled, and
 *   all nodes in the last level are as far left as possible (注：堆使用的就是完全二叉树).
 *
 * - 复习：
 *   1. 注意区分完全二叉树和满二叉树；
 *   2. 完全二叉树非最后一层的节点数为 2^h，最后一层的节点数为 1~2^h，其中 h 为层的深度；
 *   3. 完美二叉树的总结点数为 2^h - 1。
 * */

public class L222_CountCompleteTreeNodes {
    /*
     * 解法1：Recursion (DFS)
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int countNodes(TreeNode root) {
        if (root == null) return 0;
        return 1 + countNodes(root.left) + countNodes(root.right);
    }

    /*
     * 解法2：Iteration (BFS, level-order traversal)
     * - 思路：采用层序遍历，边遍历边计数。
     * - 实现：将 Queue 改为 Stack 即是相同思路下的 DFS 实现。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int countNodes2(TreeNode root) {
        if (root == null) return 0;
        int count = 0;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            TreeNode node = q.poll();
            count++;
            if (node.left != null) q.offer(node.left);
            if (node.right != null) q.offer(node.right);
        }

        return count;
    }

    /*
     * 解法3：Recursion（解法1的时间空间优化版）
     * - 思路：不同于解法1、2，本解法利用了2个性质：
     *     1. 完美二叉树的节点总数：2^h-1，其中 h 为树高；
     *     2. 完全二叉树左、右子树中至少有一个是完美二叉树（见 test cases）；
     *   根据这两个性质，可在解法1的基础上进行优化：在递归左右子树之前先计算树的左右深度，若左右深度相同则说明是完美二叉树，可
     *   用性质1直接计算节点数，若树的左右深度不同，则再继续递归左右子树。
     * - 👉 Bitwise operator 语法解释：
     *     1. 1<<4 表示将1往左移4位，得到二进制的 0b10000，即十进制的 1 * 2^4；
     *     2. 因此要表达 2^h - 1 就相当于 1*2^h - 1，即相当于 (1 << h) - 1，注意 << 优先级低于减号；
     *     3. 另一种表达方式是 (int) Math.pow(2, h) - 1，注意 pow 返回 double 类型，需要强转。
     * - 时间复杂度 O(logn^2)：∵ 左、右子树中至少有一个是完美二叉树，且完美的一边直接得到结果，复杂度为 O(1) ∴ 整个递归过程
     *   相当于单边递归（与 getLeftDepth 一样）∴ 复杂度为 O(logn)。而又因为每次递归中有 getLeftDepth、getRightDepth
     *   ∴ 整体复杂度为 O(logn^2)。
     * - 空间复杂度 O(logn)：∵ 完全二叉树也是平衡树，不会退化成链表。
     * */
    public static int countNodes3(TreeNode root) {
        if (root == null) return 0;
        int leftDepth = leftDepth(root);
        int rightDepth = rightDepth(root);
        return leftDepth == rightDepth
            ? (1 << leftDepth) - 1                                   // 若是完美二叉树，则总节点数为 2^h-1
            : 1 + countNodes3(root.left) + countNodes3(root.right);  // 若不完美则递归左右子树（其中至少有一个是完美的）
    }

    private static int leftDepth(TreeNode root) {
        return (root == null) ? 0 : 1 + leftDepth(root.left);
    }

    private static int rightDepth(TreeNode root) {
        return (root == null) ? 0 : 1 + rightDepth(root.right);
    }

    /*
     * 解法4：Recursion
     * - 思路：也是利用上述的完全二叉树的两个性质进行优化，比解法3更精简，但逻辑也更绕一些。
     * - 时间复杂度 O(logn^2)，空间复杂度 O(logn)。
     * */
    public static int countNodes4(TreeNode root) {
        int h = leftDepth(root);               // 先计算整棵树的左侧高度 h
        if (h == 0)	return 0;
        return leftDepth(root.right) == h - 1  // 若右子树的左侧高度 = h-1，即左右子树的左侧高度相等，此时左子树一定完美，右子树不一定（test case 1,2）
            ? (1 << h - 1) + countNodes4(root.right)  // 用公式得到左子树的节点数，再继续递归计算右子树
    		: (1 << h - 2) + countNodes4(root.left);  // 若左右子树的左侧高度不相等，说明树的最后一个节点在左子树上，此时
    }                                                 // 右子树一定完美，左子树不一定（test case 3,4）

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, 4, 5, 6, 7});
        log(countNodes4(t1));
        /*
         * expects 7.
         *        1
         *       / \
         *      2   3
         *     / \ / \
         *    4  5 6  7
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, 4, 5, 6});
        log(countNodes4(t2));
        /*
         * expects 6.
         *        1
         *       / \
         *      2   3
         *     / \ /
         *    4  5 6
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, 4, 5});
        log(countNodes4(t3));
        /*
         * expects 5.
         *        1
         *       / \
         *      2   3
         *     / \
         *    4   5
         * */

        TreeNode t4 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, 4});
        log(countNodes4(t4));
        /*
         * expects 4.
         *        1
         *       / \
         *      2   3
         *     /
         *    4
         * */
    }
}
