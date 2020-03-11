package LinkedList.S5_DoublePointer;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
 * Rotate List
 *
 * - Given a linked list, rotate the list to the right by k places, where k is non-negative.
 * */

public class L61_RotateList {
    /*
     * 解法1：截断移动
     * - 思路：要得到 rotate 之后的链表，关键在于要确定从哪里截断并链到头节点上去。例如 0->1->2->3->4，k=7，rotate 之后
     *   得到 3->4->0->1->2，即从原链表2节点之后截断，并把截断的部分放到链表头部。
     * - 实现：要确定从哪里截断则需要知道去掉套圈后的剩余距离（上例中 k % len = 2），即说明原链表中最后2个节点要被截断并放到
     *   链表头部，至此问题转化成了类似 L19 中的问题。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode rotateRight2(ListNode head, int k) {
        if (head == null || head.next == null || k == 0) return head;  // 若只有1个节点则直接返回

        int len = getLength(head);
        int numToRotate = k % len;          // 去掉套圈后需要移动的节点个数
        if (numToRotate == 0) return head;  // 若去掉套圈后没有需要移动的节点则直接返回

        int numToStay = len - numToRotate;  // 不需移动的节点个数
        ListNode prev = head;

        while (numToStay != 1) {            // 找到待截断链表的前一个节点
            prev = prev.next;
            numToStay--;
        }

        ListNode rest = prev.next, conn = rest;
        prev.next = null;                   // 截断

        while (conn.next != null)           // 获取待截断链表的最后一个节点
            conn = conn.next;
        conn.next = head;                   // 链接成新链表

        return rest;
    }

    private static int getLength(ListNode head) {
        int len = 0;
        for ( ; head != null; head = head.next) len++;
        return len;
    }

    /*
     * 解法2：截断移动（双指针版本）
     * - 思路：与解法1一致。
     * - 实现：“找到待截断链表的上一节点”的另一个思路是使用双指针技巧，例如已知待截取的节点个数为2：
     *       0 -> 1 -> 2 -> 3 -> 4
     *       l         r              - 设置两个指针 l,r，让 r 向右移动2步
     *       0 -> 1 -> 2 -> 3 -> 4
     *                 l         r    - 让 l,r 保持同时移动，当 r 到达尾节点时 l 一定停在截断点的上一节点。
     *       0 -> 1 -> 2    3 -> 4
     *       ↑___________________|    - 让 r.next = head；h = l.next；l.next = null 从而得到新链表 h。
     *                 l    h    r
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode rotateRight(ListNode head, int k) {
        if (head == null) return null;
        k %= getLength(head);

        ListNode l = head, r = head;
        for (int i = 0; i < k; i++)  // 让 l, r 相距 k 步
            r = r.next;

        while (r.next != null) {     // 同时移动双指针，直到 r 抵达尾节点（而非像 L19 中抵达 null）
            r = r.next;
            l = l.next;
        }

        r.next = head;               // 此时 l 一定停在截断点的上一节点，此时进行截断
        head = l.next;
        l.next = null;
        return head;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 2, 3, 4, 5});
        printLinkedList(rotateRight2(l1, 2));   // expects 4->5->1->2->3->NULL

        ListNode l2 = createLinkedList(new int[]{1, 2, 3, 4, 5});
        printLinkedList(rotateRight2(l2, 7));   // expects 4->5->1->2->3->NULL

        ListNode l3 = createLinkedList(new int[]{1, 2, 3, 4, 5});
        printLinkedList(rotateRight2(l3, 0));   // expects 1->2->3->4->5->NULL

        ListNode l4 = createLinkedList(new int[]{1});
        printLinkedList(rotateRight2(l4, 1));   // expects 1->NULL

        ListNode l5 = createLinkedList(new int[]{1});
        printLinkedList(rotateRight2(l5, 99));  // expects 1->NULL
    }
}
