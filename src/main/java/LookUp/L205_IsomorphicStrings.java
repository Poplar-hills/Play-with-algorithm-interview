package LookUp;

import static Utils.Helpers.log;

/*
* Isomorphic Strings
*
* - 判断两个字符串是否同构（isomorphic），即是否可以通过替换 s 中的字符来得到 t。
* */

public class L205_IsomorphicStrings {
    /*
    * 解法1
    * - 使用两个 map，分别记录 s[i] -> t[i] 和 t[i] -> s[i] 的映射关系，之后在遍历字符串过程中检查每个字符的映射关系是否与之前记录的映射关系一致。
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
            else if (sMap[sc] != tc || tMap[tc] != sc)  // 若 sc 和 tc 在两个 map 中已有记录，但映射关系与之前记录的不一致则说明不是 isomorphic
                return false;
        }

        return true;
    }
g

    public static void main(String[] args) {
        log(isIsomorphic("egg", "add"));      // expects true
        log(isIsomorphic("foo", "bar"));      // expects false
        log(isIsomorphic("paper", "title"));  // expects true
    }
}