package HashTable.S5_CachePolicy.L146_LRUCache;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/*
 * 解法3：
 * - 需求：在 LRU cache 上新增两个需求：
 *   1. 线程安全
 *   2. 可以设置过期时间（TTL）
 *   3. key、value 使用泛型
 * - 💎 思路：
 *   1. 线程安全需求：
 *     a. 使用两个线程安全的数据结构：ConcurrentHashMap、ConcurrentLinkedQueue。
 *     b. 使用 ReadWriteLock 来分别给 get()、set() 方法上读/写锁：
 *        - 缓存这种读多写少的场景是读写分离锁的典型应用场景，可以通过减少锁竞争来提高系统性能（若用 synchronized 则性能较差）。
 *        - 读写锁可以保证：
 *          1). 读线程之间不阻塞，可以并发读；
 *          2). 写线程之间互相阻塞，只能串行；
 *          3). 读线程与写线程之间互相阻塞，同一时间要么有一个或多个读线程，要么有只一个写线程。
 *   2. 给缓存设置的过期时间有多种实现方式：
 *     - Timer：不推荐，存在线程安全问题（如一个任务执行太久的话会阻塞下一个任务）、抛异常后会终止整个 Timer 线程、无 cancel 机制；
 *     - ScheduledExecutorService：定时器线程池，官方推荐用来替代 Timer，更安全、更灵活；
 *     - DelayQueue：延时队列；
 *     - Quartz：流行的开源调度框架（Elastic-job 等框架都是基于 Quartz 开发的）。
 *     这里选择 ScheduledExecutorService，主要是其易用性高（基于 DelayQueue 做了很多封装）满足需求。
 * - 💎 缺陷：
 *   1. ∵ moveToTailOfQueue() 方法中的 queue.remove() 复杂度为 O(n) ∴ 其 get()、set() 方法都是 O(n) 而非 O(1)。
 *   2. 读写锁与线程安全的数据结构效果重叠，只使用其中之一即可。
 * */

public class LRUCache_3<K, V> {

    private final int capacity;
    private final ConcurrentHashMap<K, V> map;
    private final ConcurrentLinkedQueue<K> queue;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock writeLock = readWriteLock.writeLock();
    private final Lock readLock = readWriteLock.readLock();

    private final ScheduledExecutorService scheduledExSrv;

    public LRUCache_3(int capacity) {
        if (capacity <= 0)  // 增加一个入参校验
            throw new IllegalArgumentException("Invalid capacity");
        this.capacity = capacity;
        map = new ConcurrentHashMap<>(capacity);
        queue = new ConcurrentLinkedQueue<>();
        scheduledExSrv = Executors.newScheduledThreadPool(3);
    }

    public V get(K key) {
        readLock.lock();  // 读之前上读锁
        try {
            if (!map.containsKey(key)) return null;
            moveToTailOfQueue(key);
            return map.get(key);
        } finally {
            readLock.unlock();  // 读之后解读锁
        }
    }

    public void set(K key, V val, long expireTime) {
        writeLock.lock();  // 写之前上写锁
        try {
            if (map.containsKey(key)) {
                moveToTailOfQueue(key);
                map.put(key, val);
            } else {
                if (map.size() == capacity) evict();
                queue.add(key);
                map.put(key, val);
                if (expireTime > 0)  // 添加完数据后还要给数据设置过期时间
                    setExpireTime(key, expireTime);
            }
        } finally {
            writeLock.unlock();  // 写之后解写锁
        }
    }

    private void moveToTailOfQueue(K key) {
        queue.remove(key);  // O(n)
        queue.add(key);
    }

    private void evict() {
        K expiredKey = queue.poll();
        if (expiredKey != null)
            map.remove(expiredKey);
    }

    private void setExpireTime(K key, long expireTime) {
        scheduledExSrv.schedule(() -> {
           map.remove(key);
           queue.remove(key);
        }, expireTime, TimeUnit.MILLISECONDS);
    }
}