package BinaryTreeAndRecursion.S5_LowestCommonAncestor;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.function.Consumer;

import Utils.Helpers.TreeNode;

/*
 * Kth Smallest Element in a BST
 *
 * - Given a binary search tree, write a function to find the kth smallest element in it.
 *
 * - Note: You may assume k is always valid, 1 ≤ k ≤ BST's total elements.
 *
 * - Follow up: What if the BST is modified (insert/delete operations) often and you need to find the kth
 *   smallest frequently? How would you optimize the kthSmallest routine?
 *   👉 Very good Analysis SEE: https://leetcode.com/problems/kth-smallest-element-in-a-bst/solution/
 * */

public class L230_KthSmallestElementInBST {
    /*
     * 解法1：DFS（In-order traversal）
     * - 思路：∵ BST 的中序遍历具有从小到大有序的性质 ∴ 只要返回中序遍历结果列表中的第 k 个元素即可。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int kthSmallest(TreeNode root, int k) {
        List<Integer> list = new ArrayList<>();
        inorder(root, list);
        return list.get(k - 1);
    }

    private static void inorder(TreeNode node, List<Integer> list) {
        if (node == null) return;
        inorder(node.left, list);
        list.add(node.val);
        inorder(node.right, list);
    }

    /*
     * 解法2：Recursion（解法1的性能优化版）
     * - 思路：解法1会先中序遍历所有节点，最后再取第 k 大的，而该解法则是在遍历到第 k 大的节点时就返回，不再继续遍历 ∴ 性能优于解法1。
     * - 实现：count 要实现为类成员变量。若实现为用 Integer 包装 count，并在函数中传递并不能达到传引用的效果 ∵ Integer 源码
     *   中 value 是 final 的，即一旦 Integer 对象创建之后其值就不能被修改 ∴ count++ 时只会创建一个新对象。
     * - 时间复杂度 O(h+k)，其中 h 是树高 ∵ 中序遍历在访问到第一个节点之前要先走到最左边的节点，该过程最大是 h 的时间，再加上
     *   访问找到第 k 大节点 ∴ 整体是 O(h+k)，对于平衡树是 O(logn+k)，对于完全不平衡树则是 O(n+k)。
     * - 空间复杂度 O(h)。
     * */
    private static int count;  // 类成员变量

    public static int kthSmallest2(TreeNode root, int k) {
        count = 0;
        return inorder2(root, k);
    }

    private static Integer inorder2(TreeNode node, int k) {
        if (node == null) return null;
        Integer res = inorder2(node.left, k);
        if (res != null) return res;        // 若在左子树中找到了结果，则直接返回（不再往下执行）
        if (++count == k) return node.val;  // 若在该节点就是第 k 大，则返回它
        return inorder2(node.right, k);     // 若左子树中没有找到，同时也不是该节点，则一定在右子树中
    }

    /*
     * 解法3：Iteration (DFS, In-order Traversal) (解法2的非递归实现)
     * - 时间复杂度 O(h+k)，解释同解法2；
     * - 空间复杂度 O(h+k) ∵ stack 中最多有 h+k 个节点。
     * */
    public static int kthSmallest3(TreeNode root, int k) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode curr = root;

        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
            TreeNode node = stack.pop();
            if (--k == 0) return node.val;
            curr = node.right;
        }

        throw new IllegalArgumentException("There's no kth node in this BST.");
    }

    /*
     * 解法4：BFS + Max heap
     * - 思路：类似解法 L215_KthLargestElementInArray 解法2，开辟 k+1 大小的最小堆，然后在遍历 BST 的过程中将所有大于第
     *    k 个节点的节点从堆中移出，最后留在堆顶的即是第 k 小的节点。
     * - 时间复杂度 O(nlogk)，空间复杂度 O(n)。
     * */
    public static int kthSmallest4(TreeNode root, int k) {
        PriorityQueue<TreeNode> pq = new PriorityQueue<>(k + 1, (a, b) -> b.val - a.val);  // max heap
        pq.offer(root);
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            TreeNode node = q.poll();

            Consumer<TreeNode> fn = n -> {
                q.offer(n);
                pq.offer(n);
                if (pq.size() == k + 1) pq.poll();
            };

            if (node.left != null) fn.accept(node.left);
            if (node.right != null) fn.accept(node.right);
        }

        return pq.poll().val;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{3, 1, 4, null, 2});
        log(kthSmallest3(t1, 1));
        /*
         * expects 1.
         *       3
         *      / \
         *     1   4
         *      \
         *       2
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{5, 3, 6, 2, 4, null, null, 1});
        log(kthSmallest3(t2, 3));
        /*
         * expects 3.
         *          5
         *         / \
         *        3   6
         *       / \
         *      2   4
         *     /
         *    1
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{1});
        log(kthSmallest3(t3, 1));
        /*
         * expects 1.
         * */
    }
}
