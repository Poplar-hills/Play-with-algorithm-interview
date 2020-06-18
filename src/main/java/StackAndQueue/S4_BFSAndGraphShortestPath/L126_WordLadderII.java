package StackAndQueue.S4_BFSAndGraphShortestPath;

import java.util.*;

import static Utils.Helpers.log;

/*
 * Word Ladder II
 *
 * - 题目与 L127 一致，区别在于要返回所有的最短路径。
 *
 * - 👉该题是非常有助于理解 BFS、DFS 的各自优势和局限性，以及在如何各尽所能相互配合。
 * */

public class L126_WordLadderII {
    /*
     * 超时解：
     * - 思路：与“找出图上任意两点之间的所有路径”的思路一致（SEE: Play-with-algorithms/Graph/Path 中的 allPaths 方法）。
     * - 实现：BFS 过程中，第一次找到的路径一定是最短路径。根据这一规律，可以停止对超过最短路径长度的其他路径的遍历。
     * - 时间复杂度 O(n^n)：虽然有进行优化，但复杂度量级没变 —— 每找到一个相邻顶点都可能多出 n 种可能 ∴ 是 O(n^n)。
     * */
    public static List<List<String>> findLadders_1(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> res = new ArrayList<>();
        if (!wordList.contains(endWord)) return res;

        Queue<List<String>> q = new LinkedList<>();
        List<String> initialList = new ArrayList<>();
        initialList.add(beginWord);
        q.offer(initialList);
        Integer minStep = null;

        while (!q.isEmpty()) {
            List<String> path = q.poll();
            if (minStep != null && path.size() == minStep) continue;  // 若 q 中拿出来的 path 长度已经等于 minStep 则抛弃掉
            String lastWord = path.get(path.size() - 1);

            for (String w : wordList) {
                if (isSimilar(w, lastWord)) {
                    if (w.equals(endWord)) {
                        List<String> newPath = new ArrayList<>(path);
                        newPath.add(w);
                        res.add(newPath);
                        minStep = newPath.size();  // 第一次找到的路径一定是最短路径 ∴ 若之后再找到的路径的长度 > 该路径长度，则不是最短路径
                        continue;
                    }
                    if (minStep != null && path.size() + 1 == minStep) continue;  // 若 w 不是 endWord 且 path 长度已经为 minStep-1 则抛弃
                    if (path.contains(w)) continue;                               // 若 w 已存在于该路径中，则说明已经访问过，不再继续（否则会成环）
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(w);
                    q.offer(newPath);
                }
            }
        }

        return res;
    }

    /*
     * 解法1：构建邻接表 + BFS + DFS
     * - 思路：BFS 在求解“单条最短路径步数”问题时非常好用，但对于“求所有最短路径”问题则会产生超高复杂度（👆超时解）。改进方法是
     *   通过扩散性的 BFS 生成一个对 DFS 友好的辅助数据结构（即 L127 解法5中的 steps 数组），记录从起点到达每个顶点的最小步数，
     *   再基于该结构使用纵深性的 DFS 快速找到所有最短路径（即需要配合使用 BFS、DFS）。
     * - 实现：
     *   1. 在 BFS 之前要生成 graph，而本解法中构建的 graph 是无向邻接表（Adjacency List），若用邻接矩阵则会超时。
     *   2. 为了便于查找，本解法中的 steps 使用 Map 实现。
     *   3. DFS 过程：从 beginWord 出发，借助 steps 中的信息查找哪个（或哪几个）相邻 word 是最短路径上的下一个顶点，如此重复
     *      直到到达 endWord，并记录下沿途的顶点即可获得最短路径。
     * - 👉注意：DFS 的实现是基于回溯法的（SEE: https://mp.weixin.qq.com/s/sAutzAzhaGArkl2Ban5guA）。一般说起“回溯”，
     *   指的也就是 DFS，这两个词是 interchangable 的。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * - 👉注意：一般来说 DFS、BFS 的时间复杂度都是 O(V+E)，但具体要看数据结构，对于邻接矩阵是 O(V^2)，对于邻接表是 O(V+E)。
     * */
    public static List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> res = new ArrayList<>();
        if (!wordList.contains(endWord)) return res;
        if (!wordList.contains(beginWord)) wordList.add(beginWord);

        List<List<Integer>> graph = buildGraph(wordList);                // 先构建无向邻接表

        int beginIndex = wordList.indexOf(beginWord);
        int endIndex = wordList.indexOf(endWord);
        Map<Integer, Integer> steps = bfs(graph, beginIndex, wordList);  // 通过 BFS 来填充 steps map

        List<Integer> path = new ArrayList<>(beginIndex);                // 回溯中待填充的路径（存储最短路径上每个顶点的 index）
        dfs(graph, beginIndex, endIndex, wordList, steps, path, res);    // 通过 DFS 搜索填充 path，再转换成 word path 后放入 res

