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
    * 该方法是最基本的层序遍历，不是该题的解，只是用于和解法1进行对比。
    * */
    public static List<Integer> simpleLevelOrder(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Queue<TreeNode> q = new LinkedList<>();
        if (root == null) return res;

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
    * 解法1：
    * */
    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        Queue<Pair<TreeNode, Integer>> q = new LinkedList<>();  // 因为结果要求同一层的节点放在一个列表中，因此这里队列中除了保存节点之外还需要保存层级信息
        if (root == null) return res;

        q.offer(new Pair<>(root, 1));
        while (!q.isEmpty()) {
            Pair<TreeNode, Integer> pair = q.poll();
            TreeNode node = pair.getKey();
            Integer level = pair.getValue();

            if (level > res.size()) {  //
                List<Integer> l = new ArrayList<>();
                l.add(node.val);
                res.add(l);
            } else
                res.get(level - 1).add(node.val);

            if (node.left != null) q.offer(new Pair<>(node.left, level + 1));
            if (node.right != null) q.offer(new Pair<>(node.right, level + 1));
        }

        return res;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, null, 15, 7});
        log(simpleLevelOrder(t1));  // expects [3, 9, 20, 15, 7]

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, null, 15, 7});
        log(levelOrder(t2));  // expects [[3], [9,20], [15,7]]
    }
}
