package HashTable.S5_CachePolicy;

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
 *   3. void put(int key, int value) Update the value of the key if the key exists. Otherwise, add the key-value pair to the cache. If the number of keys exceeds the capacity from this operation, evict the least recently used key.
 *   4. The functions get and put must each run in O(1) average time complexity.
 * */

public class L146_LRUCache {
    /*
     * 解法1：Doubly linked list + Map
     * */
    private static class Node {  // 双向链表节点类
        int key, val;
        Node prev, next;
        public Node(int key, int val) {
            this.key = key;
            this.val = val;
        }
    }

    Map<Integer, Node> keys;
    int capacity, count;
    Node head, tail;

    public L146_LRUCache(int capacity) {
        keys = new HashMap<>();
        this.capacity = capacity;
        count = 0;
        head = new Node(0,0);
        tail = new Node(0,0);
        head.next = tail;
        tail.prev = head;
    }

    public int get(int key) {
        if (!keys.containsKey(key)) return -1;
        update(keys.get(key), 0);
        return keys.get(key).val;
    }

    private void update(Node node, int val) {
        if (val != 0)
            node.val = val;
        node.prev.next = node.next;
        node.next.prev = node.prev;
        add(node, 0);
    }

    private void add(Node node, int key) {
        node.prev = head;
        node.next = head.next;
        head.next = node;
        node.next.prev = node;
        if (key != 0) {
            keys.put(key, node);
            count++;
        }
    }

    public void put(int key, int value) {
        if (!keys.containsKey(key)) {
            add(new Node(key, value), key);
            if (count > capacity)
                evict();
        } else {
            update(keys.get(key), value);
        }
    }

    private void evict() {
        keys.remove(tail.prev.key);
        tail.prev = tail.prev.prev;
        tail.prev.next = tail;
    }

    public static void main(String[] args) {
        L146_LRUCache lRUCache = new L146_LRUCache(2);
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
