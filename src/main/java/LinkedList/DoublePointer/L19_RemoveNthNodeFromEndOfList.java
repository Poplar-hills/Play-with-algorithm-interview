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
    * - ∵ 需要遍历两遍 ∴ 是 two pass algorithm。
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

    /*
     * 解法2：双指针同时移动
     * - 思路：对于 1 -> 2 -> 3 -> 4 -> 5 -> NULL，n=2 来说：
     *   1. 要删除的节点是4，则需获取节点3，因此需要一个指针最终指向3；
     *   2. n=2 是指从后往前数2个节点，即待删除节点与链表尾部的索引差是固定的 n。又因为 n 是从1开始，因此节点4与 NULL 的索引
     *      差固定是 n，因此可知节点3与 NULL 的索引差固定是 n+1。
     *   3. 利用这个固定的索引差即可从链表头部找定位到待删除节点的前一节点（见实现）。
     * - 时间复杂度 O(n)，空间复杂度 O(1)（虽然时间复杂度也是 O(n)，但只遍历一遍 ∴ 是 one pass algorithm，比解法1更优）。
     * */
    public static ListNode removeNthFromEnd2(ListNode head, int n) {
        if (head == null || n < 0) return head;

        ListNode dummnyHead = new ListNode(), left = dummnyHead, right = dummnyHead;  // 要删除节点就一定需要虚拟头结点
        dummnyHead.next = head;
        while (n + 1 != 0) {     // 先将 right 移动到距离 left 指针 n+1 位置的地方
            if (right == null) return head;
            right = right.next;
            n--;
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
        printLinkedList(removeNthFromEnd2(l3, 2));  // expects 1->NULL

        ListNode l4 = createLinkedListFromArray(new int[]{});
        printLinkedList(removeNthFromEnd2(l4, 2));  // expects NULL
    }
}
