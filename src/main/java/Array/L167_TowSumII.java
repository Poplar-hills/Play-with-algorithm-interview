package Array;

import static Utils.Helpers.log;

/*
* Two Sum II - Input array is sorted
*
* */

public class L167_TowSumII {
    public static int[] twoSum(int[] nums, int target) {

    }

    public static void main(String[] args) {
        int[] arr1 = new int[] {2, 7, 11, 15};
        int[] arr2 = new int[] {3, 2, 4};
        log(twoSum(arr1, 9));  // expects [0, 1]
        log(twoSum(arr2, 6));  // expects [1, 2]（注意不能是重复元素相加）
    }
}
