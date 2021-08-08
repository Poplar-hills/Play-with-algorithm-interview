package HashTable.S2_ClassicHashTable;

import java.util.*;

import static Utils.Helpers.log;

/*
 * Three Sum
 *
 * - 找出整形数组中所有不同的三元组（a, b, c）使得 a + b + c = 0。
 * - 注意：元组是元素顺序无关的，即 (1, 2, 3) 和 (3, 2, 1) 是相同的三元组。
 *
 * - 与 TwoSum 不同之处在于 TwoSum 要返回的是元素索引，而 3Sum、4Sum 要返回的是元素值。
 * */

public class L15_3Sum {
    /*
     * 解法1：3Sum -> 2Sum（查找表 + Set 去重）
     * - 思路：首先大体思路是在遍历过程中，每次固定一个元素 nums[i]，然后在 (i..] 范围内进行 2Sum 搜索。但这样可能会得到重复
     *   的结果（如 test case 1 中 [-1,0,1]、[0,1,-1]）∴ 需要对解进行去重。∵ 解是使用 List 记录的 ∴ 若采用 Set 对 List
     *   去重，则需要所有 List 有序 ∴ 最简单的办法就是直接在最开始对 nums 排序，从而后面得到的每个解都是有序的，从而可以去重。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> threeSum(int[] nums) {
        if (nums == null || nums.length < 3) return new ArrayList<>();
        Set<List<Integer>> resSet = new HashSet<>();  // 用于存储结果并对结果进行去重的 Set
        int n = nums.length;
        Arrays.sort(nums);                     // 这里要重新排序，从而让 resSet 能对后面找到的三元组去重

        Set<Integer> set = new HashSet<>();    // 放在 for 外面是为了复用，效率会高一些（也可以放在第一层 for 内部）
        for (int i = 0; i < n; i++) {          // 固定元素 nums[i]
            for (int j = i + 1; j < n; j++) {  // 内部是标准的 2Sum（与 L1_TwoSum 解法5一致）
                int complement = 0 - nums[i] - nums[j];
                if (set.contains(complement))  // 内部的 set 是用来查找 complement 的，而非用来去重
                    resSet.add(Arrays.asList(nums[i], nums[j], complement));
                set.add(nums[j]);
            }
            set.clear();                       // ∵ set 放在了 for 外面 ∴ 每轮 2Sum 完成之后要情况 set
        }

        return new ArrayList<>(resSet);        // Set 和 List 都是 Collection，可以通过构造函数互相转化
    }

    /*
     * 解法2：3Sum -> 2Sum（指针对撞 + Set 去重）
     * - 思路：与解法1一致。
     * - 实现：在“将 3Sum 转化为 2Sum 问题”的思路下可以采用 L1_TwoSum 中的任何一种实现来解决 2Sum 问题。本解法采用 L1 中
     *   解法2的指针对撞实现。而 ∵ 指针对撞的前提也是数组有序 ∴ 同样需要先对 nums 排序。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> threeSum2(int[] nums) {
        if (nums == null || nums.length < 3) return new ArrayList<>();
        Set<List<Integer>> resSet = new HashSet<>();
        int n = nums.length;
        Arrays.sort(nums);

        for (int i = 0; i < n - 2; i++) {  // 外层固定一个元素，在内部通过指针对撞来搜索 2Sum
            int l = i + 1, r = n - 1;      // 在 (i..] 范围内进行指针对撞
            while (l < r) {
                int sum = nums[i] + nums[l] + nums[r];
                if (sum > 0) r--;
                else if (sum < 0) l++;
                else resSet.add(Arrays.asList(nums[i], nums[l++], nums[r--]));  // 注意不要忘记让 l++，r--
            }
        }

        return new ArrayList<>(resSet);
    }

    /*
     * 解法3：3Sum -> 2Sum（二分查找 + Set 去重）
     * - 思路：与解法1、2一致。
     * - 实现：采用 L1 中解法3的 Binary Search。而 ∵ Binary Search 的前提也是数组有序 ∴ 同样需要先对 nums 排序。
     * - 时间复杂度 O(n^2 * logn)，空间复杂度 O(1)。
     * */
    public static List<List<Integer>> threeSum3(int[] nums) {
        if (nums == null || nums.length < 3) return new ArrayList<>();
        Set<List<Integer>> resSet = new HashSet<>();
        int n = nums.length;
        Arrays.sort(nums);

        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 1; j < n - 1; j++) {
                int complement = 0 - nums[i] - nums[j];
                if (binarySearch(nums, j + 1, n - 1, complement) != -1)
                    resSet.add(Arrays.asList(nums[i], nums[j], complement));
            }
        }

        return new ArrayList<>(resSet);
    }

    private static int binarySearch(int[] nums, int l, int r, int target) {
        if (l > r) return -1;
        int mid = (r - l) / 2 + l;
        if (target < nums[mid]) return binarySearch(nums, l, mid - 1, target);
        if (target > nums[mid]) return binarySearch(nums, mid + 1, r, target);
        return mid;
    }

    /*
     * 解法4：3Sum -> 2Sum（指针对撞 + 手动去重）
     * - 思路：与解法2一致。
     * - 实现：解法1、2、3都是使用 Set 自动去重，虽然方便但是效率较低 ∵ 要先找到解，再通过 Set 检查是否重复。本解法中采用
     *   的手动去重的关键就是在对 nums 进行排序后，检查相邻的重复元素，若重复则无需再再后面搜索 2Sum。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> threeSum4(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length < 3) return res;
        int n = nums.length;
        Arrays.sort(nums);

        for (int i = 0; i < n - 2; i++) {            // i < n-2 即可，不用再对最后一个元素进行指针对撞了
            if (i == 0 || nums[i] != nums[i - 1]) {  // 手动去重，后面的元素只要不等于前一个元素即可
                int l = i + 1, r = n - 1;            // 在 (i..] 范围内进行指针对撞
                while (l < r) {
                    int sum = nums[i] + nums[l] + nums[r];
                    if (sum < 0) l++;
                    else if (sum > 0) r--;
                    else {
                        res.add(Arrays.asList(nums[i], nums[l++], nums[r--]));  // 当 sum == target 时找到一个解
                        while (l < r && nums[l] == nums[l - 1]) l++;            // 若下一个元素重复，则让 l, r 跳过
                        while (l < r && nums[r] == nums[r + 1]) r--;
                    }
                }
            }
        }
        return res;
    }

    /*
     * 解法5：3Sum -> 2Sum（查找表 + 手动去重）
     * - 思路：与解法1一致。
     * - 实现：解法1的手动去重版。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> threeSum5(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length < 3) return res;
        int n = nums.length;
        Arrays.sort(nums);

        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < n - 1; i++) {            // 固定第1个元素
            if (i == 0 || nums[i] != nums[i - 1]) {  // 手动去重
                for (int j = i + 1; j < n; j++) {                // 内部搜索 2Sum（也可以看做固定第2个元素）
                    if (j == i + 1 || nums[j] != nums[j - 1]) {  // 再次手动去重
                        int complement = 0 - nums[i] - nums[j];
                        if (set.contains(complement))
                            res.add(Arrays.asList(nums[i], nums[j], complement));
                        set.add(nums[j]);
                    }
                }
                set.clear();
            }
        }
        return res;
    }

    public static void main(String[] args) {
        log(threeSum3(new int[]{-1, 0, 1, 2, -1, -4}));  // expects [[-1,0,1], [-1,-1,2]]
        log(threeSum3(new int[]{1, 0, -2, 1, -2, 4}));   // expects [[1,-2,1], [-2,-2,4]]
        log(threeSum3(new int[]{-1, 0, 1}));             // expects [-1, 0, 1]
        log(threeSum3(new int[]{1, 0, 1, 0}));           // expects []
    }
}
