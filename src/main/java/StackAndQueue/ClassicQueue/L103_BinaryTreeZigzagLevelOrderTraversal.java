package StackAndQueue.ClassicQueue;

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

    public static void main(String[] args) {
        TreeNode t = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, null, 15, 7});
        log(zigzagLevelOrder(t));   // expects [[3], [20,9], [15,7]]
        log(zigzagLevelOrder2(t));  // expects [[3], [20,9], [15,7]]
    }
}
