package BinaryTreeAndRecursion.S3_DefineRecursiveProblem;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.*;

import java.util.*;
import java.util.stream.Collectors;

import Utils.Helpers.TreeNode;

/*
 * Sum Root to Leaf Numbers
 *
 * - Given a binary tree containing digits from 0-9 only, each root-to-leaf path could represent a number.
 *   For example the root-to-leaf path 1->2->3 represents the number 123. Find the total sum of all
 *   root-to-leaf numbers.
 * */

public class L129_SumRootToLeafNumbers {
    /*
     * 解法1：DFS (Recursion)
     * - 思路：从下到上，在每个节点上拼接字符串，返回该节点下所有分支的路径的节点值字符串：
     *           4               ['495','491','40']    -->  495 + 491 + 40
     *          / \                 /         \
     *         9   0    -->   ['95','91']    ['0']
     *        / \               /    \
     *       5   1          ['5']   ['1']
     * - 👉 实现：列表中之所以存的是 String 而非 Integer 是 ∵ 若存 Integer 则每层递归都需要根据下层返回的结果对列表中的数字
     *   乘以 10、100、1000... 而在 java 中获取一个 Integer 位数的最简单方式就是转化为字符串：String.valueOf(int).length，
     *   ∴ 不如一开始就直接存 String。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * - 注：最后在用 stream 求字符串之和时会遍历列表 ∵ 列表最大长度 = 完美二叉树分支数 = 完美二叉树最底层节点数 = 节点数/2
     *   ∴ 遍历列表的时间复杂度为 O(n/2)。
     * */
    public static int sumNumbers(TreeNode root) {
        if (root == null) return 0;
        return dfs(root).stream()        // 用 stream 求字符串之和
                .map(Integer::parseInt)  // 另一种写法：.mapToInt(Integer::parseInt).sum();
                .reduce(0, Integer::sum);
    }

    private static List<String> dfs(TreeNode root) {
        if (root.left == null && root.right == null)
            return List.of(String.valueOf(root.val));

        List<String> nums = new ArrayList<>();
        if (root.left != null) nums.addAll(dfs(root.left));
        if (root.right != null) nums.addAll(dfs(root.right));

        return nums.stream()             // 拼接两个字符串列表
                .map(s -> root.val + s)  // 给每个字符串前面拼接当前节点值
                .collect(Collectors.toList());
    }

    /*
     * 解法2：DFS (Recursion)
     * - 思路：从根节点开始从上到下逐层累积当前 path 的 sum，当到达叶子节点时 add 到 res 中，最后返回根节点后再求和。
     *            4                    f(4,0,[])                      [495,491,40]
     *           / \                  ↙        ↘                       ↗      ↖
     *          9   0    --->    f(9,4,[])   f(0,4,[])   --->    [495,491]    [40]
     *         / \               ↙       ↘                       ↗     ↖
     *        5   1        f(5,49,[])  f(1,49,[])            [495]    [491]
     *
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int sumNumbers2(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        dfs2(root, 0, res);
        return res.stream().reduce(Integer::sum).get();  // or res.stream().mapToInt(Integer::intValue).sum();
    }

    private static void dfs2(TreeNode root, int sum, List<Integer> res) {
        if (root == null) return;
        int newSum = sum * 10 + root.val;
        if (root.left == null && root.right == null) {
            res.add(newSum);
            return;
        }
        dfs2(root.left, newSum, res);
        dfs2(root.right, newSum, res);
    }

    /*
     * 解法3：DFS (Recursion) (解法2的简化版，🥇最优解)
     * - 思路：与解法2一致。
     * - 实现：不使用 res 存储找到的解，而是让每个递归函数 f(root, sum) 都返回以 sum 为基数、以 root 为根的二叉树的
     *   sum of root to leaf numbers，即每个递归函数是一个完整的子问题，从而最终从下到上递推出原问题的解：
     *            4                  f(4,0)                    1026
     *           / \                 ↙    ↘                   ↗    ↖
     *          9   0    --->    f(9,4)  f(0,4)    --->     986     40
     *         / \               ↙    ↘                    ↗   ↖
     *        5   1         f(5,49)  f(1,49)            495    491
     *
     * - 💎 总结：相比解法2，该解法更加函数式：
     *   1. ∵ 没有外部遍历 ∴ 无副作用；
     *   2. ∵ 递归函数的语义中描述了返回值 ∴ 每个递归函数都是原问题的一个完整的子问题，是“真递归”。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int sumNumbers3(TreeNode root) {
        return dfs3(root, 0);
    }

    private static int dfs3(TreeNode root, int sum) {  // 返回以 sum 为基数、以 root 为根的二叉树的 root-to-leaf numbers 之和
        if (root == null) return 0;
        int newSum = sum * 10 + root.val;
        if (root.left == null && root.right == null) return newSum;
        return dfs3(root.left, newSum) + dfs3(root.right, newSum);
    }

    /*
     * 解法4：DFS (Iteration)
     * - 思路：与解法2、3一致，都是将当前路径的 base 带在每个节点上，一层层往下传递。
     * - 实现：只需将 Stack 替换为 Queue 就得到了 BFS 解法。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int sumNumbers4(TreeNode root) {
        if (root == null) return 0;
        int sum = 0;
        Stack<Pair<TreeNode, Integer>> stack = new Stack<>();  // 存储 <节点, 当前节点的 pathSum>
        stack.push(new Pair<>(root, 0));

        while (!stack.isEmpty()) {
            Pair<TreeNode, Integer> p = stack.pop();
            TreeNode node = p.getKey();
            int newSum = p.getValue() * 10 + node.val;

            if (node.left == null && node.right == null)
                sum += newSum;
            if (node.left != null)
                stack.push(new Pair<>(node.left, newSum));
            if (node.right != null)
                stack.push(new Pair<>(node.right, newSum));
        }

        return sum;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3});
        log(sumNumbers(t1));
        /*
         * expects 25. (12 + 13)
         *        1
         *       / \
         *      2   3
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{4, 9, 0, 5, 1});
        log(sumNumbers(t2));
        /*
         * expects 1026. (495 + 491 + 40)
         *        4
         *       / \
         *      9   0
         *     / \
         *    5   1
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{5, 3, 2, 7, 0, 6, null, null, null, 0});
        log(sumNumbers(t3));
        /*
         * expects 6363. (537 + 5300 + 526)
         *           5
         *       /      \
         *      3       2
         *     / \     /
         *    7   0   6
         *       /
         *      0
         * */
    }
}
