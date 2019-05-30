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
    /*
    * 错误解：采用类似判断括号匹配的思路
    * - 匹配的括号（如 {([])}）一定是 palindrome，而 palindrome 不一定是匹配括号的形态。例如 test case 3 中的 [1,0,1]，中间
    *   有单个没有匹配的元素，这种情况也是 palindrome，但无法用括号匹配的思路来判断。
    * */
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
    public static boolean isPalindrome1(ListNode head) {
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

    /*
    * 解法2：生成反向链表
    * - 思路：直接生成一个反向链表。
    * - 注意：反向链表需要重新创建，而不能用 L206_ReverseLinkedList 中原地修改的方式，否则原链表会被修改导致后面无法正确遍历。
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static boolean isPalindrome2(ListNode head) {
        ListNode curr1 = head, curr2 = createReversedList(head);
        while (curr1 != null && curr2 != null && curr1 != curr2) {
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
            dummyNode.next = new ListNode(curr.val);  // 这里新建节点而不是直接将 curr 聊到 dummyNode 上
            curr = curr.next;
            dummyNode.next.next = temp;
        }
        return dummyNode.next;
    }

    /*
     * 解法3：
     * - 思路：截断链表，逐个比较前一半、后一半中的节点。
     * - 优势：解法1、2都需要创建反向链表，占用更多空间。而该解法原地变换、比较链表，因此更省空间。
     * - 时间复杂度 O(n)，空间复杂度 O(1)：
     *   - 虽然解法1、2、3的复杂度都是 O(n)，但该解法的统计性能最优，因为三次遍历都是遍历 n/2 个节点。
     *   - 如果 reverse 使用递归实现则空间复杂度是 O(n)。
     * */
    public static boolean isPalindrome3(ListNode head) {
        if (head == null || head.next == null) return true;

        ListNode secondHalf = reverse(partition(head));  // reverse 和 partition 都是 O(n/2)
        ListNode firstHalf = head;
        ListNode curr1 = firstHalf, curr2 = secondHalf;

        while (curr1 != null && curr2 != null) {  // 也是 O(n/2)
            if (curr1.val != curr2.val) return false;
            curr1 = curr1.next;
            curr2 = curr2.next;
        }
        return true;
    }

    private static ListNode partition(ListNode head) {  // 对于 1->2->3->4，返回 3->4；对于 1->2->3->4->5，返回 3->4->5
        ListNode preSlow = null, slow = head, fast = head, secondHalf;
        while (fast != null && fast.next != null) {
            preSlow = slow;
            slow = slow.next;
            fast = fast.next.next;  // 若有偶数个节点则 fast 最后会停在 null 上，若有奇数个节点则会停在尾节点上
        }
        if (fast == null) slow = preSlow;  // 若有偶数个节点则让 slow 往左移一位，从而可统一两种情况进行截断
        secondHalf = slow.next;
        slow.next = null;
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