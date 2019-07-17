package LinkedList.DoublePointer;

import Utils.Helpers.ListNode;

import java.util.ArrayDeque;
import java.util.Deque;

import static Utils.Helpers.*;

/*
* Palindrome Linked List
*
* - Given a singly linked list, determine if it is a palindrome (回文).
* */

public class L234_PalindromeLinkedList {
    /*
    * 错误解：采用类似判断括号匹配的思路
    * - 匹配的括号（如 {([])}）一定是 palindrome，而 palindrome 不一定是匹配括号的形态。例如 test case 3 中的 [1,0,1]，中间
    *   有单个没有匹配的元素，这种情况也是 palindrome，但无法用括号匹配的思路来判断。
    * */
    public static boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null) return true;

        Deque<ListNode> stack = new ArrayDeque<>();
        for (ListNode curr = head; curr != null; curr = curr.next) {
            if (!stack.isEmpty() && curr.val == stack.peek().val)
                stack.pop();
            else
                stack.push(curr);
        }

        return stack.isEmpty();
    }

    /*
    * 解法1：指针对撞 + 使用 Stack 实现反向遍历
    * - 思路：要看一个链表或数组是否是 palindrome，需要同时从前、后两个方向逐个节点对照，若节点值相等则过，不相等则说明不是 palindrome。
    *   因此需要一个能反向遍历链表的方式，因此可以采用 Stack。
    * - 实现：类似 L143 中的解法1。
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static boolean isPalindrome1(ListNode head) {
        if (head == null || head.next == null) return true;

        Deque<ListNode> stack = new ArrayDeque<>();
        for (ListNode curr = head; curr != null; curr = curr.next)
            stack.push(curr);

        int len = stack.size();              // 注意该变量不能 inline，∵ 循环中会不断改变 stack.size
        ListNode left = head;
        for (int i = 0; i < len / 2; i++) {  // 遍历一半的节点，O(n/2)
            ListNode right = stack.pop();
            if (left.val != right.val) return false;
            left = left.next;
        }

        return true;
    }

    /*
    * 解法2：生成反向链表
    * - 思路：直接生成一个反向链表，然后与原链表逐一比较节点。
    * - 注意：反向链表需要重新创建，而不能用 L206_ReverseLinkedList 中原地修改的方式，否则原链表会被修改导致后面无法正确遍历。
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static boolean isPalindrome2(ListNode head) {
        ListNode curr1 = head, curr2 = createReversedList(head);
        while (curr1 != null && curr2 != null && curr1 != curr2) {  // 以 curr1 != curr2 作为终止条件的话对于 test case 3 这种偶数个节点的链表来说会遍历整个链表，即 O(n)
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
            dummyNode.next = new ListNode(curr.val);  // 新建节点并插入到 dummyHead 之后，从而实现对原链表的反向
            curr = curr.next;
            dummyNode.next.next = temp;
        }
        return dummyNode.next;
    }

    /*
     * 解法3：截断链表后比较
     * - 思路：从链表中点截断链表，之后再逐个比较前一半，以及反向过的后一半。要截断首先需要找到中点 —— 可以采用 slow/fast 技巧（同
     *   L143 解法2 中的 mid 方法）。
     * - 时间复杂度 O(n)，空间复杂度 O(1)（该解法原地变换、比较链表，无需开辟辅助空间）。
     * */
    public static boolean isPalindrome3(ListNode head) {
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
        ListNode prev = new ListNode(), fast = head;  // ∵ 要截断链表 ∴ 需要获取链表中点的前一个节点 prev
        prev.next = head;
        while (fast != null && fast.next != null) {
            prev = prev.next;
            fast = fast.next.next;  // 若有偶数个节点则 fast 最后会停在 null 上，若有奇数个节点则会停在尾节点上
        }
        ListNode secondHalf = prev.next;
        prev.next = null;           // 截断链表
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
        ListNode l0 = createLinkedListFromArray(new int[]{1, 2});
        log(isPalindrome3(l0));  // expects false

        ListNode l1 = createLinkedListFromArray(new int[]{1, 1, 2, 1});
        log(isPalindrome3(l1));  // expects false

        ListNode l2 = createLinkedListFromArray(new int[]{1, 2, 2, 1});
        log(isPalindrome3(l2));  // expects true

        ListNode l3 = createLinkedListFromArray(new int[]{1, 0, 1});
        log(isPalindrome3(l3));  // expects true

        ListNode l4 = createLinkedListFromArray(new int[]{1});
        log(isPalindrome3(l4));  // expects true

        ListNode l5 = createLinkedListFromArray(new int[]{});
        log(isPalindrome3(l5));  // expects true
    }
}