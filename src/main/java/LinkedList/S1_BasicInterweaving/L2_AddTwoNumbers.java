package LinkedList.S1_BasicInterweaving;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
 * Add Two Numbers
 *
 * - 给出两个非空链表，代表两个非负整数。其中每个整数各位上的数字以逆序存储，返回这两个整数之和的逆序链表。
 *   如 342 + 1465 = 1807，输入是 2->4->3、5->6->4->1，输出应为 7->0->8->1。
 * */

public class L2_AddTwoNumbers {
    /*
     * 有缺陷的解：
     * - 思路：先把两个链表逆序转成 long，再相加两个 long，最后再将结果逆序转成链表。
     * - 这种方法的优势是不需要手动处理进位问题；劣势是无法处理超过 long 精度的链表（可以使用 BigInteger）。
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
            s.insert(0, l.val);  // 将链表上的每个节点添加到字符串的头部（要用 insert 而不是 append）
            l = l.next;
        }
        return Long.parseLong(s.toString());  // "123" 转换为 123 的两种方法：1. Integer.parseInt 2. Integer.valueOf
    }

    private static ListNode longToLinkedList(long num) {
        ListNode dummyHead = new ListNode();
        ListNode curr = dummyHead;
        while (num > 0) {
            curr.next = new ListNode((int)(num % 10));  // 注意类型转换时后面如果是表达式要加上括号提高优先级
            curr = curr.next;
            num /= 10;
        }
        return dummyHead.next;
    }

    /*
     * 错误解：
     * - 思路：模拟加法运算，遍历链表，手动处理进位。
     * - 实现：∵ while 的条件是 l1 != null && l2 != null ∴ 当抵达较短的链表尾部时循环就结束了，之后再将较长链表的剩余节点
     *   拼接到 node 后面。
     * - 问题：这种实现在 test case 2 中会出错 ∵ 在拼接剩余节点时，没有（也不便于）处理进位问题。
     * */
    public static ListNode addTwoNumbers0(ListNode l1, ListNode l2) {
        if (l1 == null && l2 == null) return null;

        ListNode dummyHead = new ListNode();
        ListNode node = dummyHead;
        int carry = 0;

        while (l1 != null && l2 != null) {  // 当抵达较短的链表尾部时就结束循环
            int sum = l1.val + l2.val + carry;
            carry = sum / 10;
            node.next = new ListNode(sum % 10);
            node = node.next;
            l1 = l1.next;
            l2 = l2.next;
        }

        if (l1 != null) node.next = l1;  // 循环结束后再将较长链表的剩余元素拼接到 node 后面
        if (l2 != null) node.next = l2;
        return dummyHead.next;
    }

    /*
     * 解法1：模拟加法运算
     * - 实现：不同于上面的错误解，该解法的循环次数取决于较长的链表 ∴ 也会一直处理进位直到最后，从而避免了上面错误解中的问题。
     * - 时间复杂度 O(max(m,n))，空间复杂度 O(max(m,n))。
     * */
    public static ListNode addTwoNumbers2(ListNode l1, ListNode l2) {
        ListNode dummyHead = new ListNode();
        ListNode curr = dummyHead;
        int carry = 0;

        while (l1 != null || l2 != null || carry != 0) {  // 关键点：循环的退出条件（与解法3的递归结束条件一致）
            int v1 = l1 == null ? 0 : l1.val;
            int v2 = l2 == null ? 0 : l2.val;
            int sum = v1 + v2 + carry;

            carry = sum / 10;
            curr.next = new ListNode(sum % 10);
            curr = curr.next;

            if (l1 != null) l1 = l1.next;
            if (l2 != null) l2 = l2.next;
        }

        return dummyHead.next;
    }

    /*
     * 解法3：模拟加法运算（解法2的递归版）
     * - 思路：递归地创建下一个节点，直到两个链表都穷尽。
     * - 时间复杂度 O(max(m,n))，空间复杂度 O(max(m,n))。
     * */
    public static ListNode addTwoNumbers3(ListNode l1, ListNode l2) {
        return helper3(l1, l2, 0);
    }

    private static ListNode helper3(ListNode l1, ListNode l2, int carry) {
        if (l1 == null && l2 == null && carry == 0)  // 关键点：三个条件同时满足才结束递归（与解法2一致）
            return null;

        int v1 = l1 == null ? 0 : l1.val;
        int v2 = l2 == null ? 0 : l2.val;
        int sum = v1 + v2 + carry;

        carry = sum / 10;
        ListNode head = new ListNode(sum % 10);  // 每层递归创建一个节点

        if (l1 != null) l1 = l1.next;
        if (l2 != null) l2 = l2.next;
        head.next = helper3(l1, l2, carry);  // 下层创建的节点链接到该层创建的节点后面

        return head;  // 每层递归最后返回该层创建出来的节点
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{2, 4, 3});
        ListNode l2 = createLinkedList(new int[]{5, 6, 4, 1});
        log(addTwoNumbers2(l1, l2));   // expects 7->0->8->1->NULL

        ListNode l3 = createLinkedList(new int[]{3, 9, 9, 9, 9, 9, 9, 9, 9, 9});
        ListNode l4 = createLinkedList(new int[]{7});
        log(addTwoNumbers2(l3, l4));   // expects 0->0->0->0->0->0->0->0->0->0->1->NULL
    }
}

