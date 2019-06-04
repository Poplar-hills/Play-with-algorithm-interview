package StackAndQueue.StackAndRecursion;

import java.util.ArrayList;
import java.util.List;

import static Utils.Helpers.*;

/*
* Binary Tree Inorder Traversal
*
* - Given a binary tree, return the inorder traversal of its nodes' values.
* */

public class L94_BinaryTreeInorderTraversal {
    /*
    * 解法1：递归
    * */
    public static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        inorderTraversal(root, list);
        return list;
    }

    private static void inorderTraversal(TreeNode node, List<Integer> list) {
        if (node == null) return;
        inorderTraversal(node.left, list);
        list.add(node.val);
        inorderTraversal(node.right, list);
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeFromArray(new Integer[]{1, null, 2, 3});
        log(inorderTraversal(t1));  // expects [1, 3, 2]

        TreeNode t2 = createBinaryTreeFromArray(new Integer[]{});
        log(inorderTraversal(t2));  // expects []
    }
}
