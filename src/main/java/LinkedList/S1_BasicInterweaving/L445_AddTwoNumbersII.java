package LinkedList.S1_BasicInterweaving;

import Utils.Helpers.ListNode;

import java.util.Stack;

import static Utils.Helpers.*;

/*
* Add Two Numbers II
*
* - 给出两个非空链表，代表两个非负整数。其中每个整数的各个位上的数字以顺序存储（此处于 L2 不同，L2 是逆序存储），返回这两个整数之和的顺序链表。
*   如 243 + 1564 = 1807，则给出 2->4->3、1—>5->6->4，返回 1->8->0->7。
* */

public class L445_AddTwoNumbersII {
    /*
    * 解法1：先将链表反向，再模拟加法运算，最后再反向。
    * - 思路：本题与 L2 的不同点在于输入、输出都是顺序链表，因此在模拟加法运算之前需要解决数位对应的问题（个位与个位相加，十位与
    *   十位相加……）。因此可以先将两个链表反向，再对这两个逆序链表求和，此时求和过程就是从个位开始相加，不存在数位对应问题，因此
    *   求和逻辑可以采用 L2 中解法3的思路。最后再将结果链表再次反向即可。
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
        ListNode s = new ListNode(sum % 10);         // 空间复杂度（即最终新生成的链表长度）为 O(max(m,n))

        ListNode l1Next = l1 == null ? null : l1.next;
        ListNode l2Next = l2 == null ? null : l2.next;
        s.next = addTwoNumbers(l1Next, l2Next, carry);  // 空间复杂度（即递归深度）为 O(max(m,n))

        return s;
    }

    /*
    * 解法2：模拟加法运算（用 stack 辅助）
    * - 思路：解法1是采用重置节点间链接的方式反向链表，从而解决数位对齐问题。而反向链表的另一种方式是使用 stack（如 L2 解法3）。
    * - Bonus：该解法不会修改 l1、l2。
    * - 时间复杂度 O(max(m,n))，空间复杂度 O(m+n)。
    * */
    public static ListNode addTwoNumbers2(ListNode l1, ListNode l2) {
        Stack<Integer> s1 = new Stack<>(), s2 = new Stack<>();
        ListNode curr1 = l1, curr2 = l2;

        while (curr1 != null || curr2 != null) {  // 先将两个链表反向存储在 stack 中。O(max(m,n))
            if (curr1 != null) {
                s1.push(curr1.val);
                curr1 = curr1.next;
            }
            if (curr2 != null) {
                s2.push(curr2.val);
                curr2 = curr2.next;
            }
        }

        ListNode resList = null;
        int carry = 0;
        while (!s1.isEmpty() || !s2.isEmpty() || carry != 0) {  // 再模拟加法运算（非递归）。O(max(m,n))
            int s1Val = !s1.isEmpty() ? s1.pop() : 0;
            int s2Val = !s2.isEmpty() ? s2.pop() : 0;
            int sum = s1Val + s2Val + carry;

            carry = sum / 10;                            // 定义结果链表
            ListNode head = new ListNode(sum % 10);
            head.next = resList;                         // 将新生成的 sum 节点放到结果链表头部，从而免去解法1中最后再反向一次的必要
            resList = head;
        }
        return resList;
    }

    public static void main(String[] args) {
        ListNode l3 = createLinkedListFromArray(new int[]{2, 4, 3});
        ListNode l4 = createLinkedListFromArray(new int[]{1, 5, 6, 4});
        printLinkedList(addTwoNumbers2(l3, l4));   // expects 1->8->0->7->NULL

        ListNode l5 = createLinkedListFromArray(new int[]{5});
        ListNode l6 = createLinkedListFromArray(new int[]{5});
        printLinkedList(addTwoNumbers2(l5, l6));   // expects 1->0->NULL
    }
}
