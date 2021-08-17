package HashTable.S5_CachePolicy.L460_LFUCache;

import java.util.*;

import static Utils.Helpers.log;

/*
 * 解法2：TreeMap + HashMap
 * - 思路：与解法1的区别：
 *   1. 解法1使用 Map<Integer, LinkedHashSet> 记录 count -> keys 的映射，在 count 相同情况下由 LinkedHashSet 保存数据的
 *      最近访问顺序；而本解法中使用 TreeMap<CacheVal, Integer> 记录 value/count -> key 的映射，通过定制 TreeMap 的比较器
 *      来对缓存数据进行排序（先比较 count，count 相同时比较 timestamp）。
 *   2. 在淘汰数据时，解法1中通过维护的 minCount 来快速找到 LFU、LRU 数据；而本解法中由于 TreeMap 的比较器中已经揉进了对
 *      timestamp 的比较 ∴ 在淘汰数据时直接 remove 比较出来的最"小"数据即可。
 * - 时间复杂度：set、get 方法均为 O(log(capacity))。
 * */

public class LFUCache_2 {

    private static class CacheVal {
        int value, count, stamp;
        public CacheVal(int value, int stamp, int count) {
            this.value = value;
            this.stamp = stamp;
            this.count = count;
        }
    }

    private final int capacity;
    private int stamp;  // 全局 timestamp，每次 get、put 操作都会让其自增，从而为缓存数据提供时间戳作用（用于在 TreeMap 上比较）
    private final Map<Integer, CacheVal> keyToVal;  // 记录缓存数据（用 key 查 value、count、stamp）
    private final TreeMap<CacheVal, Integer> treeMap;  // 记录缓存数据到 key 的映射（TreeMap 具有排序能力）

    public LFUCache_2(int capacity) {
        this.capacity = capacity;
        stamp = 0;
        keyToVal = new HashMap<>();
        treeMap = new TreeMap<>((c1, c2) -> c1.count == c2.count  // 自定义 key-sort function 的 TreeMap
                ? c1.stamp - c2.stamp     // 两个缓存数据，当 count 相同时，比较 timestamp（在遍历时，较小的会先被遍历到）
                : c1.count - c2.count);
    }

    public int get(int key) {
        if (!keyToVal.containsKey(key)) return -1;
        // update the cache value on the treeMap and keyToVal
        CacheVal cacheVal = keyToVal.get(key);
        treeMap.remove(cacheVal);
        CacheVal newCacheVal = new CacheVal(cacheVal.value, stamp++, cacheVal.count + 1);
        treeMap.put(newCacheVal, key);
        keyToVal.put(key, newCacheVal);
        // return the value
        return cacheVal.value;
    }

    public void put(int key, int value) {
        if (capacity == 0) return;

        if (keyToVal.containsKey(key)) {
            // if key exists, update the cache
            CacheVal cacheVal = keyToVal.get(key);
            treeMap.remove(cacheVal);
            CacheVal newCacheVal = new CacheVal(value, stamp++, cacheVal.count + 1);
            treeMap.put(newCacheVal, key);
            keyToVal.put(key, newCacheVal);
        } else {
            // if key doesn't exist, create a new one
            if (treeMap.size() == capacity) {
                int endKey = treeMap.pollFirstEntry().getValue();  // returns the first Entry in the TreeMap according to the TreeMap's key-sort function
                keyToVal.remove(endKey);  // evict the LRU
            }
            CacheVal newCacheVal = new CacheVal(value, stamp++, 1);
            keyToVal.put(key, newCacheVal);
            treeMap.put(newCacheVal, key);
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
