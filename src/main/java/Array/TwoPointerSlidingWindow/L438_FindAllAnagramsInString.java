package Array.TwoPointerSlidingWindow;

import java.util.List;

/*
* Find All Anagrams in a String
*
*
* */

public class L438_FindAllAnagramsInString {
    public static List<Integer> findAnagrams(String s, String p) {

    }

    public static void main(String[] args) {
        findAnagrams("cbaebabacd", "abc");  // expects [0, 6] ("cba", "bac")
        findAnagrams("abab", "ab");         // expects [0, 1, 2] ("ab", "ba", "ab")
    }
}
