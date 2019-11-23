package RecursionAndBackTracking.Permutations;

import static Utils.Helpers.*;

import java.util.ArrayList;
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
     * 解法1：Recursion + Backtracking
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
     * 解法2：Iteration
     * - 思路：在 L46_Permutations 解法2的基础上加入用于去重的 Set。
     * - 时间复杂度 O(n^n)，空间复杂度 O(n^n)。
     * */
    public static List<List<Integer>> permuteUnique2(int[] nums) {
        if (nums.length == 0) return new ArrayList<>();
        Set<List<Integer>> set = new HashSet<>();
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

        return new ArrayList<>(new HashSet<>(q));
    }

    public static void main(String[] args) {
        log(permuteUnique2(new int[]{1, 1, 2}));  // expects [[1,1,2], [1,2,1], [2,1,1]]
        log(permuteUnique2(new int[]{1, 2}));     // expects [[1,2], [2,1]]
        log(permuteUnique2(new int[]{1}));        // expects [[1]]
        log(permuteUnique2(new int[]{}));         // expects []
    }
}
