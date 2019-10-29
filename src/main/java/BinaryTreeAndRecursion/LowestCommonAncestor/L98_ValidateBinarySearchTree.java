package BinaryTreeAndRecursion.LowestCommonAncestor;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import java.util.HashSet;
import java.util.Set;
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
     * 解法1：Recursion
     * - 思路：根据题中给出的 BST 设计递归函数：
     *     1. 父节点 > 左子树中的所有节点；
     *     2. 父节点 < 右子树中的所有节点；
     *     3. 左、右子树都是 BST。
     *   因此递归函数可以定义为：检查当前节点是否 > 其左子树上的所有节点，且 < 其右子树上的所有节点。
     * - 时间复杂度 O(nlogn)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static boolean isValidBST(TreeNode root) {
        if (root == null) return true;
        if (!isMaxInBST(root, root.left) || !isMinInBST(root, root.right)) return false;
        return isValidBST(root.left) && isValidBST(root.right);
    }

    private static boolean isMaxInBST(TreeNode node, TreeNode root) {  // 检查 node 是否 > root 上的所有节点
        if (root == null) return true;
        if (node.val <= root.val) return false;
        return isMaxInBST(node, root.right);  // 若 node > 右子树上的所有节点则说明 node > root 上的所有节点
    }                                         // （右子树是否是 BST 交给 isValidBST 去检查）

    private static boolean isMinInBST(TreeNode node, TreeNode root) {  // 检查 node 是否 < root 上的所有节点
        if (root == null) return true;
        if (node.val >= root.val) return false;
        return isMinInBST(node, root.left);  // 若 node < 左子树上的所有节点则说明 node < root 上的所有节点
    }                                        // （左子树是否是 BST 交给 isValidBST 去检查）

    /*
     * 解法2：Recursion
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
    public static boolean isValidBST2(TreeNode root) {
        return withinBounds(root, null, null);
    }

    private static boolean withinBounds(TreeNode root, Integer lower, Integer upper) {
        if (root == null) return true;
        if (lower != null && root.val <= lower) return false;
        if (upper != null && root.val >= upper) return false;
        return withinBounds(root.left, lower, root.val)    // 父节点的下界也是左子节点的下界，父节点本身是左子节点的上界
            && withinBounds(root.right, root.val, upper);  // 父节点的上界也是右子节点的上界，父节点本身是右子节点的下界
    }

    /*
     * 解法3：Iteration (DFS) (解法2的迭代版)
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean isValidBST3(TreeNode root) {
        if (root == null) return true;
        Stack<TreeNode> stack = new Stack<>();
        Stack<Integer> lowers = new Stack<>();
        Stack<Integer> uppers = new Stack<>();
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
                stack.push(node.left);  // 可以将这三个操作抽成方法，但前提是需要将 stack, lowers, uppers 作为类成员变量
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
     * 解法4：Iteration (中续遍历)
     * - 思路：检查一棵树是否是 BST 可以利用 BST 最特别的性质 —— BST 中序遍历的顺序是从小到大。因此该题只需在中序遍历的基础上
     *   检查每次后一个访问的节点是否 > 前一个访问的节点即可。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean isValidBST4(TreeNode root) {
        if (root == null) return true;
        Integer prev = null;
        Stack<TreeNode> stack = new Stack<>();
        Set<TreeNode> set = new HashSet<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            while (stack.peek().left != null && !set.contains(stack.peek().left))
                stack.push(stack.peek().left);                   // 向左走到最左，一路入栈没访问过的节点

            TreeNode node = stack.pop();
            if (prev != null && node.val <= prev) return false;  // 访问栈顶节点
            prev = node.val;
            set.add(node);                                       // 访问完后将其加入 set
            if (node.right != null) stack.push(node.right);      // 入栈右子节点
        }

        return true;
    }

    /*
     * 解法5：Iteration (中续遍历的另一种实现)
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean isValidBST5(TreeNode root) {
        if (root == null) return true;
        Stack<TreeNode> stack = new Stack<>();
        Integer prev = null;
        TreeNode curr = root;

        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {  // 先向左走到最左，一路入栈节点
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.pop();     // 访问栈顶节点
            if (prev != null && curr.val <= prev) return false;
            prev = curr.val;
            curr = curr.right;      // 转向右子节点，重新开始循环
        }

        return true;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{2, 1, 3});
        log(isValidBST5(t1));
        /*
         * expects true.
         *       2
         *      / \
         *     1   3
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{3, 1, 5, 0, 2, 4, 6});
        log(isValidBST5(t2));
        /*
         * expects true.
         *       3
         *     /   \
         *    1     5
         *   / \   / \
         *  0   2 4   6
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{5, 1, 4, null, null, 3, 6});
        log(isValidBST5(t3));
        /*
         * expects false.
         *       5
         *      / \
         *     1   4
         *        / \
         *       3   6
         * */

        TreeNode t4 = createBinaryTreeBreadthFirst(new Integer[]{1, 1});
        log(isValidBST5(t4));
        /*
         * expects false. (等值节点的情况)
         *       1
         *      /
         *     1
         * */

        TreeNode t5 = createBinaryTreeBreadthFirst(new Integer[]{10, 5, 15, null, null, 6, 20});
        log(isValidBST5(t5));
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
