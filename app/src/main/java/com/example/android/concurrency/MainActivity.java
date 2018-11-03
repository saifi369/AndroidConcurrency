package com.example.android.concurrency;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyTag";
    private static final String MESSAGE_KEY = "message_key";
    private ScrollView mScroll;
    private TextView mLog;
    private ProgressBar mProgressBar;
    private MyTask mTask;
    private boolean mTaskRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

    }

    public void runCode(View v) {

//        log("Running code");
//        displayProgressBar(true);

        if(mTaskRunning && mTask!=null){
            mTask.cancel(true);
            mTaskRunning=false;
        }else {
            mTask = new MyTask();
            mTask.execute("Red", "Green", "Blue", "Yellow");
            mTaskRunning=true;
        }

    }

    private void initViews() {
        mScroll = (ScrollView) findViewById(R.id.scrollLog);
        mLog = (TextView) findViewById(R.id.tvLog);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    private void log(String message) {
        Log.i(TAG, message);
        mLog.append(message + "\n");
        scrollTextToEnd();
    }

    private void scrollTextToEnd() {
        mScroll.post(new Runnable() {
            @Override
            public void run() {
                mScroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private void displayProgressBar(boolean display) {
        if (display) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void clearOutput(View view) {
        mLog.setText("");
        scrollTextToEnd();
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

            log(values[0]);

        }

        @Override
        protected void onPostExecute(String s) {
            log(s);

        }

        @Override
        protected void onCancelled() {
            log("task has been canelled");
        }

        @Override
        protected void onCancelled(String s) {
            log("Cancelled with return data "+s);
        }
    }
}