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
 *
 * - Path Sum 系列：
 *   - L112_PathSum 判断是否存在节点和为 sum 的 root-to-leaf 路径；
 *   - L113_PathSumII 打印所有节点和为 sum 的 root-to-leaf 路径；
 *   - L437_PathSumIII 统计节点和为 sum 的路径个数（路径不必须为 root-to-leaf，可从任意节点开始/结束）。
 * */

public class L437_PathSumIII {
    /*
     * 解法1：DFS + Double recursion
     * - 思路：L112_PathSum、L113_PathSumII 中寻找符合条件的目标路径的方式是在递归过程中不断让 sum - node.val，这其实隐含了
     *   “node 节点一定在目标路径上”的信息。而本题中目标路径不一定是 root-to-leaf 的，即任意一个节点都有可能在或不在目标路径上
     *   ∴ 对每个节点都需要分情况讨论：
     *     1. 若 node 在目标路径上，则问题转化为求"以 node 为根的二叉树中有几条从 node 到任意节点，且节点和为 sum 的 path"，
     *        该问题可继续使用 L112、L113 中的方法，检查 sum - node.val 是否为0来确定目标路径；
     *     2. 若 node 不在目标路径上，则继续用同样的方法递归搜索 node 的左右子树。
     *   形式化表达：f(node, sum) = 包含 node 的目标路径数 + 不包含 node 的目标路径数
     *                          = f2(node, sum) + f(node.left, sum) + f(node.right, sum)。
     *   其中，"以 node 为根的二叉树中有几条从 node 到任意节点，且节点和为 sum 的 path" 的状态转移方程为：
     *             f2(node, sum) = node.val == sum ? 1 : 0
     *                           + f(node.left, sum - node.val)
     *                           + f(node.right, sum - node.val)
     * - 💎 总结：
     *   1. 该解法的实现采用两套递归来分别计算两种不同情况下的结果，最后加在一起返回；
     *   2. pathSumWithNode 方法可以单独单做一道题来做。
     * - 时间复杂度 O(n^2)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int pathSum(TreeNode root, int sum) {
        if (root == null) return 0;
        return pathSumWithNode(root, sum)                           // 包含节点 root 的目标路径数
             + pathSum(root.left, sum) + pathSum(root.right, sum);  // 若不包含节点 root，则继续用同样的方法搜索左右子树
    }

    private static int pathSumWithNode(TreeNode node, int sum) {  // 返回以 node 为根的二叉树中有几条从 node 到任意节点，且节点和为 sum 的 path
        if (node == null) return 0;
        int count = 0;
        if (node.val == sum) count++;  // 找到一个解，但不能就此返回 ∵ 后面的路径上可能有正、负数节点值，加在一起又等于 sum
        count += pathSumWithNode(node.left, sum - node.val);
        count += pathSumWithNode(node.right, sum - node.val);
        return count;
    }

    /*
     * 解法2：DFS + Prefix sum + Backtracking（🥇最优解）
     * - 思路：该题可以看做是 L560_SubarraySumEqualsK 的二叉树版，即二叉树上的区间求和问题 ∴ 同样可采用 Prefix Sum 技巧来
     *   优化性能，例如 test case 1 中 pathSum(3->3) = pathSum(10->5->3->3) - pathSum(10->5)。
     * - 推演：路径 10 -> 5 -> 3 -> -10 的 path 的推演过程如下：
     *            10         - map(0:1), pathSum=10, map.get(10-8)=0 ∴ count=0
     *           /  \
     *          5   -3       - map(0:1,10:1), pathSum=15, map.get(15-8)=0 ∴ count=0
     *         / \    \
     *        3   2   11     - map(0:1,10:1,15:1), pathSum=18, map.get(18-8)=1 ∴ count=1
     *       / \   \
     *      3 -10   1        - map(0:1,10:1,15:1), pathSum=8, map.get(8-8)=1 ∴ count=2
     *
     * - 👉 总结：该题与 L560_SubarraySumEqualsK 都是 Prefix Sum 和 Two Sum 思想的经典应用。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int pathSum2(TreeNode root, int sum) {
        Map<Integer, Integer> map = new HashMap<>();  // Map<prefixSum, count>
        map.put(0, 1);
        return backtrack2(root, sum, 0, map);
    }

    private static int backtrack2(TreeNode root, int sum, int pathSum, Map<Integer, Integer> map) {
        if (root == null) return 0;

        pathSum += root.val;                        // 累积 pathSum（也就是 prefix sum）
        int count = map.getOrDefault(pathSum - sum, 0);  // 检查 Map 中 complement 的个数（即查找该路径上有几个子路径和能让 pathSum - 子路径和 == sum）
        map.merge(pathSum, 1, Integer::sum);  // 在 Map 中插入或更新 prefix sum 的频率，相当于 map.put(pathSum, map.getOrDefault(pathSum, 0) + 1);

        count += backtrack2(root.left, sum, pathSum, map);  // 递归处理左右子树，并将结果累积在 count 上
        count += backtrack2(root.right, sum, pathSum, map);

        map.merge(pathSum, -1, Integer::sum);  // 注意在回溯到上一层之前将 prefix sum 的频率-1以恢复原状，相当于 map.put(pathSum, map.get(pathSum) - 1);
        return count;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{10, 5, -3, 3, 2, null, 11, 3, -10, null, 1});
        log(pathSum(t1, 8));
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
        log(pathSum(t2, 8));
        /*
         * expects 3. (8, 10->8->-10, 10->-2)
         *         10
         *        /  \
         *       8   -2
         *      /
         *    -10
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{1});
        log(pathSum(t3, 0));
        /*
         * expects 0.
         * */

        TreeNode t4 = createBinaryTreeBreadthFirst(new Integer[]{});
        log(pathSum(t4, 0));
        /*
         * expects 0.
         * */
    }
}
