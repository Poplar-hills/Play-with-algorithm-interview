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
    * 解法1：map + PriorityQueue（最大堆）
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
        pq.addAll(freq.keySet());  // O(nlogn)（freq 中最多有 n 个 entry，pq 会对每个 entry 进行一次 sift up，因此是 O(n * logn)）

        for (int i = 0; i < k; i++)  // O(n)
            res.add(pq.poll());

        return res;
    }

    /*
     * 解法2：map + PriorityQueue (最小堆)
     * - 思路：与解法1思路相同，不同点在于：
     *   1. 创建最小堆而非解法1中的最大堆；
     *   2. 堆中只保留 k 个频率最大的 key，频率小的 key 会被频率大的替换出去；
     *   注：最后得到的结果的元素顺序可能跟解法1不同。
     * - 时间复杂度 O()，空间复杂度 O(n)。
     * */
    public static List<Integer> topKFrequent2(int[] nums, int k) {
        List<Integer> res = new ArrayList<>();
        if (nums.length == 0) return res;

        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums)      // O(n)
            freq.put(n, freq.getOrDefault(n, 0) + 1);

        Queue<Integer> pq = new PriorityQueue<>(Comparator.comparingInt(freq::get));  // 创建最小堆
        for (int key : freq.keySet()) {  // O(n)
            if (pq.size() < k)  // 在入队之前先判断 size
                pq.offer(key);
            else if (freq.get(key) > freq.get(pq.peek())) {  // 若当前 key 的频率 > 堆顶 key 的频率则移除堆顶 key、添加当前 key
                pq.poll();      // 因为 pq 是最小堆，每次 poll 会移除频率最小的，这样到最后留在 pq 中的就是频率最大的 k 个 key
                pq.offer(key);
            }
        }

        while (!pq.isEmpty())
            res.add(pq.poll());  // 注意：因为 pq 是最小堆，通过 while poll 出到 res 中的是倒序的（频率小的先进入 res，频率大的后入）

        return res;
    }

    public static void main(String[] args) {
        log(topKFrequent2(new int[]{1, 1, 1, 2, 2, 3}, 2));        // expects [1, 2] or [2, 1]
        log(topKFrequent2(new int[]{4, 1, -1, 2, -1, 2, 3}, 2));   // expects [-1, 2] or [2, -1]
        log(topKFrequent2(new int[]{1}, 1));                       // expects [1]
        log(topKFrequent2(new int[]{}, 1));                        // expects []
    }
}
