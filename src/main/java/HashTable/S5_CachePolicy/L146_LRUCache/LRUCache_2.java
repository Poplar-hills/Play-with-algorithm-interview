package HashTable.S5_CachePolicy.L146_LRUCache;

import java.util.LinkedHashMap;
import java.util.Map;

import static Utils.Helpers.log;

/*
 * 解法2：LinkedHashMap
 * - 思路：
 * */

public class LRUCache_2 extends LinkedHashMap<Integer, Integer> {  // 直接扩展 LinkedHashMap（但这样做封装性不是很好）
    private final int capacity;

    public LRUCache_2(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
        return size() > capacity;
    }

    public int get(int key) {
        return getOrDefault(key, -1);
    }

    public void set(int key, int value) {
        put(key, value);
    }

    public static void main(String[] args) {
        LRUCache_2 lRUCache = new LRUCache_2(2);
        lRUCache.put(1, 1);    // cache is {1:1}
        lRUCache.put(2, 2);    // cache is {1:1, 2:2}
        log(lRUCache.get(1));  // return 1
        lRUCache.put(3, 3);    // LRU key was 2, evicts key 2, cache is {1:1, 3:3}
        log(lRUCache.get(2));  // returns -1 (not found)
        lRUCache.put(4, 4);    // LRU key was 1, evicts key 1, cache is {4:4, 3:3}
        log(lRUCache.get(1));  // return -1 (not found)
        log(lRUCache.get(3));  // return 3
        log(lRUCache.get(4));  // return 4
    }
}
