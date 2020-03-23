package StackAndQueue.S3_ClassicQueue;

import java.util.*;

import Utils.Helpers.TreeNode;

import static Utils.Helpers.*;

/*
 * Binary Tree Zigzag Level Order Traversal
 *
 * - Given a binary tree, return the zigzag level order traversal of its nodes' values.
 *   (ie, from left to right, then right to left for the next level and alternate between).
 * */

public class L103_BinaryTreeZigzagLevelOrderTraversal {
    /*
     * 解法1：递归（最后 reverse）
     * - 思路：与 L102 的解法2一致，都是 DFS，只是最后要 reverse res 中的奇数层的列表。
     * - 时间复杂度 O(n*h)，其中遍历节点是 O(n)，而最后 reverse 是 O(n*h)（res 中有 h 个列表，每个列表最多有 n/2 个元素）；
     * - 空间复杂度 O(h)。
     * */
    public static List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        helper(root, res, 0);
        reverseOddLists(res);
        return res;
    }

    private static void helper(TreeNode node, List<List<Integer>> res, int level) {
        if (node == null) return;
        if (level == res.size())
            res.add(new ArrayList<>());
        res.get(level).add(node.val);
        helper(node.left, res, level + 1);
        helper(node.right, res, level + 1);
    }

    private static void reverseOddLists(List<List<Integer>> res) {
        for (int i = 0; i < res.size(); i++)
            if (i % 2 == 1)
                Collections.reverse(res.get(i));
    }

    /*
     * 解法2：迭代（BFS）
     * - 思路：在 L102 解法1的基础上加入对层级数的奇偶判断，若是偶数层则下一层先遍历右子节点再遍历左子节点，若为奇数层则反过来。
     * - 实现：另一种实现是，将对层级数的奇偶判断放在往 res 相应的 list 中插入元素的时候，若为偶数层，则向 list 尾部追加，若
     *   为奇数层则往 list 头部插入（SEE 解法2）。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> zigzagLevelOrder2(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        Queue<Pair<TreeNode, Integer>> q = new LinkedList<>();
        q.offer(new Pair<>(root, 0));

        while (!q.isEmpty()) {
            Pair<TreeNode, Integer> p = q.poll();
            TreeNode node = p.getKey();
            int level = p.getValue();

            if (level == res.size())
                res.add(new ArrayList<>());
            res.get(level).add(node.val);

            if (level % 2 == 0) {  // 先右后左
                if (node.right != null) q.offer(new Pair<>(node.right, level + 1));
                if (node.left != null) q.offer(new Pair<>(node.left, level + 1));
            } else {               // 先左后右
                if (node.left != null) q.offer(new Pair<>(node.left, level + 1));
                if (node.right != null) q.offer(new Pair<>(node.right, level + 1));
            }
        }

        return res;
    }

    /*
     * 解法3：递归
     * - 思路：与解法1一致。
     * - 实现：1. 采用递归；
     *        2. 采用解法1实现中描述的实现，将对层级数的奇偶判断放在往 res 相应的 list 中插入元素的时候。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高。
     * */
    public static List<List<Integer>> zigzagLevelOrder3(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        helper3(root, res, 0);
        return res;
    }

    private static void helper3(TreeNode node, List<List<Integer>> res, int level) {
        if (node == null) return;
        if (level == res.size())
            res.add(new ArrayList<>());
        if (level % 2 == 0)
            res.get(level).add(node.val);
        else
            res.get(level).add(0, node.val);
        helper3(node.left, res, level + 1);
        helper3(node.right, res, level + 1);
    }

    /*
     * 解法4：迭代（层级列表）
     * - 思路：类似 L107 的解法3。
     * - 实现：类似解法3。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> zigzagLevelOrder4(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        Queue<Pair<TreeNode, Integer>> q = new LinkedList<>();
        q.offer(new Pair<>(root, 0));

        while (!q.isEmpty()) {
            List<Integer> levelList = new ArrayList<>();  // 每轮循环都将该层级的列表生成完后再进入下一轮
            int size = q.size();
            for (int i = 0; i < size; i++) {              // 当前层级中的元素个数 == 上一轮循环中往 q 中添加的元素个数
                Pair<TreeNode, Integer> pair = q.poll();
                TreeNode node = pair.getKey();
                int level = pair.getValue();

                if (level % 2 == 0)
                    levelList.add(node.val);
                else
                    levelList.add(0, node.val);

                if (node.left != null) q.offer(new Pair<>(node.left, level + 1));
                if (node.right != null) q.offer(new Pair<>(node.right, level + 1));
            }
            res.add(levelList);
        }

        return res;
    }

    /*
     * 解法5：迭代（层级列表 + reverse）
     * - 思路：与解法4类似，区别在于不在队列中保持节点的层级信息，而是在层级列表 levelList 生成完之后判断该层是否需要 reverse，
     *   判断的依据就是当前迭代到了第几层，这就需要在每次创建 levelList 时进行计数。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> zigzagLevelOrder5(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        int levelCount = 0;
        while (!q.isEmpty()) {
            List<Integer> levelList = new ArrayList<>();
            int size = q.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = q.poll();
                levelList.add(node.val);
                if (node.left != null) q.offer(node.left);
                if (node.right != null) q.offer(node.right);
            }

            if (levelCount % 2 == 1)  // levelList 创建完后判断当前在第几层，若是奇数层则 reverse
                Collections.reverse(levelList);

            res.add(levelList);
            levelCount++;
        }

        return res;
    }

	public static void main(String[] args) {
        TreeNode t = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, null, 15, 7});
        /*
         *           3
         *         /   \
         *        9    20
         *            /  \
         *           15   7
         * */

        log(zigzagLevelOrder(t));    // expects [[3], [20,9], [15,7]]
        log(zigzagLevelOrder2(t));   // expects [[3], [20,9], [15,7]]
        log(zigzagLevelOrder3(t));   // expects [[3], [20,9], [15,7]]
        log(zigzagLevelOrder4(t));   // expects [[3], [20,9], [15,7]]
        log(zigzagLevelOrder5(t));   // expects [[3], [20,9], [15,7]]
    }
}
