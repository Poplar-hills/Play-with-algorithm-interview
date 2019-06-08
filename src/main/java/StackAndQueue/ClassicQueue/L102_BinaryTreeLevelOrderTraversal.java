package StackAndQueue.ClassicQueue;

import java.util.List;

import static Utils.Helpers.*;

/*
* Binary Tree Level Order Traversal
*
* - Given a binary tree, return the level order traversal of its nodes' values. (ie, from left to right, level by level).
* */

public class L102_BinaryTreeLevelOrderTraversal {
    public static List<List<Integer>> levelOrder(TreeNode root) {

    }

    public static void main(String[] args) {
        TreeNode t = createBinaryTreeFromArray(new Integer[]{3, 9, 20, null, null, 15, 7});
        log(levelOrder(t));  // expects [[3], [9,20], [15,7]]
    }
}
