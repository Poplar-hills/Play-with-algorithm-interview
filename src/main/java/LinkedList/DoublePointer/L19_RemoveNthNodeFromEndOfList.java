package LinkedList.DoublePointer;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
* Remove Nth Node From End of List
*
* - 删除给定链表的倒数第 n 个节点（n 从1开始）。
* */

public class L19_RemoveNthNodeFromEndOfList {
    /*
    * 解法1：取链表长度，再删除指定节点。
    * - 时间复杂度 O(n)，空间复杂度 O(1)。
    * */
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        if (head == null || n < 0) return head;

        int len = 0;
        for (ListNode curr = head; curr != null; curr = curr.next)  // 取得链表总长度
            len++;
        if (n > len) return head;  // 处理 n 越界的情况

        ListNode dummyHead = new ListNode(), prev = dummyHead, curr = head;
        dummyHead.next = head;
        while (len != n) {      // 找到指定节点
            prev = curr;
            curr = curr.next;
            len--;
        }

        prev.next = curr.next;  // 删除指定节点
        return dummyHead.next;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 2, 3, 4, 5});
        printLinkedList(removeNthFromEnd(l1, 2));  // expects  1->2->3->5->NULL

        ListNode l2 = createLinkedListFromArray(new int[]{1, 2, 3});
        printLinkedList(removeNthFromEnd(l2, 3));  // expects  2->3->NULL

        ListNode l3 = createLinkedListFromArray(new int[]{1});
        printLinkedList(removeNthFromEnd(l3, 2));  // expects  1->NULL
    }
}
