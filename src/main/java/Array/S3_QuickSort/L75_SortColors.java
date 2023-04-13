package Array.S3_QuickSort;

import static Utils.Helpers.log;
import static Utils.Helpers.swap;

import java.util.Arrays;
import java.util.Random;

/*
 * Sort Colors
 *
 * - Given an array with n objects colored red, white or blue, sort them in-place so that objects of the same
 *   color are adjacent, with the colors in the order red, white and blue.
 * - Here, we will use the integers 0, 1, and 2 to represent the color red, white, and blue respectively.
 * - Note: You are not suppose to use the library's sort function for this problem.
 *
 * - 分析：该题本质上就是数组排序，但是不同于一般的排序，题中给出了更多信息：只有3种颜色，且3种颜色的值是固定的0, 1, 2 ∴ 有3种思路：
 *   1. 使用通用的快排、归并排序算法，最低是 O(nlogn) 的复杂度；
 *   2. 从信息中可知数组元素的取值范围是固定的，且范围很小 ∴ 适用计数排序，复杂度为 O(2n)（遍历数组2次）；
 *   3. ∵ 数组元素刚好只有3种取值 ∴ 可在三路快排的基础上去掉递归过程，只进行一轮 partition 即可排好，复杂度为 O(n)。
 *
 * - 可见很多面试题实际上是对经典算法思想的应用，因此：
 *   1. 对经典算法思想要熟练掌握；
 *   2. 对这些思想的应用场景和使用套路要有感觉。
 * */

public class L75_SortColors {
    /*
     * 解法1：Counting sort（计数排序）
     * - 时间复杂度 O(2n)，遍历数组2遍；
     * - 空间复杂度 O(1)。
     * */
    private static void sortColors(int[] arr) {
        int[] buckets = new int[3];      // 构造 bucket 数组，三个位置分别存储 arr 中0，1，2的个数（计数过程）
        for (int c : arr) {              // 遍历 arr 填充 bucket
            assert (c >= 0 && c <= 2);   // 在赋值之前先检元素的有效性
            buckets[c]++;
        }
        int i = 0;
        for (int b = 0; b < buckets.length; b++)  // 遍历 bucket，根据0，1，2的个数重新填充 arr
            for (int n = 0; n < buckets[b]; n++)
                arr[i++] = b;
    }

    /*
     * 解法2：Standard merge sort
     * - 思路：归并排序的过程是：
     *     1. 将数组不断二分，直到每个元素为一组；
     *     2. merge 两个各自有序的数组。
     *   归并排序的实现（merge 方法）有3个要点：
     *     1. 要开辟辅助空间；
     *     2. 遍历的是该辅助空间里的每个位置，而非遍历待合并数组；
     *     3. 遍历过程中先讨论越界情况，再讨论没越界情况。
     * - 时间复杂度 O(nlogn)，空间复杂度 O(n)。
     * */
    private static void sortColors2(int[] arr) {
        if (arr == null || arr.length == 0) return;
        mergeSort(arr, 0, arr.length - 1);
    }

    private static void mergeSort(int[] arr, int l, int r) {
        if (l >= r) return;
        int mid = (r - l) / 2 + l;    // 先不断进行二分，直到每个元素被单独分为一组
        mergeSort(arr, l, mid);
        mergeSort(arr, mid + 1, r);
        if (arr[mid] > arr[mid + 1])  // 若分解完之后若 [l,r] 还不是有序的，则进行归并，否则跳过
            merge(arr, l, mid, r);    // 对 [l,mid] 和 [mid+1,r] 两部分进行归并
    }

    private static void merge(int[] arr, int l, int mid, int r) {
        int[] copy = Arrays.copyOfRange(arr, l, r + 1);  // ∵ 要原地排序 ∴ 要 copy = arr[l,r]，即读 copy，写 arr[l,r]，让写不影响读
        int i = l, j = mid + 1;           // 将 i、j 置于两个待归并数组的起点
        for (int k = l; k <= r; k++) {    // 遍历 arr[l,r]
            if (i > mid)                  // 先讨论越界情况（左半部分已处理完）
                arr[k] = copy[j++ - l];
            else if (j > r)               // 越界情况（右半部分已处理完）
                arr[k] = copy[i++ - l];
            else if (copy[i - l] < copy[j - l])  // 再讨论未越界的情况
                arr[k] = copy[i++ - l];
            else
                arr[k] = copy[j++ - l];
        }
    }

