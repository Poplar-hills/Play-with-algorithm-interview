package StackAndQueue;

import Utils.Helpers;

import java.util.*;

import static Utils.Helpers.*;

/*
 * - 总结：
 * 1. Queue 是尾进头出：
 *        poll <-- [1|2|3|4|5] <-- offer
 *
 * 2. Stack 是尾进尾出：
 *        [1|2|3|4|5] <-- push
 *                    --> pop
 *
 * 3. Deque 当做 queue 用时是尾进头出（与 Queue 一致）；
 *        poll <-- [1|2|3|4|5] <-- offer
 *
 *    Deque 当做 stack 用时是头进头出（∴ 与 Stack 的存储、遍历顺序相反）：
 *        push --> [5|4|3|2|1]
 *        pop <--
 * */

class DequeTest {
      public static void main(String[] args) {
        Queue<Integer> q = new LinkedList<>();
        q.offer(1); q.offer(2); q.offer(3);
        q.forEach(Helpers::log);  // -> 1, 2, 3

        Stack<Integer> s = new Stack<>();
        s.push(1); s.push(2); s.push(3);
        s.forEach(Helpers::log);  // -> 1, 2, 3

        Deque<Integer> d = new ArrayDeque<>();
        d.push(1); d.push(2); d.push(3);  // Deque 用作 Stack
        d.forEach(Helpers::log);  // -> 3, 2, 1 (与 Stack 的遍历顺序相反)

        Deque<Integer> d2 = new ArrayDeque<>();
        d2.offer(1); d2.offer(2); d2.offer(3); // Deque 用作 Queue
        d2.forEach(Helpers::log);  // -> 1, 2, 3（与 Queue 的遍历顺序一致）

        Deque<Integer> d3 = new ArrayDeque<>();
        d3.offer(1); d3.push(2); d3.offer(3);  // Deque 的 offer 从右边进，push 从左进 ∴ 内部存储形式为 [2|1|3]
        d3.forEach(Helpers::log);  // -> 2, 1, 3

        Deque<Integer> d4 = new ArrayDeque<>();
        d4.offer(1); d4.push(2); d4.offer(3);  // Deque 的 pop、poll 都是从左出
        log(d4.pop()); log(d4.pop()); log(d4.poll());  // -> 2, 1, 3

        Deque<Integer> d5 = new ArrayDeque<>();
        d5.offer(1); d5.push(2); d5.offer(3);
        log(d5.pollFirst()); log(d5.pollLast());  // -> 2, 3（若需要双端取值的场景，如 L143_ReorderList，可使用的 pollFirst、pollLast）
    }
}