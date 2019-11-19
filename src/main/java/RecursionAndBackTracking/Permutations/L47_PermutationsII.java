package RecursionAndBackTracking.Permutations;

import static Utils.Helpers.*;

import java.util.List;

/*
 * Permutations II
 *
 * - Given a collection of distinct integers, return all possible permutations (求 n 个数字的全排列).
 * */

public class L47_PermutationsII {
    /*
     * 解法1：
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static List<List<Integer>> permuteUnique(int[] nums) {
        return null;
    }

    public static void main(String[] args) {
        log(permuteUnique(new int[]{1, 1, 2}));  // expects [[1,1,2], [1,2,1], [2,1,1]]
        log(permuteUnique(new int[]{1, 2}));     // expects [[1,2], [2,1]]
        log(permuteUnique(new int[]{1}));        // expects [[1]]
        log(permuteUnique(new int[]{}));         // expects []
    }
}
