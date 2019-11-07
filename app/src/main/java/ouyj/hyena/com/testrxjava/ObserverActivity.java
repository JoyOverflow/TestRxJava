package ouyj.hyena.com.testrxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class ObserverActivity extends AppCompatActivity {

    private static final String TAG = ObserverActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observer);


        //观察者使用重载模式（单独处理数据和成功、异常事件）
        Button btnAction = findViewById(R.id.btn_action);
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Observable.just("Hello, world!")
                        .subscribe(
                                new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        String str=String.format("consumer.accept：%s",s);
                                        Log.d(TAG, str);
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        Log.d(TAG, "consumer.error");
                                    }
                                }, new Action() {
                                    @Override
                                    public void run() throws Exception {
                                        Log.d(TAG, "action.complete");
                                    }
                                });
            }
        });


        //忽略完成和异常事件，仅对发射数据进行处理
        Button btnConsumer = findViewById(R.id.btn_consumer);
        btnConsumer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Observable.just(String.valueOf(System.currentTimeMillis()))
                        .subscribe(
                                new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        String str=String.format("consumer.accept：%s",s);
                                        Log.d(TAG, str);
                                    }
                                }
                        );
            }
        });




        Button btnFunction = findViewById(R.id.btn_function);
        btnFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
