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

public class NestedIterator implements Iterator<Integer> {
    private Queue<Integer> queue = new LinkedList<>();

    public NestedIterator(List<NestedInteger> nestedList) {
        addToQueue(nestedList);
    }

    private void addToQueue(List<NestedInteger> nestedList) {
        for (NestedInteger n : nestedList) {
            if (n.isInteger())
                queue.add(n.getInteger());
            else
                addToQueue(n.getList());
        }
    }

    @Override
    public Integer next() { return queue.poll(); }

    @Override
    public boolean hasNext() { return !queue.isEmpty(); }
}
