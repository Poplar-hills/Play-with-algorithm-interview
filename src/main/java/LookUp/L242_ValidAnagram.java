package LookUp;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static Utils.Helpers.log;

/*
* Valid Anagram
*
* - Determine if string t is an anagram of string s.
* */

public class L242_ValidAnagram {
    public static boolean isAnagram(String s, String t) {  // 解法1：使用 map，时间复杂度 O(n)，空间复杂度 O(n)
        if (t.length() != s.length())
            return false;

        Map<Character, Integer> map = new HashMap<>();
        for (char c : s.toCharArray())  // O(n)
            map.put(c, map.getOrDefault(c, 0) + 1);

        for (char c : t.toCharArray()) {  // O(n)
            if (!map.containsKey(c))
                return false;
            if (map.get(c) > 0) {
                map.put(c, map.get(c) - 1);
                if (map.get(c) == 0)
                    map.remove(c);
            }
        }
        return map.isEmpty();
    }

    public static boolean isAnagram2(String s, String t) {  // 解法2：使用 map，时间复杂度 O(n)，空间复杂度 O(n)
        if (t.length() != s.length())
            return false;

        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {  // O(n)，因为之前已经处理过长度不等的情况，因此这里可以一次遍历处理两个字符串
            char sc = s.charAt(i);
            map.put(sc, map.getOrDefault(sc, 0) + 1);  // 一个加
            char tc = t.charAt(i);
            map.put(tc, map.getOrDefault(tc, 0) - 1);  // 一个减
        }

        for (int n : map.values())  // O(n)，这里需要遍历 map
            if (n != 0)
                return false;
        return true;
    }

    public static boolean isAnagram3(String s, String t) {  // 解法3：排序，时间复杂度 O(nlogn)，空间复杂度 O(1)
        if (t.length() != s.length())
            return false;
        char[] sArr = s.toCharArray();
        char[] tArr = t.toCharArray();
        Arrays.sort(sArr);
        Arrays.sort(tArr);
        return Arrays.equals(sArr, tArr);
    }

    public static void main(String[] args) {
        log(isAnagram("anagram", "nagaram"));  // expects true
        log(isAnagram("rat", "car"));          // expects false
        log(isAnagram("abcd", "abc"));         // expects false
        log(isAnagram("aacc", "ccac"));        // expects false

        log(isAnagram2("anagram", "nagaram"));  // expects true
        log(isAnagram2("rat", "car"));          // expects false
        log(isAnagram2("abcd", "abc"));         // expects false
        log(isAnagram2("aacc", "ccac"));        // expects false
    }
}
