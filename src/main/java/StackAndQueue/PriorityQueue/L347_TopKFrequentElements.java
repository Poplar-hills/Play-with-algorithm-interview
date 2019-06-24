package StackAndQueue.PriorityQueue;

import java.util.*;

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
    * 解法1：map + sort（merge sort）
    * - 思路：要求 most frequent elements，自然想到先用 map 统计所有元素的出现频率。之后问题就是如何从 map 中选出频率
    *   最高的 k 个 key 了，最直接的实现就是排序 —— 对 map 中的 key 根据 value 进行排序，最后拿到前 k 个最大的。
    * - 时间复杂度 O(nlogn)，空间复杂度 O(n)。
    * */
    public static List<Integer> topKFrequent(int[] nums, int k) {
        List<Integer> res = new ArrayList<>();
        if (nums.length == 0) return res;

        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums)
            freq.put(n, freq.getOrDefault(n, 0) + 1);

        for (int key : freq.keySet()) res.add(key);
        res.sort((a, b) -> freq.get(b) - freq.get(a));  // sort 底层是把 list 转成 array 进行 Arrays.sort（本质是 merge sort，且对近乎有序的数组有很好的优化）

        return res.subList(0, k);
    }

    /*
    * 解法2：map + sort (heap sort)
    * - 思路：与解法1相同，排序选择的是堆排序；堆排序的实现可以选择 PriorityQueue，因为底层实现是堆（默认是最小堆，但这里需要最大堆）。
    * - 时间复杂度 O(nlogn)，空间复杂度 O(n)。
    * */
    public static List<Integer> topKFrequent2(int[] nums, int k) {
        List<Integer> res = new ArrayList<>();
        if (nums.length == 0) return res;

        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums)
            freq.put(n, freq.getOrDefault(n, 0) + 1);

        Queue<Integer> pq = new PriorityQueue<>((a, b) -> freq.get(b) - freq.get(a));  // 创建最大堆
        pq.addAll(freq.keySet());  // 装入所有元素来进行堆排序

        for (int i = 0; i < k; i++)  // 最后 poll 出 k 个元素
            res.add(pq.poll());

        return res;
    }

    /*
     * 解法3：map + PriorityQueue (最小堆)
     * - 思路：与解法1、2不同，该解法不进行排序，而是充分利用最小堆的特性求解：最小堆中只保留 k 个频率最大的 key，而频率小的 key
     *   会在向堆中添加 key 的过程中不断被 sift up 到堆顶，最后被移除出去，从而堆中只剩下频率最大的 k 个 key。
     * - 注：最后得到的结果的元素顺序可能跟解法1、2不同。
     * - 时间复杂度 O(nlogk)，空间复杂度 O(n)。∵ 一直在只有 k 个元素的优先队列中工作，因此 poll, offer 都是 O(logk) 级别的。
     *   - 这个时间复杂度在 n 和 k 差距较大的情况下优势较明显，若 n 和 k 相近则没什么优势。
     * */
    public static List<Integer> topKFrequent3(int[] nums, int k) {
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
                pq.poll();      // 因为 pq 是最小堆，每次 poll 会移除频率最小的 key，而最后留在 pq 中的就是频率最大的 k 个 key。时间复杂度为 O(logk)
                pq.offer(key);  // O(logk)
            }
        }

        while (!pq.isEmpty())
            res.add(pq.poll());  // 注意：因为 pq 是最小堆，通过 while poll 出到 res 中的是倒序的（频率小的先进入 res，频率大的后入）

        return res;
    }

    /*
    * 解法4：
    * - 思路：
    * - 时间复杂度 O(nlog(n-k))，空间复杂度 O(n)。
    * */
    public static List<Integer> topKFrequent4(int[] nums, int k) {

    }

    public static void main(String[] args) {
        log(topKFrequent4(new int[]{1, 1, 1, 2, 2, 3}, 2));        // expects [1, 2] or [2, 1]
        log(topKFrequent4(new int[]{4, 1, -1, 2, -1, 2, 3}, 2));   // expects [-1, 2] or [2, -1]
        log(topKFrequent4(new int[]{1}, 1));                       // expects [1]
        log(topKFrequent4(new int[]{}, 1));                        // expects []
    }
}
