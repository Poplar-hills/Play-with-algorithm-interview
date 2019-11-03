package BinaryTreeAndRecursion.LowestCommonAncestor;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import Utils.Helpers.TreeNode;

/*
 * Lowest Common Ancestor of a Binary Tree
 *
 * - Given a binary tree, find the lowest common ancestor (LCA) of two given nodes in the tree.
 *
 * - Note:
 *   1. All of the nodes' values will be unique.
 *   2. p and q are different and both values will exist in the binary tree.
 * */

public class L236_LCAOfBinaryTree {
    /*
     * 解法1：Recursion (DFS)
     * - 思路：在每次进入下层递归之前先通过 contains 方法确定 p、q 在哪个子树上。
     * - 时间复杂度 O(n^2)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null)
            return null;
        if (contains(root.left, p) && contains(root.left, q))
            return lowestCommonAncestor(root.left, p, q);
        if (contains(root.right, p) && contains(root.right, q))
            return lowestCommonAncestor(root.right, p, q);
        return root;
    }

    private static boolean contains(TreeNode root, TreeNode node) {  // O(n)
        if (root == null) return false;
        if (root == node) return true;
        return contains(root.left, node) || contains(root.right, node);
    }

    /*
     * 解法2：Recursion (DFS 后续遍历 + Backtracking)
     * - 思路：
     *   1. 本题是后续遍历的典型应用 —— 只有遍历过左、右子树后，才能确定当前节点是否符合条件；
     *   2. 使用回溯法 —— 先通过后续遍历到达叶子节点，然后在递归返回的路上，用左右子树的递归函数返回值来判断当前节点是否是 LCA 节点：
     *           3                        2        - 节点3的 sum=2 ∴ LCA 是3节点
     *         /   \      p=6, q=4      /   \                ↑
     *        5     4    --------->    1     1     - 节点4就是 q ∴ 返回1；节点5处的 sum=1 ∴ 返回1
     *       / \                      / \                    ↑
     *      6   2                    1   0         - 节点6就是 p ∴ 返回1
     *
     *           3                        1        - 注意节点5处的返回值是1而不能是2，否则节点3处的 sum 也会是2，从而将 lca 节点覆盖
     *         /   \      p=5, q=2      /   \                ↑
     *        5     4    --------->    2     0     - 节点5就是 p ∴ sum=2 ∴ 该节点就是 LCA 节点
     *       / \                      / \                    ↑
     *      6   2                    0   1         - 节点2就是 q ∴ 返回1
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    private static TreeNode lca = null;  // lca 节点的指针作为类成员变量

    public static TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {
        helper(root, p, q);
        return lca;
    }

    private static int helper(TreeNode node, TreeNode p, TreeNode q) {
        if (node == null) return 0;

        int left = helper(node.left, p, q);          // 先遍历左右子树
        int right = helper(node.right, p, q);
        int mid = (node == p || node == q) ? 1 : 0;  // 再访问当前节点

        int sum = left + right + mid;
        if (sum == 2) lca = node;
        return sum > 0 ? 1 : 0;
    }

    /*
     * 解法3：Iteration (DFS) + Map + Set
     * - 思路：非常有意思的思路！利用多种数据结构，思路见下面代码注释。
     * - 👉 总结 ：Step 2、3实际上就是“找到两个链表交叉点”（L160_IntersectionOfTwoLinkedLists 解法1）的应用。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static TreeNode lowestCommonAncestor3(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) return null;
        Stack<TreeNode> stack = new Stack<>();                // 用于存储 p 节点的所有祖先节点
        Map<TreeNode, TreeNode> parentMap = new HashMap<>();  // 用于存储<节点, 父节点>
        stack.push(root);
        parentMap.put(root, null);

        // Step 1: 建立 parentMap
        while (!parentMap.containsKey(p) || !parentMap.containsKey(q)) {  // 若 p、q 已经被收录进 Map 则说明他们的
            TreeNode node = stack.pop();                                  // 所有祖先节点也都已被收录进 Map 中了

            if (node.left != null) {
                parentMap.put(node.left, node);  // 收录节点
                stack.push(node.left);
            }
            if (node.right != null) {
                parentMap.put(node.right, node);  // 收录节点
                stack.push(node.right);
            }
        }

        // Step 2: 查出 p 的所有祖先节点并放入 Set
        Set<TreeNode> pParentSet = new HashSet<>();  // 当 Map 建立完毕后
        while (p != null) {
            pParentSet.add(p);
            p = parentMap.get(p);
        }

        // Step 3: 从下往上依次查询 q 的每一个祖先节点是否在 pParentSet 中，找到的第一个就是 LSC
        while (!pParentSet.contains(q))
            q = parentMap.get(q);

        return q;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{3, 5, 1, 6, 2, 0, 8, null, null, 7, 4});
        /*
         *           3
         *        /     \
         *       5       1
         *      / \     / \
         *     6   2   0   8
         *        / \
         *       7   4
         * */

        log(lowestCommonAncestor3(t1, t1.get(5), t1.get(1)));  // expects 3. (The LCA of nodes 5 and 1 is 3.)
        log(lowestCommonAncestor3(t1, t1.get(7), t1.get(0)));  // expects 3.
        log(lowestCommonAncestor3(t1, t1.get(5), t1.get(4)));  // expects 5.
        log(lowestCommonAncestor3(t1, t1.get(4), t1.get(6)));  // expects 5.
    }
}
