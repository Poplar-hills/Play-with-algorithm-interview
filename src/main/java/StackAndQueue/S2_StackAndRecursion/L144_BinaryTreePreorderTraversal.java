package StackAndQueue.S2_StackAndRecursion;

import java.util.*;

import static Utils.Helpers.*;

/*
 * Binary Tree Preorder Traversal
 *
 * - Given a binary tree, return the preorder traversal of its nodes' values.
 * */

public class L144_BinaryTreePreorderTraversal {
    /*
     * 解法1：递归
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
        Stack<TreeNode> stack = new Stack<>();
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
        Stack<TreeNode> stack = new Stack<>();
        TreeNode curr = root;

        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                res.add(curr.val);  // 与 L94 的解法2不同，前序遍历在这里先访问节点
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.pop();
            curr = curr.right;
        }

        return res;
    }

    /*
     * 解法4：迭代（解法3的变种）
     * - 思路：与解法3一致。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是树高。
     * */
    public static List<Integer> preorderTraversal4(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        TreeNode curr = root;

        while (curr != null || !stack.isEmpty()) {
            if (curr != null) {  // 同样也是先往左走到底
                res.add(curr.val);
                stack.push(curr);
                curr = curr.left;
            } else {             // 到底之后开始从 stack 中取出父节点，并转向去遍历父节点的右子树
                curr = stack.pop();
                curr = curr.right;
            }
        }

        return res;
    }

    /*
     * 解法5：迭代（模拟指令）
     * - 思路：使用栈来模拟树的遍历能写出解法2，但解法2是在栈中存储节点。若在栈中存入节点的同时还存入对该节点的操作指令（遍历或访问），
     *   则可以得到另一种解法：
     *             5       |      |     |      |     |      |     |      |     |      |     |      |
     *           /   \     |      |     |______|     |      |     |_v__3_|     |______|     |______|
     *          3     8    |      | --> |_v__5_| --> |______| --> |_i__1_| --> |_i__1_| --> |_v__1_| --> ...
     *         / \   /     |______|     |_i__3_|     |_i__3_|     |_i__4_|     |_i__4_|     |_i__4_|
     *        1   4 6      |_i__5_|     |_i__8_|     |_i__8_|     |_i__8_|     |_i__8_|     |_i__8_|
     *                        []           []          [5]          [5]         [5,3]        [5,3]
     *    1. 首先入栈1条指令"遍历节点5"，出栈执行这条指令时相应要入顺序栈"遍历节点8"、"遍历节点3"、"访问节点5"这3条指令；
     *    2. 之后出栈执行"访问节点5"，不需要入栈任何命令；
     *    3. 之后再出栈执行"遍历节点3"时相应要入栈"遍历节点4"、"遍历节点1"、"访问节点3"这3条指令。
     *    4. 依次类推....
     * - 优势：这种解法虽然繁琐一点，但是更加灵活，只需极少的改动即可变为中序或后续遍历（SEE: L94 的解法4、L145 的解法5）。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是树高。
     * */
    private static class Command {  // 将节点和指令的 pair 抽象成 Command
        String type;                // "traverse" or "visit"（更好的方式是用枚举类）
        TreeNode node;
        Command(String type, TreeNode node) {
            this.type = type;
            this.node = node;
        }
    }

    public static List<Integer> preorderTraversal5(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        Stack<Command> stack = new Stack<>();   // 栈中存的是 Command
        stack.push(new Command("traverse", root));

        while (!stack.isEmpty()) {
            Command cmd = stack.pop();
            TreeNode node = cmd.node;
            if (cmd.type.equals("visit"))
                res.add(node.val);
            else {                  // 若 type 是 "traverse"
                if (node.right != null)
                    stack.push(new Command("traverse", node.right));
                if (node.left != null)
                    stack.push(new Command("traverse", node.left));
                stack.push(new Command("visit", node));  // "visit" 指令最后入栈、最先执行
            }
        }

        return res;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeDepthFirst(new Integer[]{1, null, 2, 3});
        log(preorderTraversal(t1));
        /*
         * expects [1, 2, 3]
         *      1
         *       \
         *        2
         *       /
         *      3
         * */

        TreeNode t2 = createBinaryTreeDepthFirst(new Integer[]{});
        log(preorderTraversal(t2));
        /*
         * expects []
         * */

        TreeNode t3 = createBinaryTreeDepthFirst(new Integer[]{5, 3, 1, null, null, 4, null, null, 7, 6});
        log(preorderTraversal(t3));
        /*
         * expects [5, 3, 1, 4, 7, 6]
         *         5
         *       /   \
         *      3     7
         *     / \   /
         *    1   4 6
         * */
    }
}
