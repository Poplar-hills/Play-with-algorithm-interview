package HashTable.SetAndMapBasics;

import static Utils.Helpers.log;

/*
* Isomorphic Strings
*
* - 判断两个字符串是否同构（isomorphic），即是否可以通过替换 s 中的字符来得到 t。
* */

public class L205_IsomorphicStrings {
    /*
    * 解法1：双 map
    * - 思路：判断两个字符串是否是 isomorphic 的，只需判断两者中各字符的相对位置是否相同，因此需要建立 s 中的字符 -> t 中的字符的映射。
    * - 实现：两个 map 分别记录 s[i] -> t[i] 和 t[i] -> s[i] 的映射关系，之后在遍历字符串过程中检查每个字符的映射关系是否与之前记录的映射关系一致。
    * - 时间复杂度 O(n)，空间复杂度 O(2len(charset))。
    * */
    public static boolean isIsomorphic(String s, String t) {
        if (s.length() != t.length())
            return false;

        char[] sMap = new char[256];  // 也可以使用 Map 实现
        char[] tMap = new char[256];

        for (int i = 0; i < s.length(); i++) {
            char sc = s.charAt(i), tc = t.charAt(i);
            if (sMap[sc] == 0 && tMap[tc] == 0) {  // 若 sc 和 tc 没有被记录过则记录下来（char[] 的默认值是 '\u0000'，其十进制值为0）
                sMap[sc] = tc;
                tMap[tc] = sc;
            }
            else if (sMap[sc] != tc || tMap[tc] != sc)  // 若已有记录，但映射关系与之前记录的不一致，则说明不是 isomorphic 的
                return false;
        }

        return true;
    }

    /*
     * 解法2：单 map
     * - 思路：对于 s 和 t 中的每个字符（如 s[i], t[i]）来说，只需要知道他们上次出现时的索引相同即可判断 s 和 t 是 isomorphic 的。
     * - 实现：一个 map 分成两部分使用，每部分分别记录 s[i] -> i 和 t[i] -> i 的映射。
     * - 时间复杂度 O(n)，空间复杂度 O(2len(charset))。
     * */
    public static boolean isIsomorphic2(String s, String t) {
        int[] map = new int[256];
        for (int i = 0; i < s.length(); i++) {
            if (map[s.charAt(i)] != map[t.charAt(i) + 128])
                return false;
            map[s.charAt(i)] = map[t.charAt(i) + 128] = i + 1;  // i+1 是因为要避免0，因为 int[] 的默认值是0
        }
        return true;
    }

    public static void main(String[] args) {
        log(isIsomorphic("egg", "add"));      // expects true
        log(isIsomorphic("paper", "title"));  // expects true
        log(isIsomorphic("foo", "bar"));      // expects false
        log(isIsomorphic("ab", "aa"));        // expects false

        log(isIsomorphic2("egg", "add"));      // expects true
        log(isIsomorphic2("paper", "title"));  // expects true
        log(isIsomorphic2("foo", "bar"));      // expects false
        log(isIsomorphic2("ab", "aa"));        // expects false
    }
}