package DP.KnapsackProblem;

import static Utils.Helpers.*;

/*
* Partition Equal Subset Sum
*
* - Given a non-empty array containing only positive integers, find if the array can be partitioned into two
*   subsets such that the sum of elements in both subsets is equal.
* */

public class L416_PartitionEqualSubsetSum {
    /*
    * 解法1：
    * - 思路：
    * - 时间复杂度 O()，空间复杂度 O()。
    * */
    public static boolean canPartition(int[] nums) {
        return true;
    }

    public static void main(String[] args) {
        log(canPartition(new int[]{1, 5, 11, 5}));  // expects true. (can be partitioned as [1, 5, 5] and [11])
        log(canPartition(new int[]{1, 2, 3, 5}));   // expects false. (cannot be partitioned into equal sum subsets)
    }
}
