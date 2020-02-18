package HashTable.S2_ClassicHashTable;

import java.util.*;

import static Utils.Helpers.log;

/*
 * Four Sum
 *
 * - 从 nums 中找到所有和为 target 的四元组（4个不同元素）。
 *
 * - 思路：将 4Sum 化简到 3Sum 再化简到 2Sum 求解，即对于 nums = [1, 0, -1, 0, -2, 2]，target = 0 来说：
 *   - 首先对 nums 排序：[-2, -1, 0, 0, 1, 2]。
 *   - 化简到 3Sum：引入指针 i 固定第一个元素使得 nums[i] + 3Sum = 0：
 *     若 i = 0，则有 -2 + 3Sum = 0，即只需找到三个和为2的元素即可。
 *   - 化简到 2Sum：引入指针 j 固定第二个元素使得 nums[j] + 2Sum = 2：
 *     若 j = 1，则有 -1 + 2Sum = 2，即只需找到两个和为3的元素即可。
 *   - 指针对撞解决 2Sum：引入指针 l, r 使得 nums[l] + nums[r] = 3：
 *     l, r 在 nums 的后四个元素中寻找（因为其前两个已经固定，不能重复使用），最终找到 1, 2，使得 [-2,-1,1,2] 是一个符合条件的四元组。
 *   - 当 l, r 对撞完毕，没有找到更多四元组，则回到上一层，j++，nums[j] = 0，之后 l, r 在后三个元素中再次开始 2Sum 对撞，并最终找到
 *     0, 2，使得 [-2,0,0,2] 成为第二个符合条件的四元组。
 *   - 之后 j 继续往右滑动（nums[j] = 0），因为 nums[j-1] 就是等于0，若此次再以0开始遍历，则得到的四元组很可能与上一个重复，因此跳过。
 *   - j 再次 ++ 后超越了有效滑动范围（j 要 < nums.length-2，l,r 才能有元素用），遍历结束。回到上一层，i++，nums[i] = -1，开启新一
 *     轮 3Sum 遍历，j = i+1，nums[j] = 0，找到第三个有效四元组 [-1,0,0,1]。之后 j++，因为超越有效滑动范围结束遍历。
 *   - i 再次++，此时 j,l,r 的取值范围只剩下最后3个元素，没得可选，因为不是有效四元组，结束遍历。整个 4Sum 过程结束，返回3个有效结果。
 *
 * - 时间复杂度 O(n^3)，空间复杂度 O(1)。
 * */

public class L18_4Sum {
    /*
     * 解法1：4Sum -> 3Sum -> 2Sum（查找表 + Set 去重）
     * - 思路：类似 L15_3Sum 中的解法1。该解法代码最简洁，但 ∵ 要先找到解，再通过 Set 检查是否重复 ∴ 性能比手动去重的解法要差。
     * - 时间复杂度 O(n^3)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> fourSum(int[] nums, int target) {
        if (nums == null || nums.length < 4) return new ArrayList<>();
        Set<List<Integer>> resSet = new HashSet<>();
        int n = nums.length;

        Arrays.sort(nums);

        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 1; j < n - 1; j++) {
                for (int k = j + 1; k < n; k++) {
                    int complement = target - nums[i] - nums[j] - nums[k];
                    if (set.contains(complement))
                        resSet.add(Arrays.asList(nums[i], nums[j], nums[k], complement));
                    set.add(nums[k]);
                }
                set.clear();
            }
        }

        return new ArrayList<>(resSet);
    }

    /*
     * 解法2：4Sum -> 3Sum -> 2Sum（指针对撞 + Set 去重）
     * - 思路：类似 L15_3Sum 解法3。
     * - 时间复杂度 O(n^3)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> fourSum2(int[] nums, int target) {
        if (nums == null || nums.length < 4) return new ArrayList<>();
        Set<List<Integer>> resSet = new HashSet<>();
        int n = nums.length;

        Arrays.sort(nums);

        for (int i = 0; i < n - 3; i++) {          // ∵ 里面要 n-2 ∴ 这里要 n-3
            for (int j = i + 1; j < n - 2; j++) {  // i < n-2 即可，不用再最后一个元素进行指针对撞了
                int l = j + 1, r = n - 1;
                while (l < r) {
                    int sum = nums[i] + nums[j] + nums[l] + nums[r];
                    if (sum < target) l++;
                    else if (sum > target) r--;
                    else resSet.add(Arrays.asList(nums[i], nums[j], nums[l++], nums[r--]));
                }
            }
        }

        return new ArrayList<>(resSet);
    }

    /*
     * 解法3：4Sum -> 3Sum -> 2Sum（指针对撞 + 手动去重）
     * - 思路：类似 L15_3Sum 解法4。
     * - 时间复杂度 O(n^3)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> fourSum3(int[] nums, int target) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length < 4) return res;
        int n = nums.length;

        Arrays.sort(nums);

        for (int i = 0; i < n - 3; i++) {            // 固定第1个元素，将问题转化为 3Sum
            if (i == 0 || nums[i] != nums[i - 1]) {  // 去重
                for (int j = i + 1; j < n - 2; j++) {            // 固定第2个元素，将问题转化为 2Sum
                    if (j == i + 1 || nums[j] != nums[j - 1]) {  // 去重
                        int l = j + 1, r = n - 1;
                        while (l < r) {
                            int sum = nums[i] + nums[j] + nums[l] + nums[r];
                            if (sum < target) l++;
                            else if (sum > target) r--;
                            else {                                            // 当 sum == target 时找到一个解
                                res.add(Arrays.asList(nums[i], nums[j], nums[l++], nums[r--]));
                                while (l < r && nums[l] == nums[l - 1]) l++;  // 若下一个元素重复，则让 l, r 跳过
                                while (l < r && nums[r] == nums[r + 1]) r--;
                            }
                        }
                    }
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        log(fourSum2(new int[] {1, 0, -1, 0, -2, 2}, 0));
        // expects [[-1,0,0,1], [-2,-1,1,2], [-2,0,0,2]]

        log(fourSum2(new int[] {-1, 0, -5, -2, -2, -4, 0, 1, -2}, -9));
        // expects [[-5,-4,-1,1], [-5,-4,0,0], [-5,-2,-2,0], [-4,-2,-2,-1]]
    }
}