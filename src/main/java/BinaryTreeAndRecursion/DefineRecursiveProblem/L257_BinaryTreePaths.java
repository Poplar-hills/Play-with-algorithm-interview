package BinaryTreeAndRecursion.DefineRecursiveProblem;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;
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

    /*
     * 解法2：Iteration (BFS)
     * - 思路：类似 L70_ClimbingStairs 解法4。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<String> binaryTreePaths2(TreeNode root) {
        List<String> res = new ArrayList<>();
        if (root == null) return res;

        Queue<List<TreeNode>> q = new LinkedList<>();
        List<TreeNode> initialPath = new ArrayList<>();
        initialPath.add(root);
        q.offer(initialPath);

        while (!q.isEmpty()) {
            List<TreeNode> path = q.poll();
            TreeNode lastNode = path.get(path.size() - 1);

            if (lastNode.left == null && lastNode.right == null) {  // 若是叶子节点则说明该 path 是完整的
                res.add(convertToString(path));
                continue;
            }

            Consumer<TreeNode> fn = node -> {
                List<TreeNode> newPath = new ArrayList<>(path);
                newPath.add(node);
                q.offer(newPath);
            };

            if (lastNode.left != null) fn.accept(lastNode.left);
            if (lastNode.right != null) fn.accept(lastNode.right);
        }

        return res;
    }

    private static String convertToString(List<TreeNode> path) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            builder.append(Integer.toString(path.get(i).val));
            if (i != path.size() - 1)
                builder.append("->");
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, null, 4});
        log(binaryTreePaths2(t1));
        /*
         * expects ["1->2->4", "1->3"].
         *       1
         *     /   \
         *    2     3
         *     \
         *      4
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, null, 4, 5, 6, null, null, 7});
        log(binaryTreePaths2(t2));
        /*
         * expects ["1->2->4", "1->3->5->7", "1->3->6"].
         *        1
         *     /     \
         *    2       3
         *     \     / \
         *      4   5   6
         *         /
         *        7
         * */
    }
}
