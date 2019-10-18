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
     * - 思路：该题中，左叶子节点的特点是：
     *     1. 一定是叶子节点；
     *     2. 一定是某个节点的左子节点。
     *   ∴ 递归函数 f(n) 可以定义为：求以节点 n 为根的二叉树的左叶子节点之和。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int sumOfLeftLeaves(TreeNode root) {
        if (root == null) return 0;
        TreeNode lChild = root.left;
        return (lChild != null && lChild.left == null && lChild.right == null)  // 判断是否有左子节点，且左子节点是叶子节点（即满足条件1、2）
            ? lChild.val + sumOfLeftLeaves(root.right)                   // 是则将左子节点值计入 sum，只继续递归右子节点
            : sumOfLeftLeaves(root.left) + sumOfLeftLeaves(root.right);  // 否则左右子节点都继续往下递归
    }

    /*
     * 解法2：Iteration (BFS, level-order traversal)
     * - 思路：用层序遍历，检查每一层的所有节点的左子节点是否是叶子节点，若是则计入 sum。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int sumOfLeftLeaves2(TreeNode root) {
        if (root == null) return 0;
        int sum = 0;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            TreeNode node = q.poll();
            TreeNode lChild = node.left;
            if (lChild != null) {
                q.offer(lChild);
                if (lChild.left == null && lChild.right == null)  // 若是叶子节点，则计入 sum
                    sum += lChild.val;
            }
            if (node.right != null) q.offer(node.right);
        }

        return sum;
    }

    /*
     * 解法3：Iteration (DFS)
     * - 思路：思路与解法2一致，只是采用 DFS 遍历。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int sumOfLeftLeaves3(TreeNode root) {
        if (root == null) return 0;
        int sum = 0;
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            TreeNode lChild = node.left;
            if (node.left != null) {
                stack.push(lChild);
                if (lChild.left == null && lChild.right == null)  // 若是叶子节点，则计入 sum
                    sum += lChild.val;
            }
            if (node.right != null) stack.push(node.right);
        }

        return sum;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, null, null, 4, 5});
        log(sumOfLeftLeaves3(t1));
        /*
         * expects 6. (2 + 4)
         *       1
         *      / \
         *     2   3
         *        / \
         *       4   5
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, 4, null, null, 5});
        log(sumOfLeftLeaves3(t2));
        /*
         * expects 4. (4)
         *       1
         *      / \
         *     2   3
         *    /     \
         *   4       5
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, 4, null, 5, 6});
        log(sumOfLeftLeaves3(t3));
        /*
         * expects 9. (4 + 5)
         *       1
         *      / \
         *     2   3
         *    /   / \
         *   4   5   6
         * */

        TreeNode t4 = createBinaryTreeBreadthFirst(new Integer[]{1, null, 2});
        log(sumOfLeftLeaves3(t4));
        /*
         * expects 0. (没有左叶子节点的情况)
         *      1
         *       \
         *        2
         * */

        TreeNode t5 = createBinaryTreeBreadthFirst(new Integer[]{1});
        log(sumOfLeftLeaves3(t5));
        /*
         * expects 0. (根节点不是叶子节点，不能计入 sum)
         * */
    }
}
