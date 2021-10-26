package StackAndQueue.S4_BFSAndGraphShortestPath;

import java.util.*;
import java.util.stream.Collectors;

import static Utils.Helpers.log;

/*
 * Word Ladder II
 *
 * - 题目与 L127_WordLadder 一致，区别在于要返回所有的最短路径。
 *
 * - 👉 该题是非常有助于理解 BFS、DFS 的各自优势和局限性，以及在如何各尽所能相互配合。
 * */

public class L126_WordLadderII {
    /*
     * 解法1：BFS
     * - 初步分析："求最短路径"可使用 BFS 解决，而"求所有最短路径"则需要在通过 BFS 遍历找到最短路径后，继续遍历完所有路径，
     *   这样才能找到所有最短路径。
     * - 思路：与“找出图上任意两点之间的所有路径”的思路一致（SEE: Play-with-algorithms/Graph/Path 中的 allPaths 方法），
     *   即通过 BFS 遍历从 beginWord 到 endWord 之间的所有路径 ∵ 第一次找到的路径一定是最短的 ∴ 在找到第一个路径之后再找到
     *   的路径要么不是最短，要么跟第一条一样长 ∴ 只需根据每条路径的长度进行判断，若长度超过最短路径长度，则直接抛弃即可，这样
     *   最后拿到的所有路径就都是最短路径。
     * - 时间复杂度 O(n^n)，空间复杂度 O(n)。
     * */
    public static List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> res = new ArrayList<>();
        if (!wordList.contains(endWord)) return res;

        Set<String> visited = new HashSet<>();
        Queue<List<String>> q = new LinkedList<>();  // Queue 中保存的是一条路径
        List<String> initialPath = new ArrayList<>();

        initialPath.add(beginWord);
        q.offer(initialPath);
        Integer minStep = null;  // 记录最短路径的长度，用于识别超过该长度的路径

