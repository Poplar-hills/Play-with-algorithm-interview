package BinaryTreeAndRecursion.S1_Basics;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import Utils.Helpers.TreeNode;

/*
 * Symmetric Tree
 *
 * - Given a binary tree, check whether it is a mirror of itself (ie, symmetric around its center, both in
 *   structure and node values).
 * */

public class L101_SymmetricTree {
    /*
     * 解法1：BFS (Level palindrome)
     * - 思路：最 instinctive 的思路就是检查树中每层节点是否是一个 palindrome —— 通过 BFS 进行层序遍历，将每层节点放入一个
     *   线性结构中，然后检查其该是否是 palindrome。
     * - 实现：注意 ∵ 题中 symmetric 的定义是 structure 和 value 都对应 ∴ 若只将非空节点放入线性结构则只能验证 value 是否
     *   对应，而无法验证 structure 对应（如 test case 4、5）∴ 还需要将 null 节点也放入线性结构，只有 null 节点的位置也
     *   对应上了才说明是 palindrome；
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static boolean isSymmetric(TreeNode root) {
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            for (int i = 0, size = q.size(); i < size; i++) {
                TreeNode node = q.poll();
                if (node != null) {
                    q.offer(node.left);
                    q.offer(node.right);
                }
            }
            if (!isPalindrome(new ArrayList<>(q)))  // ∵ isPalindrome 方法需要随机访问元素 ∴ 需要先转换为支持随机访问的数据结构
                return false;
        }

        return true;
    }

    private static boolean isPalindrome(List<TreeNode> list) {
        for (int i = 0; i < list.size(); i++) {
            TreeNode l = list.get(i);
            TreeNode r = list.get(list.size() - 1 - i);
            if (l == null && r == null) continue;   // 除了 TreeNode 节点，还要验证 null 节点是否对应
            if (l == null || r == null || l.val != r.val) return false;
        }
        return true;
    }

    /*
     * 解法2：DFS (Recursion)
     * - 思路：另一种思路是，若一棵树是对称的，则其左右子树应互为镜像 ∴ 可以用这棵树与自身进行对照 —— 这就将问题转化为了两棵树
     *   之间的对比问题。
     * - 实现：若两棵树互为镜像，则：
     *     1. 他们的根节点的节点值相同；
     *     2. 树1的左子树和树2的右子树互为镜像；
     *     3. 树1的右子树和树2的左子树互为镜像。
     *   这是个递归定义 ∴ 可以很自然的用递归求解，递归函数的语义就是求两棵树是否互为镜像。而这里“互为镜像”的含义是：
     *     1. 两棵树上任意相同位置同时有或同时没有节点；
     *     2. 若有节点，则节点值相同。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static boolean isSymmetric2(TreeNode root) {
        return isMirror(root, root);  // 检查两棵树是否互为镜像
    }

    private static boolean isMirror(TreeNode t1, TreeNode t2) {
        if (t1 == null && t2 == null) return true;   // 同时有节点
        if (t1 == null || t2 == null) return false;  // 同时没有节点
        return t1.val == t2.val && isMirror(t1.left, t2.right) && isMirror(t1.right, t2.left);
    }

    /*
     * 解法3：BFS
     * - 思路：与解法1一致。
     * - 实现：使用 BFS 对树同时从两个方向进行层序遍历，注意 null 节点也要入队检查。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean isSymmetric3(TreeNode root) {
        if (root == null) return true;
        Queue<TreeNode> q1 = new LinkedList<>();
        Queue<TreeNode> q2 = new LinkedList<>();
        q1.offer(root);
        q2.offer(root);

        while (!q1.isEmpty()) {
            TreeNode n1 = q1.poll(), n2 = q2.poll();
            if (n1 == null && n2 == null) continue;
            if (n1 == null || n2 == null || n1.val != n2.val) return false;

            q1.offer(n1.left);   // q1 先入队 n1.left
            q1.offer(n1.right);
            q2.offer(n2.right);  // q2 先入队 n2.right
            q2.offer(n2.left);
        }

        return true;
    }

    /*
     * 解法4：BFS
     * - 思路：与解法2、3一致。
     * - 实现：只使用一个 Queue，同时入队左右两棵子树的节点。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean isSymmetric4(TreeNode root) {
        if (root == null) return true;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        q.offer(root);

        while (!q.isEmpty()) {
            TreeNode n1 = q.poll(), n2 = q.poll();
            if (n1 == null && n2 == null) continue;
            if (n1 == null || n2 == null || n1.val != n2.val) return false;

            q.offer(n1.left);  // 注意这里跟解法2中的入队顺序不同，需对称入队
            q.offer(n2.right);
            q.offer(n1.right);
            q.offer(n2.left);
        }

        return true;
    }

    /*
     * 解法5：DFS (Iteration)
     * - 思路：与解法2、3、4一致。
     * - 思路：与解法3的区别是使用 DFS（只有数据结构改成了 Stack），即对树同时从左右两边对树进行 DFS。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean isSymmetric5(TreeNode root) {
        if (root == null) return true;
        Stack<TreeNode> s = new Stack<>();
        s.add(root);
        s.add(root);

        while (!s.isEmpty()) {
            TreeNode n1 = s.pop(), n2 = s.pop();
            if (n1 == null && n2 == null) continue;
            if (n1 == null || n2 == null || n1.val != n2.val) return false;

            s.add(n1.left);  // 这里同解法4，需对称入栈
            s.add(n2.right);
            s.add(n1.right);
            s.add(n2.left);
        }

        return true;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, 3, 4, 4, 3});
        log(isSymmetric(t1));
        /*
         * expects true.
         *        1
         *       / \
         *      2   2
         *     / \ / \
         *    3  4 4  3
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, 3, null, null, 3});
        log(isSymmetric(t2));
        /*
         * expects true.
         *        1
         *       / \
         *      2   2
         *     /     \
         *    3       3
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, 3, 4, 3, 4});
        log(isSymmetric(t3));
        /*
         * expects false.
         *        1
         *       / \
         *      2   2
         *     / \ / \
         *    3  4 3  4
         * */

        TreeNode t4 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, null, 3, null, 3});
        log(isSymmetric(t4));
        /*
         * expects false.
         *        1
         *       / \
         *      2   2
         *       \   \
         *        3   3
         * */

        TreeNode t5 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 2, 2, null, 2});
        log(isSymmetric(t5));
        /*
         * expects false.
         *        1
         *       / \
         *      2   2
         *     /   /
         *    2   2
         * */
    }
}
