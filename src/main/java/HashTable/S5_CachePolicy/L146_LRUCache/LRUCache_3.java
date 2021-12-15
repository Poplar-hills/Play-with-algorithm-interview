package HashTable.S5_CachePolicy.L146_LRUCache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/*
 * 解法3：线程安全的 LRU cache
 * - 💎 思路：
 *   1. 使用两个线程安全的数据结构：
 *     - ConcurrentHashMap
 *     - ConcurrentLinkedQueue
 *   2. 使用 ReadWriteLock 来分别给 get()、set() 方法上读/写锁。
 *     - 缓存这种读多写少的场景更适合使用这种读写分离锁，以减少锁竞争，提高系统性能。
 *     - 读写锁可以保证：
 *       * 读线程之间不阻塞，可以并发读；
 *       * 写线程之间互相阻塞，只能串行；
 *       * 读线程与写线程之间互相阻塞，同一时间要么有一个或多个读线程，要么有只一个写线程。
 * - 👉 时间复杂度：∵ moveToTailOfQueue() 方法中的 queue.remove() 是一个 O(n) 复杂度的方法 ∴ 该实现的 get()、set() 方法
 *   都是 O(n) 而非 O(1) ∴ 是不满足要求的。
 * */

public class LRUCache_3<K, V> {

    private final int capacity;
    private final ConcurrentHashMap<K, V> map;
    private final ConcurrentLinkedQueue<K> queue;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock writeLock = readWriteLock.writeLock();
    private final Lock readLock = readWriteLock.readLock();

    public LRUCache_3(int capacity) {
        this.capacity = capacity;
        map = new ConcurrentHashMap<>(capacity);
        queue = new ConcurrentLinkedQueue<>();
    }

    public V get(K key) {
        readLock.lock();
        try {
            if (!map.containsKey(key)) return null;
            moveToTailOfQueue(key);
            return map.get(key);
        } finally {
            readLock.unlock();
        }
    }

    public void set(K key, V val) {
        writeLock.lock();
        try {
            if (map.containsKey(key)) {
                moveToTailOfQueue(key);
                map.put(key, val);
            } else {
                if (map.size() == capacity)
                    removeOldestKey();
                queue.add(key);
                map.put(key, val);
            }
        } finally {
            writeLock.unlock();
        }
    }

    private void moveToTailOfQueue(K key) {
        queue.remove(key);
        queue.add(key);
    }

    private void removeOldestKey() {
        K oldestKey = queue.poll();
        if (oldestKey != null)
            map.remove(oldestKey);
    }
}