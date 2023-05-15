package StackAndQueue.S3_ClassicQueue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import Utils.Helpers.Pair;
import Utils.Helpers.TreeNode;

import static Utils.Helpers.*;

/*
 * Binary Tree Level Order Traversal
 *
 * - Given a binary tree, return the level order traversal of its nodes' values.
 *   (ie, from left to right, level by level).
 * */

public class L102_BinaryTreeLevelOrderTraversal {
    /*
     * 基础1：二叉树非递归层序遍历（用于和解法1进行对比）
     * - 思路：二叉树层序遍历本质上就是广度优先遍历（BFS）。
     * */
    public static List<Integer> basicLevelOrder(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            TreeNode curr = q.poll();
            res.add(curr.val);
            if (curr.left != null) q.offer(curr.left);
            if (curr.right != null) q.offer(curr.right);
        }

        return res;
    }

    /*
     * 基础2：基础1的递归版
     * - 思路：与解法1一致，仍然是 BFS（👉🏻递归也可以实现 BFS）。
     * - 实现：要使用递归先要想清楚递归遍历的对象是什么。∵ 该题中要广度优先遍历 ∴ 不能对树进行纵向的递归。而 ∵ 要使用 Queue
     *   来保证遍历的输出顺序 ∴ 递归遍历的对象应该是 Queue 中的节点。
     * - 💎 总结：
     *   - 树的广度优先遍历（BFS）通常使用 Queue 作为辅助数据结构（递归或非递归实现都一样）；
     *   - 树的深度优先遍历（DFS），如前、中、后序遍历的非递归实现，通常使用 Stack 作为辅助数据结构（递归实现则不需要辅助结构）。
     * */
    public static List<Integer> basicLevelOrder2(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        helper2(q, res);  // 用递归代替基础1中的 while 循环
        return res;
    }

    private static void helper2(Queue<TreeNode> q, List<Integer> res) {
        if (q.isEmpty()) return;
        TreeNode curr = q.poll();
        res.add(curr.val);
        if (curr.left != null) q.offer(curr.left);
        if (curr.right != null) q.offer(curr.right);
        helper2(q, res);
    }

    /*
     * 解法1：迭代（BFS）
     * - 思路：按行处理 —— 每次入队、出队、访问一整行的节点，并创建单独的 List 来存储每行的节点值。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            int n = q.size();
            List<Integer> levelList = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                TreeNode node = q.poll();
                levelList.add(node.val);
                if (node.left != null) q.offer(node.left);
                if (node.right != null) q.offer(node.right);
            }
            res.add(levelList);
        }

        return res;
    }

    /*
     * 解法2：迭代（BFS）
     * - 思路：∵ 要按树的 level 对遍历的节点值进行分组 ∴ 在遍历过程中需要知道每个节点的 level 信息。而每个节点的 level 信息
     *   可以通过父节点的 level + 1 得到。
     * - 实现：在基础1的基础上，让 Queue 中存储节点和其 level 的 Pair（也可以抽象成单独的类），同时保存节点和节点的层级信息。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> levelOrder2(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        Queue<Pair<TreeNode, Integer>> q = new LinkedList<>();
        q.offer(new Pair<>(root, 0));      // level 从0开始

        while (!q.isEmpty()) {
            Pair<TreeNode, Integer> pair = q.poll();
            TreeNode node = pair.getKey();
            Integer level = pair.getValue();

            if (res.size() == level)       // 若 size == level，说明需在 res 中创建新的列表存储新一层的节点值
                res.add(new ArrayList<>());
            res.get(level).add(node.val);  // 创建完或者 res 中本来已经存在，则将节点值放入列表（注意这里的链式写法）

            if (node.left != null) q.offer(new Pair<>(node.left, level + 1));
            if (node.right != null) q.offer(new Pair<>(node.right, level + 1));
        }

        return res;
    }

    /*
     * 解法3：递归（DFS）
     * - 思路：与解法2类似，该解法使用 DFS 对二叉树进行前序遍历，将遍历到的节点值追加到结果集中相应 level 的分组中，
     *   从而通过 DFS 实现了 BFS 的遍历效果 —— ∵ 在递归中传递了 level 信息 ∴ 在遍历到节点时可直接 add 到第 level 个
     *   列表中，与是 BFS 还是 DFS 遍历无关。
     * - 实现：
     *   - 在递归层级中传递 level 信息；
     *   - 根据 level 该信息判断当前节点值应该放在 res 中的第几个 list 里。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高。
     * */
    public static List<List<Integer>> levelOrder3(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        dfs3(root, res, 0);
        return res;
    }

    private static void dfs3(TreeNode node, List<List<Integer>> res, int level) {
        if (node == null) return;
        if (level == res.size())
            res.add(new ArrayList<>());
        res.get(level).add(node.val);
        dfs3(node.left, res, level + 1);
        dfs3(node.right, res, level + 1);
    }

    public static void main(String[] args) {
        TreeNode t = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, 8, 15, 7, 1, 2});
        /*
         *          3
         *      /       \
         *     9        20
         *      \       / \
         *       8     15  7
         *      / \
         *     1   2
         * */

        log(basicLevelOrder(t));   // expects [3, 9, 20, 8, 15, 7, 1, 2]
        log(basicLevelOrder2(t));  // expects [3, 9, 20, 8, 15, 7, 1, 2]

        log(levelOrder(t));        // expects [[3], [9,20], [8,15,7], [1,2]]
        log(levelOrder2(t));       // expects [[3], [9,20], [8,15,7], [1,2]]
    }
}
