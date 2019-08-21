package DP.KnapsackProblem;

import static Utils.Helpers.*;

/*
* Partition Equal Subset Sum
*
* - Given a non-empty array containing only positive integers, find if the array can be partitioned into two
*   subsets such that the sum of elements in both subsets is equal.
*
* - 分析：该题是个经典的背包问题，只需稍稍转化一下即可看出本质 —— 要求将 n 个数字分成和相等的两部分，那么这两部分各自的和就一定都
*   是 sum/2，因此该题可以转化为：从 n 个物品中选出能填满 sum/2 容量的背包的物品。转化后的问题与0/1背包问题还稍有不同：
*   1. 不涉及物品价值，所以只有一个变量；
*   2. 背包需要完全填满，不允许有剩余空间。
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
        log(canPartition(new int[]{1, 5, 11, 5}));  // expects true. ([1, 5, 5] and [11])
        log(canPartition(new int[]{1, 2, 3, 4}));   // expects true. ([1, 4] and [2, 3])
        log(canPartition(new int[]{1, 2, 3, 5}));   // expects false
        log(canPartition(new int[]{1}));            // expects false
    }
}
