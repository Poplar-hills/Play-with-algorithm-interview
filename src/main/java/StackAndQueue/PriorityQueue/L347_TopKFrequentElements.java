package StackAndQueue.PriorityQueue;

import java.util.*;
import java.util.Map.Entry;

import static Utils.Helpers.*;

/*
* Top K Frequent Elements
*
* - Given a non-empty array of integers, return the k most frequent elements.
*
* - 注：Java 中的 PriorityQueue 在使用 iterator 遍历时不保证任何顺序性。
* */

public class L347_TopKFrequentElements {
    /*
    * 解法1：
    * - 时间复杂度 O()，空间复杂度 O()。
    * */
    public static List<Integer> topKFrequent(int[] nums, int k) {
        List<Integer> res = new ArrayList<>();
        if (nums.length == 0) return res;
        Map<Integer, Integer> freq = new HashMap<>();

        for (int n : nums) {  // O(n)
            int newFreq = freq.getOrDefault(n, 0) + 1;
            freq.put(n, newFreq);
        }

        Queue<Entry<Integer, Integer>> pq = new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());
        for (Entry<Integer, Integer> entry : freq.entrySet())  // O(nlogn)（freq 中最多有 n 个 entry，pq 会对每个 entry 进行一次 sift up，因此是 O(n * logn)）
            pq.offer(entry);

        for (int i = 0; i < k; i++)  // O(n)
            res.add(pq.poll().getKey());

        return res;
    }

    public static void main(String[] args) {
        log(topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2));        // expects [1, 2]
        log(topKFrequent(new int[]{4, 1, -1, 2, -1, 2, 3}, 2));   // expects [-1, 2]
        log(topKFrequent(new int[]{1}, 1));                       // expects [1]
        log(topKFrequent(new int[]{}, 1));                        // expects []
    }
}
