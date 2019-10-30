package BinaryTreeAndRecursion.LowestCommonAncestor;

import static Utils.Helpers.printBinaryTreeBreadthFirst;

import java.util.Stack;

import Utils.Helpers.TreeNode;

/*
 * Delete Node in a BST
 *
 * - Given an array where elements are sorted in ascending order, convert it to a height-balanced BST.
 *
 * - For this problem, a height-balanced binary tree is defined as a binary tree in which the depth of the
 *   two subtrees of every node never differ by more than 1.
 * */

public class L108_ConvertSortedArrayToBST {
    /*
     * 解法1：Recursion + 二分查找
     * - 思路：∵ BST 中的元素天生就是以二分的形式排列的 ∴ 从有序数组建立 BST 的过程其实就是对数组不断进行二分查找，然后顺序
     *   添加到 BST 上，将该过程可视化出来：
     *      [-6, -4, -2, 0, 1, 3, 5]
     *                   ↑               - 数组中央节点就是根节点
     *            ↑            ↑         - 再从中央节点左右两边的范围中找到根节点的左右子节点
     *        ↑       ↑     ↑     ↑      - 以此类推
     * - 时间复杂度 O(n)，空间复杂度 O(logn)。
     * */
    public static TreeNode sortedArrayToBST(int[] nums) {
        if (nums == null || nums.length == 0) return null;
        return helper(nums, 0, nums.length - 1);
    }

    private static TreeNode helper(int[] nums, int l, int r) {
        if (l > r) return null;
        int mid = (r - l) / 2 + l;
        TreeNode node = new TreeNode(nums[mid]);
        node.left = helper(nums, l, mid - 1);
        node.right = helper(nums, mid + 1, r);
        return node;
    }

    /*
     * 解法2：Iteration（解法1的迭代版）
     * - 思路：1. 创建 NodeCell 类来封装 <节点, 节点值下界, 节点值上界> 信息，一个 NodeCell 代表了一个节点以及其取值范围；
     *        2. 使用 DFS 来遍历要生成的 BST 上的每一个节点位置：
     *           a). 出栈该位置上的 NodeCell，并根据其中取值范围计算出节点值，形成完整的节点。
     *           b). 通过对 nums 二分来获得该节点的子节点的取值范围，生成 NodeCell 并入栈；
     * - 实现：该过程使用 DFS（Stack）或 BFS（Queue）是一样的。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    private static class NodeCell {
        TreeNode node;
        int lower, upper;
        public NodeCell(TreeNode node, int lower, int upper) {
            this.node = node;
            this.lower = lower;
            this.upper = upper;
        }
    }

    public static TreeNode sortedArrayToBST2(int[] nums) {
        if (nums == null || nums.length == 0) return null;
        TreeNode root = new TreeNode(0);           // 先生成一个占位节点
        NodeCell rootCell = new NodeCell(root, 0, nums.length - 1);
        Stack<NodeCell> stack = new Stack<>();
        stack.push(rootCell);

        while (!stack.isEmpty()) {
            NodeCell cell = stack.pop();           // 得到栈顶 NodeCell，为其确定节点值
            int mid = (cell.upper - cell.lower) / 2 + cell.lower;
            cell.node.val = nums[mid];             // nums[mid] 即是该节点的真正节点值，替换原来的占位节点值

            if (cell.lower != mid) {               // 若 nums[mid] 左侧还有元素
                cell.node.left = new TreeNode(0);  // 创建左子占位节点（主要是先创建两节点之间的连接关系，节点值后面再覆盖）
                stack.push(new NodeCell(cell.node.left, cell.lower, mid - 1));
            }
            if (cell.upper != mid) {               // 若 nums[mid] 右侧还有元素
                cell.node.right = new TreeNode(0);
                stack.push(new NodeCell(cell.node.right, mid + 1, cell.upper));
            }
        }

        return root;
    }

    public static void main(String[] args) {
      int[] arr1 = new int[]{-6, -4, -2, 0, 1, 3, 5};
      printBinaryTreeBreadthFirst(sortedArrayToBST2(arr1));
      /*
       * expects [0,-4,3,-6,-2,1,5] or [-2,-4,3,-6,null,1,5,null,null,0] or others.
       *                  0                           -2
       *                /   \                        /  \
       *              -4     3                     -4    3
       *              / \   / \                    /    / \
       *            -6  -2 1   5                 -6    1   5
       *                                              /
       *                                             0
       * */
    }
}
