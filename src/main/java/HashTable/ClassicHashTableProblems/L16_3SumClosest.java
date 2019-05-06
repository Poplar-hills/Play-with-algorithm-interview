package HashTable.ClassicHashTableProblems;

import java.util.Arrays;

import static Utils.Helpers.log;

/*
* 3Sum Closest
*
* 从数组中找出三个元素 a, b, c，使得他们的和 sum 最接近给定的 target，返回 sum。
* */

public class L16_3SumClosest {
    /*
    * 解法1：Brute force
    * - 时间复杂度 O(n^3)，空间复杂度 O(1)。
    * */
    public static int threeSumClosest(int[] nums, int target) {
        int minSum = nums[0] + nums[1] + nums[2];
        for (int i = 0; i < nums.length - 2; i++) {
            for (int j = i + 1; j < nums.length - 1; j++) {
                for (int k = j + 1; k < nums.length; k++) {
                    int sum = nums[i] + nums[j] + nums[k];
                    if (Math.abs(sum - target) < Math.abs(minSum - target))
                        minSum = sum;
                }
            }
        }
        return minSum;
    }

    /*
     * 解法2：
     * - 时间复杂度 O(n^2)，空间复杂度 O(1)。
     * */
    public static int threeSumClosest2(int[] nums, int target) {
        if (nums == null || nums.length < 3) return -1;
        Arrays.sort(nums);
        int minSum = nums[0] + nums[1] + nums[2];

        for (int i = 0; i < nums.length - 2; i++) {
            int l = i + 1, r = nums.length - 1;
            while (l < r) {
                int sum = nums[i] + nums[l] + nums[r];
                if (Math.abs(sum - target) < Math.abs(minSum - target))
                    minSum = sum;
                if (sum < target) l++;
                else if (sum > target) r--;
                else return sum;
            }
        }

        return minSum;
    }

    public static void main(String[] args) {
        log(threeSumClosest2(new int[] {-1, 2, 1, -4}, 1));  // expects 2
        log(threeSumClosest2(new int[] {0, 1, 2}, 0));       // expects 3
    }
}
