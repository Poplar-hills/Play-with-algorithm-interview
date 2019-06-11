package StackAndQueue.BFSAndGraphShortestPath;

import java.util.Arrays;
import java.util.List;

import static Utils.Helpers.log;

/*
* Word Ladder
*
* - Given two words (beginWord and endWord), and a dictionary's word list, find the length of shortest
*   transformation sequence from beginWord to endWord, such that:
*   1. Only one letter can be changed at a time.
*   2. Each transformed word must exist in the word list. Note that beginWord is not a transformed word.
*   3. Return 0 if there is no such transformation sequence.
*   4. All words have the same length.
*   5. All words contain only lowercase alphabetic characters.
*   6. You may assume no duplicates in the word list.
*   7. You may assume beginWord and endWord are non-empty and are not the same.
* */

public class L127_WordLadder {
    public static int ladderLength(String beginWord, String endWord, List<String> wordList) {
        return 0;
    }

    public static void main(String[] args) {
        List<String> wordList = Arrays.asList("hot", "dot", "dog", "lot", "log", "cog");
        log(ladderLength("hit", "cog", wordList));
        // expects 5. (As one shortest transformation is "hit" -> "hot" -> "dot" -> "dog" -> "cog")

        List<String> wordList2 = Arrays.asList("hot", "dot", "dog", "lot", "log");
        log(ladderLength("hit", "cog", wordList2));
        // expects 0. (The endWord "cog" is not in wordList, therefore no possible transformation)
    }
}
