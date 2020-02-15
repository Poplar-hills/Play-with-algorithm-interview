package HashTable.S1_SetAndMap;

import java.util.*;

import static Utils.Helpers.log;

/*
 * Sort Characters By Frequency
 *
 * - 按照字母出现频率的倒序重组整个字符串。
 * */

public class L451_SortCharactersByFrequency {
    /*
     * 解法1：Map 频谱 + TreeMap 排序
     * - 思路：先为 s 生成频谱，再让频谱根据 value 进行排序，最后再根据排序后的频谱生成结果字符串。
     * - 实现：
     *   1. 重点在于如何让频谱根据 value 进行排序。首先只有 TreeMap 具有有序性质，但 TreeMap 自身只能根据 key 排序，若要
     *      根据 value 排序则需要借助另一个外部 TreeMap；
     *   2. 外部 TreeMap 需要自定义 Comparator，但要注意：
     *      a. Comparator 的特性是：
     *        - 若返回-1，则认为 a, b 乱序，需要交换；
     *        - 若返回0，则认为 a, b 相等，不需要交换；
     *        - 若返回1，则认为 a, b 有序，不需要交换；
     *      b. ∵ 该 Comparator 要用于 Map 中，若返回0，则 Map 会认为 a, b 两个 key 相等，从用 b 覆盖掉 a ∴ 只能返回1。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static String frequencySort(final String s) {
        Map<Character, Integer> freq = new HashMap<>();  // ∵ 要借助外部 TreeMap 进行排序 ∴ 这里就不用再使用 TreeMap 了
        for (char c : s.toCharArray())
            freq.merge(c, 1, Integer::sum);

        Map<Character, Integer> sortedMap = new TreeMap<>((a, b) -> {  // 指定 TreeMap 的 Comparator
            int compare = freq.get(b) - freq.get(a);                   // 按 value 的值从大到小排列
            return compare == 0 ? 1 : compare;  // 若 a, b 的 value 相等，则返回1，使 a, b 两个 Entry 原地不动（不交换位置也不覆盖）
        });
        sortedMap.putAll(freq);                 // 将存有频次的 map 中的所有 Entry 都放入 TreeMap 中排序

        StringBuilder b = new StringBuilder();
        for (Map.Entry<Character, Integer> en : sortedMap.entrySet())  // 遍历排序后的 TreeMap 构建结果字符串
            for (int i = 0; i < en.getValue(); i++)
                b.append(en.getKey());

        return b.toString();
    }

    /*
     * 解法2：Map 频谱 + PriorityQueue 排序（解法1的 PriorityQueue 版）
     * - 思路：思路与解法1一致。
     * - 实现：
     *   1. 使用 PriorityQueue 替代解法1中的 TreeMap 来根据 value 对 freq 进行排序；
     *   2. PriorityQueue 默认是最小堆，需要自定义 Comparator 才能得到最大堆；
     *   3. ∵ PriorityQueue 不是 Map，key 不需要唯一 ∴ 不存在解法1中 Comparator 不能返回0的问题。
     * - 👉语法：∵ PriorityQueue 也继承了 Collection ∴ 也有 addAll 方法（List, Map, Set, Queue 都有该方法）。
     * - 时间复杂度 O(nlogn)，空间复杂度 O(n)。
     * */
    public static String frequencySort2(String s) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : s.toCharArray())
            freq.put(c, freq.getOrDefault(c, 0) + 1);

        PriorityQueue<Character> maxHeap = new PriorityQueue<>((c1, c2) -> freq.get(c2) - freq.get(c1));  // 最大堆
        maxHeap.addAll(freq.keySet());

        StringBuilder b = new StringBuilder();
        while (!maxHeap.isEmpty()) {  // 用 while 遍历 maxHeap
            char c = maxHeap.poll();
            for (int i = 1; i <= freq.get(c); i++)
                b.append(c);
        }
        return b.toString();
    }

    /*
     * 解法3：Map + List[]
     * - 思路：与解法1相同的是使用 Map 记录字符出现频次，不同的是记录字符出现频次的方式改用 buckets。
     *   buckets 的索引是频次，值是出现了该频次的所有字符组成的列表。最后再根据 buckets 构建字符串。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static String frequencySort3(String s) {
        Map<Character, Integer> freq = new HashMap<>();  // test case 1 中该 freq = {t: 1, r: 1, e: 2}

        for (char c : s.toCharArray())  // 时间 O(n)
            freq.put(c, freq.getOrDefault(c, 0) + 1);

        List<Character>[] buckets = new List[s.length() + 1];  // [null, [t,r], [e], null]. 注意要数组长度要+1（∵ 有 0 ~ s.length() 种可能）
        for (char c : freq.keySet()) {  // 时间 O(n)
            int f = freq.get(c);
            if (buckets[f] == null)
                buckets[f] = new ArrayList<>();
            buckets[f].add(c);
        }

        StringBuilder b = new StringBuilder();
        for (int i = buckets.length - 1; i >= 0; i--)  // 从大到小遍历 buckets 中每个列表中的每个字符。时间 O(n)，空间 O(1)
            if (buckets[i] != null)
                for (char c : buckets[i])
                    for (int j = 1; j <= i; j++)
                        b.append(c);

        return b.toString();
    }

    public static void main(String[] args) {
        log(frequencySort3("tree"));    // expects "eert" or "eetr"
        log(frequencySort3("cccaaa"));  // expects "cccaaa" or "aaaccc"
        log(frequencySort3("Aabb"));    // expects "bbAa" or "bbaA"
        log(frequencySort3("eeeee"));   // expects "eeeee"
    }
}