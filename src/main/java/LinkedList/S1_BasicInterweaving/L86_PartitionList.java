package LinkedList.S1_BasicInterweaving;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
 * Partition List
 *
 * - 根据给定值 x 将链表分割成两部分，所有 < x 的节点在前一部分，所有 ≥ x 的节点在后一部分。
 * - Note you should preserve the original relative order of the nodes in each of the two partitions.
 * */

public class L86_PartitionList {
    /*
     * 解法1：In-place Insertion
     * - 思路：总体思路是仿照快速排序的方式将所有 < x 的节点移动到链表头部。具体思路是，先找到链表中最后一个 < x 的节点，并在
     *   往后遍历过程中，每当遇到 < x 的节点，就将其插入到该节点后面。例如：1->2->4->3->5->0，x=3 中，最后一个 < x 的节点
     *   是2，则在继续向后遍历过程中，将3插入到2后面、将0插入到3后面。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode partition(ListNode head, int x) {
        ListNode dummyhead = new ListNode();          // 用 dummyHead 来统一正常与特殊情况的处理（SEE test case 2）
        dummyhead.next = head;

        ListNode lastLtX = dummyhead, curr = head;
        while (curr != null && curr.val < x) {        // 先找到最后一个 < x 的节点
            lastLtX = curr;
            curr = curr.next;
        }

        ListNode prev = lastLtX;                      // 从 lastLtX 节点继续遍历
        while (curr != null) {
            if (curr.val < x) {
                ListNode next = curr.next;
                lastLtX = insertNode(curr, lastLtX);  // 将 curr 插入到 lastLtX 后面，并更新 lastLtX 的指向
                prev.next = next;                     // 让 prev 断开与 curr 的链接，链到下个节点上
                curr = next;
            } else {                                  // 若节点值 ≥ x 则直接跳过
                prev = curr;
                curr = curr.next;
            }
        }

        return dummyhead.next;
    }

    private static ListNode insertNode(ListNode node, ListNode prev) {  // 将 node 插入 prev 之后，返回 node 节点
        ListNode next = prev.next;
        prev.next = node;
        node.next = next;
        return node;
    }

    /*
     * 解法2：双链表拼接
     * - 思路：在遍历链表过程中将 < x 的节点放到一个链表上；将 ≥ x 的节点放到另一个链表上，最后将两个链表拼接在一起。
     * - 实现：这也是很多工具函数库中 partition 的实现方式。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode partition2(ListNode head, int x) {
        ListNode l1 = new ListNode(), dummyHead1 = l1;
        ListNode l2 = new ListNode(), dummyHead2 = l2;

        while (head != null) {
            if (head.val < x) {
                l1.next = head;
                l1 = l1.next;
            } else {
                l2.next = head;
                l2 = l2.next;
            }
            head = head.next;
        }

        l2.next = null;  // 注意 l2 的最后一个节点的 next 还链接着原来的节点，需要断开链接。
        l1.next = dummyHead2.next;
        return dummyHead1.next;
    }

    /*
     *   D -> 1 -> 2 -> 2 -> 4 -> 3 -> 5 -> NULL
     *   p    c
     *   l
     *
     *
     *
     * */
    public static ListNode partition0(ListNode head, int x) {
        ListNode dummyHead = new ListNode();
        dummyHead.next = head;
        ListNode prev = dummyHead, curr = head, lastLtX = dummyHead;

        while (curr != null) {
            if (curr.val >= x) {
                prev = curr;
                curr = curr.next;
            } else {
                prev.next = curr.next;
                insert2(lastLtX, curr);
                curr = prev.next;
            }
        }

        return head;
    }

    private static void insert2(ListNode prev, ListNode node) {
        ListNode next = prev.next;
        prev.next = node;
        node.next = next;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 4, 3, 2, 5, 2});
        printLinkedList(partition(l1, 3));  // expects 1->2->2->4->3->5->NULL（第1个元素 < x 的情况，此时 joint 存在）

        ListNode l2 = createLinkedList(new int[]{2, 4, 1, 0, 3});
        printLinkedList(partition(l2, 2));  // expects 1->0->2->4->3->NULL（第1个元素就 ≥ x 的情况，此时 joint 不存在）
    }
}
