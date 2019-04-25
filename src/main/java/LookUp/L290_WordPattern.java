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
        String[] words = str.split(" ");
        if (pattern.length() != words.length)
            return false;

        Map<Character, String> charToWord = new HashMap<>();
        Map<String, Character> wordToChar = new HashMap<>();

        for (int i = 0; i < words.length; i++) {
            String w = words[i];
            char c = pattern.charAt(i);

            if (charToWord.containsKey(c) && !charToWord.get(c).equals(w))
                return false;
            if (wordToChar.containsKey(w) && wordToChar.get(w) != c)
                return false;

            charToWord.put(c, w);
            wordToChar.put(w, c);
        }

        return true;
    }

    public static void main(String[] args) {
        log(wordPattern("abba", "dog cat cat dog"));   // true
        log(wordPattern("abba", "dog cat cat fish"));  // false
        log(wordPattern("aaaa", "dog cat cat dog"));   // false
        log(wordPattern("aaaa", "dog dog dog dog"));   // true
        log(wordPattern("abba", "dog dog dog dog"));   // false
        log(wordPattern("jquery", "jquery"));          // false
    }
}
