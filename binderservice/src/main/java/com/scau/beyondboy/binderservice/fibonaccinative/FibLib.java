package com.scau.beyondboy.binderservice.fibonaccinative;

/**
 * @Author: beyondboy
 * @Gmail: guoli.lxgl@alibaba-inc.com
 * @Date: 2016-05-20
 * @Time: 19:15
 * 计算非波那契數列的具体实现
*/
public class FibLib {
    /**java层递归计算非波那契數列*/
    public static long fibJR(long n) { // <1>
        return n <= 0 ? 0 : n == 1 ? 1 : fibJR(n - 1) + fibJR(n - 2);
    }
    /**java层迭代计算非波那契數列*/
    public static long fibJI(long n) { // <2>
        long previous = -1;
        long result = 1;
        for (long i = 0; i <= n; i++) {
            long sum = result + previous;
            previous = result;
            result = sum;
        }
        return result;
    }
    /**native层递归计算非波那契數列*/
    public native static long fibNR(long n); // <3>
    /**native层迭代计算非波那契數列*/
    public native static long fibNI(long n); // <4>
    static {
        System.loadLibrary("binder-service");
    }
}
