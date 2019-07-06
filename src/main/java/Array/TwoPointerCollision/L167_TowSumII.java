package Array.TwoPointerCollision;

import static Utils.Helpers.log;

/*
* Two Sum II - Input array is sorted
*
* - 在一个有序数组中找到两个不同元素之和等于给定的值，返回这两个元素各自的元素序号（从1开始）。
*
* - 提示：条件中说了是数组是"序数"的，因此联想：
*   - 是否应用分治、递归思路？
*   - 是否可以使用二分查找？
*   - 是否可以构建一棵搜索树？
* */

public class L167_TowSumII {
    /*
    * 解法1：二分查找
    * - 思路：遍历数组，对于每个元素 nums[i]，若数组中包含值为 target - nums[i] 的元素，则说明有解，返回它们的序号即可。而要查找
    *   有序数组中是否包含某个值，二分查找是最快的。因此只要在遍历数组的过程中，对每个 target - nums[i] 进行二分查找即可。
    * - 时间复杂度 O(nlogn)，空间复杂度 O(logn)。
    * */
    public static int[] twoSum(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            int p = binarySearch(nums, target - nums[i], i + 1, nums.length - 1);  // 从 i 之后的元素中进行二分查找即可
            if (p != -1) return new int[] {i + 1, p + 1};
        }
        return null;
    }

    private static int binarySearch(int[] nums, int e, int l, int r) {
        if (l > r) return -1;
        int mid = (r - l) / 2 + l;
        if (e < nums[mid]) return binarySearch(nums, e, l, mid - 1);
        if (e > nums[mid]) return binarySearch(nums, e, mid + 1, r);
        return mid;
    }

    /*
    * 解法2：指针对撞
    * - 时间复杂度 O(n)，空间复杂度 O(1)。
    * - 注：两路、三路快排实际上也是指针对撞的一种应用
    * */
    public static int[] twoSum2(int[] nums, int target) {
        int i = 0, j = nums.length - 1;
        while (i != j) {
            int sum = nums[i] + nums[j];
            if (sum < target) i++;
            else if (sum > target) j--;
            else return new int[] {i + 1, j + 1};
        }
        return null;
    }

    /*
    * 解法3：解法2的简化版
    * - 时间复杂度 O(n)，空间复杂度 O(1)。
    */
    public static int[] twoSum3(int[] nums, int target) {
        int i = 0, j = nums.length - 1;
        while (nums[i] + nums[j] != target) {
            if (nums[i] + nums[j] < target) i++;
            else j--;
        }
        return new int[] {i + 1, j + 1};
    }

    public static void main(String[] args) {
        log(twoSum(new int[]{2, 7, 11, 15}, 9));  // expects [1, 2]（注意返回的是从1开始的元素序号）
        log(twoSum(new int[]{2, 3, 4}, 6));       // expects [1, 3]
    }
}
