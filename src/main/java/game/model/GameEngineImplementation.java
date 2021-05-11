package game.model;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameEngineImplementation implements GameEngine {

    /**
     * The height of the game engine
     */
    private double height;

    /**
     * The current level
     */
    private Level currentLevel;

    /**
     * Map of all the levels
     */
    private Map<Integer, Level> levels;

    /**
     * Used to create distinct level id's for each level
     */
    private int levelId;

    /**
     * Level id of the current level
     */
    private int currentLevelId;

    /**
     * Json path to the level configuration file
     */
    private String jsonPath;

    /**
     * Used to keep track of how long it takes the user to complete the game
     */
    private Instant start;

    /**
     * Used to keep track of how long it takes the user to complete the game
     */
    private Duration interval;

    /**
     * The number of lives the hero has
     */
    private int lives;

    private int currentLevelNum;

    private double totalScore = 0;

    private ArrayList<String> jsonPathList;

    /**
     * Creates the game engine using the specified json configuration file and height
     * @param jsonPathList The path to the json configuration file containing the level information
     * @param height The height of the game engine's window
     */
    public GameEngineImplementation(ArrayList<String> jsonPathList, double height) {
        this.jsonPathList = jsonPathList;
        this.jsonPath = jsonPathList.get(0);
        this.height = height;
        this.levels = new HashMap<>();
        this.levelId = 1;
        this.currentLevelId = 1;
        this.lives = 3;
        this.currentLevelNum=1;
        createLevels();
        startLevel();
    }


    /**
     * Creates the levels associated with the json file
     */
    public void createLevels() {

        LevelBuilder levelBuilder = new LevelBuilder(this.jsonPath);
        LevelDirector levelDirector = new LevelDirector(levelBuilder);
        levelDirector.buildLevel();
        this.levels.put(this.levelId, levelDirector.getLevel());
        levelId+= 1;

    }

    public void nextLevel(){

        LevelBuilder levelBuilder = new LevelBuilder(jsonPathList.get(currentLevelNum));
        LevelDirector levelDirector = new LevelDirector(levelBuilder);
        levelDirector.buildLevel();
        this.levels.put(this.currentLevelId, levelDirector.getLevel());
        this.currentLevel.setOverallScore(totalScore);
        this.currentLevel.setIsFinished(false);
        currentLevelNum=currentLevelNum+1;
        startLevel();
        if(jsonPathList.size() == currentLevelNum) {
            this.currentLevel.setIsFinished(true);
        }
    }

    @Override
    public double getOverallScore(){
        return currentLevel.getOverallScore();
    }

    @Override
    public double getTotalScore(){
        return this.totalScore;
    }

    @Override
    public double getTargetTime(){
        return currentLevel.getTargetTime();
    }

    @Override
    public double getScore(){
        return currentLevel.getScore();
    }

    @Override
    public void startLevel() {
        this.currentLevel = levels.get(currentLevelId);
        start = Instant.now();
    }

    @Override
    public Level getCurrentLevel() {
        return this.currentLevel;
    }

    @Override
    public boolean jump() {
        return this.currentLevel.jump();
    }

    @Override
    public boolean moveLeft() {
        return this.currentLevel.moveLeft();
    }

    @Override
    public boolean moveRight() {
        return this.currentLevel.moveRight();
    }

    @Override
    public boolean stopMoving() {
        return this.currentLevel.stopMoving();
    }

    @Override
    public void tick() {
        this.currentLevel.tick();
        interval = Duration.between(start, Instant.now());
        if(currentLevel.isFinished()) {
            totalScore = totalScore + currentLevel.getOverallScore();
            nextLevel();
        }

    }

    @Override
    public void resetCurrentLevel() {
        this.lives--;
        if (this.lives == 0) {
            return;
        }
        LevelBuilder levelBuilder = new LevelBuilder(this.jsonPath);
        LevelDirector levelDirector = new LevelDirector(levelBuilder);
        levelDirector.buildLevel();
        this.levels.put(this.currentLevelId, levelDirector.getLevel());
        startLevel();
    }

    @Override
    public boolean isFinished() {
        return currentLevel.isFinished();
    }

    @Override
    public Duration getDuration() {
        return interval;
    }

    @Override
    public boolean gameOver() {
        return this.lives == 0;
    }

    @Override
    public int getLives() {
        return this.lives;
    }
}

