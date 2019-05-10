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
    public static ListNode partition(ListNode head, int x) {
        return head;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 4, 3, 2, 5, 2});
        printLinkedList(partition(l1, 3));  // expects 1->2->2->4->3->5
    }
}
