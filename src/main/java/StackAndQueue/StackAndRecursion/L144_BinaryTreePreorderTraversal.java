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
    * 解法1：intuitive 递归
    * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是树高。
    * */
    public static List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        preorderTraversal(root, res);
        return res;
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
    * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是树高。
    * */
    public static List<Integer> preorderTraversal2(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode curr = stack.pop();
            res.add(curr.val);
            if (curr.right != null) stack.push(curr.right);
            if (curr.left != null) stack.push(curr.left);
        }

        return res;
    }

    /*
     * 解法3：迭代2
     * - 思路：先向左递归到底，一路上访问每一个节点并入栈每一个左子节点，到底后出栈节点并开始从其右子节点重复前面这样的过程。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是树高。
     * */
    public static List<Integer> preorderTraversal3(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode curr = root;

        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                res.add(curr.val);  // 与 L94 的解法2不同，前序遍历在这里先访问节点
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.pop().right;
        }

        return res;
    }

    /*
     * 解法4：迭代（解法3的变种）
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是树高。
     * */
    public static List<Integer> preorderTraversal4(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode curr = root;

        while (curr != null || !stack.isEmpty()) {
            if (curr != null) {  // 同样也是先往左走到底
                res.add(curr.val);
                stack.push(curr);
                curr = curr.left;
            } else {             // 再转向右一步
                curr = stack.pop();
                curr = curr.right;
            }
        }

        return res;
    }

    /*
    * 解法5：迭代（模拟指令）
    * - 思路：使用栈来模拟树的遍历能写出解法2，但解法2是在栈中存储节点。若在栈中存入节点的同时还存入对该节点的操作指令（遍历或访问），
    *   则可以得到另一种解法。例如：
    *               5       |      |       |      |
    *            /    \     |      |       |______|
    *          3       8    |      |  -->  |_v__5_| 访问节点5
    *        /  \     /     |______|       |_i__3_| 遍历节点3
    *       1    4   6      |_i__5_|       |_i__8_| 遍历节点8
    *   首先入栈1条指令"遍历节点5"，执行这条指令时相应要入栈"遍历节点8"、"遍历节点3"、"访问节点5"这3条指令。
    * - 优势：这种解法虽然繁琐一点，但是更加灵活，只需极少的改动即可变为中序或后续遍历（SEE: L94 的解法4、L145 的解法4）。
    * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是树高。
    * */
    static class Command {  // 将节点和指令的组合抽象成 Command（对什么节点做什么事）
        String type;        // type 取值只能为 "iterate" or "visit"（更好的方式是用枚举）
        TreeNode node;
        Command(String type, TreeNode node) {
            this.type = type;
            this.node = node;
        }
    }

    public static List<Integer> preorderTraversal5(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        Deque<Command> stack = new ArrayDeque<>();   // 栈中存的是 Command
        stack.push(new Command("iterate", root));

        while (!stack.isEmpty()) {
            Command cmd = stack.pop();
            TreeNode curr = cmd.node;
            if (cmd.type.equals("visit"))
                res.add(curr.val);
            else {                  // 若 type 是 "iterate"
                if (curr.right != null)
                    stack.push(new Command("iterate", curr.right));
                if (curr.left != null)
                    stack.push(new Command("iterate", curr.left));
                stack.push(new Command("visit", curr));  // visit 指令最后入栈、最先执行
            }
        }

        return res;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeDepthFirst(new Integer[]{1, null, 2, 3});
        log(preorderTraversal3(t1));  // expects [1, 2, 3]

        TreeNode t2 = createBinaryTreeDepthFirst(new Integer[]{});
        log(preorderTraversal3(t2));  // expects []

        TreeNode t3 = createBinaryTreeDepthFirst(new Integer[]{5, 3, 1, null, null, 4, null, null, 7, 6});
        log(preorderTraversal3(t3));  // expects [5, 3, 1, 4, 7, 6]
    }
}
