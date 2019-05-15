package LinkedList.DummyHead;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
* Remove Linked List Elements
*
* - Remove all elements from a linked list of integers that have value val.
* */

public class L203_RemoveLinkedListElements {
    /*
    * 解法1：创建新链表
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static ListNode removeElements(ListNode head, int val) {
        ListNode dummyHead = new ListNode();

        for (ListNode curr = head, curr2 = dummyHead; curr != null; curr = curr.next) {
            if (curr.val == val) continue;
            curr2.next = new ListNode(curr.val);
            curr2 = curr2.next;
        }

        return dummyHead.next;
    }

    /*
     * 解法2：修改原有链表
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode removeElements2(ListNode head, int val) {
        ListNode dummyHead = new ListNode();
        dummyHead.next = head;
        ListNode prev = dummyHead, curr = head;

        while (curr != null) {
            if (curr.val == val)
                prev.next = curr.next;  // 跳过值为 val 的节点
            else
                prev = prev.next;
            curr = curr.next;
        }

        return dummyHead.next;
    }

    /*
    * 解法3：解法2的优化版（只是用 curr 一个变量）
    * - 时间复杂度 O(n)，空间复杂度 O(1)。
    * */
    public static ListNode removeElements3(ListNode head, int val) {
        ListNode dummyHead = new ListNode();
        dummyHead.next = head;
        ListNode curr = dummyHead;

        while (curr.next != null) {
            if (curr.next.val == val)
                curr.next = curr.next.next;
            else
                curr = curr.next;
        }

        return dummyHead.next;
    }

    public static void main(String[] args) {
        ListNode l = createLinkedListFromArray(new int[]{1, 2, 6, 3, 4, 5, 6});
        printLinkedList(removeElements3(l, 6));
    }
}
