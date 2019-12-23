package HashTable.S2_ClassicHashTable;

import java.util.*;

import static Utils.Helpers.log;

/*
* Three Sum
*
* - 找出整形数组中所有不同的三元组（a, b, c）使得 a + b + c = 0。
*
* - 与 TwoSum 不同之处在于 TwoSum 要返回的是元素索引，而 3Sum、4Sum 要返回的是元素值。
* */

public class L15_3Sum {
    /*
     * 解法1：化简成 2Sum + 指针对撞 + 手动去重
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length < 3) return res;
        Arrays.sort(nums);

        for (int i = 0; i < nums.length - 2; i++) {  // 固定第一个元素将问题化简为 2Sum（∵ 要保证后面的 j,k 有元素可用 ∴ 至少要留出2个元素，即 i 的滑动范围是第0个~倒数第3个元素）
            if (i == 0 || nums[i] != nums[i - 1]) {  // 手动去重（i == 0 时是第一个元素，不可能重复，而后面的元素只要不等于前一个元素即可）
                int l = i + 1;
                int r = nums.length - 1;
                while (l < r) {                      // 内部指针对撞
                    int sum = nums[i] + nums[l] + nums[r];
                    if (sum < 0) l++;
                    else if (sum > 0) r--;
                    else {
                        res.add(Arrays.asList(nums[i], nums[l++], nums[r--]));
                        while (l < r && nums[l] == nums[l - 1]) l++;  // 碰到重复元素则 l 继续 ++
                        while (l < r && nums[r] == nums[r + 1]) r--;  // 碰到重复元素则 r 继续 --
                    }
                }
            }
        }
        return res;
    }

    /*
    * 解法2：化简成 2Sum + 指针对撞 + Set 去重
    * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
    * - 通过 Set 去重，代码更简洁，但效率低一些，因为重复结果会先存入 Set，再利用 Set 去重。
    * */
    public static List<List<Integer>> threeSum2(int[] nums) {
        if (nums == null || nums.length < 3) return new ArrayList<>();
        Set<List<Integer>> set = new HashSet<>();    // Set 可以直接对 List 进行去重
        Arrays.sort(nums);                           // 指针对撞的前提是数组有序，O(nlogn)

        for (int i = 0; i < nums.length - 2; i++) {  // 固定第一个元素将问题化简为 2Sum，之后在内部做指针对撞，O(n)
            int l = i + 1;
            int r = nums.length - 1;
            while (l < r) {                          // 指针对撞，O(n)
                int sum = nums[i] + nums[l] + nums[r];
                if (sum > 0) r--;
                else if (sum < 0) l++;
                else set.add(Arrays.asList(nums[i], nums[l++], nums[r--]));  // 注意不要忘记让 l++，r--
            }
        }
        return new ArrayList<>(set);                 // Set 和 List 可以通过构造函数互相直接转化
    }

    /*
     * 解法3：查找表 + 手动去重
     * - 思路：类似 L1_TwoSum 中的解法4。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * - 还可以采用 查找表 + Set 去重的解法，这里不再赘述。
     * */
    public static List<List<Integer>> threeSum3(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length < 3) return res;
        Map<Integer, Integer> map = new HashMap<>();
        Arrays.sort(nums);

        for (int i = 0; i < nums.length; i++)
            map.put(nums[i], i);  // 因为最终要返回的是元素值（而非像 L1_TwoSum 中的元素索引），因此可以把元素都放到 Map 中，不怕覆盖

        for (int i = 0; i < nums.length - 2; i++) {  // 固定第一个元素
            if (i == 0 || nums[i] != nums[i - 1]) {  // 去重
                for (int j = i + 1; j < nums.length - 1; j++) {  // 固定第二个元素
                    if (j == i + 1 || nums[j] != nums[j - 1]) {  // 去重
                        int complement = 0 - nums[i] - nums[j];  // TwoSum 解法
                        if (map.containsKey(complement) && map.get(complement) > j)  // 注意这里要通过 > j 去重
                            res.add(Arrays.asList(nums[i], nums[j], complement));
                    }
                }
            }
        }
        return res;
    }

    /*
     * 解法4：查找表 + Set 去重
     * - 思路：与解法2对照，解法2中是固定第一个元素后，内部指针对撞（同时改变两个变量）；而该解法是固定第一个元素后，再固定第二个元素，
     *   内部只有一个变量，因此可以用 Map 进行查找。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> threeSum0(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length < 3) return res;

        Map<Integer, Integer> map = new HashMap<>();
        Set<List<Integer>> set = new HashSet<>();
        Arrays.sort(nums);

        for (int i = 0; i < nums.length; i++)
            map.put(nums[i], i);  // 可能产生覆盖，但后添加的元素的索引一定比先添加的元素的索引大，因此在下面 map.get(complement) > j 处刚好不会有问题

        for (int i = 0; i < nums.length - 2; i++) {          // 固定第一个元素
            for (int j = i + 1; j < nums.length - 1; j++) {  // 固定第二个元素
                int complement = 0 - nums[i] - nums[j];
                if (map.containsKey(complement) && map.get(complement) > j)  // 在 map 中查找第三个元素，第三个元素的索引必须 > 第二个元素的索引（保证不使用重复元素）
                    set.add(Arrays.asList(nums[i], nums[j], complement));
            }
        }
        return new ArrayList<>(set);
    }

    /*
    * 解法5：二分查找 + Set 去重
    * - 思路：在固定前两个元素的情况下，不使用查找表，而是使用二分查找来在找到 complement（因为数组有序）。
    * - 时间复杂度 O(n^2 * logn)，空间复杂度 O(1)。
    * */
    public static List<List<Integer>> threeSum4(int[] nums) {
        if (nums == null || nums.length < 3) return new ArrayList<>();
        Set<List<Integer>> res = new HashSet<>();
        Arrays.sort(nums);

        for (int i = 0; i < nums.length - 2; i++) {
            for (int j = i + 1; j < nums.length - 1; j++) {
                int complement = 0 - nums[i] - nums[j];
                int k = binarySearch(nums, complement, j + 1, nums.length - 1);
                if (k != -1) res.add(Arrays.asList(nums[i], nums[j], nums[k]));
            }
        }
        return new ArrayList(res);
    }

    private static int binarySearch(int[] nums, int target, int l, int r) {
        if (l > r) return -1;
        int mid = (r - l) / 2 + l;
        if (target < nums[mid])
            return binarySearch(nums, target, l, mid - 1);
        if (target > nums[mid])
            return binarySearch(nums, target, mid + 1, r);
        return mid;
    }

    public static void main(String[] args) {
        log(threeSum0(new int[] {-1, 0, 1, 2, -1, -4}));  // expects [[-1,0,1], [-1,-1,2]]
        log(threeSum0(new int[] {-1, 0, 1}));             // expects [-1, 0, 1]
        log(threeSum0(new int[] {1, 0, 1, 0}));           // expects []
    }
}
