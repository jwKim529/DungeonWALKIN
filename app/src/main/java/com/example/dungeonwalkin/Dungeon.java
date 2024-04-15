package com.example.dungeonwalkin;

import java.util.Random;

import static com.example.dungeonwalkin.Instance.BOSS_ROOM;
import static com.example.dungeonwalkin.Instance.EMPTY_ROOM;
import static com.example.dungeonwalkin.Instance.MONSTER_ROOM;
import static com.example.dungeonwalkin.Instance.REWARD_ROOM;

public class Dungeon {
    private final int MIN_DUNGEON_LENGTH = 5;
    private final int MAX_DUNGEON_LENGTH = 9;

    private final int MIN_DUNGEON_CLEAR_REWARD = 200;
    private final int MAX_DUNGEON_CLEAR_REWARD = 500;

    private final int MONSTER_EXIST_LIMIT = 3;
    private final int REWARD_EXIST_LIMIT = 1;

    private final int BOSS_LEVEL_MODIFY = 3;
    private final String[] MONSTER_NAMESET = {
            "AAA", "BBB", "CCC", "DDD", "EEE"
    };
    private final String[] BOSS_NAMESET = {
            "ZZZZZ","YYYYY","XXXXX","WWWWW"
    };

    private String dungeonName;
    private int dungeonLevel;
    private int dungeonLength;
    private Instance[] dungeonLocation;
    private int dungeonClearReward;

    public Dungeon (String name, int cleared) {

        this.dungeonName = name;
        this.dungeonLevel = cleared;

        Random random= new Random();
        this.dungeonLength = random.nextInt(
                MAX_DUNGEON_LENGTH - MIN_DUNGEON_LENGTH + 1)
                +MIN_DUNGEON_LENGTH
                + 1;
        this.dungeonClearReward = random.nextInt(
                MAX_DUNGEON_CLEAR_REWARD - MIN_DUNGEON_CLEAR_REWARD + 1)
                + MIN_DUNGEON_CLEAR_REWARD;
        this.dungeonLocation = new Instance[this.dungeonLength];
        for(int i=0;i<this.dungeonLength;i++){
            dungeonLocation[i] = new Instance(EMPTY_ROOM);
        }

        // 몬스터 생성
        // 몬스터 생성 시 주의 :
        // 몬스터 생성 시에는 인스턴스 타입 지정과 같이 몬스터 스텟 반드시 업데이트 필요
        int monsterCount = 0;
        while(monsterCount < MONSTER_EXIST_LIMIT){
            Instance monsterInstance = dungeonLocation[random.nextInt(dungeonLength-1)];
            if(monsterInstance.getInstanceType().equals(EMPTY_ROOM)){
                monsterInstance.setInstanceType(MONSTER_ROOM,
                        MONSTER_NAMESET[random.nextInt(MONSTER_NAMESET.length)]);
                monsterInstance.setMonsterStatus(dungeonLevel);
                monsterCount++;
            }
        }

        // 보상 생성
        int rewardCount = 0;
        while(rewardCount < REWARD_EXIST_LIMIT){
            int currentLocation = random.nextInt(dungeonLength-1);
            if(dungeonLocation[currentLocation].getInstanceType().equals(EMPTY_ROOM)){
                dungeonLocation[currentLocation].setInstanceType(REWARD_ROOM);
                rewardCount++;
            }
        }

        // 보스 생성
        Instance bossInstance = dungeonLocation[dungeonLength-1];
        bossInstance.setInstanceType(BOSS_ROOM,
                BOSS_NAMESET[random.nextInt(BOSS_NAMESET.length)]);
        bossInstance.setMonsterStatus(dungeonLevel+BOSS_LEVEL_MODIFY);

    }

    public String getDungeonName() {
        return dungeonName;
    }

    public void setDungeonName(String dungeonName) {
        this.dungeonName = dungeonName;
    }

    public int getDungeonLevel() {
        return dungeonLevel;
    }

    public void setDungeonLevel(int dungeonLevel) {
        this.dungeonLevel = dungeonLevel;
    }

    public int getDungeonLength() {
        return dungeonLength;
    }

    public Instance getDungeonLocation(int location) {
        return dungeonLocation[location];
    }

    public int getDungeonClearReward() {
        return dungeonClearReward;
    }

    public void setDungeonLength(int dungeonLength) {
        this.dungeonLength = dungeonLength;
    }

    public void setDungeonLocation(int location, Instance locationState) {
        this.dungeonLocation[location] = locationState;
    }

    public void setDungeonClearReward(int dungeonClearReward) {
        this.dungeonClearReward = dungeonClearReward;
    }
}
