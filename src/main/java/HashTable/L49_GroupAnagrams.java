package HashTable;

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
    * - 思路：将排序过的 strs 元素作为查找内容，因此查找表结构为 {"aer": ["are","ear,"era"]}。
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
        return new ArrayList<>(map.values());  // 这个写法很简洁，List 构造函数可以接受一个 Collection。
    }

    /*
    * 解法2：查找表
    * - 思路：∵ anagram 的特点是两个字符串中每个字符的 count 相同 ∴ 可以将 strs 元素按照字符频次进行 encode。
    *        然后作为查找表的查找内容，这样查找表结构可以是 {"#2#1#0...#0": ["aab", "baa", "aba"]}。
    * - 时间复杂度 O(nk)，n 为 strs 元素个数，k 为 strs 元素的最大字符个数。
    * - 空间复杂度 O(nk)，即查找表的大小（字符集是常数级的因此没考虑在内g）。
    * */
    public static List<List<String>> groupAnagrams2(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        int[] freq = new int[26];

        for (String s : strs) {  // O(n)
            Arrays.fill(freq, 0);
            for (char c : s.toCharArray()) freq[c - 'a']++;  // 注意字符集范围，此处假设字符集是 a-z。时间复杂度 O(k)

            StringBuilder b = new StringBuilder();
            for (int i = 0; i < freq.length; i++) {  // O(k)
                b.append('#');
                b.append(freq[i]);
            }

            String key = b.toString();
            if (!map.containsKey(key))
                map.put(key, new ArrayList<>());
            map.get(key).add(s);
        }
        return new ArrayList<>(map.values());
    }

    public static void main(String[] args) {
        String[] strs = {"eat", "tea", "tan", "ate", "nat", "bat"};

        log(groupAnagrams(strs));   // [["ate","eat","tea"], ["nat","tan"], ["bat"]]
        log(groupAnagrams2(strs));  // [["ate","eat","tea"], ["nat","tan"], ["bat"]]
    }
}
