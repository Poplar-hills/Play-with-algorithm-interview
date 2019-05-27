package LinkedList.DoublePointer;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
* Palindrome Linked List
*
* - Given a singly linked list, determine if it is a palindrome.
* */

public class L234_PalindromeLinkedList {
    public static boolean isPalindrome(ListNode head) {
        return false;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 2});
        log(isPalindrome(l1));  // expects false

        ListNode l2 = createLinkedListFromArray(new int[]{1, 2, 2, 1});
        log(isPalindrome(l2));  // expects true

    }
}