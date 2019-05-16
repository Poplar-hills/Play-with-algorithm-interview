package LinkedList.DummyHead;

import Utils.Helpers.ListNode;
import javafx.util.Pair;

import static Utils.Helpers.*;

/*
* Remove Duplicates from Sorted List II
*
* - 删除一个有序链表中所有的重复节点。
* */

public class L82_RemoveDuplicatesFromSortedListII {
    /*
    * 解法1：Intuitive solution
    * - 思路：对于 2->3->3->4 来说，若想将去除 3->3，则需要链接3之前的2节点和第一个4节点，因此这两个节点需要有指针指向才行。
    *        因此首先设置虚拟头结点，再创建两个指针（分别指向重复节点之前和之后的节点）+ 一个标志位（标志当前是否发现了重复节点）：
    *        D -> 2 -> 3 -> 3 -> 4
    *        a    b                 此时 b.val != b.next.val，则 a, b 向后移动
    *        D -> 2 -> 3 -> 3 -> 4
    *             a    b            此时 b.val == b.next.val，则 a 不动，b 向后移动，标志位置为 true
    *        D -> 2 -> 3 -> 3 -> 4
    *             a         b       此时 b.val != b.next.val，但因为标志位置为 true，所以 a 不动，b 向后移动，标志位置为 false，让 a.next 指向 b
    *        D -> 2 -> 4
    *             a    b            此时 b.next == null，结束遍历
    * - 注意特殊情况的处理：D -> 2 -> 1 -> 1，此时重复节点后面没有更多节点，因此 a 停在 2 上，b 停在最后一个1上，无法按上面的过
    *   程完成删除（即循环已结束，但标志位还是 true），因此可直接将 a.next 指向 null 即可。
    * - 时间复杂度 O(n)，空间复杂度 O(1)。
    */
    public static ListNode deleteDuplicates(ListNode head) {
        if (head == null) return null;  // 注意这个 case 得特殊处理
        ListNode dummyHead = new ListNode();
        dummyHead.next = head;
        ListNode conn = dummyHead, curr = head;
        boolean foundDuplicates = false;

        while (curr.next != null) {
            if (!foundDuplicates && curr.val != curr.next.val) {
                conn = conn.next;
                curr = curr.next;
            }
            else if (curr.val == curr.next.val) {
                foundDuplicates = true;
                curr = curr.next;
            }
            else {
                foundDuplicates = false;
                curr = curr.next;
                conn.next = curr;
            }
        }

        if (foundDuplicates)  // curr.next == null 的情况（SEE test case 3）
            conn.next = null;

        return dummyHead.next;
    }

    /*
    * 解法2：解法1的递归版
    * - 思路：关键在于返回上一层时需要让上一层知道是否已经出现了重复节点，从而删除其中一个，这就需要在 return 时加入标志位。
    * - 演示：D -> 2 -> 3 -> 3 -> 4                      D -> 1 -> 1 -> 1 -> 2
    *                         ← 返回 (4, false)                           ← 返回 (2, false)
    *                    ← 返回 (3->4, false)                        ← 返回 (1->2, false)
    *               ← 返回 (3->4，true)                         ← 返回 (1->2, true)
    *          ← 返回 (2->4，false)                        ← 返回 (1->2, true)
    *                                                   到达最上层时判断若标志位仍然为 true 则取第二个节点之后的链表，即只有2
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static ListNode deleteDuplicates2(ListNode head) {
        if (head == null) return null;
        Pair<ListNode, Boolean> pair = deleteHeadDuplicates(head);
        ListNode r = pair.getKey();
        return pair.getValue() ? r.next : r;
    }

    private static Pair<ListNode, Boolean> deleteHeadDuplicates(ListNode head) {
        if (head.next == null)
            return new Pair<>(head, false);

        Pair<ListNode, Boolean> pair = deleteHeadDuplicates(head.next);
        ListNode next = pair.getKey();
        boolean foundDuplicate = pair.getValue();

        if (head.val == next.val)
            return new Pair<>(next, true);
        if (foundDuplicate) {
            head.next = next.next;
            return new Pair<>(head, false);
        }
        head.next = next;
        return new Pair<>(head, false);
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 2, 3, 3, 4, 4, 5});
        printLinkedList(deleteDuplicates2(l1));  // expects 1->2->5->NULL

        ListNode l2 = createLinkedListFromArray(new int[]{1, 1, 1, 2, 3});
        printLinkedList(deleteDuplicates2(l2));  // expects 2->3->NULL

        ListNode l3 = createLinkedListFromArray(new int[]{1, 1});
        printLinkedList(deleteDuplicates2(l3));  // expects NULL

        ListNode l4 = createLinkedListFromArray(new int[]{});
        printLinkedList(deleteDuplicates2(l4));  // expects NULL
    }
}
