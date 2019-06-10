package StackAndQueue.ClassicQueue;

import java.util.*;

import static Utils.Helpers.*;

/*
* Binary Tree Right Side View
*
* - Given a binary tree, imagine yourself standing on the right side of it, return the values of the nodes
*   you can see ordered from top to bottom. For example:
*         1            <---
*       /   \
*      2     3         <---
*       \     \
*        5     4       <---
*   should return [1, 3, 4]
* */

public class L199_BinaryTreeRightSideView {
    public static List<Integer> rightSideView(TreeNode root) {
        return new ArrayList<>();
    }

    public static void main(String[] args) {
        TreeNode t = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, null, 5, null, 4});
        log(rightSideView(t));  // expects [1, 3, 4]
    }
}
