package DP.StateTransition;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javafx.util.Pair;

/*
* House Robber III
*
* - 基本与 L198_HouseRobber 中的条件一样，只是本题中的街道为一棵二叉树，问在该街道中，在不触发警报的情况下，最多能盗取多少财产。
*   其本质上是在问如何在二叉树上选择不相邻的节点，使节点值之和最大。
* */

public class L337_HouseRobberIII {
    /*
    * 解法1：
    * - 思路：
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static int rob(TreeNode root) {
        if (root == null) return 0;
        List<Integer> rowSums = calcRowSums(root);
        return calcMaxRobbedMoney(rowSums);
    }

    public static List<Integer> calcRowSums(TreeNode root) {
        List<Integer> res = new ArrayList<>();   // list 中保存每行节点的节点值之和
        Queue<Pair<TreeNode, Integer>> q = new LinkedList<>();
        q.offer(new Pair<>(root, 0));

        while (!q.isEmpty()) {
            Pair<TreeNode, Integer> p = q.poll();
            TreeNode node = p.getKey();
            int level = p.getValue();

            if (level == res.size()) res.add(0);
            res.set(level, res.get(level) + node.val);

            if (node.left != null)
                q.offer(new Pair<>(node.left, level + 1));
            if (node.right != null)
                q.offer(new Pair<>(node.right, level + 1));
        }

        return res;
    }

    private static int calcMaxRobbedMoney(List<Integer> rowSums) {
        if (rowSums == null || rowSums.size() == 0) return 0;
        int n = rowSums.size();
        if (n == 1) return rowSums.get(0);
        if (n == 2) return Math.max(rowSums.get(0), rowSums.get(1));

        int[] dp = new int[n];
        for (int i = 2; i < dp.length; i++)
            dp[i] = Math.max(dp[i], rowSums.get(i) + dp[i - 2]);

        return dp[n - 1];
    }

    public static void main(String[] args) {
        /*
        *      3
        *     / \
        *    2   2
        *     \   \
        *      3   1
        * */
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{3, 2, 2, null, 3, null, 1});
        log(rob(t1));  // expects 7. (3 + 3 + 1)

        /*
        *        1
        *       / \
        *      5   5
        *     / \   \
        *    1   1   1
        * */
        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 5, 5, 1, 1, null, 1});
        log(rob(t2));  // expects 10. (5 + 5)

        /*
        *        1
        *       / \
        *      1   5
        *     / \   \
        *    5   5   1
        * */
        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{1, 1, 5, 5, 5, null, 1});
        log(rob(t3));  // expects 15. (5 + 5 + 5)

        /*
        *          4
        *         /
        *        1
        *       /
        *      1
        *     /
        *    4
        * */
        TreeNode t4 = createBinaryTreeBreadthFirst(new Integer[]{4, 1, null, 1, null, 4});
        log(rob(t4));  // expects 8. (4 + 4)
    }
}
