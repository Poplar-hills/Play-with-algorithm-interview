package StackAndQueue.S2_StackAndRecursion;

import java.util.*;

import static Utils.Helpers.*;

/*
 * Binary Tree Postorder Traversal
 *
 * - Given a binary tree, return the postorder traversal of its nodes' values.
 * - Postorder traversal：首先遍历右子树，然后遍历左子树，最后访问根结点（根结点最后访问）。
 * */

public class L145_BinaryTreePostorderTraversal {
    /*
     * 解法1：递归
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是树高。
     * */
    public static List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        postorderTraversal(root, res);
        return res;
    }

    private static void postorderTraversal(TreeNode node, List<Integer> list) {
        if (node == null) return;
        postorderTraversal(node.left, list);
        postorderTraversal(node.right, list);
        list.add(node.val);
    }

    /*
     * 解法2：迭代（经典）
     * - 思路：∵ 后序遍历需要先访问两个子节点后再访问父节点，而访问右子节点又必须经过父节点才能拿到，因此：
     *   1. 经过父节点时，需要知道其右子节点是否被访问过；
     *   2. 若右子节点未被访问过，则经过父节点拿到右子节点后需要把父节点再放回 stack 中，等右子节点访问完后再回来处理父节点。
     * - 💎 技巧：
     *   - 对于要求节点访问顺序的题目，可以考虑 👆这种把节点拿出来再放回去的技巧。
     *   - 这种遍历树的题目中可以使用 curr = null 来改变代码行进路线，本解法中：
     *     > 若 curr 有右子树，则最后 curr 会指向右子节点，并在下一轮循环中会继续走进内层 while，对右子树进行遍历；
     *     > 若 curr 没有右子树，则访问父节点，且最后的 curr = null 会让代码在下一轮循环中避开内层 while，直接 stack.pop()。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是树高。
     * */
    public static List<Integer> postorderTraversal2(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        Stack<TreeNode> stack = new Stack<>();
        TreeNode prev = null, curr = root;  // 多维护一个 prev 指针，记录上一次访问的节点

        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {  // 先往左走到底，一路上入栈所有节点
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.pop();
            if (curr.right != null && curr.right != prev) {  // 若父节点有右子节点且还未被访问过，则把父节点放回 stack 中，先遍历右子树
                stack.push(curr);
                curr = curr.right;
            } else {                // 若父节点没有右子节点，或有右子节点但已经被访问过，则访问父节点
                res.add(curr.val);
                prev = curr;
                curr = null;        // 置空 curr 好跳过 while 循环（∵ 访问完父节点之后就该回到再上一层的节点了）
            }
        }

        return res;
    }

    /*
     * 解法3：迭代（切断子节点）
     * - 思路：另一个思路是 ∵ 后续遍历的访问顺序是 左 → 右 → 父 ∴ 可以在访问完一个子节点后就将其切断，从而确保访问顺序。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是树高。
     * */
    public static List<Integer> postorderTraversal3(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode node = stack.peek();   // 在不出栈的情况下检查该节点是否有左、右子节点
            if (node.left != null) {        // 若有左子节点
                stack.push(node.left);
                node.left = null;           // 入栈左子节点后就将其置空，以避免重复访问
            }
            else if (node.right != null) {  // 若只有右子节点
                stack.push(node.right);
                node.right = null;          // 入栈右子节点后就将其置空，以避免访重复访问
            }
            else {                          // 在确保没有左、右子节点后再访问该节点
                res.add(node.val);
                stack.pop();
            }
        }

        return res;
    }

    /*
     * 解法4：迭代
     * - 思路：前序遍历的方法之一是先往左遍历到底，一路上访问节点，当到底后再转向访问右子树，如此循环。由此可想：若若从根节点开始
     *   先往右遍历，一路上访问节点，当没有右子节点时再转向访问左子树，如此循环会得到什么？结果是访问到的节点顺序刚好与后序遍历
     *   应有的结果顺序相反 ∴ 可以使用一个 stack 将该结果倒序输出即可。
     * - 实现：根据该思路可知需要2个 stack —— 一个用于实现往右遍历，另一个用于倒序输出遍历结果。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 是树高。
     * - 👉语法：Java 中：
     *   - Stack 接口的实现有：Stack, ArrayDeque, LinkedList 都可以（其中 Stack 已经被 JavaDoc deprecated，推荐用 Deque 代替）；
     *   - Deque 接口的实现有：ArrayDeque, LinkedList；
     * */
    public static List<Integer> postorderTraversal4(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Stack<TreeNode> stack1 = new Stack<>();
        Stack<TreeNode> stack2 = new Stack<>();
        TreeNode curr = root;

        while (curr != null || !stack1.isEmpty()) {
            while (curr != null) {  // 先往右走到底，一路上入栈（2个栈）所有节点
                stack1.push(curr);
                stack2.push(curr);
                curr = curr.right;
            }
            curr = stack1.pop();    // 到底后转向开始遍历左子树（这里并不访问节点，只入栈）
            curr = curr.left;
        }

        while (!stack2.isEmpty())   // 使用 stack2 倒序输出
            res.add(stack2.pop().val);

        return res;
    }

    /*
     * 解法5：迭代（模拟指令）
     * - 思路：在栈中存入节点的同时还存入对该节点的操作指令（遍历或访问）：
     *            5       |      |     |      |     |_i__1_|     |_v__1_|     |______|     |      |
     *          /   \     |      |     |______|     |_i__4_|     |_i__4_|     |_v__4_|     |______|
     *         3     8    |      | --> |_i__3_| --> |_v__3_| --> |_v__3_| --> |_v__3_| --> |_v__3_| ...
     *        / \   /     |______|     |_i__8_|     |_i__8_|     |_i__8_|     |_i__8_|     |_i__8_|
     *       1   4 6      |_i__5_|     |_v__5_|     |_v__5_|     |_v__5_|     |_v__5_|     |_v__5_|
     *                       []           []           []           []          [1]         [1,4]
     * - 优势：这种解法虽然繁琐一点，但是更加灵活，只需极少的改动即可变为中序或后续遍历（SEE: L94 的解法4、L144 的解法5）。
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

    public static List<Integer> postorderTraversal5(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        Stack<Command> stack = new Stack<>();   // 栈中存的是 Command（将节点和指令的 pair）
        stack.push(new Command("traverse", root));

        while (!stack.isEmpty()) {
            Command cmd = stack.pop();
            TreeNode curr = cmd.node;
            if (cmd.type.equals("visit"))
                res.add(cmd.node.val);
            else {
                stack.push(new Command("visit", curr));  // visit 指令最先入栈、最后执行
                if (curr.right != null)
                    stack.push(new Command("traverse", curr.right));
                if (curr.left != null)
                    stack.push(new Command("traverse", curr.left));
            }
        }

        return res;
    }

    public static List<Integer> postorderTraversal0(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        Stack<TreeNode> stack = new Stack<>();
        TreeNode prev = null, curr = root;

        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.pop();
            if (curr.right != null && curr.right != prev) {
                stack.push(curr);
                curr = curr.right;
            } else {
                res.add(curr.val);
                prev = curr;
            }
        }

        return res;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeDepthFirst(new Integer[]{1, null, 2, 3});
        log(postorderTraversal2(t1));
        /*
         * expects [3, 2, 1]
         *      1
         *       \
         *        2
         *       /
         *      3
         * */

        TreeNode t2 = createBinaryTreeDepthFirst(new Integer[]{});
        log(postorderTraversal2(t2));
        /*
         * expects []
         * */

        TreeNode t3 = createBinaryTreeDepthFirst(new Integer[]{5, 3, 1, null, null, 4, null, null, 7, 6});
        log(postorderTraversal2(t3));
        /*
         * expects [1, 4, 3, 6, 7, 5]
         *         5
         *       /   \
         *      3     7
         *     / \   /
         *    1   4 6
         * */
    }
}
