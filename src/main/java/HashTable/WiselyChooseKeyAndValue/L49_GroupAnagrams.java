package HashTable.WiselyChooseKeyAndValue;

import java.util.*;

import static Utils.Helpers.log;

/*
* L49 Group Anagrams
*
* - Given an array of strings, group anagrams together (the output order doesn't matter).
* - 思路：找到 anagram 的特点 —— Two strings are anagrams if and only if their character counts are the same.
* */

public class L49_GroupAnagrams {
    /*
    * 解法1：查找表
    * - 思路：将 strs 中的每个字符串排序后作为查找表的 key（即查找内容），因此查找表的结构为 {"aer": ["are","ear,"era"]}。
    * - 时间复杂度 O(nklogk)，n 为 strs 元素个数，k 为 strs 元素的最大字符个数。
    * - 空间复杂度 O(nk)，即查找表的大小。
    * */
    public static List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();

        for (String s : strs) {                // O(n)
            char[] chars = s.toCharArray();
            Arrays.sort(chars);                // O(klogk)
            String key = String.valueOf(chars);
            if (!map.containsKey(key))
                map.put(key, new ArrayList<>());
            map.get(key).add(s);
        }

        return new ArrayList<>(map.values());  // 直接从 map.values() 构造 ArrayList（构造函数可以接受 Collection）。
    }

    /*
    * 解法2：查找表
    * - 思路：∵ anagram 的特点是两个字符串中每个字符的出现频率相同 ∴ 建立的查找表的 key（即查找内容）应该能体现这一特点 ——
    *   从 strs 中的每个字符串里提取每个字符的频次信息，并 encode 成为查找表的 key。这样具有相同字符频率的字符串通过查找表后
    *   就会被 add 到同一个 key 所对应的 List 中。
    *   - 其中 encode 的规则是："aab" -> "#2#1#0#0#0...#0"（2个a、1个b，其他字符都是0个，用"#"分隔是为了不混淆前后字母的频率）；
    *   - 因此查找表的结构是：{"#2#1#0#0#0...#0": ["aab", "baa", "aba"]}。
    * - 时间复杂度 O(nk)，n 为 strs 元素个数，k 为 strs 元素的最大字符个数。
    * - 空间复杂度 O(nk)，即查找表的大小（字符集是常数级的因此没考虑在内g）。
    * */
    public static List<List<String>> groupAnagrams2(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();  // 查找表的结构与解法1中一致
        int[] freq = new int[26];

        for (String s : strs) {             // 遍历 strs，为每个字符串进行 encode
            Arrays.fill(freq, 0);      // freq 是复用的，每次用之前先清空
            for (char c : s.toCharArray())  // O(k)
                freq[c - 'a']++;            // 此处 assume 字符集是 a-z

            StringBuilder b = new StringBuilder();
            for (int i = 0; i < freq.length; i++) {  // 进行 encode。O(k)
                b.append('#');
                b.append(freq[i]);
            }
            String key = b.toString();               // encode 结果

            if (!map.containsKey(key))               // 构建查找表
                map.put(key, new ArrayList<>());
            map.get(key).add(s);
        }

        return new ArrayList<>(map.values());  // 最后也是从 map.values() 直接建立 ArrayList
    }

    public static void main(String[] args) {
        log(groupAnagrams(new String[]{"eat", "tea", "tan", "ate", "nat", "bat"}));  // [["ate","eat","tea"], ["nat","tan"], ["bat"]]
        log(groupAnagrams(new String[]{}));  // []
    }
}
