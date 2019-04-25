package LookUp;

import static Utils.Helpers.log;

/*
* Sort Characters By Frequency
*
* - 按照字母出现频率的倒序重组整个字符串。
* */

public class L451_SortCharactersByFrequency {
    public static String frequencySort(String s) {
        
    }

    public static void main(String[] args) {
        log(frequencySort("tree"));     // expects "eert" or "eetr"
        log(frequencySort("cccaaa"));   // expects "cccaaa" or "aaaccc"
        log(frequencySort("Aabb"));     // expects "bbAa" or "bbaA"
    }
}