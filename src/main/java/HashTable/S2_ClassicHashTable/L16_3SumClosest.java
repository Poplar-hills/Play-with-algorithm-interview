package HashTable.S2_ClassicHashTable;

import java.util.Arrays;

import static Utils.Helpers.log;

/*
 * 3Sum Closest
 *
 * - 从数组 nums 中找出三个元素 a, b, c，使得他们的和 sum 最接近给定的 target，最后返回 sum。
 * */

public class L16_3SumClosest {
    /*
     * 解法1：Brute force
     * - 时间复杂度 O(n^3)，空间复杂度 O(1)。
     * */
    public static int threeSumClosest(int[] nums, int target) {
        int closest = nums[0] + nums[1] + nums[2];
        int n = nums.length;

        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 1; j < n - 1; j++) {
                for (int k = j + 1; k < n; k++) {
                    int sum = nums[i] + nums[j] + nums[k];
                    if (Math.abs(sum - target) < Math.abs(closest - target))
                        closest = sum;
                }
            }
        }
        return closest;
    }

    /*
     * 解法2：转化为 2Sum（指针对撞）
     * - 💎 思路：这道题本质上与 L15_3Sum 是类似的，只是判断条件由等于 target 变为了最接近 target ∴ 同样可以用 3Sum 的思路
     *   求解 —— 通过在遍历中挨个固定元素，将 3Sum 化简为 2Sum 问题。而一点问题变成了 2Sum，那么久可以考虑 2Sum 的三种经典
     *   思路：1. 指针对撞；2. Binary Search；3. 查找表。但由于本题条件是最接近 target，而非等于 target ∴ 无法使用
     *   Binary Search、查找表这样进行精确搜索的办法，只能使用指针对撞来对逼近 target，并沿途判断哪个是最接近的解。
     * - 💎 经验：该题很好的说明了在什么场景下不适合使用查找表的题目 —— 查找表是用来进行精确匹配的，不适用于这种搜索 closest 的场景。
     * - 时间复杂度 O(n^2)，空间复杂度 O(1)。
     * */
    public static int threeSumClosest2(int[] nums, int target) {
        if (nums == null || nums.length < 3) return -1;

        Arrays.sort(nums);
        int closest = nums[0] + nums[1] + nums[2];
        int n = nums.length;

        for (int i = 0; i < n - 2; i++) {  // 固定一个元素将问题化简为 2Sum，之后在 (i,..] 范围内做指针对撞
            int l = i + 1, r = n - 1;
            while (l < r) {
                int sum = nums[i] + nums[l] + nums[r];
                if (Math.abs(sum - target) < Math.abs(closest - target))
                    closest = sum;
                if (sum < target) l++;
                else if (sum > target) r--;
                else return sum;
            }
        }

        return closest;
    }

    public static void main(String[] args) {
        log(threeSumClosest2(new int[]{-1, 2, 1, -4}, 1));  // expects 2. (-1 + 2 + 1)
        log(threeSumClosest2(new int[]{0, 1, 2}, 0));       // expects 3. (0 + 1 + 2)
    }
}
