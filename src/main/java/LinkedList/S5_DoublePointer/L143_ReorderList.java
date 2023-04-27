package LinkedList.S5_DoublePointer;

import Utils.Helpers.ListNode;

import java.util.Stack;
import java.util.ArrayDeque;
import java.util.Deque;

import static Utils.Helpers.*;

/*
 * Reorder List
 *
 * - Given a singly linked list L0 -> L1 -> ... -> Ln-1 -> Ln
 *   reorder it to: L0 -> Ln -> L1 -> Ln-1 -> L2 -> Ln-2 -> ...
 *
 * - You may not modify the values in the list's nodes, only nodes itself may be changed.
 * */

public class L143_ReorderList {
    /*
     * 解法1：Stack 反向 + 插入
     * - 思路：根据题意总结出如下需求：
     *   1. Take the nodes of the second half of the linked list
     *   2. Reverse the nodes
     *   3. Insert the reversed nodes into the first half of the linked list
     *   为了满足需求，需先找到链表的长度，然后从中间开始 take 链表后半部分的 nodes，借助 Stack 反向，然后再插入前半部分。
     * - 👉🏻 改进：找链表中点的方式可以改进为解法4中的 mid 方法。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static void reorderList(ListNode head) {
        if (head == null || head.next == null) return;
        // 1. get length
        int len = getLength(head);
        int numToTake = len / 2;

        // 2. take the second half of the linked list to reverse
        Stack<ListNode> stack = new Stack<>();
        ListNode curr = head, mid = null;
        for (int i = 0; i < len; i++) {
            if (i == numToTake) mid = curr;
            if (i > numToTake) stack.push(curr);
            curr = curr.next;
        }
        mid.next = null;

        // 3. insert the reversed nodes into the first half of the linked list
        curr = head;
        while (!stack.isEmpty()) {
            ListNode third = curr.next;
            curr.next = stack.pop();
            curr.next.next = third;
            curr = third;
        }
    }

    private static int getLength(ListNode head) {
        int len = 1;
        for (ListNode curr = head; curr.next != null; curr = curr.next)
            len++;
        return len;
    }

    /*
     * 解法2：借助 Stack 获取反向链表
     * - 思路：另一种思路是将题中的 reorder 需求理解为将正序和倒序的该链表 merge 起来，直到中点为止 ∴ 需要：
     *   1. 正序链表：已经给出了；
     *   2. 倒序链表：转化为 L206_ReverseLinkedList，该解法中通过 stack 实现；
     *   3. merge 结束：若有10个节点则需要 merge 5个倒序节点，若有9个节点则需要 merge 4个倒序节点 ∴ 当 merge 节点的个数
     *      达到 len/2 时即可停止。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static void reorderList2(ListNode head) {
        if (head == null || head.next == null) return;

        Stack<ListNode> stack = new Stack<>();
        for (ListNode curr = head; curr != null; curr = curr.next)
            stack.push(curr);

        ListNode curr = head;
        int len = stack.size();              // stack.size 就是链表长度（注意该变量不能 inline）
        for (int i = 0; i < len / 2; i++) {  // 一共有 链表长度/2 个节点待插入 ∴ 就遍历这么多遍即可
            ListNode third = curr.next;
            curr.next = stack.pop();
            curr.next.next = third;
            curr = third;
        }
        curr.next = null;  // 注意最后要断开最后一个节点与下一个节点（已被穿插到前面去了）的链接，否则会与前一个节点形成环
    }

    /*
     * 解法3：借助 Deque 获取反向链表
     * - 思路：与解法2一致，都是要先获得反向链表，再与正向链表进行 merge。
     * - 💎 实现：解法2是借助 Stack 来获取反向链表，再与正向的 LinkedList 合并，涉及两个数据结构。而本解法用 Deque 将两个
     *   结构合二为一，从前面出队得到正向链表的节点，从后面出队得到反向链表的节点 ∴ 要 merge 正反链表，只需从 Deque 前后两端
     *   不断切换着取值即可。
     * - 👉🏻 Deque 的操作 SEE: DequeTest。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static void reorderList3(ListNode head) {
        if (head == null || head.next == null) return;

        Deque<ListNode> dq = new ArrayDeque<>();
        for (ListNode curr = head; curr != null; curr = curr.next)
            dq.offer(curr);    // Deque 的 add、addLast 都是从尾部添加元素

        ListNode dummyHead = new ListNode();
        ListNode prev = dummyHead;
        for (int i = 0; !dq.isEmpty(); i++) {
            prev.next = (i % 2 == 0) ? dq.pollFirst() : dq.pollLast();  // 用 pollFirst、pollLast 方法分别获取正、反向链表的节点
            prev = prev.next;
        }

        prev.next = null;    // 最后记得 put an end
    }

    /*
     * 解法4：生成反向链表后 merge
     * - 思路：与解法2、3一致，还是要先获得反向链表，再与正向链表进行 merge。
     * - 实现：解法2、3都是借用其它数据结构来获得整个原链表的反向链表。但实际上只需为原链表的后半部分生成反向链表即可 ∴ 可以只要
     *   找到原链表的中点，将后半部分截断、reverse、再与前半部分 merge 即可。例如 1->2->3->4->5 的中间节点为3，后半部分的
     *   反向链表为 5->4，merge 后得到：1->5->2->4->3；
     * - 💎 技巧：找链表的中间点的方法有很多（如解法1中的先求得长度），但 slow/fast 方式是最快的 O(n/2)。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static void reorderList4(ListNode head) {
        ListNode midNode = mid(head);
        ListNode reversed = reverse(midNode.next);
        midNode.next = null;
        ListNode prev = head;

        while (reversed != null) {
            ListNode next = prev.next;
            prev.next = reversed;
            reversed = reversed.next;
            prev.next.next = next;    // 至此完成节点的插入
            prev = next;              // 将指针移到下一个待插入位置之前的节点
        }
    }

    private static ListNode mid(ListNode head) {  // 💎 找链表的中间点的最佳方法
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {  // 若 fast 指针到达尾节点（test case 1）或到达 null（test case 2）则循环结束
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    private static ListNode reverse(ListNode head) {  // 链表反向的实现应该非常熟练
        if (head == null) return null;
        ListNode prev = null, curr = head;
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return prev;  // prev 最后停在原链表的尾节点上，即反向之后的头结点
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 2, 3, 4, 5});
        reorderList3(l1);
        log(l1);  // expects 1->5->2->4->3->NULL

        ListNode l2 = createLinkedList(new int[]{1, 2, 3, 4});
        reorderList3(l2);
        log(l2);  // expects 1->4->2->3->NULL

        ListNode l3 = createLinkedList(new int[]{1, 1, 1, 2, 1, 3, 1, 1, 3});
        reorderList3(l3);
        log(l3);  // expects 1->3->1->1->1->1->2->3->1->NULL
    }
}
