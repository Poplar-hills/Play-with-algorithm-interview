package RecursionAndBackTracking.Combinations;

import static Utils.Helpers.*;

import java.util.List;

/*
 * Subsets
 *
 * - Given a set of distinct integers, nums, return all possible subsets (the power set).
 *
 * - Note: The solution set must not contain duplicate subsets.
 * */

public class L78_Subsets {
    /*
     * 解法1：Recursion + Backtracking
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static List<List<Integer>> subsets(int[] nums) {
        return null;
    }

    public static void main(String[] args) {
        log(subsets(new int[]{1, 2, 3}));  // expects [[3], [1], [2], [1,2,3], [1,3], [2,3], [1,2], []]
    }
}
