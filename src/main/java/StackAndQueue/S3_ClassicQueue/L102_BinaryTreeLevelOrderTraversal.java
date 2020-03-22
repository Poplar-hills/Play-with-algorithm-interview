package StackAndQueue.S3_ClassicQueue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import Utils.Helpers.TreeNode;

import static Utils.Helpers.*;

/*
 * Binary Tree Level Order Traversal
 *
 * - Given a binary tree, return the level order traversal of its nodes' values. (ie, from left to right, level by level).
 * */

public class L102_BinaryTreeLevelOrderTraversal {
    /*
     * 基础1：二叉树非递归层序遍历，用于和解法1进行对比。
     * - 思路：二叉树层序遍历的本质实际上就是广度优先遍历（Breadth-first traversal, BFT），因此可以采用类似图论 BFS 的思路，
     *   使用 queue 作为辅助结构。
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
     * - 实现：要使用递归先要想清楚递归遍历的对象是什么。∵ 该题中要广度优先遍历 ∴ 不能对树进行纵向的递归。而 ∵ 要使用 Queue
     *   来保证遍历的输出顺序 ∴ 递归遍历的对象应该是 Queue 中的节点。
     * - 💎总结：
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
     * 解法1：迭代（BFT）
     * - 思路：在基础1的基础上实现，区别在于队列中以 Pair 形式（也可以抽象成单独的类）同时保存节点和节点的层级信息。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        Queue<Pair<TreeNode, Integer>> q = new LinkedList<>();  // 因为结果要求同一层的节点放在一个列表中，因此这里队列中除了保存节点之外还需要保存层级信息
        q.offer(new Pair<>(root, 0));  // 层数从0开始

        while (!q.isEmpty()) {
            Pair<TreeNode, Integer> pair = q.poll();
            TreeNode node = pair.getKey();
            Integer level = pair.getValue();

            if (level == res.size())  // 此时需在 res 中创建新的列表存储新一层的节点值（如上面 poll 出来的是 level=0 的根节点，此时 res 中还没有任何列表，因此需要创建）
                res.add(new ArrayList<>());
            res.get(level).add(node.val);  // 创建完或者 res 中本来已经存在，则将节点值推入

            if (node.left != null) q.offer(new Pair<>(node.left, level + 1));
            if (node.right != null) q.offer(new Pair<>(node.right, level + 1));
        }

        return res;
    }

    /*
     * 解法2：递归（DFT）
     * - 思路：
     *   - 在递归中传递 level 信息，并根据该信息判断当前节点值应该放在第几个 list 中。
     *   - 本质上是深度优先遍历 DFT，但是因为将不同 level 的节点值放到了不同位置的 list 中，从而达到了 BFT 的效果。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高。
     * */
    public static List<List<Integer>> levelOrder2(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        levelOrder2(root, res, 0);
        return res;
    }

    private static void levelOrder2(TreeNode node, List<List<Integer>> res, int level) {
        if (node == null) return;
        if (level == res.size())       // 是否需要在 res 中创建新的列表存储新一层的节点值
            res.add(new ArrayList<>());
        res.get(level).add(node.val);  // 创建完之后这里再获取，从而统一了两种情况（创建新列表或直接添加），而不再需要 if else。
        levelOrder2(node.left, res, level + 1);
        levelOrder2(node.right, res, level + 1);
    }

	public static void main(String[] args) {
        TreeNode t = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, 8, 15, 7, 1, 2});
        /*
         *         3
         *      /     \
         *     9      20
         *      \     / \
         *       8   15  7
         *      / \
         *     1   2
         * */

        log(basicLevelOrder(t));   // expects [3, 9, 20, 8, 15, 7, 1, 2]
        log(basicLevelOrder2(t));  // expects [3, 9, 20, 8, 15, 7, 1, 2]

        log(levelOrder(t));        // expects [[3], [9,20], [8,15,7], [1,2]]
        log(levelOrder2(t));       // expects [[3], [9,20], [8,15,7], [1,2]]
    }
}
