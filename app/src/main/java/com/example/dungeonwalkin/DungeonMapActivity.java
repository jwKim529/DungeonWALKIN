package com.example.dungeonwalkin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

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
                startActivity(intent);
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
}