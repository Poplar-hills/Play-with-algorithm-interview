package BinaryTreeAndRecursion.S5_LowestCommonAncestor;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import java.util.Stack;

import Utils.Helpers.TreeNode;

/*
 * Validate Binary Search Tree
 *
 * - Given a binary tree, determine if it is a valid binary search tree (BST).
 *
 * - Assume a BST is defined as follows:
 *   1. The left subtree of a node contains only nodes with keys less than the node's key.
 *   2. The right subtree of a node contains only nodes with keys greater than the node's key.
 *   3. Both the left and right subtrees must also be binary search trees.
 * */

public class L98_ValidateBinarySearchTree {
    /*
     * 解法1：Definition + DFS
     * - 思路：根据题中给出的 BST 定义可知，满足以下4个条件的节点才算是 BST：
     *     1. 父节点 > 左子树中的所有节点；
     *     2. 父节点 < 右子树中的所有节点；
     *     3. 左子树是 BST；
     *     4. 右子树是 BST。
     *   ∴ 可以紧贴定义设计代码，为每个节点都进行这4项检查。
     * - 时间复杂度 O(n^n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static boolean isValidBST(TreeNode root) {
        if (root == null) return true;
        return root.val > maxVal(root.left) && root.val < minVal(root.right)
            && isValidBST(root.left) && isValidBST(root.right);
    }

    private static int maxVal(TreeNode root) {  // 在以 root 为根的二叉树中找到最大的节点值
        if (root == null) return Integer.MIN_VALUE;
        if (root.left == null && root.right == null) return root.val;
        int maxChild = Math.max(maxVal(root.left), maxVal(root.right));
        return Math.max(maxChild, root.val);
    }

    private static int minVal(TreeNode root) {  // 在以 root 为根的二叉树中找到最小的节点值
        if (root == null) return Integer.MAX_VALUE;
        if (root.left == null && root.right == null) return root.val;
        int minChild = Math.min(minVal(root.left), minVal(root.right));
        return Math.min(minChild, root.val);
    }

    /*
     * 解法2：Definition + DFS（解法1的简化版）
     * - 思路：与解法1一致，都是严格按照 BST 的定义进行检查。
     * - 实现：解法1中的 maxVal、minVal 方法会检查各自树上的所有节点，从中找到最大、小节点，这其实并非必要。本解法中的
     *   isMaxInBST、isMinInBST 方法都先假设左、右子树都是 BST，而是否真的是 BST 的检查则交给 isValidBST 递归地进行
     *   ∴ 复杂度要比解法1低的多。
     * - 时间复杂度 O(nlogn)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static boolean isValidBST2(TreeNode root) {
        if (root == null) return true;
        return isMaxInBST(root, root.left) && isMinInBST(root, root.right)
            && isValidBST2(root.left) && isValidBST2(root.right);
    }

    private static boolean isMaxInBST(TreeNode node, TreeNode root) {  // 检查 node 是否 > root 树上的所有节点
        if (root == null) return true;
        if (node.val <= root.val) return false;
        return isMaxInBST(node, root.right);  // 右子树是否是 BST 交给 isValidBST 去检查，这里假设右子树是 BST ∴ 只要
    }                                         // node > root 树上的最右侧节点即说明 node > root 树上的所有节点

    private static boolean isMinInBST(TreeNode node, TreeNode root) {  // 检查 node 是否 < root 树上的所有节点
        if (root == null) return true;
        if (node.val >= root.val) return false;
        return isMinInBST(node, root.left);
    }

    /*
     * 解法3：Recursion
     * - 思路：另一种思路是使用上下界来检查每层节点是否合法，例如下面这棵树：
     *                5
     *              /   \
     *             3     7
     *            / \   / \
     *           2   4 8   6
     *     - 节点3是合法的 ∵ 它 < 5；      - 节点7是合法的 ∵ 它 > 5；
     *     - 节点2是合法的 ∵ 它 < 3；      - 节点8是非法的 ∵ 它应该 < 7 且 > 5；
     *     - 节点4是合法的 ∵ 3 < 它 < 5；  - 节点6是非法的 ∵ 它应该 > 7；
     *   可见每个节点值的上、下界是由其父节点及祖父节点决定的，例如对于节点3来说，5是它的上界，而对于节点4来说5就变成了上界，而3
     *   是它的下界。这可以通过给递归函数传递上、下界参数来实现。而每层的递归函数可以定义为：当前节点是否 > 下界，且 < 上界。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static boolean isValidBST3(TreeNode root) {
        return helper3(root, null, null);  // ∵ 节点值可能不在整型取值范围内 ∴ 不能使用 Integer.MIN_VALUE、Integer.MAX_VALUE
    }

    private static boolean helper3(TreeNode root, Integer lowerBound, Integer upperBound) {
        if (root == null) return true;
        if (lowerBound != null && root.val <= lowerBound) return false;
        if (upperBound != null && root.val >= upperBound) return false;
        return helper3(root.left, lowerBound, root.val)
            && helper3(root.right, root.val, upperBound);
    }

    /*
     * 解法4：DFS (Iteration) (解法3的迭代版)
     * - 思路：与解法3一致。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean isValidBST4(TreeNode root) {
        if (root == null) return true;
        Stack<TreeNode> stack = new Stack<>();  // 也可以只用一个栈：Stack<Triplet<节点, 下界, 上界>>
        Stack<Integer> lowers = new Stack<>();  // 对应存储 stack 中每个节点的取值下界
        Stack<Integer> uppers = new Stack<>();  // 对应存储 stack 中每个节点的取值上界
        stack.push(root);
        lowers.push(null);
        uppers.push(null);

        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            Integer lower = lowers.pop();
            Integer upper = uppers.pop();

            if (lower != null && node.val <= lower) return false;
            if (upper != null && node.val >= upper) return false;

            if (node.left != null) {
                stack.push(node.left);
                lowers.push(lower);
                uppers.push(node.val);
            }
            if (node.right != null) {
                stack.push(node.right);
                lowers.push(node.val);
                uppers.push(upper);
            }
        }

        return true;
    }

    /*
     * 解法5：DFS (Iteration, In-order traversal)
     * - 思路：另一种解法是利用 BST 的性质 —— BST 的中序遍历是从小到大有序的 ∴ 可以在中序遍历的过程中检查遍历到的节点是否有序。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean isValidBST5(TreeNode root) {
        if (root == null) return true;
        Stack<TreeNode> stack = new Stack<>();
        Integer prev = null;        // ∵ 节点值可能不在整型取值范围内 ∴ 不能使用 Integer.MIN_VALUE
        TreeNode curr = root;

        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {  // 先向左走到最左，一路入栈节点
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.pop();     // 访问栈顶节点
            if (prev != null && curr.val <= prev) return false;
            prev = curr.val;
            curr = curr.right;      // 转向右子节点，重新开始循环，若没有右子节点，则会跳过内层 while 出栈下一个节点
        }

        return true;
    }

    /*
     * 解法6：DFS (Recursion, In-order traversal)
     * - 思路：与解法5一致。
     * - 实现：采用递归实现。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    private static Integer prev;

    public static boolean isValidBST0(TreeNode root) {
        prev = null;           // 为了防止 test case 之间冲突
        return helper6(root);
    }

    public static boolean helper6(TreeNode root) {
        if (root == null) return true;
        if (!helper6(root.left)) return false;
        if (prev != null && root.val <= prev) return false;
        prev = root.val;
        if (!helper6(root.right)) return false;
        return true;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{2, 1, 3});
        log(isValidBST0(t1));
        /*
         * expects true.
         *       2
         *      / \
         *     1   3
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{3, 1, 5, 0, 2, 4, 6});
        log(isValidBST0(t2));
        /*
         * expects true.
         *         3
         *       /   \
         *      1     5
         *     / \   / \
         *    0   2 4   6
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{5, 1, 4, null, null, 3, 6});
        log(isValidBST0(t3));
        /*
         * expects false.
         *       5
         *      / \
         *     1   4
         *        / \
         *       3   6
         * */

        TreeNode t4 = createBinaryTreeBreadthFirst(new Integer[]{1, 1});
        log(isValidBST0(t4));
        /*
         * expects false. (等值节点的情况)
         *       1
         *      /
         *     1
         * */

        TreeNode t5 = createBinaryTreeBreadthFirst(new Integer[]{10, 5, 15, null, null, 6, 20});
        log(isValidBST0(t5));
        /*
         * expects false. (跨层级的情况：6 < 10)
         *      10
         *     /  \
         *    5   15
         *       /  \
         *      6   20
         * */
    }
}
