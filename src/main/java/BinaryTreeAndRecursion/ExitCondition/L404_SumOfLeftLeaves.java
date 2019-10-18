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
     * - 思路：先找到 left leaves 的特点：1.是左子节点；2.是叶子节点；
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
            TreeNode node = q.poll();
            TreeNode left = node.left;
            if (left != null) {
                q.offer(left);
                if (left.left == null && left.right == null)
                    sum += left.val;
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

        TreeNode t4 = createBinaryTreeBreadthFirst(new Integer[]{1, null, 2});
        log(sumOfLeftLeaves2(t4));
        /*
         * expects 0. (没有左叶子节点的情况)
         *      1
         *       \
         *        2
         * */

        TreeNode t5 = createBinaryTreeBreadthFirst(new Integer[]{1});
        log(sumOfLeftLeaves2(t5));
        /*
         * expects 0. (根节点不是叶子节点，不能计入 sum)
         * */
    }
}
