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
import javafx.util.Pair;

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
     * 解法2：Recursion
     * - 思路：递归函数 f(n) 定义：生成以 n 为最后一个节点的 path 字符串，若该 path 已经到底则将其添加到结果列表 res 中。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static List<String> binaryTreePaths2(TreeNode root) {
        List<String> res = new ArrayList<>();
        if (root != null) helper2(root, "", res);
        return res;
    }

    private static void helper2(TreeNode root, String path, List<String> res) {
        if (root == null) return;
        path += root.val;  // += 操作符对 String 同样有效

        if (root.left == null && root.right == null) {  // 若当前 path 已经到底
            res.add(path);
            return;
        }
        helper2(root.left, path + "->", res);
        helper2(root.right, path + "->", res);
	}

	/*
     * 解法3：Iteration (BFS)
     * - 思路：与 L70_ClimbingStairs 解法4一致。
     * - 同理：只需将 Queue 替换为 Stack 就得到了 DFS 解法。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<String> binaryTreePaths3(TreeNode root) {
        List<String> res = new ArrayList<>();
        if (root == null) return res;

        Queue<List<TreeNode>> q = new LinkedList<>();  // 队列中每个元素：根节点到当前节点的节点列表
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

	/*
     * 解法4：Iteration (BFS)
     * - 思路：解法3的简化版，在遍历过程中持续拼接字符串。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<String> binaryTreePaths4(TreeNode root) {
        List<String> res = new ArrayList<>();
        if (root == null) return res;

        Queue<Pair<TreeNode, String>> q = new LinkedList<>();  // 队列中每个元素：<节点, 根节点到该节点的 path 字符串>
        q.offer(new Pair<>(root, ""));

        while (!q.isEmpty()) {
            Pair<TreeNode, String> pair = q.poll();
            TreeNode node = pair.getKey();
            String pathStr = pair.getValue();

            if (node.left == null && node.right == null) {
                res.add(pathStr + node.val);
                continue;
            }

            if (node.left != null)
                q.offer(new Pair<>(node.left, pathStr + node.val + "->"));
            if (node.right != null)
                q.offer(new Pair<>(node.right, pathStr + node.val + "->"));
        }

        return res;
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
