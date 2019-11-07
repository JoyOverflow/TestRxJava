package ouyj.hyena.com.testrxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class OperatorsActivity extends AppCompatActivity {

    private static final String TAG = OperatorsActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operators);


        //将（被观察者）发射数据，经映射函数处理后再传递给观察者
        Button btnMap = findViewById(R.id.btn_map);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Observable.just(1,2,3,4,5)
                        .map(
                                new Function<Integer, Integer>(){
                                    @Override
                                    public Integer apply(Integer integer) throws Exception {
                                        return (integer * 3);
                                    }
                                }
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer i) throws Exception {
                                Log.d(TAG, i.toString());
                            }
                        });
            }
        });



        //解决有依赖的网络请求（不保证子事件的完成顺序，因各子任务耗时不同）
        //最后将子事件的结果合并成一个Observable将所有数据发射出去
        Button btnFlatmap= findViewById(R.id.btn_flatmap);
        btnFlatmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发送3个数据主事件
                List<Integer> list = Arrays.asList(1,2,3);
                Observable.fromIterable(list)
                        .flatMap(
                                //每个主事件又分2个子事件
                                new Function<Integer, Observable<String>>() {
                                    @Override
                                    public Observable<String> apply(Integer integer) throws Exception {
                                        List<String> lists = new ArrayList<>();
                                        lists.add(String.format("%d 子任务A",integer));
                                        lists.add(String.format("%d 子任务B",integer));
                                        if(integer == 1)
                                            return Observable.fromIterable(lists).delay(10, TimeUnit.MILLISECONDS);
                                        else
                                            return Observable.fromIterable(lists);
                                    }
                                }
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Log.d(TAG, s);
                            }
                        });
            }
        });







        Button btnConcat= findViewById(R.id.btn_concat);
        btnConcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Button btnZip= findViewById(R.id.btn_zip);
        btnZip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
