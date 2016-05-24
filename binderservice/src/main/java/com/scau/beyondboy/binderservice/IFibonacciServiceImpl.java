package com.scau.beyondboy.binderservice;

import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import com.scau.beyondboy.bindercommon.FibonacciRequest;
import com.scau.beyondboy.bindercommon.FibonacciResponse;
import com.scau.beyondboy.bindercommon.IFibonacciService;
import com.scau.beyondboy.binderservice.fibonaccinative.FibLib;
/**
 * @Author: beyondboy
 * @Gmail: guoli.xgl@alibaba-inc.com
 * @Date: 2016-05-20
 * @Time: 19:21
 * 服务端的binder对象
 */
public class IFibonacciServiceImpl extends  IFibonacciService.Stub{
    private static final String TAG = "IFibonacciServiceImpl";
    @Override
    public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

    }

    @Override
    public long fibJR(long n) throws RemoteException {
        Log.d(TAG, String.format("fibJR(%d)", n));
        return FibLib.fibJR(n);
    }

    @Override
    public long fibJI(long n) throws RemoteException {
        Log.d(TAG, String.format("fibJI(%d)", n));
        return FibLib.fibJI(n);
    }

    @Override
    public long fibNR(long n) throws RemoteException {
        Log.d(TAG, String.format("fibNR(%d)", n));
        return FibLib.fibNR(n);
    }

    @Override
    public long fibNI(long n) throws RemoteException {
        Log.d(TAG, String.format("fibNI(%d)", n));
        return FibLib.fibNI(n);
    }


    /**返回响应给客户端*/
    @Override
    public FibonacciResponse fib(FibonacciRequest request) throws RemoteException {
        Log.d(TAG,
                String.format("fib(%d, %s)", request.getN(), request.getType()));
        long timeInMillis = SystemClock.uptimeMillis();
        long result;
        switch (request.getType()) {
            case ITERATIVE_JAVA:
                result = this.fibJI(request.getN());
                break;
            case RECURSIVE_JAVA:
                result = this.fibJR(request.getN());
                break;
            case ITERATIVE_NATIVE:
                result = this.fibNI(request.getN());
                break;
            case RECURSIVE_NATIVE:
                result = this.fibNR(request.getN());
                break;
            default:
                return null;
        }
        timeInMillis = SystemClock.uptimeMillis() - timeInMillis;
        return new FibonacciResponse(result, timeInMillis) {
        };
    }
}
