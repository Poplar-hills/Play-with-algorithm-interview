package BinaryTreeAndRecursion.S1_Basics;

import static Utils.Helpers.*;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/*
 * Same Tree
 *
 * - Given two binary trees, check if they are the same or not.
 * - Two binary trees are considered the same if they are identical in both structure and nodes.
 * */

public class L100_SameTree {
    /*
     * 解法1：Recursion (DFS)
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) return true;
        if (p == null || q == null || p.val != q.val) return false;
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }

    /*
     * 解法2：Iteration (BFS)
     * - 时间复杂度 O(n)；
     * - 空间复杂度 O(n)，∵ q 中同时最多容纳 n/2 个节点（即完美二叉树的最后一行）∴ 是 O(n) 级别。
     * */
    public static boolean isSameTree2(TreeNode p, TreeNode q) {
        if (p == null && q == null) return true;
        Queue<Pair<TreeNode, TreeNode>> queue = new LinkedList<>();
        queue.offer(new Pair<>(p, q));

        while (!queue.isEmpty()) {
            Pair<TreeNode, TreeNode> pair = queue.poll();
            TreeNode n1 = pair.getKey();
            TreeNode n2 = pair.getValue();
            if (n1 == null && n2 == null) continue;
            if (n1 == null || n2 == null || n1.val != n2.val) return false;
            queue.offer(new Pair<>(n1.left, n2.left));
            queue.offer(new Pair<>(n1.right, n2.right));
        }

        return true;
    }

    /*
     * 解法3：Iteration (BFS)
     * - 思路：与解法2一致。
     * - 实现：使用双 queue。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean isSameTree3(TreeNode t1, TreeNode t2) {
        Queue<TreeNode> q1 = new LinkedList<>();
        Queue<TreeNode> q2 = new LinkedList<>();
        q1.offer(t1);
        q2.offer(t2);

        while (!q1.isEmpty() && !q2.isEmpty()) {
            TreeNode node1 = q1.poll();
            TreeNode node2 = q2.poll();
            if (node1 == null && node2 == null) continue;
            if (node1 == null || node2 == null || node1.val != node2.val) return false;
            q1.offer(node1.left);
            q1.offer(node1.right);
            q2.offer(node2.left);
            q2.offer(node2.right);
        }

        return q1.isEmpty() && q2.isEmpty();  // 使用两个 queue 的时候注意最后要确认两个 queue 是否都为空
    }

    /*
     * 解法4：Iteration (DFS)
     * - 思路：与解法2的逻辑一致，与 L226_InvertBinaryTree 解法3的思路一致。
     * - 实现：不同与解法2，该解法：
     *   1. DFS 使用 Stack 实现；
     *   2. 采用2个 Stack<TreeNode> 而不是一个 Queue 中存储 Pair<TreeNode, TreeNode>。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean isSameTree4(TreeNode p, TreeNode q) {
        Stack<TreeNode> pStack = new Stack<>();
        Stack<TreeNode> qStack = new Stack<>();
        pStack.push(p);
        qStack.push(q);

        while (!pStack.isEmpty()) {
            TreeNode pNode = pStack.pop();
            TreeNode qNode = qStack.pop();
            if (pNode == null && qNode == null) continue;
            if (pNode == null || qNode == null || pNode.val != qNode.val) return false;
            pStack.push(pNode.left);
            pStack.push(pNode.right);
            qStack.push(qNode.left);
            qStack.push(qNode.right);
        }

        return true;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3});
        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3});
        log(isSameTree3(t1, t2));
        /*
         * expects true.
         *      1         1
         *     / \       / \
         *    2   3     2   3
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{1, 2});
        TreeNode t4 = createBinaryTreeBreadthFirst(new Integer[]{1, null, 2});
        log(isSameTree3(t3, t4));
        /*
         * expects false. (值相同而结构不同)
         *      1         1
         *     /           \
         *    2             2
         * */

        TreeNode t5 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 1});
        TreeNode t6 = createBinaryTreeBreadthFirst(new Integer[]{1, 1, 2});
        log(isSameTree3(t5, t6));
        /*
         * expects false.（结构相同而值不同）
         *      1         1
         *     / \       / \
         *    2   1     1   2
         * */
    }
}
