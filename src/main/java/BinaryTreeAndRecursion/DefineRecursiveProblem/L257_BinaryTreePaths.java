package BinaryTreeAndRecursion.DefineRecursiveProblem;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Utils.Helpers.TreeNode;

/*
 * Binary Tree Paths
 *
 * - Given a binary tree, return all root-to-leaf paths.
 * */

public class L257_BinaryTreePaths {
    /*
     * 解法1：Recursion
     * - 思路：递归函数 f(n) 定义：返回以 n 为根的二叉树的全部 root-to-leaf paths。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static List<String> binaryTreePaths(TreeNode root) {
        if (root == null) return new ArrayList<>();

        List<String> paths = binaryTreePaths(root.left);
        paths.addAll(binaryTreePaths(root.right));

        if (paths.size() == 0) {
            paths.add(Integer.toString(root.val));
            return paths;
        }

        return paths.stream()
            .map(p -> root.val + "->" + p)
            .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, null, 5});
        log(binaryTreePaths(t1));
        /*
         * expects ["1->2->5", "1->3"].
         *       1
         *     /   \
         *    2     3
         *     \
         *      5
         * */
    }
}
