package LinkedList.S5_DoublePointer;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
 * Remove Nth Node From End of List
 *
 * - 删除给定链表的倒数第 n 个节点（n 从1开始），返回删除节点后的链表。
 * */

public class L19_RemoveNthNodeFromEndOfList {
    /*
     * 解法1：获取长度（Two-pass）
     * - 思路：先求链表长度，再找到并删除目标节点。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        if (head == null || n < 1) return null;
        int len = getLength(head);

        if (len < n) return head;
        int m = len - n;   // 从头开始第 m 个元素即是待删除元素（m 从 0 开始）

        ListNode dummyHead = new ListNode();  // ∵ 待删除的可能是第一个节点 ∴ 需要 dummyHead
        dummyHead.next = head;
        ListNode prev = dummyHead, curr = head;

        while (m-- != 0) {
            prev = prev.next;
            curr = curr.next;
        }
        prev.next = curr.next;

        return dummyHead.next;
    }

    private static int getLength(ListNode head) {
        int len = 0;
        for ( ; head != null; head = head.next) len++;
        return len;
    }

    /*
     * 解法2：双指针（One-pass）
     * - 思路：解法1中的瓶颈在于需要先获取链表长度。而另一种思路是通过双指针同时移动找到待删除节点的上一节点。例如：
     *   对于 1->2->3->4->5->null，n=2 来说：
     *   1. 待删除的节点4 ∴ 只要获得节点3即可完成删除；
     *   2. 虽然不知道链表长度，但能知道节点3与 null 的差距是 n+1 ∴ 可以利用这个差距，设置两个初始差距为 n+1 的指针，然后
     *      让他们同时移动，当右指针到达 null 时，左指针即指向节点3；
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode removeNthFromEnd2(ListNode head, int n) {
        if (head == null || n < 0) return head;

        ListNode dummnyHead = new ListNode();
        dummnyHead.next = head;
        ListNode l = dummnyHead, r = dummnyHead;

        for (int i = 0; i < n + 1; i++) {  // 先将 r 移动到距 l n+1 的位置
            if (r == null) return head;    // 处理 n 越界的情况
            r = r.next;
        }

        while (r != null) {     // 同时移动两个指针，当 r 到达 null 时，l 就到了待删除节点的前一节点上了
            l = l.next;
            r = r.next;
        }
        l.next = l.next.next;   // 删除节点

        return dummnyHead.next;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 2, 3, 4, 5});
        printLinkedList(removeNthFromEnd2(l1, 2));  // expects 1->2->3->5->NULL

        ListNode l2 = createLinkedList(new int[]{1, 2, 3});
        printLinkedList(removeNthFromEnd2(l2, 3));  // expects 2->3->NULL

        ListNode l3 = createLinkedList(new int[]{1});
        printLinkedList(removeNthFromEnd2(l3, 2));  // expects 1->NULL (n 越界的 case)

        ListNode l4 = createLinkedList(new int[]{});
        printLinkedList(removeNthFromEnd2(l4, 2));  // expects NULL
    }
}
