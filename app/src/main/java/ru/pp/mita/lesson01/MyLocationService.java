package ru.pp.mita.lesson01;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MyLocationService extends Service  {
    //public MyFirstService() {
    //}

/*

// Acquire a reference to the system Location Manager
    LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

    // Define a listener that responds to location updates
    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
            //makeUseOfNewLocation(location);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
    };

// Register the listener with the Location Manager to receive location updates
    locationManager.requestLocationUpdates(LocationManager.FUSED_PROVIDER, 0, 0, locationListener);

*/
    final String LOG_TAG = "mitaLocSvc";


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
        Log.d(LOG_TAG, "MyLocService onStartCommand " + startId);
        String cmd = "";
        int time = intent.getIntExtra("time", 1);
        cmd = intent.getStringExtra("cmd");
        if ("stop".equals(cmd)) {
            Log.d(LOG_TAG, "MyLocService onStartCommand STOP CMD " + startId);
            stopTasks();
        } else {
            LocationRun mr = new LocationRun(time, startId);
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
        Log.d(LOG_TAG, "MyLocService onDestroy");
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



    class LocationRun implements Runnable {

        int time;
        int startId;

        public LocationRun(int time, int startId) {
            this.time = time;
            this.startId = startId;
            Log.d(LOG_TAG, "LR#" + startId + " create");
        }

        public void run() {
            Log.d(LOG_TAG, "LR#" + startId + " start, time = " + time);
            EventBus.getDefault().post(new MessageEvent("Starting " + startId ));
            try {
                TimeUnit.SECONDS.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Log.d(LOG_TAG, "LR#" + startId + " someRes = " + someRes.getClass());
            } catch (NullPointerException e) {
                Log.d(LOG_TAG, "LR#" + startId + " error, null pointer");
            }
            stop();
        }

        void stop() {
            Log.d(LOG_TAG, "LR#" + startId + " end, stopSelf(" + startId + ")");
            EventBus.getDefault().post(new MessageEvent("Ended " + startId ));
            stopSelf(startId);
        }


    }
}
