package StackAndQueue.BFSAndGraphShortestPath;

import javafx.util.Pair;

import java.util.*;

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
    /*
    * 解法1：BFS
    * */
    public static int ladderLength(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;

        Set<String> wordSet = new HashSet<>(wordList);
        Queue<Pair<String, Integer>> q = new LinkedList<>();
        q.offer(new Pair<>(beginWord, 1));

        while (!q.isEmpty()) {
            Pair<String, Integer> pair = q.poll();
            String word = pair.getKey();
            int step = pair.getValue();

            for (Iterator<String> it = wordSet.iterator(); it.hasNext(); ) {
                String w = it.next();
                if (isSimilarTo(w, word)) {
                    if (w.equals(endWord)) return step + 1;
                    q.offer(new Pair<>(w, step + 1));
                    it.remove();
                }
            }
        }

        return 0;
    }

    private static boolean isSimilarTo(String word1, String word2) {
        if (word1.length() != word2.length() || word1.equals(word2))
            return false;

        int diffCount = 0;
        for (int i = 0; i < word1.length(); i++) {
            if (word1.charAt(i) != word2.charAt(i)) diffCount++;
            if (diffCount > 1) return false;
        }

        return true;
    }

    public static void main(String[] args) {
        List<String> wordList = Arrays.asList("hot", "dot", "dog", "lot", "log", "cog");
        log(ladderLength("hit", "cog", wordList));
        // expects 5. (As one shortest transformation is "hit" -> "hot" -> "dot" -> "dog" -> "cog")

        List<String> wordList2 = Arrays.asList("hot", "dot", "dog", "lot", "log");
        log(ladderLength("hit", "cog", wordList2));
        // expects 0. (The endWord "cog" is not in wordList, therefore no possible transformation)

        List<String> wordList3 = Arrays.asList("hot", "dog");
        log(ladderLength("hot", "dog", wordList3));
        // expects 0. (No solution)
    }
}
