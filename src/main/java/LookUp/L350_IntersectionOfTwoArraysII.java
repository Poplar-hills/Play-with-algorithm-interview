package LookUp;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Utils.Helpers.log;

/*
* Intersection of Two Arrays II
* */

public class L350_IntersectionOfTwoArraysII {
    public static int[] intersect(int[] nums1, int[] nums2) {
        Map<Integer, Integer> map = new HashMap<>();
        List<Integer> list = new ArrayList<>();

        for (int n : nums1)
            map.put(n, map.getOrDefault(n, 0) + 1);
        for (int m : nums2) {
            if (map.getOrDefault(m, 0) > 0) {
                list.add(m);
                map.put(m, map.get(m) - 1);
            }
        }

        int[] res = new int[list.size()];
        for (int i = 0; i < res.length; i++)
            res[i] = list.get(i);
        return res;
    }

    public static void main(String[] args) {
        log(intersect(new int[] {1, 2, 2, 1}, new int[] {2, 2}));        // expects [2, 2]
        log(intersect(new int[] {4, 9, 5}, new int[] {9, 4, 9, 8, 4}));  // expects [4, 9]
    }
}