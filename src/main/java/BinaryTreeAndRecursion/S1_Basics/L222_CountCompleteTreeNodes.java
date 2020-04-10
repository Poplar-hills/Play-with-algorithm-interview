package BinaryTreeAndRecursion.S1_Basics;

import static Utils.Helpers.*;

import java.util.LinkedList;
import java.util.Queue;

import Utils.Helpers.TreeNode;

/*
 * Count Complete Tree Nodes
 *
 * - Given a complete binary tree (完全二叉树), count the number of nodes.
 * - Definition of a complete binary tree: Every level, except possibly the last, is completely filled, and
 *   all nodes in the last level are as far left as possible (注：堆使用的就是完全二叉树).
 *
 * - 复习：
 *   1. 注意区分完全二叉树和满二叉树（SEE: Play-with-data-structure/BST）；
 *   2. 完全二叉树非最后一层的节点数为 2^h，最后一层的节点数为 1~2^h，其中 h 为层的深度；
 *   3. 完美二叉树的总结点数为 2^h - 1。
 * */

public class L222_CountCompleteTreeNodes {
    /*
     * 解法1：DFS (Recursion)
     * - 思路：最 intuitive 的方式就是遍历所有节点并数数。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int countNodes(TreeNode root) {
        if (root == null) return 0;
        return countNodes(root.left) + countNodes(root.right) + 1;
    }

	/*
     * 解法2：BFS (Iteration)
     * - 思路：与解法1一致。
     * - 实现：将 Queue 改为 Stack 即是相同思路的 DFS 实现。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
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
     * 解法3：BFS (Recursion)
     * - 思路：与解法1、2一致。
     * - 实现：BFS 的递归是对 queue 进行的（即替换解法2的 while 循环）。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    private static int count;

    public static int countNodes3(TreeNode root) {
        if (root == null) return 0;
        count = 0;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        helper3(q);     // 对 queue 进行递归（即替换解法2的 while 循环）
        return count;
    }

    private static void helper3(Queue<TreeNode> q) {
        if (q.isEmpty()) return;
        TreeNode node = q.poll();
        count++;
        if (node.left != null) q.offer(node.left);
        if (node.right != null) q.offer(node.right);
        helper3(q);
	}

    /*
     * 解法4：Recursion（解法1的时间空间优化版）
     * - 思路：不同于解法1、2、3，本解法利用了2个性质：
     *     1. 完美二叉树的节点总数：2^h-1，其中 h 为树高；
     *     2. 完全二叉树左、右子树中至少有一个是完美二叉树（见 test cases）；
     *   根据这两个性质，可在解法1的基础上进行优化：在递归左右子树之前先计算树的左右深度，若左右深度相同则说明是完美二叉树，可直接
     *   用性质1计算出节点数，若左右深度不同，则再继续递归左右子树。
     * - 👉 实现：Bitwise operator 语法解释：
     *     1. 1<<4 表示将1在二进制中左移4位，得到 0b10000，即十进制中的 1 * 2^4 ∴ 将 1<< 几位就是2的几次方；
     *     2. ∴ 2^h - 1 就相当于 (1<<h) - 1（注意 << 优先级低于减号）；
     *     3. 另一种表达方式是 (int) Math.pow(2, h) - 1（注意 pow 返回 double 类型，需要强转）。
     * - 时间复杂度 O(logn^2)：∵ 左、右子树中至少有一个是完美二叉树，且完美的一边直接得到结果，复杂度为 O(1) ∴ 整个递归过程
     *   相当于单边递归（与 getLeftDepth 一样）∴ 复杂度为 O(logn)。而又因为每次递归中有 getLeftDepth、getRightDepth
     *   ∴ 整体复杂度为 O(logn^2)。
     * - 空间复杂度 O(logn)：∵ 完全二叉树也是平衡树，不会退化成链表。
     * */
    public static int countNodes4(TreeNode root) {
        if (root == null) return 0;
        int lDepth = getLeftDepth(root);
        int rDepth = getRightDepth(root);
        return lDepth == rDepth                                      // 若是完美二叉树
            ? (1 << lDepth) - 1                                      // 直接计算总节点数 2^h-1
            : countNodes4(root.left) + countNodes4(root.right) + 1;  // 若不完美则递归左右子树（其中至少有一个是完美的）
    }

    private static int getLeftDepth(TreeNode root) {
        return (root == null) ? 0 : getLeftDepth(root.left) + 1;
    }

    private static int getRightDepth(TreeNode root) {
        return (root == null) ? 0 : getRightDepth(root.right) + 1;
    }

    /*
     * 解法5：Recursion
     * - 思路：与解法4一致。
     * - 实现：也是利用上述的完全二叉树的两个性质进行优化，比解法3更精简，但逻辑也更绕一些。
     * - 时间复杂度 O(logn^2)，空间复杂度 O(logn)。
     * */
    public static int countNodes5(TreeNode root) {
        int h = getLeftDepth(root);               // 先计算整棵树的左侧高度 h
        if (h == 0)	return 0;
        return getLeftDepth(root.right) == h - 1  // 若右子树的左侧高度 = h-1，即左右子树的左侧高度相等，此时左子树一定完美，右子树不一定（test case 1,2）
            ? (1 << h - 1) + countNodes4(root.right)  // 用公式得到左子树的节点数，再继续递归计算右子树
    		: (1 << h - 2) + countNodes4(root.left);  // 若左右子树的左侧高度不相等，说明树的最后一个节点在左子树上，此时
    }                                                 // 右子树一定完美，左子树不一定（test case 3,4）

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, 4, 5, 6, 7});
        log(countNodes3(t1));
        /*
         * expects 7.
         *        1
         *       / \
         *      2   3
         *     / \ / \
         *    4  5 6  7
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, 4, 5, 6});
        log(countNodes3(t2));
        /*
         * expects 6.
         *        1
         *       / \
         *      2   3
         *     / \ /
         *    4  5 6
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, 4, 5});
        log(countNodes3(t3));
        /*
         * expects 5.
         *        1
         *       / \
         *      2   3
         *     / \
         *    4   5
         * */

        TreeNode t4 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, 4});
        log(countNodes3(t4));
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
