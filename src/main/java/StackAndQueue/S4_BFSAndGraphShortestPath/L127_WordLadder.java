package StackAndQueue.S4_BFSAndGraphShortestPath;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import Utils.Helpers.Pair;

import static Utils.Helpers.log;

/*
 * Word Ladder
 *
 * - Given two words (beginWord and endWord), and a dictionary's word list, find the length of shortest
 *   transformation sequence from beginWord to endWord, such that:
 *     1. Only one letter can be changed at a time.
 *     2. Each transformed word must exist in the word list. Note that beginWord is not a transformed word.
 *     3. Return 0 if there is no such transformation sequence.
 *   Note that:
 *     1. All words have the same length.
 *     2. All words contain only lowercase alphabetic characters.
 *     3. You may assume no duplicates in the word list.
 *     4. You may assume beginWord and endWord are non-empty and are not the same.
 *
 * - 注意：本题求的是最短路径上的顶点数（包含头尾顶点），而非最短路径的上步数（L279 中求的是步数），这个要区分清楚。
 * */

public class L127_WordLadder {
    /*
     * 超时解（但结果正确）：BFS
     * - 思路：该题是个典型求最短路径的题，而求图上两点的最短路径可采用 BFS。
     * - 实现：∵ 题中要求返回最短路径的步数 ∴ 队列中除了存储路径上的每一个顶点之外，还要存储从起点开始到该顶点的步数信息。
     * */
    public static int ladderLength(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;            // 无解的情况
        Queue<Pair<String, Integer>> q = new LinkedList<>();  // BFS 需要使用 Queue 作为辅助数据结构
        q.offer(new Pair<>(beginWord, 1));                    // ∵ 最终返回的步数是包括 beginWord 的 ∴ 要从1开始计数（不同于 L279 解法1）

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
     * 解法1：超时解的性能优化版
     * - 思路：与解法1一致，使用 BFS。
     * - 实现：解法1的问题在于同一个顶点会被重复访问（例如，顶点 A、B 都与 C 相邻，当分别为 A、B 寻找相邻顶点时，C 会被入队2次）。
     *   ∴ 需要一个 Set 记录哪些顶点还未被访问过，并且在寻找相邻顶点时只在该 Set 中寻找。
     * - 注意：当需要一边遍历 Set，一边增/删其中元素（动态增删）时，不能使用 for、while、forEach，需要使用 iterator。
     * - 优化：更简单的做法是使用 Set 记录已经访问过的顶点，这样就不需要入队之后将访问的顶点从 Set 中移除。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int ladderLength1(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;
        Set<String> unvisited = new HashSet<>(wordList);
        Queue<Pair<String, Integer>> q = new LinkedList<>();
        q.offer(new Pair<>(beginWord, 1));

        while (!q.isEmpty()) {                         // 最差情况下遍历了所有顶点才到达 endWord ∴ 时间复杂度 O(n)
            Pair<String, Integer> pair = q.poll();
            String word = pair.getKey();
            int step = pair.getValue();

            Iterator<String> it = unvisited.iterator();
            while (it.hasNext()) {                     // 遍历 unvisited 而非 wordList，时间复杂度 O(n)
                String w = it.next();
                if (isSimilar(w, word)) {
                    if (w.equals(endWord)) return step + 1;
                    q.offer(new Pair<>(w, step + 1));  // 将相邻顶点入队待访问
                    it.remove();                       // 从 unvisited 中删除（动态删除 unvisited 中的元素）
                }
            }
        }

        return 0;
    }

    /*
     * 解法2：解法1的性能优化版
     * - 思路：与解法1一致。
     * - 实现：性能优化点在于寻找相邻顶点的过程：不同于解法1中的 isSimilar，该解法尝试对 word 中的每个字母用 a-z 进行替换，
     *   若替换后的 tWord 存在于 unvisitied 中，则说明 tWord 与 word 相邻。∵ 只有26个字母 ∴ 该方法复杂度为 len(word) * 26；
     *   而解法1中 isSimilar 的复杂度为 wordList.size() * len(word)，若 wordList 中元素个数 >26 时，性能会差于本解法。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int ladderLength2(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;
        Queue<Pair<String, Integer>> q = new LinkedList<>();
        Set<String> unvisited = new HashSet<>(wordList);
        q.offer(new Pair<>(beginWord, 1));

        while (!q.isEmpty()) {
            Pair<String, Integer> p = q.poll();
            String word = p.getKey();
            int step = p.getValue();

            for (int i = 0; i < word.length(); i++) {  // 替换 word 中的每个字母，查看替换后的单词 tWord 是否与 word 相邻
                StringBuilder wordSb = new StringBuilder(word);
                for (char c = 'a'; c <= 'z'; c++) {
                    if (c == word.charAt(i)) continue;
                    wordSb.setCharAt(i, c);            // 上面创建 StringBuilder 是为了这里能按索引修改字符串中的字符
                    String tWord = wordSb.toString();
                    if (unvisited.contains(tWord)) {   // 若 unvisitied 中有 tWord，则说明找到了一个相邻顶点
                        if (tWord.equals(endWord)) return step + 1;
                        q.offer(new Pair<>(tWord, step + 1));
                        unvisited.remove(tWord);
                    }
                }
            }
        }

        return 0;
    }

    /*
     * 解法3：Bi-directional BFS
     * - 策略：采用 Bi-directional BFS 能有效减小搜索复杂度：
     *   - 复杂度：设 branching factor 是 b，两点间距是 d，则单向 BFS/DFS 的时间及空间复杂度为 O(b^d)，而双向 BFS 的时间
     *     及空间复杂度为 O(b^(d/2) + b^(d/2)) 即 O(b^(d/2))，比起 O(b^d) 要小得多。
     *   - 使用条件：1. 已知头尾两个顶点  2. 两个方向的 branching factor 相同。
     *
     * - 思路：使用2个队列 startQ、endQ，分别从 beginWord/endWord 开始交替进行正/反向 BFS，即交替遍历 startQ、endQ 中的
     *   所有顶点，为每一个顶点寻找相邻顶点：
     *     1. 若其中任一相邻顶点出现在另一队列中（即出现在对面方向最外层顶点中），则说明正反向查找相遇，找到了最短路径，返回顶点数即可；
     *     2. 若没有相邻顶点出现在另一队列中，则说明正反向查找还未相遇，则调换方向继续另一端的 BFS 过程。
     * （将 endQ 作为 startQ 开始新一轮遍历、将 neighbours 作为 endQ 用于查看下一轮
     *         中的相邻顶点是否出现在对面方向最外层顶点中）。
     *
     * - 实现：
     *   1. ∵ 本题中进行 BFS 时的顺序不重要（先访问哪个相邻顶点都可以，这是 graph 的一个特定），且对于两边队列中的每个顶点都要
     *      检查其是否存在于另一边的队列（即另一边 BFS 的最外层顶点）中 ∴ 两边的队列 startQ、endQ 可使用 Set 实现（startSet、endSet）。
     *   2. ∵ 使用 Set 代替队列 ∴ 对于 BFS 过程中找到的相邻顶点，不再需要将其入队回去，而是放入一个 neighbours Set 中，
     *      最后在调换方向时直接用它来替换其中一边的队列 Set。
     *   3. 在调换方向时，可以加一步判断 —— 不是每次都交替遍历 startSet 和 endSet，而是每次都找到两者中元素数少的进行遍历。
     *      这样可以让两边队列 Set 中的元素数保持相对平衡，不会在一边持续产生指数型增长。
     *
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int ladderLength3(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;

        Set<String> visited = new HashSet<>();
        Set<String> startSet = new HashSet<>();  // 用于正向 BFS
        Set<String> endSet = new HashSet<>();    // 用于反向 BFS
        startSet.add(beginWord);
        endSet.add(endWord);
        int stepCount = 2;                       // 从2开始是已包含头尾顶点

        while (!startSet.isEmpty()) {
            Set<String> neighbours = new HashSet<>();  // 多个顶点有可能有相同的相邻顶点 ∴ 使用 Set 去重
            for (String word : startSet) {             // 遍历 startSet，为每一个单词寻找相邻单词（neighbouring words）
                for (String w : wordList) {
                    if (visited.contains(w)) continue;
                    if (isSimilar(word, w)) {
                        if (endSet.contains(w)) return stepCount;  // 若本侧的相邻顶点出现在另一边 BFS 的最外层顶点中，
                        neighbours.add(w);                         // 说明正反向查找相遇，找到了最短路径
                    }
                }
                visited.add(word);
            }
            stepCount++;                              // 若上面过程中没有 return，说明正反向查找还未相遇 ∴ 让路径步数+1

            if (endSet.size() < neighbours.size()) {  // 调换方向之前先判断两边 set 中的节点数，谁少就用谁做 startSet
                startSet = endSet;
                endSet = neighbours;
            } else {
                startSet = neighbours;
            }
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
