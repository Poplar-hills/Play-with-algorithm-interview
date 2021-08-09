package StackAndQueue.S2_StackAndRecursion;

import java.util.*;

import static Utils.Helpers.*;

/*
 * Binary Tree Inorder Traversal
 *
 * - Given a binary tree, return the inorder traversal of its nodes' values.
 * - Inorder traversal：首先遍历左子树，然后访问根结点，最后遍历右子树（根结点在中间访问）；
 * */

public class L94_BinaryTreeInorderTraversal {
    /*
     * 解法1：递归
     * - 实现：最简单常见的 DFS 就是使用递归实现。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是树高。
     * */
    public static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        inorderTraversal(root, res);
        return res;
    }

    private static void inorderTraversal(TreeNode node, List<Integer> res) {
        if (node == null) return;
        inorderTraversal(node.left, res);
        res.add(node.val);
        inorderTraversal(node.right, res);
    }

    /*
     * 解法2：迭代
     * - 思路：与 L144 解法3类似，区别在于访问节点的时机 —— 先向左迭代到底，一路上只入栈而不不访问节点，到底之后开始出栈
     *   时再访问节点。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是树高。
     * */
    public static List<Integer> inorderTraversal2(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
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
     * 解法3：迭代（解法2的另一种写法）
     * - 思路：与解法2一致。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是树高。
     * */
    public static List<Integer> inorderTraversal3(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
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
     *             5       |      |     |      |     |_t__1_|     |_v__1_|     |______|     |      |
     *           /   \     |      |     |______|     |_v__3_|     |_v__3_|     |_v__3_|     |______|
     *          3     7    |      | --> |_t__3_| --> |_t__4_| --> |_t__4_| --> |_t__4_| --> |_t__4_| ...
     *         / \   /     |______|     |_v__5_|     |_v__5_|     |_v__5_|     |_v__5_|     |_v__5_|
     *        1   4 6      |_t__5_|     |_t__7_|     |_t__7_|     |_t__7_|     |_t__7_|     |_t__7_|
     *                        []           []           []           []          [1]         [1,3]
     * - 优势：这种解法虽然繁琐一点，但是更加灵活，只需极少的改动即可变为中序或后续遍历（SEE: L144 的解法5、L145 的解法5）。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是树高。
     * */
    public enum CmdType {
        TRAVERSE, VISIT
    }

    private static class Command {
        CmdType type;
        TreeNode node;
        Command(CmdType type, TreeNode node) {
            this.type = type;
            this.node = node;
        }
    }

    public static List<Integer> inorderTraversal4(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        Stack<Command> stack = new Stack<>();  // 栈中存的是 Command（将节点和指令的 pair）
        stack.push(new Command(CmdType.TRAVERSE, root));

        while (!stack.isEmpty()) {
            Command cmd = stack.pop();
            TreeNode node = cmd.node;
            if (cmd.type == CmdType.VISIT)
                res.add(node.val);
            else {
                if (node.right != null)
                    stack.push(new Command(CmdType.TRAVERSE, node.right));  // 先入栈右子节点
                stack.push(new Command(CmdType.VISIT, node));               // 访问父节点
                if (node.left != null)
                    stack.push(new Command(CmdType.TRAVERSE, node.left));   // 再入栈左子节点
            }
        }

        return res;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeDepthFirst(new Integer[]{5, 3, 1, null, null, 4, null, null, 7, 6});
        log(inorderTraversal4(t1));
        /*
         * expects [1, 3, 4, 5, 6, 7].（若是 BST，则中序遍历结果一定是有序的）
         *         5
         *       /   \
         *      3     7
         *     / \   /
         *    1   4 6
         * */

        TreeNode t2 = createBinaryTreeDepthFirst(new Integer[]{1, null, 2, 3});
        log(inorderTraversal4(t2));
        /*
         * expects [1, 3, 2]
         *      1
         *       \
         *        2
         *       /
         *      3
         * */

        TreeNode t3 = createBinaryTreeDepthFirst(new Integer[]{});
        log(inorderTraversal4(t3));
        /*
         * expects []
         * */
    }
}
