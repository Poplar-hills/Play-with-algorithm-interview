package LinkedList;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
* Partition List
*
* - 根据给定值 x 将链表分割成两部分，所有 < x 的节点在前一部分，所有 ≥ x 的节点在后一部分。
*   Note you should preserve the original relative order of the nodes in each of the two partitions.
* */

public class L86_PartitionList {
    /*
    * 解法1：移动节点
    * - 思路：找到链表中的 joint 节点（第一个 ≥ x 的节点的前一个节点），并将后面所有 < x 的节点移动到 joint 之前。例如：
    *   1->2->2->4->3->5，x=3 中，joint 节点为第二个2，在继续向后遍历过程中将3节点移动到 joint 之后。
    * - 时间复杂度 O(n)，空间复杂度 O(1)。
    * */
    public static ListNode partition(ListNode head, int x) {
        ListNode dummyhead = new ListNode();
        dummyhead.next = head;

        ListNode joint = dummyhead, curr = head;  // joint 指向 dummyHead，统一正常与特殊情况的处理（SEE test case 2）
        while (curr != null && curr.val < x) {    // 找到 joint 节点
            joint = curr;
            curr = curr.next;
        }

        ListNode prev = joint;
        while (curr != null) {
            if (curr.val < x) {
                ListNode temp = curr.next;
                joint = insertNode(curr, joint);
                prev.next = temp;
                curr = temp;
            } else {
                prev = curr;
                curr = curr.next;
            }
        }

        return dummyhead.next;
    }

    private static ListNode insertNode(ListNode curr, ListNode p) {
        ListNode third = p.next;
        p.next = curr;
        curr.next = third;
        return curr;
    }

    /*
    * 解法2：双链表拼接
    * - 模拟很多函数库中的 partition 操作，在遍历链表过程中将 < x 的节点放到一个链表（before）上，讲 ≥ x 的节点放到另一个链表（after）上，
    *   最后将两个链表拼接在一起。
    * - 时间复杂度 O(n)，空间复杂度 O(1)。
    * */
    public static ListNode partition2(ListNode head, int x) {
        ListNode before = new ListNode(), beforeHead = before;
        ListNode after = new ListNode(), afterHead = after;
        ListNode curr = head;

        while (curr != null) {
            if (curr.val < x) {
                before.next = curr;
                before = before.next;
            } else {
                after.next = curr;
                after = after.next;
            }
            curr = curr.next;
        }

        after.next = null;  // 注意 after 的最后一个节点的 next 还链接着原来的节点，需要断开链接。
        before.next = afterHead.next;
        return beforeHead.next;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 4, 3, 2, 5, 2});  // 第一个元素 < x 的情况（此时 joint 存在）
        printLinkedList(partition2(l1, 3));  // expects 1->2->2->4->3->5->NULL

        ListNode l3 = createLinkedListFromArray(new int[]{2, 4, 1, 0, 3});  // 第一个元素就 ≥ x 的情况（此时 joint 不存在）
        printLinkedList(partition2(l3, 2));  // expects 1->0->2->4->3->NULL
    }
}
