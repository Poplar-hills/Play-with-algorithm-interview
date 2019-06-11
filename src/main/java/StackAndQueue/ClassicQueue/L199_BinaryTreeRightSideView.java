package StackAndQueue.ClassicQueue;

import java.util.*;

import static Utils.Helpers.*;

/*
* Binary Tree Right Side View
*
* - Given a binary tree, imagine yourself standing on the right side of it, return the values of the nodes
*   you can see ordered from top to bottom. For example:
*         1            <---                1            <---
*       /   \                            /   \
*      2     3         <---             2     3         <---
*       \     \                          \
*        5     4       <---               5             <---
*   should return [1, 3, 4]          should return [1, 3, 5]
* */

public class L199_BinaryTreeRightSideView {
    public static List<Integer> rightSideView(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Queue<TreeNode> q = new LinkedList<>();
        if (root == null) return res;

        q.offer(root);
        while (!q.isEmpty()) {
            int size = q.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = q.poll();
                if (i == size - 1) res.add(node.val);
                if (node.left != null) q.offer(node.left);
                if (node.right != null) q.offer(node.right);
            }
        }

        return res;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, null, 5, null, 4});
        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, null, 5});
        log(rightSideView(t1));  // expects [1, 3, 4]
        log(rightSideView(t2));  // expects [1, 3, 5]
    }
}
