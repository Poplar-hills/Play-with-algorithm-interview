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
     * - 思路：先为 s 生成频谱，再让频谱根据 value 进行排序，最后再根据排序后的频谱生成结果字符串。重点在于如何让频谱根据 value
     *   进行排序。首先 HashMap 无法排序 ∴ 需要借助外部数据结构。在所有数据结构中，TreeMap 和 PriorityQueue 具有排序能力。本
     *   解法使用 TreeMap 辅助排序。
     * - 实现：注意 TreeMap 本身是根据 key 的插入顺序排序的，若要定制排序机制则需定制 comparator：
     *   - 若返回1，则认为 a, b 有序，不需要交换；
     *   - 若返回-1，则认为 a, b 乱序，需要交换；
     *   - 若返回0，则认为 a, b 相等，不需要交换（但会认为 a, b 两个 key 相等，从而用 b 覆盖掉 a ∴ 不能返回0）；
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static String frequencySort(final String s) {
        Map<Character, Integer> freq = new HashMap<>();  // ∵ 要借助外部 TreeMap 进行排序 ∴ 这里就不用再使用 TreeMap 了
        for (char c : s.toCharArray())
            freq.merge(c, 1, Integer::sum);

        Map<Character, Integer> sortedMap = new TreeMap<>((a, b) -> {  // 指定 TreeMap 的 Comparator
            int diff = freq.get(b) - freq.get(a);                      // 按频率从大到小排列
            return diff == 0 ? 1 : diff;  // 若 a, b 的频率相等，则返回1，使 a, b 两个 key 原地不动（不交换位置也不覆盖）
        });
        sortedMap.putAll(freq);           // 放入 TreeMap 中排序

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Character, Integer> en : sortedMap.entrySet())  // 遍历排序后的 TreeMap 构建结果字符串
            for (int i = 0; i < en.getValue(); i++)
                sb.append(en.getKey());

        return sb.toString();
    }

    /*
     * 解法2：Map 频谱 + PriorityQueue 排序（解法1的 PriorityQueue 版）
     * - 思路：思路与解法1一致。
     * - 实现：
     *   1. 使用 PriorityQueue 替代解法1中的 TreeMap 来根据 value 对频谱 freq 进行排序；
     *   2. PriorityQueue 默认是最小堆，需要自定义 Comparator 才能得到最大堆；
     *   3. ∵ PriorityQueue 不是 Map，key 不需要唯一 ∴ 不存在解法1中 Comparator 不能返回0的问题。
     * - 👉 语法：
     *   - ∵ PriorityQueue 继承了 Collection ∴ 有 addAll 方法（List, Set, Queue 都是 Collection）；
     *   - 注意 Map 没有继承 Collection（Java 有意这么设计的），它有自己的 putAll 方法。
     * - 时间复杂度 O(nlogn)，空间复杂度 O(n)。
     * */
    public static String frequencySort2(String s) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : s.toCharArray())
            freq.put(c, freq.getOrDefault(c, 0) + 1);

        PriorityQueue<Character> pq = new PriorityQueue<>((a, b) -> freq.get(b) - freq.get(a));  // 最大堆
        pq.addAll(freq.keySet());  // 这里只需 add 所有 key 进去就可以排序

        StringBuilder sb = new StringBuilder();
        while (!pq.isEmpty()) {
            char c = pq.poll();
            for (int i = 0; i < freq.get(c); i++)
                sb.append(c);
        }
        return sb.toString();
    }

    /*
     * 解法3：Map + buckets
     * - 思路：与解法1、2相同。
     * - 实现：使用 buckets 方式根据 value 对频谱 freq 进行排序。buckets 的索引表示频次，值是出现了该频次的所有字符组成的列表。
     *   例如对于 s="tree"，构造出的 List[] buckets = [ null, List.of('t', 'r'), List.of('e'), null ]。
     * - 👉语法：
     *   1. List.of() 是 Java 9中的方法，构造的是 immutable List（定长、元素不可变）；
     *   2. Arrays.asList() 构造的是 mutable List（定长，但元素可变）；
     *   SEE: https://stackoverflow.com/questions/46579074/what-is-the-difference-between-list-of-and-arrays-aslist
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static String frequencySort3(String s) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : s.toCharArray())
            freq.put(c, freq.getOrDefault(c, 0) + 1);

        List<Character>[] buckets = new List[s.length() + 1];  // 注意要数组长度要+1（∵ 有 [0, s.length()] 种可能）
        for (char c : freq.keySet()) {
            int f = freq.get(c);
            if (buckets[f] == null)
                buckets[f] = new ArrayList<>();
            buckets[f].add(c);
        }

        StringBuilder b = new StringBuilder();
        for (int i = buckets.length - 1; i >= 0; i--)  // 倒序遍历 buckets
            if (buckets[i] != null)
                for (char c : buckets[i])              // 遍历一个 bucket 中列表里的每个字符
                    for (int n = 1; n <= i; n++)
                        b.append(c);

        return b.toString();
    }

    public static void main(String[] args) {
        log(frequencySort("tree"));    // expects "eert" or "eetr"
        log(frequencySort("cccaaa"));  // expects "cccaaa" or "aaaccc"
        log(frequencySort("Aabb"));    // expects "bbAa" or "bbaA"
        log(frequencySort("eeeee"));   // expects "eeeee"
    }
}