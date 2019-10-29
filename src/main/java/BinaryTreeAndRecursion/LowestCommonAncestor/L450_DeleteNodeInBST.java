package BinaryTreeAndRecursion.LowestCommonAncestor;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import Utils.Helpers.TreeNode;

/*
 * Delete Node in a BST
 *
 * - Given a root node of a BST, delete the node with the given key in the BST. Return the root node
 *   (possibly updated) of the BST.
 *
 * - Basically, the deletion can be divided into two stages:
 *   1. Search for a node to remove.
 *   2. If the node is found, delete the node.
 * */

public class L450_DeleteNodeInBST {
    /*
     * 解法1：
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static TreeNode deleteNode(TreeNode root, int key) {
        return null;
    }

    public static void main(String[] args) {
      TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{5, 3, 6, 2, 4, null, 7});
      log(deleteNode(t1, 7));
      /*
       * expects [5,4,6,2,null,null,7] or [5,2,6,null,4,null,7]
       *       5                                5                  5
       *      / \       After deleting         / \                / \
       *     3   6     --------------->       4   6      or      2   6
       *    / \   \       the node 3         /     \              \   \
       *   2   4   7                        2       7              4   7
       * */
    }
}
