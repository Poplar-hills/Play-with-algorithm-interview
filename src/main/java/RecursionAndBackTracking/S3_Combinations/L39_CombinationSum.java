package RecursionAndBackTracking.S3_Combinations;

import static Utils.Helpers.*;

import java.util.*;

/*
 * Combination Sum
 *
 * - Given a set of unique numbers and a target number, find all the unique combinations in the numbers
 *   where the numbers sums to the target.
 *
 * - Notes:
 *   1. The same repeated number may be chosen from nums unlimited number of times.
 *   2. All numbers (including target) will be positive integers.
 *   3. The solution set must not contain duplicate combinations.
 *
 * - L39_CombinationSum：nums 中的所有元素都是唯一的，且可以使用无限多次，但结果集中不能有重复解；
 * - L40_CombinationSumII：nums 中的元素可能重复，且都只能使用一次，但结果集中不能有重复解。
 *
 * - 分析：虽然组合不关注顺序，但 ∵ 每个元素可以使用无限多次 ∴ 每次分支时仍然要遍历所有元素。在此基础上再考虑如何剪枝提速。
 * */

public class L39_CombinationSum {
    /*
     * 解法1：Recursion + Backtracking + Set 去重
     * - 思路：尝试用 nums 中的每一个元素对 target 进行递归分解，其中：
     *     1. 若 target 减去一个 num 之后等于0则找到一个解；
     *     2. num 必须 <= target 才可以相减。
     *   例如对于 nums=[2, 5, 3], target=8 来说：
     *                              8
     *               2/            5|           3\
     *               6              3             5
     *         2/   3|   5\       2/ 3\      2/  3|  5\
     *         4     3    1       1   0      3    2    0    - 找到 [5,3]、[3,5]
     *       2/ 3\ 2/ 3\                   2/ 3\ 2|
     *       2   1 1   0                   1   0  0         - 找到 [2,3,3]、[3,2,3]、[3,3,2]
     *      2|
     *       0                                              - 找到 [2,2,2,2]
     *   但 ∵ 组合不关注顺序（如 [3,5] 和 [5,3] 等价）∴ 需对找到的解进行去重，在找到解时先对其中的元素进行排序，再用 Set 去重。
     * - 时间复杂度 O(n^n)，空间复杂度 O(???)。
     * */
    public static List<List<Integer>> combinationSum(int[] nums, int target) {
        Set<List<Integer>> set = new HashSet<>();
        if (nums == null || nums.length == 0) return new ArrayList<>();
        backtrack(nums, target, new ArrayList<>(), set);
        return new ArrayList<>(set);
    }

