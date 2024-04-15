package com.example.dungeonwalkin;

import static com.example.dungeonwalkin.DWApp.REQUEST_CODE;
import static com.example.dungeonwalkin.DWApp.RESULT_CODE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DungeonMapActivity extends AppCompatActivity {



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dungeon_map);

        DWApp MyApp = (DWApp)getApplication();
        MyApp.setCurrentDungeon("MyTestDungeon");
        Dungeon currentDungeon = MyApp.getCurrentDungeon();

        TextView dungeonName = findViewById(R.id.textViewDungeonName);
        dungeonName.setText(currentDungeon.getDungeonName());

        TextView dungeonLevel = findViewById(R.id.textViewDungeonLV);
        dungeonLevel.setText(""+currentDungeon.getDungeonLevel());

        Button enterButton = findViewById(R.id.enterButton);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DungeonMapActivity.this, DungeonCrawlActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        Button returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_CODE){
            if(((DWApp)getApplication()).isCurrentCleared()) {
                Intent intent = new Intent();
                setResult(RESULT_CODE, intent);
                finish();
            }
        }
    }
}