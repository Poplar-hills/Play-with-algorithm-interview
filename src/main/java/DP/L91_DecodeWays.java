package DP;

import static Utils.Helpers.log;

import java.util.HashMap;
import java.util.Map;

/*
* Decode Ways
*
* - A message containing letters from A-Z is being encoded to numbers using the following mapping: 'A' -> 1,
*   'B' -> 2, ..., 'Z' -> 26. Given a non-empty string containing only digits, determine the total number
*   of ways to decode it.
* */
public class L91_DecodeWays {
    /*
    * 解法1：
    * - 思路：
    * - 时间复杂度 O()，空间复杂度 O()。
    * */
    public static int numDecodings(String s) {
        if(s == null || s.length() == 0) return 0;
        HashMap<Integer, Integer> map = new HashMap<>();
        return dfs(s, map, 0);
    }

    public static int dfs(String str, HashMap<Integer, Integer> map, int index){
        if (index == str.length()) return 1;
        if (map.containsKey(index)) return map.get(index);
        if (str.charAt(index) == '0') {
            map.put(index, 0);
            return 0;
        }
        int res = dfs(str, map, index + 1);
        if(index + 1 < str.length() && (str.charAt(index) == '1' || (str.charAt(index) == '2' && str.charAt(index + 1) < '7'))){
            res += dfs(str, map ,index + 2);
        }
        map.put(index, res);
        return res;
    }

    public static void main(String[] args) {
        log(numDecodings("12"));   // expects 2. It could be decoded as "AB" (1 2) or "L" (12)
        log(numDecodings("226"));  // expects 3. It could be decoded as "BZ" (2 26), "VF" (22 6), or "BBF" (2 2 6)
    }
}