    private static void backtrack(int[] nums, int target, List<Integer> list, Set<List<Integer>> set) {
        if (target == 0) {
            List<Integer> newList = new ArrayList<>(list);  // 先复制
            newList.sort((a, b) -> a - b);  // 再排序
            set.add(newList);               // 最后 Set 去重
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] <= target) {        // Pruning，跳过 target - nums[i] < 0 的分支
                list.add(nums[i]);
                backtrack(nums, target - nums[i], list, set);
                list.remove(list.size() - 1);
            }
        }
    }

    /*
     * 解法2：Recursion + Backtracking
     * - 思路：与解法1一致。
     * - 实现：解法1是在找到解之后再去重，而更优的做法是根本不产生重复解，即对树进行更进一步的剪枝以避免进入产生重复解的分支。
     *   具体做法是让每个节点在遍历 nums 时不回头，只遍历索引 >= 当前节点的 nums ∴ 解法1中的树会被剪成这样：
     *                          8
     *               2/        5|       3\
     *               6          3         5    - 3节点不再考虑分支2，只考虑5、3分支（而5分支无效，只剩下3）；5节点不再考虑2、5分支
     *         2/   3|   5\    3|        3|
     *         4     3    1     0         2    - 第一个3节点不再考虑分支2，只考虑5、3分支（而5分支无效，只剩下3）；0节点即找到解 [5,3]；2节点不再考虑分支2、5，而3分支又无效，所以不再有分支
     *       2/ 3\  3|
     *       2   1   0                         - 找到解 [2,3,3]
     *      2|
     *       0                                 - 找到解 [2,2,2,2]
     *
     * - 👉 注意：∵ 题中说了 nums 中的元素可以被使用无限多次 ∴ 👆在给6节点分支时，还要包含上层已经使用过的2分支。即在向下递归时
     *   要传的是 j，而非 j+1（这是与 L40 的不同点）。
     * - 时间复杂度 << O(n^n)，空间复杂度 O(target)。
     * */
    public static List<List<Integer>> combinationSum2(int[] nums, int target) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length == 0) return res;
        backtrack2(nums, target, 0, new ArrayList<>(), res);
        return res;
    }

    private static void backtrack2(int[] nums, int target, int i, List<Integer> list, List<List<Integer>> res) {
        if (target == 0) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int j = i; j < nums.length; j++) {  // 在遍历 nums 时不回头，只遍历 [i..) 范围的
            if (nums[j] <= target) {
                list.add(nums[j]);
                backtrack2(nums, target - nums[j], j, list, res);  // 向下递归时要传 j，而非 j+1（这是与 L40 的不同点）
                list.remove(list.size() - 1);
            }
        }
    }

    /*
     * 解法3：Recursion + Backtracking + sort + 剪枝
     * - 思路：与解法1、2一致。
     * - 实现：解法2通过不回头的遍历 nums 进行了进一步剪枝，而该解法更进一步，再添加一种剪枝的情况，进一步提升算法效率。例如
     *   对于 nums=[2,3,5,6], target=3 来说：
     *                   3
     *         2/    3|    5|   6\
     *        ...     √     ×    ×
     *   当遍历到 n=3 时 target - n 就已经等于0了，无需再遍历5、6 ∴ 可对5、6进行剪枝。但这种剪枝方式依赖于 nums 有序
     *   ∴ 可以在回溯开始前对 nums 进行从小到大排序。
     * - 时间复杂度 << O(n^n)，空间复杂度 O(target)。
     * */
    public static List<List<Integer>> combinationSum3(int[] nums, int target) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length == 0) return res;
        Arrays.sort(nums);  // 先对 nums 排序（若不先排序，那下面就不能把 nums[j] <= target 的条件放在 for 中，只能放在 for 内部）
        backtrack3(nums, target, 0, new ArrayList<>(), res);
        return res;
    }

    private static void backtrack3(int[] nums, int target, int i, List<Integer> list, List<List<Integer>> res) {
        if (target == 0) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int j = i; j < nums.length && nums[j] <= target; j++) {  // 若 n > target 时直接退出循循环（而非解法2中在循环内做判断）
            list.add(nums[j]);
            backtrack3(nums, target - nums[j], j, list, res);
            list.remove(list.size() - 1);
        }
    }

    /*
     * 解法4：DP
     * - 思路：// TODO: ????
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static List<List<Integer>> combinationSum4(int[] nums, int target) {
        Arrays.sort(nums);
        List<List<Integer>>[] dp = new List[target + 1];  // dp[i] 保存所有和为 i 的组合（∴ 最后返回 dp[target] 即可）

        for (int i = 0; i <= target; i++) {
            List<List<Integer>> comboList = new ArrayList<>();  // 和为 i 的组合

            for (int j = 0; j < nums.length && nums[j] <= i; j++) {  // 找到所有和为 i 的组合 j ∈ [0,i]
                int n = nums[j];
                if (n == i) {
                    comboList.add(Arrays.asList(n));     // n == i 的情况需特殊处理
                    continue;
                }
                for (List<Integer> list : dp[i - n]) {      // 遍历 dp[i-n] 中的每一个组合
                    if (n >= list.get(list.size() - 1)) {
                        List<Integer> newList = new ArrayList<>(list);
                        newList.add(n);
                        comboList.add(newList);
                    }
                }
            }
            dp[i] = comboList;
        }

        return dp[target];
    }

    public static void main(String[] args) {
        log(combinationSum2(new int[]{2, 7, 3, 6}, 7));  // expects [[7], [2,2,3]]
        log(combinationSum2(new int[]{2, 3, 5}, 8));     // expects [[2,2,2,2], [2,3,3], [3,5]]
        log(combinationSum2(new int[]{2, 3, 5}, 5));     // expects [[2,3], [5]]
        log(combinationSum2(new int[]{3}, 8));           // expects []
    }
}
