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
    * 超时解（但结果正确）：BFS
    * - 思路：该题是个典型的可以用图论建模，求最短路径的题目：
    *   1. 求图上两点的最短路径应采用广度优先遍历（BFS）。
    *   2. BFS 需要队列作为辅助数据结构。
    *   3. 因为题中要求返回最短路径的步数，因此队列中除了存储路径上的每一个顶点之外，还要存储从起点开始到该顶点的步数信息。
    * */
    public static int ladderLength(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;

        Queue<Pair<String, Integer>> q = new LinkedList<>();
        q.offer(new Pair<>(beginWord, 1));

        while (!q.isEmpty()) {
            Pair<String, Integer> pair = q.poll();
            String word = pair.getKey();
            int step = pair.getValue();

            for (String w : wordList) {
                if (isSimilar(w, word)) {
                    if (w.equals(endWord)) return step + 1;
                    q.offer(new Pair<>(w, step + 1));
                }
            }
        }

        return 0;
    }

    private static boolean isSimilar(String w1, String w2) {
        if (w1.length() != w2.length() || w1.equals(w2)) return false;
        int diffCount = 0;
        for (int i = 0; i < w1.length(); i++) {
            if (w1.charAt(i) != w2.charAt(i)) diffCount++;
            if (diffCount > 1) return false;
        }
        return true;
    }

    /*
    * 解法1：在超时解的基础上对 BFS 进行优化
    * - 思路：在 BFS 过程中记录访问过的顶点，当再次碰到时不再重复访问：
    *   1. "顶点"即该题中 wordList 里的每一个 word，因此要记录 wordList 里的每一个 word 是否已被访问过。
    *   2. 首先会想到可使用 Map<String, Boolean> 的方式记录，但所有 value 为 boolean 的 map 都可以使用 set 化简 —— 一个元素
    *      "在/不在" set 中即可表达是否访问过，因此无需再为其设置 true/false。因此只需创建一个 set 并放入 wordList 中的元素即可。
    *   3. 需要注意的是，当需要一边遍历 set，一边增/删其中元素（动态增删）时，不能使用 for, while 或者 forEach，需要使用 iterator。
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static int ladderLength2(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;

        Set<String> wordSet = new HashSet<>(wordList);  // 可以用构造器直接从 Collection 创建 Set
        Queue<Pair<String, Integer>> q = new LinkedList<>();
        q.offer(new Pair<>(beginWord, 1));

        while (!q.isEmpty()) {
            Pair<String, Integer> pair = q.poll();
            String word = pair.getKey();
            int step = pair.getValue();

            for (Iterator<String> it = wordSet.iterator(); it.hasNext(); ) {  // 遍历所有没访问过的 word（不再遍历 wordList）
                String w = it.next();
                if (isSimilar(w, word)) {
                    if (w.equals(endWord)) return step + 1;
                    q.offer(new Pair<>(w, step + 1));
                    it.remove();  // 动态删减 wordSet 中的元素
                }
            }
        }

        return 0;
    }

    public static void main(String[] args) {
        List<String> wordList = Arrays.asList("hot", "dot", "dog", "lot", "log", "cog");
        log(ladderLength2("hit", "cog", wordList));
        // expects 5. (As one shortest transformation is "hit" -> "hot" -> "dot" -> "dog" -> "cog")

        List<String> wordList2 = Arrays.asList("hot", "dot", "dog", "lot", "log");
        log(ladderLength2("hit", "cog", wordList2));
        // expects 0. (The endWord "cog" is not in wordList, therefore no possible transformation)

        List<String> wordList3 = Arrays.asList("hot", "dog");
        log(ladderLength2("hot", "dog", wordList3));
        // expects 0. (No solution)
    }
}
