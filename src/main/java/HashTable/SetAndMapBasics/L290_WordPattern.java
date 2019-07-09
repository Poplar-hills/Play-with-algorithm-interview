package HashTable.SetAndMapBasics;

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

    /*
     * 解法2：使用单 map，比较 pattern 中的字符和 str 中的 word 上次在 map 中出现的索引是否相等。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * - map.put(a, b) 方法返回更新之前 a 的值。该解法虽然更简洁，但语义不如解法1清晰。
     * */
    public static boolean wordPattern2(String pattern, String str) {
        String[] words = str.split(" ");
        if (words.length != pattern.length())
            return false;

        Map map = new HashMap();  // Map 的 key 既有 Character 也有 String
        for (Integer i = 0; i < words.length; i++)
            if (map.put(pattern.charAt(i), i) != map.put(words[i], i))
                return false;

        return true;
    }

    public static void main(String[] args) {
        log(wordPattern2("abba", "dog cat cat dog"));   // true
        log(wordPattern2("abba", "dog cat cat fish"));  // false
        log(wordPattern2("aaaa", "dog cat cat dog"));   // false
        log(wordPattern2("aaaa", "dog dog dog dog"));   // true
        log(wordPattern2("abba", "dog dog dog dog"));   // false
        log(wordPattern2("jquery", "jquery"));          // false
    }
}
