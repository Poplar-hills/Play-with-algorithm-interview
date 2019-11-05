package BinaryTreeAndRecursion.Basics;

import static Utils.Helpers.*;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import Utils.Helpers.TreeNode;

/*
 * Symmetric Tree
 *
 * - Given a binary tree, check whether it is a mirror of itself (ie, symmetric around its center, both in
 *   structure and node values).
 * */

public class L101_SymmetricTree {
    /*
     * 解法1：Recursion (DFS)
     * - 思路：若一棵树是对称的，则其左右子树应互为镜像。若两棵树互为镜像，则：
     *     1. 他们的根节点的节点值相同；
     *     2. 树1的左子树和树2的右子树互为镜像；
     *     3. 树1的右子树和树2的左子树互为镜像。
     *   这是个递归定义 ∴ 可以很自然的用递归求解，递归函数的语义就是求两棵树是否互为镜像。而这里“互为镜像”的含义是：
     *     1. 两棵树上任意相同位置同时有或同时没有节点；
     *     2. 若有节点，则节点值相同。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static boolean isSymmetric(TreeNode root) {
        return isMirror(root, root);  // 递归入口需要特殊处理一下，以符合递归函数的语义
    }

    private static boolean isMirror(TreeNode t1, TreeNode t2) {  // 检查两棵树是否互为镜像
        if (t1 == null && t2 == null) return true;               // 同时有节点
        if (t1 == null || t2 == null) return false;              // 同时没有节点
        return t1.val == t2.val && isMirror(t1.left, t2.right) && isMirror(t1.right, t2.left);
    }

    /*
     * 解法2：Iteration (BFS)
     * - 思路：对树同时从两个方向进行层序遍历，注意 null 节点也要入队检查。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean isSymmetric2(TreeNode root) {
        if (root == null) return true;
        Queue<TreeNode> q1 = new LinkedList<>();
        Queue<TreeNode> q2 = new LinkedList<>();
        q1.offer(root);
        q2.offer(root);

        while (!q1.isEmpty()) {
            TreeNode node1 = q1.poll();
            TreeNode node2 = q2.poll();
            if (node1 == null && node2 == null) continue;
            if (node1 == null || node2 == null) return false;
            if (node1.val != node2.val) return false;
            q1.offer(node1.left);   // q1 先入队 node1.left
            q1.offer(node1.right);
            q2.offer(node2.right);  // q2 先入队 node2.right
            q2.offer(node2.left);
        }

        return true;
    }

    /*
     * 解法3：Iteration (BFS)
     * - 思路：使用一个 Queue 同时入队左右两棵子树的节点。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean isSymmetric3(TreeNode root) {
        if (root == null) return true;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        q.offer(root);

        while (!q.isEmpty()) {
            TreeNode node1 = q.poll();
            TreeNode node2 = q.poll();
            if (node1 == null && node2 == null) continue;
            if (node1 == null || node2 == null) return false;
            if (node1.val != node2.val) return false;
            q.offer(node1.left);
            q.offer(node2.right);  // 注意这里跟解法2中的入队顺序不同，需对称入队
            q.offer(node1.right);
            q.offer(node2.left);
        }

        return true;
    }

    /*
     * 解法4：Iteration (DFS)
     * - 思路：对树同时从两个方向进行深度优先遍历（与解法3的差别只是数据结构不同）。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean isSymmetric4(TreeNode root) {
        if (root == null) return true;
        Stack<TreeNode> s = new Stack<>();
        s.add(root);
        s.add(root);

        while (!s.isEmpty()) {
            TreeNode node1 = s.pop();
            TreeNode node2 = s.pop();
            if (node1 == null && node2 == null) continue;
            if (node1 == null || node2 == null) return false;
            if (node1.val != node2.val) return false;
            s.add(node1.left);
            s.add(node2.right);  // 这里同解法3，需对称入栈
            s.add(node1.right);
            s.add(node2.left);
        }

        return true;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, 3, 4, 4, 3});
        log(isSymmetric4(t1));
        /*
         * expects true.
         *        1
         *       / \
         *      2   2
         *     / \ / \
         *    3  4 4  3
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, 3, null, null, 3});
        log(isSymmetric4(t2));
        /*
         * expects true.
         *        1
         *       / \
         *      2   2
         *     /     \
         *    3       3
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, 3, 4, 3, 4});
        log(isSymmetric4(t3));
        /*
         * expects false.
         *        1
         *       / \
         *      2   2
         *     / \ / \
         *    3  4 3  4
         * */

        TreeNode t4 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, null, 3, null, 3});
        log(isSymmetric4(t4));
        /*
         * expects false.
         *        1
         *       / \
         *      2   2
         *       \   \
         *        3   3
         * */
    }
}
