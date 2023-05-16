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
     *     👉 ∵ ArrayList 可以作为 Queue、Stack 的底层实现 ∴ 所有使用 Stack 的场景都也可以使用 ArrayList 代替，
     *        只需倒序被遍历即可。
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
     * - 注意：∵ 要获取树高 ∴ 不能使用 Queue 那种入队、出队的方式（∵ 一旦出队就没法得到树高） ∴ 只能使用 ArrayList，
     *   并在其中保存所有元素。
     * - 时间复杂度 O(2n)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> levelOrderBottom2(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        List<Pair<TreeNode, Integer>> l = new ArrayList<>();  // 起到 Queue 的作用，但又不出队任何元素（这样最后才能获得树高）
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

        int h = l.get(l.size() - 1).getValue();  // 通过节点的 level 信息获得树高（树的高度就是 res 中应有的列表个数）
        for (Pair<TreeNode, Integer> p : l) {
            TreeNode node = p.getKey();
            int level = p.getValue();
            res.get(h - level).add(node.val);  // h - level 得到该节点值应放入 res 中的哪个列表里
        }

        return res;
    }

    /*
     * 解法3：迭代3（迭代中的最优解🥇）
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
     *   1. 创建新列表时，总是插入到 res 的头部；
     *   2. 访问节点时，通过计算索引（res.size() - 1 - level），总是将节点值放入当前 res 中的第0个列表里（若 level=0，
     *      则此时 res.size() 应为1；若 level=1，则此时 res.size() 应为2；...）。
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

            res.get(res.size() - 1 - level).add(node.val);  // 访问节点时，总是将节点值插入 res.get(0) 中
            if (node.left != null) q.offer(new Pair<>(node.left, level + 1));
            if (node.right != null) q.offer(new Pair<>(node.right, level + 1));
        }

        return res;
    }

    /*
     * 解法5：解法4的 DFS 递归版（递归中的最优解🥇）
     * - 思路：类似 L102 的解法2，同样采用 DFS 来实现 BFS 的效果 ∵ 在递归中传递了 level 信息 ∴ 在访问节点时可直接将其 add
     *   到第 level 个列表中，与是 BFS 还是 DFS 遍历无关。
     * - 💎 实现：
     *   1. 本实现采用前序遍历（先访问父节点，后访问左右子节点），但实际上 ∵ 只要在访问节点时找到对应的列表插入 ∴ 遍历顺序无关紧要，
     *      使用后续遍历（在两个 dfs5(...) 之后再访问节点）同样可行；
     *   2. 注意在往 res 中插入空列表时要插入到 res 的头部，否则 test case 2 的右倾的二叉树会出错（当左侧递归已完成时，右侧递归
     *      的最底层节点需要插入到 res 的头部才行）。
     *   3. ∵ 每次都将空列表插入到 res 的头部 ∴ 每次在访问节点时就要把节点值插入到 res 的第0个列表中。而 res.size() - 1 - level
     *      即可以达成该需求。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高。
     * */
    public static List<List<Integer>> levelOrderBottom5(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        dfs5(root, 0, res);
        return res;
    }

    private static void dfs5(TreeNode node, int level, List<List<Integer>> res) {
        if (node == null) return;
        if (level == res.size())
            res.add(0, new ArrayList<>());  // 在递归去程时向 res 中插入空列表（注意要插入在 res 的头部）
        res.get(res.size() - 1 - level).add(node.val);  // 并将节点值推入 res 中的对应列表里
        dfs5(node.left, level + 1, res);
        dfs5(node.right, level + 1, res);
    }

    /*
     * 解法6：递归 + reverse（最 intuitive 解）
     * - 思路：与解法3类似，仍然是 DFS。
     * - 实现：区别在于：
     *   1. 递归结束后再统一 reverse，而非在每层递归中通过 res.get 找到应加入的列表 ∴ 性能差于解法3；
     *   2. 本解法中采用后续遍历，在两个 dfs6(...) 之后访问节点。
     * - 时间复杂度 O(n+h)：其中遍历节点是 O(n)，而最后 reverse 是 O(h)（res 中有 h 个列表）；
     * - 空间复杂度 O(h)。
     * */
    public static List<List<Integer>> levelOrderBottom6(TreeNode root) {
        List<List<Integer>> res = new LinkedList<>();
        dfs6(root, 0, res);
        Collections.reverse(res);  // 递归结束后需要再 reverse 一下
        return res;
    }

    private static void dfs6(TreeNode node, int level, List<List<Integer>> res) {
        if (node == null) return;
        if (level == res.size())
            res.add(new LinkedList<>());
        dfs6(node.left, level + 1, res);
        dfs6(node.right, level + 1, res);
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
        log(levelOrderBottom2(t1));       // expects [[1,2], [8,15,7], [9,20], [3]]

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, null, 15, 7});
        /*
         *           3
         *         /   \
         *        9    20
         *            /  \
         *           15   7
         * */

        log(levelOrderBottom2(t2));  // expects [[15,7], [9,20], [3]] (注意不能是 [[9,15,7], [20], [3]])
    }
}
