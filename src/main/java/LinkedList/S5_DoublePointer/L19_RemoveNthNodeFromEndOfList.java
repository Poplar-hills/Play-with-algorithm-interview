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
    * 解法1：两次遍历
    * - 思路：先求得链表长度，再找到并删除目标节点。
    * - 时间复杂度 O(n)，空间复杂度 O(1)。
    * - 不足：∵ 需要遍历两遍 ∴ 是 two pass algorithm。
    * */
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        if (head == null || n < 0) return head;

        int len = 0;
        for (ListNode curr = head; curr != null; curr = curr.next)  // 取得链表总长度
            len++;
        if (n > len) return head;  // 处理 n 越界的情况

        ListNode dummyHead = new ListNode();
        ListNode prev = dummyHead, curr = head;
        dummyHead.next = head;

        while (len != n) {      // 找到指定节点（这种方法很巧，通过让 len-- 并与 n 比较来找到倒数第 n 个节点）
            len--;
            prev = curr;
            curr = curr.next;
        }

        prev.next = curr.next;  // 删除（跳过）n 处的节点
        return dummyHead.next;
    }

    /*
     * 解法2：双指针 + 一次遍历
     * - 思路：通过双指针同时移动找到待删除节点的上一节点。例如对于 1->2->3->4->5->NULL，n=2 来说：
     *   1. 要删除的节点是4，因此只要获得节点3即可完成删除；
     *   2. 节点3与 NULL 的差距是 n+1，因此可以设置两个指针，并让他们的初始差距为 n+1，然后同时移动当右指针到达 NULL 时，左指针即
     *      到达待删除节点的上一节点；
     * - 时间复杂度 O(n)，空间复杂度 O(1)（∵ 但只遍历一遍 ∴ 是 one pass algorithm，比解法1更优）。
     * */
    public static ListNode removeNthFromEnd2(ListNode head, int n) {
        if (head == null || n < 0) return head;

        ListNode dummnyHead = new ListNode();            // ∵ 待删除的可能是第一个节点 ∴ 需要 dummyHead
        ListNode left = dummnyHead, right = dummnyHead;  // 要删除节点就一定需要虚拟头结点
        dummnyHead.next = head;

        for (int i = 0; i < n + 1; i++) {     // 先将 right 移动到距离 left 指针 n+1 位置的地方
            if (right == null) return head;   // 处理 n 越界的情况
            right = right.next;
        }

        while (right != null) {  // 同时移动两个指针，当 right 到达 null 时，left 就到了待删除节点的前一节点上了
            left = left.next;
            right = right.next;
        }

        left.next = left.next.next;  // 删除节点
        return dummnyHead.next;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 2, 3, 4, 5});
        printLinkedList(removeNthFromEnd2(l1, 2));  // expects 1->2->3->5->NULL

        ListNode l2 = createLinkedListFromArray(new int[]{1, 2, 3});
        printLinkedList(removeNthFromEnd2(l2, 3));  // expects 2->3->NULL

        ListNode l3 = createLinkedListFromArray(new int[]{1});
        printLinkedList(removeNthFromEnd2(l3, 2));  // expects 1->NULL (n 越界的 case)

        ListNode l4 = createLinkedListFromArray(new int[]{});
        printLinkedList(removeNthFromEnd2(l4, 2));  // expects NULL
    }
}
