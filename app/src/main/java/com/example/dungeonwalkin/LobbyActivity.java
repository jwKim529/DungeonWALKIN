package com.example.dungeonwalkin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class LobbyActivity extends AppCompatActivity {

    private final String TAG = "Lobby";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        //경험치로 변환 및 레벨업 처리
        DWApp dwApp = (DWApp)getApplication();
        ActiveObject playerData = dwApp.readPlayerStatusInDB();
        Log.i(TAG, "걸음 수 : "+dwApp.getCurrentStep().getSteps());
        playerData.setMyExperience(dwApp.getCurrentStep().getSteps());

        dwApp.setPlayerData(playerData);

        dwApp.writePlayerStatusInDB(playerData);

        TextView textViewLevel = findViewById(R.id.textViewLevel);
        textViewLevel.setText("Lv "+playerData.getCurrentLevel());

        TextView textViewExp = findViewById(R.id.textViewExp);
        textViewExp.setText(playerData.getMyExperience()+"/"+ playerData.getRequireExperience());

        TextView textViewGold = findViewById(R.id.textViewGold);
        textViewGold.setText(""+playerData.getCurrentGold());

        //되돌아가기
        Button returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LobbyActivity.this, PedometerActivity.class);
                startActivity(intent);
            }
        });

        //내정보진입
        Button enterMyPageButton = findViewById(R.id.enterMyPageButton);
        enterMyPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LobbyActivity.this, StatusActivity.class);
                startActivity(intent);
            }
        });

        //던전진입
        Button enterDungeonButton = findViewById(R.id.enterDungeonButton);
        enterDungeonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LobbyActivity.this, DungeonMapActivity.class);
                startActivity(intent);
            }
        });

        //상점진입
        Button enterShopButton = findViewById(R.id.enterShopButton);
    }

}