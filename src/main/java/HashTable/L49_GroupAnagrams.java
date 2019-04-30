package HashTable;

import java.util.*;

import static Utils.Helpers.log;

/*
* L49 Group Anagrams
*
* Given an array of strings, group anagrams together (the output order doesn't matter).
* */

public class L49_GroupAnagrams {
    /*
    * 解法1：查找表
    * - 思路：将排序过的 strs 元素作为查找内容，因此查找表结构为 {"aer": ["are","ear,"era"]}。
    * - 时间复杂度 O(nklogk)，n 为 strs 元素个数，k 为 strs 元素的最大字符个数。
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
        return new ArrayList<>(map.values());  // 这个写法很简洁，List 构造函数可以接受一个 Collection
    }

    /*
    * 解法2：查找表
    * - 思路：将 encode 过的 strs 元素作为查找内容，因此查找表结构为 {"#2#1#0...#0": ["aab", "baa", "aba"]}。
    * */
    public static List<List<String>> groupAnagrams2(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        int[] freq = new int[26];
        for (String s : strs) {
            Arrays.fill(freq, 0);
            for (char c : s.toCharArray()) freq[c - 'a']++;  // 注意字符集范围，此处假设字符集是 a-z

            StringBuilder b = new StringBuilder();
            for (int i = 0; i < freq.length; i++) {
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
