package BinaryTreeAndRecursion.S5_LowestCommonAncestor;

import Utils.Helpers.TreeNode;

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
     * 解法1：迭代
     * - 思路：先找到 successor 节点的规律：
     *   1. 若 p 有右子树，则其右子树上的最左节点就是 successor；
     *   2. 若 p 没有右子树，则：
     *      a). 若 p 是父节点的左子节点，则其父节点就是 successor；
     *      b). 若 p 是父节点的右子节点，则其祖先节点中上一个比 p 大的是其 successor（若没有则为 null）；
     * - 时间复杂度 O(logn)，在 BST 不平衡的情况下，最坏会退化成 O(n)。
     * - 空间复杂度 O(1)。
     * */
    private static TreeNode inorderSuccessor(TreeNode root, TreeNode p) {
        if (root == null || p == null) return null;
        if (p.right != null) return getMin(p.right);  // 若 p 有右子树，则其右子树上的最左节点就是 successor
        TreeNode successor = null;

        while (root != null) {
            if (p.val < root.val) {  // 若 p 在父节点的左子树上，则继续往左查找
                successor = root;    // 若最后 p 是父节点的左子节点，则其父节点就是 successor ∴ 要预先记录父节点
                root = root.left;
            } else if (p.val > root.val) {  // 若 p 在父节点的右子树上，则继续往右查找，但 ∵ 其 successor 不是父节点 ∴ 不用记录父节点
                root = root.right;
            } else {
                break;
            }
        }

        return successor;
    }

    private static TreeNode getMin(TreeNode root) {
        return root.left == null ? root : getMin(root.left);
    }

    /*
     * 解法2：递归
     * - 思路：？？
     * - 时间复杂度 O(logn)，空间复杂度 O(logn)，在 BST 不平衡的情况下，最坏会退化成 O(n)。
     * */
    private static TreeNode inorderSuccessor2(TreeNode root, TreeNode p) {
        if (root == null || p == null) return null;
        if (p.val < root.val) {
            TreeNode successor = inorderSuccessor2(root.left, p);
            return successor == null ? root : successor;
        }
        return inorderSuccessor2(root.right, p);
    }

    /*
     * 解法3：利用 BST 中序遍历的顺序性
     * - 思路：
     * - 时间复杂度 O(logn)，空间复杂度 O(logn)，在 BST 不平衡的情况下，最坏会退化成 O(n)。
     * */
    private static TreeNode inorderSuccessor3(TreeNode root, TreeNode p) {
        return null;
    }

    public static void main(String[] args) {
        TreeNode t = createBinaryTreeBreadthFirst(new Integer[]{6, 3, 7, 2, 4, null, null, 1, null, null, 5});
        TreeNode successor1 = inorderSuccessor2(t, t.get(2));
        log(successor1);                                        // expects 3
        TreeNode successor2 = inorderSuccessor2(t, t.get(4));
        log(successor2);                                        // expects 5
        TreeNode successor3 = inorderSuccessor2(t, t.get(5));
        log(successor3);                                        // expects 6
        TreeNode successor4 = inorderSuccessor2(t, t.get(6));
        log(successor4);                                        // expects 7
        TreeNode successor5 = inorderSuccessor2(t, t.get(7));
        log(successor5);                                        // expects null. (无 successor 的情况)
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
