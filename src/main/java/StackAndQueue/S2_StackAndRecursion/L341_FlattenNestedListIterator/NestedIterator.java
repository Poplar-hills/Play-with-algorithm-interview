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
        for (NestedInteger n : nestedList) {  // 横向遍历
            if (n.isInteger())
                queue.offer(n.getInteger());
            else
                addToQueue(n.getList());      // 纵向递归
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
 * - 👉总结：将解法1的递归式改写为迭代式的关键在于用 Stack + while 模拟系统调用栈（很好的练习）。
 * - 时间复杂度 O(n)，空间复杂度 O(n)。
 * */
class NestedIterator2 implements Iterator<Integer> {
    private Queue<Integer> queue = new LinkedList<>();

    public NestedIterator2(List<NestedInteger> nestedList) {
        Stack<NestedInteger> stack = new Stack<>();  // 模拟解法1中的调用栈，存储还未解析的 NestedInteger（解析过的 int 则放入 Queue 中）
        pushInReverseOrder(nestedList, stack);       // 先加载数据

        while (!stack.isEmpty()) {                   // 对加载的数据进行解析
            NestedInteger n = stack.pop();
            if (n.isInteger())                       // 若 NestedInteger 解析为 int 则直接入队等待消费
                queue.offer(n.getInteger());
            else                                     // 否解析为 List<NestedInteger> 则继续入栈，等待解析
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
 *   everything into memory, which can be a big waste of resource。要解决这个问题可使用 lazy style：Lazy 与 eager
 *   的区别在于实例化时（构造器中）做的事情：
 *   - Eager iterator 在实例化时要完成所有计算和加载工作；
 *   - Lazy iterator 在实例化时只加载数据，而计算工作等到真正消费时（hasNext、next）再进行。
 * - 实现：与解法2类似，需要使用 Stack 模拟调用栈，将加载的数据放到 Stack 中等待消费时去计算。
 * */
class NestedIterator3 implements Iterator<Integer> {
    private Stack<NestedInteger> stack = new Stack<>();

    public NestedIterator3(List<NestedInteger> nestedList) {
        pushInReverseOrder(nestedList);  // 实例化时只将数据加载到 Stack 中
    }

    private void pushInReverseOrder(List<NestedInteger> list) {
        for (int i = list.size() - 1; i >= 0; i--)
            stack.push(list.get(i));
    }

    @Override
    public boolean hasNext() {
        while (!stack.isEmpty()) {         // 在消费时再进行计算（解析数据）
            if (stack.peek().isInteger())  // 若是 int 则放在那等待消费
                return true;
            pushInReverseOrder(stack.pop().getList());  // 否则再加载到 Stack 中进行解析
        }
        return false;
    }

    @Override
    public Integer next() {
        return hasNext() ? stack.pop().getInteger() : null;
    }
}
