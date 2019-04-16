package Array;

import static Utils.Helpers.log;

/*
* Reverse Vowels of a String
*
* - 指针对撞思路
* */

public class L345_ReverseVowelsOfString {
    public static String reverseVowels(String s) {
        char[] arr = s.toCharArray();
        int i = 0, j = arr.length - 1;
        for (; i < j; i++, j--) {
            while (i < j && !isVowel(arr[i])) i++;
            while (j > i && !isVowel(arr[j])) j--;
            if (i != j) swap(arr, i, j);
        }
        return String.valueOf(arr);  // 将一个 char array join 起来的方式
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

    public static void main(String[] args) {
        log(reverseVowels("hello"));     // expects "holle"
        log(reverseVowels("leetcode"));  // expects "leotcede"
        log(reverseVowels("aA"));        // expects "Aa"
    }
}
