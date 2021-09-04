package HashTable.S3_ChooseKeyAndValue;

import java.util.*;
import java.util.stream.Collectors;

import static Utils.Helpers.log;

/*
 * L49 Group Anagrams
 *
 * - Given an array of strings, group anagrams together (the output order doesn't matter).
 * */

public class L49_GroupAnagrams {
    /*
     * 解法1：查找表（模式匹配）
     * - 思路：anagram 的特点是字符个数相同但顺序不同 ∴ group anagrams 的本质是对 strs 中的字符串按模式进行分组 ∴ 需要两步：
     *     1. 生成模式：根据每个字符串的字符频次信息生成模式；
     *     2. 模式匹配：将模式相同的字符串分到一组中；
     *   其中：
     *     1. 模式生成规则为："aab" -> "#2#1#0#0#0...#0"（2个a、1个b，其他字符都是0个，用"#"分隔是为了不混淆前后字母的频率）；
     *     2. 模式匹配时使用查找表进行匹配，查找表结构为：Map<"#2#1#0#0#0...#0", ["aab","baa","aba"]>。
     * - 实现：如果直接按👆思路1、2的顺序编程会需要 2-pass 或更高的时间复杂度。实际上这两个步骤可以在遍历 strs 时对每个字符串
     *   执行一遍，从而只需 1-pass 即可完成。
     * - 时间复杂度 O(nk)，n 为 strs 元素个数，k 为 strs 元素的最大字符个数。
     * - 空间复杂度 O(nk)，即查找表的大小（字符集是常数级的因此没考虑在内g）。
     * */
    public static List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();  // 定义查找表结构
        int[] freq = new int[26];

        for (String s : strs) {
            Arrays.fill(freq, 0);        // freq 是复用的，每次用之前先清空
            for (char c : s.toCharArray())  // 找到每个字符对应的数字，复杂度为 O(k)
                freq[c - 'a']++;

            StringBuilder b = new StringBuilder();
            for (int n : freq) {            // 为字符串生成模式，复杂度为 O(k)
                b.append('#');
                b.append(n);
            }
            String pattern = b.toString();

            if (!map.containsKey(pattern))   // 构建查找表
                map.put(pattern, new ArrayList<>());
            map.get(pattern).add(s);
        }

        return new ArrayList<>(map.values());  // 最后从 map.values() 直接构建结果列表
    }

    /*
     * 解法2：查找表（查找排序后相同的 str）
     * - 思路：不同于解法1为每个字符串生成模式，然后再比较模式之间是否一致，该解法直接将字符顺序统一，并将统一后的字符串作为模式
     *   进行匹配查找，从而更容易的确定哪些字符串应被分为一组。
     * - 实现：查找表的结构与解法1一致，只是 key 不同：Map<"aer", ["are","ear,"era"]>。
     * - 👉 总结：该思路本质上与解法1一致，都是先生成模式，再根据模式进行匹配查找。但该解法生成模式的方式是对字符串进行排序，用
     *   排序后的字符串作为模式。
     * - 时间复杂度 O(nklogk)，n 为 strs 元素个数，k 为 strs 元素的最大字符个数。
     * - 空间复杂度 O(nk)，即查找表的大小。
     * */
    public static List<List<String>> groupAnagrams2(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();

        for (String s : strs) {
            char[] chars = s.toCharArray();
            Arrays.sort(chars);  // O(klogk)
            String sortedStr = String.valueOf(chars);
            if (!map.containsKey(sortedStr))
                map.put(sortedStr, new ArrayList<>());
            map.get(sortedStr).add(s);
        }

        return new ArrayList<>(map.values());  // 直接从 map.values() 构造 List
    }

    public static void main(String[] args) {
        log(groupAnagrams(new String[]{"eat", "tea", "tan", "ate", "nat", "bat"}));
        // expects [["ate","eat","tea"], ["nat","tan"], ["bat"]]

        log(groupAnagrams(new String[]{"sstt", "xyz", "tsst", "xYz"}));
        // expects [["sstt","tsst"], ["xyz"], ["xYz"]]

        log(groupAnagrams(new String[]{}));
        // expects []
    }
}
