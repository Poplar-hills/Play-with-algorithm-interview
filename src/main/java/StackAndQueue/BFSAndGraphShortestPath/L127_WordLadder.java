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
*   Note that:
*   1. All words have the same length.
*   2. All words contain only lowercase alphabetic characters.
*   3. You may assume no duplicates in the word list.
*   4. You may assume beginWord and endWord are non-empty and are not the same.
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
    * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
    * */
    public static int ladderLength2(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;

        Set<String> wordSet = new HashSet<>(wordList);  // 可以用构造器直接从 Collection 创建 Set
        Queue<Pair<String, Integer>> q = new LinkedList<>();
        q.offer(new Pair<>(beginWord, 1));

        while (!q.isEmpty()) {  // 最差情况下遍历了所有顶点才到达 endWord，因此时间复杂度 O(n)
            Pair<String, Integer> pair = q.poll();
            String word = pair.getKey();
            int step = pair.getValue();

            for (Iterator<String> it = wordSet.iterator(); it.hasNext(); ) {  // 遍历 wordSet 而非 wordList，时间复杂度 O(n)
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

    /*
    * 解法2：Bi-directional BFS
    * -
    * */
    public static int ladderLength3(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;

        Queue<Pair<String, Integer>> q1 = new LinkedList<>(), q2 = new LinkedList<>();
        q1.offer(new Pair<>(beginWord, 1));
        q2.offer(new Pair<>(endWord, 1));

        Set<String> wordSet = new HashSet<>(wordList);
        wordSet.remove(endWord);

        while (!q1.isEmpty() && !q2.isEmpty()) {
            Pair<String, Integer> p1 = q1.poll(), p2 = q2.poll();
            String word1 = p1.getKey(), word2 = p2.getKey();
            int step1 = p1.getValue(), step2 = p2.getValue();

            if (isSimilar(word1, word2)) return step1 + step2;
            List<String> s1 = findSimilar(word1, wordSet);
            if (s1.contains(endWord)) return step1 + 1;
            List<String> s2 = findSimilar(word2, wordSet);
            Set<String> intersection = new HashSet<>(s1);
            intersection.retainAll(s2);

            if (intersection.size() > 0)
                return step1 + step2 + 1;
            for (String s : s1) {
                q1.add(new Pair<>(s, step1 + 1));
                wordSet.remove(s);
            }
            for (String s : s2) {
                q2.add(new Pair<>(s, step2 + 1));
                wordSet.remove(s);
            }
        }

        return 0;
    }

    private static List<String> findSimilar(String word, Set<String> wordSet) {
        List<String> res = new ArrayList<>();
        for (String w : wordSet)
            if (isSimilar(w, word))
                res.add(w);
        return res;
    }

    public static void main(String[] args) {
        List<String> wordList = Arrays.asList("hot", "dot", "dog", "lot", "log", "cog");
        log(ladderLength3("hit", "cog", wordList));
        // expects 5. (One shortest transformation is "hit" -> "hot" -> "dot" -> "dog" -> "cog")

        List<String> wordList2 = Arrays.asList("a", "b", "c");
        log(ladderLength3("a", "c", wordList2));
        // expects 2. ("a" -> "c")

        List<String> wordList3 = Arrays.asList("ted", "tex", "red", "tax", "tad", "den", "rex", "pee");
        log(ladderLength3("red", "tax", wordList3));
        // expects 4. (One shortest transformation is "red" -> "ted" -> "tad" -> "tax")

        List<String> wordList4 = Arrays.asList("hot", "dot", "dog", "lot", "log");
        log(ladderLength3("hit", "cog", wordList4));
        // expects 0. (The endWord "cog" is not in wordList, therefore no possible transformation)

        List<String> wordList5 = Arrays.asList("hot", "dog");
        log(ladderLength3("hot", "dog", wordList5));
        // expects 0. (No solution)

        List<String> wordList6 = Arrays.asList("lest", "leet", "lose", "code", "lode", "robe", "lost");
        log(ladderLength3("leet", "code", wordList6));
        // expects 6. ("leet" -> "lest" -> "lost" -> "lose" -> "lode" -> "code")

        List<String> wordList7 = Arrays.asList("miss", "dusk", "kiss", "musk", "tusk", "diss", "disk", "sang", "ties", "muss");
        log(ladderLength3("kiss", "tusk", wordList7));
        // expects 5. ("kiss" -> "miss" -> "muss" -> "musk" -> "tusk")
    }
}
