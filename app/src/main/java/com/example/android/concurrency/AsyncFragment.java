package com.example.android.concurrency;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

public class AsyncFragment extends Fragment {

    private static final String TAG = "MyTag";
    private MyTaskHandler mTaskHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    public void runTask(String... params){
        MyTask mTask = new MyTask();
        mTask.execute(params);
    }


    public interface MyTaskHandler {
        void handleTask(String message);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.d(TAG, "onAttach: fragment attached");

        if(context instanceof MyTaskHandler){
            mTaskHandler = (MyTaskHandler) context;
        }

    }

    class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {


            for (String value : strings) {

                if(isCancelled()){
                    publishProgress("task is cancelled");
                    break;
                }

                Log.d(TAG, "doInBackground: " + value);
                publishProgress(value);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return "Download COmpleted";
        }

        @Override
        protected void onProgressUpdate(String... values) {

            mTaskHandler.handleTask(values[0]);

        }

        @Override
        protected void onPostExecute(String s) {
            mTaskHandler.handleTask(s);

        }
    }
}
