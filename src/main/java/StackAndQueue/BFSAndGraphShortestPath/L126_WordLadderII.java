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

        List<String> wordList2 = Arrays.asList("hot", "dot", "dog", "lot", "log");
        log(findLadders("hit", "cog", wordList2));
        // expects []
    }
}
