package BinaryTreeAndRecursion.S5_LowestCommonAncestor;

import Utils.Helpers.TreeNode;

import java.util.ArrayList;
import java.util.List;

import static Utils.Helpers.*;

/*
 * Inorder Successor in BST
 *
 * - Given a binary search tree and a node in it, find the in-order successor of that node in the BST.
 *   The successor of a node p is the node with the smallest key greater than p.val.
 *
 * - Note:
 *   1. If the given node has no in-order successor in the tree, return null.
 *   2. It's guaranteed that the values of the tree are unique.
 * */


public class L285_InorderSuccessorInBST {
    /*
     * 解法1：利用 BST 中序遍历的顺序性
     * - 思路：先收集所有节点，然后再遍历查找 p 的下一个节点。
     * - 时间复杂度 O(n)，空间复杂度 O(logn)，在 BST 不平衡的情况下，最坏会退化成 O(n)。
     * */
    private static TreeNode inorderSuccessor(TreeNode root, TreeNode p) {
        // 1. Collect nodes via inorder traversal
        List<TreeNode> list = new ArrayList<>();
        dfs(root, p, list);

        // 2. Search for the next node of p
        return binarySearchNext(list, p, 0, list.size() - 1);
    }

    private static void dfs(TreeNode root, TreeNode p, List<TreeNode> list) {
        if (root == null) return;
        dfs(root.left, p, list);
        list.add(root);
        dfs(root.right, p, list);
    }

    private static TreeNode binarySearchNext(List<TreeNode> list, TreeNode p, int l, int r) {
        if (l > r) return null;
        int mid = (r - l) / 2 + l;
        int midVal = list.get(mid).val;
        if (p.val < midVal) return binarySearchNext(list, p, l, mid - 1);
        if (p.val > midVal) return binarySearchNext(list, p,mid + 1, r);
        return mid == list.size() - 1 ? null : list.get(mid + 1);  // 若 p 是 list 中的最后一个元素，则无 successor
    }

    /*
     * 解法2：迭代
     * - 思路：先找到 successor 节点的规律：
     *   1. 若 p 有右子树，则其 successor 是其右子树上的最左节点；
     *   2. 若 p 没有右子树，则其 successor 是其上游节点中第一个比 p 大的节点（若没有则为 null）。
     *   ∴ 若 p 有右子树，则搜索右子树上的最左节点；若无右子树，则从 root 开始从上到下搜索最后一个 > p 的节点。
     * - 时间复杂度 O(logn)，在 BST 不平衡的情况下，最坏会退化成 O(n)。
     * - 空间复杂度 O(1)。
     * */
    private static TreeNode inorderSuccessor2(TreeNode root, TreeNode p) {
        if (root == null || p == null) return null;

        // 1. 若 p 有右子树，则其右子树上的最左节点就是 successor
        if (p.right != null) return getMin(p.right);

        // 2. 若 p 无右子树，则从 root 开始从上到下搜索最后一个 > p 的节点
        TreeNode successor = null;
        while (root != null && p.val < root.val) {  // 若当前节点 > p，则记录该节点，并继续往左查找
            successor = root;
            root = root.left;
        }

        return successor;
    }

    private static TreeNode getMin(TreeNode root) {
        return root.left == null ? root : getMin(root.left);
    }

    /*
     * 解法3：解法2的简化版
     * - 思路：与解法2一致。
     * - 实现：解法2中的两个分支逻辑可以合二为一。
     * - 时间复杂度 O(logn)，空间复杂度 O(logn)，在 BST 不平衡的情况下，最坏会退化成 O(n)。
     * */
    private static TreeNode inorderSuccessor3(TreeNode root, TreeNode p) {
        if (root == null || p == null) return null;

        TreeNode successor = null;
        while (root != null) {
            if (p.val < root.val) {  // 若 p < 当前节点，则向左查找，并记录 successor（即解法2中的 p 无右子树的情况）
                successor = root;
                root = root.left;
            } else {                 // 当 p >= 当前节点时，再再右子树上查找最左节点（即解法2中的 p 有右子树的情况）
                root = root.right;
            }
        }
        return successor;
    }

    /*
     * 解法4：解法3的递归版
     * - 思路：与解法2、3一致。
     * - 时间复杂度 O(logn)，空间复杂度 O(logn)，在 BST 不平衡的情况下，最坏会退化成 O(n)。
     * */
    private static TreeNode inorderSuccessor4(TreeNode root, TreeNode p) {
        if (root == null || p == null) return null;
        if (p.val < root.val) {
            TreeNode successor = inorderSuccessor4(root.left, p);
            return successor == null ? root : successor;
        }
        return inorderSuccessor4(root.right, p);
    }

    public static void main(String[] args) {
        TreeNode t = createBinaryTreeBreadthFirst(new Integer[]{6, 3, 7, 2, 4, null, null, 1, null, null, 5});
        log(inorderSuccessor2(t, t.get(2)));  // expects 3
        log(inorderSuccessor2(t, t.get(3)));  // expects 4
        log(inorderSuccessor2(t, t.get(4)));  // expects 5
        log(inorderSuccessor2(t, t.get(5)));  // expects 6
        log(inorderSuccessor2(t, t.get(6)));  // expects 7
        log(inorderSuccessor2(t, t.get(7)));  // expects null. (无 successor 的情况)
        /*
         *           6
         *         /   \
         *        3     7
         *      /  \
         *     2   4
         *   /      \
         *  1        5
         * */
    }
}
