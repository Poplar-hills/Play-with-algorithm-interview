package LookUp;

import java.util.*;

import static Utils.Helpers.log;

/*
* Sort Characters By Frequency
*
* - 按照字母出现频率的倒序重组整个字符串。
* */

public class L451_SortCharactersByFrequency {
    /*
    * 解法1：Map + PriorityQueue
    * - 使用 Map 记录字符出现频次，使用 PriorityQueue 对字符按照出现频次排序，最后再根据频次排序构建字符串。
    * - 时间复杂度 O(nlogn)，空间复杂度 O(n)。
    * */
    public static String frequencySort(String s) {
        Map<Character, Integer> freq = new HashMap<>();  // {t: 1, r: 1, e: 2}

        for (char c : s.toCharArray())  // 时间 O(n)，空间 O(n)
            freq.put(c, freq.getOrDefault(c, 0) + 1);

        PriorityQueue<Character> heap = new PriorityQueue<>((c1, c2) -> freq.get(c2) - freq.get(c1));
        heap.addAll(freq.keySet());  // 时间 O(nlogn)，空间 O(n)

        StringBuilder b = new StringBuilder();
        while (!heap.isEmpty()) {  // 时间 O(n)，空间 O(1)
            char c = heap.poll();
            for (int i = 1; i <= freq.get(c); i++)
                b.append(c);
        }
        return b.toString();
    }

    /*
     * 解法2：Map + List[]
     * - 与解法1相同的是使用 Map 记录字符出现频次，不同的是记录字符出现频次的方式改用 buckets。
     *   buckets 的索引是频次，值是出现了该频次的所有字符组成的列表。最后再根据 buckets 构建字符串。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static String frequencySort2(String s) {
        Map<Character, Integer> freq = new HashMap<>();  // {t: 1, r: 1, e: 2}

        for (char c : s.toCharArray())  // 时间 O(n)，空间 O(n)
            freq.put(c, freq.getOrDefault(c, 0) + 1);

        List<Character>[] buckets = new List[s.length() + 1];  // [null, [t, r], [e], null]，注意要 s.length()+1（考虑"eeeee"的情况）
        for (char c : freq.keySet()) {  // 时间 O(n)，空间 O(n)
            int f = freq.get(c);
            if (buckets[f] == null)
                buckets[f] = new ArrayList<>();
            buckets[f].add(c);
        }

        StringBuilder b = new StringBuilder();
        for (int i = buckets.length - 1; i >= 0; i--) {  // 遍历 buckets 中每个列表中的每个字符。时间 O(n)，空间 O(1)
            if (buckets[i] != null)
                for (char c : buckets[i])
                    for (int j = 1; j <= i; j++)
                        b.append(c);
        }

        return b.toString();
    }

    public static void main(String[] args) {
        log(frequencySort("tree"));     // expects "eert" or "eetr"
        log(frequencySort("cccaaa"));   // expects "cccaaa" or "aaaccc"
        log(frequencySort("Aabb"));     // expects "bbAa" or "bbaA"
        log(frequencySort("eeeee"));    // expects "eeeee"

        log(frequencySort2("tree"));     // expects "eert" or "eetr"
        log(frequencySort2("cccaaa"));   // expects "cccaaa" or "aaaccc"
        log(frequencySort2("Aabb"));     // expects "bbAa" or "bbaA"
        log(frequencySort2("eeeee"));    // expects "eeeee"
    }
}