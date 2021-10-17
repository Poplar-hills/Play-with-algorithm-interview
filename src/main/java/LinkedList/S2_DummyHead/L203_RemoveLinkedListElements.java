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
            if (curr.val == val) continue;
            prev.next = new ListNode(curr.val);  // 创建新节点
            prev = prev.next;
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
     * 解法3：解法2的递归版
     * - 实现：在递归去程的路上执行处理逻辑。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode removeElements4(ListNode head, int val) {
        if (head == null) return null;
        if (head.val == val) return removeElements4(head.next, val);  // 若当前节点是待删除节点则直接跳过，处理下一个节点
        head.next = removeElements4(head.next, val);                  // 若非待删除节点则正常处理
        return head;
    }

    /*
     * 解法4：解法3的改进版
     * - 实现：在递归回程的路上执行处理逻辑。
     * - 👉比较：解法3、4都适用递归，但一个在去程、一个回程时执行业务逻辑。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode removeElements5(ListNode head, int val) {
        if (head == null) return null;
        head.next = removeElements4(head.next, val);
        return head.val == val ? head.next : head;
    }

    public static void main(String[] args) {
        ListNode l = createLinkedList(new int[]{1, 2, 6, 3, 4, 5, 6});
        log(removeElements(l, 6));  // expects 1->2->3->4->5->NULL  //TODO 无限循环

        ListNode l2 = createLinkedList(new int[]{6, 6});
        log(removeElements(l2, 6));  // expects NULL
    }
}
