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
    * 解法1：找到 joint 节点（第一个 ≥ x 的节点的前一个节点），并将后面所有 < x 的节点移动到 joint 之前
    * - 例如：1->2->2->4->3->5，x=3 中，joint 节点为第二个2，在继续向后遍历过程中将3节点移动到 joint 之后。
    * */
    public static ListNode partition(ListNode head, int x) {
        ListNode dummyhead = new ListNode();
        dummyhead.next = head;

        ListNode joint = dummyhead, curr = head;  // joint 节点初始化为空节点，统一正常与特殊情况的处理（SEE test case 2）
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

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 4, 3, 2, 5, 2});
        printLinkedList(partition(l1, 3));  // expects 1->2->2->4->3->5

        ListNode l3 = createLinkedListFromArray(new int[]{2, 4, 1, 0, 3});  // 第一个元素就 ≥ x
        printLinkedList(partition(l3, 2));  // expects 1->0->2->4->3
    }
}
