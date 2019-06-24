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
    * - 思路：要求 most frequent elements，自然想到先用 map 统计所有元素的出现频率。之后问题就只剩下如何从 map 中选出频率
    *   最高的 k 个 entry 了。
    * - 时间复杂度 O(nlogn)，空间复杂度 O(n)。
    * */
    public static List<Integer> topKFrequent(int[] nums, int k) {
        List<Integer> res = new ArrayList<>();
        if (nums.length == 0) return res;

        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums)  // O(n)
            freq.put(n, freq.getOrDefault(n, 0) + 1);

        Queue<Integer> pq = new PriorityQueue<>((a, b) -> freq.get(b) - freq.get(a));  // 创建最大堆
        for (int key : freq.keySet())  // O(nlogn)（freq 中最多有 n 个 entry，pq 会对每个 entry 进行一次 sift up，因此是 O(n * logn)）
            pq.offer(key);

        for (int i = 0; i < k; i++)  // O(n)
            res.add(pq.poll());

        return res;
    }

    public static void main(String[] args) {
        log(topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2));        // expects [1, 2]
        log(topKFrequent(new int[]{4, 1, -1, 2, -1, 2, 3}, 2));   // expects [-1, 2]
        log(topKFrequent(new int[]{1}, 1));                       // expects [1]
        log(topKFrequent(new int[]{}, 1));                        // expects []
    }
}
