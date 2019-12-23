package HashTable.S4_SlidingWindow;

import java.util.*;

import static Utils.Helpers.log;

/*
* Contains Duplicate
*
* - 给定一个整型数组，若数组中存在相同的元素则返回 true。
* */

public class L217_ContainsDuplicate {
    /*
    * 解法1：查找表
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static boolean containsDuplicate(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int n : nums) {
            if (set.contains(n)) return true;
            set.add(n);
        }
        return false;
    }

    /*
     * 解法2：排序后遍历
     * - 时间复杂度 O(nlogn)，空间复杂度 O(1)。
     * */
    public static boolean containsDuplicate2(int[] nums) {
        Arrays.sort(nums);
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i - 1])
                return true;
        }
        return false;
    }

    public static void main(String[] args) {
        log(containsDuplicate2(new int[] {1, 2, 3, 1}));  // expects true
        log(containsDuplicate2(new int[] {1, 2, 3, 4}));  // expects false
        log(containsDuplicate2(new int[] {1, 1, 1, 3, 3, 4, 3, 2, 4, 2}));  // expects true
    }
}
