package StackAndQueue.S4_BFSAndGraphShortestPath;

import java.util.*;

import static Utils.Helpers.log;

/*
 * Word Ladder II
 *
 * - 题目与 L127 一致，区别在于要返回所有的最短路径。
 * */

public class L126_WordLadderII {
    /*
     * 超时解：
     * - 思路：与“找出图上任意两点之间的所有路径”的思路一致（SEE: Play-with-algorithms/Graph/Path 中的 allPaths 方法）。
     * - 实现：BFS 过程中，第一次找到的路径一定是最短路径。根据这一规律，可以停止对超过最短路径长度的其他路径的遍历。
     * - 时间复杂度 O(n^n)：虽然有进行优化，但复杂度量级没变 —— 每找到一个相邻顶点都可能多出 n 种可能 ∴ 是 O(n^n)。
     * */
    public static List<List<String>> findLadders0(String beginWord, String endWord, List<String> wordList) {
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
     * 解法1：构建邻接表 + BFS + Backtracking
     * - 思路：L127 解法5中先构建了 graph，然后在 BFS 过程中生成 steps 数组。而 steps 中记录了从起点到达每个顶点的最小步数
     *   ∴ 只要借助 steps 对 graph 进行回溯搜索，并记录下沿途的顶点即可获得所有最短路径。
     * - 实现：
     *   1. 本解法中构建的 graph 是无向邻接表（Adjacency List），若用邻接矩阵则会超时。
     *   2. 为了便于查找，本解法中的 steps 使用 Map 实现。
     *   3. 回溯过程：从 beginWord 出发，借助 steps 中的信息查找哪个（或哪几个）相邻 word 是最短路径上的下一个顶点，如此重复
     *      直到到达 endWord，并记录下沿途的顶点即可获得最短路径。
     * - 👉注意：说起“回溯”，其实就是指 DFS ∵ DFS 是基于回溯的（SEE: https://mp.weixin.qq.com/s/sAutzAzhaGArkl2Ban5guA）
     *   ∴ 本解法其实就是 BFS + DFS。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static List<List<String>> findLadders00(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> res = new ArrayList<>();
        if (!wordList.contains(endWord)) return res;
        if (!wordList.contains(beginWord)) wordList.add(beginWord);

        List<List<Integer>> graph = buildGraph(wordList);                    // 先构建无向邻接表

        int beginIndex = wordList.indexOf(beginWord);
        int endIndex = wordList.indexOf(endWord);
        Map<Integer, Integer> steps = bfs(graph, beginIndex, wordList);      // 通过 BFS 来填充 steps map

        List<Integer> path = new ArrayList<>(beginIndex);                    // 待填充的最短路径（存储最短路径上每个顶点的 index）
        backTrack(graph, beginIndex, endIndex, wordList, steps, path, res);  // 通过回溯搜索填充 path，再转换成 word path 后放入 res

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

    private static void backTrack(List<List<Integer>> graph, int i, int endIndex, List<String> wordList,
                            Map<Integer, Integer> steps, List<Integer> path, List<List<String>> res) {  // 每层递归找到最短路径上的一个顶点，放入 path
        if (!path.isEmpty() && path.get(path.size() - 1) == endIndex) {  // 到达 endWord 时递归到底
            res.add(getWords(path, wordList));         // 到达 endWord 时最短路径 path 被填充完整 ∴ 要为其中的每个 index 找到对应的 word，形成一个解
            return;
        }
        for (int adj : graph.get(i)) {                 // 遍历所有相邻顶点的 index
            if (steps.get(adj) == steps.get(i) + 1) {  // 检查索引为 adj 的顶点是否是最短路径上的下一个顶点（保证 path 是最短路径；可能有多个最短路径）
                path.add(adj);
                backTrack(graph, adj, endIndex, wordList, steps, path, res);  // 从 adj 开始继续往深处搜索
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
     * 解法2：更简洁更高效的解法（TODO: 没有完全看懂）
     * */
    public static List<List<String>> findLadders2(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> res = new ArrayList<>();
        if (!wordList.contains(endWord)) return res;

        HashMap<String, List<String>> nextMap = new HashMap<>();
        Set<String> beginSet = new HashSet<>();
        Set<String> endSet = new HashSet<>();
        Set<String> wordSet = new HashSet<>(wordList);
        beginSet.add(beginWord);
        endSet.add(endWord);
        bfs(beginSet, endSet, wordSet, nextMap, true);  // 通过 bfs 计算各个节点的相邻节点，并放入 nextMap

        List<String> currList = new ArrayList<>();
        currList.add(beginWord);
        dfs(beginWord, endWord, nextMap, currList, res);

        return res;
    }

    private static void bfs(Set<String> beginSet, Set<String> endSet, Set<String> wordList, HashMap<String, List<String>> nextMap, boolean isForwardSearch) {
        wordList.removeAll(beginSet);
        wordList.removeAll(endSet);
        boolean connected = false;
        Set<String> neighbours = new HashSet<>();

        for (String word : beginSet) {
            for (int i = 0, l = word.length(); i < l; i++) {
                StringBuilder transformed = new StringBuilder(word);
                for (char c = 'a'; c <= 'z'; c++) {
                    if (c == word.charAt(i)) continue;
                    transformed.setCharAt(i, c);
                    String tWord = transformed.toString();
                    if (endSet.contains(tWord) || (!connected && wordList.contains(tWord))) {
                        if (endSet.contains(tWord)) connected = true;
                        else neighbours.add(tWord);

                        String nextWord = isForwardSearch ? tWord : word;
                        String currWord = isForwardSearch ? word : tWord;
                        List<String> nextWords = nextMap.containsKey(currWord) ? nextMap.get(currWord) : new ArrayList<>();
                        nextWords.add(nextWord);
                        nextMap.put(currWord, nextWords);
                    }
                }
            }
        }

        if (!connected && !neighbours.isEmpty()) {  // 若已经找到最短路径则不再继续递归
            if (beginSet.size() > endSet.size())
                bfs(endSet, neighbours, wordList, nextMap, !isForwardSearch);
            else
                bfs(neighbours, endSet, wordList, nextMap, isForwardSearch);
        }
    }

    private static void dfs(String currWord, String endWord, HashMap<String, List<String>> nextMap, List<String> currList, List<List<String>> res) {
        if (currWord.equals(endWord)) {          // 递归到底的条件是到达 endWord
            res.add(new ArrayList<>(currList));  // 到达 endWord 后将该最短路径以复制的方式 add 到 res 里（new ArrayList(currList) 就是复制 currList）
            return;
        }
        if (!nextMap.containsKey(currWord)) return;
        List<String> nextWords = nextMap.get(currWord);

        for (String next : nextWords) {  // 对每个顶点的对每个分支路径进行 DFS，找出该路径上的所有顶点并放入 currList，再切换到下一个分支上继续
            currList.add(next);
            dfs(next, endWord, nextMap, currList, res);
            currList.remove(currList.size() - 1);  // 递归到底后，在每次从下层顶点返回上层顶点之前都移除 currList 的最后一个元素，从而在有分叉的路径上可以改变方向进行检索
        }
    }

    public static void main(String[] args) {
        List<String> wordList = new ArrayList<>(Arrays.asList("hot", "dot", "dog", "lot", "log", "cog"));
        log(findLadders("hit", "cog", wordList));
        // expects [["hit","hot","dot","dog","cog"], ["hit","hot","lot","log","cog"]]

        List<String> wordList2 = new ArrayList<>(Arrays.asList("hot", "cog", "dot", "dog", "hit", "lot", "log"));
        log(findLadders("hit", "cog", wordList2));
        // expects [["hit","hot","dot","dog","cog"], ["hit","hot","lot","log","cog"]]

        List<String> wordList3 = new ArrayList<>(Arrays.asList("a", "b", "c"));
        log(findLadders("a", "c", wordList3));
        // expects [["a","c"]]

        List<String> wordList4 = new ArrayList<>(Arrays.asList("ted", "tex", "red", "tax", "tad", "den", "rex", "pee"));
        log(findLadders("red", "tax", wordList4));
        // expects [["red","ted","tad","tax"], [red, ted, tad, tax], [red, rex, tex, tax]]

        List<String> wordList5 = new ArrayList<>(Arrays.asList("hot", "dot", "dog", "lot", "log"));
        log(findLadders("hit", "cog", wordList5));
        // expects []

        List<String> wordList6 = new ArrayList<>(Arrays.asList("hot", "dog"));
        log(findLadders("hot", "dog", wordList6));
        // expects []

        List<String> wordList7 = new ArrayList<>(Arrays.asList("lest", "leet", "lose", "code", "lode", "robe", "lost"));
        log(findLadders("leet", "code", wordList7));
        // expects [["leet","lest","lost","lose","lode","code"]]

        List<String> wordList8 = new ArrayList<>(Arrays.asList("miss", "dusk", "kiss", "musk", "tusk", "diss", "disk", "sang", "ties", "muss"));
        log(findLadders("kiss", "tusk", wordList8));
        // expects [[kiss, miss, muss, musk, tusk], [kiss, diss, disk, dusk, tusk]]
    }
}
