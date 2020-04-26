package RecursionAndBackTracking.S2_Permutations;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/*
 * Permutations II
 *
 * - Given a collection of numbers that might contain duplicates, return all possible unique permutations.
 * */

public class L47_PermutationsII {
    /*
     * 解法1：Recursion + Backtracking + Set
     * - 思路：在 L46_Permutations 解法1的基础上加入用于去重的 Set。
     * - 时间复杂度 O(n!)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> permuteUnique(int[] nums) {
        if (nums.length == 0) return new ArrayList<>();
        Set<List<Integer>> set = new HashSet<>();
        backtrack(nums, new ArrayList<>(), new boolean[nums.length], set);
        return new ArrayList<>(set);
    }

    private static void backtrack(int[] nums, List<Integer> list, boolean[] used, Set<List<Integer>> set) {
        if (list.size() == nums.length) {
            set.add(new ArrayList<>(list));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (!used[i]) {
                list.add(nums[i]);
                used[i] = true;
                backtrack(nums, list, used, set);
                list.remove(list.size() - 1);
                used[i] = false;
            }
        }
    }

    /*
     * 解法2：Iteration + Set
     * - 思路：在 L46_Permutations 解法4的基础上加入用于去重的 Set。
     * - 时间复杂度 O(n * n!)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> permuteUnique2(int[] nums) {
        if (nums.length == 0) return new ArrayList<>();
        Queue<List<Integer>> q = new LinkedList<>();
        q.offer(new ArrayList<>());

        for (int i = 0; i < nums.length; i++) {
            while (q.peek().size() == i) {
                List<Integer> list = q.poll();
                for (int j = 0; j <= list.size(); j++) {
                    List<Integer> newList = new ArrayList<>(list);
                    newList.add(j, nums[i]);
                    q.offer(newList);
                }
            }
        }

        return new ArrayList<>(new HashSet<>(q));  // 最后 Set 去重
    }

    /*
     * 解法3：Recursion + In-place swap + Set
     * - 思路：在 L46_Permutations 解法4的基础上加入用于去重的 Set。
     * - 时间复杂度 O(n!)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> permuteUnique3(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums.length == 0) return res;
        backtrack3(nums, 0, res);
        return new ArrayList<>(new HashSet<>(res));  // 最后 Set 去重
    }

    private static void backtrack3(int[] nums, int i, List<List<Integer>> res) {
        if (i == nums.length - 1) {
            List<Integer> list = new ArrayList<>();
            for (int n : nums) list.add(n);
            res.add(list);
            return;
        }
        for (int j = i; j < nums.length; j++) {
            swap(nums, i, j);
            backtrack3(nums, i + 1, res);
            swap(nums, i, j);
        }
    }

    /*
     * 解法4：Recursion + Backtracking + Inner Set
     * - 思路：与解法1一致。
     * - 实现：不同于解法1、2、3使用 Set 对结果集进行去重，本解法对树进行剪枝，从而不产生重复解。例如对于 nums=[1,2,1] 来说：
     *                              []
     *                  1/          2|          1\
     *              [1]             [2]             [1]
     *           2/     1\       1/    1\        1/    2\
     *         [1,2]   [1,1]   [2,1]   [2,1]   [1,1]   [1,2]
     *          1|      2|      1|      1|      2|      1|
     *        [1,2,1] [1,1,2] [2,1,1] [2,1,1] [1,1,2] [1,2,1]
     *
     *   在对 []、[2] 进行分支时，都出现了重复的分支，而剪掉重复分支的关键在于判断 nums 中的重复元素 —— 只要元素是重复的，
     *   则分支一定重复 ∴ 可以在解法1的基础上使用 Set 进行判断。
     * - 时间复杂度 O(n!)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> permuteUnique4(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums.length == 0) return res;
        backtrack4(nums, new ArrayList<>(), new boolean[nums.length], res);
        return res;
    }

    private static void backtrack4(int[] nums, List<Integer> list, boolean[] used, List<List<Integer>> res) {
        if (list.size() == nums.length) {
            res.add(new ArrayList<>(list));
            return;
        }
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            if (!used[i] && !set.contains(nums[i])) {  // 通过 Set 识别 nums 中未使用过的元素是否与之前使用过的重复
                set.add(nums[i]);
                list.add(nums[i]);
                used[i] = true;
                backtrack4(nums, list, used, res);
                list.remove(list.size() - 1);
                used[i] = false;
            }
        }
    }

    /*
     * 解法5：Recursion + Backtracking + Sort
     * - 思路：与解法4一致。
     * - 实现：不同于解法4中采用 Set 识别 nums 中的重复元素，该解法先对 nums 排序，再通过判断前后两个元素是否相等的方式去重。
     * - 时间复杂度 O(nlogn + n!)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> permuteUnique5(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums.length == 0) return res;
        Arrays.sort(nums);    // 这里要先排序，后面才能进行 nums[i] == nums[i-1] 的判断
        backtrack5(nums, new ArrayList<>(), new boolean[nums.length], res);
        return res;
    }

    private static void backtrack5(int[] nums, List<Integer> list, boolean[] used, List<List<Integer>> res) {
        if (list.size() == nums.length) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
            if (i > 0 && nums[i] == nums[i - 1] && used[i - 1]) continue;  // 通过比较前后两个元素来识别重复
            list.add(nums[i]);
            used[i] = true;
            backtrack5(nums, list, used, res);
            list.remove(list.size() - 1);
            used[i] = false;
        }
    }

    /*
     * 解法6：Recursion + In-place swap + Inner Set
     * - 思路：与解法3一致。
     * - 实现：在解法3的基础上加入解法4中对树进行剪枝的优化。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> permuteUnique6(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums.length == 0) return res;
        backtrack6(nums, 0, res);
        return res;
    }

    private static void backtrack6(int[] nums, int i, List<List<Integer>> res) {
        if (i == nums.length - 1) {
            List<Integer> list = new ArrayList<>();
            for (int n : nums) list.add(n);
            res.add(list);
            return;
        }
        Set<Integer> set = new HashSet<>();
        for (int j = i; j < nums.length; j++) {
            if (!set.contains(nums[j])) {  // 若 nums[j] 已经在 set 中了（已经与其余元素 swap 过了）则跳过
                set.add(nums[j]);
                swap(nums, i, j);
                backtrack6(nums, i + 1, res);
                swap(nums, i, j);
            }
        }
    }

    public static void main(String[] args) {
        log(permuteUnique6(new int[]{1, 1, 2}));     // expects [[1,1,2], [1,2,1], [2,1,1]]
        log(permuteUnique6(new int[]{1, 2, 1}));     // expects [[1,1,2], [1,2,1], [2,1,1]]
        log(permuteUnique6(new int[]{1, 1, 2, 1}));  // expects [[1,1,1,2], [1,1,2,1], [1,2,1,1], [2,1,1,1]]
        log(permuteUnique6(new int[]{1, 2}));        // expects [[1,2], [2,1]]
        log(permuteUnique6(new int[]{1}));           // expects [[1]]
        log(permuteUnique6(new int[]{}));            // expects []
    }
}
