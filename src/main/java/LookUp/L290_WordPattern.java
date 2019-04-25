package LookUp;

import java.util.HashMap;
import java.util.Map;

import static Utils.Helpers.log;

/*
* Word Pattern
*
* - 判断一个字符串是否符合指定 pattern
* */

public class L290_WordPattern {
    /*
    * 解法1：使用两个 map 分别记录 pattern -> str 的映射，以及 str -> pattern 的映射。
    * - 时间复杂度 O(n)，空间复杂度 O(n)
    * */
    public static boolean wordPattern(String pattern, String str) {
        Map<Character, String> charToStr = new HashMap<>();
        Map<String, Character> strToChar = new HashMap<>();
        char[] chars = pattern.toCharArray();
        String[] strs = str.split(" ");

        if (chars.length != strs.length)
            return false;

        for (int i = 0; i < chars.length; i++) {
            String s = strs[i];
            char c = chars[i];

            if (charToStr.containsKey(c) && !charToStr.get(c).equals(s))
                return false;
            if (strToChar.containsKey(s) && strToChar.get(s) != c)
                return false;

            charToStr.put(c, s);
            strToChar.put(s, c);
        }

        return true;
    }

    public static boolean wordPattern2(String pattern, String str) {

        return false;
    }

    public static void main(String[] args) {
        log(wordPattern("abba", "dog cat cat dog"));   // true
        log(wordPattern("abba", "dog cat cat fish"));  // false
        log(wordPattern("aaaa", "dog cat cat dog"));   // false
        log(wordPattern("aaaa", "dog dog dog dog"));   // true
        log(wordPattern("abba", "dog dog dog dog"));   // false
        log(wordPattern("jquery", "jquery"));   // false
    }
}
