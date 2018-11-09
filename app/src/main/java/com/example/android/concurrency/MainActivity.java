package com.example.android.concurrency;

import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private static final String TAG = "MyTag";
    private static final String MESSAGE_KEY = "message_key";
    private static final String DATA_KEY = "data_key";
    private ScrollView mScroll;
    private TextView mLog;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

    }

    public void runCode(View v) {

        Bundle bundle=new Bundle();
        bundle.putString(DATA_KEY,"some url that returns some data");

        getSupportLoaderManager().restartLoader(100,bundle,this).forceLoad();

    }

    private void initViews() {
        mScroll = (ScrollView) findViewById(R.id.scrollLog);
        mLog = (TextView) findViewById(R.id.tvLog);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    private void log(String message) {

        if(mLog.getText().equals("Ready to Code!"))
            mLog.setText("");

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

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {

        List<String> songsList= Arrays.asList(Playlist.songs);

        return new MyTaskLoader(this,args,songsList);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        log(data);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    public static class MyTaskLoader extends AsyncTaskLoader<String>{

        private Bundle mArgs;
        private List<String> mSongsList;

        public MyTaskLoader(@NonNull Context context, Bundle args, List<String> songsList) {
            super(context);
            this.mArgs=args;
            mSongsList=songsList;
        }

        @Nullable
        @Override
        public String loadInBackground() {

            String data=mArgs.getString(DATA_KEY);

            Log.d(TAG, "loadInBackground: URL: "+data);

            Log.d(TAG, "loadInBackground: Thread Name: "+Thread.currentThread().getName());

            for (String song:mSongsList){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "loadInBackground: Song Name: "+song);

            }


            Log.d(TAG, "loadInBackground: Thread Terminated: ");


            return "result from loader";
        }

        @Override
        public void deliverResult(@Nullable String data) {
            data+=" :modified";

            super.deliverResult(data);
        }
    }

}