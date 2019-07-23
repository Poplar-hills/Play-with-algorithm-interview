package StackAndQueue.StackAndRecursion.L341_FlattenNestedListIterator;

import java.util.*;

/*
* Flatten Nested List Iterator
*
* - Given a nested list of integers, implement an iterator to flatten it.
* - Each element is either an integer, or a list -- whose elements may also be integers or other lists.
*
* - Your NestedIterator object will be instantiated and called as such:
*     NestedIterator i = new NestedIterator(nestedList);
*     while (i.hasNext()) v[f()] = i.next();
*
* - Example 1: Input: [[0,1],2,[3,4]], Output: [0,1,2,3,4]. By calling next repeatedly until hasNext returns false,
*   the order of elements returned by next should be: [0,1,2,3,4].
*
* - Example 2: Input: [1,[4,[6]]], Output: [1,4,6].
* */


/*
 * 解法1：Eager approach
 * - 缺点：对于大数据集存在性能问题 —— we are pre-computing and pre-loading everything into the queue, which is a big waste of resource.
 * */
class NestedIterator implements Iterator<Integer> {
    private Queue<Integer> queue = new LinkedList<>();

    public NestedIterator(List<NestedInteger> nestedList) {
        addToQueue(nestedList);
    }

    private void addToQueue(List<NestedInteger> nestedList) {
        for (NestedInteger n : nestedList) {
            if (n.isInteger())
                queue.offer(n.getInteger());
            else
                addToQueue(n.getList());
        }
    }

    @Override
    public Integer next() { return queue.poll(); }

    @Override
    public boolean hasNext() { return !queue.isEmpty(); }
}


/*
 * 解法2：Lazy approach
 * - 实现：使用迭代而非递归。
 *
 * - 总结：Queue 是尾进头出： poll <-- [1|2|3|4|5] <-- offer
 *
 *        Stack 是尾进尾出： [1|2|3|4|5] <-- push
 *                                     --> pop
 *
 *        Deque 实现的 queue 是尾进头出（与 Queue 一致）；而 Deque 实现的 stack 是头进头出（与 Stack 的存储、遍历顺序相反）：
 *                         poll <-- [1|2|3|4|5] <-- offer         push --> [5|4|3|2|1]
 *                                                                 pop <--
 * */
class NestedIterator2 implements Iterator<Integer> {
    private Deque<NestedInteger> dq = new ArrayDeque<>();  // Deque 接口的实现可以是 ArrayDeque 也可以是 LinkedList

    public NestedIterator2(List<NestedInteger> nestedList) {
        for (NestedInteger n : nestedList)
            dq.offer(n);    // 从尾部添加第一层的所有 NestedInteger
    }

    @Override
    public boolean hasNext() {
        while (!dq.isEmpty() && !dq.peek().isInteger()) {
            List<NestedInteger> nestedList = dq.pop().getList();
            for (int i = nestedList.size() - 1; i >= 0; i--)  // 让 nestedList 倒序入栈，因此其第一个元素会先被访问
                dq.push(nestedList.get(i));
        }
        return !dq.isEmpty();
    }

    @Override
    public Integer next() {
        return hasNext() ? dq.pop().getInteger() : null;
    }
}