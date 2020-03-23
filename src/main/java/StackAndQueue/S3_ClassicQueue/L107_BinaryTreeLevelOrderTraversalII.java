package StackAndQueue.S3_ClassicQueue;

import java.util.*;

import static Utils.Helpers.*;

/*
 * Binary Tree Level Order Traversal II
 *
 * - Given a binary tree, return the bottom-up level order traversal of its nodes' values.
 *   (ie, from left to right, level by level from leaf to root).
 * */

public class L107_BinaryTreeLevelOrderTraversalII {
    /*
     * 基础1：自底向上的层序遍历
     * - 思路：自底向上的层序遍历 = reverse(自顶向下的层序遍历(先右后左))。
     * - 实现：用 Queue 进行广度优先遍历，另外再用一个 Stack 对结果进行 reverse。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<Integer> basicLevelOrderBottom(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        Queue<TreeNode> q = new LinkedList<>();
        Stack<TreeNode> stack = new Stack<>();
        q.offer(root);

        while (!q.isEmpty()) {
            TreeNode node = q.poll();
            stack.push(node);
            if (node.right != null) q.offer(node.right);  // 注意先访问 right 再访问 left，最后倒序输出的结果顺序才正确
            if (node.left != null) q.offer(node.left);
        }

        while (!stack.isEmpty())  // reverse
            res.add(stack.pop().val);

        return res;
    }

    /*
     * 基础2：自底向上的层序遍历（list 实现）。
     * - 思路：与解法1一致。
     * - 实现：即能为元素排队实现广度优先遍历，又能倒序输出 —— 这两个需求其实用 ArrayList 一种数据结构就可满足（∵ ArrayList
     *   可以作为 Queue、Stack 的底层实现 ∴ 自然具有它们两者的特性）。
     * - 时间复杂度 O(n)，空间复杂度 O(n)（空间复杂度比基础1降低一半）。
     * */
    public static List<Integer> basicLevelOrderBottom2(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        List<TreeNode> l = new ArrayList<>();
        l.add(root);

        for (int i = 0; i < l.size(); i++) {  // 一边遍历 list 一边往里添加元素（实际上基础1中的 Queue 也是一样）
            TreeNode node = l.get(i);         // 类似 queue 的出队操作
            if (node.right != null) l.add(node.right);  // 同样要先访问 right 再访问 left，最后倒序输出的结果顺序才正确
            if (node.left != null) l.add(node.left);
        }

        for (int i = l.size() - 1; i >= 0; i--)  // 倒序输出
            res.add(l.get(i).val);

        return res;
    }

    /*
     * 解法1：在基础2的基础上实现
     * - 思路：以 Pair 形式同时存储节点和节点的层级信息在 list 中（也可以抽象成单独的类），记录节点的层级的层级信息用于获取树的高度，
     *   树的高度用于得知该某一节点应该放在 res 的哪个列表里。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> levelOrderBottom(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        List<Pair<TreeNode, Integer>> l = new ArrayList<>();
        if (root == null) return res;

        l.add(new Pair<>(root, 0));
        for (int i = 0; i < l.size(); i++) {
            TreeNode node = l.get(i).getKey();
            int level = l.get(i).getValue();

            if (node.right != null)
                l.add(new Pair<>(node.right, level + 1));
            if (node.left != null)
                l.add(new Pair<>(node.left, level + 1));
            if (level == res.size())
                res.add(new ArrayList<>());
        }

        int levelCount = l.get(l.size() - 1).getValue();  // 通过节点的 level 信息获得二叉树高度（树的高度就是 res 中应有的列表个数）
        for (int i = l.size() - 1; i >= 0; i--) {
            TreeNode node = l.get(i).getKey();
            int level = l.get(i).getValue();
            res.get(levelCount - level).add(node.val);  // levelCount - level 得到该节点值应放入 res 中的哪个列表里
        }

        return res;
    }

    /*
     * 解法2：迭代2
     * - 思路：比解法1更聪明简单 —— 让 queue 每次入队一个层级的所有节点，并在一个 while 迭代中全部处理完，并入队下一个层级的所
     *   有节点（从而能在下个迭代中处理掉）。
     * - 优势：不再需要根据当前层级来判断是否需要创建新的层级列表，因此也不需要在队列中保存节点的层级信息，队列的 size 就是该层级
     *   需要处理的节点个数。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> levelOrderBottom2(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            List<Integer> levelList = new ArrayList<>();
            int size = q.size();              // 注意 size 不能 inline，否则 q.size() 每次取值会不同（因为循环体中会 offer）
            for (int i = 0; i < size; i++) {  // size = 该层级的节点个数（∵ 上个迭代出队了所有上个层级的所有节点，并入队了这个层级的所有节点）
                TreeNode node = q.poll();
                levelList.add(node.val);
                if (node.left != null) q.offer(node.left);
                if (node.right != null) q.offer(node.right);
            }
            res.add(0, levelList);      // 最后将该层列表添加到 res 头部（注意是头部）
        }

        return res;
    }

    /*
     * 解法3：递归 DFT
     * - 思路：类似 L102 的解法2，采用 DFT（深度优先遍历），但达到了 BFT 的效果。与 L102 的区别在于：
     *   1. 该解法通过后续遍历（先访问子节点再访问父节点）实现对二叉树的从下到上的遍历（后续遍历的特点就是从下到上遍历）；
     *   2. 在向 res 中添加空列表时要插入到 res 的头部，否则对于如 test case 2 的右倾的二叉树会出错。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高。
     * */
    public static List<List<Integer>> levelOrderBottom3(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        levelOrderBottom2(root, res, 0);
        return res;
    }

