package LinkedList.ClassicProblems;

import Utils.Helpers.ListNode;

import java.util.Stack;

import static Utils.Helpers.*;

/*
* Add Two Numbers II
*
* - 给出两个非空链表，代表两个非负整数。其中每个整数的各个位上的数字以顺序存储，返回这两个整数之和的顺序链表。
*   如 7243 + 564 = 7807，则给出 7—>2->4->3、5->6->4，返回 7->8->0->7。
* */

public class L445_AddTwoNumbersII {
    /*
    * 解法1：先将链表反向，再模拟加法运算（同 L2 中解法3），最后再反向。
    * - 不足之处是该解法会修改输入链表。
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
    * 解法2：模拟加法运算（用 Stack 作为辅助数据结构）
    * - 思路：本题与 L2 的不同点在于输入、输出都是顺序链表，因此在模拟加法运算之前需要解决位数对应的问题（个位与个位相加，
    *   十位与十位相加……），解法1中先将链表反向的思路就是在解决这个问题，而更优雅的做法是使用 Stack 解决这个问题，这与
    *   BST 的前序、中序遍历是同样的思路。
    * - Bonus：该解法不会修改 l1、l2。
    * - 时间复杂度 O(max(m,n))，空间复杂度 O(m+n)。
    * */
    public static ListNode addTwoNumbers2(ListNode l1, ListNode l2) {
        Stack<Integer> s1 = new Stack<>(), s2 = new Stack<>();
        ListNode curr1 = l1, curr2 = l2;

        while (curr1 != null || curr2 != null) {  // O(max(m,n))
            if (curr1 != null) {
                s1.push(curr1.val);
                curr1 = curr1.next;
            }
            if (curr2 != null) {
                s2.push(curr2.val);
                curr2 = curr2.next;
            }
        }

        ListNode list = null;
        int carry = 0;
        while (!s1.empty() || !s2.empty() || carry != 0) {  // O(max(m,n))
            int s1Val = !s1.empty() ? s1.pop() : 0;
            int s2Val = !s2.empty() ? s2.pop() : 0;
            int sum = s1Val + s2Val + carry;

            carry = sum / 10;
            ListNode head = new ListNode(sum % 10);
            head.next = list;
            list = head;
        }
        return list;
    }

    public static void main(String[] args) {
        ListNode l3 = createLinkedListFromArray(new int[]{7, 2, 4, 3});
        ListNode l4 = createLinkedListFromArray(new int[]{5, 6, 4});
        printLinkedList(addTwoNumbers2(l3, l4));   // expects 7->8->0->7->NULL

        ListNode l5 = createLinkedListFromArray(new int[]{5});
        ListNode l6 = createLinkedListFromArray(new int[]{5});
        printLinkedList(addTwoNumbers2(l5, l6));   // expects 1->0->NULL
    }
}
