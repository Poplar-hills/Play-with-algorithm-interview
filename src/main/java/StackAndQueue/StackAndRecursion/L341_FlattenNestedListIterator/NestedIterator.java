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
* - Example 1: Input: [[1,1],2,[1,1]], Output: [1,1,2,1,1]. By calling next repeatedly until hasNext returns false,
*   the order of elements returned by next should be: [1,1,2,1,1].
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
 * -
 * */
class NestedIterator2 implements Iterator<Integer> {
    private Deque<NestedInteger> stack = new ArrayDeque<>();  // Deque 接口的实现可以是：ArrayDeque, LinkedList

    public NestedIterator2(List<NestedInteger> nestedList) {
        for (NestedInteger n : nestedList)
            stack.push(n);
    }

    @Override
    public boolean hasNext() {
        while (!stack.isEmpty() && !stack.peek().isInteger()) {
            List<NestedInteger> nestedList = stack.pop().getList();
            for (int i = nestedList.size() - 1; i >= 0; i--)
                stack.push(nestedList.get(i));
        }
        return !stack.isEmpty();
    }

    @Override
    public Integer next() {
        return hasNext() ? stack.pop().getInteger() : null;
    }
}