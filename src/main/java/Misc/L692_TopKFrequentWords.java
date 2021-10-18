package Misc;

import java.util.List;

import static Utils.Helpers.log;

/*
 * Top K Frequent Words
 *
 * - Given an array of strings words and an integer k, return the k most frequent strings. Return the answer
 *   sorted by the frequency from highest to lowest. Sort the words with the same frequency by their
 *   lexicographical order.
 *
 * - Follow-up: Could you solve it in O(n log(k)) time and O(n) extra space?
 * */

public class L692_TopKFrequentWords {
    /*
     * 解法1：
     * */
    public static List<String> topKFrequent(String[] words, int k) {
        return null;
    }

    public static void main(String[] args) {
        String[] words1 = new String[]{"i", "love", "leetcode", "i", "love", "coding"};
        log(topKFrequent(words1, 2));  // expects ["i", "love"]

        String[] words2 = new String[]{"the", "day", "is", "sunny", "the", "the", "the", "sunny", "is", "is"};
        log(topKFrequent(words2, 4));  // expects ["the", "is", "sunny", "day"]
    }
}
