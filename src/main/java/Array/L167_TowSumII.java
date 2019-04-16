package Array;

import static Utils.Helpers.log;

/*
* Two Sum II - Input array is sorted
*
* - 在一个有序数组中找到两个不同元素之和等于给定的值，返回这两个元素各自的索引值+1。
* - 条件中说了是数组是"序数"的，因此联想：
*   - 是否应用分治、递归思路？
*   - 是否可以使用二分查找？
*   - 是否可以构建一棵搜索树？
* */

public class L167_TowSumII {
    /*
    * 解法一：二分查找思路
    * - 遍历一边数组，每次遍历进行一次二分查找，因此总体是 O(nlogn)。
    * */
    public static int[] twoSum(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            int p = binarySearch(nums, target - nums[i], i + 1, nums.length - 1);  // 在后面的元素中二分查找 target - nums[i]
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
    * - 只需遍历一次，因此复杂度为 O(n)
    * - 两路、三路快排实际上也是指针对撞的一种应用
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

    // 再优化一下：
    public static int[] twoSum3(int[] nums, int target) {
        int i = 0, j = nums.length - 1;
        while (nums[i] + nums[j] != target) {
            if (nums[i] + nums[j] < target) i++;
            else j--;
        }
        return new int[] {i + 1, j + 1};
    }

    public static void main(String[] args) {
        int[] arr1 = new int[] {2, 7, 11, 15};
        int[] arr2 = new int[] {2, 3, 4};  // 注意 target 不能是重复元素相加
        log(twoSum(arr1, 9));       // expects [1, 2]（注意返回的得是索引+1）
        log(twoSum(arr2, 6));       // expects [1, 3]

        int[] arr3 = new int[] {2, 7, 11, 15};
        int[] arr4 = new int[] {2, 3, 4};
        log(twoSum3(arr3, 9));       // expects [1, 2]（注意返回的得是索引+1）
        log(twoSum3(arr4, 6));       // expects [2, 3]
    }
}
