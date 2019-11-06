package ouyj.hyena.com.testrxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ObservableActivity extends AppCompatActivity {

    private static final String TAG = ObservableActivity.class.getSimpleName();


    /**
     * (1)创建被观察者（当被订阅时，subscribe方法会自动调用，其内事件会依次触发）
     * @return
     */
    private Observable<String> getObservable() {
        //创建一个被观察者
        Observable<String> myObservable  = Observable.create(new ObservableOnSubscribe<String>() {
            //
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //发射数据
                emitter.onNext("hello");
                emitter.onNext("ouyang");
                emitter.onNext("jun");
                //发射完成事件（触发Observer的onCompleted方法）
                emitter.onComplete();
            }
        });
        return myObservable;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observable);


        //常规方式创建Observable
        Button btnCreate = findViewById(R.id.btn_create);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //(3)将Observable与Subscriber关联起来（发生订阅）
                getObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getObserver());//订阅
            }
        });
        //快捷方式创建Observable
        Button btnJust = findViewById(R.id.btn_just);
        btnJust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //创建被观察者并自动调用onNext发射数据,发射完自动onComplete
                Observable.just("Cricket", "Football")
                        //被观察者在线程中执行操作
                        .subscribeOn(Schedulers.io())
                        //观察者在UI主线程执行
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getObserver());
            }
        });



        Button btnFrom = findViewById(R.id.btn_from);
        btnFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        Button btnInterval = findViewById(R.id.btn_interval);
        btnInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        Button btnRange = findViewById(R.id.btn_range);
        btnRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        Button btnTimer = findViewById(R.id.btn_timer);
        btnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Button btnRepeat = findViewById(R.id.btn_repeat);
        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //重复指定次数（发射数据）
                Observable.just("Cricket", "Football").repeat(2)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getObserver());
            }
        });
    }

    /**
     * (2)创建观察者（用来接收被观察者发射的数据和事件）
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
