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
    * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是二叉树的高度。
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
    * 解法2：迭代
    * - 思路：访问并入栈每个元素，出栈时入栈其左右子节点。
    * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是二叉树的高度。
    * */
    public static List<Integer> preorderTraversal2(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        Deque<TreeNode> stack = new ArrayDeque<>();
        if (root == null) return list;

        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode curr = stack.pop();
            list.add(curr.val);
            if (curr.right != null) stack.push(curr.right);
            if (curr.left != null) stack.push(curr.left);
        }

        return list;
    }

    /*
     * 解法3：迭代
     * - 思路：先向左递归到底，入栈并访问每一个左子节点，到底后出栈并遍历每一个节点的右子节点。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是二叉树的高度。
     * */
    public static List<Integer> preorderTraversal3(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        Deque<TreeNode> stack = new ArrayDeque<>();
        if (root == null) return list;

        TreeNode curr = root;
        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                list.add(curr.val);  // 与 L94 的解法2不同，前序遍历在这里访问节点
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.pop().right;
        }
        return list;
    }

    /*
     * 解法4：迭代（解法3的变种）
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是二叉树的高度。
     * */
    public static List<Integer> preorderTraversal4(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode curr = root;

        while (curr != null || !stack.isEmpty()) {
            if (curr != null) {
                list.add(curr.val);
                stack.push(curr);
                curr = curr.left;
            } else {
                curr = stack.pop();
                curr = curr.right;
            }
        }

        return list;
    }

    /*
    * 解法5：迭代
    * - 思路：模拟系统栈的指令 —— 解法1的递归过程可以抽象为：1.访问节点值 2.遍历左子节点 3.遍历右子节点。这个过程若用栈来模拟：
    *               5       |      |       |      |
    *            /    \     |      |       |______|
    *          3       8    |      |  -->  |_v__5_| 访问5
    *        /  \     /     |______|       |_i__3_| 遍历左子节点3
    *       1    4   6      |_i__5_|       |_i__8_| 遍历右子节点8
    *   根据这种模拟能写出解法2，但解法2的实现是只在栈中存储节点。若我们在栈中存节点的同时还存入对该节点的操作指令（遍历或访问），
    *   即将节点和操作 pair 起来存储，从而可以得到另一种解法。
    * - 优势：这种解法虽然繁琐一点，但是更加灵活，只需极少的改动即可变为中序或后续遍历（SEE: L94 的解法4、L145 的解法4）。
    * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是二叉树的高度。
    * */
    static class Command {  // 将节点和指令的 pair 抽象成一个 Command
        String type;        // type 的取值只能为 "iterate" or "visit"（遍历或访问）（更好的方式是用枚举）
        TreeNode node;
        Command(String type, TreeNode node) {
            this.type = type;
            this.node = node;
        }
    }

    public static List<Integer> preorderTraversal5(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        Deque<Command> stack = new ArrayDeque<>();   // 栈中存的是 Command（将节点和指令的 pair）
        if (root == null) return list;

        stack.push(new Command("iterate", root));
        while (!stack.isEmpty()) {
            Command cmd = stack.pop();
            TreeNode curr = cmd.node;
            if (cmd.type.equals("visit"))
                list.add(curr.val);
            else {                          // 若是 "iterate"
                if (curr.right != null)
                    stack.push(new Command("iterate", curr.right));
                if (curr.left != null)
                    stack.push(new Command("iterate", curr.left));
                stack.push(new Command("visit", curr));  // visit 指令最后入栈、最先执行
            }
        }

        return list;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeDepthFirst(new Integer[]{1, null, 2, 3});
        log(preorderTraversal5(t1));  // expects [1, 2, 3]

        TreeNode t2 = createBinaryTreeDepthFirst(new Integer[]{});
        log(preorderTraversal5(t2));  // expects []

        TreeNode t3 = createBinaryTreeDepthFirst(new Integer[]{5, 3, 1, null, null, 4, null, null, 7, 6});
        log(preorderTraversal5(t3));  // expects [5, 3, 1, 4, 7, 6]
    }
}
