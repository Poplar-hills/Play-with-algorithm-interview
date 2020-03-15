package StackAndQueue.S4_BFSAndGraphShortestPath;

import java.util.*;

import Utils.Helpers.Pair;

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
     * - 思路：
     *   1. 该题是个典型求最短路径的题，而求图上两点的最短路径可采用 BFS。
     *   2. 题中要求返回最短路径的步数，因此队列中除了存储路径上的每一个顶点之外，还要存储从起点开始到该顶点的步数信息。
     * */
    public static int ladderLength(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;            // 无解的情况
        Queue<Pair<String, Integer>> q = new LinkedList<>();  // BFS 需要使用 queue 作为辅助数据结构
        q.offer(new Pair<>(beginWord, 1));                    // ∵ beginWord 不在 wordList 中 ∴ 要从1开始计数（不同于 L279 解法1）

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

    private static boolean isSimilar(String w1, String w2) {  // 复杂度 O(len(word))
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
     * - 问题：在超时解中，很可能出现这种情况：顶点 A、B 都与 C 相邻，当分别为 A 和 B 寻找相邻节点时 C 都会出现，因此也会被入队2次，
     *   从而造成不必要的重复访问。
     * - 思路：记录哪些顶点还未被访问过，当寻找相邻顶点时只在这些未被访问过的顶点中寻找，找到后只要不是 endWord 就让它们入队待访问，
     *   并将它们从未被访问的记录中删除。
     * - 注意：当需要一边遍历 set，一边增/删其中元素（动态增删）时，不能使用 for, while 或者 forEach，需要使用 iterator。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int ladderLength1(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;
        Set<String> unvisited = new HashSet<>(wordList);  // 通过 Collection 构造 Set
        Queue<Pair<String, Integer>> q = new LinkedList<>();
        q.offer(new Pair<>(beginWord, 1));

        while (!q.isEmpty()) {  // 最差情况下遍历了所有顶点才到达 endWord，因此时间复杂度 O(n)
            Pair<String, Integer> pair = q.poll();
            String word = pair.getKey();
            int step = pair.getValue();

            for (Iterator<String> it = unvisited.iterator(); it.hasNext(); ) {  // 遍历 unvisited 而非 wordList，时间复杂度 O(n)
                String w = it.next();
                if (isSimilar(w, word)) {
                    if (w.equals(endWord)) return step + 1;
                    q.offer(new Pair<>(w, step + 1));  // 将相邻节点入队待访问
                    it.remove();                       // 从 unvisited 中删除（动态删除 unvisited 中的元素）
                }
            }
        }

        return 0;
    }

    /*
     * 解法2：解法2的性能改进版
     * - 思路：改进点在于寻找相邻顶点的过程 —— 解法2中该过程是通过 isSimilar 方法比较 wordSet 中每个单词是否与待比较单词 word
     *   相似来完成的；而本解法中采用另一种策略 —— 尝试对 word 中的每个字母用 a-z 中的字母进行替换，看替换后的单词 tWord 是否
     *   存在于 wordSet 中，若存在则说明确实与 word 相邻。这种方法复杂度更低 ∵ 只有26个字母 ∴ 复杂度为 len(word) * 26；而解法1
     *   中的复杂度为 wordSet.size() * len(word)，因此在 wordSet 中元素数较多时，性能会差于解法2。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    private static int ladderLength2(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;
        Queue<Pair<String, Integer>> q = new LinkedList<>();
        Set<String> unvisitied = new HashSet<>(wordList);
        q.offer(new Pair<>(beginWord, 1));

        while (!q.isEmpty()) {
            Pair<String, Integer> p = q.poll();
            String word = p.getKey();
            int step = p.getValue();

            for (int i = 0; i < word.length(); i++) {  // 替换 word 中的每个字母，查看替换后的单词 tWord 是否与 word 相邻
                StringBuilder transformed = new StringBuilder(word);
                for (char c = 'a'; c <= 'z'; c++) {
                    if (c == word.charAt(i)) continue;
                    transformed.setCharAt(i, c);       // 上面创建 StringBuilder 是为了这里能按索引修改字符串中的字符
                    String tWord = transformed.toString();
                    if (unvisitied.contains(tWord)) {  // 若 tWord 在 unvisitied 中，则说明找到了一个相邻节点
                        if (tWord.equals(endWord)) return step + 1;
                        q.offer(new Pair<>(tWord, step + 1));
                        unvisitied.remove(tWord);
                    }
                }
            }
        }

        return 0;
    }

    /*
     * 解法3：Bi-directional BFS
     * - 策略：采用 Bi-directional BFS 能有效减小搜索复杂度：
     *   - 复杂度：设 branching factor 是 b，两点间距是 d，则单向 BFS/DFS 的时间及空间复杂度为 O(b^d)，而双向 BFS 的时间及空间
     *     复杂度为 O(b^(d/2) + b^(d/2)) 即 O(b^(d/2))，比起 O(b^d) 要小得多。
     *   - 使用条件：1. 已知头尾两个顶点  2. 两个方向的 branching factor 相同。
     *
     * - 思路：虽然代码不少，但思路并不复杂：
     *   1. 使用2个队列 startQ 和 endQ，分别用于辅助正/反向查找；
     *   2. startQ 放入起始顶点 beginWord，endQ 放入终结顶点 endWord；
     *   3. 从 startQ 开始，遍历其中所有顶点，为每一个顶点寻找相邻顶点：
     *      a. 若其中任一相邻顶点出现在 endQ 中（即出现在对面方向最外层顶点中），则说明正反向查找相遇，找到了最短路径，返回顶点数即可；
     *      b. 若没有相邻顶点出现在 endQ 中，则说明正反向查找还未相遇，此时：
     *        1). 经过的顶点数+1；
     *        2). 将所有未访问过的相邻顶点加入 neighbours 集合；
     *        3). 调换方向开始下一轮查找（刚才是正向查找一步，下一轮是反向查找一步），将 endQ 作为 startQ 开始遍历，并将 neighbours
     *            作为 endQ 用于查看下一轮中的相邻顶点是否出现在对面方向最外层顶点中。
     *
     * - 优化：在最后要调换方向时，可以加一步判断 —— Choose the shortest between the startQ and endQ in hopes to alternate
     *   between them to meet somewhere at the middle. This optimizes the code, because we are processing smallest
     *   queue first, so the # of words in the queues dont blow up too fast. basically balancing between the two queues.
     *
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int ladderLength3(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;

        Set<String> unvisited = new HashSet<>(wordList);
        Set<String> startQ = new HashSet<>();  // 辅助正向 BFS 的集合
        Set<String> endQ = new HashSet<>();    // 辅助反向 BFS 的集合
        startQ.add(beginWord);
        endQ.add(endWord);

        int stepCount = 2;                     // stepCount 为最终所求的最短路径顶点数，从2开始是已包含头尾的顶点
        while (!startQ.isEmpty()) {
            Set<String> neighbours = new HashSet<>();  // 多个单词很有可能有重复的相邻单词，因此使用 set 去重
            for (String word: startQ) {        // 不同于解法1，这里要遍历 startQ 中的所有单词，为每一个单词寻找相邻单词（neighbouring words）
                for (int i = 0; i < word.length(); i++) {
                    StringBuilder transformed = new StringBuilder(word);
                    for (char c = 'a'; c <= 'z'; c++) {
                        if (c == word.charAt(i)) continue;
                        transformed.setCharAt(i, c);
                        String tWord = transformed.toString();
                        if (endQ.contains(tWord)) return stepCount;  // 本侧的相邻顶点也出现在对面方向的最外层顶点中，说明正反向查找相遇，找到了最短路径
                        if (unvisited.contains(tWord)) {
                            neighbours.add(tWord);
                            unvisited.remove(tWord);
                        }
                    }
                }
            }
            stepCount++;                            // 上面循环过程中没有 return 说明正反向查找还未相遇，因此让路径上的顶点数+1

            if (endQ.size() < neighbours.size()) {  // 若 endQ 中的顶点数少，则调换方向，下一轮从反向查找，遍历 endQ 中的顶点
                startQ = endQ;                      // 下一轮要遍历谁就把谁赋给 startQ
                endQ = neighbours;                  // 本轮中找到的相邻顶点（本侧最外层顶点）作为下一轮中的 endQ，用于检测是否正反向相遇
            }
            else startQ = neighbours;               // 若 endQ 中顶点多，则下一轮继续正向查找（即将本轮中正向的最外层顶点作为 startQ）
        }

        return 0;
    }

    /*
     * 解法4：解法3的递归版
     * - 思路：每次递归都相当于从正/反方向往前走一步，进行一次正/反向查找，查找这一侧最外层顶点的相邻顶点。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int ladderLength4(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;
        Set<String> unvisited = new HashSet<>(wordList);
        return ladderLength4(Collections.singleton(beginWord), Collections.singleton(endWord), unvisited, 2);
    }

    private static int ladderLength4(Set<String> startQ, Set<String> endQ, Set<String> unvisited, int stepCount) {
        if (startQ.isEmpty()) return 0;
        Set<String> neighbours = new HashSet<>();
        for (String word : startQ) {
            for (int i = 0; i < word.length(); i++) {
                StringBuilder transformed = new StringBuilder(word);
                for (char c = 'a'; c <= 'z'; c++) {
                    if (c == word.charAt(i)) continue;
                    transformed.setCharAt(i, c);
                    String tWord = transformed.toString();
                    if (endQ.contains(tWord)) return stepCount;  // 当正反向查找找到交点时，unvisited 也为空了（∵ 正反向查找时都会从中 remove 元素）
                    if (unvisited.contains(tWord)) {  // 不再像解法2中使用 visited 变量，而是将访问过的顶点从 unvisited 中移除，效果一样
                        neighbours.add(tWord);
                        unvisited.remove(tWord);
                    }
                }
            }
        }
        return endQ.size() < neighbours.size()
                ? ladderLength4(endQ, neighbours, unvisited, stepCount + 1)
                : ladderLength4(neighbours, endQ, unvisited, stepCount + 1);
    }

    /*
     * 解法5：生成 Graph + BFS
     * - 思路：不同于解法1-4，该解法先构建邻接矩阵更直接地将问题建模为图论问题，再通过 BFS 求解。而构建邻接矩阵的过程实际上就是确定
     *   顶点直接的链接关系，即每个顶点相邻顶点都有哪些。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int ladderLength5(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;
        if (!wordList.contains(beginWord)) wordList.add(beginWord);  // 需要把 beginWord 加入 wordList 才能开始建立图

        int n = wordList.size();
        boolean[][] graph = new boolean[n][n];  // 创建基于 wordList index 的邻接矩阵（邻接表也可以，SEE: L126 方法1）
        for (int i = 0; i < n; i++)
            for (int j = 0; j < i; j++)         // 注意是 i < j，即只遍历矩阵的左下部分，而通过下面的 graph[i][j] = graph[j][i] = .. 使得右上部分也被填充
                graph[i][j] = graph[j][i] = isSimilar(wordList.get(i), wordList.get(j));  // 矩阵中存储的是两两 word 是否相邻的关系

        return bsf(graph, wordList, beginWord, endWord);  // 在邻接矩阵上进行 BFS
    }

    private static int bsf(boolean[][] graph, List<String> wordList, String beginWord, String endWord) {
        Queue<Integer> q = new LinkedList<>();    // 队列中存储的是顶点在 wordList 中的 index（而非具体单词），因为构建的邻接矩阵也是使用顶点的 index 构建的
        int[] steps = new int[wordList.size()];   // steps 中每个位置存储从 beginWord 出发到 wordList 中对应位置上的单词的步数（这是不同于 Queue<String, Integer> 的另一种计数方式）
        int beginIndex = wordList.indexOf(beginWord);
        int endIndex = wordList.indexOf(endWord);

        q.offer(beginIndex);
        steps[beginIndex] = 1;                    // 因为题中要求最终结果里 beginWord 也算一步，因此这里初始化为1

        while (!q.isEmpty()) {
            int currIndex = q.poll();             // poll 出来的是 index，而非具体单词
            boolean[] edges = graph[currIndex];   // 在邻接矩阵中找到当前顶点与其他顶点的链接关系
            for (int i = 0; i < edges.length; i++) {
                if (edges[i] && steps[i] == 0) {  // 如果与 currIndex 所指单词相邻，且还未被访问过
                    if (i == endIndex) return steps[currIndex] + 1;
                    steps[i] = steps[currIndex] + 1;
                    q.offer(i);
                }
            }
        }
        return 0;
    }

    /*
     * 解法6：生成 Graph + Bi-directional BFS
     * - 思路：在解法5的邻接矩阵的基础上使用 Bi-directional BFS。但该解法中 Bi-directional BFS 的实现（即 biDirectionalBfs
     *   方法）不同于解法3、4中两个方向交替进行查找的方式，而是：
     *   1. 同时从起点和终点开始对整个图进行 BFS。
     *   2. 每次正向和反向都向前查找一个顶点的所有相邻顶点，计算从起/终点到这些顶点的步数，并分别记录在 beginSteps 和 endSteps 中。
     *   3. 检测 beginSteps、endSteps 中是否存在能同时从起、终点到达的顶点，并从所有这样的路径中求出最短的来。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int ladderLength6(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;
        if (!wordList.contains(beginWord)) wordList.add(beginWord);

        int n = wordList.size();
        boolean[][] graph = new boolean[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < i; j++)
                graph[i][j] = graph[j][i] = isSimilar(wordList.get(i), wordList.get(j));

        return biDirectionalBfs(graph, wordList, beginWord, endWord);  // 双向 BFS
    }

    private static int biDirectionalBfs(boolean[][] graph, List<String> wordList, String beginWord, String endWord) {
        Queue<Integer> beginQ = new LinkedList<>();    // 队列中存储的是顶点在 wordList 中的 index
        Queue<Integer> endQ = new LinkedList<>();
        int beginIndex = wordList.indexOf(beginWord);  // 首先入队的是起点和终点的 index，因此要先获取他们
        int endIndex = wordList.indexOf(endWord);
        beginQ.offer(beginIndex);
        endQ.offer(endIndex);

        int n = wordList.size();
        int[] beginSteps = new int[n], endSteps = new int[n];  // 为正向和反向 BFS 各设置一个 steps 数组，这样会不互相干扰
        beginSteps[beginIndex] = endSteps[endIndex] = 1;

        while (!beginQ.isEmpty() && !endQ.isEmpty()) {  // 若其中一个方向的查找完成时还没有从起点到终点的路径出现则说明无解，程序结束
            int currBeginIndex = beginQ.poll(), currEndIndex = endQ.poll();  // 正、反向队列分别出队一个顶点的 index

            for (int i = 0; i < n; i++) {                              // 从所有顶点的 index 中...
                if (graph[currBeginIndex][i] && beginSteps[i] == 0) {  // 1. 找到 currBeginIndex 的相邻顶点的 index
                    beginSteps[i] = beginSteps[currBeginIndex] + 1;    // 2. 计算起点到该相邻顶点的步数
                    beginQ.offer(i);                                   // 3. 将该相邻顶点的 index 入队
                }
                if (graph[currEndIndex][i] && endSteps[i] == 0) {      // 4. 找到 currEndIndex 的相邻顶点的 index
                    endSteps[i] = endSteps[currEndIndex] + 1;          // 5. 计算终点到该相邻顶点的步数
                    endQ.offer(i);                                     // 6. 将该相邻顶点的 index 入队
                }
            }
            // check intersection
            int minStep = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++)
                if (beginSteps[i] != 0 && endSteps[i] != 0)  // 若 beginSteps、endSteps 在 i 位置上同时有值说明从起、终点都能到达 i 上的顶点，即找到一条联通起终点的路径
                    minStep = Integer.min(minStep, beginSteps[i] + endSteps[i] - 1);  // 求所有联通路径中最短的那条

            if (minStep != Integer.MAX_VALUE)
                return minStep;
        }

        return 0;
    }

    public static void main(String[] args) {
        List<String> wordList = new ArrayList<>(Arrays.asList("hot", "dot", "dog", "lot", "log", "cog"));
        log(ladderLength3("hit", "cog", wordList));
        // expects 5. (One shortest transformation is "hit" -> "hot" -> "dot" -> "dog" -> "cog")

        List<String> wordList2 = new ArrayList<>(Arrays.asList("a", "b", "c"));
        log(ladderLength3("a", "c", wordList2));
        // expects 2. ("a" -> "c")

        List<String> wordList3 = new ArrayList<>(Arrays.asList("ted", "tex", "red", "tax", "tad", "den", "rex", "pee"));
        log(ladderLength3("red", "tax", wordList3));
        // expects 4. (One shortest transformation is "red" -> "ted" -> "tad" -> "tax")

        List<String> wordList4 = new ArrayList<>(Arrays.asList("hot", "dot", "dog", "lot", "log"));
        log(ladderLength3("hit", "cog", wordList4));
        // expects 0. (The endWord "cog" is not in wordList, therefore no possible transformation)

        List<String> wordList5 = new ArrayList<>(Arrays.asList("hot", "dog"));
        log(ladderLength3("hot", "dog", wordList5));
        // expects 0. (No solution)

        List<String> wordList6 = new ArrayList<>(Arrays.asList("lest", "leet", "lose", "code", "lode", "robe", "lost"));
        log(ladderLength3("leet", "code", wordList6));
        // expects 6. ("leet" -> "lest" -> "lost" -> "lose" -> "lode" -> "code")

        List<String> wordList7 = new ArrayList<>(Arrays.asList("miss", "dusk", "kiss", "musk", "tusk", "diss", "disk", "sang", "ties", "muss"));
        log(ladderLength3("kiss", "tusk", wordList7));
        // expects 5. ("kiss" -> "miss" -> "muss" -> "musk" -> "tusk")
    }
}
