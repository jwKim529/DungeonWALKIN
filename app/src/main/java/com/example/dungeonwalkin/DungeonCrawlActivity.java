package com.example.dungeonwalkin;

import static com.example.dungeonwalkin.Instance.BOSS_ROOM;
import static com.example.dungeonwalkin.Instance.EMPTY_ROOM;
import static com.example.dungeonwalkin.Instance.MONSTER_ROOM;
import static com.example.dungeonwalkin.Instance.REWARD_ROOM;

import static com.example.dungeonwalkin.DWApp.RESULT_CODE;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DungeonCrawlActivity extends AppCompatActivity implements DialogInterface.OnDismissListener{
    private final String TAG = "DungeonCrawlActivity";
    private final String EMPTY = "빈 방";
    private final String REWARD = "보물상자";

    private DWApp MyApp;

    private TextView textViewDungeonName;
    private TextView textViewInstanceName;
    private Button frontButton;
    private Button exitButton;
    private String dungeonName;
    private int currentLocation = 0;
    private boolean dead = false;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dungeon_crawl);
        Log.i(TAG,"Create Activity");
        MyApp = (DWApp)getApplication();
        MyApp.setCurrentPlayerLocation(currentLocation);
        MyApp.setCurrentDead(0);
        MyApp.setCurrentCleared(false);
        Dungeon currentDungeon = MyApp.getCurrentDungeon();
        dungeonName = currentDungeon.getDungeonName();
        textViewDungeonName = findViewById(R.id.textViewDungeonName);
        textViewDungeonName.setText(dungeonName+" : "+currentLocation);
        textViewInstanceName = findViewById(R.id.textViewInstanceSimpleView);

        progressDungeon(currentDungeon);

        frontButton = findViewById(R.id.frontButton);
        frontButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dead && currentLocation+1 < currentDungeon.getDungeonLength()) {
                    progressDungeon(currentDungeon);
                }
            }
        });

        exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApp.setCurrentPlayerLocation(0);
                finish();
            }
        });
    }
    private void progressDungeon(Dungeon currentDungeon){
        currentLocation++;
        MyApp.setCurrentPlayerLocation(currentLocation);
        Instance currentInstance = currentDungeon.getDungeonLocation(currentLocation);
        switch (currentInstance.getInstanceType()) {
            case EMPTY_ROOM:
                changeView(EMPTY, currentInstance.isMonsterExist());
                break;
            case REWARD_ROOM:
                changeView(REWARD, currentInstance.isMonsterExist());
                break;
            case MONSTER_ROOM:
                changeView("MONSTER\n" + currentInstance.getMonsterStatus().getObjectName(),
                        currentInstance.isMonsterExist());
                break;
            case BOSS_ROOM:
                changeView("BOSS\n" + currentInstance.getMonsterStatus().getObjectName(),
                        currentInstance.isMonsterExist());
                break;
        }
    }

    private void changeView (String instanceName, boolean isMonsterExist) {
        textViewDungeonName.setText(dungeonName+" : "+currentLocation);
        textViewInstanceName.setText(instanceName);
        if(isMonsterExist){
            BattleDialog dialogFragment = new BattleDialog((DWApp)getApplication());
            dialogFragment.show(getSupportFragmentManager(),"BattleDialog");
        }
    }
    private void changeView (String instanceData) {
        textViewInstanceName.setText(instanceData);
    }
    private void deadOrAlive(int deadMan){
        ActiveObject playerData = MyApp.getPlayerData();
        Instance currentLocation = MyApp.getCurrentDungeon().getDungeonLocation(MyApp.getCurrentPlayerLocation());
        switch (deadMan){
            case -1://플레이어 사망
                dead = true;
                changeView("당신은\n죽었습니다");
                break;
            case 0://도망침
                dead = true;
                changeView("당신은\n도망쳤습니다.\n\n탐색을 종료하세요.");
                break;
            case 1://몬스터 처치 -> 보상처리
                int getGold = currentLocation.getClear_reward();
                playerData.addCurrentGold(getGold);
                changeView(currentLocation.getMonsterStatus().getObjectName()
                        +" (을)를\n처치하였습니다!\n보상 : "+getGold+" G");
                if(currentLocation.getInstanceType().equals(BOSS_ROOM)){
                    playerData.setClearedHighestDungeon(MyApp.getCurrentDungeon().getDungeonLevel());
                    frontButton.setText("탐색완료\n보상받기");
                    exitButton.setVisibility(View.INVISIBLE);
                    frontButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MyApp.getPlayerData().addCurrentGold(
                                    MyApp.getCurrentDungeon().getDungeonClearReward());

                            Intent intent = new Intent();
                            setResult(RESULT_CODE, intent);
                            finish();
                        }
                    });
                }
                break;
        }
        MyApp.setPlayerData(playerData);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        int deadMan = MyApp.getCurrentDead();
        Log.i(TAG,"deadMan : "+deadMan);
        deadOrAlive(deadMan);
    }
}