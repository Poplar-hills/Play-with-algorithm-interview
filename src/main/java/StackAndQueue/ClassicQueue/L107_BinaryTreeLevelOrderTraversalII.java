package StackAndQueue.ClassicQueue;

import javafx.util.Pair;

import java.util.*;

import static Utils.Helpers.*;

/*
* Binary Tree Level Order Traversal II
*
* - Given a binary tree, return the bottom-up level order traversal of its nodes' values.
*   (ie, from left to right, level by level from leaf to root).
* */

public class L107_BinaryTreeLevelOrderTraversalII {
    /*
    * 基础1：自底向上的层序遍历。使用一个队列、一个栈实现。
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static List<Integer> simpleLevelOrderBottom(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        Stack<TreeNode> stack = new Stack<>();
        if (root == null) return res;

        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (node.right != null) queue.offer(node.right);
            if (node.left != null) queue.offer(node.left);
            stack.push(node);
        }

        while (!stack.isEmpty())
            res.add(stack.pop().val);

        return res;
    }

    /*
     * 基础2：自底向上的层序遍历。使用一个列表实现（空间复杂度更低）。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<Integer> simpleLevelOrderBottom2(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        List<TreeNode> arr = new ArrayList<>();
        if (root == null) return res;

        arr.add(root);
        for (int i = 0; i < arr.size(); i++) {  // 一遍遍历列表一遍该边列表长度是可以的
            TreeNode node = arr.get(i);         // 因为需要随机访问能力，因此采用 ArrayList
            if (node.right != null) arr.add(node.right);
            if (node.left != null) arr.add(node.left);
        }

        for (int i = arr.size() - 1; i >= 0; i--)
            res.add(arr.get(i).val);

        return res;
    }

    /*
    * 解法1：在基础2的基础上实现，区别在于 arr 中以 Pair 形式（也可以抽象成单独的类）同时保存节点和节点的层级信息。
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static List<List<Integer>> levelOrderBottom(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        List<Pair<TreeNode, Integer>> arr = new ArrayList<>();
        if (root == null) return res;

        arr.add(new Pair<>(root, 0));
        for (int i = 0; i < arr.size(); i++) {
            TreeNode node = arr.get(i).getKey();
            int level = arr.get(i).getValue();

            if (node.right != null)
                arr.add(new Pair<>(node.right, level + 1));
            if (node.left != null)
                arr.add(new Pair<>(node.left, level + 1));
            if (level == res.size())
                res.add(new ArrayList<>());
        }

        int maxLevel = arr.get(arr.size() - 1).getValue();
        for (int i = arr.size() - 1; i >= 0; i--) {
            TreeNode node = arr.get(i).getKey();
            int level = arr.get(i).getValue();
            res.get(maxLevel - level).add(node.val);
        }

        return res;
    }

    /*
    * 解法2：递归
    * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高。
    * */
    public static List<List<Integer>> levelOrderBottom2(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        levelOrderBottom2(root, res, 0);
        return res;
    }

    private static void levelOrderBottom2(TreeNode node, List<List<Integer>> res, int level) {
        if (node == null) return;
        if (level == res.size())
            res.add(0, new ArrayList<>());       // 在 res 头部插入空列表（否则 test cast t2 会出错，试一下就知道了）
        levelOrderBottom2(node.left, res, level + 1);
        levelOrderBottom2(node.right, res, level + 1);
        res.get(res.size() - 1 - level).add(node.val);  // 递归到底之后再开始将节点值推入 res 中的对应列表
    }

    /*
    * 解法3：迭代（第2版）
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static List<List<Integer>> levelOrderBottom3(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        Queue<TreeNode> q = new LinkedList<>();
        if (root == null) return res;

        q.offer(root);
        while (!q.isEmpty()) {
            List<Integer> levelList = new ArrayList<>();
            int size = q.size();              // 注意 size 不能 inline，否则 q.size() 每次取值会不同（因为循环体中会 offer）
            for (int i = 0; i < size; i++) {  // size 的大小就是该层的节点个数，因此循环 size 次把所有节点值添加进该层的列表 levelList 中
                TreeNode node = q.poll();
                levelList.add(node.val);
                if (node.left != null) q.offer(node.left);
                if (node.right != null) q.offer(node.right);
            }
            res.add(0, levelList);      // 最后将该层列表添加到 res 头部（注意是头部）
        }

        return res;
    }

    /*
     * 解法3：递归 + 最后 reverse
     * - 思路：与解法2大体相同，区别在于递归最后 res.get() 时的索引没有倒置，因此递归结束后需要再 reverse 一下，因此统计性能稍差于解法2。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高。
     * */
    public static List<List<Integer>> levelOrderBottom4(TreeNode root) {
        List<List<Integer>> res = new LinkedList<>();
        levelOrderBottom4(root, res, 0);
        Collections.reverse(res);  // 递归结束后需要再 reverse 一下
        return res;
    }

    private static void levelOrderBottom4(TreeNode node, List<List<Integer>> res, int level) {
        if (node == null) return;
        if (level == res.size()) res.add(new LinkedList<>());
        levelOrderBottom4(node.left, res, level + 1);
        levelOrderBottom4(node.right, res, level + 1);
        res.get(level).add(node.val);  // 直接获取第 level 个列表，因此递归结束后得到的 res 是反着的
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, 8, 15, 7});
        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, null, 15, 7});

        log(simpleLevelOrderBottom(t1));   // expects [8, 15, 7, 9, 20, 3]
        log(simpleLevelOrderBottom2(t1));  // expects [8, 15, 7, 9, 20, 3]

        log(levelOrderBottom(t1));         // expects [[8,15,7], [9,20], [3]]
        log(levelOrderBottom2(t1));        // expects [[8,15,7], [9,20], [3]]
        log(levelOrderBottom3(t1));        // expects [[8,15,7], [9,20], [3]]
        log(levelOrderBottom4(t1));        // expects [[8,15,7], [9,20], [3]]

        log(levelOrderBottom(t2));        // expects [[15,7], [9,20], [3]]
        log(levelOrderBottom2(t2));       // expects [[15,7], [9,20], [3]] (注意不应该是 [[9,15,7], [20], [3]])
        log(levelOrderBottom3(t2));       // expects [[15,7], [9,20], [3]]
        log(levelOrderBottom4(t2));       // expects [[15,7], [9,20], [3]]
    }
}
