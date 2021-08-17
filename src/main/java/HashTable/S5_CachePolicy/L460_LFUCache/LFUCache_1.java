package HashTable.S5_CachePolicy.L460_LFUCache;

import java.util.HashMap;
import java.util.LinkedHashSet;

import static Utils.Helpers.log;

/*
 * LFU Cache
 *
 * - Design and implement a data structure for a Least Frequently Used (LFU) cache.
 *
 * - Implement the LFUCache class:
 *   1. LFUCache(int capacity) Initializes the object with the capacity of the data structure.
 *   2. int get(int key) Gets the value of the key if the key exists in the cache. Otherwise, returns -1.
 *   3. void put(int key, int value) Update the value of the key if present, or inserts the key if not already present.
 *      When the cache reaches its capacity, it should invalidate and remove the least frequently used key before
 *      inserting a new item. For this problem, when there is a tie (i.e., two or more keys with the same frequency),
 *      the least recently used key would be invalidated.
 *   4. The functions get and put must each run in O(1) average time complexity.
 *
 * - To determine the least frequently used key, a use counter is maintained for each key in the cache. The key with
 *   the smallest use counter is the least frequently used key.
 * - When a key is first inserted into the cache, its use counter is set to 1 (due to the put operation). The use
 *   counter for a key in the cache is incremented either a get or put operation is called on it.
 * */

/*
 * 解法1：
 * - 思路：根据题意，LFU 在淘汰数据时先比较使用次数，对于使用次数相同的缓存数据会退化成 LRU，即比较其最近的使用时间。因此整体思路为：
 *   1. 用1个 Map 保存缓存数据：用 key 查 value；
 *   2. 用1个 Map 保存缓存数据的访问次数：用 key 查 count；
 *   3. 用1个 Map 保存不同访问次数所对应的缓存数据（查询次数相同的数据用要保存访问先后顺序）：用 count 查 keys。
 * - Java 语法：
 *   - LinkedHashSet.remove(1) will take 1 as an object;
 *   - LinkedList.remove(1) will take 1 as index;
 *   - For example 1->6->3, when remove(1), linkedHashSet will remove 1 itself while LinkedList will remove 6 (index=1)
 * - 时间复杂度：；空间复杂度：。
 * */
public class LFUCache_1 {

    private int minCount;  // 维护最小访问次数值（从而能以 O(1) 速度淘汰数据）
    private final int capacity;
    private final HashMap<Integer, Integer> keyToVal;    // 记录缓存数据
    private final HashMap<Integer, Integer> keyToCount;  // 记录每条缓存数据的访问次数
    private final HashMap<Integer, LinkedHashSet<Integer>> countToLRUKeys;  // 不同访问次数所对应的缓存数据（查询次数相同的数据用 LinkedHashSet 保存访问的先后顺序）

    public LFUCache_1(int capacity) {
        this.minCount = -1;
        this.capacity = capacity;
        this.keyToVal = new HashMap<>();
        this.keyToCount = new HashMap<>();
        this.countToLRUKeys = new HashMap<>();
    }

    public int get(int key) {
        if (!keyToVal.containsKey(key)) return -1;

        int count = keyToCount.get(key);
        countToLRUKeys.get(count).remove(key);  // ∵ 访问数据时要将访问次数+1 ∴ 要将该 count 对应的数据从 countToLRUKeys 中删除
        if (countToLRUKeys.get(count).size() == 0 && count == minCount)  // 若上一行中从 countToLRUKeys 删除数据后，该 count
            minCount++;                         // 不再有对应的数据，且该 count == minCount，说明此时 minCount 需要自增

        putCount(key, count + 1);  // 更新该数据的 count
        return keyToVal.get(key);
    }

    public void put(int key, int value) {
        if (capacity <= 0) return;
        // 若 key 存在于缓存中
        if (keyToVal.containsKey(key)) {
            keyToVal.put(key, value);  // 更新该 key 的 value
            get(key);                  // 更新该 key 的 count（通过 get 方法来实现更新）
            return;
        }
        // 若超过缓存容量则淘汰 count 最小的数据
        if (keyToVal.size() >= capacity)
            evict(countToLRUKeys.get(minCount).iterator().next());  // evict LRU from the min count bucket
        // 向缓存中新增数据
        minCount = 1;
        putCount(key, minCount);   // adding new key and count
        keyToVal.put(key, value);  // adding new key and value
    }

    private void evict(int key) {
        countToLRUKeys.get(minCount).remove(key);
        keyToVal.remove(key);
    }

    private void putCount(int key, int count) {
        keyToCount.put(key, count);
        countToLRUKeys.computeIfAbsent(count, ignore -> new LinkedHashSet<>());
        countToLRUKeys.get(count).add(key);
    }

    public static void main(String[] args) {
        LFUCache_1 cache = new LFUCache_1(2);
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
