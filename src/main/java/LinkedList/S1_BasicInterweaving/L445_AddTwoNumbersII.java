package LinkedList.S1_BasicInterweaving;

import Utils.Helpers.ListNode;

import java.util.Stack;

import static Utils.Helpers.*;

/*
 * Add Two Numbers II
 *
 * - 给出两个非空链表，代表两个非负整数。其中每个整数的各个位上的数字以顺序存储（此处于 L2 不同，L2 是逆序存储），返回这两个整数
 *   之和的顺序链表。如 243 + 1564 = 1807，则给出 2->4->3、1—>5->6->4，返回 1->8->0->7。
 * */

public class L445_AddTwoNumbersII {
    /*
     * 解法1：先将链表反向，再模拟加法运算，最后再反向。
     * - 思路：本题与 L2 的不同点在于输入、输出都是顺序链表，因此在模拟加法运算之前需要解决数位对应的问题（个位与个位相加，十位
     *   与十位相加……）∴ 可以先将两个链表反向，再对这两个逆序链表求和，此时求和过程就是从个位开始相加，不存在数位对应问题。
     * - 实现：求和过程采用 L2 解法3的实现；反向过程采用 L206 解法1的实现。
     * - 时间复杂度 O(max(m,n))，空间复杂度 O(max(m,n))。
     * */
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode reversedL1 = reverse(l1);  // O(m)
        ListNode reversedL2 = reverse(l2);  // O(n)
        ListNode reversedSum = addTwoNumbers(reversedL1, reversedL2, 0);  // O(max(m,n))
        return reverse(reversedSum);        // O(max(m,n))
    }

    private static ListNode reverse(ListNode head) {
        ListNode prev = null, curr = head;
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
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
        ListNode s = new ListNode(sum % 10);  // 空间复杂度（即最终新生成的链表长度）为 O(max(m,n))

        ListNode l1Next = l1 == null ? null : l1.next;
        ListNode l2Next = l2 == null ? null : l2.next;
        s.next = addTwoNumbers(l1Next, l2Next, carry);  // 空间复杂度（即递归深度）为 O(max(m,n))

        return s;
    }

    /*
     * 解法2：模拟加法运算（运用 Stack）
     * - 思路：该题的难点在于如何解决数位对齐问题，本解法与解法1一致，也是通过反向链表来对其数位。
     * - 实现：解法1是通过反向节点间的链接来反向链表，从而对齐数位。而本解法使用 Stack（如 L2 解法3）结构来反向链表，从而对齐数位。
     *   使用 Stack 的好处是不会修改原链表 l1、l2。
     * - 时间复杂度 O(max(m,n))，空间复杂度 O(m+n)。
     * */
    public static ListNode addTwoNumbers2(ListNode l1, ListNode l2) {
        Stack<Integer> s1 = toStack(l1);
        Stack<Integer> s2 = toStack(l2);

        int carry = 0;
        ListNode head = null;  // 结果链表

        while (!s1.isEmpty() || !s2.isEmpty() || carry != 0) {  // 再模拟加法运算（非递归）。O(max(m,n))
            int v1 = s1.isEmpty() ? 0 : s1.pop();
            int v2 = s2.isEmpty() ? 0 : s2.pop();
            int sum = v1 + v2 + carry;

            carry = sum / 10;
            ListNode newHead = new ListNode(sum % 10);
            newHead.next = head;  // 将新生成的节点放到结果链表头部（逆序生成链表），从而免去解法1中最后再 reverse 一次的必要
            head = newHead;
        }

        return head;
    }

    private static Stack<Integer> toStack(ListNode head) {
        Stack<Integer> stack = new Stack<>();
        while (head != null) {
            stack.push(head.val);
            head = head.next;
        }
        return stack;
    }

    public static void main(String[] args) {
        ListNode l3 = createLinkedList(new int[]{2, 4, 3});
        ListNode l4 = createLinkedList(new int[]{1, 5, 6, 4});
        printLinkedList(addTwoNumbers2(l3, l4));   // expects 1->8->0->7->NULL

        ListNode l5 = createLinkedList(new int[]{5});
        ListNode l6 = createLinkedList(new int[]{5});
        printLinkedList(addTwoNumbers2(l5, l6));   // expects 1->0->NULL
    }
}
