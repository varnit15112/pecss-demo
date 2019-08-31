package edu.gatech.pecssinbuiltsensors;

import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class recordingPlayer extends AppCompatActivity implements SensorEventListener {

    private commonData commonDataObject;
    MediaPlayer mediaPlayer;

    public static List<appUsageInformation> appInfoList;
    public static List<Double> accList = new ArrayList<>();
    public static List<Double> accListNoteTimes = new ArrayList<>();
    Long phoneUsageToday = 0L;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    float x1=-100;
    float y1=-100;
    float z1=-100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_player);

        Log.i("testing","RecordingClass");
        Log.i("testing",String.valueOf(commonData.getTime()));

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.rec_08_29_2019_1);
        mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                Log.i("testing","DONE PLAYING MUSIC");
                getUsage();
//                printUsage();

                Intent activityChangeIntent;
                activityChangeIntent = new Intent(recordingPlayer.this, showResults.class);
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                recordingPlayer.this.startActivity(activityChangeIntent);
                finish();

            }
        });


        senSensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);


    }

    @Override
    protected void onStart() {
        super.onStart();
//        mediaPlayer.start();
        Log.i("testing","AppStartedAgain");
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        mediaPlayer.stop();
//        mediaPlayer.release();
//    }


    private void printUsage(){

        for (int i = 0; i < appInfoList.size(); i++) {
            System.out.println(appInfoList.get(i));
            appUsageInformation aui = appInfoList.get(i);
            System.out.println(aui.timeInForeground);
            System.out.println(aui.packageName);

        }


    }


    private void getUsage(){


        UsageEvents.Event currentEvent;
        List<UsageEvents.Event> allEvents = new ArrayList<>();
        HashMap<String, appUsageInformation> map = new HashMap <String, appUsageInformation> ();

        long currTime = System.currentTimeMillis();
        long startTime = commonData.getTime(); //querying past three hours
        Log.i("stats",String.valueOf(currTime));
        Log.i("stats",String.valueOf(commonData.getTime()));


        UsageStatsManager mUsageStatsManager =  (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);

        assert mUsageStatsManager != null;
        UsageEvents usageEvents = mUsageStatsManager.queryEvents(startTime, currTime);

//capturing all events in a array to compare with next element

        while (usageEvents.hasNextEvent()) {
            currentEvent = new UsageEvents.Event();
            usageEvents.getNextEvent(currentEvent);
            if (currentEvent.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND ||
                    currentEvent.getEventType() == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                allEvents.add(currentEvent);
                String key = currentEvent.getPackageName();
// taking it into a collection to access by package name
                if (map.get(key)==null)
                    map.put(key,new appUsageInformation(key));

            }
        }

//iterating through the arraylist
        for (int i=0;i<allEvents.size()-1;i++){
            UsageEvents.Event E0=allEvents.get(i);
            UsageEvents.Event E1=allEvents.get(i+1);

//for launchCount of apps in time range
            if (!E0.getPackageName().equals(E1.getPackageName()) && E1.getEventType()==1){
// if true, E1 (launch event of an app) app launched
                map.get(E1.getPackageName()).launchCount++;
//                Log.i("stats",E1.getPackageName());
//                Log.i("stats",String.valueOf(map.get(E1.getPackageName()).launchCount));
            }

//for UsageTime of apps in time range
            if (E0.getEventType()==1 && E1.getEventType()==2
                    && E0.getClassName().equals(E1.getClassName())){
                long diff = E1.getTimeStamp()-E0.getTimeStamp();
                phoneUsageToday+=diff; //gloabl Long var for total usagetime in the timerange
                map.get(E0.getPackageName()).timeInForeground+= diff;
                Log.i("stats",E1.getPackageName());
                Log.i("stats",String.valueOf(map.get(E0.getPackageName()).timeInForeground));
            }
        }
//transferred final data into modal class object
        appInfoList = new ArrayList<>(map.values());


//        Log.i("stats","hoha");
        Log.i("stats",appInfoList.toString());

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            if (x1==-100){
                x1=x;
            }
            if (y1==-100){
                y1=y;
            }
            if (z1==-100){
                z1=z;
            }

//            if ( Math.pow((x1-x),2) + Math.pow((y1-y),2) + Math.pow((z1-z),2) < 50 ){
//                text.setText("Don’t you get bored of me");
//            }else{
//                text.setText("See, I told you");
//            }
//
//            Log.i("stats",String.valueOf(Math.pow((x1-x),2)));
//            Log.i("stats",String.valueOf(Math.pow((y1-y),2)));
//            Log.i("stats",String.valueOf(Math.pow((z1-z),2)));


            System.out.print("Dislacement: ");
            Double disp = Math.pow(Math.pow((x1-x),2) + Math.pow((y1-y),2) + Math.pow((z1-z),2),0.5);
            System.out.println(disp);
            accList.add(disp);

            if (disp>1.0){
                accListNoteTimes.add((System.currentTimeMillis()-commonData.getTime())/1000.0);
//                System.out.println((System.currentTimeMillis()-commonData.getTime())/1000.0);
            }




            //System.out.println(Float.toString(x)+","+Float.toString(y)+","+Float.toString(z));

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


}
