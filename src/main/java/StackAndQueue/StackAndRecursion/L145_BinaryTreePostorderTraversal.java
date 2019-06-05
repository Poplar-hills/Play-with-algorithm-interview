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
    * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是二叉树的高度。
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

    /*
     * 解法2：迭代
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是二叉树的高度。
     * */
    public static List<Integer> postorderTraversal2(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        Deque<TreeNode> stack = new ArrayDeque<>();
        if (root == null) return list;

        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode temp = stack.peek();
            if (temp.left != null) {
                stack.push(temp.left);
                temp.left = null;
            }
            else if (temp.right != null) {
                stack.push(temp.right);
                temp.right = null;
            }
            else {
                list.add(temp.val);
                stack.pop();
            }
        }

        return list;
    }


    /*
     * 解法4：遍历3
     * - 思路：模拟系统栈的指令
     * - 优势：这种解法虽然繁琐一点，但是更加灵活，只需极少的改动即可变为中序或后续遍历（SEE: L94 的解法4、L144 的解法4）。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是二叉树的高度。
     * */
    static class Command {
        String type;
        TreeNode node;
        Command(String type, TreeNode node) {
            this.type = type;
            this.node = node;
        }
    }

    public static List<Integer> postorderTraversal4(TreeNode root) {
        Deque<Command> stack = new ArrayDeque<>();   // 栈中存的是 Command（将节点和指令的 pair）
        List<Integer> list = new ArrayList<>();
        if (root == null) return list;

        stack.push(new Command("iterate", root));
        while (!stack.isEmpty()) {
            Command cmd = stack.pop();
            TreeNode curr = cmd.node;
            if (cmd.type.equals("visit"))
                list.add(cmd.node.val);
            else {
                stack.push(new Command("visit", curr));
                if (curr.right != null)
                    stack.push(new Command("iterate", curr.right));
                if (curr.left != null)
                    stack.push(new Command("iterate", curr.left));
            }
        }

        return list;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeFromArray(new Integer[]{1, null, 2, 3});
        log(postorderTraversal4(t1));  // expects [3, 2, 1]

        TreeNode t2 = createBinaryTreeFromArray(new Integer[]{});
        log(postorderTraversal4(t2));  // expects []

        TreeNode t3 = createBinaryTreeFromArray(new Integer[]{5, 3, 1, null, null, 4, null, null, 7, 6});
        log(postorderTraversal4(t3));  // expects [1, 4, 3, 6, 7, 5]
    }
}
