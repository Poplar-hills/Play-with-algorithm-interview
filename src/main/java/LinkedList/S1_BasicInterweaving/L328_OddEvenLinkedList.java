package LinkedList.S1_BasicInterweaving;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
 * Odd Even Linked List
 *
 * - Given a singly linked list, group all odd-index nodes together followed by the even-index nodes.
 * - 注意：索引从1开始。
 * */

public class L328_OddEvenLinkedList {
    /*
     * 解法1：移动节点
     * - 思路：同 L86 的解法1。
     * - 时间复杂度 O(n)，空间复杂度 O(1)，不需要开辟额外空间。
     * */
    public static ListNode oddEvenList(ListNode head) {
        ListNode dummyHead = new ListNode();
        dummyHead.next = head;

        ListNode joint = dummyHead, curr = head;
        int i = 1;
        while (curr != null && i % 2 != 0) {  // 先找到链表中第一个偶数索引的节点的前一个节点（joint 节点）
            joint = curr;
            curr = curr.next;
            i++;
        }

        ListNode prev = joint;
        while (curr != null) {  // 继续向后遍历，每次遇到偶数索引的节点就将它插入到 joint 处，并让 joint 后移
            if (i % 2 != 0) {
                ListNode temp = curr.next;
                joint = insertNode(curr, joint);
                prev.next = temp;
                curr = temp;
            } else {
                prev = curr;
                curr = curr.next;
            }
            i++;
        }

        return dummyHead.next;
    }

    private static ListNode insertNode(ListNode node, ListNode prev) {  // 将 node 插到 prev 之后
        ListNode third = prev.next;
        prev.next = node;
        node.next = third;
        return node;
    }

    /*
     * 解法2：双链表拼接
     * - 思路：同 L86 的解法2。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode oddEvenList2(ListNode head) {
        ListNode odd = new ListNode(), oddHead = odd;
        ListNode even = new ListNode(), evenHead = even;
        ListNode curr = head;

        for (int i = 1; curr != null; i++, curr = curr.next) {
            if (i % 2 != 0) {
                odd.next = curr;
                odd = odd.next;
            } else {
                even.next = curr;
                even = even.next;
            }
        }

        even.next = null;
        odd.next = evenHead.next;
        return oddHead.next;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 2, 3, 4, 5});
        printLinkedList(oddEvenList(l1));  // expects 1->3->5->2->4->NULL

        ListNode l2 = createLinkedList(new int[]{2, 1, 3, 5, 6, 4, 7});
        printLinkedList(oddEvenList(l2));  // expects 2->3->6->7->1->5->4->NULL
    }
}
