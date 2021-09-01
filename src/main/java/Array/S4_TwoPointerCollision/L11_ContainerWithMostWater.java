package Array.S4_TwoPointerCollision;

import static Utils.Helpers.log;

/*
 * Container With Most Water
 *
 * - Find two lines, which together with x-axis forms a container that contains the most water.
 *
 * - For example: [1, 8, 6, 2, 5, 4, 8, 3, 7]
 *
 *         |                   |
 *         |                   |       |
 *         |   |               |       |
 *         |   |       |       |       |
 *         |   |       |   |   |       |
 *         |   |       |   |   |   |   |
 *         |   |   |   |   |   |   |   |
 *     |   |   |   |   |   |   |   |   |
 *   --------------------------------------   line 1 and line 8 forms the largest container ((8-1) * 7 = 49 units)
 *     0   1   2   3   4   5   6   7   8
 * */

public class L11_ContainerWithMostWater {
    /*
     * 解法1：指针对撞
     * - 思路：
     *   - 先思考影响面积大小的因素 —— 1. 两条线的间距；2. 两条线的高度；3. 两条线的高度差。
     *   - 在两个指针不断靠近的过程中，两条线的间距是在不断缩小的 ∴ 只有让两条线更高，且高度差尽量小才有可能得到更大的面积，
     *     ∴ 在对撞过程中要不断补短板，看左、右两边哪边是短板，让短的一边向前移一步，看是否能获得更大的面积。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int maxArea(int[] arr) {
        assert arr.length >= 2;
        int l = 0, r = arr.length - 1;
        int maxArea = 0;
        while (l < r) {  // 不同于 L125 该指针对撞不能同时移动 l, r
            maxArea = Math.max(maxArea, calcArea(arr, l, r));
            if (arr[l] < arr[r]) l++;
            else r--;
        }
        return maxArea;
    }

    private static int calcArea(int[] arr, int l, int r) {
        return Math.min(arr[l], arr[r]) * (r - l);
    }

    /*
     * 解法2：指针对撞（解法1的时间优化版）
     * - 思路：在解法1的基础上，当每次找到短板时，不马上进入下一次循环求面积，而是先检查下一块板子是否比当前这块长，若还没当前
     *   这块长，则肯定不会得到比之前更大的面积，因此继续寻找。
     * - 💎 经验：在使用 while 给 l、r 寻找下一个有效元素时，要注意判断越界情况：l < r；
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int maxArea2(int[] arr) {
        assert arr.length >= 2;
        int l = 0, r = arr.length - 1;
        int maxArea = 0;
        while (l < r) {
            maxArea = Math.max(maxArea, calcArea(arr, l, r));
            if (arr[l] < arr[r]) {
                int oldL = l;
                while (l < r && arr[l] <= arr[oldL]) l++;  // 直到找到比当前板子长的新板子；注意判断越界情况（l < r）
            } else {
                int oldR = r;
                while (r > l && arr[r] <= arr[oldR]) r--;  // 同上
            }
        }
        return maxArea;
    }

    public static void main(String[] args) {
        int[] arr1 = new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7};
        log(maxArea2(arr1));  // expects 49. （第一个8和最后一个7所组成的面积最大）
    }
}