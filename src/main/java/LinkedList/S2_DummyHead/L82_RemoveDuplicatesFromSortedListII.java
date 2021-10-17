package LinkedList.S2_DummyHead;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
 * Remove Duplicates from Sorted List II
 *
 * - 删除一个有序链表中所有的重复节点。
 * - 说明：不同于 L83_RemoveDuplicatesFromSortedList 中删除原节点后面的所有重复节点，该题要求删除所有具有相同值的节点。
 *   例如对于 1->2->2->2->3 来说，三个值为2的节点都要删除。
 * */

public class L82_RemoveDuplicatesFromSortedListII {
    /*
     * 解法1：Map 计数
     * - 思路：使用 Map 记录链表中节点值的频次，然后再次遍历，将频次 >1 的节点移除。
     * - 实现：需要遍历链表两遍，效率较低。
     * - 时间复杂度 O(2n)，空间复杂度 O(n)。
     * */
    public static ListNode deleteDuplicates(ListNode head) {
        Map<Integer, Integer> freq = new HashMap<>();

        ListNode curr = head;
        while (curr != null) {
            freq.merge(curr.val, 1, Integer::sum);
            curr = curr.next;
        }

        ListNode dummyHead = new ListNode();  // ∵ 链表的头节点可能就是需要移除的重复节点 ∴ 要使用虚拟头结点
        dummyHead.next = head;
        ListNode prev = dummyHead;
        curr = head;
        while (curr != null) {
            if (freq.get(curr.val) > 1)
                prev.next = curr.next;  // 将重复节点移除后 prev 不动，只更新 curr 的指向即可
            else
                prev = curr;
            curr = prev.next;
        }

        return dummyHead.next;
    }

