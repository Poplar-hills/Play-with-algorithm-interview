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
*
* - 注意：本题求的是最短路径上的顶点数（包含头尾顶点），而非最短路径的上步数（L279 中求的是步数），这个要区分清楚。
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
    * - 策略：采用 Bi-directional BFS 能有效减小搜索复杂度：
    *   - 复杂度：设 branching factor 是 b，两点间距是 d，则单向 BFS/DFS 的时间及空间复杂度为 O(b^d)，而双向 BFS 的的时间及空间
    *     复杂度为 O(b^(d/2) + b^(d/2)) 即 O(b^(d/2))，比起 O(b^d) 要小得多。
    *   - 使用条件：1. 已知头尾两个顶点  2. 两个方向的 branching factor 相同。
    *
    * - 思路：虽然代码不少，但思路并不复杂：
    *   1. startQ 放入起点，endQ 放入终点；
    *   2. 从 startQ 开始，遍历其中每一个顶点，为每一个顶点寻找其所有相邻顶点：
    *      a. 若其中任一相邻顶点出现在 endQ 中（即出现在对面方向最外层顶点中），则说明正反向查找相遇，找到了最短路径，返回路径上的顶点数即可；
    *      b. 若没有相邻顶点出现在 endQ 中，则说明正反向查找还未相遇，此时：
    *        1). 经过的顶点数+1；
    *        2). 从所有相邻顶点中筛出所有之前未访问过的，加入 neighbours 集合（同时也加入 visited 集合）；
    *        2). 调换方向开始下一轮查找（刚才是正向查找一步，下一轮是反向查找一步），将 endQ 作为 startQ 开始遍历，并将 neighbours
    *            作为 endQ 用于查看下一轮中的相邻顶点是否出现在对面方向最外层顶点中。
    *
    * - 优化：在最后要调换方向时，加一步判断 —— Choose the shortest between the startQ and endQ in hopes to alternate
    *   between them to meet somewhere at the middle. This optimizes the code, because we are processing smallest
    *   queue first, so the # of words in the queues dont blow up too fast. basically balancing between the two queues.
    * */
    public static int ladderLength3(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;

        Set<String> wordSet = new HashSet<>(wordList);
        Set<String> visited = new HashSet<>();
        Set<String> startQ = new HashSet<>();  // 辅助正向 BFS 的队列
        Set<String> endQ = new HashSet<>();    // 辅助反向 BFS 的队列
        startQ.add(beginWord);
        endQ.add(endWord);

        int steps = 2;                         // steps 为该题所求的最短路径顶点数，从2开始是已包含头尾的顶点
        while (!startQ.isEmpty()) {
            Set<String> neighbours = new HashSet<>();
            for (String word: startQ) {                    // 遍历 startQ 中的每一个单词
                for (int i = 0; i < word.length(); i++) {  // 寻找每一个单词的相邻单词（neighbouring words）
                    StringBuilder transformWord = new StringBuilder(word);
                    char exclude = transformWord.charAt(i);
                    for (char c = 'a'; c <= 'z'; c++) {    // 替换 word 中的每个字母，查看替换后的单词 tWord 是否是 word 的相邻单词
                        if (c == exclude) continue;
                        transformWord.setCharAt(i, c);     // 上面创建 StringBuilder 是为了这里能按索引修改字符串中的字符
                        String tWord = transformWord.toString();
                        if (endQ.contains(tWord)) return steps;  // 本侧的相邻顶点出现在对面方向的最外层顶点中，说明正反向查找相遇，找到了最短路径
                        if (wordSet.contains(tWord) && visited.add(tWord))  // 如果是有效的、未访问过的顶点（这里用了 add 返回值的技巧）
                            neighbours.add(tWord);
                    }
                }
            }

            steps++;                                // 路径上的顶点数+1

            if (endQ.size() < neighbours.size()) {  // 若 endQ 中的顶点数少，则调换方向，下一轮从反向查找，遍历 endQ 中的顶点
                startQ = endQ;
                endQ = neighbours;                  // 本轮中找到的相邻顶点（本侧最外层顶点）作为下一轮中的 endQ，用于检测是否正反向相遇
            }
            else startQ = neighbours;               // 若 endQ 中顶点多，则下一轮继续正向查找（即将本轮中正向的最外层顶点作为 startQ）
        }

        return 0;
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
