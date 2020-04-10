package BinaryTreeAndRecursion.S1_Basics;

import static Utils.Helpers.*;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/*
 * Invert Binary Tree
 * */

public class L226_InvertBinaryTree {
    /*
     * 解法1：DFS (Recursion)
     * - 思路：观察 test cases 可知，要反转一棵二叉树实际上就要为树上的每个节点交换左右子树，例如：
     *               4                    4                    4
     *             /   \                /   \                /   \
     *            2     7    ----->    2     7    ----->    7     2
     *           / \   / \            / \   / \            / \   / \
     *          1   3 6   9          3   1 9   6          9   6 3   1
     * - 实现：根据以上思路，可以采用 DFS，先交换当前节点的左、右子节点，然后再分别递归交换后的左右子节点。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 n 为节点数，h 为树高。
     * */
    public static TreeNode invertTree(TreeNode root) {
        if (root == null) return null;
        TreeNode temp = root.left;
        root.left = root.right;
        root.right = temp;
        invertTree(root.left);
        invertTree(root.right);
        return root;
    }

    /*
     * 解法2：DFS (Recursion)
     * - 思路：与解法1一致。
     * - 实现：与解法1不同之处在于，先分别递归左、右子树，然后在回程路上交换左右子节点。例如解法1中的例子：
     *   - 4的左子树 = invert 之后的4的右子树，即 4.left = invertTree(7)；
     *   - 4的右子树 = invert 之后的4的左子树，即 4.right = invertTree(2)。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 n 为节点数，h 为树高。
     * */
    public static TreeNode invertTree2(TreeNode root) {
        if (root == null) return null;
        TreeNode temp = root.left;
        root.left = invertTree(root.right);
        root.right = invertTree(temp);
        return root;
    }

    /*
     * 解法3：BFS
     * - 思路：与解法1、2一致。
     * - 实现：采用 BFS，在标准的层序遍历基础上，将访问节点的逻辑用交换左右子树代替。
     * - 时间复杂度 O(n)，其中 n 为节点数。
     * - 空间复杂度 O(n)，∵  同一时间 q 中最多存在 n/2 个节点（即完美二叉树的最后一行）∴ 是 O(n) 级别。
     * */
    public static TreeNode invertTree3(TreeNode root) {
        if (root == null) return null;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            TreeNode node = q.poll();

            TreeNode temp = node.left;
            node.left = node.right;
            node.right = temp;

            if (node.left != null) q.offer(node.left);
            if (node.right != null) q.offer(node.right);
        }

        return root;
    }

    /*
     * 解法4：DFS (Iteration)
     * - 思路：与解法1、2、3一致。
     * - 实现：采用 DFS 迭代实现，即只是在解法3的基础上将数据结构改为了 Stack。
     * - 💎 总结：可见使用 Queue/Stack 决定了节点的访问顺序，即 BFS/DFS。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static TreeNode invertTree4(TreeNode root) {
        if (root == null) return null;
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();

            TreeNode temp = node.left;
            node.left = node.right;
            node.right = temp;

            if (node.left != null) stack.push(node.left);
            if (node.right != null) stack.push(node.right);
        }

        return root;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{4, 2, 7, 1, 3, 6, 9});
        printBinaryTreeBreadthFirst(invertTree(t1));
        /*
         * expects [4, 7, 2, 9, 6, 3, 1].
         *         4                  4
         *       /   \              /   \
         *      2     7    --->    7     2
         *     / \   / \          / \   / \
         *    1   3 6   9        9   6 3   1
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2});
        printBinaryTreeBreadthFirst(invertTree(t2));
        /*
         * expects [1, null, 2].
         *      1              1
         *     /      --->      \
         *    2                  2
         * */
    }
}
