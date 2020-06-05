
import static Utils.Helpers.*;

/*
 * 时间复杂度：
 *
 * - 基础
 *   - O (big 0): In academia, it describes an upper bound on the time.
 *   - Ω (big omega): In academia, it describes an lower bound on the time.
 *   - In industry (and therefore in interviews), people seem to have merged Ω and O together.
 *
 * - 均摊时间/均摊复杂度（Amortized Time/Complexity）
 *   - For dynamically resizing array like ArrayList, X insertions take 0(2X) time. The amortized time for
 *     each insertion is 0(1).
 *
 * - Recursive Runtimes
 *                            f(4)
 *                     /               \
 *                  f(3)               f(3)
 *                /      \            /     \
 *             f(2)     f(2)       f(2)     f(2)
 *            /   \    /    \     /   \     /   \
 *          f(1) f(1) f(1) f(1) f(1) f(1) f(1) f(1)
 *
 *   Q: How many calls are in this tree?（该问题实际上是在问，深度为 n 的二叉树有几个节点？）
 *   A: There will be 2^0 + 2^1 + 2^2 + 2^3 ... + 2^n = 2^(n+1) - 1 nodes, namely 2^(n+1) - 1 calls, and O(2^n) runtime.
 *   - 二叉递归计算每往下递归一层就会多出一倍的计算量，所以时间复杂度是 O(2^n)；而二叉递归查找只会经过树高度个节点，因此是 O(logn)。
 *   - 二叉递归计算的空间复杂度是 O(n)  ∵ 虽然有 O(2^n) 个节点，但只有 O(n) 个节点会同时存在。
 *
 * - 建立数据规模概念：
 *   - O(n^2) 的算法大约可以在1s内处理 10^4 级别的数据；
 *   - O(nlogn) 的算法大约可以在1s内处理 10^7 级别的数据；
 *   - O(n) 的算法大约可以在1s内处理 10^8 级别的数据；
 *   - O(logn) 的算法能处理的数据级别更大更大；
 *   这个规模并不准确，因为忽略了系数的影响，只作为粗略参考。
 *
 * */

public class TimeComplexity {
    // O(1)
    public void swap(int i, int j) {
        int temp = i;
        i = j;
        j = temp;
    }

