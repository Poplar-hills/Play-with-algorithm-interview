package StackAndQueue.S3_ClassicQueue;

import java.util.*;

import static Utils.Helpers.*;

/*
 * Binary Tree Level Order Traversal II
 *
 * - Given a binary tree, return the bottom-up level order traversal of its nodes' values.
 *   (ie, from left to right, level by level from leaf to root).
 *
 * - 👉 前中后序遍历都是 DFS，层序遍历是 BFS。
 * */

public class L107_BinaryTreeLevelOrderTraversalII {
    /*
     * 基础1：自底向上的层序遍历
     * - 思路：观察结果可知，自底向上的层序遍历 = reverse(先访问右子树再访问左子树的自顶向下的 BFS) ∴ 需要可以在自顶向下的
     *   层序遍历基础上改造，满足：
     *     1. 先访问右子树再访问左子树；
     *     2. 对遍历结果进行 reverse。
     * - 实现：
     *     1. 用 Queue 进行 BFS，另外再用一个 Stack 对结果进行 reverse（👉 提起 reverse 就要想起 Stack）。
     *     2. 也可以不使用 Stack，直接放入 res，而在最后 Collections.reverse(res) 即可。
     * - 时间复杂度 O(2n)，空间复杂度 O(n)。
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
     * 基础2：自底向上的层序遍历（ArrayList 版）
     * - 思路：与解法1一致。
     * - 实现：既能为元素排队实现广度优先遍历，又能倒序输出 —— 这两个需求其实用 ArrayList 一种数据结构就可满足（∵ ArrayList
     *   可以作为 Queue、Stack 的底层实现 ∴ 自然具有它们两者的特性）。
     * - 时间复杂度 O(2n)，空间复杂度 O(n)（空间复杂度比基础1降低一半）。
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
     * 解法1：迭代 + Stack
     * - 思路：在基础1 Stack 的思路上对每层内的节点进行分组（类似 L102 解法1的分组逻辑）。
     * - 实现：
     *   - Queue 用于 BFS，但 ∵ 需要分组 ∴ Queue 中的每个节点要带上 level 信息；
     *   - Stack 只用于对结果进行 reverse ∴ 在 reverse 之前 Stack 中存储的应是分好组的节点，即 Stack 中的数据类型是 List<Integer>。
     * - 时间复杂度 O(2n)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> levelOrderBottom(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        Stack<List<Integer>> stack = new Stack<>();
        Queue<Pair<TreeNode, Integer>> q = new LinkedList<>();
        q.offer(new Pair<>(root, 0));

        while (!q.isEmpty()) {
            Pair<TreeNode, Integer> pair = q.poll();
            TreeNode node = pair.getKey();
            int level = pair.getValue();

            if (stack.size() == level)
                stack.push(new ArrayList<>());  // 往 stack 中添加分组（节点在 stack 中的存储形式是一棵倒置的树）
            stack.get(level).add(node.val);

            if (node.left != null) q.offer(new Pair<>(node.left, level + 1));  // 注意这里要先 left 再 right（∵ 分组内的节点顺序和树上的顺序一致）
            if (node.right != null) q.offer(new Pair<>(node.right, level + 1));
        }

        while (!stack.isEmpty())
            res.add(stack.pop());  // 或先 Collections.reverse(stack) + res.addAll(stack);（当然这样就无需使用 Stack 了）

        return res;
    }

    /*
     * 解法2：迭代2
     * - 思路：在 BFS 树上节点时，让每个节点带上层级信息以 Pair 的形式存在 List 中。同时根据层级信息先往结果集中插入 h
     *   个空列表，当遍历完所有节点后即可得到树的高度 h，这样就能为每个 Pair 中的节点计算出应该放到结果集中的哪个列表里了。
     * - 时间复杂度 O(2n)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> levelOrderBottom2(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        List<Pair<TreeNode, Integer>> l = new ArrayList<>();
        l.add(new Pair<>(root, 0));

        for (int i = 0; i < l.size(); i++) {
            TreeNode node = l.get(i).getKey();
            int level = l.get(i).getValue();

            if (level == res.size())
                res.add(new ArrayList<>());  // 往 res 中添加空列表
            if (node.left != null)
                l.add(new Pair<>(node.left, level + 1));  // 遍历子节点时先 left 再 right（这样在下面插入对应分组时的顺序才是对的）
            if (node.right != null)
                l.add(new Pair<>(node.right, level + 1));
        }

        int h = l.get(l.size() - 1).getValue();  // 通过节点的 level 信息获得二叉树高度（树的高度就是 res 中应有的列表个数）
        for (int i = 0; i < l.size(); i++) {
            TreeNode node = l.get(i).getKey();
            int level = l.get(i).getValue();
            res.get(h - level).add(node.val);  // h - level 得到该节点值应放入 res 中的哪个列表里
        }

        return res;
    }

    /*
     * 解法3：迭代3
     * - 思路：比解法1、2更聪明简单 —— 让 Queue 每次入队一个层级的所有节点，并在下一轮 while 循环中全部处理完，并再入队下一
     *   层级的所有节点。这种方式的聪明之处在于，不再需要根据当前层级来判断是否需要创建新的层级列表 ∴ 也不需要在队列中保存节点的
     *   层级信息，队列的 size 就是该层级需要处理的节点个数。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> levelOrderBottom3(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        Queue<TreeNode> q = new LinkedList<>();  // queue 里无需带上层级信息
        q.offer(root);

        while (!q.isEmpty()) {
            List<Integer> levelList = new ArrayList<>();  // 创建临时的层级列表
            for (int i = 0, size = q.size(); i < size; i++) {  // 遍历该层的所有节点（此时 q 中的就是这一层的所有节点）
                TreeNode node = q.poll();
                levelList.add(node.val);
                if (node.left != null) q.offer(node.left);
                if (node.right != null) q.offer(node.right);
            }
            res.add(0, levelList);  // 最后将该层列表添加到 res 头部
        }

        return res;
    }

    /*
     * 解法4：迭代4
     * - 思路：在正常 BFS 遍历基础上做两个修改：
     *   1. 创建新列表时，总是插入 res 头部；
     *   2. 访问节点时，通过索引计算（res.size() - 1 - level），将节点值放入对应列表中（若 level=0，则放入最后一个列表，若
     *      level=1，则放入倒数第二个列表）。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> levelOrderBottom4(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        Queue<Pair<TreeNode, Integer>> q = new LinkedList<>();
        q.offer(new Pair<>(root, 0));

        while (!q.isEmpty()) {
            Pair<TreeNode, Integer> p = q.poll();
            TreeNode node = p.getKey();
            int level = p.getValue();

            if (res.size() == level)
                res.add(0, new ArrayList<>());  // 创建新列表时，总是插入 res 头部

            res.get(res.size() - 1 - level).add(node.val);  // 访问节点时，将节点值放入对应列表中
            if (node.left != null) q.offer(new Pair<>(node.left, level + 1));
            if (node.right != null) q.offer(new Pair<>(node.right, level + 1));
        }

        return res;
    }

    /*
     * 解法5：解法4的 DFS 递归版
     * - 💎 思路：类似 L102 的解法2，同样采用 DFS 来实现 BFS 的效果 ∵ 在递归中传递了 level 信息 ∴ 在访问节点时可直接将其 add
     *   到第 level 个列表中，与是 BFS 还是 DFS 遍历无关。
     * - 实现：
     *   1. 💎 本实现采用前序遍历（先访问父节点，后访问左右子节点），但实际上 ∵ 只要在访问节点时找到对应的列表插入 ∴ 遍历顺序
     *      无关紧要，使用后续遍历（在两个 helper(...) 之后再访问节点）同样可行；
     *   2. 注意在往 res 中插入空列表时要插入到 res 的头部，否则 test case 2 的右倾的二叉树会出错（当左侧递归已完成时，右侧递归
     *      的最底层节点需要插入到 res 的头部才行）。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高。
     * */
    public static List<List<Integer>> levelOrderBottom5(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        helper4(root, 0, res);
        return res;
    }

