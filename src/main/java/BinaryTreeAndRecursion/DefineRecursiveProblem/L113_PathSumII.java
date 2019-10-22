package BinaryTreeAndRecursion.DefineRecursiveProblem;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import Utils.Helpers.TreeNode;
import javafx.util.Pair;

/*
 * Path Sum II
 *
 * - Given a binary tree and a sum, find all root-to-leaf paths where each path's sum equals the given sum.
 * */

public class L113_PathSumII {
    /*
     * 解法1：Recursion (DFS)
     * - 思路：递归函数 f(n) 定义：将以 n 为根的二叉树上所有节点值之和为 sum 的 root-to-leaf path。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static List<List<Integer>> pathSum(TreeNode root, int sum) {
        return null;
    }

    /*
     * 解法2：Recursion (DFS)
     * - 思路：从根节点开始递归生成路径（path），若到达叶子节点且剩余 sum 为0，则说明是一条符合要求的路径，添加到结果集 res 中。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static List<List<Integer>> pathSum2(TreeNode root, int sum) {
        List<List<Integer>> res = new ArrayList<>();
        if (root != null) {
            List<Integer> path = new ArrayList<>();
            path.add(root.val);
            helper(root, sum, path, res);
        }
        return res;
    }

    private static void helper(TreeNode root, int sum, List<Integer> path, List<List<Integer>> res) {
        if (root.left == null && root.right == null && root.val == sum) res.add(path);
        if (root.left != null) {
            List<Integer> newPath = new ArrayList<>(path);
            newPath.add(root.left.val);
            helper(root.left, sum - root.val, newPath, res);
        }
        if (root.right != null) {
            List<Integer> newPath = new ArrayList<>(path);
            newPath.add(root.right.val);
            helper(root.right, sum - root.val, newPath, res);
        }
	}

	/*
     * 解法3：Iteration (DFS) (解法2的迭代版)
     * - 思路：与 L257_BinaryTreePaths 解法3一致。
     * - 同理：只需将 Stack 替换为 Queue 就得到了 BFS 解法。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> pathSum3(TreeNode root, int sum) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;

        Stack<Pair<List<TreeNode>, Integer>> stack = new Stack<>();  // stack 中的数据格式：<path 节点列表, path 节点之和>
        List<TreeNode> initialList = new ArrayList<>();
        initialList.add(root);
        stack.push(new Pair<>(initialList, root.val));

        while (!stack.isEmpty()) {
            Pair<List<TreeNode>, Integer> pair = stack.pop();
            List<TreeNode> list = pair.getKey();
            int currSum = pair.getValue();
            TreeNode lastNode = list.get(list.size() - 1);

            if (currSum == sum && lastNode.left == null && lastNode.right == null) {
                List<Integer> valList = list.stream().map(n -> n.val).collect(Collectors.toList());  // 将节点列表转化为整型列表
                res.add(valList);
                continue;
            }

            Consumer<TreeNode> fn = node -> {
                List<TreeNode> newList = new ArrayList<>(list);
                newList.add(node);
                stack.push(new Pair<>(newList, currSum + node.val));
            };

            if (lastNode.left != null) fn.accept(lastNode.left);
            if (lastNode.right != null) fn.accept(lastNode.right);
        }

        return res;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, 6, null, 5, -2, 2, 8, null, null, 7, 9});
        log(pathSum2(t1, 9));
        /*
         * expects [[1,3,-2,7], [1,3,5]].（注意 [1,2,6] 不是）
         *        1
         *       / \
         *      2   3
         *     /   / \
         *    6   5  -2
         *   / \     / \
         *  2   8   7   9
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{});
        log(pathSum2(t2, 1));
        /*
         * expects [].
         * */
    }
}
