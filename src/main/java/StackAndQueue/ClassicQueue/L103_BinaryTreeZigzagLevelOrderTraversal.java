package StackAndQueue.ClassicQueue;

import java.util.*;

import static Utils.Helpers.*;

/*
* Binary Tree Zigzag Level Order Traversal
*
* - Given a binary tree, return the zigzag level order traversal of its nodes' values. (ie, from left to right,
*   then right to left for the next level and alternate between).
* */

public class L103_BinaryTreeZigzagLevelOrderTraversal {
    /*
    * 解法1：递归 + 最后 reverse
    * */
    public static List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        zigzagLevelOrder(root, res, 0);
        reverseOddLists(res);
        return res;
    }

    private static void zigzagLevelOrder(TreeNode node, List<List<Integer>> res, int level) {
        if (node == null) return;
        if (level == res.size()) res.add(new ArrayList<>());
        res.get(level).add(node.val);
        zigzagLevelOrder(node.left, res, level + 1);
        zigzagLevelOrder(node.right, res, level + 1);
    }

    private static void reverseOddLists(List<List<Integer>> res) {
        for (int i = 0; i < res.size(); i++)
            if (i % 2 == 1)
                Collections.reverse(res.get(i));
    }

    public static void main(String[] args) {
        TreeNode t = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, null, 15, 7});
        log(zigzagLevelOrder(t));  // expects [[3], [20,9], [15,7]]
    }
}
