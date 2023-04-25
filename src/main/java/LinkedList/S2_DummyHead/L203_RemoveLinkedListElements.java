package LinkedList.S2_DummyHead;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
 * Remove Linked List Elements
 *
 * - Remove all elements from a linked list of integers that have value val.
 * */

public class L203_RemoveLinkedListElements {
    /*
     * 解法1：创建新节点和新链表
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode removeElements(ListNode head, int val) {
        ListNode dummyHead = new ListNode();
        ListNode prev = dummyHead, curr = head;

        while (curr != null) {
            if (curr.val != val) {
                prev.next = new ListNode(curr.val);  // 创建新节点
                prev = prev.next;
            }
            curr = curr.next;
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
     * 解法3：解法3的简化版
     * - 实现：在递归回程的路上执行处理逻辑。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode removeElements3(ListNode head, int val) {
        if (head == null) return null;
        head.next = removeElements3(head.next, val);
        return head.val == val ? head.next : head;
    }

    public static void main(String[] args) {
        ListNode l = createLinkedList(new int[]{1, 2, 6, 3, 4, 5, 6});
        log(removeElements3(l, 6));  // expects 1->2->3->4->5->NULL

        ListNode l2 = createLinkedList(new int[]{6, 6});
        log(removeElements3(l2, 6));  // expects NULL
    }
}
