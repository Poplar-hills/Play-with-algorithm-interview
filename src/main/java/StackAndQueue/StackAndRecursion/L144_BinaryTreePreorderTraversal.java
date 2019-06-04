package StackAndQueue.StackAndRecursion;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
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

    /*
    * 解法2：遍历
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static List<Integer> preorderTraversal2(TreeNode root) {
        Deque<TreeNode> stack = new ArrayDeque<>();
        List<Integer> list = new ArrayList<>();
        if (root == null) return list;

        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            list.add(node.val);
            if (node.right != null) stack.push(node.right);
            if (node.left != null) stack.push(node.left);
        }

        return list;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeFromArray(new Integer[]{1, null, 2, 3});
        log(preorderTraversal2(t1));  // expects [1, 2, 3]

        TreeNode t2 = createBinaryTreeFromArray(new Integer[]{});
        log(preorderTraversal2(t2));  // expects []
    }
}
