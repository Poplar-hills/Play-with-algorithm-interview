package BinaryTreeAndRecursion.S5_LowestCommonAncestor;

import static Utils.Helpers.printBinaryTreeBreadthFirst;

import java.util.Stack;

import Utils.Helpers.TreeNode;

/*
 * Convert Sorted Array to Binary Search Tree
 *
 * - Given an ascending array, convert it to a height-balanced BST.
 *
 * - For this problem, a height-balanced binary tree is defined as a binary tree in which the depth of the
 *   two subtrees of every node never differ by more than 1.
 * */

public class L108_ConvertSortedArrayToBST {
    /*
     * 解法1：DFS + Binary search（Recursion）
     * - 思路：∵ BST 中的节点天生就是以二分的形式排布的 ∴ 从有序数组建立 BST 的过程实际上就是对数组不断进行二分查找的过程：
     *       [-6, -4, -2, 0, 1, 3, 5]
     *                    ⌾               - 先找到数组中央元素 mid，作为根节点
     *             ⌾            ⌾         - 再从分别从 [0..mid)、(mid..n] 中找到根节点的左、右子节点
     *         ⌾       ⌾     ⌾     ⌾      - 以此类推
     * - 时间复杂度 O(n)，空间复杂度 O(logn)。
     * */
    public static TreeNode sortedArrayToBST(int[] nums) {
        if (nums == null || nums.length == 0) return null;
        return helper(nums, 0, nums.length - 1);
    }

    private static TreeNode helper(int[] nums, int l, int r) {
        if (l > r) return null;     // l == r 的情况下面已经涵盖了 ∴ 这里只讨论 l > r 的情况即可
        int mid = (r - l) / 2 + l;
        TreeNode node = new TreeNode(nums[mid]);
        node.left = helper(nums, l, mid - 1);
        node.right = helper(nums, mid + 1, r);
        return node;
    }

    /*
     * 解法2：DFS + Binary search（解法1的迭代版）
     * - 思路：与解法1一致。
     * - 实现：
     *   1. 创建 NodeCell 类来封装 <节点, 节点值下界, 节点值上界>，∴ 一个 NodeCell 代表了一个节点以及其取值范围；
     *   2. 前序遍历待生成的 BST 上的每一个节点位置：
     *      a). 出栈该位置上的 NodeCell，并根据其中取值范围计算出节点值，形成完整的节点；
     *      b). 通过对 nums 二分来获得该节点的子节点的取值范围，生成 NodeCell 并入栈。
     *   - 将 Stack 换成 Queue 即是 BFS 的实现。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    private static class NodeCell {
        TreeNode node;
        int l, r;

        public NodeCell(TreeNode node, int l, int r) {
            this.l = l;
            this.r = r;
            this.node = node;
        }
    }

    public static TreeNode sortedArrayToBST2(int[] nums) {
        if (nums == null || nums.length == 0) return null;
        Stack<NodeCell> stack = new Stack<>();
        TreeNode root = new TreeNode(0);                             // 先生成一个占位节点
        NodeCell rootCell = new NodeCell(root, 0, nums.length - 1);  // 封装 NodeCell
        stack.push(rootCell);

        while (!stack.isEmpty()) {
            NodeCell cell = stack.pop();           // 为出栈的 NodeCell 更新节点值
            int mid = (cell.r - cell.l) / 2 + cell.l;
            cell.node.val = nums[mid];             // 在访问节点时计算该节点的真正节点值，并替换原来的占位节点值

            if (cell.l != mid) {                   // 若 nums[mid] 左侧还有元素
                cell.node.left = new TreeNode(0);  // 创建左子占位节点（这里先创建两节点之间的连接关系，节点值会在后面出栈时再更新）
                stack.push(new NodeCell(cell.node.left, cell.l, mid - 1));
            }
            if (cell.r != mid) {                   // 若 nums[mid] 右侧还有元素
                cell.node.right = new TreeNode(0);
                stack.push(new NodeCell(cell.node.right, mid + 1, cell.r));
            }
        }

        return root;
    }

    public static void main(String[] args) {
        TreeNode t1 = sortedArrayToBST(new int[]{-6, -4, -2, 0, 1, 3, 5});
        printBinaryTreeBreadthFirst(t1);
        /*
         * expects [0,-4,3,-6,-2,1,5] or [-2,-4,3,-6,null,1,5,null,null,0] etc. (there are more valid solutions).
         *                  0                           -2
         *                /   \                        /  \
         *              -4     3                     -4    3
         *              / \   / \                    /    / \
         *            -6  -2 1   5                 -6    1   5
         *                                              /
         *                                             0
         * */

        TreeNode t2 = sortedArrayToBST(new int[]{-10, -3, 0, 5, 9});
        printBinaryTreeBreadthFirst(t2);
        /*
         * expects [0,-10,5,null,-3,null,9] or [0,-3,5,-10,null,null,9] etc. (there are more valid solutions).
         *                     0                           0
         *                   /   \                       /   \
         *                 -10    5                    -3     5
         *                   \     \                   /       \
         *                   -3     9                -10        9
         * */
    }
}
