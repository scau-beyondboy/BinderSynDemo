package com.scau.beyondboy.binderclient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.scau.beyondboy.bindercommon.FibonacciRequest;
import com.scau.beyondboy.bindercommon.FibonacciResponse;
import com.scau.beyondboy.bindercommon.IFibonacciService;
import com.scau.beyondboy.binderservice.FibonacciService;
import java.util.Locale;
/**
 * @Author: beyondboy
 * @Gmail: guoli.lxgl@alibaba-inc.com
 * @Date: 2016-05-21
 * @Time: 12:42
 */
public class FibonacciActivity extends Activity implements View.OnClickListener,ServiceConnection{
    private static final String TAG = "FibonacciActivity";

    private EditText input;

    private Button button; // 触发非波那契數列的计算

    private RadioGroup type; // 非波那契數列实现类型

    private TextView output; // 输出非波那契數列的计算结果

    private IFibonacciService service; // 引用非波那契數列的服务

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.main);
        this.input = (EditText) super.findViewById(R.id.input);
        this.button = (Button) super.findViewById(R.id.button);
        this.type = (RadioGroup) super.findViewById(R.id.type);
        this.output = (TextView) super.findViewById(R.id.output);
        this.button.setOnClickListener(this);
        // 当我们链接上我们的服务才让按钮变为可按
        this.button.setEnabled(false);
    }

    @Override
    protected void onResume() {
      //  Log.d(TAG, "onResume()'ed");
        super.onResume();
        Intent intent=new Intent(this, FibonacciService.class);
       // intent.addCategory(Intent.CATEGORY_DEFAULT);
        //开启服务
        if (!super.bindService(intent,
                this, BIND_AUTO_CREATE)) {
            Log.w(TAG, "Failed to bind to service");
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()'ed");
        super.onPause();
        //停止服务
        super.unbindService(this);
    }

    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d(TAG, "onServiceConnected()'ed to " + name);
        // 获得我们的IFibonacciService
        this.service = IFibonacciService.Stub.asInterface(service);
        this.button.setEnabled(true);
    }

    public void onServiceDisconnected(ComponentName name) {
        Log.d(TAG, "onServiceDisconnected()'ed to " + name);
        this.service = null;
        this.button.setEnabled(false);
    }

    public void onClick(View view) {
        final long n;
        String s = this.input.getText().toString();
        if (TextUtils.isEmpty(s)) {
            return;
        }
        try {
            n = Long.parseLong(s);
        } catch (NumberFormatException e) {
            this.input.setError(super.getText(R.string.input_error));
            return;
        }

        final FibonacciRequest.Type type;
        switch (FibonacciActivity.this.type.getCheckedRadioButtonId()) {
            case R.id.type_fib_jr:
                type = FibonacciRequest.Type.RECURSIVE_JAVA;
                break;
            case R.id.type_fib_ji:
                type = FibonacciRequest.Type.ITERATIVE_JAVA;
                break;
            case R.id.type_fib_nr:
                type = FibonacciRequest.Type.RECURSIVE_NATIVE;
                break;
            case R.id.type_fib_ni:
                type = FibonacciRequest.Type.ITERATIVE_NATIVE;
                break;
            default:
                return;
        }
        final FibonacciRequest request = new FibonacciRequest(n, type);

        // 展示计算进度
        final ProgressDialog dialog = ProgressDialog.show(this, "",
                super.getText(R.string.progress_text), true);
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    long totalTime = SystemClock.uptimeMillis();
                    FibonacciResponse response = FibonacciActivity.this.service
                            .fib(request);
                    totalTime = SystemClock.uptimeMillis() - totalTime;
                    // 获取结果
                    return String.format(Locale.ENGLISH,
                            "fibonacci(%d)=%d\nin %d ms\n(+ %d ms)", n,
                            response.getResult(), response.getTimeInMillis(),
                            totalTime - response.getTimeInMillis());
                } catch (RemoteException e) {
                    Log.wtf(TAG, "Failed to communicate with the service", e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                dialog.dismiss();
                if (result == null) {
                    // 处理错误
                    Toast.makeText(FibonacciActivity.this, R.string.fib_error,
                            Toast.LENGTH_SHORT).show();
                } else {
                    // 展示结果
                    FibonacciActivity.this.output.setText(result);
                }
            }
        }.execute();
    }

   }
