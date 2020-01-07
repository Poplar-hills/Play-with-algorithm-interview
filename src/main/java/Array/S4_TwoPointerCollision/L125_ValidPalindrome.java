package Array.S4_TwoPointerCollision;

import static Utils.Helpers.log;

/*
 * Valid Palindrome
 *
 * - Given a string, determine if it is a palindrome, considering only alphanumeric characters and ignoring cases.
 *   Also, empty string is a valid palindrome（即只考虑字母和数字，不考虑空格、标点符号、大小写）.
 * */

public class L125_ValidPalindrome {
    /*
     * 解法1：指针对撞
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    private static boolean validPalindrome(String s) {
        s = s.toLowerCase();
        for (int l = 0, r = s.length() - 1; l < r; l++, r--) {  // 不同于 L11，该指针对撞需同时移动 l, r
            while (l < r && !Character.isLetterOrDigit(s.charAt(l))) l++;  // 跳过无效字符
            while (r > l && !Character.isLetterOrDigit(s.charAt(r))) r--;
            if (s.charAt(l) != s.charAt(r)) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        log(validPalindrome("A man, a plan, a canal: Panama"));  // expects true
        log(validPalindrome("race a car"));  // expects false
    }
}
