package StackAndQueue.S4_BFSAndGraphShortestPath;

import Utils.Helpers.Pair;

import java.util.*;

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
 * - 注意：本题求的是最短路径上的顶点数（包含头尾顶点），而非最短路径的上步数（L279 中求的是步数，即顶点数-1），这个要区分清楚。
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
            int stepCount = pair.getValue();

            for (String w : wordList) {
                if (isSimilar(w, word)) {
                    if (w.equals(endWord)) return stepCount + 1;
                    q.offer(new Pair<>(w, stepCount + 1));
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
     * - 👉 注意：当需要一边遍历 Set，一边增/删其中元素（动态增删）时，不能使用 for、while、forEach，需要使用 iterator。
     * - 优化：更简单的做法是使用 Set 记录已经访问过的顶点，这样即不需要复制 wordList 也不需要在入队之后将访问的顶点从 Set 中移除。
     * - 时间复杂度 O(n^2 * l)，其中 l 为单词长度；空间复杂度 O(n)。
     * */
    public static int ladderLength1(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;
        Set<String> unvisited = new HashSet<>(wordList);
        Queue<Pair<String, Integer>> q = new LinkedList<>();  // Queue<Pair<word, stepCount>>
        q.offer(new Pair<>(beginWord, 1));  // 注意 ∵ 题目要求 A -> B -> C 算3步而非2步 ∴ beginWord 的 stepCount 为1

        while (!q.isEmpty()) {              // 最差情况下遍历了所有顶点才到达 endWord ∴ 时间复杂度 O(n)
            Pair<String, Integer> pair = q.poll();
            String word = pair.getKey();
            int stepCount = pair.getValue();

            if (word.equals(endWord)) return stepCount;

            Iterator<String> it = unvisited.iterator();  // 遍历 unvisited 而非 wordList，时间复杂度 O(n)
            while (it.hasNext()) {
                String w = it.next();
                if (isSimilar(w, word)) {                // 寻找可替换单词，时间复杂度 O(len(word))
                    q.offer(new Pair<>(w, stepCount + 1));
                    it.remove();                         // 从 unvisited 中删除（动态删除 unvisited 中的元素）
                }
            }
        }

        return 0;
    }

    /*
     * 解法2：解法1的性能优化版
     * - 思路：与解法1一致。
     * - 实现：性能优化点在于寻找相邻顶点的过程：不同于解法1中的 isSimilar，该解法尝试对 word 中的每个字母用 a-z 进行替换，若替换
     *   后的 tWord 存在于 wordList 中且未被访问过，则说明 tWord 与 word 相邻。∵ 只有26个字母 ∴ 用该方法搜索单个节点的相邻节点的
     *   复杂度为 len(word) * 26；而解法1中 isSimilar 的复杂度为 n * len(word)。∵ 实际当中字典一般都满足 n >> 26 ∴ 该解法的
     *   性能会由于解法1。
     * - 时间复杂度 O(26 * n * l^2)，其中 l 为单词长度；空间复杂度 O(n)。
     * */
    public static int ladderLength2(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;
        Queue<Pair<String, Integer>> q = new LinkedList<>();
        Set<String> unvisited = new HashSet<>(wordList);
        q.offer(new Pair<>(beginWord, 1));

        while (!q.isEmpty()) {
            Pair<String, Integer> p = q.poll();
            String word = p.getKey();
            int stepCount = p.getValue();

            if (word.equals(endWord)) return stepCount;

            for (int i = 0; i < word.length(); i++) {  // 为 word 中的每个字母进行替换匹配，O(len(word) * 26)
                StringBuilder sb = new StringBuilder(word);
                for (char c = 'a'; c <= 'z'; c++) {    // 👉 for 可以直接遍历 ASCII 字符
                    if (c == word.charAt(i)) continue;
                    sb.setCharAt(i, c);                // 上面创建 StringBuilder 是为了这里能按索引修改字符串中的字符
                    String tWord = sb.toString();
                    if (unvisited.contains(tWord)) {   // unvisitied 中有 tWord，说明找到了一个相邻顶点（在 Set 中匹配字符串的复杂度为 O(len(word))）
                        q.offer(new Pair<>(tWord, stepCount + 1));
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
     *   - 使用条件：1. 已知头尾两个顶点；2. 两个方向的 branching factor 相同。
     *
     * - 思路：使用2个队列 beginQ、endQ，分别从 beginWord/endWord 开始交替进行正/反向 BFS，即交替遍历 beginQ、endQ 中的
     *   所有顶点，为每一个顶点寻找相邻顶点：
     *     1. 若其中任一相邻顶点出现在另一队列中（即出现在对面方向最外层顶点中），则说明正反向查找相遇，找到了最短路径，返回顶点数即可；
     *     2. 若没有相邻顶点出现在另一队列中，则说明正反向查找还未相遇，则调换方向继续另一端的 BFS 过程（将 endQ 作为 beginQ 开始
     *        新一轮遍历、将 neighbours 作为 endQ 用于查看下一轮中的相邻顶点是否出现在对面方向最外层顶点中）。
     *
     * - 实现：
     *   1. ∵ 本题中进行 BFS 时的顺序不重要（先访问哪个相邻顶点都可以，这是 graph 的一个特点），且对于两边队列中的每个顶点都要检查
     *      其是否存在于另一边的队列（即另一边 BFS 的最外层顶点）中 ∴ 两个队列 beginQ、endQ 可使用 Set 实现（beginSet、endSet）。
     *   2. ∵ 使用 Set 代替队列 ∴ 对于 BFS 过程中找到的相邻顶点，不再需要将其入队回去，而是放入一个 neighbours Set 中，最后在
     *      调换方向时直接用它来替换其中一边的队列 Set。
     *   3. 在调换方向时，可以加一步判断 —— 不是每次都交替遍历 beginSet 和 endSet，而是每次都找到两者中元素数少的进行遍历。
     *      这样可以让两边队列 Set 中的元素数保持相对平衡，不会在一边持续产生指数型增长。
     *
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int ladderLength3(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;

        Set<String> visited = new HashSet<>();
        Set<String> beginSet = new HashSet<>();  // 用于正向 BFS
        Set<String> endSet = new HashSet<>();    // 用于反向 BFS
        beginSet.add(beginWord);
        endSet.add(endWord);
        int stepCount = 2;                       // 从2开始是已包含头尾顶点

        while (!beginSet.isEmpty()) {
            Set<String> neighbours = new HashSet<>();  // 多个顶点有可能有相同的相邻顶点 ∴ 使用 Set 去重
            for (String word : beginSet) {             // 遍历 beginSet，为每一个单词寻找相邻单词（neighbouring words）
                for (String w : wordList) {
                    if (!visited.contains(w) && isSimilar(word, w)) {
                        if (endSet.contains(w)) return stepCount;  // 若本侧的相邻顶点出现在另一边 BFS 的最外层顶点中，
                        neighbours.add(w);                         // 说明正反向查找相遇，找到了最短路径
                    }
                }
                visited.add(word);
            }
            stepCount++;                              // 若上面过程中没有 return，说明正反向查找还未相遇 ∴ 让路径步数+1

            if (endSet.size() < neighbours.size()) {  // 调换方向之前先判断两边 set 中的顶点数，谁少就用谁做 beginSet
                beginSet = endSet;
                endSet = neighbours;
            } else {
                beginSet = neighbours;
            }
        }

        return 0;
    }

    /*
     * 解法4：解法3的递归版
     * - 思路：每轮递归都从正或反方向进行一步 BFS，查找出这一侧最外层顶点的相邻顶点。
     * - 实现：字母替换匹配的方式与解法2中的方式稍有区别，采用 char[] 而非 StringBuilder。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int ladderLength4(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;
        Set<String> unvisited = new HashSet<>(wordList);
        return helper4(Collections.singleton(beginWord), Collections.singleton(endWord), unvisited, 2);
    }

    private static int helper4(Set<String> beginSet, Set<String> endSet, Set<String> unvisited, int stepCount) {
        if (beginSet.isEmpty()) return 0;  // 递归结束条件是当一边已找不到未访问过的相邻顶点，此时若正反向 BFS 还未相遇，则说明两点间无连通路径
        Set<String> neighbours = new HashSet<>();

        for (String word : beginSet) {                  // 遍历 beginSet 中的单词
            for (int i = 0; i < word.length(); i++) {   // 开始为每个单词的每个字母进行替换匹配
                char[] chars = word.toCharArray();
                for (char c = 'a'; c <= 'z'; c++) {
                    if (c == chars[i]) continue;
                    chars[i] = c;
                    String tWord = new String(chars);
                    if (endSet.contains(tWord)) return stepCount;  // tWord 在对面最外层出现说明正反向 BFS 相遇，得到最短路径
                    if (unvisited.contains(tWord)) {
                        neighbours.add(tWord);
                        unvisited.remove(tWord);
                    }
                }
            }
        }

        return endSet.size() < neighbours.size()  // 选择元素少的一边进行下一轮 BFS
                ? helper4(endSet, neighbours, unvisited, stepCount + 1)
                : helper4(neighbours, endSet, unvisited, stepCount + 1);
    }

    /*
     * 解法5：先构建 Graph + BFS
     * - 思路：不同于解法1-4中遍历到某个顶点时再现去搜索相邻顶点，该解法先构建无向图的邻接矩阵（Adjacency Matrix），一次性梳理出
     *   所有节点间的链接关系，从而让后面的 BFS 可以直接使用邻接矩阵找到任意顶点的所有相邻顶点；
     * - 实现：
     *     1. 邻接矩阵本质上就是一个 boolean[][]，描述各顶点之间的链接关系。本解法中构建的是无向图的邻接矩阵（更多介绍
     *        SEE: Play-with-algorithms/GraphBasics/_Introduction.txt）。其实用邻接表也可以（SEE: L126 解法1）。
     *     2. ∵ 邻接矩阵是基于顶点的 index 构建的 ∴ 在进行 BFS 时，队列中存储的也应是顶点的 index，统一操作方式。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int ladderLength5(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;
        if (!wordList.contains(beginWord)) wordList.add(beginWord);  // 需先把 beginWord 加入 wordList 才能开始建立图

        int n = wordList.size();
        boolean[][] graph = new boolean[n][n];  // 基于 wordList 创建邻接矩阵
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++)     // j 从 i+1 开始，不重复的遍历 wordList 中所有的两两组合
                if (isSimilar(wordList.get(i), wordList.get(j)))
                    graph[i][j] = graph[j][i] = true;  // ∵ 要构建无向图的邻接矩阵 ∴ 数据分布沿对角线对称

        Queue<Pair<Integer, Integer>> q = new LinkedList<>();
        q.offer(new Pair<>(wordList.indexOf(beginWord), 1));
        int endIndex = wordList.indexOf(endWord);
        Set<Integer> visited = new HashSet<>();

        while (!q.isEmpty()) {                  // 开始基于 graph 进行 BFS
            Pair<Integer, Integer> p = q.poll();
            int i = p.getKey();
            int stepCount = p.getValue();

            if (i == endIndex) return stepCount;

            for (int j = 0; j < graph[i].length; j++) {  // 在 graph 上查找所有与索引为 i 的 word 相邻的单词
                if (graph[i][j] && !visited.contains(j)) {
                    q.offer(new Pair<>(j, stepCount + 1));
                    visited.add(j);
                }
            }
        }

        return 0;
    }

    /*
     * 解法6：先构建 Graph + BFS 生成 steps 数组
     * - 思路：在解法5的基础上，不在 BFS 过程中给每个顶点带上步数信息（即不在 Queue 中存储 Pair<顶点，起点到该顶点的步数>），
     *   而是通过 BFS 建立一个 steps 数组，每个位置保存起点到每个顶点的最小步数信息，最终获得起点到终点的最小步数。
     * - 实现：
     *   1. steps[i] = n 表示从 beginWord 到 wordList[i] 的最小步数为 n。
     *   2. BFS 过程中，每个顶点 i 都可能被当做其他顶点的相邻顶点而被多次访问，但只有第一次访问时才能给 steps[i] 赋值，
     *      ∵ BFS 的最大特点就是从起点扩散性的向外逐层访问顶点 ∴ 第一次访问到某个顶点时走过的路径就是从起点到该顶点的最短路径。
     *   3. ∵ 不会重复给 steps 中的元素赋值 ∴ steps 实际上起到了解法1-5中 visited/unvisited Set 的作用。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int ladderLength6(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;
        if (!wordList.contains(beginWord)) wordList.add(beginWord);
        boolean[][] graph = buildAdjacencyMatrix(wordList);
        return bfs6(graph, wordList, beginWord, endWord);
    }

    private static boolean[][] buildAdjacencyMatrix(List<String> wordList) {
        int n = wordList.size();
        boolean[][] graph = new boolean[n][n];

        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++)
                graph[i][j] = graph[j][i] = isSimilar(wordList.get(i), wordList.get(j));

        return graph;
    }

    private static int bfs6(boolean[][] graph, List<String> wordList, String beginWord, String endWord) {
        Queue<Integer> q = new LinkedList<>();   // ∵ 邻接矩阵中的顶点使用 index 访问 ∴ BFS 遍历时队列中存储的也是顶点的 index
        int[] steps = new int[wordList.size()];  // steps[i] 表示从 beginWord 到 wordList 中第 i 个单词的最短步数
        int beginIndex = wordList.indexOf(beginWord);
        int endIndex = wordList.indexOf(endWord);

        steps[beginIndex] = 1;  // ∵ beginWord 也算一步 ∴ 初始化为1
        q.offer(beginIndex);

        while (!q.isEmpty()) {
            int i = q.poll();
            if (i == endIndex) return steps[i];
            for (int j = 0; j < graph[i].length; j++) {  // 遍历邻接矩阵中的第 i 行，检查每个 wordList[j] 是否与 wordList[i] 相邻
                if (graph[i][j] && steps[j] == 0) {      // 若相邻且还未被访问过（第一次访问时的步数就是最小步数 ∴ 不能覆盖）
                    steps[j] = steps[i] + 1;
                    q.offer(j);                          // 将该相邻顶点的 index 放入 q 中
                }
            }
        }

        return 0;
    }

    /*
     * 解法7：先构建 Graph + Bi-directional BFS
     * - 思路：在解法5的邻接矩阵的基础上使用 Bi-directional BFS。
     * - 实现：不同于解法3、4中两个方向交替进行查找的方式，该解法中：
     *     1. 同时从起点和终点开始进行 BFS。
     *     2. 每次正向和反向都向前查找一个顶点的所有相邻顶点，并计算从起/终点到这些顶点的步数，别记录在 beginSteps、endSteps 中。
     *     3. 每一步 BFS 完之后都检测 beginSteps、endSteps 中是否存在共同的顶点，并从所有公共顶点中找出到达起、终点最短的路径长度来。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int ladderLength7(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;
        if (!wordList.contains(beginWord)) wordList.add(beginWord);

        int n = wordList.size();
        boolean[][] graph = new boolean[n][n];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < i; j++)
                graph[i][j] = graph[j][i] = isSimilar(wordList.get(i), wordList.get(j));

        return biDirectionalBfs(graph, wordList, beginWord, endWord);  // 借助邻接表进行双向 BFS
    }

    private static int biDirectionalBfs(boolean[][] graph, List<String> wordList, String beginWord, String endWord) {
        Queue<Integer> beginQ = new LinkedList<>();
        Queue<Integer> endQ = new LinkedList<>();
        int beginIndex = wordList.indexOf(beginWord);
        int endIndex = wordList.indexOf(endWord);
        beginQ.offer(beginIndex);
        endQ.offer(endIndex);

        int n = wordList.size();
        int[] beginSteps = new int[n], endSteps = new int[n];  // 为正、反向 BFS 各设置一个 steps 数组，这样会不互相干扰
        beginSteps[beginIndex] = endSteps[endIndex] = 1;

        while (!beginQ.isEmpty() && !endQ.isEmpty()) {  // 若其中一个方向的查找完成时还没有从起点到终点的路径出现则说明无解，程序结束
            int currBeginIndex = beginQ.poll(), currEndIndex = endQ.poll();  // 正、反向队列分别出队一个顶点的 index

            for (int i = 0; i < n; i++) {                              // 从所有顶点的 index 中...
                if (graph[currBeginIndex][i] && beginSteps[i] == 0) {  // 1. 找到 currBegin 顶点相邻，且未访问过的顶点的 index
                    beginSteps[i] = beginSteps[currBeginIndex] + 1;    // 2. 计算起点到该相邻顶点的步数
                    beginQ.offer(i);                                   // 3. 将该相邻顶点的 index 入队
                }
                if (graph[currEndIndex][i] && endSteps[i] == 0) {      // 4. 找到 currEnd 顶点的相邻顶点的 index
                    endSteps[i] = endSteps[currEndIndex] + 1;          // 5. 计算终点到该相邻顶点的步数
                    endQ.offer(i);                                     // 6. 将该相邻顶点的 index 入队
                }
            }
            // check intersection
            int minStep = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++)
                if (beginSteps[i] != 0 && endSteps[i] != 0)  // 若 beginSteps、endSteps 在 i 位置上同时有值说明从起、终点都能到达 i 上的顶点，即找到一条联通起终点的路径
                    minStep = Math.min(minStep, beginSteps[i] + endSteps[i] - 1);  // 求所有联通路径中最短的那条

            if (minStep != Integer.MAX_VALUE)
                return minStep;
        }

        return 0;
    }

    public static void main(String[] args) {
        List<String> wordList = new ArrayList<>(Arrays.asList("hot", "dot", "dog", "lot", "log", "cog"));
        log(ladderLength4("hit", "cog", wordList));
        // expects 5. (One shortest transformation is "hit" -> "hot" -> "dot" -> "dog" -> "cog")

        List<String> wordList2 = new ArrayList<>(Arrays.asList("a", "b", "c"));
        log(ladderLength4("a", "c", wordList2));
        // expects 2. ("a" -> "c")

        List<String> wordList3 = new ArrayList<>(Arrays.asList("ted", "tex", "red", "tax", "tad", "den", "rex", "pee"));
        log(ladderLength4("red", "tax", wordList3));
        // expects 4. (One shortest transformation is "red" -> "ted" -> "tad" -> "tax")

        List<String> wordList4 = new ArrayList<>(Arrays.asList("hot", "dot", "dog", "lot", "log"));
        log(ladderLength4("hit", "cog", wordList4));
        // expects 0. (The endWord "cog" is not in wordList, therefore no possible transformation)

        List<String> wordList5 = new ArrayList<>(Arrays.asList("hot", "dog"));
        log(ladderLength4("hot", "dog", wordList5));
        // expects 0. (No solution)

        List<String> wordList6 = new ArrayList<>(Arrays.asList("lest", "leet", "lose", "code", "lode", "robe", "lost"));
        log(ladderLength4("leet", "code", wordList6));
        // expects 6. ("leet" -> "lest" -> "lost" -> "lose" -> "lode" -> "code")

        List<String> wordList7 = new ArrayList<>(Arrays.asList("miss", "dusk", "kiss", "musk", "tusk", "diss", "disk", "sang", "ties", "muss"));
        log(ladderLength4("kiss", "tusk", wordList7));
        // expects 5. ("kiss" -> "miss" -> "muss" -> "musk" -> "tusk")
    }
}
