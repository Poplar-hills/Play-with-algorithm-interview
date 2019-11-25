package RecursionAndBackTracking.Permutations;

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
     * - 思路：在 L46_Permutations 解法3的基础上加入用于去重的 Set。
     * - 时间复杂度 O(n^n)，空间复杂度 O(n^n)。
     * */
    public static List<List<Integer>> permuteUnique(int[] nums) {
        if (nums.length == 0) return new ArrayList<>();
        Set<List<Integer>> set = new HashSet<>();
        helper(nums, new ArrayList<>(), new boolean[nums.length], set);
        return new ArrayList<>(set);
    }

    private static void helper(int[] nums, List<Integer> list, boolean[] used, Set<List<Integer>> set) {
        if (list.size() == nums.length) {
            set.add(new ArrayList<>(list));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (!used[i]) {
                list.add(nums[i]);
                used[i] = true;
                helper(nums, list, used, set);
                list.remove(list.size() - 1);
                used[i] = false;
            }
        }
    }

    /*
     * 解法2：Iteration + Set
     * - 思路：在 L46_Permutations 解法2的基础上加入用于去重的 Set。
     * - 时间复杂度 O(n * n!)，空间复杂度 O(n * n!)。
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
     * 解法3：Iteration + In-place swap + Set
     * - 思路：在 L46_Permutations 解法4的基础上加入用于去重的 Set。
     * - 时间复杂度 O(n!)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> permuteUnique3(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums.length == 0) return res;
        helper3(nums, 0, res);
        return new ArrayList<>(new HashSet<>(res));  // 最后 Set 去重
    }

    private static void helper3(int[] nums, int i, List<List<Integer>> res) {
        if (i == nums.length) {
            List<Integer> list = new ArrayList<>();
            for (int n : nums) list.add(n);
            res.add(list);
            return;
        }
        for (int j = i; j < nums.length; j++) {
            swap(nums, i, j);
            helper3(nums, i + 1, res);
            swap(nums, i, j);
        }
    }

    /*
     * 解法4：Recursion + Backtracking + Inner Set
     * - 思路：不同于解法1、2、3，该解法不使用 Set 去重，而是先观察 test case，例如对于 nums=[1,2,1] 来说：
     *                              []
     *                  1/          2|          1\
     *               [1]            [2]            [1]
     *           2/     1\       1/    1\        1/    2\
     *         [1,2]   [1,1]   [2,1]   [2,1]   [1,1]   [1,2]
     *          1|      2|      1|      1|      2|      1|
     *        [1,2,1] [1,1,2] [2,1,1] [2,1,1] [1,1,2] [1,2,1]
     *
     *   可见在对 []、[2] 进行分支时，都出现了重复的分支 ∴ 需要一种能消除重复的分支的方法。而消除重复分支的关键在于判断 nums
     *   中的重复元素 ∴ 可以采用 Set 实现。
     * - 时间复杂度 O(n!)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> permuteUnique4(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums.length == 0) return res;
        helper4(nums, new ArrayList<>(), new boolean[nums.length], res);
        return res;
    }

    private static void helper4(int[] nums, List<Integer> list, boolean[] used, List<List<Integer>> res) {
        if (list.size() == nums.length) {
            res.add(new ArrayList<>(list));
            return;
        }
        Set<Integer> set = new HashSet<>();                  // 创建 Set 用于
        for (int i = 0; i < nums.length; i++) {
            if (used[i] || set.contains(nums[i])) continue;  // 这里通过 Set 识别 nums 中的重复元素
            set.add(nums[i]);
            list.add(nums[i]);
            used[i] = true;
            helper4(nums, list, used, res);
            list.remove(list.size() - 1);
            used[i] = false;
        }
    }

    /*
     * 解法5：Recursion + Backtracking + Sort
     * - 思路：与解法4完全相同。
     * - 实现：不同于解法4中采用 Set 识别 nums 中的重复元素，该解法先对 nums 排序，再通过判断前后两个元素是否相等的方式去重。
     * - 时间复杂度 O(nlogn + n!)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> permuteUnique5(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums.length == 0) return res;
        Arrays.sort(nums);    // 这里要先排序，后面才能进行 nums[i] == nums[i-1] 的判断
        helper5(nums, new ArrayList<>(), new boolean[nums.length], res);
        return res;
    }

    private static void helper5(int[] nums, List<Integer> list, boolean[] used, List<List<Integer>> res) {
        if (list.size() == nums.length) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
            if (i > 0 && nums[i] == nums[i - 1] && used[i - 1]) continue;  // 通过比较前后两个元素来识别重复
            list.add(nums[i]);
            used[i] = true;
            helper4(nums, list, used, res);
            list.remove(list.size() - 1);
            used[i] = false;
        }
    }

    public static void main(String[] args) {
        log(permuteUnique4(new int[]{1, 1, 2}));     // expects [[1,1,2], [1,2,1], [2,1,1]]
        log(permuteUnique4(new int[]{1, 2, 1}));     // expects [[1,1,2], [1,2,1], [2,1,1]]
        log(permuteUnique4(new int[]{1, 1, 2, 1}));  // expects [[1,1,1,2], [1,1,2,1], [1,2,1,1], [2,1,1,1]]
        log(permuteUnique4(new int[]{1, 2}));        // expects [[1,2], [2,1]]
        log(permuteUnique4(new int[]{1}));           // expects [[1]]
        log(permuteUnique4(new int[]{}));            // expects []
    }
}
