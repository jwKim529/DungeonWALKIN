package com.example.dungeonwalkin;

import android.util.Log;

public class ActiveObject {
    private final String TAG = "ActiveObject";

    // 해당 클래스가 가지는 정보
    // 이름, 레벨, 경험치, 공격력, 방어력, 체력

    private String objectName;
    private int currentLevel;

    //레벨당 경험치 요구량 증가 비율
    //lev(n) = lev(n-1) * 1.1
    private double experienceRatePerLevel = 1.1;
    private int requireExperience;
    private int myExperience = 0;

    private final int defaultAttack = 1;
    private final int defaultDefence = 1;
    private final int defaultLife = 1;

    private int currentAttack;
    private int currentDefence;
    private int currentLife;
    private int currentGold;
    private int clearedHighestDungeon = 0;

    //레벨전환비

    //공격 : 0.5
    //방어 : 0.2
    //체력 : 2.0
    private double correctAttack = 0.5;
    private double correctDefence = 0.2;
    private double correctLife = 2.0;

    public ActiveObject(String name) {
        this.objectName = name;
        initObject();
    }

    public ActiveObject(String name, int cleared) {
        this.objectName = name;
        initObject();
        this.clearedHighestDungeon = cleared;
    }

    public void initObject(){
        this.currentLevel = 0;
        this.requireExperience = 100;
        this.currentGold = 0;
        setMyStatus();
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public int getClearedHighestDungeon() {
        return clearedHighestDungeon;
    }

    public void setClearedHighestDungeon(int clearedHighestDungeon) {
        this.clearedHighestDungeon = clearedHighestDungeon;
    }

    private void levelUP (){
        while(this.myExperience >= requireExperience){
            Log.i(TAG,"ActiveObject:"+objectName+", LevelUP");
            this.myExperience-= requireExperience;
            requireExperience = (int)(requireExperience*experienceRatePerLevel);
            currentLevel++;
        }
        setMyStatus();
    }

    public int getMyExperience() {
        return myExperience;
    }

    public void setMyExperience(int myExp) {
        this.myExperience += myExp;
        levelUP();
    }

    public int getRequireExperience() {
        return requireExperience;
    }

    public void setRequireExperience(int requireExperience) {
        this.requireExperience = requireExperience;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int desiredLevel) {
        for(int i=this.currentLevel; i< desiredLevel; i++){
            currentLevel++;
            requireExperience = (int)(requireExperience*experienceRatePerLevel);
        }
        this.myExperience = 0;
        setMyStatus();
    }

    private void setMyStatus(){
        this.currentAttack = defaultAttack + (int)(currentLevel*correctAttack);
        this.currentDefence = defaultDefence + (int)(currentLevel*correctDefence);
        this.currentLife = defaultLife + (int)(currentLevel*correctLife);
    }

    public int getCurrentAttack() {
        return currentAttack;
    }

    public int getCurrentDefence() {
        return currentDefence;
    }

    public int getCurrentLife() {
        return currentLife;
    }

    public void setCurrentAttack(int currentAttack) {
        this.currentAttack = currentAttack;
    }

    public void setCurrentDefence(int currentDefence) {
        this.currentDefence = currentDefence;
    }

    public void setCurrentLife(int currentLife) {
        this.currentLife = currentLife;
    }

    public int getCurrentGold() {
        return currentGold;
    }

    public void addCurrentGold(int Gold) {
        this.currentGold += currentGold;
    }
    public void removeCurrentGold(int Gold) {
        this.currentGold -= currentGold;
    }
}
