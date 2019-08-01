package LinkedList.DoublePointer;

import Utils.Helpers.ListNode;

import java.util.Stack;

import static Utils.Helpers.*;

/*
* Reorder List
*
* - Given a singly linked list L0 -> L1 -> ... -> Ln-1 -> Ln, reorder it to: L0 -> Ln -> L1 -> Ln-1 -> L2 -> Ln-2 -> ...
* - You may not modify the values in the list's nodes, only nodes itself may be changed.
* */

public class L143_ReorderList {
    /*
    * 解法1：借助 stack 实现反向遍历
    * - 思路：若要按要求 reorder 链表，则需从后往前逐个移动链表尾部的节点，因此需要一个能从后往前移动的指针，这可以通过 stack 实现。
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static void reorderList(ListNode head) {
        if (head == null || head.next == null) return;

        Stack<ListNode> stack = new Stack<>();
        for (ListNode curr = head; curr != null; curr = curr.next)
            stack.push(curr);

        ListNode curr = head;
        int len = stack.size();  // stack.size 就是链表长度，不需手动计算（注意该变量不能 inline，∵ 循环中会不断改变 stack.size）
        for (int i = 0; i < len / 2; i++) {  // 一共有 stack.size / 2 个节点待插入，因此就遍历这么多遍即可
            ListNode third = curr.next;
            curr.next = stack.pop();
            curr.next.next = third;
            curr = third;
        }
        curr.next = null;  // 对于 test case 1、2 来说，最后 curr 都会指向3节点（但 case 2 中的3节点会与自己形成环）（自己画一下）
    }

    /*
    * 解法2：生成反向链表后 merge
    * - 思路：如果我们需要的是一个能从后往前移动的指针，那么最直接的方式就是先生成一个反向链表。步骤如下：
    *   1. 生成反向链表 —— 不需包含全部节点，只需要原链表的后一半节点即可，因此需要找到链表的中点，将中点到尾节点这部分链表反向。
    *   2. 至此问题转变成了 merge 两个链表，即将反向后的半截链表中的每个节点插入原链表中：
    *      - 1->2->3->4->5 的中间节点为3，可得反向链表 5->4->3，merge 后得到：1->5->2->4->3；
    *      - 1->2->3->4 的中间节点为3，可得反向链表 4->3，merge 后得到：1->4->2->3->3；
    *      - 注意：反向链表的最后一个节点不需要 merge 到原链表中 ∴ merge 的循环结束条件是到达反向链表的最后一个节点（而非到达 null）。
    * - 技巧：找链表的中点的最佳方式是采用 slow/fast 技巧。
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static void reorderList2(ListNode head) {
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
        ListNode l1 = createLinkedListFromArray(new int[]{1, 2, 3, 4, 5});
        reorderList(l1);
        printLinkedList(l1);  // expects 1->5->2->4->3->NULL

        ListNode l2 = createLinkedListFromArray(new int[]{1, 2, 3, 4});
        reorderList2(l2);
        printLinkedList(l2);  // expects 1->4->2->3->NULL

        ListNode l3 = createLinkedListFromArray(new int[]{1, 1, 1, 2, 1, 3, 1, 1, 3});
        reorderList2(l3);
        printLinkedList(l3);  // expects 1->3->1->1->1->1->2->3->1->NULL
    }
}