    private static void levelOrderBottom2(TreeNode node, List<List<Integer>> res, int level) {
        if (node == null) return;
        if (level == res.size())
            res.add(0, new ArrayList<>());       // 每次将空列表插入 res 头部
        levelOrderBottom2(node.left, res, level + 1);
        levelOrderBottom2(node.right, res, level + 1);
        res.get(res.size() - 1 - level).add(node.val);  // 递归到底之后再开始将节点值推入 res 中的对应列表（后续遍历）
    }

    /*
     * 解法4：递归 DFT + 最后 reverse
     * - 思路：与解法3大体相同，仍然是 DFT，区别在于递归结束后再统一 reverse，而非在每层递归中通过 res.get 找到应加入的列表，因此统计性能稍差于解法3。
     * - 时间复杂度 O(n*h)：其中遍历节点是 O(n)，而最后 reverse 是 O(n*h)（res 中有 h 个列表）；
     * - 空间复杂度 O(h)。
     * */
    public static List<List<Integer>> levelOrderBottom4(TreeNode root) {
        List<List<Integer>> res = new LinkedList<>();
        levelOrderBottom4(root, res, 0);
        Collections.reverse(res);  // 递归结束后需要再 reverse 一下
        return res;
    }

    private static void levelOrderBottom4(TreeNode node, List<List<Integer>> res, int level) {
        if (node == null) return;
        if (level == res.size())
            res.add(new LinkedList<>());
        levelOrderBottom4(node.left, res, level + 1);
        levelOrderBottom4(node.right, res, level + 1);
        res.get(level).add(node.val);  // 直接获取第 level 个列表，因此递归结束后得到的 res 是反着的
    }

    public static List<Integer> basicLevelOrderBottom0(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        Queue<TreeNode> q = new LinkedList<>();
        Stack<TreeNode> stack = new Stack<>();
        q.offer(root);

        while (!q.isEmpty()) {
            TreeNode node = q.poll();
            stack.push(node);
            if (node.right != null) q.offer(node.right);
            if (node.left != null) q.offer(node.left);
        }

        while (!stack.isEmpty())
            res.add(stack.pop().val);

        return res;
    }

    public static void helper0(TreeNode node, Queue<TreeNode> q) {

    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, 8, 15, 7, 1, 2});
        /*
         *            3
         *         /     \
         *        9      20
         *         \    /  \
         *          8  15   7
         *         / \
         *        1   2
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, null, 15, 7});
        /*
         *           3
         *         /   \
         *        9    20
         *            /  \
         *           15   7
         * */

        log(basicLevelOrderBottom0(t1));   // expects [1, 2, 8, 15, 7, 9, 20, 3]
        log(basicLevelOrderBottom2(t1));  // expects [1, 2, 8, 15, 7, 9, 20, 3]

        log(levelOrderBottom(t1));        // expects [[1,2], [8,15,7], [9,20], [3]]
        log(levelOrderBottom(t2));        // expects [[15,7], [9,20], [3]] (注意不是 [[9,15,7], [20], [3]])
    }
}
