package examples.aaronhoskins.com.multithreadingandroid;

import android.os.AsyncTask;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

public class Async extends AsyncTask<String, String, String> {
    AsyncCallback asyncCallback;
    public Async(AsyncCallback asyncCallback) {
        this.asyncCallback = asyncCallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        for(int i = 0 ; i < 10; i++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            strings[0] = "Run Number + " + i;
            publishProgress(strings);
        }

        return strings[strings.length - 1];
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Log.d("TAG", "onProgressUpdate: " + values[0]);

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //EventBus.getDefault().post(new MessagingEvent(s));
        asyncCallback.returnString(s);
    }

    interface AsyncCallback{
        void returnString(String string);
    }


}
