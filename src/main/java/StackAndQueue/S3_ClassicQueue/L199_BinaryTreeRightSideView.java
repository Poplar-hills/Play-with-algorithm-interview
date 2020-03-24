package StackAndQueue.S3_ClassicQueue;

import java.util.*;

import static Utils.Helpers.*;

/*
 * Binary Tree Right Side View
 *
 * - Given a binary tree, imagine yourself standing on the right side of it, return the values of the nodes
 *   you can see ordered from top to bottom. For example:
 *
 *         1            <---                1            <---
 *       /   \                            /   \
 *      2     3         <---             2     3         <---
 *       \     \                          \
 *        5     4       <---               5             <---
 *   should return [1, 3, 4]          should return [1, 3, 5]
 * */

public class L199_BinaryTreeRightSideView {
    /*
     * 解法1：迭代
     * - 思路：该题实际上就是取每层的最后一个节点值 ∴ 可以采用 L107 解法3的形式进行 BFS，让 Queue 每次入队一个层级的所有节点，
     *   并在下一轮 while 循环中全部处理完，再入队下一层级的所有节点。唯一不同点是每层只将最后一个节点值加入 res。
     * - 时间复杂度 O(n)，空间复杂度 O(h)（因为队列中只存 h 个元素），其中 h 为树高。
     * */
    public static List<Integer> rightSideView(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            int size = q.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = q.poll();
                if (i == size - 1) res.add(node.val);  // 将该层的最后一个节点放入 res 中
                if (node.left != null) q.offer(node.left);
                if (node.right != null) q.offer(node.right);
            }
        }

        return res;
    }

    /*
     * 解法2：递归
     * - 思路：∵ 树的每一层要拿出一个节点放到 res 中的对应位置上（如第0层的节点放到 res[0] 上、第1层出一个节点放到 res[1] 上）。
     *   ∴ 在向 res 添加元素时，若对应位置上已有元素则用 List.set 进行替换，若没有则用 List.add 添加。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高。
     * */
    public static List<Integer> rightSideView2(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        helper2(root, res, 0);
        return res;
    }

    private static void helper2(TreeNode node, List<Integer> res, int level) {
        if (node == null) return;
        if (level == res.size()) res.add(node.val);
        else res.set(level, node.val);
        helper2(node.left, res, level + 1);
        helper2(node.right, res, level + 1);
    }

    /*
     * 解法3：递归（简化版）
     * - 思路：在解法2的基础上化简 —— 对于一个节点，若每次先遍历它的右子节点再遍历左子节点，则第一个访问到的节点值就是所需节点值。
     *   即对于每一层来说，只需向 res 中放入第一个访问到的节点值即可。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高。
     * */
    public static List<Integer> rightSideView3(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        helper3(root, res, 0);
        return res;
    }

    private static void helper3(TreeNode node, List<Integer> res, int level) {
        if (node == null) return;
        if (level == res.size()) res.add(node.val);
        helper3(node.right, res, level + 1);
        helper3(node.left, res, level + 1);
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, null, 5, null, 4});
        /*
         *         1
         *       /   \
         *      2     3
         *       \     \
         *        5     4
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, null, 5});
        /*
         *         1
         *       /   \
         *      2     3
         *       \
         *        5
         * */

        log(rightSideView2(t1));   // expects [1, 3, 4]
        log(rightSideView2(t2));   // expects [1, 3, 5]
    }
}
