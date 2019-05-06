package HashTable.HashTableAndSlidingWindow;

import static Utils.Helpers.log;

/*
* Contains Duplicate II
*
* - 对于整型数组 nums 和整数 k，是否存在索引 i 和 j，使得 nums[i] == nums[j]，且 i 和 j 之间的差不超过 k。
* */

public class L219_ContainsDuplicateII {
    public static boolean containsNearbyDuplicate(int[] nums, int k) {

    }

    public static void main(String[] args) {
        log(containsNearbyDuplicate(new int[] {1, 2, 3, 1}, 3));        // expects true
        log(containsNearbyDuplicate(new int[] {1, 0, 1, 1}, 1));        // expects true
        log(containsNearbyDuplicate(new int[] {1, 2, 3, 1, 2, 3}, 2));  // expects false
    }
}
