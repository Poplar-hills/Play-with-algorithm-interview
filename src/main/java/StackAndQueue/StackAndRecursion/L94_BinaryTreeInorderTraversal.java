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
    * - 思路：（与 L144 的解法3思路相同）先向左递归到底，入栈每一个左子节点，到底后出栈并访问每一个节点的右子节点。
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

    /*
     * 解法3：迭代（解法2的变种）
     * - 思路：（与 L144 的解法3思路相同）先向左递归到底，入栈每一个左子节点，到底后出栈并访问每一个节点的右子节点。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是二叉树的高度。
     * */
    public static List<Integer> inorderTraversal3(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode curr = root;

        while (curr != null || !stack.isEmpty()) {
            if (curr != null) {
                stack.push(curr);
                curr = curr.left;
            } else {
                curr = stack.pop();
                list.add(curr.val);
                curr = curr.right;
            }
        }

        return list;
    }

    /*
    * 解法4：迭代
    * - 思路：模拟系统栈的指令
    * - 优势：这种解法虽然繁琐一点，但是更加灵活，只需极少的改动即可变为中序或后续遍历（SEE: L144 的解法5、L145 的解法4）。
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

    public static List<Integer> inorderTraversal4(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        Deque<Command> stack = new ArrayDeque<>();   // 栈中存的是 Command（将节点和指令的 pair）
        if (root == null) return list;

        stack.push(new Command("iterate", root));
        while (!stack.isEmpty()) {
            Command cmd = stack.pop();
            TreeNode curr = cmd.node;
            if (cmd.type.equals("visit"))
                list.add(cmd.node.val);
            else {
                if (curr.right != null)
                    stack.push(new Command("iterate", curr.right));
                stack.push(new Command("visit", curr));  // visit 指令在 iterate 两个子节点之间执行
                if (curr.left != null)
                    stack.push(new Command("iterate", curr.left));
            }
        }

        return list;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeDepthFirst(new Integer[]{1, null, 2, 3});
        log(inorderTraversal4(t1));  // expects [1, 3, 2]

        TreeNode t2 = createBinaryTreeDepthFirst(new Integer[]{});
        log(inorderTraversal4(t2));  // expects []

        TreeNode t3 = createBinaryTreeDepthFirst(new Integer[]{5, 3, 1, null, null, 4, null, null, 7, 6});
        log(inorderTraversal4(t3));  // expects [1, 3, 4, 5, 6, 7]
    }
}
