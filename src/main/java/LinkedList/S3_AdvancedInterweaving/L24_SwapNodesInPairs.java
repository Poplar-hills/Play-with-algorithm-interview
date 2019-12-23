package LinkedList.S3_AdvancedInterweaving;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
* Swap Nodes in Pairs
*
* - Given a linked list, swap every two adjacent nodes and return its head.（注意不能交换值）。
* */

public class L24_SwapNodesInPairs {
    /*
    * 解法1：遍历
    * - 思路：要交换两个节点需要有指针指向这两个节点之前和之后的节点，这样交换完之后才能链回去。因此 swapNodes 方法的输入
    *        参数应该是待交换节点的前一个节点（它后面的三个节点都能通过它访问到）。但因为链表头结点前面没有更多节点，因此可
    *        以设置虚拟一个头结点以便于交换链表的前两个节点。
    * - 过程：D -> 1 -> 2 -> 3 -> 4 -> 5 -> NULL
    *        p    c                               交换节点1和2，返回节点1，并赋给 p，c = p.next
    *        D -> 2 -> 1 -> 3 -> 4 -> 5 -> NULL
    *                  p    c                     交换节点3和4，返回节点3，并赋给 p，c = p.next
    *        D -> 2 -> 1 -> 4 -> 3 -> 5 -> NULL
    *                            p    c           ∵ c.next == null，返回 c，并赋给 p，∵ p.next == null，c = null
    *        D -> 2 -> 1 -> 4 -> 3 -> 5 -> NULL
    *                                 p     c     ∵ c == null，循环结束
    * - 时间复杂度 O(n)，空间复杂度 O(1)。
    * */
    public static ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode dummyHead = new ListNode();
        dummyHead.next = head;
        ListNode prev = dummyHead;

        while (prev.next != null)
            prev = swapNodes(prev);  // 返回交换后的最后一个节点并赋给 prev（相当于让 prev 向后移两步）

        return dummyHead.next;
    }

    private static ListNode swapNodes(ListNode prev) {  // 交换 curr 和 next 并返回交换后的最后一个节点
        ListNode first = prev.next, second = prev.next.next;
        if (second == null) return first;
        ListNode temp = second.next;
        second.next = first;
        first.next = temp;
        prev.next = second;  // 把交换完的链表链回上一个节点
        return first;
    }

    /*
    * 解法2：递归
    * - 思路：1 -> 2 -> 3 -> 4 -> 5 -> NULL
    *                          ← 5->N
    *                ← 4->3->5->N
    *      ← 2->1->4->3->5->N
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static ListNode swapPairs2(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode first = swapNodes2(head), second = first.next;
        if (second != null)
            second.next = swapPairs(second.next);
        return first;
    }

    private static ListNode swapNodes2(ListNode first) {  // 交换 first 和 second 并返回交换后的第一个节点（∵ 是用于递归 ∴ 不需要提供 first 的上一个节点）
        ListNode second = first.next;
        if (second == null) return first;
        ListNode temp = second.next;
        second.next = first;
        first.next = temp;
        return second;
    }

    /*
    * 解法3：解法2的精简版
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static ListNode swapPairs3(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode second = head.next;
        head.next = swapPairs3(second.next);
        second.next = head;
        return second;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 2, 3, 4});
        printLinkedList(swapPairs3(l1));  // expects 2->1->4->3->NULL

        ListNode l2 = createLinkedListFromArray(new int[]{1, 2, 3, 4, 5});
        printLinkedList(swapPairs3(l2));  // expects 2->1->4->3->5->NULL

        ListNode l3 = createLinkedListFromArray(new int[]{1});
        printLinkedList(swapPairs3(l3));  // expects 1->NULL
    }
}
