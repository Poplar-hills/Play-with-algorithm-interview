package StackAndQueue.StackAndRecursion;

import java.util.*;

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
    * 解法3：迭代
    * - 思路：先从右侧开始入栈右子节点，再转而遍历左子节点，使用两个 stack。
    * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是二叉树的高度。
    * - 注：Java 中 Stack 接口的实现有很多：Stack, ArrayDeque, LinkedList 都可以（其中 Stack 已经被 JavaDoc deprecated）。
    * */
    public static List<Integer> postorderTraversal3(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        Deque<TreeNode> stack1 = new ArrayDeque<>(), stack2 = new ArrayDeque<>();  // stack1 可入可出，stack2 只入不出
        TreeNode curr = root;

        while (curr != null || !stack1.isEmpty()) {
            if (curr != null) {     // 先沿着右侧遍历所有右子节点
                stack1.push(curr);  // 将右子节点同时入栈到 stack1、stack2 中
                stack2.push(curr);
                curr = curr.right;
            } else {                // 当不再有右子节点，转而开始遍历 stack1 中节点的左子节点
                curr = stack1.pop();
                curr = curr.left;
            }
        }

        while (!stack2.isEmpty())
            list.add(stack2.pop().val);

        return list;
    }

    /*
    * 解法4：迭代
    * - 思路：模拟系统栈的指令
    * - 优势：这种解法虽然繁琐一点，但是更加灵活，只需极少的改动即可变为中序或后续遍历（SEE: L94 的解法4、L144 的解法5）。
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
                stack.push(new Command("visit", curr));  // visit 指令最先入栈、最后执行
                if (curr.right != null)
                    stack.push(new Command("iterate", curr.right));
                if (curr.left != null)
                    stack.push(new Command("iterate", curr.left));
            }
        }

        return list;
    }

    /*
    * 解法5：迭代
    * - 思路：使用一个标志位表示一个节点是否已经被遍历过，若没有则遍历它，若已遍历过则访问它的值。
    * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是二叉树的高度。
    * */
    static class TaggedNode {
        boolean hasIterated;
        TreeNode node;
        TaggedNode(TreeNode node) {
            this.node = node;
            hasIterated = false;
        }
    }

    public static List<Integer> postorderTraversal5(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        Deque<TaggedNode> stack = new ArrayDeque<>();
        if (root == null) return list;

        TreeNode curr = root;
        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                stack.push(new TaggedNode(curr));
                curr = curr.left;
            }
            TaggedNode tNode = stack.pop();
            curr = tNode.node;
            if (!tNode.hasIterated) {
                tNode.hasIterated = true;
                stack.push(tNode);        // 将标志位置为 true 后放回 stack 中
                curr = curr.right;
            } else {
                list.add(curr.val);
                curr = null;              // 访问过值的节点就直接丢弃掉
            }
        }

        return list;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeFromArray(new Integer[]{1, null, 2, 3});
        log(postorderTraversal5(t1));  // expects [3, 2, 1]

        TreeNode t2 = createBinaryTreeFromArray(new Integer[]{});
        log(postorderTraversal5(t2));  // expects []

        TreeNode t3 = createBinaryTreeFromArray(new Integer[]{5, 3, 1, null, null, 4, null, null, 7, 6});
        log(postorderTraversal5(t3));  // expects [1, 4, 3, 6, 7, 5]
    }
}
