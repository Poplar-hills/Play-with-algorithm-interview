package StackAndQueue.ClassicQueue;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static Utils.Helpers.*;

/*
* Binary Tree Level Order Traversal
*
* - Given a binary tree, return the level order traversal of its nodes' values. (ie, from left to right, level by level).
* */

public class L102_BinaryTreeLevelOrderTraversal {
    /*
    * 解法1：
    * */
    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> list = new ArrayList<>();
        Queue<Pair<TreeNode, Integer>> q = new LinkedList<>();
        if (root == null) return list;

        q.offer(new Pair<>(root, 1));
        while (!q.isEmpty()) {
            Pair<TreeNode, Integer> pair = q.poll();
            TreeNode node = pair.getKey();
            Integer level = pair.getValue();

            if (level > list.size()) {
                List<Integer> l = new ArrayList<>();
                l.add(node.val);
                list.add(l);
            } else
                list.get(level - 1).add(node.val);

            if (node.left != null) q.offer(new Pair<>(node.left, level + 1));
            if (node.right != null) q.offer(new Pair<>(node.right, level + 1));
        }

        return list;
    }

    public static void main(String[] args) {
        TreeNode t = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, null, 15, 7});
        log(levelOrder(t));  // expects [[3], [9,20], [15,7]]
    }
}
