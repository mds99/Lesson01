package ru.pp.mita.lesson01;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MyFirstService extends Service  {
    //public MyFirstService() {
    //}

    final String LOG_TAG = "mitaSvc";
//    String cmd = "";
    ExecutorService es;
    Object someRes;

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");
        es = Executors.newFixedThreadPool(3);
        someRes = new Object();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "MyService onStartCommand " + startId);
        String cmd = "";
        int time = intent.getIntExtra("time", 1);
        cmd = intent.getStringExtra("cmd");
        if ("stop".equals(cmd)) {
            Log.d(LOG_TAG, "MyService onStartCommand STOP CMD " + startId);
            stopTasks();
        } else {
            MyRun mr = new MyRun(time, startId);
            es.execute(mr);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public List<Runnable> stopTasks() {
        super.onDestroy();
        return es.shutdownNow();
    }
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "MyService onDestroy");
        someRes = null;
}
    void onClick(View v) {
    }
    void someTask() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d(LOG_TAG, "onBind");
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }



    class MyRun implements Runnable {

        int time;
        int startId;

        public MyRun(int time, int startId) {
            this.time = time;
            this.startId = startId;
            Log.d(LOG_TAG, "MyRun#" + startId + " create");
        }

        public void run() {
            Log.d(LOG_TAG, "MyRun#" + startId + " start, time = " + time);
            EventBus.getDefault().post(new MessageEvent("Starting " + startId ));
            try {
                TimeUnit.SECONDS.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Log.d(LOG_TAG, "MyRun#" + startId + " someRes = " + someRes.getClass());
            } catch (NullPointerException e) {
                Log.d(LOG_TAG, "MyRun#" + startId + " error, null pointer");
            }
            stop();
        }

        void stop() {
            Log.d(LOG_TAG, "MyRun#" + startId + " end, stopSelf(" + startId + ")");
            EventBus.getDefault().post(new MessageEvent("Ended " + startId ));
            stopSelf(startId);
        }


    }
}
