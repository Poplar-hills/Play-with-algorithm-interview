package HashTable.S4_SlidingWindow;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static Utils.Helpers.log;

/*
 * Contains Duplicate II
 *
 * - 对于整型数组 nums 和整数 k，是否存在索引 i 和 j，使得 nums[i] == nums[j]，且 abs(i - j) <= k。
 * */

public class L219_ContainsDuplicateII {
    /*
     * 解法1：滑动窗口 + Set
     * - 思路：题中说"i 和 j 之差不超过 k"，可理解为有一个长度为 k 的窗口在数组上滑动，若窗口内存在重复元素即可返回 true。
     * - 实现：
     *   1. 窗口采用双指针实现，窗口定义：[l,r)，r 指向待进入窗口的下一个元素；
     *   2. “是否存在重复元素”的问题由 Set 来回答。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean containsNearbyDuplicate(int[] nums, int k) {
        if (nums == null || nums.length < 2 || k == 0) return false;
        Set<Integer> window = new HashSet<>();
        int l = 0, r = 0, n = nums.length;   // 窗口定义：[l,r)，r 指向待进入窗口的下一个元素

        while (window.size() < k && r < n) {  // 先将窗口长度扩展到 k（∵ 窗口大小是 [l,r) ∴ window.size() 最大只能是 k-1）
            if (window.contains(nums[r])) return true;  // 该过程中若发现重复，则直接返回 true
            window.add(nums[r]);
            r++;
        }
        while (r < n) {                      // 开始让窗口以固定长度滑动
            if (window.contains(nums[r])) return true;
            window.add(nums[r++]);           // 先排除左边的最后一个元素
            window.remove(nums[l++]);        // 再纳入右边的下一个元素
        }

        return false;
    }

    /**
     * 解法2：滑动窗口 + Set
     * - 思路：与解法1相同。
     * - 实现：窗口定义为 [l,r]，r 指向窗口内最右侧的元素。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     */
    public static boolean containsNearbyDuplicate2(int[] nums, int k) {
        if (nums == null || nums.length < 2 || k == 0) return false;
        Set<Integer> window = new HashSet<>();
        int l = 0, r = 0, n = nums.length;
        window.add(nums[0]);                       // 根据窗口定义，窗口中要预放入第0个元素

        while (window.size() <= k && r < n - 1) {  // 👉🏻 ∵ 窗口大小为 [l,r] ∴ window.size() 最大值是 k（与解法1不同点）
            int next = nums[++r];                  // ∵ 这里要 ++r ∴ 上面要保证 r < n-1
            if (window.contains(next)) return true;
            window.add(next);
        }
        while (r < n - 1) {
            int last = nums[l++], next = nums[++r];
            window.remove(last);
            if (window.contains(next)) return true;
            window.add(next);
        }

        return false;
    }

    /*
     * 解法3：查找表 Set + 滑动窗口
     * - 思路：同样是滑动窗口，但让窗口中最多只保存 k 个元素，然后在遍历 nums 时判断每个元素是否在窗口中有 duplicate。
     * - 实现：
     *   1. 👉🏻 注意！限制条件 abs(i-j) <= k 意味着在 nums=[0,1,0], k=2 中，两个0是满足该条件的，即判断 k+1 个元素中是否有
     *      duplicate（而非 k 个元素中）∴ 在实现时要将窗口大小设为 k，判断窗口外的第 k+1 个元素是否在窗口内有 duplicate；
     *   2. 窗口大小固定设为 k，即窗口中最多只保存 k 个元素，但在实现时 Set 要开辟 k+1 空间（见代码）；
     *   3. ∵ 固定了窗口大小，并且用 add to/remove from 窗口来扩展/收缩窗口 ∴ 无需像解法1一样使用 l,r 来手动操作窗口边界。
     * 在"判断每个元素是否在窗口中有 duplicate"时，该元素此时还在窗口之外，
     * - 时间复杂度 O(n)，空间复杂度 O(k)。
     * */
    public static boolean containsNearbyDuplicate3(int[] nums, int k) {
        Set<Integer> window = new HashSet<>(k + 1);  // 最多存 k+1 个元素

        for (int i = 0; i < nums.length; i++) {
            if (window.contains(nums[i])) return true;  // 先判断当前元素（此时还在窗口之外）是否在窗口内有 duplicate
            window.add(nums[i]);
            if (window.size() == k + 1)      // Set 中元素个数达到 k+1 之前只添加不删除（巧妙之处）
                window.remove(nums[i - k]);  // 在 [0,1,2,0] 中，若 i=3，则要从 Set 中删除的就是 i=0 的元素
        }

        return false;
    }

    /*
     * 解法4：查找表 Map
     * - 思路：∵ 要找的是索引差在 k 之内的重复元素 ∴ 可以利用 map.put() 方法，在插入元素时，若已存在则返回其之前的索引，
     *   并判断与当前的索引之差是否小于 k。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean containsNearbyDuplicate4(int[] nums, int k) {
        Map<Integer, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            Integer lastIdx = indexMap.put(nums[i], i);  // 若 nums[i] 在 Map 中已存在则返回其之前的 index，否则返回 null
            if (lastIdx != null && i - lastIdx <= k)
                return true;
        }
        return false;
    }

    public static void main(String[] args) {
        log(containsNearbyDuplicate2(new int[]{1, 0, 1, 1}, 1));        // expects true
        log(containsNearbyDuplicate2(new int[]{4, 1, 2, 3, 1}, 3));     // expects true
        log(containsNearbyDuplicate2(new int[]{1, 2, 3, 1, 2, 3}, 2));  // expects false
        log(containsNearbyDuplicate2(new int[]{1}, 1));                 // expects false
    }
}
