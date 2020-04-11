package BinaryTreeAndRecursion.S3_DefineRecursiveProblem;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import Utils.Helpers.TreeNode;

/*
 * Path Sum II
 *
 * - Given a binary tree and a sum, find all root-to-leaf paths where each path's sum equals the given sum.
 *
 * - Path Sum 系列：
 *   - L112_PathSum 判断是否存在节点和为 sum 的 root-to-leaf 路径；
 *   - L113_PathSumII 打印所有节点和为 sum 的 root-to-leaf 路径；
 *   - L437_PathSumIII 统计节点和为 sum 的路径个数（不管是否是 root-to-leaf 都算）。
 * */

public class L113_PathSumII {
    /*
     * 解法1：Recursion (DFS)
     * - 思路：从根节点开始递归生成路径 path，若到达叶子节点且剩余 sum 为0，则说明该 path 是符合要求的路径，添加到结果集 res 中。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static List<List<Integer>> pathSum(TreeNode root, int sum) {
        List<List<Integer>> res = new ArrayList<>();
        helper(root, sum, new ArrayList<>(), res);
        return res;
    }

    private static void helper(TreeNode root, int sum, List<Integer> path, List<List<Integer>> res) {
        if (root == null) return;
        path.add(root.val);

        if (root.left == null && root.right == null && root.val == sum) {
            res.add(path);
            return;
        }
        helper(root.left, sum - root.val, new ArrayList<>(path), res);
        helper(root.right, sum - root.val, new ArrayList<>(path), res);
	}

    /*
     * 解法2：DFS + Backtracking (解法2的简化版)
     * - 思路：与解法1一致。
     * - 实现：与解法1不同之处在于该解法：
     *   1. 使用回溯技巧使得 path 从始至终都是复用的 —— ∵ 递归会先往左下递归到底再返回上层递归右子树 ∴ 若要继续复用 path 对象，
     *      则需在递归返回上一层之前将 path 恢复原状（这也是回溯的关键）；
     *   2. 只有在确定该 path 符合条件时才会被复制进 res（这也是该解法比其他解法快的原因）。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static List<List<Integer>> pathSum2(TreeNode root, int sum) {
        List<List<Integer>> res = new ArrayList<>();
        helper2(root, sum, new ArrayList<>(), res);
        return res;
    }

    private static void helper2(TreeNode root, int sum, List<Integer> path, List<List<Integer>> res) {
        if (root == null) return;
        path.add(root.val);

        if (root.left == null && root.right == null && root.val == sum) {
            res.add(new ArrayList<>(path));  // 若是符合要求的 path，则复制进 res 里
            path.remove(path.size() - 1);    // 返回上层递归之前将添加的元素移除，让 path 恢复原状，这样回到上层后才能继续复用 path
            return;
        }
        helper2(root.left, sum - root.val, path, res);  // 则继续递归并复用 path
        helper2(root.right, sum - root.val, path, res);
        path.remove(path.size() - 1);        // 同样在返回上层递归之前要将 path 恢复原状
    }

    /*
     * 解法3：DFS (Recursion)
     * - 思路：与 L257_BinaryTreePaths 解法1的思路一致。递归函数 f(n, sum) 定义为：返回以 n 为根的二叉树上的所有节点值之和
     *   为 sum 的 root-to-leaf paths。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static List<List<Integer>> pathSum3(TreeNode root, int sum) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;

        if (root.left == null && root.right == null && sum == root.val) {  // 若该路径符合要求，则在 res 中创建包含该节点的新列表
            List<Integer> path = new ArrayList<>();
            path.add(root.val);
            res.add(path);
            return res;
        }

        List<List<Integer>> paths = pathSum3(root.left, sum - root.val);  // 将左右子树返回的 res 连接起来
        paths.addAll(pathSum3(root.right, sum - root.val));

        return paths.stream().map(path -> {  // 向连接后的 res 中的每个 path 头部添加当前节点值
            path.add(0, root.val);
            return path;
        }).collect(Collectors.toList());
    }

	/*
     * 解法4：Iteration (DFS) (解法2的迭代版)
     * - 思路：与 L257_BinaryTreePaths 解法3一致。
     * - 同理：只需将 Stack 替换为 Queue 就得到了 BFS 解法。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> pathSum4(TreeNode root, int sum) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;

        Stack<Pair<List<TreeNode>, Integer>> stack = new Stack<>();  // stack 中保存 <path 节点列表, path 节点之和>
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

	/*
     * 解法5：Iteration (DFS, post-order traversal)
     * - 思路：采用后续遍历的思路，在往下遍历的过程中形成 path，若到达叶子节点时 sum=0，则该该 path 有效，此时将 path 中的
     *   最后一个元素去除，恢复 path 在父节点时的状态，转而去访问右子树。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> pathSum5(TreeNode root, int sum) {
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> path = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<TreeNode>();

        int currSum = 0;
        TreeNode prev = null, curr = root;

        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {  // 先往左遍历到最左节点（不一定是叶子节点），一路上入栈每个节点、记录路径、累加节点值
                stack.push(curr);
                path.add(curr.val);
                currSum += curr.val;
                curr = curr.left;
            }
            curr = stack.peek();    // 取栈顶节点
            if (curr.right != null && curr.right != prev) {  // 若栈顶节点有右子树，且右子树没访问过
                curr = curr.right;                           // 转向开始用同样的方式处理右子树
                continue;
            }
            if (curr.left == null && curr.right == null && currSum == sum)  // 若到达叶子节点，将该路径放入结果集
                res.add(new ArrayList<>(path));
            prev = curr;                   // 访问完当前节点后，用 prev 标记其为已访问
            stack.pop();                   // 将当前节点出栈
            path.remove(path.size() - 1);  // 将 path 恢复到父节点的状态（因为要复用）
            currSum -= curr.val;           // 将 currSum 也恢复到父节点的状态
            curr = null;
        }

        return res;
    }


    public static List<List<Integer>> pathSum0(TreeNode root, int sum) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;



        return res;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, 6, null, 5, -2, 2, 8, null, null, 7, 9});
        log(pathSum0(t1, 9));
        /*
         * expects [[1,3,-2,7], [1,3,5]].（注意 [1,2,6] 不是）
         *            1
         *           / \
         *          2   3
         *         /   / \
         *        6   5  -2
         *       / \     / \
         *      2   8   7   9
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{});
        log(pathSum0(t2, 1));
        /*
         * expects [].
         * */
    }
}
