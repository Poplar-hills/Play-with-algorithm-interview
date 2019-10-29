package BinaryTreeAndRecursion;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.log;

import Utils.Helpers.TreeNode;

/*
 * Serialize and Deserialize Binary Tree
 *
 * -
 * */

public class L297_SerializeAndDeserializeBinaryTree {
    /*
     * 解法1：
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static String serialize(TreeNode root) {
        return "";
    }

    public static TreeNode deserialize(String data) {
        return new TreeNode(0);
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, null, null, 4, 5});
        log(serialize(t1));
        /*
         * expects "[1,2,3,null,null,4,5]".
         * */
    }
}
