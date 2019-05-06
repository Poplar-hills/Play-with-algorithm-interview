package HashTable.HashTableAndSlidingWindow;

import java.util.HashMap;
import java.util.Map;

import static Utils.Helpers.log;

/*
* Contains Duplicate II
*
* - 对于整型数组 nums 和整数 k，是否存在索引 i 和 j，使得 nums[i] == nums[j]，且 i 和 j 之间的差不超过 k。
* */

public class L219_ContainsDuplicateII {
    public static boolean containsNearbyDuplicate(int[] nums, int k) {
        if (nums == null || nums.length < 2 || k <= 0)
            return false;

        Map<Integer, Integer> freq = new HashMap<>();
        int l = 0, r = 0;

        for (; r < nums.length && r - l <= k; r++)
            freq.put(nums[r], freq.getOrDefault(nums[r], 0) + 1);
        for (int f : freq.values())
            if (f > 1)
                return true;

        while (r < nums.length) {
            int oldE = nums[l], newE = nums[r];
            freq.put(oldE, freq.get(oldE) - 1);
            freq.put(newE, freq.getOrDefault(newE, 0) + 1);
            if (freq.get(newE) > 1) return true;
            l++; r++;
        }
        return false;
    }

    public static void main(String[] args) {
        log(containsNearbyDuplicate(new int[] {1, 0, 1, 1}, 1));        // expects true
        log(containsNearbyDuplicate(new int[] {4, 1, 2, 3, 1}, 3));     // expects true
        log(containsNearbyDuplicate(new int[] {1, 2, 3, 1, 2, 3}, 2));  // expects false
        log(containsNearbyDuplicate(new int[] {1}, 1));                 // expects false
        log(containsNearbyDuplicate(new int[] {99, 99}, 2));            // expects true
    }
}
