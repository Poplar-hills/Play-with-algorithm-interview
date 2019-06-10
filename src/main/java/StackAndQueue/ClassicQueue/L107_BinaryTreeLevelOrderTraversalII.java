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

    public static void main(String[] args) {
        TreeNode t = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, 8, 15, 7});
        log(simpleLevelOrderBottom(t));   // expects [8, 15, 7, 9, 20, 3]
        log(simpleLevelOrderBottom2(t));  // expects [8, 15, 7, 9, 20, 3]
        log(levelOrderBottom(t));         // expects [[8,15,7], [9,20], [3]]
    }
}
