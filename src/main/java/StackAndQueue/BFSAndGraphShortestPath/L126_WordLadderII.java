package StackAndQueue.BFSAndGraphShortestPath;

/*
* Word Ladder II
*
* - 题目与 L127 基本一致，区别在于返回结果得是所有的最短路径。
* */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Utils.Helpers.log;

public class L126_WordLadderII {
    public static List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        return new ArrayList<>();
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
