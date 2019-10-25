package BinaryTreeAndRecursion.ComplexRecursiveProblem;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import Utils.Helpers.TreeNode;

/*
 * Path Sum III
 *
 * - Given a binary tree, find the number of paths that sum to a given value.
 * - Note that the path does not need to start or end at the root or a leaf, but it must go downwards
 *   (traveling only from parent nodes to child nodes).
 * - The tree has no more than 1,000 nodes and the values are in the range -1,000,000 to 1,000,000.
 * */

public class L437_PathSumIII {
    /*
     * 解法1：Recursion (DFS)
     * - 思路：在 L112_PathSum 和 L113_PathSumII 中，我们寻找符合条件的目标路径的方式是在递归过程中不断让 sum - node.val，
     *   这其实隐含了“节点 node 一定在目标路径上”的信息。而本题中目标路径不一定是 root-to-leaf 的，所以有些节点（例如根节点）
     *   不一定在目标路径上，因此需要分情况讨论：
     *     1. 若 node 在目标路径上，则继续使用 L112、L113 中的方法，检查 sum - node.val 是否为0来确定目标路径；
     *     2. 若 node 不在目标路径上，递归地在 node 的子树中寻找目标路径。
     *   用公式表达：f(node, sum) = 包含 node 的目标路径数 + 不包含 node 的目标路径数
     *                          = f2(node, sum) + f(node.left, sum) + f(node.right, sum)。
     * - 💎总结：该解法的实现采用2套递归来分别计算2种不同情况下的结果，最后加在一起返回。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static int pathSum(TreeNode root, int sum) {  // 定义：在以 root 为根的二叉树中计算目标路径个数（root 不在目标路径上）
        if (root == null) return 0;
        int count = pathSumWith(root, sum);              // 计算有多少条目标路径包含节点 node
        count += pathSum(root.left, sum) + pathSum(root.right, sum);  // 计算有多少目标路径不包含节点 node
        return count;
    }

    private static int pathSumWith(TreeNode root, int sum) {  // 定义：在以 root 为根的二叉树中计算目标路径个数（root 在目标路径上）
        int count = 0;
        if (root == null) return count;
        if (root.val == sum) count++;  // 找到一条目标路径，但不能就此 return ∵ 后面的路径上可能有正、负数节点值，加在一起又等于 sum
        count += pathSumWith(root.left, sum - root.val) + pathSumWith(root.right, sum - root.val);
        return count;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{10, 5, -3, 3, 2, null, 11, 3, -10, null, 1});
        log(pathSum(t1, 8));
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
        log(pathSum(t2, 8));
        /*
         * expects 2. (8, 10->-2)
         *         10
         *        /  \
         *       8   -2
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{});
        log(pathSum(t3, 0));
        /*
         * expects 0.
         * */
    }
}
