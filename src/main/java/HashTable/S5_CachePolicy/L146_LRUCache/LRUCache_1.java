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
 * - 思路：对于 get() 操作可使用 Map 以 O(1) 速度访问数据（访问数据时也重新插入到头部）；而对于容量已满时的 put() 操作需要能快速
 *   定位到 the least recently used 的数据 ∴ 使用队列结构记录数据的访问时间，在每次访问数据（get/put）时，将被访问的数据移动到
 *   队首，而让 the least recently used 的数据逐渐归到队尾，从而在缓存已满时能以 O(1) 的速度从队尾淘汰。
 * - 实现：
 *   1. ∵ 既要能让数据移动到队首，又要能快速获取队尾数据 ∴ 需要 dummyHead、dummyTail 两个指针；
 *   2. 若是从单向链表尾部删除节点需要先拿到上一个节点 ∴ 要从头遍历过去才能拿到，无法满足 O(1) 的要求 ∴ 不如直接使用双向链表方便。
 *               k1     k2     k3     k4            - Map 的 keys
 *                ↓      ↓      ↓      ↓
 *      head <-> n1 <-> n2 <-> n3 <-> n4 <-> tail   - Map 的 values，同时也是双向链表
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

    private final Map<Integer, Node> map;  // 👉 关键点：Map<数据的 key, 该数据的 value 在链表上对应的 Node>
    private final Node dummyHead, dummyTail;
    private final int capacity;

    public LRUCache_1(int capacity) {
        this.capacity = capacity;
        map = new HashMap<>();
        dummyHead = new Node(0,0);
        dummyTail = new Node(0,0);
        join(dummyHead, dummyTail);  // 👉 初始化头尾两个节点，并互相连接（即初始链表为空）
    }

    private void join(Node node1, Node node2) {
        node1.next = node2;
        node2.prev = node1;
    }

    public int get(int key) {
        if (!map.containsKey(key)) return -1;
        Node node = map.get(key);
        remove(node);      // 先将该节点从 DLL 上移除
        moveToHead(node);  // 再将其移动到链表头部
        return node.val;
    }

    private void remove(Node node) {  // 移除操作就是 join 该节点的上一节点和下一节点
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
            if (map.size() == capacity) evict();  // 若缓存已达最大容量则淘汰 LRU entry
            Node node = new Node(key, value);  // 创建新节点
            map.put(key, node);
            moveToHead(node);
        }
    }

    private void evict() {  // 清理掉最长时间没使用到的数据项（即 DLL 上的 dummyTail.prev）
        if (dummyHead.next != dummyTail) {   // 或 if (!map.isEmpty())
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
