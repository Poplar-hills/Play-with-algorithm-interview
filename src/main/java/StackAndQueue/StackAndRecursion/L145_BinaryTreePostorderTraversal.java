package StackAndQueue.StackAndRecursion;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static Utils.Helpers.*;

/*
* Binary Tree Postorder Traversal
*
* - Given a binary tree, return the postorder traversal of its nodes' values.
* */

public class L145_BinaryTreePostorderTraversal {
    /*
    * 解法1：递归
    * -
    * */
    public static List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        postorderTraversal(root, list);
        return list;
    }

    private static void postorderTraversal(TreeNode node, List<Integer> list) {
        if (node == null) return;
        postorderTraversal(node.left, list);
        postorderTraversal(node.right, list);
        list.add(node.val);
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeFromArray(new Integer[]{1, null, 2, 3});
        log(postorderTraversal(t1));  // expects [3, 2, 1]

        TreeNode t2 = createBinaryTreeFromArray(new Integer[]{});
        log(postorderTraversal(t2));  // expects []

        TreeNode t3 = createBinaryTreeFromArray(new Integer[]{5, 3, 1, null, null, 4, null, null, 7, null, 9});
        log(postorderTraversal(t3));  // expects [1, 4, 3, 9, 7, 5]
    }
}
