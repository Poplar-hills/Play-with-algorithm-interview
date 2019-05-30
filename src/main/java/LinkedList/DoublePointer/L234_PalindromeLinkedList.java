package LinkedList.DoublePointer;

import Utils.Helpers.ListNode;

import java.util.Stack;

import static Utils.Helpers.*;

/*
* Palindrome Linked List
*
* - Given a singly linked list, determine if it is a palindrome.
* */

public class L234_PalindromeLinkedList {
    public static boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null) return true;

        Stack<ListNode> stack = new Stack<>();
        for (ListNode curr = head; curr != null; curr = curr.next) {
            if (!stack.isEmpty() && curr.val == stack.peek().val)
                stack.pop();
            else
                stack.push(curr);
        }

        return stack.empty();
    }

    /*
    * 解法1：指针对撞 + 使用 Stack 实现反向遍历
    * - 思路：要看一个链表或数组是否是 palindrome，需要同时从前、后两个方向逐个节点对照，若节点值相等则过，不相等则说明不是 palindrome。
    *   因此需要一个能反向遍历链表的方式，因此可以采用 Stack。
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static boolean isPalindrome2(ListNode head) {
        if (head == null || head.next == null) return true;

        Stack<ListNode> stack = new Stack<>();
        for (ListNode curr = head; curr != null; curr = curr.next)
            stack.push(curr);

        ListNode left = head;
        while (!stack.isEmpty() && left != stack.peek()) {  // 注意在调用 stack.peek() 之前要检查 !stack.isEmpty()，否则会报错
            ListNode right = stack.pop();
            if (left.val != right.val) return false;
            left = left.next;
        }

        return true;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 2});
        log(isPalindrome2(l1));  // expects false

        ListNode l2 = createLinkedListFromArray(new int[]{1, 2, 2, 1});
        log(isPalindrome2(l2));  // expects true

        ListNode l3 = createLinkedListFromArray(new int[]{1, 0, 1});
        log(isPalindrome2(l3));  // expects true

        ListNode l4 = createLinkedListFromArray(new int[]{1});
        log(isPalindrome2(l4));  // expects true

        ListNode l5 = createLinkedListFromArray(new int[]{});
        log(isPalindrome2(l5));  // expects true
    }
}