package LinkedList.S3_AdvancedInterweaving;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

import java.util.Stack;

/*
 * Reverse Nodes in k-Group
 *
 * - 给定一个链表，每 k 个节点为一组，反向每一组节点，返回修改后的链表。
 *
 * - Notes:
 *   1. If the number of nodes is not a multiple of k then left-out nodes in the end should remain as it is.
 *   2. Only constant extra memory is allowed.
 * */

public class L25_ReverseNodesInKGroup {
    /*
     * 解法1：递归
     * - 思路：非常 straightforward —— 每层递归处理 k 个节点，在递归去程时先检查一组中的节点是否充足，若充足则进行反向，否则
     *   直接返回该组头节点。
     * - 实现：节点反向采用迭代写法，直接反向两点间链接。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode reverseKGroup(ListNode head, int k) {
        ListNode prev = null, curr = head, temp = head;  // ∵ 要反向节点链接 ∴ prev 可初始化为 null，从而让第一个节点.next = null

        for (int i = 0; i < k; i++, temp = temp.next)    // 检查本组中是否有足够的节点
            if (temp == null)
                return head;           // 若本组内节点数不足 k 个，则不进行反向

        for (int i = 0; i < k; i++) {  // 反向本组中的节点链接
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }

        head.next = reverseKGroup(curr, k);  // 反向完成后 head 变成了本组的尾节点、prev 变成了本组的头节点
        return prev;                         // ∴ 继续对 curr 进行递归，并最后返回 prev
    }

    /*
     * 解法2：递归
     * - 思路：与解法1一致，也是先检测每组几点数是否够 k 个，若够则反向，否则直接返回。
     * - 实现：先递归到底，然后在递归回程路上对每组内的节点进行反向，即从整个链表上来看是从右到左按组反向的。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode reverseKGroup2(ListNode head, int k) {
        ListNode curr = head;
        int count = 0;

        while (curr != null && count < k) {  // 先让 curr 走过本组的节点以检查节点是否充足（最后 curr 停在下一组的第一个节点上）
            curr = curr.next;
            count++;
        }
        if (count != k) return head;     // 若走不到说明本组节点不足，直接返回本组头节点

        curr = reverseKGroup2(curr, k);  // 若充足则进入下一层递归

        while (count-- > 0) {            // 递归返回之后再反向本组的节点
            ListNode next = head.next;
            head.next = curr;
            curr = head;
            head = next;
        }

        return curr;  // 反向完成后 curr 会指向本组头结点 ∴ 返回 curr
    }

    /*
    * 解法3：递归
    * - 思路：与解法1一致，也是在去程中检测每组节点数是否足够，若够则直接反向，否则直接返回 head。然后再继续处理下一组。
    * - 实现：与解法1不同之处在于节点反向采用递归写法，递归最终返回一个节点数组：[最后一个节点, 最后一个节点的下一个节点]
    *   （与 L92_ReverseLinkedListII 解法3类似）。
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static ListNode reverseKGroup3(ListNode head, int k) {
        ListNode curr = head;
        for (int i = 0; i < k; i++) {  // 检查本组中是否有足够的节点
            if (curr == null) return head;
            curr = curr.next;
        }

        ListNode[] reversed = reverseKNodes(head, k);  // 反向本组中的节点，返回 [本组最后一个节点, 下一组的第一个节点]
        ListNode newHead = reversed[0];
        ListNode rest = reversed[1];

        head.next = reverseKGroup3(rest, k);  // 继续处理下一组节点
        return newHead;
    }

    private static ListNode[] reverseKNodes(ListNode head, int k) {
        if (k == 1) return new ListNode[]{head, head.next};  // 递归到底后返回 [本组最后一个节点, 下一组的第一个节点]
        ListNode[] reversed = reverseKNodes(head.next, --k);
        head.next.next = head;  // 将 A -> B -> C 改为 A <-> B   C
        head.next = null;       // 将 A <-> B   C 改为 A <- B   C
        return reversed;        // 返回的总是递归到底后返回的节点
    }

    /*
     * 解法4：迭代 + Stack
     * - 思路：与解法1、2、3类似，都是先检查本组节点数是否够 k 个，再决定是否反向本组。
     * - 实现：该解法通过将一组的节点入栈、检查 stack.size() 来确定一组节点是否够数、是否需要反向，具体反向操作通过 Stack 完成。
     * - 💎 总结：提到"反向"先想 Stack，采用 Stack 反向通常是最简单的实现方式。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode reverseKGroup4(ListNode head, int k) {
        ListNode dummyHead = new ListNode();
        dummyHead.next = head;
        ListNode prev = dummyHead, curr = head;
        Stack<ListNode> stack = new Stack<>();

        while (curr != null) {
            if (stack.size() < k) {   // 不够 k 个之前持续入栈
                stack.push(curr);
                curr = curr.next;
            }                         // 入栈 k 个节点后，curr 停在 k+1 节点处 ∴ 当本组反向完成后，可以直接继续入栈下一组
            if (stack.size() == k) {  // 若够了 k 个则开始反向，若不够则 curr 一定走到了 null 上 ∴ 可以直接退出，不够一组的节点就留在 Stack 里
                while (!stack.isEmpty()) {
                    prev.next = stack.pop();  // 通过 Stack 反向输出链表节点
                    prev = prev.next;
                }
                prev.next = curr;     // 反向结束时 prev 指向本组反向后的最后一个节点 ∴ 要让下一组头结点链到它上
            }
        }

        return dummyHead.next;
    }

    /*
     * 解法5：递归 + Stack
     * - 思路：与解法1、2、3、4一致。
     * - 实现：结合解法2的递归和解法3的 Stack。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode reverseKGroup5(ListNode head, int k) {
        // 1. 数出 k 个节点
        Stack<ListNode> stack = new Stack<>();
        ListNode curr = head;
        int count = 0;

        while (count < k && curr != null) {  // 在去程时按组检查节点是否充足，并将节点入栈
            stack.push(curr);
            curr = curr.next;
            count++;
        }
        if (count != k) return head;  // 若本组节点不够 k 个，则不反向

        // 2. 若该组节点充足且下个节点不为空，则继续递归下一组
        ListNode reversedHead = null;
        if (curr != null)
            reversedHead = reverseKGroup4(curr, k);

        // 3. 在回程时反向本组节点，并将下一组结果链到本组反向后的尾部
        ListNode dummyHead = new ListNode();
        ListNode prev = dummyHead;

        while (!stack.isEmpty()) {
            prev.next = stack.pop();
            prev = prev.next;
        }
        prev.next = reversedHead;  // 将下一组反向的结果链到本组尾节点上

        // 4. 最后返回本组反向的结果
        return dummyHead.next;
    }

    /*
     * 解法6：递归 + Stack（解法5的简化版）
     * - 思路：与解法1、2、3、4、5一致。
     * - 实现：不同于解法5在回程时反向本组节点，该解法在去程时如果数够了 k 个节点就马上反向，而回程时不做任何操作。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode reverseKGroup0(ListNode head, int k) {
        if (head == null) return null;

        // 1. 数出 k 个节点
        Stack<ListNode> stack = new Stack<>();
        ListNode curr = head;
        int count = 0;

        while (count < k && curr != null) {
            stack.push(curr);
            curr = curr.next;
            count++;
        }

        if (count != k) return head;  // 若本组节点不够 k 个，则不反向

        // 2. 反向本组的这 k 个节点
        ListNode dummyHead = new ListNode(), prev = dummyHead;

        while (!stack.isEmpty()) {
            prev.next = stack.pop();
            prev = prev.next;
        }
        prev.next = reverseKGroup0(curr, k);  // 将下一组反向的结果链到本组尾节点上

        // 3. 最后返回本组反向的结果
        return dummyHead.next;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 2, 3, 4});
        log(reverseKGroup0(l1, 2));  // expects 2->1->4->3->NULL

        ListNode l2 = createLinkedList(new int[]{1, 2, 3, 4});
        log(reverseKGroup0(l2, 3));  // expects 3->2->1->4->NULL

        ListNode l3 = createLinkedList(new int[]{1, 2, 3, 4, 5, 6, 7, 8});
        log(reverseKGroup0(l3, 3));  // expects 3->2->1->6->5->4->7->8->NULL. (最后一组不够3个 ∴ 应该保持不变)

        ListNode l4 = createLinkedList(new int[]{1, 2, 3});
        log(reverseKGroup0(l4, 1));  // expects 1->2->3->NULL
    }
}
