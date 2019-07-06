package Array.TwoPointerCollision;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static Utils.Helpers.log;

/*
* Reverse Vowels of a String
*
* - Write a function that takes a string as input and reverse only the vowels of a string.
* */

public class L345_ReverseVowelsOfString {
    /*
    * 解法1：指针对撞
    * - 时间复杂度 O(n)，空间复杂度 O(1)。
    * */
    public static String reverseVowels(String s) {
        char[] arr = s.toCharArray();
        for (int i = 0, j = arr.length - 1; i < j; i++, j--) {
            while (i < j && !isVowel(arr[i])) i++;
            while (j > i && !isVowel(arr[j])) j--;
            swap(arr, i, j);
        }
        return String.valueOf(arr);  // Java 中将 char array 转成 String 的方式
    }

    private static boolean isVowel(char c) {
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' ||
                c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U';
    }

    private static void swap(char[] arr, int i, int j) {
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /*
    * 解法2：解法1的优化版
    * */
    public static String reverseVowels2(String s) {
        Set<Character> vowels = new HashSet<>(Arrays.asList('a', 'e', 'i', 'o', 'u'));  // 使用 Set 代替解法1中的 isVowel 方法
        char[] arr = s.toCharArray();
        for (int i = 0, j = s.length() - 1; i < j; i++, j--) {
            while (i < j && !vowels.contains(Character.toLowerCase(arr[i]))) i++;  // 在检查是否是元音之前先 toLowerCase
            while (j > i && !vowels.contains(Character.toLowerCase(arr[j]))) j--;
            swap(arr, i, j);
        }
        return String.valueOf(arr);
    }

    public static void main(String[] args) {
        log(reverseVowels2("hello"));     // expects "holle"
        log(reverseVowels2("leetcode"));  // expects "leotcede"
        log(reverseVowels2("aA"));        // expects "Aa"
        log(reverseVowels2("azozzA"));    // expects "Azozza"
        log(reverseVowels2("ccccc"));     // expects "ccccc"
    }
}
