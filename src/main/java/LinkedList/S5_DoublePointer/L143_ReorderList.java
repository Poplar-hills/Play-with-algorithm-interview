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
     * 解法1：借助 Stack 获取反向链表
     * - 思路：题中的 reorder 需求是将正序链表和倒序链表 merge 起来，直到中点为止 ∴ 需要：
     *   1. 正序链表：已经给出了；
     *   2. 倒序链表：转化为 L206_ReverseLinkedList，该解法中通过 stack 实现；
     *   3. merge 结束：若有10个节点则需要 merge 5个倒序节点，若有9个节点则需要 merge 4个倒序节点 ∴ 当 merge 节点的个数
     *      达到 len/2 时即可停止。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static void reorderList(ListNode head) {
        if (head == null || head.next == null) return;

        Stack<ListNode> stack = new Stack<>();
        for (ListNode curr = head; curr != null; curr = curr.next)
            stack.push(curr);

        ListNode curr = head;
        int len = stack.size();              // stack.size 就是链表长度（注意该变量不能 inline）
        for (int i = 0; i < len / 2; i++) {  // 一共有 stack.size / 2 个节点待插入 ∴ 就遍历这么多遍即可
            ListNode next = curr.next;
            curr.next = stack.pop();
            curr.next.next = next;
            curr = next;
        }
        curr.next = null;  // 注意最后要断开最后一个节点与下一个节点（已被穿插到前面去了）的链接，否则会与前一个节点形成环
    }

    /*
     * 解法2：借助 Deque 获取反向链表
     * - 思路：与解法1一致，都是要先获得反向链表，再与正向链表进行 merge。
     * - 实现：解法1中是借助 Stack 来获取反向链表，再与正向的 LinkedList 合并，涉及两个数据结构。而本解法中使用 Deque 将两个
     *   结构合二为一，从前面出队即得到正向链表，从后面出队即得到反向链表。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static void reorderList2(ListNode head) {
        if (head == null || head.next == null) return;

        Deque<ListNode> dq = new ArrayDeque<>();
        for (ListNode curr = head; curr != null; curr = curr.next)
            dq.add(curr);    // Deque 的 add、addLast 都是从尾部添加元素

        ListNode dummyHead = new ListNode();
        ListNode prev = dummyHead;
        for (int i = 0; !dq.isEmpty(); i++) {
            prev.next = (i % 2 == 0) ? dq.pollFirst() : dq.pollLast();  // 用 pollFirst、pollLast 方法分别获取正、反向链表的节点
            prev = prev.next;
        }

        prev.next = null;    // 最后记得 put an end
    }

    /*
     * 解法3：生成反向链表后 merge
     * - 思路：如果我们需要的是一个能从后往前移动的指针，那么最直接的方式就是先生成一个反向链表。步骤如下：
     *   1. 生成反向链表 —— 不需包含全部节点，只需要原链表的后一半节点即可，因此需要找到链表的中点，将中点到尾节点这部分链表反向。
     *   2. 至此问题转变成了 merge 两个链表，即将反向后的半截链表中的每个节点插入原链表中：
     *      - 1->2->3->4->5 的中间节点为3，可得反向链表 5->4->3，merge 后得到：1->5->2->4->3；
     *      - 1->2->3->4 的中间节点为3，可得反向链表 4->3，merge 后得到：1->4->2->3->3；
     *      - 注意：反向链表的最后一个节点不需要 merge 到原链表中 ∴ merge 的循环结束条件是到达反向链表的最后一个节点（而非到达 null）。
     * - 技巧：找链表的中点的最佳方式是采用 slow/fast 技巧。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static void reorderList3(ListNode head) {
        ListNode midNode = mid(head);
        ListNode head2 = reverse(midNode);
        ListNode prev = head;

        while (head2.next != null) {  // merge 的循环结束条件是到达反向链表的最后一个节点（最后一个节点不 merge 到原链表中）
            ListNode temp = prev.next;
            prev.next = head2;
            head2 = head2.next;
            prev.next.next = temp;    // 至此完成节点的插入
            prev = temp;              // 将指针移到下一个节点的插入位置之前
        }
    }

    private static ListNode reverse(ListNode head) {
        if (head == null) return null;
        ListNode prev = null, curr = head;
        while (curr != null) {
            ListNode temp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = temp;
        }
        return prev;
    }

    private static ListNode mid(ListNode head) {  // 找链表的中间点的方法有很多（如先求得长度），但这种 slow/fast 方式是最快的 O(n/2)
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {  // 若 fast 指针到达尾节点（test case 1）或到达 null（test case 2）则循环结束
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 2, 3, 4, 5});
        reorderList2(l1);
        printLinkedList(l1);  // expects 1->5->2->4->3->NULL

        ListNode l2 = createLinkedList(new int[]{1, 2, 3, 4});
        reorderList2(l2);
        printLinkedList(l2);  // expects 1->4->2->3->NULL

        ListNode l3 = createLinkedList(new int[]{1, 1, 1, 2, 1, 3, 1, 1, 3});
        reorderList2(l3);
        printLinkedList(l3);  // expects 1->3->1->1->1->1->2->3->1->NULL
    }
}
