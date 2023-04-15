package Array.S4_TwoPointerCollision;

import java.util.HashMap;
import java.util.Map;

import static Utils.Helpers.log;

/*
 * Two Sum II - Input array is sorted
 *
 * - 在一个有序数组中找到两个不同元素之和等于给定的值，返回这两个元素各自的元素序号（从1开始）。
 *
 * - 💎 套路：条件中说了是数组是"有序"的，由此可联想：
 *   - 是否可使用二分查找？
 *   - 是否可使用分治、递归思路？
 *   - 是否可构建一棵搜索树？
 * */

public class L167_TwoSumII {
    /**
     * 超时解：双指针遍历（Brute force）
     * - 对于 [-3, -2, 2, 3], target=0 来说：
     *         l   r
     *         l      r
     *         l         r
     *             l  r
     *             l     r
     *                l  r    - 👉🏻从推演可知 l ∈ [0,n-1], r ∈ [1,n]
     * - 时间复杂度 O(n^2)，空间复杂度 O(1)。
     */
    public static int[] twoSum0(int[] nums, int target) {
        if (nums == null || nums.length < 2) return null;
        for (int l = 0; l < nums.length - 1; l++)      // 从推演可知 l ∈ [0,n-1]
            for (int r = l + 1; r < nums.length; r++)  // r ∈ [1,n]
                if (nums[l] + nums[r] == target)
                    return new int[]{l + 1, r + 1};
        return null;
    }

    /*
     * 解法1：二分查找
     * - 思路：将该问题转化为搜索问题，而要在有序数组内搜索某个值，二分查找是最快的 ∴ 只需在遍历数组的过程中不断在 [i+1..n] 范围内
     *   二分查找 target - nums[i]，直到找到解或遍历结束即可。
     * - 对于 [-5, -2, 1, 3, 4], target=5 来说：
     *         i               - nums[i]=-5 ∴ binary search target+5 within [1,n]
     *             i           - nums[i]=-2 ∴ binary search target+2 within [2,n]
     *                i        - nums[i]=0  ∴ binary search target-1 within [3,n], found solution [2,4]
     * - 👉🏻时间复杂度 O(nlogn)，空间复杂度 O(logn)。∵ 二分搜索要进行递归 ∴ 空间复杂度为 O(logn)。
     * */
    public static int[] twoSum(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            int p = binarySearch(nums, i + 1, nums.length - 1, complement);  // 在 nums(i..] 中进行查找
            if (p != -1)
                return new int[]{i + 1, p + 1};
        }
        return null;
    }

    private static int binarySearch(int[] nums, int l, int r, int target) {
        if (l > r) return -1;
        int mid = (r - l) / 2 + l;
        if (target < nums[mid]) return binarySearch(nums, l, mid - 1, target);
        if (target > nums[mid]) return binarySearch(nums, mid + 1, r, target);
        return mid;
    }

    /*
     * 解法2：指针对撞
     * - 思路：与 L11_ContainerWithMostWater 类似，可以采用指针对撞 —— 每次移动左右中的一个指针，具体移动哪个则由当前计算结果
     *   若当前计算结果 < 目标值，则左指针++来增大计算结果，否则右指针--来减小计算结果。又 ∵ nums 是有序的 ∴ 计算过程就是比较
     *   nums[l] + nums[r] 与 target 的大小。
     * - 对于 [ -5, -2, 1, 3, 4 ], target 4 来说：
     *          l            r   - nums[l] + nums[r] = -1 < target ∴ l++
     *              l        r   - nums[l] + nums[r] = -2 < target ∴ l++
     *                 l     r   - nums[l] + nums[r] = 5 > target ∴ r--
     *                 l  r      - nums[l] + nums[r] = 5 == target, found solution
     * - 注：两路、三路快排实际上也是指针对撞的一种应用。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int[] twoSum2(int[] nums, int target) {
        int l = 0, r = nums.length - 1;
        while (l != r) {
            int sum = nums[l] + nums[r];
            if (sum < target) l++;
            else if (sum > target) r--;
            else return new int[] {l + 1, r + 1};
        }
        return null;
    }

    /*
     * 解法3：查找表
     * - 思路：一遍遍历，一遍构建 index map，一遍查找当前元素的 complement。
     * - 对于 [-2, -2, 1, 3, 4], target=5 来说：
     *         i                - target-nums[i]=7, not in Map ∴ Map(-2:0)
     *             i            - target-nums[i]=7, not in Map ∴ Map(-2:1)
     *                i         - target-nums[i]=4, not in Map ∴ Map(-2:1, 1:2)
     *                   i      - target-nums[i]=2, not in Map ∴ Map(-2:1, 1:2, 3:3)
     *                      i   - target-nums[i]=1, found in Map ∴ return [map.get(1)+1, i+1]
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int[] twoSum3(int[] nums, int target) {
        Map<Integer, Integer> indexMap = new HashMap<>();  // Map<num, index>
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (indexMap.containsKey(complement))
                return new int[]{indexMap.get(complement) + 1, i + 1};
            indexMap.put(nums[i], i);
        }
        return null;
    }

    public static void main(String[] args) {
        log(twoSum3(new int[]{-5, -2, 1, 3, 4}, 5));  // expects [3, 5]（注意返回的是从1开始的元素序号）
        log(twoSum3(new int[]{2, 7, 11, 15}, 9));     // expects [1, 2]
        log(twoSum3(new int[]{2, 3, 4}, 6));          // expects [1, 3]
        log(twoSum3(new int[]{-3, -2, 2, 3}, 0));     // expects [1, 4] or [2, 3]
        log(twoSum3(new int[]{-1, 0}, -1));           // expects [1, 2]
        log(twoSum3(new int[]{1, 2, 3, 4}, -1));      // expects null
    }
}