    /*
     * 解法3：Standard 3-way quick sort
     * - 时间复杂度 O(nlogn)，空间复杂度 O(1)。
     * */
    private static void sortColors3(int[] arr) {
        if (arr == null || arr.length == 0) return;
        quickSort(arr, 0, arr.length - 1);
    }

    private static void quickSort(int[] arr, int l, int r) {
        if (l >= r) return;
        int[] ps = partition(arr, l, r);  // 快速排序在"分"上下功夫，通过 partition 找到 pivot 后再"治" pivot 两边的元素
        quickSort(arr, l, ps[0]);
        quickSort(arr, ps[1], r);
    }

    private static int[] partition(int[] arr, int l, int r) {
        int vIndex = new Random().nextInt(r - l + 1) + l;  // 随机选取 [l,r] 中的值作为 pivot index
        swap(arr, l, vIndex);                // 将 pivot 换到数组第0位上

        int v = arr[l], lt = l, gt = r + 1;  // v 即是 pivot；lt 指向 < v 的最后一个元素；gt 指向 > v 的第一个元素
        for (int i = l + 1; i < gt; ) {      // 遍历 [l+1, gt)（注意：1.gt 是动态的；2.∵ arr[l] 是 pivot ∴ 跳过）
            if (arr[i] < v)
                swap(arr, i++, ++lt);
            else if (arr[i] > v)
                swap(arr, i, --gt);
            else
                i++;
        }
        swap(arr, l, lt);          // 再将 pivot 放到正确的位置上（即所有 < v 的元素之后、所有 == v 的元素之前）
        lt--;                      // ∵ 把 pivot 放到了 lt 上 ∴ lt 需要-1才能继续指向 < v 的最后一个元素
        return new int[]{lt, gt};
    }

    /*
     * 解法4：One-pass 3-way quick sort（单次遍历的三路快排）
     * - 👉🏻 思路：∵ arr 中只有三种值 ∴ 使用三路快排，将1作为 pivot，只需一轮 partition 就可以完成，无需对每个分区再进行递归。
     *        [2, 0, 2, 1, 1, 0]
     *      l  i                 f   - arr[i] > 1 ∴ swap(i, --f)
     *        [0, 0, 2, 1, 1, 2]
     *      l  i              f      - arr[i] < 1 ∴ swap(i++, ++l)
     *        [0, 0, 2, 1, 1, 2]
     *         l  i           f      - arr[i] < 1 ∴ swap(i++, ++l)
     *        [0, 0, 2, 1, 1, 2]
     *            l  i        f      - arr[i] > 1 ∴ swap(i, --f)
     *        [0, 0, 1, 1, 2, 2]
     *            l  i     f         - arr[i] == 1 ∴ i++
     *        [0, 0, 1, 1, 2, 2]
     *            l     i  f         - arr[i] == 1 ∴ i++
     *        [0, 0, 1, 1, 2, 2]
     *            l        if        - i == f, loop ends
     * - 时间复杂度 O(n) ∵ 没有递归过程 ∴ 只遍历数组1遍 ∴ 复杂度是 O(n)；
     * - 空间复杂度 O(1)。
     * */
    private static void sortColors4(int[] arr) {
        int last0Idx = -1;                 // 指向最后一个等于0的元素
        int first2Idx = arr.length;         // 指向第一个等于2的元素
        for (int i = 0; i < first2Idx; ) {  // 手动控制 i 的自增（∵ arr[i] == 2 时 i 不需要自增）
            if (arr[i] == 0)
                swap(arr, i++, ++last0Idx);
            else if (arr[i] == 2)
                swap(arr, i, --first2Idx);
            else i++;                      // arr[i] == 1 的情况
        }
    }

    public static void main(String[] args) {
        int[] arr1 = new int[]{2, 0, 2, 1, 1, 0};
        sortColors(arr1);
        log(arr1);  // expects [0, 0, 1, 1, 2, 2]

        int[] arr2 = new int[]{0, 1, 2, 2, 1, 0};
        sortColors(arr2);
        log(arr2);  // expects [0, 0, 1, 1, 2, 2]
    }
}
