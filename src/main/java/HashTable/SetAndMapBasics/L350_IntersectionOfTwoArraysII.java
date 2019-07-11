package HashTable.SetAndMapBasics;

import java.util.*;

import static Utils.Helpers.log;

/*
* Intersection of Two Arrays II
*
* - Given two arrays, write a function to compute their intersection.
* - 注意：与 L349_IntersectionOfTwoArrays 要求不同的是，返回的数组是不去重的，有几个算几个，SEE test case 1。
* */

public class L350_IntersectionOfTwoArraysII {
    /*
    * 解法1：使用 Map
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static int[] intersect(int[] nums1, int[] nums2) {
        Map<Integer, Integer> map = new HashMap<>();
        List<Integer> list = new ArrayList<>();

        for (int n : nums1)    // 用 map 记录 nums1 中所有元素的频率
            map.put(n, map.getOrDefault(n, 0) + 1);

        for (int m : nums2) {  // 检查 nums2 中每个元素在 map 中的频率，若频率 > 0，说明，若存在则频率-1
            if (map.getOrDefault(m, 0) > 0) {  // 若这里改成用 map.containsKey(m) 检查，则最后需要 if (map.get(m) == 0) map.remove(m);（否则 test case 2 会失败）
                list.add(m);
                map.put(m, map.get(m) - 1);
                if (map.get(m) == 0) map.remove(m);
            }
        }

        int[] res = new int[list.size()];
        for (int i = 0; i < res.length; i++)  // 将 list 转为 array
            res[i] = list.get(i);
        return res;
    }

    /*
    * 解法2：two pointer
    * - 时间复杂度 O(nlogn)，空间复杂度 O()。
    * */
    public static int[] intersect2(int[] nums1, int[] nums2) {
        List<Integer> list = new ArrayList<>();
        Arrays.sort(nums1);
        Arrays.sort(nums2);  // 若 nums1, nums2 本身有序，则整个算法复杂度降为 O(logn)

        for (int i = 0, j = 0; i < nums1.length && j < nums2.length; ) {  // 同时遍历 nums1, nums2，但手动控制 i, j 的自增
            if (nums1[i] == nums2[j]) {
                list.add(nums1[i]);
                i++; j++;
            }
            else if (nums1[i] > nums2[j]) j++;
            else i++;
        }

        int[] res = new int[list.size()];
        for (int i = 0; i < res.length; i++)  // 将 list 转为 array
            res[i] = list.get(i);
        return res;
    }

    public static void main(String[] args) {
        log(intersect(new int[]{1, 2, 2, 1}, new int[]{2, 2}));         // expects [2, 2]. 注意返回的数组是不去重的
        log(intersect(new int[]{4, 9, 5}, new int[]{9, 4, 9, 8, 4}));   // expects [4, 9] or [9, 4]
    }
}