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
    public static List<List<Integer>> zigzagLevelOrder(TreeNode root) {

    }

    public static void main(String[] args) {
        TreeNode t = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, null, 15, 7});
        log(zigzagLevelOrder(t));  // expects [[3], [20,9], [15,7]]
    }
}
