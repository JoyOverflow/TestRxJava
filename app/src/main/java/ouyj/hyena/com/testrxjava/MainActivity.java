package ouyj.hyena.com.testrxjava;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onTest2Click(View view) {
        startActivity(
                new Intent(MainActivity.this, SimpleActivity.class)
        );
    }







    public void onTestClick(View view) {
        //(3)将Observable与Subscriber关联起来（发生订阅）
        getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());//订阅
    }
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
                //发射完成事件（会触发Observer的onCompleted方法）
                emitter.onComplete();
            }
        });
        return myObservable;
    }
    /**
     * (2)创建观察者（用来接收被观察者发射的数据和事件）
     * @return
     */
    private Observer<String> getObserver() {
        //实现Observer接口
        return new Observer<String>() {
            //进行订阅时触发
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
            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError！");
            }
            //接收完成事件
            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete！");
            }
        };
    }
}
