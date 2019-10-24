package com.example.project;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;


import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private TextView text;
    private TextToSpeech myTTS;
    private SpeechRecognizer mySpeeachRecog;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.registerReceiver(this.mBathinfoReciever, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));





        text=findViewById(R.id.Start);
        text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Open();
//                startVoiceRecognitionActivity();




                return false;
            }


        });




        initializeTextToSpeech();









    }

//    private void startVoiceRecognitionActivity() {
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//
//        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
//
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//
//        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
//
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
//
//        startActivityForResult(intent, 1234);
//
//
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == Activity.RESULT_OK) {
//            //pull all of the matches
//            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//
//            String topResult = matches.get(0);
//            System.out.println(topResult);
//
//        }};




    private BroadcastReceiver mBathinfoReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            System.out.println(String.valueOf(level));// for battery

        }
    };










    private void initializeTextToSpeech() {
        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(MainActivity.this, "Tag", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    myTTS.setLanguage(Locale.ENGLISH);
                    speak("Hello");

                }

            }


        });
    }

    private void speak(String s) {
        if (Build.VERSION.SDK_INT >= 21){
            myTTS.speak(s,TextToSpeech.QUEUE_FLUSH, null, null);


        }else {
            myTTS.speak(s,TextToSpeech.QUEUE_FLUSH,null);
        }
    }


    public void onPause(){
        super.onPause();
        myTTS.shutdown();
    }

    public void Open(){
        Intent intent = new Intent(this, Censor.class);
        startActivity(intent);




    }
}
