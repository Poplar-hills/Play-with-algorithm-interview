package HashTable.S1_SetAndMap;

import static Utils.Helpers.log;

import java.util.HashMap;
import java.util.Map;

/*
 * Isomorphic Strings
 *
 * - 判断两个字符串是否同构（isomorphic），即是否可以通过替换 s 中的字符来得到 t。
 * */

public class L205_IsomorphicStrings {
    /*
     * 解法1：双查找表
     * - 思路：根据题意中的“同构”和对 test case 4 的纸上演算可知，若只用一个 Map 记录 s -> t 的字符映射是不够的，同时还需要
     *   记录 t -> s 的字符映射，保证双向都能匹配上才行（例如 test case 4）∴ 使用双查找表。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean isIsomorphic(String s, String t) {
        return helper(s, t, new HashMap<>(), new HashMap<>());
    }

    private static boolean helper(String s, String t, Map<Character, Character> sMap, Map<Character, Character> tMap) {
        if (s.length() == 0 && t.length() == 0) return true;

        char sc = s.charAt(0), tc = t.charAt(0);
        if (!sMap.containsKey(sc) && !tMap.containsKey(tc)) {
            sMap.put(sc, tc);
            tMap.put(tc, sc);
        } else {
            boolean s2tMatch = sMap.containsKey(sc) && sMap.get(sc) == tc;
            boolean t2sMatch = tMap.containsKey(tc) && tMap.get(tc) == sc;
           if (!s2tMatch || !t2sMatch) return false;   // 若只有单向匹配上了则不是同构
        }

        return helper(s.substring(1), t.substring(1), sMap, tMap);  // 这里采用截取字符串的方式，也可以传递索引 i
    }

    /*
     * 解法2：双查找表（解法1的 char[256] 版）
     * - 思路：与解法1一致。
     * - 时间复杂度 O(n)，空间复杂度 O(len(charset))。
     * */
    public static boolean isIsomorphic2(String s, String t) {
        if (s.length() != t.length()) return false;

        char[] sMap = new char[256];
        char[] tMap = new char[256];

        for (int i = 0; i < s.length(); i++) {
            char sc = s.charAt(i), tc = t.charAt(i);
            if (sMap[sc] == 0 && tMap[tc] == 0) {  // char[] 的默认值是'\u0000'，即十进制的0 ∴ 可以直接用 == 0 判断
                sMap[sc] = tc;
                tMap[tc] = sc;
            }
            else if (sMap[sc] != tc || tMap[tc] != sc)  // 若已记录过，但记录中的频次不一样，则说明不是 isomorphic
                return false;
        }

        return true;
    }

    /*
     * 解法3：双查找表（映射到统一的编码上）
     * - 思路：不同于解法1、2中将 s、t 中的字符互相映射，该解法将 s、t 中的字符映射到索引 i 上（统一编码），这样每次只需检查
     *   s、t 对应位置上的字符是被否映射到了相同的数字上即可：
     *       例如对于 s="egg", t="add"：       而对于 s="aba", t="baa"：
     *          e -> 0 <- a                      a ->    0   <- b    - sMap[a]=1，tMap[b]=1
     *          g -> 1 <- d                      b ->    1   <- a    - sMap[b]=2，tMap[a]=2
     *          g -> 2 <- d                      a -> 0 != 1 <- a    - sMap[a] != tMap[a] ∴ return false
     * - 时间复杂度 O(n)，空间复杂度 O(len(charset))。
     * */
    public static boolean isIsomorphic3(String s, String t) {
        if (s.length() != t.length()) return false;
        Map<Character, Integer> sMap = new HashMap<>();
        Map<Character, Integer> tMap = new HashMap<>();

        for (int i = 0; i < s.length(); i++) {
            char sc = s.charAt(i), tc = t.charAt(i);
            if (!sMap.containsKey(sc) && !tMap.containsKey(tc)) {
                sMap.put(sc, i);
                tMap.put(tc, i);
            } else {
                if (sMap.get(sc) != tMap.get(tc)) return false;
            }
        }

        return true;
    }

    /*
     * 解法4：双查找表（映射到统一的编码上）（解法3的 char[256] 版）
     * - 实现：注意要映射到索引 i+1 上，而不能直接映射到 i 上 ∵ 不能映射成0，否则会跟 int[] 的默认值一样。
     * - 时间复杂度 O(n)，空间复杂度 O(len(charset))。
     * */
    public static boolean isIsomorphic4(String s, String t) {
        if (s.length() != t.length()) return false;

        int[] sMap = new int[256];
        int[] tMap = new int[256];

        for (int i = 0; i < s.length(); i++) {
            char sc = s.charAt(i), tc = t.charAt(i);
            if (sMap[sc] != tMap[tc]) return false;
            sMap[sc] = i + 1;  // （这里包含一个隐式转换：sc 是 char，sMap[sc] 是在去 sc 的 ASCII 值）
            tMap[tc] = i + 1;
        }

        return true;
    }

    /*
     * 解法5：解法4的单查找表版
     * - 思路：与解法3、4一致。
     * - 实现：一个查找表分成上下两部分使用，两部分分别记录 s[i] -> i+1 和 t[i] -> i+1 的映射。
     * - 时间复杂度 O(n)，空间复杂度 O(len(charset))：空间复杂度与解法3一样。
     * */
    public static boolean isIsomorphic5(String s, String t) {
        if (s.length() != t.length()) return false;

        int[] map = new int[512];    // ∵ 要分成两部分使用 ∴ 无法用 Map 实现

        for (int i = 0; i < s.length(); i++) {
            char sc = s.charAt(i), tc = t.charAt(i);
            if (map[sc] != map[tc + 256]) return false;
            map[sc] = i + 1;        // 记录索引（+1 是因为要避免0，因为 int[] 的默认值是0）
            map[tc + 256] = i + 1;
        }

        return true;
    }

    /*
     * 解法6：双查找表（匹配上次出现位置）
     * - 思路：不对 s、t 中的字符进行互相映射，而是比较 s、t 中每个字符上次出现的位置是否相等。该思路比解法1-5都更简单，实现也更简洁。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean isIsomorphic6(String s, String t) {
        if (s.length() != t.length()) return false;
        Map<Character, Integer> sMap = new HashMap<>();
        Map<Character, Integer> tMap = new HashMap<>();

        for (int i = 0; i < s.length(); i++) {
            Integer lastSIdx = sMap.put(s.charAt(i), i);  // 注意这里需使用 boxing type 接返回值 ∵ put 可能返回 null
            Integer lastTIdx = tMap.put(t.charAt(i), i);
            if (lastSIdx != lastTIdx) return false;       // 若两边都为 null 或相等则说明匹配上了，否则匹配失败
        }

        return true;
    }

    public static void main(String[] args) {
        log(isIsomorphic5("egg", "add"));      // expects true
        log(isIsomorphic5("paper", "title"));  // expects true
        log(isIsomorphic5("foo", "bar"));      // expects false
        log(isIsomorphic5("ab", "aa"));        // expects false
        log(isIsomorphic5("aba", "baa"));      // expects false
    }
}