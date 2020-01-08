package Array.S4_TwoPointerCollision;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static Utils.Helpers.log;
import static Utils.Helpers.swap;

/*
 * Reverse Vowels of a String
 *
 * - Write a function that takes a string as input and reverse only the vowels of the string.
 *
 * - 👉Java 语法：
 *   1. 将 char[] 连接成一个 String 的方式：new String(chars) 或 String.valueOf(chars);
 *   2. 将 String 打散成 char[] 的方式：str.toCharArray();
 *   3. reverse 一个 String 的最快方式：new StringBuilder(str).reverse().toString()。
 * */

public class L345_ReverseVowelsOfString {
    /*
     * 解法1：指针对撞
     * - 思路：将字符串中的元音字母反向其实就是在 L344_ReverseString 的基础上加入对元音字母的判断即可 ∴ 总体逻辑还是指针对撞，
     *   代码外层循环让 l、r 同步移动，但 l、r 移动之后不一定指向的就是元音字母 ∴ 内层需要2个 while 循环让 l、r 移动到下一个
     *   元音字母的位置上。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static String reverseVowels(String s) {
        char[] chars = s.toCharArray();
        for (int l = 0, r = chars.length - 1; l < r; l++, r--) {
            while (l < r && !isVowel(chars[l])) l++;
            while (l < r && !isVowel(chars[r])) r--;
            if (l != r) swap(chars, l, r);  // 上面的 while 保证了 l <= r ∴ 这里只需排除 l == r 的情况即可
        }
        return String.valueOf(chars);  // 或者 new String(chars)
    }

    private static boolean isVowel(char c) {
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u'
            || c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U';
    }

    /*
     * 解法2：解法1的优化版
     * */
    public static String reverseVowels2(String s) {
        Set<Character> vowels = new HashSet<>(Arrays.asList('a', 'e', 'i', 'o', 'u'));  // 使用 Set 代替解法1中的 isVowel 方法
        char[] chars = s.toCharArray();
        for (int i = 0, j = s.length() - 1; i < j; i++, j--) {
            while (i < j && !vowels.contains(Character.toLowerCase(chars[i]))) i++;  // 在检查是否是元音之前先 toLowerCase
            while (i < j && !vowels.contains(Character.toLowerCase(chars[j]))) j--;
            if (i != j) swap(chars, i, j);
        }
        return String.valueOf(chars);
    }

    public static void main(String[] args) {
        log(reverseVowels("hello"));     // expects "holle"
        log(reverseVowels("leetcode"));  // expects "leotcede"
        log(reverseVowels("aA"));        // expects "Aa"
        log(reverseVowels("azozzA"));    // expects "Azozza"
        log(reverseVowels("ccccc"));     // expects "ccccc"
    }
}
