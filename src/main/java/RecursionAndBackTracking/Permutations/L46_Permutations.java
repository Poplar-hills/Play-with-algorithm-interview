package RecursionAndBackTracking.Permutations;

import static Utils.Helpers.*;

import java.util.List;

/*
 * Permutations
 *
 * - Given a collection of distinct integers, return all possible permutations.
 * */

public class L46_Permutations {
    /*
     * 解法1：
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static List<List<Integer>> permute(int[] nums) {
        return null;
    }

    public static void main(String[] args) {
        log(permute(new int[]{1, 2, 3}));  // expects [[1,2,3], [1,3,2], [2,1,3], [2,3,1], [3,1,2], [3,2,1]]
        log(permute(new int[]{1, 2}));     // expects [[1,2], [2,1]]
        log(permute(new int[]{1}));        // expects [[1]]
        log(permute(new int[]{}));         // expects []
    }
}
