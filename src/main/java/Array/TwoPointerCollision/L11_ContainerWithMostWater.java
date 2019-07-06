package Array.TwoPointerCollision;

import static Utils.Helpers.log;

/*
* Container With Most Water
*
* */

public class L11_ContainerWithMostWater {
    /*
    * 解法1：指针对撞
    * - 思路：
    *   - 面积取决于两个因素：1.两个元素的间距 2.两个元素本身的大小。
    *   - 在两个指针不断靠近的过程中，只有不断寻找更大的元素才有可能获得更大的面积，对撞的过程就是在不断补短板的过程 —— 看左、右两边
    *     哪边是短板，让短的一边向对面移动一步，看是否能获得更大的面积。
    * - 时间复杂度 O(n)，空间复杂度 O(1)。
    * */
    public static int maxArea(int[] arr) {
        assert arr.length >= 2;
        int maxArea = 0;
        for (int i = 0, j = arr.length - 1; i < j; ) {
            maxArea = Math.max(maxArea, calcArea(arr, i, j));
            if (arr[i] < arr[j]) i++;  // 若 arr[i] < arr[j] 则 i 是短板，i++
            else j--;
        }
        return maxArea;
    }

    private static int calcArea(int[] arr, int i, int j) {
        return Math.min(arr[i], arr[j]) * (j - i);
    }

    /*
    * 解法2：解法1的优化版
    * - 思路：在解法1的基础上，当每次找到短板时，不马上进入下一次循环求面积，而是先检查下一块板子是否比当前这块长，若还没当前
    *   这块长，则继续寻找。
    * - 时间复杂度 O(n)，空间复杂度 O(1)。
    * */
    public static int maxArea2(int[] arr) {
        assert arr.length >= 2;
        int maxArea = 0;
        for (int i = 0, j = arr.length - 1; i < j; ) {
            maxArea = Math.max(maxArea, calcArea(arr, i, j));
            if (arr[i] < arr[j]) {
                int oldI = i;
                while (i < j && arr[i] <= arr[oldI]) i++;  // 直到找到比当前这块板子还长的新板子位置
            } else {
                int oldJ = j;
                while (j > i && arr[j] <= arr[oldJ]) j--;  // 同上
            }
        }
        return maxArea;
    }

    public static void main(String[] args) {
        int[] arr1 = new int[] {1, 8, 6, 2, 5, 4, 8, 3, 7};
        log(maxArea2(arr1));  // expects 49. （第一个8和最后一个7所组成的面积最大）
    }
}