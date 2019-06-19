package examples.aaronhoskins.com.multithreadingandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity implements Async.AsyncCallback {
    TextView tvDisplay;
    Async asyncTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvDisplay = findViewById(R.id.tvDisplay);


//        Thread thread = new Thread(runnable());
//        Thread thread2 = new Thread(runnable2());
//        Log.d("TAG","Starting new Thread");
//        thread.start();
//        thread2.start();
//        Log.d("TAG","next line");

        //Looper
        LooperDemoThread looperDemoThread;
        //instantiate the looper, and handle results from the looper to the main Looper
        looperDemoThread = new LooperDemoThread(new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //get info back out of message received
                Bundle bundle = msg.getData();
                tvDisplay.setText(bundle.getString("key"));
            }
        });
        looperDemoThread.start();
        looperDemoThread.workerThreadHandler.sendMessage(new Message());


    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public Runnable runnable() {
        Runnable returnRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    EventBus.getDefault().post(new MessagingEvent("Thread 1"));
                    //Log.d("TAG", "Thread 1 is done napping");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        return returnRunnable;
    }

    public Runnable runnable2() {
        Runnable returnRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(15000);
                    EventBus.getDefault().post(new MessagingEvent("Thread 2"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        return returnRunnable;
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEvent(MessagingEvent event) {
        tvDisplay.setText(event.getMessage());
    }

    public void onClick(View view) {
        asyncTask = new Async(this);
        asyncTask.execute("Async Task");
    }

    @Override
    public void returnString(String string) {
        tvDisplay.setText(string);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        asyncTask.cancel(true);
    }
}
