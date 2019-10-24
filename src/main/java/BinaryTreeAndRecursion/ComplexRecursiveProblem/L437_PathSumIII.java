package BinaryTreeAndRecursion.ComplexRecursiveProblem;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import Utils.Helpers.TreeNode;

/*
 * Path Sum III
 *
 * - Given a binary tree, find the number of paths that sum to a given value.
 * - Note that the path does not need to start or end at the root or a leaf, but it must go downwards
 *   (traveling only from parent nodes to child nodes).
 * - The tree has no more than 1,000 nodes and the values are in the range -1,000,000 to 1,000,000.
 * */

public class L437_PathSumIII {
    /*
     * 解法1：Iteration
     * - 思路：找到所有
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int pathSum(TreeNode root, int sum) {
        if (root == null) return 0;
        Stack<List<TreeNode>> stack = new Stack<>();
        List<TreeNode> initialList = new ArrayList<>();
        initialList.add(root);
        stack.push(initialList);

        return 0;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{10, 5, -3, 3, 2, null, 11, 3, -10, null, 1});
        log(pathSum(t1, 8));
        /*
         * expects 4. (5->3, 5->2->1, -3->11, 10->5->3->-10)
         *         10
         *        /  \
         *       5   -3
         *      / \    \
         *     3   2   11
         *    / \   \
         *   3 -10   1
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{10, 8, -2});
        log(pathSum(t2, 8));
        /*
         * expects 2. (8, 10->-2)
         *         10
         *        /  \
         *       8   -2
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{});
        log(pathSum(t3, 0));
        /*
         * expects 0.
         * */
    }
}