    /*
     * 解法2：双指针 + 标志位
     * - 思路：∵ 原节点也要删除 ∴ 像 L83 中那样只使用一个指针、在发现原节点后跳过后面的重复节点的方法是不行的。需要一个指针指向
     *   原节点的上一个节点，另一个指针向后移动直到最后一个重复节点的下一个节点，最后连接这两个节点完成对中间节点的删除。
     * - 演示：例如对于 2->3->3->4->5 来说，若想将去除 3->3，则需要链接2和4 ∴ 需要先获取这两个节点：
     *      D -> 2 -> 3 -> 3 -> 4 -> 5
     *      p    c                      - p 初始指向虚拟头结点，c 指向 head。此时 c 与下个节点不重复 ∴ 让 p, c 后移
     *      D -> 2 -> 3 -> 3 -> 4 -> 5
     *           p    c                 - 此时 c 与下个节点重复 ∴ 只让 c 后移
     *      D -> 2 -> 3 -> 3 -> 4 -> 5
     *           p         c            - 此时 c 与下个节点不重复，但 ∵ c 也是需要删除的节点 ∴ 需要让 p.next = c.next，再让 c 后移
     *      D -> 2 -> 4 -> 5
     *           p    c                 - 此时 c 与下个节点不重复 ∴ 同时让 p、c 后移
     *      D -> 2 -> 4 -> 5
     *                p    c            - 此时 c.next == null，遍历结束
     * - 实现：1. ∵ 链表中可能存在多个重复节点 ∴ 可在第2步中使用 while 一直走到最后一个重复节点；
     *        2. 在第3步中，p.next = c.next 这一操作只能对最后一个重复节点进行（在第4步中就不能有）∴ 需要一个标志位标识在
     *           某一时刻 c 是否指向重复节点。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode deleteDuplicates2(ListNode head) {
        ListNode dummyHead = new ListNode();
        dummyHead.next = head;
        ListNode prev = dummyHead, curr = head;
        boolean foundDuplicates = false;

        while (curr != null && curr.next != null) {
            while (curr.next != null && curr.val == curr.next.val) {
                foundDuplicates = true;
                curr = curr.next;
            }
            if (foundDuplicates) {
                prev.next = curr.next;
                foundDuplicates = false;
            } else {
                prev = curr;
            }
            curr = curr.next;
        }

        return dummyHead.next;
    }

    /*
     * 解法3：解法2的递归版
     * - 思路：与解法2一致，使用标志位标识处理最后一个重复节点。
     * - 实现：先递归到底，在回程时决定返回、跳过哪个节点。∵ 在返回上一层时需要让上一层知道是否已经出现了重复节点，从而删除其中
     *   一个（不能两个都删掉，否则再碰到一个重复节点就无法删除了）∴ 需要在返回上一层时加入标志位：若标志位为 true 则删除上层
     *   返回的头节点：
     *            2 -> 3 -> 3 -> 4                           1 -> 1 -> 1 -> 2
     *                         ← (4, false)                               ← (2, false)
     *                    ← (3->4, false)                            ← (1->2, false)
     *               ← (3->4，true)                             ← (1->2, true)
     *          ← (2->4，false)                            ← (1->2, true)    - 最后如果还是 true 则再跳过一个节点，只返回2
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode deleteDuplicates3(ListNode head) {
        if (head == null) return null;
        Pair<ListNode, Boolean> pair = helper3(head);
        ListNode newHead = pair.getKey();
        return pair.getValue() ? newHead.next : newHead;
    }

    private static Pair<ListNode, Boolean> helper3(ListNode head) {
        if (head.next == null) return new Pair<>(head, false);

        Pair<ListNode, Boolean> pair = helper3(head.next);
        ListNode next = pair.getKey();
        boolean foundDuplicate = pair.getValue();

        if (head.val == next.val)           // 若发现重复节点
            return new Pair<>(next, true);  // 则跳过当前 head 节点，并标志位置为 true
        head.next = foundDuplicate ? next.next : next;  // 若未发现重复节点，则看标志位（可能真的没有重复节点，也可能
        return new Pair<>(head, false);                 // 当前节点为最后一个重复节点）
    }

    /*
     * 解法4：双指针 + 重复节点值
     * - 思路：与解法2类似，使用两个指针、两个 while 来跳过重复节点。
     * - 实现：对于如何判断当前节点是否是最后一个重复节点，解法2、3采用标志位，而该解法记录重复节点的节点值。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode deleteDuplicates4(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode dummyHead = new ListNode();
        dummyHead.next = head;

        ListNode prev = dummyHead, curr = head;
        int duplicateVal;  // 记录发现的重复节点的节点值

        while (curr != null) {
            if (curr.next != null && curr.val == curr.next.val) {  // 若发现重复节点
                duplicateVal = curr.val;                           // 先记录下重复节点值
                while (curr.next != null && curr.next.val == duplicateVal)  // 用 while 走到最后一个重复节点上
                    curr = curr.next;
                prev.next = curr.next;  // 将 curr.next 链接到 prev 上
            } else {
                prev = curr;
            }
            curr = curr.next;
        }

        return dummyHead.next;
    }

    /*
     * 解法5：递归 + 迭代
     * - 思路：采用递归 + 迭代的方式，在递归去程路上检查是否与下一个节点重复，若是则通过 while 走到最后一个重复节点上，并从对
     *   其后面的节点继续递归（相当于跳过了所有重复节点）；若否的话则正常递归下去。
     * - 👉 技巧：这种递归 + 迭代的实现其实非常 straightforward。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode deleteDuplicates5(ListNode head) {
        if (head == null || head.next == null) return head;

        if (head.val == head.next.val) {
            while (head.next != null && head.val == head.next.val)  // 通过 while 循环跳过 val 相同的节点
                head = head.next;
            return deleteDuplicates5(head.next);
        } else {
            head.next = deleteDuplicates5(head.next);
            return head;
        }
    }

    /*
     * 解法6：递归 + Map 计数
     * - 思路：在递归去程的路上使用 Map 统计节点值的频率，在回程时根据 Map 判断，若频率 > 1 则跳过。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode deleteDuplicates6(ListNode head) {
        return helper6(head, new HashMap<>());
    }

    private static ListNode helper6(ListNode head, Map<Integer, Integer> freq) {
        if (head == null) return null;
        freq.merge(head.val, 1, Integer::sum);
        head.next = helper6(head.next, freq);
        return freq.get(head.val) > 1 ? head.next : head;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 2, 3, 3, 3, 4, 4, 5});
        log(deleteDuplicates6(l1));  // expects 1->2->5->NULL

        ListNode l2 = createLinkedList(new int[]{1, 1, 1, 2, 3});
        log(deleteDuplicates6(l2));  // expects 2->3->NULL

        ListNode l3 = createLinkedList(new int[]{1, 1});
        log(deleteDuplicates6(l3));  // expects NULL

        ListNode l4 = createLinkedList(new int[]{});
        log(deleteDuplicates6(l4));  // expects NULL
    }
}
