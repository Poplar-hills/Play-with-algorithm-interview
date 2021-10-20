package LinkedList.S1_BasicInterweaving;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
 * Odd Even Linked List
 *
 * - Given a singly linked list, group all odd-index nodes together followed by the even-index nodes.
 * - 注意：索引从1开始。
 * */

public class L328_OddEvenLinkedList {
    /*
     * 解法1：In-place Insertion
     * - 思路：与 L86_PartitionList 解法1类似 ∵ 题目要求所有奇数索引的节点放在偶数索引的节点之前 ∴ 总体思路是在遍历过程中将
     *   所有跳过偶数索引的节点，将奇数索引的节点插入到链表的头部。这就需要一个指针（lastOdd）用来标记最后一个奇数索引的节点。
     *   在遍历过程中每当遇到奇数索引节点时，就将其插入到 lastOdd 后面，并让 lastOdd 后移一步。
     * - 演示：  1 -> 2 -> 3 -> 4 -> 5 -> null
     *      lastOdd curr                         - 2为偶数索引节点，跳过
     *          1 -> 2 -> 3 -> 4 -> 5 -> null
     *      lastOdd      curr                    - 3为奇数索引节点，插入到 lastOdd 后面
     *          1 -> 3 -> 2 -> 4 -> 5 -> null
     *            lastOdd     curr               - 4为偶数索引节点，跳过
     *          1 -> 3 -> 2 -> 4 -> 5 -> null
     *            lastOdd          curr          - 5为奇数索引节点，插入到 lastOdd 后面
     *          1 -> 3 -> 5 -> 2 -> 4 -> null
     *                 lastOdd           curr
     * - 时间复杂度 O(n)，空间复杂度 O(1)，不需要开辟额外空间。
     * */
    public static ListNode oddEvenList(ListNode head) {
        if (head == null || head.next == null) return head;

        ListNode lastOdd = head;                 // 第1个节点的索引为奇数 ∴ lastOdd 初始就是该节点
        ListNode prev = head, curr = head.next;  // ∵ 后面要移动 curr ∴ 需要维护 curr 的前一个节点 prev

        for (int i = 2; curr != null; i++) {     // 第2个节点继续向后遍历
            if (i % 2 == 0) {                    // 偶数索引的节点跳过
                prev = curr;
                curr = curr.next;
            } else {                             // 奇数索引的节点插入到 lastOdd 后面，并更新 lastOdd
                ListNode next = curr.next;
                lastOdd = insertNode(curr, lastOdd);
                prev.next = next;                // 将 curr 移走之后 prev 要链到一下个节点 next 上去
                curr = next;
            }
        }

        return head;
    }

    private static ListNode insertNode(ListNode node, ListNode prev) {  // 将 node 插到 prev 之后，并返回 node 节点
        ListNode third = prev.next;
        prev.next = node;
        node.next = third;
        return node;
    }

    /*
     * 解法2：双链表拼接
     * - 思路：同 L86_PartitionList 解法2。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode oddEvenList2(ListNode head) {
        if (head == null) return null;
        ListNode oddDummyHead = new ListNode(), currOdd = oddDummyHead;
        ListNode evenDummyHead = new ListNode(), currEven = evenDummyHead;

        int i = 1;
        for (ListNode curr = head; curr != null; curr = curr.next) {
            if (i % 2 != 0) {
                currOdd.next = curr;
                currOdd = currOdd.next;
            } else {
                currEven.next = curr;
                currEven = currEven.next;
            }
            i++;
        }
        currOdd.next = evenDummyHead.next;
        currEven.next = null;
        return oddDummyHead.next;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 2, 3, 4, 5});
        log(oddEvenList(l1));  // expects 1->3->5->2->4->NULL

        ListNode l2 = createLinkedList(new int[]{2, 1, 3, 5, 6, 4, 7});
        log(oddEvenList(l2));  // expects 2->3->6->7->1->5->4->NULL
    }
}
