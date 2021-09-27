package StackAndQueue.S5_PriorityQueue;

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
     * 解法1：Map + sort（全排序）
     * - 思路：要求 most frequent elements，自然想到先用 map 统计所有元素的出现频率。之后问题就是如何从 map 中选出频率
     *   最高的 k 个 key 了，最直接的实现就是排序 —— 对 map 中的 key 根据 value 进行排序，最后拿到前 k 个最大的。
     * - 时间复杂度 O(nlogn)，空间复杂度 O(n)。
     * */
    public static List<Integer> topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums)
            freq.put(n, freq.getOrDefault(n, 0) + 1);

        List<Integer> res = new ArrayList<>(freq.keySet());
        res.sort((a, b) -> freq.get(b) - freq.get(a));  // sort 底层是把 list 转成 array 进行 Arrays.sort
                                                        // （本质是merge sort，且对近乎有序的数组有很好的优化）
        return res.subList(0, k);
    }

    /*
     * 解法2：Map + heap sort (全排序)
     * - 思路：与解法1相同，但排序方式是使用 PriorityQueue 进行堆排序。
     * - 实现：PriorityQueue 默认创建的是最小堆，但这里需要最大堆 ∴ 要自定义 Comparator。
     * - 时间复杂度 O(nlogn)，空间复杂度 O(n)。
     * */
    public static List<Integer> topKFrequent2(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums)
            freq.put(n, freq.getOrDefault(n, 0) + 1);

        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> freq.get(b) - freq.get(a));  // 创建最大堆
        pq.addAll(freq.keySet());    // 装入所有元素来进行堆排序

        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < k; i++)  // 最后 poll 出 k 个元素
            res.add(pq.poll());

        return res;
    }

    /*
     * 解法3：Map + TreeSet sort (全排序)
     * - 思路：与解法1、2相同，但排序方式是利用 TreeSet 插入后会对元素排序的机制完成的（💎 TreeSet、TreeMap 底层是自平衡的
     *   BST，更具体来说是红黑树）。
     * - 👉 语法：Comparator 用法 -- 在构造可排序的数据结构（如 TreeSet、PriorityQueue）时可以指定 Comparator：
     *   1. 若返回负数，表示无需交换 a、b，让 a 在前 b 在后 ∴ 是升序排列；
     *   2. 若返回正数，表示需要交换 a、b，让 b 在前 a 在后 ∴ 是降序排列；
     *   对于 [1, 2, 3] 来说：                           对于 [3, 2, 1] 来说：
     *   1. 若 (a, b) -> a - b：则结果为 [1, 2, 3]；      1. 若 (a, b) -> a - b：则结果仍为 [1, 2, 3]；
     *   2. 若 (a, b) -> b - a：则结果为 [3, 2, 1]；      2. 若 (a, b) -> b - a：则结果仍为 [3, 2, 1]；
     * - 👉 技巧：由于 TreeSet、TreeMap 底层是 BST ∴ 不允许重复元素，但可以通过👇"欺骗"比较器的方式实现重复元素上树。
     * - 时间复杂度 O(nlogn)，空间复杂度 O(n)。
     * */
    public static List<Integer> topKFrequent3(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums)
            freq.merge(n, 1, Integer::sum);

        TreeSet<Integer> set = new TreeSet<>((a, b) -> freq.get(a) != freq.get(b)  // 看 a，b 的频率是否相等
                ? freq.get(b) - freq.get(a)  // 若不等，则降序排列（频率大的在左子树上，这样遍历时会降序输出）
                : 1);                        // 若相等，则"欺骗"（∵ TreeSet 不允许重复元素，若比较器返回0，则会丢掉一个元素 ∴ 写死1让比较器结果不为0）
        set.addAll(freq.keySet());           // 向 TreeSet 中插入所有元素，O(nlogn)

        List<Integer> res = new ArrayList<>();
        for (int key : set) {                // 遍历 TreeSet 时是顺序输出（元素在 TreeSet 内部也是顺序存储的，且只能用 for (:) 遍历）
            if (res.size() >= k) break;
            res.add(key);
        }

        return res;
    }

    /*
     * 解法4：Map + Min heap
     * - 思路：与前三种解法不同，该解法不进行全排序，而是充分利用最小堆的特性 —— 让频率小的 key 不断被 sift up 到堆顶并在
     *   最后被移除出去，从而堆中最后只剩下的就是频率最大的 k 个 key。
     * - 注意 ∵ 使用的是最小堆，而最小堆的遍历顺序是从小到大 ∴ 最后结果中的元素顺序可能跟前三种解法不同。
     * - 💎 技巧：要找到 k 个最大元素，需使用最小堆；要找到 k 个最小元素，需采用最大堆。
     * - 👉 本质：其实在生成了 freq map 之后，该问题就转化成了如何找到 map 中 value 第 k 大的项，本质上就是
     *   L215_KthLargestElementInArray 问题 ∴ 可以采用类似的解法。
     * - 时间复杂度 O(nlogk)，空间复杂度 O(n)。
     * */
    public static List<Integer> topKFrequent4(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums)
            freq.merge(n, 1, Integer::sum);

        PriorityQueue<Integer> pq = new PriorityQueue<>(k + 1, Comparator.comparingInt(freq::get));
        for (int key : freq.keySet()) {  // O(nlogk)
            pq.offer(key);
            if (pq.size() == k + 1) pq.poll();  // 若 pq 中存在 k+1 个元素时，先去除最小的，再添加新的
        }

        return new ArrayList<>(pq);
    }

    /*
     * 解法5：Buckets 数组
     * - 思路：创建大小为 n+1 的 buckets 数组，下标为频次，元素为有相同频次的键值 list（例如 buckets[i] = List(num) 表示
     *   buckets[i] 中存储所有频次为 i 的元素）。
     * - 💎 技巧：Buckets 数组是通过空间换时间的方式记录频次，并按频次排序。这样无需再借助堆排序，从而降低时间复杂度。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<Integer> topKFrequent5(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums)
            freq.merge(n, 1, Integer::sum);

        List<Integer>[] buckets = new List[nums.length + 1];  // buckets 数组（频率最大时是 n ∴ 数组大小为 n+1）
        for (int key : freq.keySet()) {
            int f = freq.get(key);
            if (buckets[f] == null)
                buckets[f] = new ArrayList<>();
            buckets[f].add(key);
        }

        List<Integer> res = new ArrayList<>();
        for (int i = buckets.length - 1; i >= 0 && res.size() < k; i--)  // 从频率最大的一端开始遍历
            if (buckets[i] != null)
                res.addAll(buckets[i]);

        return res;
    }

    public static void main(String[] args) {
        log(topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2));        // expects [1, 2] or [2, 1]
        log(topKFrequent(new int[]{4, 1, -1, 2, -1, 2, 3}, 2));   // expects [-1, 2] or [2, -1]
        log(topKFrequent(new int[]{1}, 1));                       // expects [1]
    }
}
