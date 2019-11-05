package ouyj.hyena.com.testrxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SimpleActivity extends AppCompatActivity {

    private static final String TAG = SimpleActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);

        //查找按钮引用并设置事件
        Button btn = findViewById(R.id.btn_test);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClick！");

                //创建被观察者并自动调用onNext发射数据,发射完自动onComplete
                Observable.just("Cricket", "Football")
                        //被观察者在线程中执行操作
                        .subscribeOn(Schedulers.io())
                        //观察者在UI主线程执行
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getObserver());
            }
        });
    }

    /**
     * 创建观察者（用来接收被观察者发射的数据和事件）
     * @return
     */
    private Observer<String> getObserver() {
        //实现Observer接口
        return new Observer<String>() {
            //是否发生订阅
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe！");
            }
            //接收到的数据
            @Override
            public void onNext(String s) {
                String str=String.format("onNext：%s",s);
                Log.d(TAG, str);
            }
            //接收到异常事件
            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError！");
            }
            //接收到完成事件
            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete！");
            }
        };
    }




}