    // 这个方法虽然有双重循环，但内层循环次数跟 n 无关，因此循环次数是 30n，而非 n^2，因此是 O(n)
    public void printSth(int n) {
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= 30; j++) {
                log(i + j);
            }
        }
    }

    // 二分查找每次迭代都会抛弃一半的元素，第1次 n 个元素，第2次 n/2 个元素，第3次 n/4 个元素，...，第?次1个元素；
    // 要想知道第?次后只剩下1个元素，相当于求 n 除以几次2之后等于1，即求以2为底的对数，因此的复杂度为 O(logn)。
    public int binarySearch(int arr[], Comparable e) {
        int l = 0, r = arr.length - 1;
        while (r > l) {
            int mid = (r - l) / 2 + l;
            if (e.compareTo(arr[mid]) < 0) r = mid - 1;
            else if (e.compareTo(arr[mid]) > 0) l = mid + 1;
            else return mid;
        }
        return -1;
    }

    // 该方法主要有两部分组成：while 循环、reverse()，分别讨论它们的复杂度后得到总体复杂度：O(logn)
    public String intToStr(long num) {
        StringBuilder s = new StringBuilder();
        while (num != 0) {
            s.append("" + num % 10);
            num /= 10;  // 每次 num 除10，直到等于0，因此该循环是 O(logn)
        }
        return s.reverse().toString();  // reverse 本身是 O(n) 的，但这里 num 的长度是 n，而 s 的长度是 log(num)，因此 reverse 也是 O(logn)
    }

    // 对内外层循环分别讨论得到总体复杂度 O(nlogn)
    public void foo(int n) {
        for (int i = 0; i < n; i += i)  // 这里相当于每次对 i 进行平方，因此这层循环的递进速度是指数级别的，而复杂度就是对数级别的，因此是 O(logn)
            for (int j = 0; j < n; j++)  // 内层循环是标准的 O(n)
                log(i + j);
    }

    // 判断一个数是否是素数：O(sqrt(n))
    public boolean isPrime(int n) {
        for (int i = 2; i * i <= n; i++) {  // i 的范围是 [2, sqrt(n)]，因此复杂度为 O(sqrt(n))
            if (n % i == 0)
                return false;
        }
        return true;
    }

    // 计算 x 的 n 次幂：O(logn)
    // 如果对 x 进行连乘来计算 n 次幂，则复杂度胃 O(n)；而采用这种递归方法则充分利用了已计算出来的结果化简算法复杂度
    public double pow(double x, int n) {
        assert n >= 0;
        if (n == 0) return 1.0;
        double t = pow(x, n / 2);
        if (n % 2 != 0)
            return t * t * x;  // 例如 x=2, n=5，则 2^5 = 2^2 * 2^2 * 2
        return t * t;
    }

    /* 内层遍历元素个数递减：
     * - 数遍历次数：第一次 0~n（n 个元素），第二次 1~n（n-1 个元素），第三次 2~n（n-2 个元素）…… 直到最后一次 n~n（1个元素）
     *   等差数列求和公式后为 n(n+1)/2，因此复杂度仍是 O(n^2)。
     * */
    public void printUnorderedPairs(int[] array) {
        for (int i = 0; i < array.length; i++)
            for (int j = i + 1; j < array.length; j++)
                System.out.println(array[i] + "," + array[j]);
    }

    /*
     * Question: Suppose we had an algorithm that took in an array of strings, sorted each string, and then sorted
     *           the full array. What would the runtime be?
     * Solution: 1. Let m be the length of the longest string and n be the length of the array.
     *           2. Sorting each string is O(mlogm), do this for n strings, so that's O(n * mlogm).
     *           3. When sorting the n strings, first we need to compare them. Each string comparison takes O(m) time.
     *              Sorting the array requires O(nlogn) comparisons, therefore this will take O(m * nlogn) time.
     *           4. Adding up these parts, you get O(n * mlogm + m * nlogn) = O(n * m * (logm + logn).
     * This is it. There is no way to reduce it further.
     * */

    /*
     * Sum of the values of all the nodes in a balanced binary search tree
     * 两种思路：
     * 1. 因为会遍历所有节点，因此是 O(n)；
     * 2. 因为二叉树节点个数 n = 2^depth，因此要遍历每个节点就需 O(2^depth) = O(2^(logn)) = O(n)。
     * */
    public int sum(TreeNode node) {
        if (node == null)
            return 0;
        return sum(node.left) + node.val + sum(node.right);
    }

    /*
     * 求 n 的阶乘（n factorial）：因为只有一条分支（不像上面二叉树那样有两个分支），因此是线性的，复杂度为 O(n)
     * */
    public int factorial(int n) {
        if (n < 0) return -1;
        else if (n == 0) return 1;
        else return n * factorial(n - 1);
    }

    /*
     * Computes the Nth Fibonacci number.
     * 对比上面的求阶乘的代码，相同点是递归深度都是输入参数 n，不同点是这段里面有2个分支，因此是 O(2^n)，这是个非常高的复杂度。
     * */
    public int fib(int n) {
        if (n < 2) return n;
        return fib(n - 1) + fib(n - 2);
    }

    /*
     * Prints all Fibonacci numbers from O to n.
     * 易错点：很容易想成 fib 是 O(2^n)，打印 n 个，因此是 O(n2^n)。错误之处在于 i 是从 0~n 变化的。实际上的运行次数：
     * 2^0 + 2^1 + 2^2 ... 2^n，等比数列求和得 2^(n+1)-1，因此复杂度还是 O(2^n)。
     * */
    public void allFib(int n) {
        for (int i = 0; i < n; i++)
            System.out.println(i + " : "+ fib(i));  // fib 方法同上
    }

    /*
     * Prints all Fibonacci numbers from O to n using cache (or called memory search).
     * 在使用缓存之后，每次计算 fib(i) 时，fib(i-1) 和 fib(i-2) 都已经存在于缓存之中了，因此每次 fib(i) 的计算复杂度为 O(1)，
     * 从而总体复杂度为 O(n)。换一种方式理解：每个数字都只计算一次，因此总共计算 n 次，即 O(n)。
     * */
    public void allFibUsingCache(int n) {
        int[] memo = new int[n + 1];  // 范围是 [0, n]，因此开辟 n+1 的空间（或使用 HashMap 也可）
        for (int i = 0; i < n; i++)
            log(i + " : " + fib(i, memo));
    }

    public int fib(int n, int[] memo) {
        if (n < 2) return n;
        if (memo[n] > 0) return memo[n];
        memo[n] = fib(n - 1, memo) + fib(n - 2, memo);
        return memo[n];
    }

    /*
     * Prints the powers of 2 from 1 through n. For example, if n = 4, it would print 1, 2, 4.
     * 因为每次递归都除以2，递归深度为 logn，因此时间复杂度为 O(logn)。
     * */
    public int powers0f2(int n) {
        if (n < 1) return 0;
        if (n == 1) {
            System.out.println(1);
            return 1;
        }
        int prev = powers0f2(n / 2);
        int curr = prev * 2;
        System.out.println(curr);
        return curr;
    }
}
