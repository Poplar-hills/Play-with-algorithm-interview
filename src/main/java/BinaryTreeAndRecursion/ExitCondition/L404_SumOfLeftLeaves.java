package BinaryTreeAndRecursion.ExitCondition;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import java.util.LinkedList;
import java.util.Queue;

import Utils.Helpers.TreeNode;

/*
 * Sum of Left Leaves
 *
 * - Find the sum of all left leaves in a given binary tree.
 * */

public class L404_SumOfLeftLeaves {
    /*
     * 解法1：Recursion
     * - 思路：先找到 left leaves 的特点：1. 是叶子节点；2.
     * Play-with-data-structure/BST/BST.java 中序遍历的应用
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int sumOfLeftLeaves(TreeNode root) {
        return 0;
    }

    /*
     * 解法2：Iteration (BFS, level-order traversal)
     * - 思路：通过层序遍历，检查每层的第一个节点是否为叶子节点，若是则计入。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int sumOfLeftLeaves2(TreeNode root) {
        if (root == null) return 0;
        int sum = 0;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            TreeNode firstNode = q.peek();   // 取每层第一个节点
            if (firstNode.left == null && firstNode.right == null)  // 若是叶子节点则计入 sum
                sum += firstNode.val;

            int qSize = q.size();
            while (qSize-- > 0) {           // 遍历出队该层所有节点，入队下一层所有节点
                TreeNode node = q.poll();
                if (node.left != null) q.offer(node.left);
                if (node.right != null) q.offer(node.right);
            }
        }

        return sum;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 8, null, null, 5, 7});
        log(sumOfLeftLeaves2(t1));
        /*
         * expects 14.
         *       3
         *      / \
         *     9   8
         *        / \
         *       5   7
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 8, 4, null, null, 7});
        log(sumOfLeftLeaves2(t2));
        /*
         * expects 4.
         *       3
         *      / \
         *     9   8
         *    /     \
         *   4       7
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 8, 2, null, 5, 7});
        log(sumOfLeftLeaves2(t3));
        /*
         * expects 2.
         *       3
         *      / \
         *     9   8
         *    /   / \
         *   2   5   7
         * */
    }
}
