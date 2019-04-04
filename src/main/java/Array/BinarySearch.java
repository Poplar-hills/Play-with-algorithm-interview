package Array;

import static Utils.Helpers.log;

public class BinarySearch {
    public static int binarySearch(Comparable[] arr, Comparable e) {
        return binarySearch(arr, e, 0, arr.length - 1);
    }

    private static int binarySearch(Comparable[] arr, Comparable e, int l, int r) {
        if (l > r) return -1;
        int mid = (r - l) / 2 + l;
        if (e.compareTo(arr[mid]) < 0)
            return binarySearch(arr, e, 0, mid - 1);
        if (e.compareTo(arr[mid]) > 0)
            return binarySearch(arr, e, mid + 1, r);
        return mid;
    }

    public static int binarySearchNR(Comparable[] arr, Comparable e) {
        int l = 0, r = arr.length - 1;
        while (l <= r) {
            int mid = (r - l) / 2 + l;
            if (e.compareTo(arr[mid]) < 0) r = mid - 1;
            else if (e.compareTo(arr[mid]) > 0) l = mid + 1;
            else return mid;
        }
        return -1;
    }

    public static void main(String[] args) {
        Integer[] arr = new Integer[]{0,2,4,6,8,10};

        log(binarySearch(arr, 4));
        log(binarySearch(arr, 10));
        log(binarySearch(arr, 20));

        log(binarySearchNR(arr, 4));
        log(binarySearchNR(arr, 10));
        log(binarySearchNR(arr, 20));
    }
}
