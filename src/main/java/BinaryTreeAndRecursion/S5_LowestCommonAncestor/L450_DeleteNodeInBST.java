package BinaryTreeAndRecursion.S5_LowestCommonAncestor;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.printBinaryTreeBreadthFirst;

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
     * 解法1：Hibbard Deletion (Recursion)
     * - 思路：与 Play-with-data-structure/BST/BST.java 中的 remove 方法一致，总的来说是：
     *   1. 先借助 BST 的二分性质找到 val == key 的目标节点 node；
     *   2. 从以 node 为根的 BST 的右子树中找到最小节点 successor；
     *      - 若 node 左子树为空，则右子树就是 successor；
     *      - 若 node 右子树为空，则左子树就是 successor；
     *      - 若 node 左右子树都不为空，则右子树中的最小节点就是 successor；
     *   3. 将 node 的左右子树移植到 successor 上：
     *      - 将 node 右子树去掉 successor 节点后的部分移植到 successor 的右子树上；
     *      - 将 node 左子树移植到 successor 的左子树上；
     *   4. 让 successor 接替 node 并放回到原 BST 的相应位置上。
     * - 时间复杂度 O(logn)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static TreeNode deleteNode(TreeNode root, int key) {
        if (root == null) return null;
        if (key < root.val)
            root.left = deleteNode(root.left, key);
        else if (key > root.val)
            root.right = deleteNode(root.right, key);
        else
            root = deleteRoot(root);
        return root;
    }

    private static TreeNode deleteRoot(TreeNode root) {  // 从以 root 为根的 BST 中删除根节点，并返回以 successor 为根的 BST
        if (root.left == null) return root.right;  // 若左子树为空，则右子树就是 successor
        if (root.right == null) return root.left;  // 若右子树为空，则左子树就是 successor
        TreeNode successor = getMin(root.right);   // 若都不为空，则右子树中的最小节点就是 successor
        successor.right = removeMin(root.right);   // 注意要先给右子树赋值（SEE: https://coding.imooc.com/learn/questiondetail/84029.html）
        successor.left = root.left;
        return successor;
    }

    private static TreeNode getMin(TreeNode node) {
        return (node.left == null) ? node : getMin(node.left);
    }

    private static TreeNode removeMin(TreeNode node) {
        if (node.left == null) return node.right;  // 找到最小节点，并用其右子节点代替（即使右子节点是 null）
        node.left = removeMin(node.left);
        return node;
    }

    /*
     * 解法2：Hibbard Deletion (解法1的非递归版)
     * - 思路：总体思路是：1. 先找到以待删除节点为根的子树； 2. 删除其父节点。具体移动过程比较复杂，要画图来辅助思考。
     * - 👉 总结：二叉树操作的非递归实现通常都需要拿到：1. 待操作节点；2. 待操作节点的父节点。
     * - 时间复杂度 O(logn)，空间复杂度 O(1)。
     * */
    public static TreeNode deleteNode2(TreeNode root, int key) {
        TreeNode prev = null, curr = root;

        while (curr != null && curr.val != key) {  // 找到待删除节点及其父节点
            prev = curr;
            curr = key < curr.val ? curr.left : curr.right;
        }

        if (prev == null) return deleteRoot2(curr);            // 待删除节点就是二叉树的根节点的情况
        if (curr == prev.left) prev.left = deleteRoot2(curr);  // 待删除节点是 prev.left 的情况
        else prev.right = deleteRoot2(curr);                   // 待删除节点是 prev.right 的情况
        return root;
    }

    private static TreeNode deleteRoot2(TreeNode root) {  // 从以 root 为根的二叉树中删除根节点，并返回以 succssor 为根的二叉树
        if (root == null) return null;
        if (root.left == null) return root.right;
        if (root.right == null) return root.left;

        TreeNode prev = null, succ = root.right;  // 若左右子树都有，则找到右子树中的最小节点（successor）及其父节点
        while (succ.left != null) {
            prev = succ;
            succ = succ.left;
        }

        succ.left = root.left;                // 用 successor 接替根节点的第一步是移植根节点的左子树
        if (succ == root.right) return succ;  // 若 successor 就是根节点的右子树（再没有左子树）则直接返回
        prev.left = succ.right;               // 在移动 successor 之前要保留其右子树（移动到父节点上，接替 successor 的位置）
        succ.right = root.right;              // 再让 successor 接替根节点
        return succ;                          // 返回新的根节点
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{5, 3, 6, 2, null, null, 7});
        printBinaryTreeBreadthFirst(deleteNode(t1, 3));
        /*
         * expects [5,2,6,null,null,null,7]
         *       5                  5
         *      / \                / \
         *     3   6      -->     2   6
         *    /     \                  \
         *   2       7                  7
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{5, 3, 6, null, 4, null, 7});
        printBinaryTreeBreadthFirst(deleteNode(t2, 3));
        /*
         * expects [5,4,6,null,null,null,7]
         *       5                  5
         *      / \                / \
         *     3   6      -->     4   6
         *      \   \                  \
         *       4   7                  7
         * */

        TreeNode t3 = createBinaryTreeBreadthFirst(new Integer[]{7, 3, 8, 1, 5, null, 9, 0, 2, null, 6});
        printBinaryTreeBreadthFirst(deleteNode(t3, 3));
        /*
         * expects [7,5,8,1,6,null,9,0,2] or [7,2,8,1,5,null,9,0,null,null,6]
         *          7                   7                    7
         *         / \                 / \                  / \
         *        3   8               5   8                2   8
         *       / \   \     -->     / \   \      or      / \   \
         *      1   5   9           1   6   9            1   5   9
         *     / \   \             / \                  /     \
         *    0   2   6           0   2                0       6
         * */
    }
}
