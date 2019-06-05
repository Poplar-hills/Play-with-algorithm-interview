package StackAndQueue.StackAndRecursion;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
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
    * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是二叉树的高度。
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

    /*
    * 解法2：迭代
    * - 思路：先向左递归到底，再访问右节点（与 L144 的解法3思路相同）。
    * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是二叉树的高度。
    * */
    public static List<Integer> inorderTraversal2(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode curr = root;

        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {  // 对一个节点先一直往左递归到底
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.pop();
            list.add(curr.val);     // 到底后访问出栈的节点（与 L144 的解法3不同，中序遍历在这里访问节点）
            curr = curr.right;      // 对其右子节点重新来过
        }
        return list;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeFromArray(new Integer[]{1, null, 2, 3});
        log(inorderTraversal2(t1));  // expects [1, 3, 2]

        TreeNode t2 = createBinaryTreeFromArray(new Integer[]{});
        log(inorderTraversal2(t2));  // expects []

        TreeNode t3 = createBinaryTreeFromArray(new Integer[]{5, 3, 1, null, null, 4, null, null, 7, 6});
        log(inorderTraversal2(t3));  // expects [1, 3, 4, 5, 6, 7]
    }
}
