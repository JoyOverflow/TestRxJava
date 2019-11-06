package ouyj.hyena.com.testrxjava;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity {

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
    public void observerClick(View view) {
        startActivity(
                new Intent(MainActivity.this, ObserverActivity.class)
        );
    }
}
