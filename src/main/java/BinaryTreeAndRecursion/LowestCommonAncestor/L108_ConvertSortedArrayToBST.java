package BinaryTreeAndRecursion.LowestCommonAncestor;

import static Utils.Helpers.printBinaryTreeBreadthFirst;

import java.util.LinkedList;
import java.util.Queue;
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
     *   添加到 BST 上。
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
     * 解法2：Iteration
     * - 思路：解法1的迭代版，用 NodeCell 类来封装 <节点, 节点值下界, 节点值上界> 信息。
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
        Queue<NodeCell> q = new LinkedList<>();
        q.offer(rootCell);

        while (!q.isEmpty()) {
            NodeCell cell = q.poll();              // 得到栈顶 NodeCell，为其确定节点值
            int mid = (cell.upper - cell.lower) / 2 + cell.lower;
            cell.node.val = nums[mid];             // nums[mid] 即是该节点的真正节点值，替换原来的占位节点值

            if (cell.lower != mid) {               // 若 nums[mid] 左侧还有元素
                cell.node.left = new TreeNode(0);  // 创建左子占位节点（主要是先创建两节点之间的连接关系，节点值后面再覆盖）
                q.offer(new NodeCell(cell.node.left, cell.lower, mid - 1));
            }
            if (cell.upper != mid) {               // 若 nums[mid] 右侧还有元素
                cell.node.right = new TreeNode(0);
                q.offer(new NodeCell(cell.node.right, mid + 1, cell.upper));
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
