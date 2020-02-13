package HashTable.S1_SetAndMap;

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
     * 解法1：双查找表
     * - 思路：类似 L205_IsomorphicStrings，该问题同样是一个模式匹配问题 ∴ 需要双向匹配，只用1个查找表是不够的 ∴ 可以采用
     *   L205 解法1的思路，使用双查找表分别记录 pattern -> str 的映射、str -> pattern 的映射。
     * - 时间复杂度 O(n)，空间复杂度 O(n)
     * */
    public static boolean wordPattern(String pattern, String str) {
        String[] words = str.split(" ");
        if (pattern.length() != words.length) return false;  // 注意处理 pattern 与 str 相同的情况（test case 6）

        Map<Character, String> pMap = new HashMap<>();
        Map<String, Character> wMap = new HashMap<>();

        for (int i = 0; i < words.length; i++) {
            char p = pattern.charAt(i);
            String w = words[i];

            if (pMap.containsKey(p) && !pMap.get(p).equals(w))
                return false;
            if (wMap.containsKey(w) && wMap.get(w) != p)
                return false;

            pMap.put(p, w);
            wMap.put(w, p);
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
        log(wordPattern("abba", "dog cat cat dog"));   // true
        log(wordPattern("aaaa", "dog dog dog dog"));   // true
        log(wordPattern("abba", "dog cat cat fish"));  // false
        log(wordPattern("aaaa", "dog cat cat dog"));   // false
        log(wordPattern("abba", "dog dog dog dog"));   // false
        log(wordPattern("jquery", "jquery"));          // false
    }
}
