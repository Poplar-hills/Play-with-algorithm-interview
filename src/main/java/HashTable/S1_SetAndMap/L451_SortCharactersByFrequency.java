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
     *   1. 使用 PriorityQueue 替代解法1中的 TreeMap 来根据 value 对频谱 freq 进行排序；
     *   2. PriorityQueue 默认是最小堆，需要自定义 Comparator 才能得到最大堆；
     *   3. ∵ PriorityQueue 不是 Map，key 不需要唯一 ∴ 不存在解法1中 Comparator 不能返回0的问题。
     * - 👉语法：
     *   - ∵ PriorityQueue 继承了 Collection ∴ 有 addAll 方法（List, Set, Queue 都是 Collection）；
     *   - 注意 Map 没有继承 Collection（Java 有意这么设计的），它有自己的 putAll 方法。
     * - 时间复杂度 O(nlogn)，空间复杂度 O(n)。
     * */
    public static String frequencySort2(String s) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : s.toCharArray())
            freq.put(c, freq.getOrDefault(c, 0) + 1);

        PriorityQueue<Character> maxHeap = new PriorityQueue<>((a, b) -> freq.get(b) - freq.get(a));  // 最大堆
        maxHeap.addAll(freq.keySet());  // 这里只需 add 所有 key 进去就可以排序

        StringBuilder b = new StringBuilder();
        while (!maxHeap.isEmpty()) {    // 用 while 遍历 maxHeap
            char c = maxHeap.poll();
            for (int i = 1; i <= freq.get(c); i++)
                b.append(c);
        }
        return b.toString();
    }

    /*
     * 解法3：Map + buckets
     * - 思路：与解法1、2相同。
     * - 实现：使用 buckets 方式根据 value 对频谱 freq 进行排序。buckets 的索引是频次，值是出现了该频次的所有字符组成的列表。
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

        List<Character>[] buckets = new List[s.length() + 1];  // 注意要数组长度要+1（∵ 有 0~s.length() 种可能）
        for (char c : freq.keySet()) {
            int f = freq.get(c);
            if (buckets[f] == null)
                buckets[f] = new ArrayList<>();
            buckets[f].add(c);
        }

        StringBuilder b = new StringBuilder();
        for (int i = buckets.length - 1; i >= 0; i--)  // 遍历 buckets 中每个列表中的每个字符
            if (buckets[i] != null)
                for (char c : buckets[i])
                    for (int n = 1; n <= i; n++)
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