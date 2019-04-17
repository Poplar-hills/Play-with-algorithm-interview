package Array.TwoPointerCollision;

import static Utils.Helpers.log;

/*
* Container With Most Water
*
* - 指针对撞思路
* - 面积取决于两个因素：1.两个元素的间距 2.两个元素的大小
* - 在两个指针不断靠近的过程中，只有不断寻找更大的两个元素才有可能获得更大的面积，因此 i++, j-- 的条件是找到比对面元素更大的元素。
* */

public class L11_ContainerWithMostWater {
    public static int maxArea(int[] arr) {
        assert arr.length >= 2;
        int maxArea = 0;
        for (int i = 0, j = arr.length - 1; i < j; ) {
            maxArea = Math.max(maxArea, calcArea(arr, i, j));
            if (arr[i] < arr[j]) i++;  // 在两个指针不断靠近的过程中不断寻找最大的两个元素
            else j--;
        }
        return maxArea;
    }

    private static int calcArea(int[] arr, int i, int j) {
        return Math.min(arr[i], arr[j]) * (j - i);
    }

    public static void main(String[] args) {
        int[] arr1 = new int[] {1, 8, 6, 2, 5, 4, 8, 3, 7};
        log(maxArea(arr1));  // expects 49. min(8, 7) * (indexOf(7) - indexOf(8)) = 7 * 7 = 49
    }
}