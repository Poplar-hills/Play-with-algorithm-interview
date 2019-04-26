package HashTable;

import java.util.*;

import static Utils.Helpers.log;

/*
* Three Sum
*
* - 找出整形数组中所有不同的三元组（a, b, c）使得 a + b + c = 0。
* */

public class L15_3Sum {
    /*
    * 解法1：外层遍历，内层指针对撞，结果用 Set 储存
    * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
    * */
    public static List<List<Integer>> threeSum(int[] nums) {
        Set<List<Integer>> set = new HashSet<>();
        Arrays.sort(nums);                           // 指针对撞的前提是数组有序，O(nlogn)

        for (int i = 0; i < nums.length - 2; i++) {  // 外层遍历（∵ 内层的指针 k 已经从最后一个元素开始了 ∴ 这里从倒数第二个开始即可），O(n)
            int j = i + 1;
            int k = nums.length - 1;
            while (j < k) {                          // 内层指针对撞，O(n)
                int sum = nums[i] + nums[j] + nums[k];
                if (sum > 0) k--;
                else if (sum < 0) j++;
                else set.add(Arrays.asList(nums[i], nums[j++], nums[k--]));  // 注意不要忘记让 j++，k--
            }
        }
        return new ArrayList<>(set);                 // Set 和 List 可以通过构造函数互相直接转化
    }

    public static void main(String[] args) {
        log(threeSum(new int[] {-1, 0, 1, 2, -1, -4}));   // expects [[-1, 0, 1], [-1, -1, 2]]
    }
}
