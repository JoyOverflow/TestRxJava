package ouyj.hyena.com.testrxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ObservableActivity extends AppCompatActivity {

    private static final String TAG = ObservableActivity.class.getSimpleName();


    /**
     * (1)创建被观察者（当它被订阅时subscribe方法会自动调用，其内事件依次触发）
     * @return
     */
    private Observable<String> getObservable() {
        //创建一个被观察者
        Observable<String> myObservable  = Observable.create(new ObservableOnSubscribe<String>() {
            //参数为发射器
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //发射数据
                emitter.onNext("hello");
                emitter.onNext("ouyang");
                emitter.onNext("jun,");
                emitter.onNext("myxing,");
                emitter.onNext("zhanhao");
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

        /*
        RxJava中内置的线程选项
        Schedulers.io：用于网络、文件读写等io密集型操作
        Schedulers.computation：CPU密集型的大量计算操作
        Schedulers.newThread：一个常规的新线程；
        AndroidSchedulers.mainThread：代表Android的UI主线程
        */
        //常规方式创建Observable
        Button btnCreate = findViewById(R.id.btn_create);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //(3)将Observable与Subscriber关联起来（发生订阅）
                getObservable()
                        .subscribeOn(Schedulers.io()) //指定发射数据和事件的线程（上游，被订阅方）
                        .observeOn(AndroidSchedulers.mainThread()) //指定观察者（下游）发生的线程
                        .subscribe(getObserver());//订阅
            }
        });


        //快捷方式创建Observable
        //Just创建被观察者并自动调用onNext发射数据，自动发生订阅和发射完成事件
        Button btnJust = findViewById(R.id.btn_just);
        btnJust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发射两条数据，观察者将触发两次Next
                Observable.just(
                        String.valueOf(System.currentTimeMillis()),
                        String.valueOf(System.currentTimeMillis())
                )
                //被观察者在线程中执行操作
                .subscribeOn(Schedulers.io())
                //观察者在UI主线程执行
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
            }
        });


        //defer（推迟）是等待observable与observer建立订阅关系时，才会创建被观察者
        Button btnDefer = findViewById(R.id.btn_defer);
        btnDefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Disposable disposer = Observable.defer(
                        new Callable<ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> call() throws Exception {
                                return Observable.create(new ObservableOnSubscribe<Integer>() {
                                    @Override
                                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                                        emitter.onNext(42);
                                        emitter.onNext(43);
                                        emitter.onNext(45);
                                    }
                                });
                            }
                        }
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.d(TAG, o.toString());
                    }
                });
            }
        });



        //自动发生订阅和完成事件（类似于Just，但发送的是集合）
        Button btnFrom = findViewById(R.id.btn_from);
        btnFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //集合内三个元素，观察者的Next将触发三次
                List<String> list=new ArrayList<>();
                list.add("42");
                list.add("43");
                list.add("46");
                Observable.fromIterable(list)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getObserver());
            }
        });


        //间隔性不断发射数据
        Button btnInterval = findViewById(R.id.btn_interval);
        btnInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //先延迟2秒，再每隔3秒发射一次数据（从0不断自增）
                Observable.interval(2,3, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                Log.d(TAG, aLong.toString());
                            }
                        });
            }
        });


        //创建被观察者并发射指定范围内数据
        Button btnRange = findViewById(R.id.btn_range);
        btnRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发射0-9共10个元素
                Disposable disposer = Observable.range(0, 10)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer i) throws Exception {
                                Log.d(TAG, i.toString());
                            }
                        });
            }
        });



        //延迟指定时间后发射一次数据（默认为0）
        Button btnTimer = findViewById(R.id.btn_timer);
        btnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,String.format("现在时间：%d",System.currentTimeMillis()));
                Disposable disposable = Observable.timer(4, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                Log.d(TAG,
                                        String.format("现在时间：%d %s",System.currentTimeMillis(),aLong)
                                );
                            }
                        });
            }
        });

        //将发射的数据再重复指定次数
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

            private Disposable disposer;

            //是否发生订阅（参数为一次性丢弃）
            @Override
            public void onSubscribe(Disposable d) {
                //获取到清除对象（它可随时切断，让当前观察者不再接收上游事件）
                disposer = d;
                Log.d(TAG, "onSubscribe！");
            }
            //接收到的数据
            @Override
            public void onNext(String s) {
                String str=String.format("onNext：%s",s);
                //收到特定字串则切断订阅，不再接收
                if(str.equals("myxing"))
                    disposer.dispose();

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
