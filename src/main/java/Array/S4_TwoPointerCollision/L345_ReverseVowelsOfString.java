package Array.S4_TwoPointerCollision;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static Utils.Helpers.log;
import static Utils.Helpers.swap;

/*
 * Reverse Vowels of a String
 *
 * - Write a function that takes a string as input and reverse only the vowels of the string.
 *
 * - 👉 Java 语法：
 *   1. 将 char[] 连接成一个 String 的方式：new String(chars) 或 String.valueOf(chars);
 *   2. 将 String 打散成 char[] 的方式：str.toCharArray();
 *   3. reverse 一个 String 的最快方式：new StringBuilder(str).reverse().toString()。
 * */

public class L345_ReverseVowelsOfString {
    /*
     * 解法1：指针对撞
     * - 思路：代码外层循环同时移动 l、r 两指针，但 l、r 移动之后不一定指向的就是元音字母 ∴ 内层需要2个 while 循环让
     *   l、r 移动到下一个元音字母的位置上。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static String reverseVowels(String s) {
        char[] chars = s.toCharArray();
        for (int l = 0, r = chars.length - 1; l < r; l++, r--) {
            while (l < r && !isVowel(chars[l])) l++;  // 👉🏻内层 while 要注意越界条件不能少！
            while (r > l && !isVowel(chars[r])) r--;
            if (l != r) swap(chars, l, r);  // 上面的 while 保证了 l <= r ∴ 这里只需排除 l == r 的情况即可（没有也可以）
        }
        return String.valueOf(chars);  // 或者 new String(chars)
    }

    private static boolean isVowel(char c) {
        char lc = Character.toLowerCase(c);
        return lc == 'a' || lc == 'e' || lc == 'i' || lc == 'o' || lc == 'u';
    }

    /*
     * 解法2：解法1的优化版
     * - 实现：在解法1基础上加入2点优化：
     *   1. 使用 Set 代替解法1中的 isVowel 方法；
     *   2. 在检查是否是元音之前先进行 toLowerCase 处理。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static String reverseVowels2(String s) {
        Set<Character> vowels = new HashSet<>(List.of('a', 'e', 'i', 'o', 'u'));  // 从 List 生成 Set
        char[] chars = s.toCharArray();
        for (int l = 0, r = s.length() - 1; l < r; l++, r--) {
            while (l < r && !vowels.contains(Character.toLowerCase(chars[l]))) l++;
            while (l < r && !vowels.contains(Character.toLowerCase(chars[r]))) r--;
            if (l != r) swap(chars, l, r);
        }
        return new String(chars);
    }

    public static void main(String[] args) {
        log(reverseVowels2("hello"));     // expects "holle"
        log(reverseVowels2("leetcode"));  // expects "leotcede"
        log(reverseVowels2("aA"));        // expects "Aa"
        log(reverseVowels2("azozzA"));    // expects "Azozza"
        log(reverseVowels2("ccccc"));     // expects "ccccc"
    }
}
