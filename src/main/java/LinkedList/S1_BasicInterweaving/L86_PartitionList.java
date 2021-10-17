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
     * - 思路：总体思路是仿照快速排序的方式将所有 < x 的节点移动到链表头部 ∴ 要先找到链表中连续 < x 的最后一个节点，并在往后
     *   遍历过程中，每当遇到 < x 的节点，就将其插入到该节点后面。例如：1->2->4->1->5->0，x=3 中，连续 < x 的最后一个节点
     *   是2，则在继续向后遍历过程中，将第二个1插入到2后面、将0插入到第二个1后面 ∴ 需要一个指针指向最后一个连续 < x 的节点。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode partition(ListNode head, int x) {
        ListNode dummyHead = new ListNode();  // 用 dummyHead 来统一正常与特殊情况的处理（SEE test case 2）
        dummyHead.next = head;

        ListNode lastLtX = dummyHead, curr = head;
        while (curr != null && curr.val < x) {  // 先找到最后一个 < x 的节点标记为 lastLtX
            lastLtX = curr;
            curr = curr.next;
        }

        ListNode prev = lastLtX;  // 从 lastLtX 节点继续遍历
        while (curr != null) {
            if (curr.val < x) {
                ListNode next = curr.next;
                lastLtX = insertNode(curr, lastLtX);  // 将 curr 插入到 lastLtX 后面，并更新 lastLtX 的指向
                prev.next = next;                     // 让 prev 断开与 curr 的链接，链到下个节点上
                curr = next;
            } else {              // 若节点值 ≥ x 则直接跳过
                prev = curr;
                curr = curr.next;
            }
        }

        return dummyHead.next;
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
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode partition2(ListNode head, int x) {
        ListNode dummyHead1 = new ListNode(), prev1 = dummyHead1;
        ListNode dummyHead2 = new ListNode(), prev2 = dummyHead2;

        for (; head != null; head = head.next) {
            if (head.val < x) {
                prev1.next = head;
                prev1 = prev1.next;
            } else {
                prev2.next = head;
                prev2 = prev2.next;
            }
        }

        prev1.next = dummyHead2.next;
        prev2.next = null;  // 注意 prev2 的最后一个节点的 next 可能还链接着原来的节点，需断开链接
        return dummyHead1.next;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 4, 3, 2, 5, 2});
        log(partition(l1, 3));  // expects 1->2->2->4->3->5->NULL（第1个元素 < x 的情况）

        ListNode l2 = createLinkedList(new int[]{2, 4, 1, 0, 3});
        log(partition(l2, 2));  // expects 1->0->2->4->3->NULL（第1个元素就 ≥ x 的情况，此时 lastLtX 不存在）
    }
}
