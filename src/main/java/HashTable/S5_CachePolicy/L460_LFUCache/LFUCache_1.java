package HashTable.S5_CachePolicy.L460_LFUCache;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import static Utils.Helpers.log;
import static Utils.Helpers.Pair;

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
 * - 💎 思路：
 *   - 第1版：要实现 LFU 只需借助 Map<key, Pair<val, count>> + PriorityQueue<Map.Entry> 即可，让 pq 根据 Map.Entry
 *     中的 count 来排序。当容量满了时只需逐出 pq 顶端的最小 count 所对应的 key 即可。但由于 pq 的增删操作都是 O(logn) 级别，
 *     无法满足题目对 get/set 时间复杂度的要求。
 *   - 第2版：数据结构改为 Map<key, val> + Map<key, count>，并手动维护一个 int minCount。每次 get(key)/set(key) 时
 *     检查该 key 的 count 是否 == minCount，若 ==，则将 minCount++。当容量满了时只需逐出两个 Map 中 minCount 对应的 key
 *     即可。但由于题目还要求在 minCount 有多个对应数据项时要 fall back 为 LRU ∴ 还需要维护这些数据项的插入顺序，当要逐出时，
 *     需从 minCount 对应的 keys 中逐出最久没用到的一个。
 *   - 第3版：在第2版基础上再添加一个 Map<count, LinkedHashSet<key>>，将 count 相同的 keys 维护在一个 LinkedHashSet 中
 *     （之所以要 Linked 是为了维护这些 keys 被访问的先后顺序，从而实现 LRU）。当要逐出时，通过 minCount 找到对应的
 *     LinkedHashSet，并从中找到头部的 LRU key，再将其从3个 Map 中移除。整合一下：
 *       1. Map<key, val>: 存数据，用 key 查 value；
 *       2. Map<key, count>: 存数据的访问次数，用 key 查 count；
 *       3. Map<count, LinkedHashSet<key>>: 存相同 count 的 keys 的访问顺序，用 count 反向查 keys。
 *       4. int minCount:
 * - Java 语法：
 *   - LinkedHashSet.remove(1) will take 1 as an object;
 *   - LinkedList.remove(1) will take 1 as index;
 *   - For example 1->6->3, when remove(1), linkedHashSet will remove 1 itself while LinkedList will remove 6 (index=1)
 * - 时间复杂度：get、put 方法均为 O(1)。
 * */
public class LFUCache_1 {

    private int minCount;  // 维护最小访问次数值（从而能以 O(1) 速度淘汰数据）
    private final int capacity;
    private final HashMap<Integer, Integer> keyToVal;    // 记录缓存数据
    private final HashMap<Integer, Integer> keyToCount;  // 记录每条缓存数据的访问次数
    private final HashMap<Integer, LinkedHashSet<Integer>> countToKeySets;  // 记录不同访问次数所对应的缓存数据集合（查询次数相同
                                                                           // 的数据用 LinkedHashSet 保存访问的先后顺序）
    public LFUCache_1(int capacity) {
        this.capacity = capacity;
        minCount = -1;
        keyToVal = new HashMap<>();
        keyToCount = new HashMap<>();
        countToKeySets = new HashMap<>();
    }

    public int get(int key) {
        if (!keyToVal.containsKey(key)) return -1;

        // 更新该 key 的 count，及其在 LinkedHashSet 中的顺序
        int count = keyToCount.get(key);
        countToKeySets.get(count).remove(key);  // ∵ 访问数据时要将访问次数+1 ∴ 要将该 count 对应的数据从 countToLRUKeys 中删除
        if (countToKeySets.get(count).isEmpty() && count == minCount)  // ∵ 在 get(key) 时需要给 key 的 count +1，若该 count
            minCount++;    // 正好是 minCount，且该 count 只对应当前这一个 key（即该 count 最小且唯一），则此时需要给 minCount++。
        putCount(key, count + 1);

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
        if (keyToVal.size() == capacity) {
            int LRUKey = countToKeySets.get(minCount).iterator().next();  // get LRU from the min count bucket
            evict(LRUKey);
        }
        // 向缓存中新增数据
        minCount = 1;
        putCount(key, minCount);   // adding new key and count
        keyToVal.put(key, value);  // adding new key and value
    }

    private void evict(int key) {
        countToKeySets.get(minCount).remove(key);
        keyToVal.remove(key);
    }

    private void putCount(int key, int count) {
        keyToCount.put(key, count);
        countToKeySets.computeIfAbsent(count, ignore -> new LinkedHashSet<>());  // 👉 对于要 new 的参数应使用 computeIfAbsent 而非 putIfAbsent
        countToKeySets.get(count).add(key);  // 将 key 加入到 count 对应的 LinkedHashSet 中（保持插入顺序）
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
