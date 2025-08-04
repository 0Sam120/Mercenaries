// GameScreen.java
package come.onto.merc.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

import come.onto.merc.MyGame;
import come.onto.merc.controls.Button;
import come.onto.merc.controls.Joystick;
import come.onto.merc.controls.WeaponSwitchButton;
import come.onto.merc.entities.Bullet;
import come.onto.merc.entities.Enemy;
import come.onto.merc.entities.Pickup;
import come.onto.merc.entities.Player;
import come.onto.merc.managers.ScoreManager;
import come.onto.merc.ui.AmmoDisplay;
import come.onto.merc.ui.UIBar;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private BitmapFont font;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Pickup> pickups;
    private Joystick movementJoystick;
    private Joystick rotationJoystick;
    private Button shootButton;
    private Button reloadButton;
    private Button rampageButton;
    private WeaponSwitchButton primaryWeaponButton;
    private WeaponSwitchButton secondaryWeaponButton;
    private ShapeRenderer shapeRenderer;
    private AmmoDisplay ammoDisplay;
    private ScoreManager scoreManager;
    private UIBar healthBar;
    private UIBar rampageBar;
    private float enemySpawnTimer;
    private float enemySpawnDelay;
    private float gameOverTimer = 0f;
    private float gameTimer = 300f;// Timer for delaying game exit
    private boolean gameOver = false;
    private boolean gameRunning = true;
    private static final int MAX_ENEMIES = 10;
    private static final float GAME_OVER_DELAY = 3.0f;

    public GameScreen(MyGame game) {
        batch = new SpriteBatch();
        font = new BitmapFont();
        shapeRenderer = new ShapeRenderer();
        scoreManager = new ScoreManager("Map1");

        player = new Player(
            Gdx.graphics.getWidth() / 2,
            Gdx.graphics.getHeight() / 2
        );

        enemies = new ArrayList<>();
        pickups = new ArrayList<>();
        enemySpawnDelay = 3.0f;
        enemySpawnTimer = enemySpawnDelay;

        float joystickRadius = 100f;
        float buttonRadius = 50f;
        float centerX = Gdx.graphics.getWidth() / 2;

        // Position the health bar
        healthBar = new UIBar(
            centerX - 150,  // Centered horizontally
            80,            // 80 pixels from the bottom of the screen
            300,           // Width
            20,            // Height
            0,             // Continuous (no segments)
            0,             // No gap
            Color.DARK_GRAY, // Background
            Color.RED        // Foreground
        );

// Position the future feature bar above the health bar
        rampageBar = new UIBar(
            centerX - 150,  // Centered horizontally
            105,           // Slightly above the health bar
            300,           // Width
            10,            // Height
            10,            // 10 segments
            2,             // Segment gap
            Color.DARK_GRAY, // Background
            Color.YELLOW     // Foreground
        );

        // Create joysticks
        movementJoystick = new Joystick(
            joystickRadius + 50,
            joystickRadius + 50,
            joystickRadius
        );

        rotationJoystick = new Joystick(
            Gdx.graphics.getWidth() - joystickRadius - 50,
            joystickRadius + 50,
            joystickRadius
        );

        shootButton = new Button(
            Gdx.graphics.getWidth() - buttonRadius - 50,  // Bottom-right
            joystickRadius + 200,                         // Position far above joystick
            buttonRadius,                                 // Circular button
            "shoot_button.png"
        );

        reloadButton = new Button(
            Gdx.graphics.getWidth() - buttonRadius - 230,  // Bottom-right
            joystickRadius + 100,                         // Position between joystick and shoot button
            buttonRadius,                                 // Circular button
            "reload_button.png"
        );

        rampageButton = new Button(
            Gdx.graphics.getWidth() - buttonRadius - 1980,  // Bottom-right
            joystickRadius + 100,                         // Position between joystick and shoot button
            buttonRadius,
            "rampage_button.png"            // Texture path
        );

        // Create ammo display
        ammoDisplay = new AmmoDisplay(
            Gdx.graphics.getWidth() - 200,
            Gdx.graphics.getHeight() - 50
        );

        primaryWeaponButton = new WeaponSwitchButton(
            centerX - 100,  // Adjust positioning
            buttonRadius + 130,
            120,  // Width of the button
            60,   // Height of the button
            "Argus"  // Label or texture
        );

        secondaryWeaponButton = new WeaponSwitchButton(
            centerX + 100,
            buttonRadius + 130,
            120,
            60,
            "Red9"  // Label or texture
        );

    }

    @Override
    public void render(float delta) {
        // Handle input
        handleInput();

        // Handle scores
        scoreManager.update(delta);

        if (gameOver) {
            renderGameOver(batch);
            gameOverTimer -= delta;
            if (gameOverTimer <= 0) {
                Gdx.app.exit(); // Close the game
            }
            return; // Skip remaining updates
        }

        if (gameRunning) {
            gameTimer -= delta;
            if (gameTimer <= 0) {
                gameTimer = 0;
                endGame(); // Trigger the stats screen
            }
        }

        // Update the player with the enemies list for aim assist
        Vector2 movementDir = movementJoystick.isActive() ? movementJoystick.getDirection() : null;
        float movementIntensity = movementJoystick.isActive() ? movementJoystick.getIntensity() : 0;
        float targetRotation = rotationJoystick.isActive() ? rotationJoystick.getAngle() : -1;

        player.update(delta, movementDir, movementIntensity, targetRotation, enemies);

        // Only shoot if the shoot button is pressed
        if (shootButton.isPressed()) {
            player.shoot();
        }

        // Update enemies and check collisions
        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            enemy.update(delta, player);

            // Check bullet collisions
            for (Bullet bullet : player.getBullets()) {
                if (bullet.isActive() && bullet.checkCollision(enemy)) {
                    enemy.takeDamage(bullet.getDamage());
                }
            }

            // Remove dead enemies
            if (!enemy.isAlive()) {
                enemies.remove(i);
                scoreManager.addPoints(150);

                // Award Rampage charge (e.g., 5% base + 2% per combo count)
                float rampageFill = 5 + 2 * scoreManager.getComboCount();
                player.getRampage().addCharge(rampageFill);

                // Drop a pickup
                Pickup pickup = enemy.dropPickup();
                if (pickup != null) {
                    pickups.add(pickup);
                }
            }
        }

        // Spawn enemies
        enemySpawnTimer -= delta;
        if (enemySpawnTimer <= 0 && player.isAlive()) {
            spawnEnemy();
            enemySpawnTimer = enemySpawnDelay;
        }

        if (!player.isAlive() && !gameOver) {
            triggerGameOver();
        }

        // Clear screen
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // First render everything that uses SpriteBatch
        batch.begin();
        font.setColor(Color.WHITE);
        font.draw(batch, "Score: " + scoreManager.getScore(), 20, Gdx.graphics.getHeight() - 20);
        font.draw(batch, "Combo: " + scoreManager.getComboCount() + "x", 20, Gdx.graphics.getHeight() - 80);
        player.render(batch);
        for (Enemy enemy : enemies) {
            enemy.render(batch);
        }
        movementJoystick.render(batch);
        rotationJoystick.render(batch);
        shootButton.render(batch);
        rampageButton.render(batch);
        reloadButton.render(batch);
        primaryWeaponButton.render(batch);
        secondaryWeaponButton.render(batch);
        for (Pickup pickup : pickups) {
            pickup.render(batch);
        }

        // Render ammo display
        ammoDisplay.render(
            batch,
            player.getCurrentWeapon().getCurrentClipAmmo(),
            player.getCurrentWeapon().getCurrentReserveAmmo(),
            player.getCurrentWeapon().isReloading()
        );
        renderTimer(batch);

        batch.end();

        // Then render shape-based items (Line)
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        player.renderReticle(shapeRenderer);
        shapeRenderer.end();

        // Render other filled shapes after that (make sure to end before starting new batch)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        if (player.getRampage().isActive()) {
            // Yellow border
            shapeRenderer.setColor(1f, 1f, 0f, 0.1f);
            shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), 10);
            shapeRenderer.rect(0, 0, 10, Gdx.graphics.getHeight());
            shapeRenderer.rect(Gdx.graphics.getWidth() - 10, 0, 10, Gdx.graphics.getHeight());
            shapeRenderer.rect(0, Gdx.graphics.getHeight() - 10, Gdx.graphics.getWidth(), 10);
            Gdx.gl.glDisable(GL20.GL_BLEND);

            // "RAMPAGE!" text
            font.getData().setScale(2.0f);
            font.setColor(Color.YELLOW);
            batch.begin();
            font.draw(batch, "RAMPAGE!", Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() - 50);
            batch.end();
            font.getData().setScale(1.0f);
        }

        // Render health bar
        float healthProgress = player.getHealth() / player.getMaxHealth();
        healthBar.render(shapeRenderer, healthProgress);

        // Render future bar (static for now)
        rampageBar.render(shapeRenderer,
            player.getRampage().getCurrentFullCharges(),
            (int)player.getRampage().getMaxCharges()
        );

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void renderTimer(SpriteBatch batch) {
        int minutes = (int) (gameTimer / 60);
        int seconds = (int) (gameTimer % 60);
        int milliseconds = (int) ((gameTimer - (minutes * 60 + seconds)) * 100);

        String timerText = String.format("%02d:%02d:%02d", minutes, seconds, milliseconds);
        font.setColor(Color.WHITE);
        font.getData().setScale(2); // Adjust size
        font.draw(batch, timerText, Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() - 20);
        font.getData().setScale(1); // Reset size
    }

    private void spawnEnemy() {
        if (enemies.size() < MAX_ENEMIES) {
            float x, y;
            if (MathUtils.randomBoolean()) {
                // Spawn on left or right edge
                x = MathUtils.randomBoolean() ? 0 : Gdx.graphics.getWidth();
                y = MathUtils.random(0, Gdx.graphics.getHeight());
            } else {
                // Spawn on top or bottom edge
                x = MathUtils.random(0, Gdx.graphics.getWidth());
                y = MathUtils.randomBoolean() ? 0 : Gdx.graphics.getHeight();
            }
            enemies.add(new Enemy(x, y));
        }
    }

    private void triggerGameOver() {
        gameOver = true;
        gameOverTimer = GAME_OVER_DELAY;
        // Freeze all actions (enemies, player, etc.)
    }

    private void endGame() {
        gameRunning = false;

        // Transition to the stats screen
        int score = scoreManager.getScore();
        int highestCombo = scoreManager.getComboCount();
        StatsScreen statsScreen = new StatsScreen(score, highestCombo);
        ((Game) Gdx.app.getApplicationListener()).setScreen(statsScreen);
    }

    private void handleInput() {
        // Reset all controls at the start of the frame
        boolean rotationTouched = false;
        boolean shootTouched = false;
        boolean movementTouched = false;
        boolean reloadTouched = false;

        rampageButton.setDisabled(!player.getRampage().canActivate());

        // Handle multi-touch
        for (int i = 0; i < 10; i++) {
            if (Gdx.input.isTouched(i)) {
                float touchX = Gdx.input.getX(i);
                float touchY = Gdx.graphics.getHeight() - Gdx.input.getY(i); // Flip Y coordinate
                Vector2 touch = new Vector2(touchX, touchY);

                // Prioritize buttons over joystick interaction
                if (!shootTouched && !rotationTouched) {
                    // Handle shoot button
                    if (shootButton.isTouched(touchX, touchY)) {
                        shootButton.handleTouch(touch);
                        player.shoot();
                        shootTouched = true;
                        continue; // Skip joystick check
                    }
                }

                if (!reloadTouched && !rotationTouched) {
                    // Handle reload button
                    if (reloadButton.isTouched(touchX, touchY)) {
                        reloadButton.handleTouch(touch);
                        player.reload();
                        reloadTouched = true;
                        continue; // Skip joystick check
                    }
                }

                if (rampageButton.isTouched(touchX, touchY)) {
                    player.getRampage().activate();
                }

                // Handle weapon switch buttons
                if (primaryWeaponButton.isTouched(touchX, touchY)) {
                    player.switchToPrimaryWeapon();
                } else if (secondaryWeaponButton.isTouched(touchX, touchY)) {
                    player.switchToSecondaryWeapon();
                }

                // Handle movement joystick
                if (touchX < Gdx.graphics.getWidth() / 2 && !movementTouched) {
                    movementJoystick.handleTouch(touch);
                    movementTouched = true;
                    continue; // Skip rotation joystick check
                }

                // Handle rotation joystick (only if no button overlaps)
                if (!rotationTouched && !shootTouched && !reloadTouched &&
                    isWithinRadius(touch, rotationJoystick.getPosition().x,
                        rotationJoystick.getPosition().y, rotationJoystick.getRadius())) {
                    rotationJoystick.handleTouch(touch);
                    rotationTouched = true;
                }
            }
        }

        // Reset controls that aren't being touched
        if (!movementTouched) {
            movementJoystick.reset();
        }
        if (!rotationTouched) {
            rotationJoystick.reset();
        }
        if (!shootTouched) {
            shootButton.reset();
        }
        if (!reloadTouched) {
            reloadButton.reset();
        }

        for (int i = pickups.size() - 1; i >= 0; i--) {
            Pickup pickup = pickups.get(i);
            if (pickup.checkCollision(player.getPosition(), player.getPickupRadius())) {
                applyPickupEffect(pickup.getType());
                pickups.remove(i);
            }
        }
    }

    private void applyPickupEffect(Pickup.PickupType type) {
        switch (type) {
            case HEALTH:
                player.restoreHealth(20); // Restore 20 health
                break;
            case AMMO:
                player.addAmmoToWeapons(10); // Add 10 reserve ammo to both weapons
                break;
            case RAMPAGE:
                player.getRampage().addCharge(100); // Add one full segment (100%)
                break;
        }
    }

    private void renderGameOver(SpriteBatch batch) {
        batch.begin();
        font.setColor(Color.RED);
        font.getData().setScale(3); // Scale up the text for visibility
        String gameOverText = "GAME OVER";
        font.draw(batch, gameOverText,
            Gdx.graphics.getWidth() / 2 - font.getRegion().getRegionWidth() / 2,
            Gdx.graphics.getHeight() / 2);
        font.getData().setScale(1); // Reset font scale
        batch.end();
    }

    private boolean isWithinRadius(Vector2 touch, float x, float y, float radius) {
        return touch.dst(x, y) <= radius;
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        player.dispose();
        for (Enemy enemy : enemies) {
            enemy.dispose();
        }
        movementJoystick.dispose();
        rotationJoystick.dispose();
        shootButton.dispose();
        rampageButton.dispose();
        reloadButton.dispose();
        ammoDisplay.dispose();
    }

    // Other required Screen interface methods
    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
