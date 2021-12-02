package HashTable.S2_ClassicHashTable;

import java.util.*;

import static Utils.Helpers.log;

/*
 * Four Sum
 *
 * - 从 nums 中找到所有和为 target 的四元组（4个不同元素）。
 *
 * - 整体思路是通过在遍历过程中固定一个元素，将 4Sum 问题转化为 3Sum 问题，再转化为 2Sum 问题，而 2Sum 问题就可以使用经典的
 *   1. 查找表；2. Binary Search；3. 指针对撞 这三种方法解决。
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

        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 1; j < n - 1; j++) {
                Set<Integer> set = new HashSet<>();  // ∵ 结果中只需保存元素而不需索引 ∴ 可用 Set 而无需使用 Map
                for (int k = j + 1; k < n; k++) {
                    int complement = target - nums[i] - nums[j] - nums[k];
                    if (set.contains(complement))
                        resSet.add(Arrays.asList(nums[i], nums[j], nums[k], complement));
                    set.add(nums[k]);
                }
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

    public static List<List<Integer>> fourSum4(int[] nums, int target) {
        if (nums == null || nums.length < 4) return new ArrayList<>();
        int n = nums.length;

        Arrays.sort(nums);

        Set<List<Integer>> set = new HashSet<>();
        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 1; j < n - 1; j++) {
                for (int k = j + 1; k < n; k++) {
                    int complement = target - nums[i] - nums[j] - nums[k];
                    int idx = binarySearch(nums, k + 1, nums.length - 1, complement);
                    if (idx >= 0)
                        set.add(Arrays.asList(nums[i], nums[j], nums[k], nums[idx]));
                }
            }
        }

        return new ArrayList<>(set);
    }

    private static int binarySearch(int[] nums, int l, int r, int target) {
        if (l > r) return -1;
        int mid = (r - l) / 2 + l;
        if (target < nums[mid]) return binarySearch(nums, l, mid - 1, target);
        if (target > nums[mid]) return binarySearch(nums, mid + 1, r, target);
        return mid;
    }

    public static void main(String[] args) {
        log(fourSum4(new int[] {1, 0, -1, 0, -2, 2}, 0));
        // expects [[-1,0,0,1], [-2,-1,1,2], [-2,0,0,2]]

        log(fourSum4(new int[] {-1, 0, -5, -2, -2, -4, 0, 1, -2}, -9));
        // expects [[-5,-4,-1,1], [-5,-4,0,0], [-5,-2,-2,0], [-4,-2,-2,-1]]
    }
}