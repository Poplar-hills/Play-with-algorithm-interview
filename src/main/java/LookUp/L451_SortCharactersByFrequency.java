package LookUp;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

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
    * - 时间复杂度 O(nlogn)，空间复杂度 O(n)
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

    public static void main(String[] args) {
        log(frequencySort("tree"));     // expects "eert" or "eetr"
        log(frequencySort("cccaaa"));   // expects "cccaaa" or "aaaccc"
        log(frequencySort("Aabb"));     // expects "bbAa" or "bbaA"
    }
}