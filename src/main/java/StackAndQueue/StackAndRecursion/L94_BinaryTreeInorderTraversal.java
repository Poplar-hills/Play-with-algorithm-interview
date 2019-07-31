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
    * 解法1：intuitive 递归
    * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是树高。
    * */
    public static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        inorderTraversal(root, res);
        return res;
    }

    private static void inorderTraversal(TreeNode node, List<Integer> list) {
        if (node == null) return;
        inorderTraversal(node.left, list);
        list.add(node.val);
        inorderTraversal(node.right, list);
    }

    /*
    * 解法2：迭代
    * - 思路：与 L144 的解法3思路类似，唯一区别在于访问节点的时机 —— 向左递归到底的路上先不访问节点，而是到底之后开始出栈时再访问节点。
    * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是树高。
    * */
    public static List<Integer> inorderTraversal2(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode curr = root;

        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.pop();
            res.add(curr.val);     // 到底后再访问出栈的节点
            curr = curr.right;     // 对右子节点重复前面的过程
        }
        return res;
    }

    /*
     * 解法3：迭代（解法2的变种）
     * - 思路：类似 L144 的解法3思路。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是树高。
     * */
    public static List<Integer> inorderTraversal3(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode curr = root;

        while (curr != null || !stack.isEmpty()) {
            if (curr != null) {
                stack.push(curr);
                curr = curr.left;
            } else {
                curr = stack.pop();
                res.add(curr.val);
                curr = curr.right;
            }
        }

        return res;
    }

    /*
    * 解法4：迭代（模拟指令）
    * - 思路：在栈中存入节点的同时还存入对该节点的操作指令（遍历或访问）：
    *               5       |      |     |      |     |_i__1_|     |_v__1_|     |______|     |      |
    *            /    \     |      |     |______|     |_v__3_|     |_v__3_|     |_v__3_|     |______|
    *          3       8    |      | --> |_i__3_| --> |_i__4_| --> |_i__4_| --> |_i__4_| --> |_i__4_| ...
    *        /  \     /     |______|     |_v__5_|     |_v__5_|     |_v__5_|     |_v__5_|     |_v__5_|
    *       1    4   6      |_i__5_|     |_i__8_|     |_i__8_|     |_i__8_|     |_i__8_|     |_i__8_|
    *                          []           []           []           []          [1]         [1,3]
    * - 优势：这种解法虽然繁琐一点，但是更加灵活，只需极少的改动即可变为中序或后续遍历（SEE: L144 的解法5、L145 的解法5）。
    * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是树高。
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
        List<Integer> res = new ArrayList<>();
        Deque<Command> stack = new ArrayDeque<>();   // 栈中存的是 Command（将节点和指令的 pair）
        if (root == null) return res;

        stack.push(new Command("iterate", root));
        while (!stack.isEmpty()) {
            Command cmd = stack.pop();
            TreeNode node = cmd.node;
            if (cmd.type.equals("visit"))
                res.add(node.val);
            else {
                if (node.right != null)
                    stack.push(new Command("iterate", node.right));
                stack.push(new Command("visit", node));  // visit 指令在 iterate 两个子节点之间执行
                if (node.left != null)
                    stack.push(new Command("iterate", node.left));
            }
        }

        return res;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeDepthFirst(new Integer[]{1, null, 2, 3});
        log(inorderTraversal(t1));  // expects [1, 3, 2]

        TreeNode t2 = createBinaryTreeDepthFirst(new Integer[]{});
        log(inorderTraversal(t2));  // expects []

        TreeNode t3 = createBinaryTreeDepthFirst(new Integer[]{5, 3, 1, null, null, 4, null, null, 7, 6});
        log(inorderTraversal(t3));  // expects [1, 3, 4, 5, 6, 7]
    }
}
