package StackAndQueue.S2_StackAndRecursion.L341_FlattenNestedListIterator;

import java.util.*;

/*
* Flatten Nested List Iterator
*
* - Given a nested list of integers, implement an iterator to flatten it.
* - Each element is either an integer, or a list -- whose elements may also be integers or other lists.
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
 * 解法1：Eager + recursive
 * - 思路：
 *   1. Eager 的 iterator 会在实例化时一次性将整个 nestedList 解析成可用的 integer list。这样每次调 hasNext/next
 *     方法时就可以直接检查/获得 integer list 中的下一个 integer 即可；
 *   2. ∵ iterator 的输出顺序应该与输入数据一致，只是将数据中的层级拍平而已 ∴ iterator 的基本数据结构应该是 queue；
 *   3. ∵ 输入数据中可能有无限层级的嵌套，这是个典型的递归结构 ∴ iterator 解析 nestedList 的逻辑应该是递归直到找到下
 *      一个 integer 为止。
 * - 缺点：对于大数据集存在性能问题 —— we are pre-computing and pre-loading everything into the queue, which
 *   is a big waste of resource.
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
 * 解法2：Eager + iterative
 * - 思路：解法1的迭代版
 * - 总结：将解法1的递归式改写为迭代式的关键在于用 stack + while 循环模拟系统调用栈（很好的练习）。
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
 * 解法3：Lazy approach
 * - 思路：Lazy 的 iterator 弱化实例化时的计算逻辑，只做数据加载，而主要逻辑放在 hasNext/next 方法中。
 * */
class NestedIterator3 implements Iterator<Integer> {
    private Stack<NestedInteger> stack = new Stack<>();

    public NestedIterator3(List<NestedInteger> nestedList) {
        pushInReverseOrder(nestedList, stack);
    }

    private void pushInReverseOrder(List<NestedInteger> list, Stack<NestedInteger> stack) {
        for (int i = list.size() - 1; i >= 0; i--)
            stack.push(list.get(i));
    }

    @Override
    public boolean hasNext() {
        while (!stack.isEmpty()) {
            if (stack.peek().isInteger()) return true;
            pushInReverseOrder(stack.pop().getList(), stack);  // 若栈顶元素不是 integer 的情况
        }
        return false;
    }

    @Override
    public Integer next() {
        return hasNext() ? stack.pop().getInteger() : null;
    }
}
