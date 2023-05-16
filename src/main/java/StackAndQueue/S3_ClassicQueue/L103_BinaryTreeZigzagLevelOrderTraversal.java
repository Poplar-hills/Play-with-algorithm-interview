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
     * - 👉🏻 时间复杂度 O(n*h)，其中遍历节点是 O(n)，而最后 reverse 是 O(h*n/2)（res 中有 h 个列表，每个列表最多有 n/2 个元素）；
     * - 空间复杂度 O(h)。
     * */
    public static List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        dfs(root, res, 0);
        reverseOddLists(res);
        return res;
    }

    private static void dfs(TreeNode node, List<List<Integer>> res, int level) {
        if (node == null) return;
        if (level == res.size())
            res.add(new ArrayList<>());
        res.get(level).add(node.val);
        dfs(node.left, res, level + 1);
        dfs(node.right, res, level + 1);
    }

    private static void reverseOddLists(List<List<Integer>> res) {
        for (int i = 0; i < res.size(); i++)
            if (i % 2 == 1)
                Collections.reverse(res.get(i));
    }

    /*
     * 解法2：迭代（BFS）
     * - 思路：不同于解法1，更高效的做法是在 L102 解法1的基础上加入对层级数的奇偶判断，若为偶数层则倒序输出节点。
     * - 实现：要在偶数层则倒序输出节点有2种方式：
     *   1. 改变子节点入队顺序：先入队右子节点，再入队左子节点，从而在之后的遍历中实现从右到左遍历的效果（本解法）；
     *   2. 头部追加节点：往 res 相应层级的 list 中追加节点时，追加到 list 头部（如解法3）；
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
     *        2. 采用解法2"实现"中描述的方法2，往 res 相应层级的 list 中追加节点时，追加到 list 头部。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高。
     * */
    public static List<List<Integer>> zigzagLevelOrder3(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        dfs3(root, 0, res);
        return res;
    }

    private static void dfs3(TreeNode node, int level, List<List<Integer>> res) {
        if (node == null) return;
        if (level == res.size())
            res.add(new ArrayList<>());
        if (level % 2 == 0)
            res.get(level).add(node.val);
        else
            res.get(level).add(0, node.val);
        dfs3(node.left, level + 1, res);
        dfs3(node.right, level + 1, res);
    }

    /*
     * 解法4：迭代（层级列表）
     * - 思路：类似 L107 的解法3，让 Queue 每次入队一个层级的所有节点，并在下一轮 while 循环中全部处理完，并再入队下一层级的
     *   所有节点。这种方式的聪明之处在于，不再需要根据当前层级来判断是否需要创建新的层级列表。
     * - 实现：类似解法3，往层级列表中追加节点时，追加到列表头部。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> zigzagLevelOrder4(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        Queue<Pair<TreeNode, Integer>> q = new LinkedList<>();
        q.offer(new Pair<>(root, 0));

        while (!q.isEmpty()) {
            List<Integer> levelList = new ArrayList<>();  // 每轮循环都将该层级的列表生成完后再进入下一轮
            for (int i = 0, size = q.size(); i < size; i++) {  // 当前层级中的元素个数 == 上一轮循环中往 q 中添加的元素个数
                Pair<TreeNode, Integer> pair = q.poll();
                TreeNode node = pair.getKey();
                int level = pair.getValue();

                if (level % 2 == 0)
                    levelList.add(node.val);
                else
                    levelList.add(0, node.val);  // 若 level 为奇数，则插入 levelList 头部

                if (node.left != null) q.offer(new Pair<>(node.left, level + 1));
                if (node.right != null) q.offer(new Pair<>(node.right, level + 1));
            }
            res.add(levelList);
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
    }
}
