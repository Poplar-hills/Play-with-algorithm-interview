package HashTable.S1_SetAndMap;

import java.util.HashMap;
import java.util.Map;

import static Utils.Helpers.log;

/*
 * Word Pattern
 *
 * - 判断一个字符串是否符合指定 pattern
 *
 * - 💎经验：对比 L205_IsomorphicStrings 可总结出这类题使用查找表的几个经典解法：
 *   1. 用双查找表进行双向匹配
 *   2. 用双查找表映射到统一编码
 *   3. 用双单/查找表匹配上次字符出现位置
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
     * 解法2：双查找表（映射到统一的编码上）
     * - 思路：类似 L205_IsomorphicStrings 解法3，将 pattern、str 中的字符/单词映射到索引+1上（统一编码），这样一来每次
     *   只需检查 pattern、str 对应位置上的字符/单词是被否映射到了相同的数字上即可：
     *       a ->    1    <- dog
     *       b ->    2    <- cat
     *       b ->    3    <- cat
     *       a -> 1 != null <- fish  ∴ return false
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean wordPattern2(String pattern, String str) {
        String[] words = str.split(" ");
        if (words.length != pattern.length()) return false;

        Map<Character, Integer> pMap = new HashMap<>();
        Map<String, Integer> wMap = new HashMap<>();

        for (int i = 0; i < words.length; i++) {
            char p = pattern.charAt(i);
            String w = words[i];
            boolean hasP = pMap.containsKey(p);
            boolean hasW = wMap.containsKey(w);

            if ((hasP && !hasW) || (!hasP && hasW))
                return false;
            if (hasP && hasW && !pMap.get(p).equals(wMap.get(w)))  // 注意 ∵ 两个 Map 的 value 是 Integer ∴ 不能用 == 比较
                return false;                                      // （除了 [-128, 127] 之间的数字），而只能用 Integer.equals()

            pMap.put(p, i + 1);
            wMap.put(w, i + 1);
        }

        return true;
    }

    /*
     * 解法3：双查找表（匹配上次出现位置）
     * - 思路：类似 L205_IsomorphicStrings 解法5，比较 pattern、str 中的字符/单词上次出现的位置是否相等。该思路比解法1、2
     *   更简单，实现也更简洁。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean wordPattern3(String pattern, String str) {
        String[] words = str.split(" ");
        if (words.length != pattern.length()) return false;

        Map<Character, Integer> pMap = new HashMap<>();
        Map<String, Integer> wMap = new HashMap<>();

        for (Integer i = 0; i < words.length; i++) {  // 注意 i 必须使用 boxing type，否则存到2个 Map 里的 i 在经过 Integer 包装后不能用 == 比较
            Integer lastPIdx = pMap.put(pattern.charAt(i), i);  // 这里也必须使用 boxing type ∵ put 可能返回 null
            Integer lastWIdx = wMap.put(words[i], i);
            if (lastPIdx != lastWIdx) return false;   // 若两边都为 null 或相等则说明匹配上了，否则匹配失败
        }

        return true;
    }

    /*
     * 解法4：解法3的单查找表版
     * - 思路：与解法3一致。
     * - 实现：使用单个 Map，其 key 即有 Character 也有 String。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean wordPattern4(String pattern, String str) {
        String[] words = str.split(" ");
        if (words.length != pattern.length()) return false;

        Map map = new HashMap();
        for (Integer i = 0; i < words.length; i++)  // 同样 i 得用 boxing type
            if (map.put(pattern.charAt(i), i) != map.put(words[i], i))
                return false;

        return true;
    }

    public static void main(String[] args) {
        log(wordPattern3("abba", "dog cat cat dog"));   // true
        log(wordPattern3("abba", "dog cat cat fish"));  // false
        log(wordPattern3("abba", "dog dog dog dog"));   // false
        log(wordPattern3("xxx", "dog dog dog"));        // true
        log(wordPattern3("xxx", "dog cat dog"));        // false
        log(wordPattern3("jquery", "jquery"));          // false
        log(wordPattern3(  // true. (SEE: https://stackoverflow.com/questions/1514910/how-to-properly-compare-two-integers-in-java)
            "ccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccdd",
            "s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s s t t"));
    }
}
