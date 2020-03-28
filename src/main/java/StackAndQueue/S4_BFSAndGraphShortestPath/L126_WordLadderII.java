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
     * 解法1：构建邻接表 + BFS + 回溯（TODO: 学完回溯法后再来 review）
     * - 思路：在 L127 解法5中，
     *
     * ∵ 要找到所有最短路径 ∴ 不能再在 BFS 中记录步数直接返回，而是：
     *   1. 要通过 BFS 计算起点到每个顶点的最少步数，保存在 steps 中；
     *   2. 再根据 steps 进行回溯查找，找到所有最短路径。回溯的逻辑本质上是 DFS：
     *      a. 从 beginWord 出发，根据 steps 中的信息不断查找最短路径上的下一个相邻顶点，直到到达 endWord，并一路上记录下该路径所经顶点，得到路径。
     *      b. 在查找最短路径上的下一个相邻顶点时，若遇到路径分叉（存在多条最短路径），则每条都要尝试一遍。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> res = new ArrayList<>();
        if (!wordList.contains(endWord)) return res;
        if (!wordList.contains(beginWord)) wordList.add(beginWord);

        List<List<Integer>> graph = buildAdjacencyList(wordList);  // 构建无向邻接表（若使用邻接矩阵则会超时）
        int beginIndex = wordList.indexOf(beginWord);
        int endIndex = wordList.indexOf(endWord);

        Map<Integer, Integer> steps = new HashMap<>();  // 保存 { 顶点: 起点到该顶点的最少步数 }
        bfs(graph, beginIndex, steps);                  // 通过 BFS 来 populate steps

        List<Integer> path = new ArrayList<>();    // 用于回溯，存储最短路径上每个顶点的 index
        path.add(beginIndex);
        dfsBackTrace(graph, beginIndex, endIndex, wordList, steps, path, res);  // 根据 steps 回溯，找到所有最短路径

        return res;
    }

    private static List<List<Integer>> buildAdjacencyList(List<String> wordList) {
        List<List<Integer>> graph = new ArrayList<>();
        int n = wordList.size();

        for (int i = 0; i < n; i++)
            graph.add(new ArrayList<>());  // 先为 graph 填充 n 个 list（这样后面就可以一次给两个 list 赋值，不需要遍历 n × n 次）

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {  // j 从 i+1 开始，不重复的遍历 wordList 中所有的两两组合
                if (isSimilar(wordList.get(i), wordList.get(j))) {
                    graph.get(i).add(j);       // 找到相邻单词后一次给两个 list 赋值
                    graph.get(j).add(i);
                }
            }
        }

        return graph;
    }

    private static void bfs(List<List<Integer>> graph, int beginIndex, Map<Integer, Integer> steps) {
        Queue<Integer> q = new LinkedList<>();
        q.offer(beginIndex);
        steps.put(beginIndex, 0);

        while (!q.isEmpty()) {
            int currIndex = q.poll();
            for (int adjIndex : graph.get(currIndex)) {  // 遍历所有相邻节点的顶点的 index
                if (!steps.containsKey(adjIndex)) {  // 若 adjIndex 存在于 steps 中，说明之前已找到过更短的到达路径 ∴ 不能再覆盖
                    steps.put(adjIndex, steps.get(currIndex) + 1);
                    q.offer(adjIndex);
                }
            }
        }
    }

    private static void dfsBackTrace(List<List<Integer>> graph, int currIndex, int endIndex, List<String> wordList, Map<Integer, Integer> steps, List<Integer> indexPath, List<List<String>> res) {
        if (!indexPath.isEmpty() && indexPath.get(indexPath.size() - 1) == endIndex) {  // 检查是否到达 endWord
            res.add(getPath(indexPath, wordList));  // 若到达则说明 indexPath 中的索引组成了一条最短路径，将对应的 word path 添加到 res 中
            return;
        }
        for (int adjIndex : graph.get(currIndex)) {                 // 遍历所有邻居顶点的 index
            if (steps.get(adjIndex) == steps.get(currIndex) + 1) {  // 检查 adjIndex 所指顶点是否是最短路径上的下一个顶点
                indexPath.add(adjIndex);
                dfsBackTrace(graph, adjIndex, endIndex, wordList, steps, indexPath, res);  // 递归查找下一个 adjIndex
                indexPath.remove(indexPath.size() - 1);      // 递归结束后将 adjIndex 移除，放入下一个再继续查找
            }
        }
    }

    private static List<String> getPath(List<Integer> tres, List<String> wordList) {
        List<String> path = new ArrayList<>();
        for (int index : tres)
            path.add(wordList.get(index));
        return path;
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
