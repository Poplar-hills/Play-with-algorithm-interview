package StackAndQueue.ClassicQueue;

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

        for (int j = arr.size() - 1; j >= 0; j--)
            res.add(arr.get(j).val);

        return res;
    }

//    public static List<List<Integer>> levelOrderBottom(TreeNode root) {
//
//    }

    public static void main(String[] args) {
        TreeNode t = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, 8, 15, 7});
        log(simpleLevelOrderBottom(t));    // expects [8, 15, 7, 9, 20, 3]
        log(simpleLevelOrderBottom2(t));   // expects [8, 15, 7, 9, 20, 3]
//        log(levelOrderBottom(t));        // expects [[8,15,7], [9,20], [3]]
    }
}
