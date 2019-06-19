package examples.aaronhoskins.com.multithreadingandroid;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class LooperDemoThread extends Thread {
    //Our handler for the Worker Thread
    Handler workerThreadHandler;
    Handler mainThreadHandler;

    public LooperDemoThread(Handler handler){
        super();
        mainThreadHandler = handler;
        workerThreadHandler = new android.os.Handler(Looper.myLooper()){
            @Override
            public void handleMessage(Message msg) {
                // When child thread handler get message from child thread message queue.
                Log.i("CHILD_THREAD", "Receive message from main thread.");
                Message message = new Message();
                message.what = msg.what;
                //put data in the message
                Bundle bundle = new Bundle();
                bundle.putString("key", "From Child Handler");
                message.setData(bundle);
                // Send the message back to main thread message queue use main thread message Handler.
                mainThreadHandler.sendMessage(message);
            }
        };
    }

    @Override
    public void run() {
        super.run();
        Looper.prepare();
        Looper.loop();
    }
}
