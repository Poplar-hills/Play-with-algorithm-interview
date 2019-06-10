package StackAndQueue.ClassicQueue;

import javafx.util.Pair;

import java.util.*;

import static Utils.Helpers.*;

/*
* Binary Tree Zigzag Level Order Traversal
*
* - Given a binary tree, return the zigzag level order traversal of its nodes' values. (ie, from left to right,
*   then right to left for the next level and alternate between).
* */

public class L103_BinaryTreeZigzagLevelOrderTraversal {
    /*
    * 解法1：递归 + 最后 reverse
    * - 时间复杂度 O(n*h)，其中遍历节点是 O(n)，而最后 reverse 是 O(n*h)（res 中有 h 个列表，每个列表最多有 n/2 个元素）；
    * - 空间复杂度 O(h)。
    * */
    public static List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        zigzagLevelOrder(root, res, 0);
        reverseOddLists(res);
        return res;
    }

    private static void zigzagLevelOrder(TreeNode node, List<List<Integer>> res, int level) {
        if (node == null) return;
        if (level == res.size()) res.add(new ArrayList<>());
        res.get(level).add(node.val);
        zigzagLevelOrder(node.left, res, level + 1);
        zigzagLevelOrder(node.right, res, level + 1);
    }

    private static void reverseOddLists(List<List<Integer>> res) {
        for (int i = 0; i < res.size(); i++)
            if (i % 2 == 1)
                Collections.reverse(res.get(i));
    }

    /*
    * 解法2：递归2
    * - 思路：在往每一层的列表中添加节点值时进行判断，如果该层是奇数层，则每次添加到列表头部，否则正常添加到列表尾部。
    * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高。
    * */
    public static List<List<Integer>> zigzagLevelOrder2(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        zigzagLevelOrder2(root, res, 0);
        return res;
    }

    private static void zigzagLevelOrder2(TreeNode node, List<List<Integer>> res, int level) {
        if (node == null) return;
        if (level == res.size()) res.add(new ArrayList<>());
        if (level % 2 == 1)
            res.get(level).add(0, node.val);
        else
            res.get(level).add(node.val);
        zigzagLevelOrder2(node.left, res, level + 1);
        zigzagLevelOrder2(node.right, res, level + 1);
    }

    /*
    * 解法3：迭代
    * - 思路：队列中以 Pair 形式同时保持节点和节点层级信息，在向层级列表 levelList 添加节点值时根据节点的层级进行判断，若是奇数层级
    *   则添加到列表头部，否则正常添加到尾部。
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static List<List<Integer>> zigzagLevelOrder3(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        Queue<Pair<TreeNode, Integer>> q = new LinkedList<>();
        if (root == null) return res;

        q.offer(new Pair<>(root, 0));
        while (!q.isEmpty()) {
            List<Integer> levelList = new ArrayList<>();
            int size = q.size();
            for (int i = 0; i < size; i++) {
                Pair<TreeNode, Integer> pair = q.poll();
                TreeNode node = pair.getKey();
                int level = pair.getValue();

                if (level % 2 == 1) levelList.add(0, node.val);
                else levelList.add(node.val);

                if (node.left != null) q.offer(new Pair<>(node.left, level + 1));
                if (node.right != null) q.offer(new Pair<>(node.right, level + 1));
            }
            res.add(levelList);
        }

        return res;
    }

    public static void main(String[] args) {
        TreeNode t = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, null, 15, 7});
        log(zigzagLevelOrder(t));   // expects [[3], [20,9], [15,7]]
        log(zigzagLevelOrder2(t));  // expects [[3], [20,9], [15,7]]
        log(zigzagLevelOrder3(t));  // expects [[3], [20,9], [15,7]]
    }
}
