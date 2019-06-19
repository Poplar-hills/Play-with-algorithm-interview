package StackAndQueue.BFSAndGraphShortestPath;

/*
* Word Ladder II
*
* - 题目与 L127 基本一致，区别在于返回结果得是所有的最短路径。
* */

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.*;

import static Utils.Helpers.log;

public class L126_WordLadderII {
    public static List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> res = new ArrayList<>();
        if (!wordList.contains(endWord)) return res;

        int n = wordList.size();
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
            for (int j = i + 1; j < n; j++) {  // 让 j = i+1 可以不重复地遍历 i、j 的组合（i=1,j=0 和 j=0,j=1 是重复的组合）
                graph.get(i).add(1);
//                if (isSimilar(wordList.get(i), wordList.get(j))) {
//                    graph.get(i).add(j);
//                    graph.get(j).add(i);
//                }
            }
        }


//        biDirectionalBfs(graph, beginWord, endWord, wordList, res);
        return res;
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

    public static void main(String[] args) {
        List<String> wordList = Arrays.asList("hot", "dot", "dog", "lot", "log", "cog");
        log(findLadders("hit", "cog", wordList));
        // expects [["hit","hot","dot","dog","cog"], ["hit","hot","lot","log","cog"]]

        List<String> wordList2 = Arrays.asList("a", "b", "c");
        log(findLadders("a", "c", wordList2));
        // expects [["a","c"]]

        List<String> wordList3 = Arrays.asList("ted", "tex", "red", "tax", "tad", "den", "rex", "pee");
        log(findLadders("red", "tax", wordList3));
        // expects [["red","ted","tad","tax"]]

        List<String> wordList4 = Arrays.asList("hot", "dot", "dog", "lot", "log");
        log(findLadders("hit", "cog", wordList4));
        // expects []

        List<String> wordList5 = Arrays.asList("hot", "dog");
        log(findLadders("hot", "dog", wordList5));
        // expects []

        List<String> wordList6 = Arrays.asList("lest", "leet", "lose", "code", "lode", "robe", "lost");
        log(findLadders("leet", "code", wordList6));
        // expects [["leet","lest","lost","lose","lode","code"]]

        List<String> wordList7 = Arrays.asList("miss", "dusk", "kiss", "musk", "tusk", "diss", "disk", "sang", "ties", "muss");
        log(findLadders("kiss", "tusk", wordList7));
        // expects [["kiss","miss","muss","musk","tusk"]]
    }
}
