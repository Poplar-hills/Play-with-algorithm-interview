package BinaryTreeAndRecursion.S3_DefineRecursiveProblem;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.*;

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
     * 解法1：DFS + Backtracking (Pre-order Traversal)
     * - 思路：通过 DFS 将每条路径上的节点收集到 path 列表中，当到达叶子节点时，将 path 转为 String 放入结果集。
     * - 实现：∵ 要在不同路径上通过回溯复用 path 对象 ∴ 需要在每次返回上层递归之前将 path 恢复原状。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static List<String> binaryTreePaths(TreeNode root) {
        List<String> res = new ArrayList<>();
        if (root == null) return res;
        helper(root, new ArrayList<>(), res);
        return res;
    }

    private static void helper(TreeNode root, List<TreeNode> path, List<String> res) {
        if (root == null) return;
        path.add(root);                                 // 访问节点（pre-order traversal）

        if (root.left == null && root.right == null) {  // 找到一条 root-to-leaf path 后将其转为 String
            res.add(toPathString(path));
            path.remove(path.size() - 1);               // 注意在 return 前要将 path 恢复原状
            return;
        }
        helper(root.left, path, res);
        helper(root.right, path, res);
        path.remove(path.size() - 1);                   // 返回上层递归之前将将 path 恢复原状
    }

    private static String toPathString(List<TreeNode> path) {
        StringBuilder sb = new StringBuilder();
        for (TreeNode node : path) {
            sb.append(node.val);
            if (node != path.get(path.size() - 1))
                sb.append("->");
        }
        return sb.toString();
    }

    /*
     * 解法2：DFS (Pre-order Traversal)
     * - 思路：与解法1一致。
     * - 实现：在解法1的基础上进行化简，直接使用 "" 代替解法1中的 path 列表。∵ String 是 immutable 的 ∴ 直接拼接字符串时
     *   不存在解法1中 path 是否能在不同分支路径上复用的问题 ∴ 也就不需要每次 return 之前的 remove 操作。
     * - 💎 语言特性：Java 中的 String 对象之所以是 immutable 的因为：
     *   1. String 类内部成员变量全部使用 final 修饰 ∴ 无法修改而只能通过构造函数来创建新对象；
     *   2. String 类中可访问内部成员变量的方法（如 toCharArray()），返回时一律构造新的 String 对象或 byte[] 或 char[]。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static List<String> binaryTreePaths2(TreeNode root) {
        List<String> res = new ArrayList<>();
        if (root != null)
            helper2(root, "", res);
        return res;
    }

    private static void helper2(TreeNode root, String path, List<String> res) {
        if (root == null) return;
        path += root.val;          // += 操作符对 String 同样有效

        if (root.left == null && root.right == null) {
            res.add(path);
            return;
        }
        helper2(root.left, path + "->", res);
        helper2(root.right, path + "->", res);
	}

    /*
     * 解法3：DFS + Concat path (Post-order traversal)
     * - 思路：类似 L113_PathSumII 解法3，采用后续遍历 —— 即先递归到底，在回程的路上拼接字符串，并返回上层：
     *               1          [["1->2->4"], ["1->3"]]
     *             /   \               ↗       ↖
     *            2     3   -->   ["2->4"]     ["3"]
     *             \                   ↖
     *              4                  ["4"]
     *   ∴ 递归函数可定义为：f(n) 返回以 n 为根的二叉树的全部 root-to-leaf paths。
     * - 💎 总结：对比解法2、3可加深对前序、后续遍历的理解。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static List<String> binaryTreePaths3(TreeNode root) {
        if (root == null) return new ArrayList<>();

        List<String> paths = binaryTreePaths(root.left);  // 先递归到底，并将左右子树的递归结果合并到结果集 res 中
        paths.addAll(binaryTreePaths(root.right));

        if (paths.size() == 0) {                  // 若是叶子节点，则只需往 res 中添加节点值即可
            paths.add(root.val + "");
            return paths;
        }

        return paths.stream()
            .map(path -> root.val + "->" + path)  // 若不是叶子节点，则要给 res 中的每个 path 头部都拼接当前节点值
            .collect(Collectors.toList());
    }

	/*
     * 解法4：BFS
     * - 思路：与 L113_PathSumII 解法4、L70_ClimbingStairs 解法4一致。
     * - 同理：只需将 Queue 替换为 Stack 就得到了 DFS 解法。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<String> binaryTreePaths4(TreeNode root) {
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
                res.add(toPathString(path));
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

	/*
     * 解法5：BFS (解法4的简化版)
     * - 思路：与解法4一致。
     * - 实现：观察解法4可知，对 queue 中的节点列表我们唯一关心的只有最后一个节点，存储其他节点只是为了最后能转化为路径字符串。
     *   基于此可以进行优化 —— 在 queue 中只存储当前路径上的最后一个节点，以及当前路径的路径字符串即可。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<String> binaryTreePaths5(TreeNode root) {
        List<String> res = new ArrayList<>();
        if (root == null) return res;

        Queue<Pair<TreeNode, String>> q = new LinkedList<>();  // 队列中存储 <当前路径的最后一个节点, 当前路径的路径字符串>
        q.offer(new Pair<>(root, ""));

        while (!q.isEmpty()) {
            Pair<TreeNode, String> pair = q.poll();
            TreeNode node = pair.getKey();
            String pathStr = pair.getValue() + node.val;       // 访问节点，拼接路径字符串

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
        log(binaryTreePaths3(t1));
        /*
         * expects ["1->2->4", "1->3"].
         *       1
         *     /   \
         *    2     3
         *     \
         *      4
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, null, 4, 5, 6, null, null, 7});
        log(binaryTreePaths3(t2));
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
