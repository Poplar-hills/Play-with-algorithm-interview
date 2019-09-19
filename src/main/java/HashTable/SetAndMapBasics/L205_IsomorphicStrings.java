package HashTable.SetAndMapBasics;

import static Utils.Helpers.log;

/*
* Isomorphic Strings
*
* - 判断两个字符串是否同构（isomorphic），即是否可以通过替换 s 中的字符来得到 t。
* */

public class L205_IsomorphicStrings {
    /*
    * 解法1：双查找表
    * - 思路：判断两个字符串是否是 isomorphic，只需判断两个字符串中的相同索引上的字符是否具有相同的频率。
    * - 实现：两个查找表分别记录两个字符串中所有字符的频率，并在之后遍历过程中检查相同索引上的两个字符的频率是否与之前记录的一致。
    * - 时间复杂度 O(n)，空间复杂度 O(2len(charset))。
    * */
    public static boolean isIsomorphic(String s, String t) {
        if (s.length() != t.length())
            return false;

        char[] sMap = new char[256];                  // 也可以使用 Map 实现
        char[] tMap = new char[256];

        for (int i = 0; i < s.length(); i++) {        // 只需遍历一遍即可
            char sc = s.charAt(i), tc = t.charAt(i);  // 取两个字符串相同索引上的字符
            if (sMap[sc] == 0 && tMap[tc] == 0) {     // 若两字符的频率还未被记录过则记录下来（char[] 的默认值是'\u0000'，即十进制的0）
                sMap[sc] = tc;
                tMap[tc] = sc;
            }
            else if (sMap[sc] != tc || tMap[tc] != sc)  // 若已记录过，但记录中的频率不一样，则说明不是 isomorphic
                return false;
        }

        return true;
    }

    /*
     * 解法2：单查找表
     * - 思路：对于 s 和 t 中的每个字符 s[i]、t[i] 来说，若他们上次在字符串中出现的索引相同，即说明 s 和 t 是 isomorphic 的。
     * - 实现：一个查找表分成两部分使用，两部分分别记录 s[i] -> i 和 t[i] -> i 的映射。
     * - 时间复杂度 O(n)，空间复杂度 O(2len(charset))。
     * */
    public static boolean isIsomorphic2(String s, String t) {
        int[] map = new int[512];      // ∵ 要分成两部分使用 ∴ 无法用 Map 实现
        for (int i = 0; i < s.length(); i++) {
            if (map[s.charAt(i)] != map[t.charAt(i) + 256])
                return false;
            map[s.charAt(i)] = map[t.charAt(i) + 256] = i + 1;  // 记录索引（+1 是因为要避免0，因为 int[] 的默认值是0）
        }
        return true;
    }

    public static void main(String[] args) {
        log(isIsomorphic("egg", "add"));      // expects true
        log(isIsomorphic("paper", "title"));  // expects true
        log(isIsomorphic("foo", "bar"));      // expects false
        log(isIsomorphic("ab", "aa"));        // expects false
    }
}