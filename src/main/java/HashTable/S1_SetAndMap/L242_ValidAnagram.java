package HashTable.S1_SetAndMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static Utils.Helpers.log;

/*
* Valid Anagram
*
* - Determine if string t is an anagram (由颠倒字母顺序而构成的词) of string s.
* - 思路：找到 anagram 的特点 —— Two strings are anagrams if and only if their character counts are the same. 即
*   若两个字符串中各自字符的频率相同，则为 anagram。
* */

public class L242_ValidAnagram {
    /*
    * 解法1：排序
    * - 时间复杂度 O(nlogn)，空间复杂度 O(1)。
    * */
    public static boolean isAnagram(String s, String t) {
        if (t.length() != s.length())
            return false;
        char[] sArr = s.toCharArray();
        char[] tArr = t.toCharArray();
        Arrays.sort(sArr);
        Arrays.sort(tArr);
        return Arrays.equals(sArr, tArr);
    }

    /*
    * 解法2：使用 Map
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static boolean isAnagram2(String s, String t) {
        if (t.length() != s.length()) return false;

        Map<Character, Integer> map = new HashMap<>();
        for (char c : s.toCharArray())
            map.put(c, map.getOrDefault(c, 0) + 1);  // 记录所有字符的频率

        for (char c : t.toCharArray()) {
            if (!map.containsKey(c)) return false;
            if (map.get(c) > 0) {
                map.put(c, map.get(c) - 1);  // 让频率-1
                if (map.get(c) == 0)
                    map.remove(c);
            }
        }
        return map.isEmpty();
    }

    /*
    * 解法3：使用 Map
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static boolean isAnagram3(String s, String t) {
        if (t.length() != s.length()) return false;

        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {  // 第一行已经处理过长度不等的情况，因此这里可以一次遍历处理两个字符串
            char sc = s.charAt(i);
            char tc = t.charAt(i);
            map.put(sc, map.getOrDefault(sc, 0) + 1);  // 一个加
            map.put(tc, map.getOrDefault(tc, 0) - 1);  // 一个减
        }

        for (int n : map.values())  // 遍历 map 检查是否每个 key 的值都是0
            if (n != 0)
                return false;
        return true;
    }

    /*
    * 解法4：思路与解法3一致，只是使用数组作为字典
    * - 时间复杂度 O(n)，空间复杂度 O(len(charset))。
    * */
    public static boolean isAnagram4(String s, String t) {
        if (t.length() != s.length())
            return false;

        int[] freq = new int[256];  // 使用数组比使用 map 开销小很多
        for (int i = 0; i < s.length(); i++) {
            freq[s.charAt(i)]++;
            freq[t.charAt(i)]--;
        }
        for (int n : freq)
            if (n != 0)
                return false;
        return true;
    }

    public static void main(String[] args) {
        log(isAnagram4("anagram", "nagaram"));  // expects true
        log(isAnagram4("weq", "qwe"));          // expects true
        log(isAnagram4("rat", "car"));          // expects false
        log(isAnagram4("abcd", "abc"));         // expects false
        log(isAnagram4("aacc", "ccac"));        // expects false
    }
}
