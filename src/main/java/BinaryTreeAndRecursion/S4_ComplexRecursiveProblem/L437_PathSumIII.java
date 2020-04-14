package BinaryTreeAndRecursion.S4_ComplexRecursiveProblem;

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
     * 解法1：DFS + Double recursion
     * - 思路：L112_PathSum、L113_PathSumII 中寻找符合条件的目标路径的方式是在递归过程中不断让 sum - node.val，这其实隐含了
     *   “node 节点一定在目标路径上”的信息。而本题中目标路径不一定是 root-to-leaf 的，即任意一个节点都有可能在或不在目标路径上
     *   ∴ 对每个节点都需要分情况讨论：
     *     1. 若 node 在目标路径上，则继续使用 L112、L113 中的方法，检查 sum - node.val 是否为0来确定目标路径；
     *     2. 若 node 不在目标路径上，递归地在 node 的子树中寻找目标路径。
     *   用公式表达：f(node, sum) = 包含 node 的目标路径数 + 不包含 node 的目标路径数
     *                          = f2(node, sum) + f(node.left, sum) + f(node.right, sum)。
     *   其中“包含 node 的目标路径数”的完整表述是：求以 node 为根的二叉树中有几条从 node 到任意节点，且节点和为 sum 的 path。
     *   其转态转移为：f(node, sum) = node.val == sum ? 1 : 0
     *                            + f(node.left, sum - node.val)
     *                            + f(node.right, sum - node.val)
     * - 💎 总结：
     *   1. 该解法的实现采用两套递归来分别计算两种不同情况下的结果，最后加在一起返回；
     *   2. pathSumWithNode 方法可以单独单做一道题来做。
     * - 时间复杂度 O(n^2)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int pathSum(TreeNode root, int sum) {
        if (root == null) return 0;
        return pathSumWithNode(root, sum)                           // 包含节点 root 的目标路径数
             + pathSum(root.left, sum) + pathSum(root.right, sum);  // 不包含节点 root 的目标路径数
    }

    private static int pathSumWithNode(TreeNode node, int sum) {  // 返回以 node 为根的二叉树中有几条从 node 到任意节点，且节点和为 sum 的 path
        if (node == null) return 0;
        int count = 0;
        if (node.val == sum) count++;  // 找到一条目标路径，但不能就此 return ∵ 后面的路径上可能有正、负数节点值，加在一起又等于 sum
        count += pathSumWithNode(node.left, sum - node.val);
        count += pathSumWithNode(node.right, sum - node.val);
        return count;
    }

    /*
     * 解法2：Recursion (DFS) + Memoization
     * - 思路：该题可以看做是 L560_SubarraySumEqualsK 的二叉树版，本质上也是区间求和问题 ∴ 可以在二叉树上采用 Prefix Sum
     *   的思路，例如 test case 1 中 5->3 的路径和 = 10->5->3->3 的路径和 - 10 的路径和。基于这个思路，可通过递归遍历所有
     *   节点，对每个节点累积 prefix sum（即根节点到当前节点的路径和），并用 map 检查是否有（以及有几个）其他的子路径和能使得
     *   prefix sum - 子路径和 = target sum，记录这样的子路径和的个数。
     * - 更多解释 SEE：https://leetcode.com/problems/path-sum-iii/discuss/91878/17-ms-O(n)-java-Prefix-sum-method
     *   中 kekezi 的评论。
     * - 👉 总结：该题与 L560_SubarraySumEqualsK 都是 Prefix Sum 和 Two Sum 思想的经典应用。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int pathSum2(TreeNode root, int sum) {
        Map<Integer, Integer> map = new HashMap<>();  // 存储 <prefixSum, frequency>
        map.put(0, 1);
        return helper2(root, 0, sum, map);
    }

    private static int helper2(TreeNode root, int preSum, int sum, Map<Integer, Integer> map) {
        if (root == null) return 0;

        preSum += root.val;                             // 累积 prefix sum
        int count = map.getOrDefault(preSum - sum, 0);  // 检查 map 中 complement 的个数（即找出该路径上有几个子路径和能让 preSum - 子路径和 == sum）
        map.merge(preSum, 1, Integer::sum);             // 在 map 中插入 prefix sum 或更新其频率，相当于
                                                        // map.put(preSum, map.getOrDefault(preSum, 0) + 1);
        count += helper2(root.left, preSum, sum, map);  // 递归处理左右子树
        count += helper2(root.right, preSum, sum, map);

        map.put(preSum, map.get(preSum) - 1);           // 在回溯到递归上一层之前将 prefix sum 的频率-1以恢复原状
        return count;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{10, 5, -3, 3, 2, null, 11, 3, -10, null, 1});
        log(pathSum2(t1, 8));
        /*
         * expects 4. (5->3, 5->2->1, -3->11, 10->5->3->-10)
         *            10
         *           /  \
         *          5   -3
         *         / \    \
         *        3   2   11
         *       / \   \
         *      3 -10   1
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{10, 8, -2, -10});
        log(pathSum2(t2, 8));
        /*
         * expects 3. (8, 10->8->-10, 10->-2)
         *         10
         *        /  \
         *       8   -2
         *      /
         *    -10
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{1});
        log(pathSum2(t3, 0));
        /*
         * expects 0.
         * */

        TreeNode t4 = createBinaryTreeBreadthFirst(new Integer[]{});
        log(pathSum2(t4, 0));
        /*
         * expects 0.
         * */
    }
}
