package com.example.dungeonwalkin;

import java.util.Random;

public class Instance {

    private final int MIN_REWARD = 10;
    private final int MAX_REWARD = 50;
    private final double REWARDROOM_MULTI = 3.0;
    private final double BOSSREWARD_MULTI = 10.0;

    //Instance type
    public static final String EMPTY_ROOM = "EMPTY";
    public static final String MONSTER_ROOM = "MONSTER";
    public static final String REWARD_ROOM = "REWARD";
    public static final String BOSS_ROOM = "BOSS";

    private String instanceType;
    private boolean monsterExist;
    private int clear_reward;
    private ActiveObject monsterStatus;

    public Instance (String instanceType){
        setInstanceType(instanceType);
    }
    public Instance (String instanceType, String monsterName){
        setInstanceType(instanceType, monsterName);
    }

    public ActiveObject getMonsterStatus() {
        return monsterStatus;
    }

    public void setMonsterStatus(int dungeonLevel) {
        this.monsterStatus.setCurrentLevel(dungeonLevel);
    }

    public String getInstanceType() {
        return instanceType;
    }

    public boolean isMonsterExist() {
        return monsterExist;
    }

    public int getClear_reward() {
        return clear_reward;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
        setMonsterExist();
        setClear_reward();
    }

    public void setInstanceType(String instanceType, String monsterName) {
        this.instanceType = instanceType;
        setMonsterExist();
        setClear_reward();
        this.monsterStatus = new ActiveObject(monsterName);
    }

    public void setMonsterExist() {
        this.monsterExist = this.instanceType.equals(MONSTER_ROOM) || this.instanceType.equals(BOSS_ROOM);
    }

    public void setClear_reward() {
        Random random = new Random();
        switch(this.instanceType) {
            case BOSS_ROOM:
                this.clear_reward = (int) (
                    (random.nextInt(MAX_REWARD - MIN_REWARD + 1) + MIN_REWARD)
                            * BOSSREWARD_MULTI);
                break;
            case REWARD_ROOM:
                this.clear_reward = (int) (
                    (random.nextInt(MAX_REWARD - MIN_REWARD + 1) + MIN_REWARD)
                            * REWARDROOM_MULTI);
                break;
            case MONSTER_ROOM:
                this.clear_reward = random.nextInt(
                        MAX_REWARD - MIN_REWARD + 1)
                        +MIN_REWARD;
                break;
            default:
                this.clear_reward = 0;
        }
    }
}
