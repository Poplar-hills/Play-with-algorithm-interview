package StackAndQueue.ClassicQueue;

import java.util.List;

import static Utils.Helpers.*;

/*
* Binary Tree Level Order Traversal II
*
* - Given a binary tree, return the bottom-up level order traversal of its nodes' values.
*   (ie, from left to right, level by level from leaf to root).
* */

public class L107_BinaryTreeLevelOrderTraversalII {
    public static List<List<Integer>> levelOrderBottom(TreeNode root) {

    }

    public static void main(String[] args) {
        TreeNode t = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, 8, 15, 7});
        log(levelOrderBottom(t));  // expects [[8,15,7], [9,20], [3]]
    }
}