    private static void helper4(TreeNode node, int level, List<List<Integer>> res) {
        if (node == null) return;
        if (level == res.size())
            res.add(0, new ArrayList<>());  // 在递归去程时向 res 中插入空列表（注意要插入在 res 的头部）
        res.get(res.size() - 1 - level).add(node.val);  // 并将节点值推入 res 中的对应列表里
        helper4(node.left, level + 1, res);
        helper4(node.right, level + 1, res);
    }

    /*
     * 解法6：递归 + reverse
     * - 思路：与解法3类似，仍然是 DFS。
     * - 实现：区别在于：
     *   1. 递归结束后再统一 reverse，而非在每层递归中通过 res.get 找到应加入的列表 ∴ 统计性能稍差于解法3；
     *   2. 本解法中采用后续遍历，节点访问放在两个 helper(...) 之后。
     * - 时间复杂度 O(n+h)：其中遍历节点是 O(n)，而最后 reverse 是 O(h)（res 中有 h 个列表）；
     * - 空间复杂度 O(h)。
     * */
    public static List<List<Integer>> levelOrderBottom6(TreeNode root) {
        List<List<Integer>> res = new LinkedList<>();
        helper5(root, 0, res);
        Collections.reverse(res);  // 递归结束后需要再 reverse 一下
        return res;
    }

    private static void helper5(TreeNode node, int level, List<List<Integer>> res) {
        if (node == null) return;
        if (level == res.size())
            res.add(new LinkedList<>());
        helper5(node.left, level + 1, res);
        helper5(node.right, level + 1, res);
        res.get(level).add(node.val);   // 直接获取第 level 个列表，因此递归结束后得到的 res 是反着的
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, 8, 15, 7, 1, 2});
        /*
         *             3
         *         /       \
         *        9        20
         *         \      /  \
         *          8    15   7
         *         / \
         *        1   2
         * */

        log(basicLevelOrderBottom(t1));   // expects [1, 2, 8, 15, 7, 9, 20, 3]
        log(basicLevelOrderBottom2(t1));  // expects [1, 2, 8, 15, 7, 9, 20, 3]
        log(levelOrderBottom5(t1));       // expects [[1,2], [8,15,7], [9,20], [3]]

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, null, 15, 7});
        /*
         *           3
         *         /   \
         *        9    20
         *            /  \
         *           15   7
         * */

        log(levelOrderBottom5(t2));  // expects [[15,7], [9,20], [3]] (注意不能是 [[9,15,7], [20], [3]])
    }
}
