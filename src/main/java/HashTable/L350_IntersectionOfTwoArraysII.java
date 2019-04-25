package HashTable;

import java.util.*;

import static Utils.Helpers.log;

/*
* Intersection of Two Arrays II
* */

public class L350_IntersectionOfTwoArraysII {
    public static int[] intersect(int[] nums1, int[] nums2) {  // 解法1：使用 map，时间复杂度 O(n)，空间复杂度 O(n)
        Map<Integer, Integer> map = new HashMap<>();
        List<Integer> list = new ArrayList<>();

        for (int n : nums1)  // 时间复杂度 O(n)，空间复杂度 O(n)
            map.put(n, map.getOrDefault(n, 0) + 1);
        for (int m : nums2) {  // 时间复杂度 O(n)，空间复杂度 O(n)
            if (map.getOrDefault(m, 0) > 0) {
                list.add(m);
                map.put(m, map.get(m) - 1);
            }
        }

        int[] res = new int[list.size()];
        for (int i = 0; i < res.length; i++)  // 时间复杂度 O(n)，空间复杂度 O(n)
            res[i] = list.get(i);
        return res;
    }

    public static int[] intersect2(int[] nums1, int[] nums2) {  // 解法2：two pointer，复杂度 O(nlogn)
        List<Integer> list = new ArrayList<>();
        Arrays.sort(nums1);
        Arrays.sort(nums2);  // nums1, nums2 本身是有序的，则整个算法复杂度降为 O(logn)

        for (int i = 0, j = 0; i < nums1.length && j < nums2.length; ) {
            if (nums1[i] == nums2[j]) {
                list.add(nums1[i]);
                i++; j++;
            }
            else if (nums1[i] > nums2[j]) j++;
            else i++;
        }

        int[] res = new int[list.size()];
        for (int i = 0; i < res.length; i++)
            res[i] = list.get(i);
        return res;
    }

    public static void main(String[] args) {
        log(intersect(new int[] {1, 2, 2, 1}, new int[] {2, 2}));         // expects [2, 2]
        log(intersect(new int[] {4, 9, 5}, new int[] {9, 4, 9, 8, 4}));   // expects [4, 9] or [9, 4]

        log(intersect2(new int[] {1, 2, 2, 1}, new int[] {2, 2}));        // expects [2, 2]
        log(intersect2(new int[] {4, 9, 5}, new int[] {9, 4, 9, 8, 4}));  // expects [4, 9] or [9, 4]
    }
}