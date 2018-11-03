package com.example.android.concurrency;

import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements AsyncFragment.MyTaskHandler{

    private static final String TAG = "MyTag";
    private static final String MESSAGE_KEY = "message_key";
    private static final String FRAGMENT_TAG = "FRAGMENT_TAG";
    private ScrollView mScroll;
    private TextView mLog;
    private ProgressBar mProgressBar;
    private boolean mTaskRunning;
    private AsyncFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        FragmentManager manager=getFragmentManager();
        mFragment= (AsyncFragment) manager.findFragmentByTag(FRAGMENT_TAG);

        if(mFragment == null){
            mFragment=new AsyncFragment();
            manager.beginTransaction().add(mFragment,FRAGMENT_TAG).commit();
        }

    }

    public void runCode(View v) {

//        log("Running code");
//        displayProgressBar(true);

        mFragment.runTask("Red", "Green", "Blue", "Yellow");

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

    @Override
    public void handleTask(String message) {
        log(message);
    }
}