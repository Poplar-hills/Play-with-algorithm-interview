package LinkedList.S1_BasicInterweaving;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
* Add Two Numbers
*
* - 给出两个非空链表，代表两个非负整数。其中每个整数的各个位上的数字以逆序存储，返回这两个整数之和的逆序链表。
*   如 342 + 1465 = 1807，输入是 2->4->3、5->6->4->1，输出应为 7->0->8->1。
* */

public class L2_AddTwoNumbers {
    /*
    * 有缺陷的解：
    * - 思路：先把两个链表逆序转成 long，再相加两个 long，最后再将结果逆序转成链表。
    * - 这种方法的：优势是不需要手动处理进位问题；劣势是无法处理超过 long 精度的链表（可以使用 BigInteger）（实际面试中很少有，不是个很大的问题）。
    * - 时间复杂度 O(m+n)，空间复杂度 O(m+n)，其中 m, n 分别为 l1, l2 的节点个数。
    * */
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        long num1 = linkedListToLong(l1);
        long num2 = linkedListToLong(l2);
        return longToLinkedList(num1 + num2);
    }

    private static long linkedListToLong(ListNode l) {  // 将 2->4->3->NULL 转成 342
        StringBuilder s = new StringBuilder();
        while (l != null) {
            s.insert(0, l.val);     // 将链表上的每个节点添加到字符串的头部（要用 insert 而不是 append）
            l = l.next;
        }
        return Long.parseLong(s.toString());  // "123" 转换为 123 的两种方法：1. Integer.parseInt 2. Integer.valueOf
    }

    private static ListNode longToLinkedList(long num) {
        ListNode dummyHead = new ListNode();
        ListNode curr = dummyHead;
        while (num >= 1) {
            curr.next = new ListNode((int)(num % 10));  // 注意类型转换时后面如果是表达式要加上括号提高优先级
            curr = curr.next;
            num /= 10;
        }
        return dummyHead.next;
    }

    /*
    * 解法1：模拟加法运算
    * - 时间复杂度 O(max(m,n))，空间复杂度 O(max(m,n))。
    * */
    public static ListNode addTwoNumbers2(ListNode l1, ListNode l2) {
        ListNode dummyHead = new ListNode();
        ListNode curr = dummyHead;
        int carry = 0;

        while (l1 != null || l2 != null) {
            int curr1Val = l1 != null ? l1.val : 0;
            int curr2Val = l2 != null ? l2.val : 0;
            int sum = curr1Val + curr2Val + carry;

            carry = sum / 10;
            curr.next = new ListNode(sum % 10);
            curr = curr.next;

            if (l1 != null) l1 = l1.next;
            if (l2 != null) l2 = l2.next;
        }

        if (carry != 0)  // 如果还有进位则需再加一位
            curr.next = new ListNode(carry);

        return dummyHead.next;
    }

    /*
    * 解法3：模拟加法运算（递归实现）
    * - 思路：递归地创建下一个节点，直到两个链表都穷尽。
    * - 时间复杂度 O(max(m,n))，空间复杂度 O(max(m,n))。
    * */
    public static ListNode addTwoNumbers3(ListNode l1, ListNode l2) {
        return addTwoNumbers3(l1, l2, 0);
    }

    private static ListNode addTwoNumbers3(ListNode l1, ListNode l2, int carry) {
        if (l1 == null && l2 == null && carry == 0)  // 三个条件同时满足才结束递归
            return null;

        int l1Val = l1 != null ? l1.val : 0;
        int l2Val = l2 != null ? l2.val : 0;
        int sum = l1Val + l2Val + carry;

        carry = sum / 10;
        ListNode s = new ListNode(sum % 10);

        ListNode l1Next = l1 == null ? null : l1.next;
        ListNode l2Next = l2 == null ? null : l2.next;
        s.next = addTwoNumbers3(l1Next, l2Next, carry);

        return s;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{2, 4, 3});
        ListNode l2 = createLinkedListFromArray(new int[]{5, 6, 4, 1});
        printLinkedList(addTwoNumbers(l1, l2));   // expects 7->0->8->1->NULL

        ListNode l3 = createLinkedListFromArray(new int[]{3, 9, 9, 9, 9, 9, 9, 9, 9, 9});
        ListNode l4 = createLinkedListFromArray(new int[]{7});
        printLinkedList(addTwoNumbers(l3, l4));   // expects 0->0->0->0->0->0->0->0->0->0->1->NULL
    }
}

