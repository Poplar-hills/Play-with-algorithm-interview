package HashTable.S5_CachePolicy.L460_LFUCache;

import java.util.*;

import static Utils.Helpers.log;

/*
 * 解法2：TreeMap + HashMap
 * - 💎 思路：类似解法1"思路"中的第一版：
 *   1. 使用两个相反的数据结构：
 *      - Map<key, CacheVal<value, count, time>>: 存储数据；
 *      - TreeMap<CacheVal<val, count, time>, key>：实现"先按 LFU 逐出，当 count 相同时再按 LRU 逐出"（借助 TreeMap
 *        的有序性，先比较 count，当 count 相同时再比较 timestamp）。
 *   2. 在淘汰数据时，解法1是通过手动维护的 minCount 来快速找到 LFU、LRU 数据。而本解法中由于 TreeMap 的比较器中已经揉进了对
 *      timestamp 的比较 ∴ 在淘汰数据时直接 remove 比较出来的最"小"数据即可。
 * - 💎 实现：
 *   - 关键点是利用 TreeMap 的排序能力对缓存数据进行多维度排序（先按 count 排，再按 timestamp 排）—— 多维排序是很常用的技巧；
 *   - 同样也可以使用 PriorityQueue 的排序能力来实现。
 * - 💎 时间复杂度：get、put 方法均为 O(log(capacity)) ∴ 无法满足题目要求。三种数据结构的时间复杂度比较：
 *                        add     get-min   remove-min  remove-any
 *      TreeMap：       O(logn)    O(logn)    O(logn)    O(logn)     - get-min 即获取 tree 的最左节点
 *      PriorityQueue： O(logn)     O(1)      O(logn)     O(n)       - get-min 即获取堆顶元素
 *      HashMap：        O(1)       O(n)       O(n)       O(1)       - 在没有辅助数据结构的情况下，需 O(n) 才能找到 min
 * */

public class LFUCache_2 {

    private static class CacheVal {
        int val, count, timestamp;
        public CacheVal(int val, int timestamp, int count) {
            this.val = val;
            this.timestamp = timestamp;
            this.count = count;
        }
    }

    private final int capacity;
    private int timer;  // 全局 timer，每次 get、put 操作都会让其自增，从而为缓存数据提供时间戳作用（用于在 TreeMap 上比较）
    private final Map<Integer, CacheVal> keyMap;  // Map<key, CacheVal<value, count, time>>
    private final TreeMap<CacheVal, Integer> cacheValMap;  // TreeMap<CacheVal<value, count, time>, key>

    public LFUCache_2(int capacity) {
        this.capacity = capacity;
        timer = 0;
        keyMap = new HashMap<>();
        cacheValMap = new TreeMap<>((a, b) -> a.count == b.count  // 自定义 TreeMap 的 key-sort function
                ? a.timestamp - b.timestamp     // 当 count 相同时，再比较 timestamp（从小到大）
                : a.count - b.count);
    }

    public int get(int key) {
        if (!keyMap.containsKey(key)) return -1;

        CacheVal cacheVal = keyMap.get(key);
        CacheVal newCacheVal = new CacheVal(cacheVal.val, timer++, cacheVal.count + 1);
        keyMap.put(key, newCacheVal);  // update the value in keyMap
        cacheValMap.remove(cacheVal);  // update key & value in cacheValMap（要先 remove 旧的再 put 新的）
        cacheValMap.put(newCacheVal, key);

        return cacheVal.val;
    }

    public void put(int key, int value) {
        if (capacity == 0) return;

        if (keyMap.containsKey(key)) {  // if key exists, update the cache
            CacheVal cacheVal = keyMap.get(key);
            CacheVal newCacheVal = new CacheVal(value, timer++, cacheVal.count + 1);
            keyMap.put(key, newCacheVal);
            cacheValMap.remove(cacheVal);
            cacheValMap.put(newCacheVal, key);
        } else {                        // if key doesn't exist, create a new one
            if (cacheValMap.size() == capacity) {
                int expiredKey = cacheValMap.pollFirstEntry().getValue();  // 根据 TreeMap 的 key-sort function 返回第一个 entry（即 TreeMap 的最左叶子节点）
                keyMap.remove(expiredKey);  // evict the LRU
            }
            CacheVal newCacheVal = new CacheVal(value, timer++, 1);
            keyMap.put(key, newCacheVal);
            cacheValMap.put(newCacheVal, key);
        }
    }

    public static void main(String[] args) {
        LFUCache_2 cache = new LFUCache_2(2);
        cache.put(1, 1);    // cache=[1,_], cnt(1)=1
        cache.put(2, 2);    // cache=[2,1], cnt(2)=1, cnt(1)=1
        log(cache.get(1));  // return 1. cache=[1,2], cnt(2)=1, cnt(1)=2
        cache.put(3, 3);    // 2 is the LFU key coz cnt(2)=1 is the smallest, invalidate 2. cache=[3,1], cnt(3)=1, cnt(1)=2
        log(cache.get(2));  // return -1 (not found)
        log(cache.get(3));  // return 3. cache=[3,1], cnt(3)=2, cnt(1)=2
        cache.put(4, 4);    // 1 and 3 have the same cnt, but 1 is LRU, invalidate 1. cache=[4,3], cnt(4)=1, cnt(3)=2
        log(cache.get(1));  // return -1 (not found)
        log(cache.get(3));  // return 3. cache=[3,4], cnt(4)=1, cnt(3)=3
        log(cache.get(4));  // return 4. cache=[3,4], cnt(4)=2, cnt(3)=3
    }

}
