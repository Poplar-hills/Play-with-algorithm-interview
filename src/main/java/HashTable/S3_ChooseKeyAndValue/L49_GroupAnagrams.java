package HashTable.S3_ChooseKeyAndValue;

import java.util.*;

import static Utils.Helpers.log;

/*
 * L49 Group Anagrams
 *
 * - Given an array of strings, group anagrams together (the output order doesn't matter).
 * */

public class L49_GroupAnagrams {
    /*
     * 解法1：查找表（查找排序后相同的 str）
     * - 思路：先找到 anagram 的特点 —— 互为 anagram 的两个字符串中的各个字符的个数相同，只是顺序不同。那么只要我们将字符
     *   顺序统一，就能很容易的使用查找表确定哪些字符串应被分为一组。
     * - 实现：使用 Map 进行查找并分组 —— 将 strs 中的每个字符串 str 排序后作为 Map 的 key（即查找内容），value 则是在
     *   排序后能匹配到 key 上的 str，即 Map 的结构为 {"aer": ["are","ear,"era"]}。
     * - 时间复杂度 O(nklogk)，n 为 strs 元素个数，k 为 strs 元素的最大字符个数。
     * - 空间复杂度 O(nk)，即查找表的大小。
     * */
    public static List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();

        for (String s : strs) {                // O(n)
            char[] chars = s.toCharArray();
            Arrays.sort(chars);                // O(klogk)
            String key = String.valueOf(chars);
            if (!map.containsKey(key))
                map.put(key, new ArrayList<>());
            map.get(key).add(s);
        }

        return new ArrayList<>(map.values());  // 直接从 map.values() 构造 List
    }

    /*
     * 解法2：查找表（查找频率编码相同的 str）
     * - 思路：∵ anagram 的特点是两个字符串中每个字符的出现频率相同 ∴ 要让查找表的 key（即查找内容）能体现这一特点 —— 从每个
     *   字符串 str 里提取每个字符的频次信息，并编码后作为查找表的 key。利用这样构建的查找表就可以对 strs 进行查找并分组。
     *   - 其中，编码规则是："aab" -> "#2#1#0#0#0...#0"（2个a、1个b，其他字符都是0个，用"#"分隔是为了不混淆前后字母的频率）；
     *   - 查找表的结构是：{"#2#1#0#0#0...#0": ["aab", "baa", "aba"]}。
     * - 时间复杂度 O(nk)，n 为 strs 元素个数，k 为 strs 元素的最大字符个数。
     * - 空间复杂度 O(nk)，即查找表的大小（字符集是常数级的因此没考虑在内g）。
     * */
    public static List<List<String>> groupAnagrams2(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();  // 查找表的结构与解法1中一致
        int[] freq = new int[26];

        for (String s : strs) {             // 遍历 strs，为每个字符串进行编码
            Arrays.fill(freq, 0);           // freq 是复用的，每次用之前先清空
            for (char c : s.toCharArray())  // O(k)
                freq[c - 'a']++;            // 此处 assume 字符集是 a-z

            StringBuilder b = new StringBuilder();
            for (int i = 0; i < freq.length; i++) {  // 进行编码。O(k)
                b.append('#');
                b.append(freq[i]);
            }
            String key = b.toString();      //编码 结果

            if (!map.containsKey(key))      // 构建查找表
                map.put(key, new ArrayList<>());
            map.get(key).add(s);
        }

        return new ArrayList<>(map.values());  // 最后也是从 map.values() 直接构建 List
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
