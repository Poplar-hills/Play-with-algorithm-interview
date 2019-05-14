package LinkedList;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
* Add Two Numbers II
*
* - 给出两个非空链表，代表两个非负整数。其中每个整数的各个位上的数字以顺序存储，返回这两个整数之和的顺序链表。
*   如 7243 + 564 = 7807，则给出 7—>2->4->3、5->6->4，返回 7->8->0->7。
* */

public class L445_AddTwoNumbersII {
    /*
    * 解法1：先将链表反向，再用 L2 中解法3的思路求和，最后再反向。
    * - 时间复杂度 O(max(m,n))，空间复杂度 O(max(m,n))。
    * */
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode reverseL1 = reverse(l1);  // O(m)
        ListNode reverseL2 = reverse(l2);  // O(n)
        ListNode reversedSum = addTwoNumbers(reverseL1, reverseL2, 0);  // O(max(m,n))
        return reverse(reversedSum);       // O(max(m,n))
    }

    private static ListNode reverse(ListNode l1) {  // 将一个链表反向
        ListNode prev = null, curr = l1;
        while (curr != null) {
            ListNode third = curr.next;
            curr.next = prev;
            prev = curr;
            curr = third;
        }
        return prev;
    }

    private static ListNode addTwoNumbers(ListNode l1, ListNode l2, int carry) {
        if (l1 == null && l2 == null && carry == 0)
            return null;

        int l1Val = l1 != null ? l1.val : 0;
        int l2Val = l2 != null ? l2.val : 0;
        int sum = l1Val + l2Val + carry;

        carry = sum / 10;
        ListNode s = new ListNode(sum % 10);

        ListNode l1Next = l1 == null ? null : l1.next;
        ListNode l2Next = l2 == null ? null : l2.next;
        s.next = addTwoNumbers(l1Next, l2Next, carry);

        return s;
    }

    /*
    * 解法2：
    * */
    public static ListNode addTwoNumbers2(ListNode l1, ListNode l2) {

    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{7, 2, 4, 3});
        ListNode l2 = createLinkedListFromArray(new int[]{5, 6, 4});
        printLinkedList(addTwoNumbers(l1, l2));   // expects 7->8->0->7->NULL
    }
}
