package HashTable.S5_CachePolicy.L460_LFUCache;

import java.util.HashMap;
import java.util.Map;

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
 * 解法1：Doubly linked list (DLL) + Map
 * - 思路：使用双向链表，从头部添加数据（访问数据时也重新插入到投入），尾部淘汰数据；
 * - 时间复杂度：get、put 操作都是 O(1)；空间复杂度 O(n)。
 * */
public class LFUCache_1 {

    public LFUCache_1(int capacity) {

    }

    public int get(int key) {
        return 0;
    }

    public void put(int key, int value) {

    }

    public static void main(String[] args) {
        LFUCache_1 cache = new LFUCache_1(2);
        cache.put(1, 1);  // cache=[1,_], cnt(1)=1
        cache.put(2, 2);  // cache=[2,1], cnt(2)=1, cnt(1)=1
        cache.get(1);     // return 1. cache=[1,2], cnt(2)=1, cnt(1)=2
        cache.put(3, 3);  // 2 is the LFU key coz cnt(2)=1 is the smallest, invalidate 2. cache=[3,1], cnt(3)=1, cnt(1)=2
        cache.get(2);     // return -1 (not found)
        cache.get(3);     // return 3. cache=[3,1], cnt(3)=2, cnt(1)=2
        cache.put(4, 4);  // 1 and 3 have the same cnt, but 1 is LRU, invalidate 1. cache=[4,3], cnt(4)=1, cnt(3)=2
        cache.get(1);     // return -1 (not found)
        cache.get(3);     // return 3. cache=[3,4], cnt(4)=1, cnt(3)=3
        cache.get(4);     // return 4. cache=[3,4], cnt(4)=2, cnt(3)=3
    }
}