        return res;
    }

    private static List<List<Integer>> buildGraph(List<String> worList) {
        int n = worList.size();
        List<List<Integer>> graph = new ArrayList<>(n);

        for (int i = 0; i < n; i++)
            graph.add(new ArrayList<>());   // 先为 graph 填充 n 个 list（这样后面就可以一次给两个 list 赋值，不需要遍历 n × n 次）

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {  // j 从 i+1 开始，不重复的遍历 wordList 中所有的两两组合
                if (isSimilar(worList.get(i), worList.get(j))) {
                    graph.get(i).add(j);       // 找到相邻单词后一次给两个 list 赋值
                    graph.get(j).add(i);
                }
            }
        }

        return graph;
    }

    private static Map<Integer, Integer> bfs(List<List<Integer>> graph, int beginIndex, List<String> wordList) {
        Map<Integer, Integer> steps = new HashMap<>();  // 存储 { wordIndex: beginWord 到该 word 的最小步数 }
        steps.put(beginIndex, 1);
        Queue<Integer> q = new LinkedList<>();
        q.offer(beginIndex);

        while (!q.isEmpty()) {
            int i = q.poll();
            for (int adj : graph.get(i)) {      // 遍历所有相邻节点的顶点的 index
                if (!steps.containsKey(adj)) {  // 若 steps 中已有 adj，说明之前已找到了更短的路径 ∴ 不能再覆盖
                    steps.put(adj, steps.get(i) + 1);
                    q.offer(adj);
                }
            }
        }

        return steps;
    }

    private static void dfs(List<List<Integer>> graph, int i, int endIndex, List<String> wordList,  // 每层递归找到最短路径上的一个顶点，放入 path
                            Map<Integer, Integer> steps, List<Integer> path, List<List<String>> res) {
        if (!path.isEmpty() && path.get(path.size() - 1) == endIndex) {  // 到达 endWord 时递归到底
            res.add(getWords(path, wordList));         // 到达 endWord 时最短路径 path 被填充完整 ∴ 要为其中的每个 index 找到对应的 word，形成一个解
            return;
        }
        for (int adj : graph.get(i)) {                 // 遍历所有相邻顶点的 index
            if (steps.get(adj) == steps.get(i) + 1) {  // 检查索引为 adj 的顶点是否是最短路径上的下一个顶点（保证 path 是最短路径；可能有多个最短路径）
                path.add(adj);
                dfs(graph, adj, endIndex, wordList, steps, path, res);  // 从 adj 开始继续往深处搜索
                path.remove(path.size() - 1);          // 返回上层递归之前将 adj 移除，恢复原来的状态（回溯的标志性操作）
            }
        }
    }

    private static List<String> getWords(List<Integer> path, List<String> wordList) {
        List<String> wordPath = new ArrayList<>();
        for (int i : path)
            wordPath.add(wordList.get(i));
        return wordPath;
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
     * 解法2：Bi-directional BFS + DFS
     * - 思路：与解法1类似，总体思路都是使用发散性的 BFS 生成一个对 DFS 友好的辅助数据结构，再使用纵深性的 DFS 找到所有最短路径。
     * - 实现：与解法1的不同点：
     *     1. 辅助数据结构是一棵用 Map 表达的树，其 key 记录 BFS 过程中在走过所有最短路径之前访问过的所有顶点，value 记录每个
     *        顶点的所有相邻顶点（之所以能只记录走过所有最短路径之前的顶点，是因为充分利用了 BFS 的性质，SEE👇的💎）；
     *     2. BFS 过程采用双向 BFS（类似 L127 解法3），若用单向 BFS 则逻辑简单些，但效率低；
     *     3. 不为 BFS 事先构建 graph，而是在 BFS 过程中现为每个顶点搜索相邻顶点。
     * - 💎 总结：
     *     1. BFS 的最大特点是从起点扩散性的向外逐层访问顶点 ∴ 最先到达终点的一定是最短路径，若存在多条最短路径，则它们都会在
     *        同一轮遍历（对最外圈顶点的遍历）中到达终点。
     *     2. 该解法中的辅助数据结构使用 Map 表达树 —— 是一个很经典且常用的技巧。
     * - 扩展：若该题目只求任意一条最短路径，则可以对 biDirBfs、dfs 方法进行改造，在找到第一条最短路径后就停止即可。
     * */
    public static List<List<String>> findLadders2(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> res = new ArrayList<>();
        if (!wordList.contains(endWord)) return res;

        Set<String> unvisited = new HashSet<>(wordList);
        Set<String> beginSet = new HashSet<>();
        Set<String> endSet = new HashSet<>();
        beginSet.add(beginWord);
        endSet.add(endWord);
        HashMap<String, List<String>> adjMap = new HashMap<>();  // 用 Map 表达的树，记录 { 顶点: [所有相邻顶点] }
        biDirBfs(beginSet, endSet, unvisited, adjMap, true);     // 通过双向 BFS 搜索各个顶点的相邻顶点，并放入 adjMap

        List<String> path = new ArrayList<>();                   // 回溯中待填充的路径
        path.add(beginWord);
        dfs(beginWord, endWord, adjMap, path, res);              // 基于 adjMap 进行回溯搜索，生成最短路径，并放入 res

        return res;
    }

    private static void biDirBfs(Set<String> beginSet, Set<String> endSet, Set<String> unvisited,
                                 HashMap<String, List<String>> adjMap, boolean isForward) {
        unvisited.removeAll(beginSet);
        unvisited.removeAll(endSet);
        boolean hasMet = false;
        Set<String> neighbours = new HashSet<>();

        for (String word : beginSet) {
            for (int i = 0; i < word.length(); i++) {
                char[] chars = word.toCharArray();     // 这种替换字符的方式比 L127 解法2、3更简便
                for (char c = 'a'; c <= 'z'; c++) {
                    if (c == chars[i]) continue;
                    chars[i] = c;
                    String tWord = new String(chars);

                    String key = isForward ? word : tWord;  // ∵ adjMap 中的 path 是从起点到终点 ∴ 在反向 BFS 时需要与正向 BFS 统一顺序
                    String adj = isForward ? tWord : word;
                    List<String> adjWords = adjMap.getOrDefault(key, new ArrayList<>());

                    if (endSet.contains(tWord)) {  // 若正反向 BFS 相遇，找到一条最短路径
                        hasMet = true;             // ∵ 要继续遍历完这一轮的所有顶点，以找到所有最短路径 ∴ 这里只设置标志位，不 return
                        adjWords.add(adj);
                        adjMap.put(key, adjWords);
                    }
                    if (!hasMet && unvisited.contains(tWord)) {  // TODO: 这里还没有完全明白
                        neighbours.add(tWord);
                        adjWords.add(adj);
                        adjMap.put(key, adjWords);
                    }
                }
            }
        }

        if (hasMet || neighbours.isEmpty()) return;  // 在找到所有最短路径后就不再继续递归（此时 adjMap 中已经包含了所有最短路径上的顶点）

        if (beginSet.size() > endSet.size())         // 双向 BFS 的性能优化技巧：选顶点较少的一边进行下一轮递归
            biDirBfs(endSet, neighbours, unvisited, adjMap, !isForward);
        else
            biDirBfs(neighbours, endSet, unvisited, adjMap, isForward);
    }

    private static void dfs(String currWord, String endWord, HashMap<String, List<String>> pathMap,  // 标准的 DFS（回溯）实现
                            List<String> path, List<List<String>> res) {
        if (currWord.equals(endWord)) {
            res.add(new ArrayList<>(path));
            return;
        }
        if (!pathMap.containsKey(currWord)) return;
        for (String adj : pathMap.get(currWord)) {  // 从 pathMap 中找到 currWord 的所有相邻顶点，为他们递归地进行 DFS
            path.add(adj);
            dfs(adj, endWord, pathMap, path, res);
            path.remove(path.size() - 1);           // 返回上层递归之前将 adj 移除，恢复原来的状态，从而在有分叉的顶点上可以改变方向继续搜索
        }
    }

    public static void main(String[] args) {
        List<String> wordList = new ArrayList<>(Arrays.asList("hot", "dot", "dog", "lot", "log", "cog"));
        log(findLadders2("hit", "cog", wordList));
        // expects [["hit","hot","dot","dog","cog"], ["hit","hot","lot","log","cog"]]

        List<String> wordList2 = new ArrayList<>(Arrays.asList("hot", "cog", "dot", "dog", "hit", "lot", "log"));
        log(findLadders2("hit", "cog", wordList2));
        // expects [["hit","hot","dot","dog","cog"], ["hit","hot","lot","log","cog"]]

        List<String> wordList3 = new ArrayList<>(Arrays.asList("a", "b", "c"));
        log(findLadders2("a", "c", wordList3));
        // expects [["a","c"]]

        List<String> wordList4 = new ArrayList<>(Arrays.asList("ted", "tex", "red", "tax", "tad", "den", "rex", "pee"));
        log(findLadders2("red", "tax", wordList4));
        // expects [["red","ted","tad","tax"], [red, ted, tad, tax], [red, rex, tex, tax]]

        List<String> wordList5 = new ArrayList<>(Arrays.asList("hot", "dot", "dog", "lot", "log"));
        log(findLadders2("hit", "cog", wordList5));
        // expects []

        List<String> wordList6 = new ArrayList<>(Arrays.asList("hot", "dog"));
        log(findLadders2("hot", "dog", wordList6));
        // expects []

        List<String> wordList7 = new ArrayList<>(Arrays.asList("lest", "leet", "lose", "code", "lode", "robe", "lost"));
        log(findLadders2("leet", "code", wordList7));
        // expects [["leet","lest","lost","lose","lode","code"]]

        List<String> wordList8 = new ArrayList<>(Arrays.asList("miss", "dusk", "kiss", "musk", "tusk", "diss", "disk", "sang", "ties", "muss"));
        log(findLadders2("kiss", "tusk", wordList8));
        // expects [[kiss, miss, muss, musk, tusk], [kiss, diss, disk, dusk, tusk]]
    }
}
