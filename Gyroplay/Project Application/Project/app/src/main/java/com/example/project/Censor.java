package com.example.project;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Censor extends AppCompatActivity implements
        SensorEventListener {


    Sensor accelerometer;
    ImageView back;
    TextView leftC;


    Socket s;
    DataOutputStream dos;
    PrintWriter pw;
    private static String ip = "192.168.0.138";


    Socket s1;
    PrintWriter pw1;

    Socket s2;
    PrintWriter pw2;

    Socket s3;
    PrintWriter pw3;

    int count = 0;

    String batterylife;




    float messagex;
    float messagey;
    private String messageX;
    private String messageY;
    private String Testing = "work";
    private String bool = "0";
    private String right = "0";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_censor);

        //for the gyroscope
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener((SensorEventListener) this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);


        this.registerReceiver(this.mBathinfoReciever, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));




        back = (ImageView) findViewById(R.id.Return);// turns into a button
        back.setOnTouchListener(new View.OnTouchListener() {//listens to the button
            @Override
            public boolean onTouch(View v, MotionEvent event) {//when button is touched this executes
                close();

                return false;
            }
        });


        leftC = (TextView) findViewById(R.id.LeftB);
        leftC.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("Tag", "Ontouch: " + event);


                Click();
                if(count % 2 == 0){
                    startVoiceRecognition();
                }



                return false;
            }
        });



    }

    private BroadcastReceiver mBathinfoReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int battery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            System.out.println(String.valueOf(battery));
            batterylife = String.valueOf(battery);


        }
    };

    public void Click(){

            bool = "1";
            count++;



    }

    public void close(){//returns user to the main page
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


//this part only works in virtual phone

    public void send(String s,String s1, String s2x){

        MyTask my = new MyTask();
        my.execute(messageX,messageY,bool);



    }

    class MyTask extends AsyncTask<String,String,Void>{

        @Override
        protected Void doInBackground(String... voids) {



            try {
                s = new Socket(ip, 4006);
                pw = new PrintWriter(s.getOutputStream());
                pw.write(messageX);

                s1 = new Socket(ip, 4006);
                pw1 = new PrintWriter(s1.getOutputStream());
                pw1.write(messageY);
                pw.flush();
                pw.close();
                s.close();

                pw1.flush();
                pw1.close();
                s1.close();

                s2 = new Socket(ip, 4006);
                pw2 = new PrintWriter(s2.getOutputStream());
                Log.d("tag","before" + bool);
                pw2.write(bool);
                bool = "0";
                Log.d("tag","after" + bool);
                pw2.flush();
                pw2.close();
                s2.close();


//                s3 = new Socket(ip,1008);
//                pw3 = new PrintWriter(s2.getOutputStream());
//                Log.d("Right","before" + right);
//                pw3.write(right);
//                right = "0";
//                Log.d("Right", "after" + right);
//                pw3.flush();
//                pw3.close();
//                s3.close();








//                s = new Socket(ip, 2002);
//                pw = new PrintWriter(s.getOutputStream());
//                pw.write(messageY);
//                pw.flush();
//                pw.close();
//                s.close();





            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

//practicing to work on IRL phone, only display null

    public void send1(String s){

        MyTask1 my = new MyTask1();
        my.execute(right);


    }

    class MyTask1 extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... voids) {

            try {
                s = new Socket(ip,4006);
                pw = new PrintWriter(s.getOutputStream());
                pw.write(right);
                right = "0";
                pw.flush();
                pw.close();
                s.close();

                s1 = new Socket(ip,4006);
                pw1 = new PrintWriter(s1.getOutputStream());
                pw1.write(batterylife);

                pw1.flush();
                pw1.close();
                s1.close();



            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("TAG","onSensorChange X: " +  event.values[0] + " Y: " + event.values[1] );





        messagex = event.values[0];
        messageX = Float.toString(messagex);
        messagey = event.values[1];
        messageY = Float.toString(messagey);

        send(messageX,messageY,bool);
        send1(right);
//        bool = "0";
//        send1(messageY);


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");

        startActivityForResult(intent, 1234);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {//takes the matching

            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            String topResult = matches.get(0);


            System.out.println(topResult);

            if(topResult.indexOf("right") !=1){
                Right();// changes state

            }


        }};

    public void Right(){
        right = "1";
    }





}




