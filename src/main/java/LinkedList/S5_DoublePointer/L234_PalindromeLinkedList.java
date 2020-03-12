package LinkedList.S5_DoublePointer;

import Utils.Helpers.ListNode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

import static Utils.Helpers.*;

/*
 * Palindrome Linked List
 *
 * - Given a singly linked list, determine if it is a palindrome (回文).
 * */

public class L234_PalindromeLinkedList {
    /*
     * 解法1：Stack + 指针对撞
     * - 思路：一个链表/数组是否是 palindrome 要同时从前、后两个方向逐个节点对照，若有节点值不等则说明不是 palindrome
     *   ∴ 可采用类似 L143_ReorderList 解法1的方式，反向链表的后半段，再与前半段来对照。
     * - 实现：类似 L143 中的解法1采用 Stack 进行反向。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean isPalindrome1(ListNode head) {
        if (head == null || head.next == null) return true;

        Stack<ListNode> stack = new Stack<>();
        for (ListNode curr = head; curr != null; curr = curr.next)
            stack.push(curr);

        int len = stack.size();              // 注意该变量不能 inline，∵ 循环中会不断改变 stack.size
        ListNode l = head;
        for (int i = 0; i < len / 2; i++) {  // 遍历一半的节点，O(n/2)
            ListNode r = stack.pop();
            if (l.val != r.val) return false;
            l = l.next;
        }

        return true;
    }

    /*
     * 解法2：生成反向链表
     * - 思路：与解法1一致。
     * - 实现：类似 L143_ReorderList 解法2，使用 Deque 来获得后半段的反向链表。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean isPalindrome2(ListNode head) {
        if (head == null || head.next == null) return true;

        Deque<ListNode> dq = new ArrayDeque<>();
        for (ListNode curr = head; curr != null; curr = curr.next)
            dq.add(curr);

        while (dq.size() > 1)
            if (dq.pollFirst().val != dq.pollLast().val)
                return false;

        return true;
    }

    /*
     * 解法3：生成反向链表
     * - 思路：与解法1、2一致。
     * - 实现：类似 L143_ReorderList 解法3，生成反向链表后再与原链表逐一对照。
     * - 💎技巧：若反向链表的过程需要重新创建节点，则可以采用不断将新建节点并插入到 dummyHead 之后的方式来实现对原链表的反向。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean isPalindrome3(ListNode head) {
        ListNode curr1 = head, curr2 = createReversedList(head);

        while (curr1 != null && curr2 != null) {
            if (curr1.val != curr2.val) return false;
            curr1 = curr1.next;
            curr2 = curr2.next;
        }

        return true;
    }

    private static ListNode createReversedList(ListNode head) {  // 该方法重新创建一个反向链表，而非原地修改
        ListNode dummyNode = new ListNode(), curr = head;
        while (curr != null) {
            ListNode temp = dummyNode.next;
            dummyNode.next = new ListNode(curr.val);  // 不断将新建节点并插入到 dummyHead 之后，从而实现对原链表的反向
            curr = curr.next;
            dummyNode.next.next = temp;
        }
        return dummyNode.next;
    }

    /*
     * 解法4：截断链表后比较
     * - 思路：从链表中点截断链表，之后再逐个比较前一半，以及反向过的后一半。要截断首先需要找到中点，而找一个链表的中点可
     *   采用 slow/fast 技巧（同 L143 解法2 中的 mid 方法）。
     * - 时间复杂度 O(n)，空间复杂度 O(1)（该解法原地变换、比较链表，无需开辟辅助空间）。
     * */
    public static boolean isPalindrome4(ListNode head) {
        if (head == null || head.next == null) return true;

        ListNode curr1 = head;
        ListNode curr2 = reverse(partition(head));  // reverse 和 partition 都是 O(n/2)

        while (curr1 != null && curr2 != null) {  // 也是 O(n/2)
            if (curr1.val != curr2.val) return false;
            curr1 = curr1.next;
            curr2 = curr2.next;
        }
        return true;
    }

    private static ListNode partition(ListNode head) {  // 对于 1->2->3->4，返回 3->4；对于 1->2->3，返回 2->3
        ListNode prevSlow = new ListNode(), fast = head;  // ∵ 要截断链表 ∴ 需获取链表中点的前一个节点 ∵ slow/fast 技巧中 slow 最后到达中点 ∴ 只需找到 slow 的上一节点即可
        prevSlow.next = head;
        while (fast != null && fast.next != null) {
            prevSlow = prevSlow.next;
            fast = fast.next.next;  // 若有偶数个节点则 fast 最后会停在 null 上，若有奇数个节点则会停在尾节点上
        }
        ListNode secondHalf = prevSlow.next;
        prevSlow.next = null;           // 截断链表
        return secondHalf;
    }

    private static ListNode reverse(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode prev = null, curr = head;
        while (curr != null) {
            ListNode temp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = temp;
        }
        return prev;
    }

    public static void main(String[] args) {
        ListNode l0 = createLinkedList(new int[]{1, 2});
        log(isPalindrome2(l0));  // expects false

        ListNode l1 = createLinkedList(new int[]{1, 1, 2, 1});
        log(isPalindrome2(l1));  // expects false

        ListNode l2 = createLinkedList(new int[]{1, 2, 2, 1});
        log(isPalindrome2(l2));  // expects true

        ListNode l3 = createLinkedList(new int[]{1, 0, 1});
        log(isPalindrome2(l3));  // expects true

        ListNode l4 = createLinkedList(new int[]{1});
        log(isPalindrome2(l4));  // expects true

        ListNode l5 = createLinkedList(new int[]{});
        log(isPalindrome2(l5));  // expects true
    }
}