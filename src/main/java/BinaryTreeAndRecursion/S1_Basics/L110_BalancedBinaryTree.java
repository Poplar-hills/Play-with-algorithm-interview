package BinaryTreeAndRecursion.S1_Basics;

import static Utils.Helpers.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import Utils.Helpers.TreeNode;

/*
 * Balanced Binary Tree
 *
 * - Given a binary tree, determine if it is height-balanced, which means the depth of the two subtrees of
 *   every node never differ by more than 1.
 * */

public class L110_BalancedBinaryTree {
    /*
     * 解法1：DFS (Recursion)
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static boolean isBalanced(TreeNode root) {
        int[] depths = getDepths(root);
        return depths[0] - depths[1] <= 1;
    }

    private static int[] getDepths(TreeNode root) {
        if (root == null) return new int[]{0, 0};
        int[] l = getDepths(root.left);
        int[] r = getDepths(root.right);
        int maxDepth = Math.max(l[0], r[0]) + 1;
        int minDepth = Math.min(l[1], r[1]) + 1;
        return new int[]{maxDepth, minDepth};
    }

    /*
     * 解法2：DFS (Recursion)
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static boolean isBalanced2(TreeNode root) {
        return maxDepth(root) != -1;
    }

    private static int maxDepth(TreeNode root) {  // 计算左右子树的最大深度，若深度差 > 1，则返回 -1，否则返回最大深度
        if (root == null) return 0;
        int left = maxDepth(root.left);
        if (left == -1) return -1;
        int right = maxDepth(root.right);
        if (right == -1) return -1;
        return Math.abs(left - right) <= 1 ? 1 + Math.max(left, right) : -1;
    }

    /*
     * 解法3：DFS (Iteration, post-order traversal)
     * - 思路：要知道一棵树是否平衡，需要先知道其左右子树的最大深度，即先访问左右子节点，再访问父节点，这其实就是二叉树的后续遍历。
     *   ∴ 需要做的就是在后续遍历的基础上将访问每个节点的逻辑替换成计算树的最大深度的逻辑即可。
     * - 👉 回顾：再反观解法1，其实就是二叉树后续遍历的递归实现（先为左右子节点进行计算，再为父节点计算）。
     * - 💎 总结：该解法是二叉树后续遍历的典型应用。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean isBalanced3(TreeNode root) {
        if (root == null) return true;
        Map<TreeNode, Integer> map = new HashMap<>();  // 使用 map 记录<节点, 以该节点为根的树的最大深度>
        Stack<TreeNode> stack = new Stack<>();         // 后续遍历是 DFS 的一种 ∴ 使用 stack 结构进行辅助
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            boolean isLeafNode = node.left == null && node.right == null;
            boolean leftDone = map.containsKey(node.left);
            boolean rightDone = map.containsKey(node.right);
            boolean childrenDone = (leftDone && rightDone) || (node.left == null && rightDone) || (node.right == null && leftDone);

            if (isLeafNode || childrenDone) {  // 若是叶子节点，或其左右子子树已经被访问过，则访问该节点并加入 map
                int leftDepth = map.getOrDefault(node.left, 0);
                int rightDepth = map.getOrDefault(node.right, 0);
                if (Math.abs(leftDepth - rightDepth) > 1) return false;
                map.put(node, 1 + Math.max(leftDepth, rightDepth));
            } else {                           // 若不是叶子节点，且左右子节点中还有没被访问过的，则放回栈中待后面访问
                stack.push(node);
                if (node.right != null) stack.push(node.right);
                if (node.left != null) stack.push(node.left);
            }
        }

        return true;
    }

    /*
     * 解法4：DFS (Iteration, post-order traversal)
     * - 思路：与解法2一致，只是采用后续遍历非递归的另一种实现。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static boolean isBalanced4(TreeNode root) {
        if (root == null) return true;
        Map<TreeNode, Integer> map = new HashMap<>();
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        TreeNode lastVisited = null, curr = root;

        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.pop();
            if (curr.right == null || curr.right == lastVisited) {
                int leftDepth = map.getOrDefault(curr.left, 0);  // 对节点的访问逻辑是一样的
                int rightDepth = map.getOrDefault(curr.right, 0);
                if (Math.abs(leftDepth - rightDepth) > 1) return false;
                map.put(curr, 1 + Math.max(leftDepth, rightDepth));

                lastVisited = curr;  // 访问完后将 curr 标记为已访问
                curr = null;         // 置空 curr，好在 stack.isEmpty() 时能退出 while 循环
            } else {
                stack.push(curr);
                curr = curr.right;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, null, null, 3, 3});
        log(isBalanced(t1));
        /*
         * expects true.
         *        1
         *       / \
         *      2   2
         *         / \
         *        3   3
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, 3, null, 3, 3});
        log(isBalanced(t2));
        /*
         * expects true.
         *         1
         *        / \
         *       2   2
         *      /   / \
         *     3   3   3
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, 3, 3, null, 3, 4, 4});
        log(isBalanced(t3));
        /*
         * expects true. (注意这个是平衡树 ∵ 本题中平衡树的定义是任意节点的左右子树的深度差最大不超过1)
         *           1
         *          / \
         *         2   2
         *        / \   \
         *       3   3   3
         *      / \
         *     4   4
         * */

        TreeNode t4 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, 3, 3, null, null, 4, 4});
        log(isBalanced(t4));
        /*
         * expects false.
         *           1
         *          / \
         *         2   2
         *        / \
         *       3   3
         *      / \
         *     4   4
         * */

        TreeNode t5 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, 3, null, null, 3, 4, null, null, 4});
        log(isBalanced(t5));
        /*
         * expects false.
         *           1
         *          / \
         *         2   2
         *        /     \
         *       3       3
         *      /         \
         *     4           4
         * */
    }
}
