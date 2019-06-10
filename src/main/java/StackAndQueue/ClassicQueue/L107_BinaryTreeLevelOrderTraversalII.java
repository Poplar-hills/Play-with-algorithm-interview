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

//    public static List<List<Integer>> levelOrderBottom(TreeNode root) {
//
//    }

    public static void main(String[] args) {
        TreeNode t = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, 8, 15, 7});
        log(simpleLevelOrderBottom(t));  // expects [8, 15, 7, 9, 20, 3]
//        log(levelOrderBottom(t));        // expects [[8,15,7], [9,20], [3]]
    }
}
