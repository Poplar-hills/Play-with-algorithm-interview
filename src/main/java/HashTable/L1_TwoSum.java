package HashTable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static Utils.Helpers.log;

/*
* Two Sum
*
* - 对于整型数组 nums，返回其中两个不同元素的索引 i 和 j 使得 nums[i] + nums[j] 等于给定的 target 值。
* */

public class L1_TwoSum {
    /*
     * 解法2：查找表
     * - 思路：将 nums 中的所有元素一次性放入 Map 中，之后遍历数组，若 target - nums[i] 存在于 Map 中，则找到了想要的结果。
     * - 问题：若一次性将所有元素都放入 Map 中，则对于 [2, 7, 15, 7] 这样的数据会有问题，因为其中有重复元素7，后一个7的索引
     *        会覆盖前一个7的索引，导致结果出错。
     * - 修正：不一次性将所有元素放入 Map，而是在每次要将元素 nums[i] 放入 Map 之前，先检查已放入 Map 中的元素是否有
     *        target - nums[i]，若有则说明找到了，没有则再继续放入。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int[] twoSum2(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement))
                return new int[] {map.get(complement), i};
            map.put(nums[i], i);
        }

        return null;
    }

    public static void main(String[] args) {g
        log(twoSum2(new int[] {2, 7, 15, 11}, 9));   // expects [0, 1]
        log(twoSum2(new int[] {2, 7, 15, 11}, 18));  // expects [1, 3]
    }
}
