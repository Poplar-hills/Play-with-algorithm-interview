package BinaryTreeAndRecursion.ComplexRecursiveProblem;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import java.util.HashMap;
import java.util.Map;

import Utils.Helpers.TreeNode;

/*
 * Path Sum III
 *
 * - Given a binary tree, find the number of paths that sum to a given value.
 *
 * - Note:
 *   1. The path does not need to start or end at the root or a leaf, but it must go downwards (traveling
 *      only from parent nodes to child nodes).
 *   2. The tree has no more than 1,000 nodes and the values are in the range -1,000,000 to 1,000,000.
 * */

public class L437_PathSumIII {
    /*
     * 解法1：Recursion (DFS)
     * - 思路：在 L112_PathSum 和 L113_PathSumII 中，我们寻找符合条件的目标路径的方式是在递归过程中不断让 sum - node.val，
     *   这其实隐含了“节点 node 一定在目标路径上”的信息。而本题中目标路径不一定是 root-to-leaf 的 ∴ 有些节点（如根节点）不一
     *   定在目标路径上 ∴ 需要分情况讨论：
     *     1. 若 node 在目标路径上，则继续使用 L112、L113 中的方法，检查 sum - node.val 是否为0来确定目标路径；
     *     2. 若 node 不在目标路径上，递归地在 node 的子树中寻找目标路径。
     *   用公式表达：f(node, sum) = 包含 node 的目标路径数 + 不包含 node 的目标路径数
     *                          = f2(node, sum) + f(node.left, sum) + f(node.right, sum)。
     * - 💎 总结：该解法的实现采用2套递归来分别计算2种不同情况下的结果，最后加在一起返回。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int pathSum(TreeNode root, int sum) {
        if (root == null) return 0;
        return pathSumWithNode(root, sum)                           // 包含节点 root 的目标路径数
             + pathSum(root.left, sum) + pathSum(root.right, sum);  // 不包含节点 root 的目标路径数（排除 root，从其子节点开始递归）
    }

    private static int pathSumWithNode(TreeNode node, int sum) {
        if (node == null) return 0;
        int count = 0;
        if (node.val == sum) count++;  // 找到一条目标路径，但不能就此 return ∵ 后面的路径上可能有正、负数节点值，加在一起又等于 sum
        count += pathSumWithNode(node.left, sum - node.val);
        count += pathSumWithNode(node.right, sum - node.val);
        return count;
    }

    /*
     * 解法2：Recursion (DFS) + Map
     * - 思路：该题可以看做是 L560_SubarraySumEqualsK 的二叉树版，即本质上也是区间求和问题 ∴ 可以在二叉树上采用类似的思路，
     *   例如 test case 1 中 5->3 这条路径的和 = 10->5->3->3 这条路径的和 - 10 这条途径的和。根据这个思路，在对二叉树进行
     *   递归的过程中不断在 map 中记录 prefix sum（即根节点到当前节点的路径和），并检查 map 中是否存在 prefix sum 与目标值
     *   sum 的差，若存在则说明找到了目标路径，而找到的目标路径的条数即是 map 中 prefix sum 的频率（即 prefix sum 的出现次数）。
     * - 更多解释 SEE：https://leetcode.com/problems/path-sum-iii/discuss/91878/17-ms-O(n)-java-Prefix-sum-method
     *   中 kekezi 的评论。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int pathSum2(TreeNode root, int sum) {
        Map<Integer, Integer> map = new HashMap<>();  // 存储 <prefixSum, frequency>
        map.put(0, 1);
        return helper2(root, 0, sum, map);
    }

    private static int helper2(TreeNode root, int preSum, int sum, Map<Integer, Integer> map) {
        if (root == null) return 0;
        preSum += root.val;                                // 得到 prefix sum
        map.put(preSum, map.getOrDefault(preSum, 0) + 1);  // 在 map 中记录 prefix sum 或更新其频率

        int count = map.getOrDefault(preSum - sum, 0);  // 若 map 中存在 currSum-sum，说明找到一条目标路径
        count += helper2(root.left, preSum, sum, map) + helper2(root.right, preSum, sum, map);

        map.put(preSum, map.get(preSum) - 1);           // 或 map.merge(currSum, 1, (a, b) -> a - b)
        return count;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{10, 5, -3, 3, 2, null, 11, 3, -10, null, 1});
        log(pathSum0(t1, 8));
        /*
         * expects 4. (5->3, 5->2->1, -3->11, 10->5->3->-10)
         *         10
         *        /  \
         *       5   -3
         *      / \    \
         *     3   2   11
         *    / \   \
         *   3 -10   1
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{10, 8, -2});
        log(pathSum0(t2, 8));
        /*
         * expects 2. (8, 10->-2)
         *         10
         *        /  \
         *       8   -2
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{});
        log(pathSum0(t3, 0));
        /*
         * expects 0.
         * */
    }
}
