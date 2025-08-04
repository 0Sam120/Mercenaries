// ScoreManager.java
package come.onto.merc.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class ScoreManager {

    private int score;
    private int highScore;
    private int comboCount;
    private float comboTimer;
    private final float comboDuration = 3.0f; // 3 seconds to chain combos
    private String currentMapName;

    private Preferences preferences;

    public ScoreManager(String mapName) {
        this.currentMapName = mapName;
        preferences = Gdx.app.getPreferences("HighScores");
        highScore = preferences.getInteger(mapName, 0); // Load high score for this map
        reset();
    }

    // Reset scores and combo state
    public void reset() {
        score = 0;
        comboCount = 0;
        comboTimer = 0;
    }

    // Add points for a kill
    public void addPoints(int points) {
        comboCount++;
        comboTimer = comboDuration; // Reset combo timer
        int totalPoints = points * comboCount; // Apply combo multiplier
        score += totalPoints;
    }

    // Update combo timer
    public void update(float deltaTime) {
        if (comboTimer > 0) {
            comboTimer -= deltaTime;
        } else {
            comboCount = 0; // Reset combo if time runs out
        }
    }

    // Save high score if applicable
    public void checkAndSaveHighScore() {
        if (score > highScore) {
            highScore = score;
            preferences.putInteger(currentMapName, highScore);
            preferences.flush(); // Save to persistent storage
        }
    }

    // Getters
    public int getScore() {
        return score;
    }

    public int getHighScore() {
        return highScore;
    }

    public int getComboCount() {
        return comboCount;
    }

    public float getComboTimer() {
        return comboTimer;
    }
}
