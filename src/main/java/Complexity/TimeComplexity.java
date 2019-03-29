package Complexity;

import static Utils.Helpers.*;

/*
* 时间复杂度：
*
* 建立数据规模概念：
* - O(n^2) 的算法大约可以在1s内处理 10^4 级别的数据；
* - O(nlogn) 的算法大约可以在1s内处理 10^7 级别的数据；
* - O(n) 的算法大约可以在1s内处理 10^8 级别的数据；
* - O(logn) 的算法能处理的数据级别更大更大；
* 这个规模并不准确，因为忽略了系数的影响，只作为粗略参考。
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

    public static void main(String[] args) {
        Long l = 1000000000000L;
        int[] arr = generateNearlyOrderedArr(l, 456456);
        String d = "";
        double a = System.nanoTime();
        d = binarySearch(arr);
        double c = System.nanoTime();
        log((c - a) / 1000000000.0);
        log(d);
    }
}
