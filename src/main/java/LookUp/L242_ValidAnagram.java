package LookUp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static Utils.Helpers.log;

/*
* Valid Anagram
*
* - Determine if string t is an anagram of string s.
* */

public class L242_ValidAnagram {
    public static boolean isAnagram(String s, String t) {  // 解法1
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

    public static boolean isAnagram2(String s, String t) {  // 解法2
        if (t.length() != s.length())
            return false;

        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {  // O(n)
            char sc = s.charAt(i);
            map.put(sc, map.getOrDefault(sc, 0) + 1);
            char tc = t.charAt(i);
            map.put(tc, map.getOrDefault(tc, 0) - 1);
        }

        for (int n : map.values())  // O(n)
            if (n != 0)
                return false;
        return true;
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
