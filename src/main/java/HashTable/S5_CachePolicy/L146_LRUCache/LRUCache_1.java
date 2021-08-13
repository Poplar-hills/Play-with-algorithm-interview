package HashTable.S5_CachePolicy.L146_LRUCache;

import java.util.HashMap;
import java.util.Map;

import static Utils.Helpers.log;

/*
 * LRU Cache
 *
 * - Design a data structure that follows the constraints of a Least Recently Used (LRU) cache.
 *
 * - Implement the LRUCache class:
 *   1. LRUCache(int capacity) Initialize the LRU cache with positive size capacity.
 *   2. int get(int key) Return the value of the key if the key exists, otherwise return -1.
 *   3. void put(int key, int value) Update the value of the key if the key exists. Otherwise, add the key-value
 *      pair to the cache. If the number of keys exceeds the capacity from this operation, evict the least recently
 *      used key.
 *   4. The functions get and put must each run in O(1) average time complexity.
 * */

/*
 * 解法1：Doubly linked list (DLL) + Map
 * - 思路：使用双向链表，从头部添加数据（访问数据时也重新插入到投入），尾部淘汰数据；
 * - 时间复杂度：get、put 操作都是 O(1)；空间复杂度 O(n)。
 * */
public class LRUCache_1 {

    private static class Node {  // 双向链表（DLL）的节点类
        int key, val;
        Node prev, next;
        public Node(int key, int val) {
            this.key = key;
            this.val = val;
        }
    }

    private final Map<Integer, Node> map;  // 注意 map 存储的是数据的 key -> 该数据在链表上对应 node 的映射
    private final Node dummyHead, dummyTail;
    private final int capacity;

    public LRUCache_1(int capacity) {
        this.capacity = capacity;
        map = new HashMap<>();
        dummyHead = new Node(0,0);
        dummyTail = new Node(0,0);
        join(dummyHead, dummyTail);  // 初始化头尾两个节点，并互相连接
    }

    private void join(Node node1, Node node2) {
        node1.next = node2;
        node2.prev = node1;
    }

    public int get(int key) {
        if (!map.containsKey(key)) return -1;
        Node node = map.get(key);
        remove(node);      // 将该节点从 DLL 上移除
        moveToHead(node);  // 并移动到链表头部
        return node.val;
    }

    private void remove(Node node) {  // remove a node from the DLL
        join(node.prev, node.next);
    }

    private void moveToHead(Node node) {
        Node next = dummyHead.next;
        join(dummyHead, node);
        join(node, next);
    }

    public void put(int key, int value) {
        if (map.containsKey(key)) {  // 若 key 已存在，则只更新 value，并将该节点移动到头部
            Node node = map.get(key);
            node.val = value;
            remove(node);
            moveToHead(node);
        } else {
            if (map.size() == capacity)  // 若缓存已达最大容量
                evict();
            Node node = new Node(key, value);  // 创建新节点
            map.put(key, node);
            moveToHead(node);
        }
    }

    private void evict() {  // 清理掉最长时间没使用到的数据项（即 DLL 上的 dummyTail.prev）
        if (dummyHead.next != dummyTail) {  // 或者判断 !map.isEmpty() 也可以
            map.remove(dummyTail.prev.key);  // 注意这两句不能颠倒顺序，若先 remove 掉节点，dummyTail.prev 已不再指向同一个节点
            remove(dummyTail.prev);
        }
    }

    public static void main(String[] args) {
        LRUCache_1 cache = new LRUCache_1(2);
        cache.put(1, 1);    // cache is {1:1}
        cache.put(2, 2);    // cache is {1:1, 2:2}
        log(cache.get(1));  // return 1
        cache.put(3, 3);    // LRU key was 2, evicts key 2, cache is {1:1, 3:3}
        log(cache.get(2));  // returns -1 (not found)
        cache.put(4, 4);    // LRU key was 1, evicts key 1, cache is {4:4, 3:3}
        log(cache.get(1));  // return -1 (not found)
        log(cache.get(3));  // return 3
        log(cache.get(4));  // return 4
    }
}
