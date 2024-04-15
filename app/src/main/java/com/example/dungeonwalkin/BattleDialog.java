package com.example.dungeonwalkin;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class BattleDialog extends DialogFragment {
    private final String TAG = "BattleDialog";

    private DWApp MyApp;

    private final double DEF_MODIFY = 2.0;

    private ActiveObject monsterData;
    private TextView textViewEnemyLife;
    private TextView textViewEnemyDamaged;
    private ProgressBar progressBarEnemyLife;
    private int currentEnemyLife;

    private ActiveObject playerData;
    private TextView textViewPlayerLife;
    private TextView textViewPlayerDamaged;
    private ProgressBar progressBarPlayerLife;
    private int currentPlayerLife;
    private int deadMan;
    private DialogInterface.OnDismissListener onDismissListener = null;
    public BattleDialog(DWApp myApp) {
        this.MyApp = myApp;
        monsterData = MyApp.getCurrentDungeon().getDungeonLocation(MyApp.getCurrentPlayerLocation()).getMonsterStatus();
        playerData = MyApp.getPlayerData();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.battle_dialog, null);

        //Enemy
        TextView textViewEnemyName = view.findViewById(R.id.textViewEnemyName);
        textViewEnemyName.setText(monsterData.getObjectName()
                +"\nLv "+monsterData.getCurrentLevel());

        textViewEnemyLife = view.findViewById(R.id.textViewEnemyLife);
        currentEnemyLife = monsterData.getCurrentLife();
        textViewEnemyLife.setText(currentEnemyLife + "/" + currentEnemyLife);

        textViewEnemyDamaged = view.findViewById(R.id.textViewEnemyDamaged);
        textViewEnemyDamaged.setText("");
        progressBarEnemyLife = view.findViewById(R.id.progressBarEnemyLife);
        progressBarEnemyLife.setProgress(100);

        //Player
        TextView textViewPlayerName = view.findViewById(R.id.textViewPlayerName);
        textViewPlayerName.setText(playerData.getObjectName()
                +"\nLv "+playerData.getCurrentLevel());

        textViewPlayerLife = view.findViewById(R.id.textViewPlayerLife);
        currentPlayerLife = playerData.getCurrentLife();
        textViewPlayerLife.setText(currentPlayerLife + "/" + currentPlayerLife);

        textViewPlayerDamaged = view.findViewById(R.id.textViewPlayerDamaged);
        textViewPlayerDamaged.setText("");
        progressBarPlayerLife = view.findViewById(R.id.progressBarPlayerLife);
        progressBarPlayerLife.setProgress(100);
        Log.i(TAG,"Start Battle");
        //Button
        Button atkButton = view.findViewById(R.id.atkButton);
        atkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(battle("ATK")){
                    if(currentEnemyLife == 0)
                        deadMan = 1;
                    else
                        deadMan = -1;
                    dismiss();
                }
            }
        });
        Button defButton = view.findViewById(R.id.defButton);
        defButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(battle("DEF")){
                    deadMan = -1;
                    dismiss();
                }
            }
        });
        Button itemButton = view.findViewById(R.id.itemButton);//미구현
        Button runButton = view.findViewById(R.id.runButton);
        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deadMan = 0;
                dismiss();
            }
        });

        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(view);
        return dialog;
    }

    private boolean battle(String commandType){
        //전투 처리(서로 공격 또는 플레이어 방어시만)
        int receivedDamageEnemy = 0;
        int receivedDamagePlayer = 0;
        switch (commandType){
            case "ATK"://서로 공격
                //전투처리
                receivedDamageEnemy = playerData.getCurrentAttack()-monsterData.getCurrentDefence();
                currentEnemyLife -= receivedDamageEnemy;
                if(currentEnemyLife<0) currentEnemyLife = 0;

                receivedDamagePlayer = monsterData.getCurrentAttack()-playerData.getCurrentDefence();
                currentPlayerLife -= receivedDamagePlayer;
                if(currentPlayerLife<0) currentPlayerLife = 0;

                break;
            case "DEF"://플레이어 방어
                receivedDamagePlayer = monsterData.getCurrentAttack()
                        - (int)(playerData.getCurrentDefence()*DEF_MODIFY);
                currentPlayerLife -= receivedDamagePlayer;
                if(currentPlayerLife<0) currentPlayerLife = 0;
                break;
        }
        changeView(receivedDamageEnemy,receivedDamagePlayer);

        if(currentEnemyLife == 0 || currentPlayerLife == 0) return true;
        return false;
    }

    private void changeView(int receivedDamageEnemy, int receivedDamagePlayer){
        //화면설정 순서
        //1.체력텍스트 표시
        //2.데미지 텍스트 표시 -> 1초후 데미지텍스트 삭제(스레드)
        //3.프로그레스바 표시

        //체력텍스트 표시
        textViewEnemyLife.setText(
                currentEnemyLife
                + "/"
                + monsterData.getCurrentLife()
        );
        textViewPlayerLife.setText(
                currentPlayerLife
                + "/"
                + playerData.getCurrentLife()
        );
        //데미지텍스트 표시
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    textViewEnemyDamaged.setText("-"+receivedDamageEnemy);
                    textViewPlayerDamaged.setText("-"+receivedDamagePlayer);
                    Thread.sleep(1000);

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textViewEnemyDamaged.setText("");
                            textViewPlayerDamaged.setText("");
                        }
                    });
                } catch (InterruptedException e) {
                    Log.i(TAG, String.valueOf(e));
                }
            }
        }).start();
        //프로그래스바 표시
        progressBarEnemyLife.setProgress(
                (int)(currentEnemyLife/monsterData.getCurrentLife()*100)
        );
        progressBarPlayerLife.setProgress(
                (int)(currentPlayerLife/playerData.getCurrentLife()*100)
        );

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.i(TAG,"Send Data and Dismiss");
        MyApp.setCurrentDead(deadMan);
        MyApp.setCurrentCleared(true);
        final Activity activity = getActivity();
        if(activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener)activity).onDismiss(dialog);
        }
    }
}
