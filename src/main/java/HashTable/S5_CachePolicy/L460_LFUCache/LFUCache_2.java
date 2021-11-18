package HashTable.S5_CachePolicy.L460_LFUCache;

import java.util.*;

import static Utils.Helpers.log;

/*
 * 解法2：TreeMap + HashMap
 * - 思路：与解法1的区别：
 *   1. 解法1使用 Map<count, LinkedHashSet<key>> 记录 count -> keys 的映射，在 count 相同情况下由 LinkedHashSet 保存
 *      数据的最近访问顺序；而本解法中使用 TreeMap<CacheInfo<val, count, time>, key>，并通过定制 TreeMap 的比较器来对缓存
 *      数据进行排序（先比较 count，当 count 相同时比较 timestamp）。
 *   2. 在淘汰数据时，解法1中通过维护的 minCount 来快速找到 LFU、LRU 数据；而本解法中由于 TreeMap 的比较器中已经揉进了对
 *      timestamp 的比较 ∴ 在淘汰数据时直接 remove 比较出来的最"小"数据即可。
 * - 💎 实现：本解法中使用 TreeMap 是因为其自定义比较排序能力 ∴ 也可以使用同样具备该特性的 PriorityQueue 实现。
 *                         add      get-min    remove-min   remove-any
 *      TreeMap：        O(logn)     O(logn)     O(logn)     O(logn)
 *      PriorityQueue：  O(logn)      O(1)       O(logn)      O(n)
 *      HashMap：         O(1)        O(1)        O(1)        O(1)
 * - 时间复杂度：get、put 方法均为 O(log(capacity))。
 * */

public class LFUCache_2 {

    private static class CacheInfo {
        int value, count, time;
        public CacheInfo(int value, int time, int count) {
            this.value = value;
            this.time = time;
            this.count = count;
        }
    }

    private final int capacity;
    private int time;  // 全局 timestamp，每次 get、put 操作都会让其自增，从而为缓存数据提供时间戳作用（用于在 TreeMap 上比较）
    private final Map<Integer, CacheInfo> keyToVal;  // 记录缓存数据（用 key 查 value、count、time）
    private final TreeMap<CacheInfo, Integer> treeMap;  // 记录缓存数据到 key 的映射（TreeMap 具有排序能力）

    public LFUCache_2(int capacity) {
        this.capacity = capacity;
        time = 0;
        keyToVal = new HashMap<>();
        treeMap = new TreeMap<>((c1, c2) -> c1.count == c2.count  // 自定义 TreeMap 的 key-sort function
                ? c1.time - c2.time     // 两个缓存数据，当 count 相同时，比较 timestamp（从小到大）
                : c1.count - c2.count);
    }

    public int get(int key) {
        if (!keyToVal.containsKey(key)) return -1;

        // update the cache info in treeMap and keyToVal
        CacheInfo cacheInfo = keyToVal.get(key);
        treeMap.remove(cacheInfo);   // 更新 treeMap 上的值要先 remove 旧的再 put 新的
        CacheInfo newCacheInfo = new CacheInfo(cacheInfo.value, time++, cacheInfo.count + 1);
        treeMap.put(newCacheInfo, key);
        keyToVal.put(key, newCacheInfo);

        return cacheInfo.value;
    }

    public void put(int key, int value) {
        if (capacity == 0) return;

        if (keyToVal.containsKey(key)) {
            // if key exists, update the cache
            CacheInfo cacheInfo = keyToVal.get(key);
            treeMap.remove(cacheInfo);
            CacheInfo newCacheInfo = new CacheInfo(value, time++, cacheInfo.count + 1);
            treeMap.put(newCacheInfo, key);
            keyToVal.put(key, newCacheInfo);
        } else {
            // if key doesn't exist, create a new one
            if (treeMap.size() == capacity) {
                int endKey = treeMap.pollFirstEntry().getValue();  // 根据 TreeMap 的 key-sort function 返回第一个 entry（即 TreeMap 的最左叶子节点）
                keyToVal.remove(endKey);  // evict the LRU
            }
            CacheInfo newCacheInfo = new CacheInfo(value, time++, 1);
            keyToVal.put(key, newCacheInfo);
            treeMap.put(newCacheInfo, key);
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
