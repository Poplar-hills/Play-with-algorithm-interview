package StackAndQueue.S2_StackAndRecursion.L341_FlattenNestedListIterator;

import java.util.*;

/*
 * Flatten Nested List Iterator
 *
 * - Given a nested list of integers, implement an iterator to flatten it.
 * - Each element is either an integer, or a list - whose elements may also be integers or other lists.
 *
 * - Your NestedIterator object will be instantiated and called as such:
 *       NestedIterator i = new NestedIterator(nestedList);
 *       while (i.hasNext())
 *           doSomethingWith(i.next());
 *
 * - Example 1: Input: [0,[[1,2],3],[4,[5,6]]]  Output: [0,1,2,3,4,5,6]
 *   Example 2: Input: [1,[4,[6]]]              Output: [1,4,6]
 * */


/*
 * 解法1：Eager style + Recursion
 * - 思路：根据题意中的调用方式，解法类中要实现一个有 hasNext、next 方法的 iterator。而 iterator 有两种基本形式：
 *     1. Eager: 先将所有元素计算都预先计算出来，然后在调用方法时输出；
 *     2. Lazy: 在每次调用方法时现进行计算并输出。
 *   本解法中采用 eager iterator 的思路 —— 预先将整个 NestedList 解析成 Integer list。该过程中：
 *     1. ∵ iterator 中元素的输出顺序应该与输入数据的元素顺序一致 ∴ iterator 的基本数据结构应该是 queue；
 *     2. 输入数据中可能有无限层级的嵌套，这是个典型的可以递归的场景。
 * - 时间复杂度 O(n)，空间复杂度 O(n)。
 * */
class NestedIterator implements Iterator<Integer> {
    private Queue<Integer> queue = new LinkedList<>();

    public NestedIterator(List<NestedInteger> nestedList) {
        addToQueue(nestedList);
    }

    private void addToQueue(List<NestedInteger> nestedList) {
        for (NestedInteger n : nestedList) {  // 递归遍历（其实也是 DFT）
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
 * 解法2：Eager style + Iteration
 * - 思路：解法1的迭代版
 * - 总结：将解法1的递归式改写为迭代式的关键在于用 Stack + while 循环模拟系统调用栈（很好的练习）。
 * - 时间复杂度 O(n)，空间复杂度 O(n)。
 * */
class NestedIterator2 implements Iterator<Integer> {
    private Queue<Integer> queue = new LinkedList<>();

    public NestedIterator2(List<NestedInteger> nestedList) {
        Stack<NestedInteger> stack = new Stack<>();  // 模拟解法1中的调用栈
        pushInReverseOrder(nestedList, stack);

        while (!stack.isEmpty()) {
            NestedInteger n = stack.pop();
            if (n.isInteger())
                queue.offer(n.getInteger());
            else
                pushInReverseOrder(n.getList(), stack);
        }
    }

    private void pushInReverseOrder(List<NestedInteger> list, Stack<NestedInteger> stack) {
        for (int i = list.size() - 1; i >= 0; i--)
            stack.push(list.get(i));
    }

    @Override
    public Integer next() { return queue.poll(); }

    @Override
    public boolean hasNext() { return !queue.isEmpty(); }
}

/*
 * 解法3：Lazy style
 * - 思路：解法1、2中的 eager style 的最大缺点就是，对于大数据集存在性能问题 —— we are pre-computing and pre-loading
 *   everything into memory, which can be a big waste of resource。要解决这个问题就要使用 lazy style：Lazy 与 eager
 *   的区别在于实例化时（构造器中）做哪些事情：
 *   - Eager iterator 在实例化时要完成所有计算和加载工作；
 *   - Lazy iterator 则弱化实例化时的计算逻辑，只加载数据，而主要计算逻辑放在 hasNext、next 方法中完成。
 * */
class NestedIterator3 implements Iterator<Integer> {
    private Stack<NestedInteger> stack = new Stack<>();  // 加载的数据放在 Stack 中

    public NestedIterator3(List<NestedInteger> nestedList) {
        pushInReverseOrder(nestedList);
    }

    private void pushInReverseOrder(List<NestedInteger> list) {
        for (int i = list.size() - 1; i >= 0; i--)
            stack.push(list.get(i));
    }

    @Override
    public boolean hasNext() {
        while (!stack.isEmpty()) {
            if (stack.peek().isInteger()) return true;
            pushInReverseOrder(stack.pop().getList());  // 若栈顶元素不是 integer 则持续寻找
        }
        return false;
    }

    @Override
    public Integer next() {
        return hasNext() ? stack.pop().getInteger() : null;
    }
}
