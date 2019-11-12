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
     * 解法1：Recursion + Pre-order Traversal
     * - 思路：使用前序遍历的思路，在从根节点往下遍历的过程中拼接 path 字符串，并往下传递，在抵达叶子节点后放入结果集中。
     *   ∴ 递归函数可定义为 f(n, path, res)：在以 n 为根的二叉树中对每个分支生成 path 字符串并放入结果集 res 中。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static List<String> binaryTreePaths(TreeNode root) {
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
     * 解法2：Recursion + Post-order Traversal
     * - 思路：不同于解法1，该解法采用后续遍历的思路，即先递归到底，在往上回溯的过程中拼接 path 字符串，并将含有 path 字符串的
     *   结果集返回上层。∴ 递归函数可定义为 f(n)：返回以 n 为根的二叉树的全部 root-to-leaf paths。
     * - 💎总结：对比解法1、2的思路可领悟前序、后续遍历的精髓。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static List<String> binaryTreePaths2(TreeNode root) {
        if (root == null) return new ArrayList<>();

        List<String> res = binaryTreePaths(root.left);  // 先递归到底，并将左右子树的递归结果合并到结果集 res 中
        res.addAll(binaryTreePaths(root.right));

        if (res.size() == 0) {                    // 若是叶子节点，则只往 res 中添加节点值字符串即可
            res.add(Integer.toString(root.val));
            return res;
        }

        return res.stream()
            .map(path -> root.val + "->" + path)  // 若不是叶子节点，则要给 res 中的每个 path 头部都拼接当前节点值
            .collect(Collectors.toList());
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

        Queue<List<TreeNode>> q = new LinkedList<>();  // 队列中存储根节点到当前节点的节点列表
        List<TreeNode> initialPath = new ArrayList<>();
        initialPath.add(root);
        q.offer(initialPath);

        while (!q.isEmpty()) {
            List<TreeNode> path = q.poll();
            TreeNode lastNode = path.get(path.size() - 1);  // 得到节点列表中的最后一个节点

            if (lastNode.left == null && lastNode.right == null) {  // 若是叶子节点则将该节点列表转化为路径字符串并放入结果集
                res.add(convertToString(path));
                continue;
            }

            Consumer<TreeNode> fn = node -> {
                List<TreeNode> newPath = new ArrayList<>(path);  // 若不是叶子节点则复制该节点列表并在加入子节点后重新入队
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
     * 解法4：Iteration (BFS) (解法3的简化版)
     * - 思路：观察解法3可知，对 queue 中的节点列表我们唯一关心的只有最后一个节点，存储其他节点只是为了最后能转化为路径字符串。
     *   基于此可以进行优化 —— 在 queue 中只存储当前路径上的最后一个节点，以及当前路径的路径字符串即可。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<String> binaryTreePaths4(TreeNode root) {
        List<String> res = new ArrayList<>();
        if (root == null) return res;

        Queue<Pair<TreeNode, String>> q = new LinkedList<>();  // 队列中存储 <当前路径的最后一个节点, 当前路径的路径字符串>
        q.offer(new Pair<>(root, ""));

        while (!q.isEmpty()) {
            Pair<TreeNode, String> pair = q.poll();
            TreeNode node = pair.getKey();
            String pathStr = pair.getValue() + node.val;  // 拼接路径字符串

            if (node.left == null && node.right == null) {
                res.add(pathStr);
                continue;
            }

            if (node.left != null)
                q.offer(new Pair<>(node.left, pathStr + "->"));
            if (node.right != null)
                q.offer(new Pair<>(node.right, pathStr + "->"));
        }

        return res;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, null, 4});
        log(binaryTreePaths4(t1));
        /*
         * expects ["1->2->4", "1->3"].
         *       1
         *     /   \
         *    2     3
         *     \
         *      4
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, null, 4, 5, 6, null, null, 7});
        log(binaryTreePaths4(t2));
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
