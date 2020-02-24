package LinkedList.S5_DoublePointer;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
 * Rotate List
 *
 * - Given a linked list, rotate the list to the right by k places, where k is non-negative (旋转链表，将链表每个节点
 *   向右移动 k 个位置).
 * */

public class L61_RotateList {
    /*
     * 解法1：双指针
     * - 思路：要得到 rotate 之后的链表，关键在于要确定从哪里截断并链到头节点上去。例如 0->1->2->3->4，k=7，rotate 之后
     *   得到 3->4->0->1->2，即从原链表2节点之后截断，并把截断的部分链回0节点上去。而要确定从哪里截断需要：
     *   1. 求去掉绕圈后的剩余距离，即 k % len = 2，说明链表中的各节点都需移动2步才能到达 rotate 之后他们各自的位置，也说明原
     *      链表中最后2个节点会被截断并链回头结点。至此问题转化成了类似 L19 中的问题 ——
     *   2. 已知截断点距离链表尾节点为2，求截断后链回头结点的链表。这个问题就可以应用双指针技巧：
     *       0 -> 1 -> 2 -> 3 -> 4
     *       l         r            - 设置两个指针 l, r，让 r 向右移动2步
     *       0 -> 1 -> 2 -> 3 -> 4
     *                 l         r  - 让 l, r 保持同时移动，直至 r 到达链表尾节点，此时 l 一定停在截断点的上一节点。
     *       0 -> 1 -> 2    3 -> 4
     *       ↑___________________|  - 让 r.next = head；h = l.next；l.next = null 从而得到新链表 h。
     *                 l    h    r
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode rotateRight(ListNode head, int k) {
        if (head == null) return null;
        k %= getLength(head);          // 求余以获取去掉绕圈后的剩余距离（常用技巧）

        ListNode left = head, right = head;
        for (int i = 0; i < k; i++)    // 让 right, left 相距 k 步
            right = right.next;

        while (right.next != null) {   // 同时移动双指针，直到 right 抵达尾节点（而非像 L19 中抵达 null）
            right = right.next;
            left = left.next;
        }

        right.next = head;             // 此时 left 一定停在截断点的上一节点，可以进行截断
        head = left.next;
        left.next = null;
        return head;
    }

    private static int getLength(ListNode head) {
        int len = 0;
        for (ListNode p = head; p != null; p = p.next, len++);
        return len;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 2, 3, 4, 5});
        printLinkedList(rotateRight(l1, 2));  // expects 4->5->1->2->3->NULL

        ListNode l2 = createLinkedList(new int[]{0, 1, 2, 3, 4});
        printLinkedList(rotateRight(l2, 7));  // expects 3->4->0->1->2->NULL
    }
}
