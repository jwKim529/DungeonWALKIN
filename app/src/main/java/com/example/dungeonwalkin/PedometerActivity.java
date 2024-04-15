package com.example.dungeonwalkin;

import static android.widget.Toast.LENGTH_SHORT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Date;

public class PedometerActivity extends AppCompatActivity implements SensorEventListener {

    private final String TAG = "PedometerActivity";

    private SensorManager sensorManager;
    private Sensor stepCountSensor;
    private TextView stepCountView;

    private CurrentStep currentStep = new CurrentStep(0,0,0);
    private final int DEFAULT_STEPS = 100;

    private int steps = DEFAULT_STEPS;


    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pedometer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        stepCountView = findViewById(R.id.stepCountView);
        stepCountView.setText(String.valueOf(steps));

        // 걸음 센서 세팅
        // * 옵션
        // - TYPE_STEP_DETECTOR:  리턴 값이 무조건 1, 앱이 종료되면 다시 0부터 시작
        // - TYPE_STEP_COUNTER : 앱 종료와 관계없이 계속 기존의 값을 가지고 있다가 1씩 증가한 값을 리턴
        //
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);


        // 디바이스에 걸음 센서의 존재 여부 체크
        if (stepCountSensor == null) {
            Toast.makeText(this, "No Step Sensor", LENGTH_SHORT).show();
        }

        TextView textViewTime = findViewById(R.id.textViewToday);
        currentStep.setStartTime(System.currentTimeMillis());
        Date date = new Date(currentStep.getStartTime());
        textViewTime.setText(""+currentStep.simpleDateFormat.format(date));

        Button convertButton = findViewById(R.id.convertButton);
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentStep.setEndTime(System.currentTimeMillis());
                Date endDate = new Date(currentStep.getEndTime());
                Toast.makeText(getApplicationContext(),
                        ""+currentStep.simpleDateFormat.format(endDate)+"\n걸음 수 : "+steps+"회",
                        Toast.LENGTH_SHORT).show();
                currentStep.setSteps(steps);
                ((DWApp)getApplication()).setCurrentStep(currentStep);
                ((DWApp)getApplication()).writeCurrentStepsInDB(currentStep);
                ((DWApp)getApplication()).savePreference(currentStep);
                Log.i(TAG, "CurrentStep : "+((DWApp)getApplication()).getCurrentStep().getSteps());
                currentStep = new CurrentStep(0,0,0);
                steps = DEFAULT_STEPS;
                Intent intent = new Intent(PedometerActivity.this, LobbyActivity.class);
                startActivity(intent);
            }
        });

        Button resultButton = findViewById(R.id.resultButton);
        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PedometerActivity.this, StepResultActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onStart() {
        super.onStart();
        if(stepCountSensor !=null) {
            // 센서 속도 설정
            // * 옵션
            // - SENSOR_DELAY_NORMAL: 20,000 초 딜레이
            // - SENSOR_DELAY_UI: 6,000 초 딜레이
            // - SENSOR_DELAY_GAME: 20,000 초 딜레이
            // - SENSOR_DELAY_FASTEST: 딜레이 없음
            //
            sensorManager.registerListener(this,stepCountSensor,SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 걸음 센서 이벤트 발생시
        if(event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR){

            if(event.values[0]==1.0f){
                // 센서 이벤트가 발생할때 마다 걸음수 증가
                steps++;
                stepCountView.setText(String.valueOf(steps));
            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}