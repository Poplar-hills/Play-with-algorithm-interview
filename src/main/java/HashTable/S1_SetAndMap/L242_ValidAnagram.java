package HashTable.S1_SetAndMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static Utils.Helpers.log;

/*
 * Valid Anagram
 *
 * - Determine if string t is an anagram (由颠倒字母顺序而构成的词) of string s.
 *
 * - 思路：找到 anagram 的特点 —— Two strings are anagrams if and only if their character counts are the same. 即
 *   若两个字符串中各自字符的频率相同，则为 anagram。
 * */

public class L242_ValidAnagram {
    /*
     * 解法1：排序
     * - 时间复杂度 O(nlogn)，空间复杂度 O(1)。
     * */
    public static boolean isAnagram(String s, String t) {
        if (t.length() != s.length()) return false;

        char[] sArr = s.toCharArray();
        char[] tArr = t.toCharArray();
        Arrays.sort(sArr);
        Arrays.sort(tArr);

        return Arrays.equals(sArr, tArr);  // 判断两数组相等的方法
    }

    /*
     * 解法2：Map
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean isAnagram2(String s, String t) {
        if (s.length() != t.length()) return false;

        Map<Character, Integer> map = new HashMap<>();
        for (char c : s.toCharArray())
            map.merge(c, 1, Integer::sum);  // 相当于 map.put(c, map.getOrDefault(c, 0) + 1);

        for (char c : t.toCharArray()) {
            map.merge(c, -1, Integer::sum);
            if (map.get(c) == 0) map.remove(c);
        }
        return map.isEmpty();
    }

    /*
     * 解法3：Map
     * - 思路：与解法2一致。
     * - 实现：在同一次遍历中同时加、同时减。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean isAnagram3(String s, String t) {
        if (t.length() != s.length()) return false;

        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {        // 第一行已经处理过长度不等的情况，因此这里可以一次遍历处理两个字符串
            map.merge(s.charAt(i), 1, Integer::sum);
            map.merge(t.charAt(i), -1, Integer::sum);
        }

        for (int v : map.values())  // 遍历 map 检查是否每个 value 是否都为0
            if (v != 0)
                return false;

        return true;
    }

    /*
     * 解法4：解法3的 int[256] 版
     * - 思路：与解法2、3一致。
     * - 实现：使用 int[] 作为查找表。
     * - 时间复杂度 O(n)，空间复杂度 O(len(charset))。
     * */
    public static boolean isAnagram4(String s, String t) {
        if (t.length() != s.length()) return false;

        int[] freq = new int[256];  // 使用数组比使用 map 开销小很多
        for (int i = 0; i < s.length(); i++) {
            freq[s.charAt(i)]++;
            freq[t.charAt(i)]--;
        }

        for (int f : freq)
            if (f != 0)
                return false;

        return true;
    }

    public static void main(String[] args) {
        log(isAnagram3("anagram", "nagaram"));  // expects true
        log(isAnagram3("weq", "qwe"));          // expects true
        log(isAnagram3("xx", "xx"));            // expects true
        log(isAnagram3("rat", "car"));          // expects false
        log(isAnagram3("abcd", "abc"));         // expects false
        log(isAnagram3("aacc", "ccac"));        // expects false
        log(isAnagram3("zzz", "zz"));           // expects false
    }
}
