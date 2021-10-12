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
     * - 思路：∵ 题中对 height-balanced 的定义是“任意节点的左右子树的高度差 <= 1” ∴ 按照该定义设计程序，只要任意子树不平衡，
     *   则整棵树都不平衡 ∴ 自下而上为每个节点计算其左右子树的高度差，即判断以每个节点为根的二叉树是否是 height-balanced 的。
     * - 实现：每层递归返回结构为 Pair<ifBalanced, currDepth>：
     *   - ifBalanced 表示以当前节点为根的二叉树是否平衡值；
     *   - currDepth 表示以当前节点为根的二叉树的最大高度；
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static boolean isBalanced(TreeNode root) {
        Pair<Boolean, Integer> res = getBalanceInfo(root);
        return res.getKey();
    }

    private static Pair<Boolean, Integer> getBalanceInfo(TreeNode root) {
        if (root == null) return new Pair<>(true, 0);

        Pair<Boolean, Integer> lInfo = getBalanceInfo(root.left);
        Pair<Boolean, Integer> rInfo = getBalanceInfo(root.right);
        if (!lInfo.getKey() || !rInfo.getKey())  // 若左右子树任一不是 height-balanced 的，则整棵树就不是
            return new Pair<>(false, null);

        boolean isCurrBalanced = Math.abs(lInfo.getValue() - rInfo.getValue()) <= 1;  // 最后再看当前节点上是否平衡
        int currDepth = Math.max(lInfo.getValue(), rInfo.getValue()) + 1;
        return new Pair<>(isCurrBalanced, currDepth);
    }

    /*
     * 解法2：DFS (Recursion)
     * - 思路：思路与解法1一致。
     * - 实现：简化解法1中每层递归的返回结构，用 -1 表示不平衡，用自然数表示高度差，从而统一返回值类型，简化代码。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static boolean isBalanced2(TreeNode root) {
        return maxDepthDiff(root) != -1;
    }

    private static int maxDepthDiff(TreeNode root) {
        if (root == null) return 0;
        int lDiff = maxDepthDiff(root.left);
        if (lDiff == -1) return -1;  // 不同于解法1，若一边子树已经不是平衡的，则没有必要再对另一子树执行 getBalanceInfo
        int rDiff = maxDepthDiff(root.right);
        if (rDiff == -1) return -1;
        return Math.abs(lDiff - rDiff) <= 1 ? Math.max(lDiff, rDiff) + 1 : -1;
    }

    /*
     * 解法3：DFS (Iteration, post-order traversal)
     * - 思路：与解法2一致，都是使用 DFS 自下而上的为每个节点计算最大深度，判断以该节点为根的二叉树是否平衡。
     * - 实现：∵ 是自下而上 ∴ 需要先获得其左右子树的深度，即先访问左右子节点，再访问父节点，这其实就是二叉树后续遍历的过程。
     *   ∴ 只要在后续遍历的基础上将访问每个节点的逻辑替换成计算以该节点为根的树的深度即可。
     * - 👉 回顾：再反观解法1、2，其实就是二叉树后续遍历的递归实现（先为左右子节点进行计算，再为父节点计算）。
     * - 💎 总结：该问题是二叉树后续遍历的典型应用。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean isBalanced3(TreeNode root) {
        if (root == null) return true;
        Map<TreeNode, Integer> map = new HashMap<>();  // 记录 <节点, 以该节点为根的树的最大深度>
        Stack<TreeNode> stack = new Stack<>();         // 后续遍历是 DFS 的一种 ∴ 使用 stack 结构进行辅助
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            boolean isLeafNode = node.left == null && node.right == null;
            boolean lDone = map.containsKey(node.left);
            boolean rDone = map.containsKey(node.right);
            boolean childrenDone = (lDone && rDone) || (node.left == null && rDone) || (node.right == null && lDone);

            if (isLeafNode || childrenDone) {  // 若是叶子节点，或其左右子子树已经被访问过，则访问该节点并加入 map
                int lDepth = map.getOrDefault(node.left, 0);
                int rDepth = map.getOrDefault(node.right, 0);
                if (Math.abs(lDepth - rDepth) > 1) return false;
                map.put(node, Math.max(lDepth, rDepth) + 1);
            } else {                           // 若既不是叶子节点，且左右子节点中还有没被访问过的，则放回栈中待后面访问
                stack.push(node);
                if (node.right != null) stack.push(node.right);
                if (node.left != null) stack.push(node.left);
            }
        }

        return true;
    }

    /*
     * 解法4：DFS (Iteration, post-order traversal)
     * - 思路：与解法3一致，只是采用后续遍历非递归的另一种实现。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static boolean isBalanced4(TreeNode root) {
        if (root == null) return true;
        Map<TreeNode, Integer> map = new HashMap<>();
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        TreeNode prev = null, curr = root;

        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.pop();
            if (curr.right == null || curr.right == prev) {
                int lDepth = map.getOrDefault(curr.left, 0);  // 对节点的访问逻辑是一样的
                int rDepth = map.getOrDefault(curr.right, 0);
                if (Math.abs(lDepth - rDepth) > 1) return false;
                map.put(curr, 1 + Math.max(lDepth, rDepth));

                prev = curr;  // 访问完后将 curr 标记为已访问
                curr = null;  // 置空 curr，好在 stack.isEmpty() 时能退出 while 循环
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
         * expects true. (注意这个是平衡树 ∵ 本题中平衡树的定义是任意节点的左右子树的最大深度差不超过1)
         *           1
         *          / \
         *         2   2
         *        / \   \
         *       3   3   3
         *      / \
         *     4   4
         * */

        TreeNode t4 = createBinaryTreeBreadthFirst(new Integer[]{1,2,2,3,3,3,3,4,4,4,4,4,4,null,null,5,5});
        log(isBalanced(t4));
        /*
         * expects true.
         *            1
         *         /     \
         *        2       2
         *       / \     / \
         *      3   3   3   3
         *     / \ / \ / \
         *     4 4 4 4 4 4
         *    / \
         *    5 5
         * */

        TreeNode t5 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, 3, 3, null, null, 4, 4});
        log(isBalanced(t5));
        /*
         * expects false. (节点1的左、右子树都是平衡树，但两个平衡树的高度差 > 1 ∴ 整体不平衡)
         *           1
         *          / \
         *         2   2
         *        / \
         *       3   3
         *      / \
         *     4   4
         * */

        TreeNode t6 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, 3, null, null, 3, 4, null, null, 4});
        log(isBalanced(t6));
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
