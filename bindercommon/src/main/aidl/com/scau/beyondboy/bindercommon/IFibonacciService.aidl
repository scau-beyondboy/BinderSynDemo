// BinderService.aidl
package com.scau.beyondboy.bindercommon;
import com.scau.beyondboy.bindercommon.FibonacciResponse;
import com.scau.beyondboy.bindercommon.FibonacciRequest;

// Declare any non-default types here with import statements

interface IFibonacciService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
            long fibJR(in long n);
            long fibJI(in long n);
            long fibNR(in long n);
            long fibNI(in long n);
            FibonacciResponse fib(in FibonacciRequest request);
}
