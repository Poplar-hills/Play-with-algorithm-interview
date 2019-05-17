package LinkedList.AdvancedProblems;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
* Swap Nodes in Pairs
*
* - Given a linked list, swap every two adjacent nodes and return its head.（注意不能交换值）。
* */

public class L24_SwapNodesInPairs {
    /*
    * 解法1：
    * 过程：D -> 1 -> 2 -> 3 -> 4 -> 5 -> NULL
    *      p    c                               交换节点1和2，返回节点1，并赋给 p，c = p.next
    *      D -> 2 -> 1 -> 3 -> 4 -> 5 -> NULL
    *                p    c                     交换节点3和4，返回节点3，并赋给 p，c = p.next
    *      D -> 2 -> 1 -> 4 -> 3 -> 5 -> NULL
    *                          p    c           ∵ c.next == null，返回 c，并赋给 p，∵ p.next == null，c = null
    *      D -> 2 -> 1 -> 4 -> 3 -> 5 -> NULL
    *                               p     c     ∵ c == null，循环结束
    * */
    public static ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode dummyHead = new ListNode();
        dummyHead.next = head;
        ListNode prev = dummyHead, curr = head;

        while (curr != null) {
            prev = swapNodes(prev, curr, curr.next);
            if (prev.next != null) curr = prev.next;
            else curr = null;
        }

        return dummyHead.next;
    }

    private static ListNode swapNodes(ListNode prev, ListNode curr, ListNode next) {  // swap curr and next
        if (next == null) return curr;
        ListNode temp = next.next;
        next.next = curr;
        curr.next = temp;
        prev.next = next;
        return curr;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 2, 3, 4});
        printLinkedList(swapPairs(l1));  // expects 2->1->4->3->NULL

        ListNode l2 = createLinkedListFromArray(new int[]{1, 2, 3, 4, 5});
        printLinkedList(swapPairs(l2));  // expects 2->1->4->3->5->NULL

        ListNode l3 = createLinkedListFromArray(new int[]{1});
        printLinkedList(swapPairs(l3));  // expects 1->NULL
    }
}
