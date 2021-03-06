package com.scau.beyondboy.bindercommon;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
/**
 * @Author: beyondboy
 * @Gmail: guoli.lxgl@alibaba-inc.com
 * @Date: 2016-05-20
 * @Time: 21:02
 * 响应具体实现
 */
public class FibonacciResponse implements Parcelable{
    private final long result;

    private final long timeInMillis;

    public FibonacciResponse(long result, long timeInMillis) {
        this.result = result;
        this.timeInMillis = timeInMillis;
    }


    public long getResult() {
        return result;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(this.result);
        parcel.writeLong(this.timeInMillis);
    }

    public static final Parcelable.Creator<FibonacciResponse> CREATOR = new Parcelable.Creator<FibonacciResponse>() {
        public FibonacciResponse createFromParcel(Parcel in) {
            return new FibonacciResponse(in.readLong(), in.readLong());
        }

        public FibonacciResponse[] newArray(int size) {
            return new FibonacciResponse[size];
        }
    };
}
