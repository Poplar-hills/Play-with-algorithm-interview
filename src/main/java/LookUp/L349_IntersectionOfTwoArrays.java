package LookUp;

import java.util.HashSet;
import java.util.Set;

import static Utils.Helpers.log;

/*
 * Intersection of Two Arrays
 * */

public class L349_IntersectionOfTwoArrays {
    public static int[] intersection(int[] nums1, int[] nums2) {
        Set<Integer> set = new HashSet<>();
        Set<Integer> common = new HashSet<>();

        for (int n : nums1)
            set.add(n);
        for (int m : nums2)
            if (set.contains(m))
                common.add(m);

        int i = 0;
        int[] res = new int[common.size()];
        for (int n : common) res[i++] = n;
        return res;
    }

    public static void main(String[] args) {
        log(intersection(new int[] {1, 2, 2, 1}, new int[] {2, 2}));
        log(intersection(new int[] {9, 4, 9, 8, 4}, new int[] {4, 9, 5}));
    }
}