        while (!q.isEmpty()) {   // 遍历所有路径（若 branching factor 为 n，即每个节点有 n 个分支，则复杂度为 O(n^n)）
            List<String> path = q.poll();
            if (minStep != null && path.size() == minStep) continue;  // 若 q 中拿出来的 path 长度已经等于 minStep 则抛弃掉
            String lastWord = path.get(path.size() - 1);  // 从 path 中拿出最后一个 word，寻找其相邻顶点
            visited.add(lastWord);

            for (String w : wordList) {
                if (!visited.contains(w) && isSimilar(w, lastWord)) {
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
     * 解法2：构建邻接表 + BFS + DFS
     * - 💎 思路：解法1的思路是使用 BFS 遍历两点间的所有路径，并在过程中比较长度，从而获得所有最短路径，但这种方式在
     *   branching factor 较大时性能会显著下降。而另一种效率更高的思路是结合 BFS 和 DFS —— 先借助 BFS 的扩散性，快速
     *   找到从起点到每个顶点的最小步数，再借助 DFS 的纵深性，沿着最小步数形成的最短路径以回溯法将所有最短路径输出出来。
     * - 实现：
     *   1. 在 BFS 之前要生成 graph，本解法中采用无向邻接表（Adjacency List），若用邻接矩阵则会超时。
     *   2. 通过 BFS 生成的从起点到各顶点的最小步需要一个数据结构来承载，可以是类似 L127_WordLadderII 解法5中的 steps
     *      数组，也可以是 Map。本解法中使用 Map 以便于检索。
     *   3. DFS 过程：从 beginWord 出发，借助 stepMap 查找哪个或哪几个相邻 word 是最短路径上的下一个顶点，如此重复直到
     *      到达 endWord，并记录下沿途的顶点即可获得最短路径。
     * - 💎 总结：
     *   1. 图上两点之间的最短路径，同时也是从起点到该路径上各顶点的最短路径，证明：
     *            0 --- 1 --- 2 --- 3     起点: 4，终点: 3            0  1  2  3  4  5  6  7
     *            |   /______/    / |     顶点4到其他各顶点的最短步数：[2, 2, 2, 3, 1, 2, 3, 4]
     *            | /           /   |     可见，从4到3的最短路径：
     *            4 --- 5 --- 6 --- 7       - 4->2->3：同时也是从4到2的最短路径
     *            |___________|             - 4->6->3：同时也是从4到6的最短路径
     *      这是本题的关键点，只有这点成立，"实现"中的第3点才能成立。
     *   2. 所有 DFS 的实现都是基于回溯法的，而说起"回溯"，指的也就是 DFS，这两个词是 interchangeable 的。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * - 👉 注意：一般来说 DFS、BFS 的时间复杂度都是 O(V+E)，但具体要看数据结构，对于邻接矩阵是 O(V^2)，对于邻接表是 O(V+E)。
     * */
    public static List<List<String>> findLadders2(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> res = new ArrayList<>();
        if (!wordList.contains(endWord)) return res;
        if (!wordList.contains(beginWord)) wordList.add(beginWord);

        // Step 1: 构建无向邻接表，O(n^2)
        List<List<Integer>> graph = buildGraph(wordList);

        // Step 2: 通过 BFS 生成 stepMap，O(n)
        int beginIndex = wordList.indexOf(beginWord);
        int endIndex = wordList.indexOf(endWord);
        Map<Integer, Integer> stepMap = bfs(graph, beginIndex, wordList);

        // Step 3: 通过 DFS 搜索并填充 path，再转换成 word path 后放入 res
        List<Integer> path = new ArrayList<>();  // 回溯中待填充的路径（存储最短路径上每个顶点的 index）
        path.add(beginIndex);
        dfs(graph, beginIndex, endIndex, wordList, stepMap, path, res);

        return res;
    }

    private static List<List<Integer>> buildGraph(List<String> worList) {  // 邻接表的结构是个 List<List<Integer>>
        int n = worList.size();
        List<List<Integer>> graph = new ArrayList<>(n);

        for (int i = 0; i < n; i++)
            graph.add(new ArrayList<>());  // 先为 graph 填充 n 个 list（后面就可以一次给两个 list 赋值，无需遍历 n × n 次）

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
        Map<Integer, Integer> stepMap = new HashMap<>();  // Map<wordIndex, beginWord 到该 word 的最小步数>
        stepMap.put(beginIndex, 1);
        Queue<Integer> q = new LinkedList<>();  // Queue<wordIndex>
        q.offer(beginIndex);

        while (!q.isEmpty()) {
            int i = q.poll();
            for (int adj : graph.get(i)) {  // 基于 graph 遍历所有相邻顶点的 index
                if (!stepMap.containsKey(adj)) {  // 若 stepMap 中已有 adj，说明之前已找到了更短的路径 ∴ 不能再覆盖
                    stepMap.put(adj, stepMap.get(i) + 1);
                    q.offer(adj);
                }
            }
        }

        return stepMap;
    }

    private static void dfs(List<List<Integer>> graph, int i, int endIndex, List<String> wordList,
                            Map<Integer, Integer> stepMap, List<Integer> path, List<List<String>> res) {
        if (!path.isEmpty() && path.get(path.size() - 1) == endIndex) {  // 到达 endWord 时最短路径 path 被填充完整
            res.add(getWords(path, wordList));             // 根据 path 中的 indexes 找到对应 words
            return;
        }
        for (int adj : graph.get(i)) {                     // 遍历所有相邻顶点的 index，找到最短路径上的下一个顶点，放入 path
            if (stepMap.get(adj) == stepMap.get(i) + 1) {  // 关键点，检查索引为 adj 的顶点是否是最短路径上的下一个顶点
                path.add(adj);
                dfs(graph, adj, endIndex, wordList, stepMap, path, res);
                path.remove(path.size() - 1);       // 返回上层前先恢复状态，以继续寻找其他最短路径
            }
        }
    }

    private static List<String> getWords(List<Integer> path, List<String> wordList) {
        return path.stream().map(wordList::get).collect(Collectors.toList());
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
     * 解法3：Bi-directional BFS + DFS
     * - 思路：与解法2类似，总体思路都是使用发散性的 BFS 生成一个对 DFS 友好的辅助数据结构，再使用纵深性的 DFS 找到所有最短路径。
     * - 实现：与解法2的不同点：
     *     1. 辅助数据结构是一棵用 Map 表达的树，其 key 记录 BFS 过程中在走过所有最短路径之前访问过的所有顶点，value 记录每个
     *        顶点的所有相邻顶点（之所以能只记录走过所有最短路径之前的顶点，是因为充分利用了 BFS 的性质，SEE👇的💎）；
     *     2. BFS 过程采用双向 BFS（类似 L127_WordLadder 解法3），若用单向 BFS 则逻辑简单些，但效率低；
     *     3. 不为 BFS 事先构建 graph，而是在 BFS 过程中现为每个顶点搜索相邻顶点。
     * - 💎 总结：
     *     1. BFS 的最大特点是从起点扩散性的向外逐层访问顶点 ∴ 最先到达终点的一定是最短路径，若存在多条最短路径，则它们都会在
     *        同一轮遍历（对最外圈顶点的遍历）中到达终点。
     *     3. 该解法中的辅助数据结构使用 Map 表达树 —— 是一个很经典且常用的技巧。
     * - 扩展：若该题目只求任意一条最短路径，则可以对 biDirBfs、dfs 方法进行改造，在找到第一条最短路径后就停止即可。
     * */
    public static List<List<String>> findLadders3(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> res = new ArrayList<>();
        if (!wordList.contains(endWord)) return res;

        Set<String> unvisited = new HashSet<>(wordList);
        Set<String> beginSet = new HashSet<>();
        Set<String> endSet = new HashSet<>();
        beginSet.add(beginWord);
        endSet.add(endWord);
        HashMap<String, List<String>> adjMap = new HashMap<>();  // 用 Map 表达的树，记录 Map<顶点, List<所有相邻顶点>>
        biDirBfs(beginSet, endSet, unvisited, adjMap, true);  // 通过双向 BFS 搜索各个顶点的相邻顶点，并放入 adjMap

        List<String> path = new ArrayList<>();       // 回溯中待填充的路径
        path.add(beginWord);
        dfs(beginWord, endWord, adjMap, path, res);  // 基于 adjMap 进行回溯搜索，生成最短路径，并放入 res

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
