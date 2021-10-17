package LinkedList.S1_BasicInterweaving;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
 * Remove Duplicates from Sorted List
 *
 * - 为有序链表去重。
 * */

public class L83_RemoveDuplicatesFromSortedList {
    /*
     * 解法1：迭代
     * - 思路：当发现下一个节点与当前节点重复时，跳过下一个节点。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode deleteDuplicates(ListNode head) {
        ListNode curr = head;
        while (curr != null && curr.next != null) {
            if (curr.val == curr.next.val)
                curr.next = curr.next.next;  // 跳过 curr.next 节点后无需再移动 curr（test case 2）
            else
                curr = curr.next;            // 若没有跳过节点则需要移动 curr
        }
        return head;
    }

    /*
     * 解法2：递归（解法1的递归版）
     * - 思路：在递归去程路上，若发现下一个节点与当前节点重复时，跳过下一个节点。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode deleteDuplicates2(ListNode head) {
        helper2(head);
        return head;  // ∵ 在递归去程路上跳过下一个节点就无法返回链表头结点了 ∴ 递归返回类型为 void，并需要在外部单独返回头结点
    }
    
    public static void helper2(ListNode head) {
        if (head == null || head.next == null) return;
        if (head.val == head.next.val) {
            head.next = head.next.next;
            helper2(head);
        } else {
            helper2(head.next);
        }
    }

    /*
     * 解法3：递归
     * - 思路：在递归回程路上，若发现下一个节点与当前节点重复时，跳过当前节点。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode deleteDuplicates3(ListNode head) {
        if (head == null || head.next == null) return head;
        head.next = deleteDuplicates3(head.next);             // 先递归到底
        return head.val == head.next.val ? head.next : head;  // 回程路上跳过/不跳过当前节点
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 1, 2});
        log(deleteDuplicates2(l1));  // expects 1->2->NULL

        ListNode l2 = createLinkedList(new int[]{0, 1, 1, 1, 2, 3, 3});
        log(deleteDuplicates2(l2));  // expects 0->1->2->3->NULL
    }
}
