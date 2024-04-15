package com.example.dungeonwalkin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StatusActivity extends AppCompatActivity {

    private final String TAG = "StatusActivity";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        Log.i(TAG, "Activity Start");
        ActiveObject currentPlayer = ((DWApp)getApplication()).getPlayerData();
        Log.i(TAG, "get CurrentPlayerStatus");
        //스테이터스 레이아웃 만들기
        TextView textViewName = findViewById(R.id.textViewName);
        textViewName.setText(currentPlayer.getObjectName());

        TextView textViewLevel = findViewById(R.id.textViewLevel);
        textViewLevel.setText(""+currentPlayer.getCurrentLevel());

        TextView textViewExp = findViewById(R.id.textViewExp);
        textViewExp.setText(currentPlayer.getMyExperience()
                +"/"+currentPlayer.getRequireExperience());

        TextView textViewATK = findViewById(R.id.textViewATK);
        textViewATK.setText(""+currentPlayer.getCurrentAttack());

        TextView textViewDEF = findViewById(R.id.textViewDEF);
        textViewDEF.setText(""+currentPlayer.getCurrentDefence());

        TextView textViewLife = findViewById(R.id.textViewLife);
        textViewLife.setText(""+currentPlayer.getCurrentLife());

        Button returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}