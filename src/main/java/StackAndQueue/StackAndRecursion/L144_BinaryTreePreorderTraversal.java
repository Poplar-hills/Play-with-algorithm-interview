package StackAndQueue.StackAndRecursion;

import java.util.ArrayList;
import java.util.List;

import static Utils.Helpers.*;

/*
* Binary Tree Preorder Traversal
*
* - Given a binary tree, return the preorder traversal of its nodes' values.
* */

public class L144_BinaryTreePreorderTraversal {
    /*
    * 解法1：递归
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        preorderTraversal(root, list);
        return list;
    }

    private static void preorderTraversal(TreeNode node, List<Integer> list) {
        if (node == null) return;
        list.add(node.val);
        preorderTraversal(node.left, list);
        preorderTraversal(node.right, list);
    }

    public static void main(String[] args) {
        TreeNode t = createBinaryTreeFromArray(new Integer[]{1, null, 2, 3});
        log(preorderTraversal(t));  // expects [1, 2, 3]
    }
}
