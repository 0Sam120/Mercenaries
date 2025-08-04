// Player.java
package come.onto.merc.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

import come.onto.merc.ui.Reticle;
import come.onto.merc.weapons.BasicGun;
import come.onto.merc.weapons.Pistol;


public class Player {
    private Texture texture;
    private Vector2 position;
    private Weapon currentWeapon;
    private Weapon primaryWeapon;
    private Weapon secondaryWeapon;
    private Rampage rampage;
    private Reticle reticle;
    private float aimAssistRadius = 150f;
    private float maxAimAssistAngle = 20f;
    private float rotation;
    private float speed;
    private float health;
    private float maxHealth;
    private float invincibilityTime;
    private float currentInvincibilityTime;

    public Player(float x, float y) {
        texture = new Texture("player.png");
        position = new Vector2(x, y);
        rotation = 0;
        speed = 200;
        maxHealth = 100;
        health = maxHealth;
        invincibilityTime = 0.5f;
        currentInvincibilityTime = 0;
        primaryWeapon = new BasicGun();
        secondaryWeapon = new Pistol();
        currentWeapon = primaryWeapon;
        reticle = new Reticle();
        rampage = new Rampage(10, 0.01f, 5.0f);
    }


    public void update(float deltaTime, Vector2 movementDirection,
                       float movementIntensity, float targetRotation,
                       ArrayList<Enemy> enemies) {
        // Update invincibility timer
        if (currentInvincibilityTime > 0) {
            currentInvincibilityTime -= deltaTime;
        }

        // Update position
        if (movementDirection != null && movementIntensity > 0) {
            float currentSpeed = speed * movementIntensity * 2;
            position.x += movementDirection.x * currentSpeed * deltaTime;
            position.y += movementDirection.y * currentSpeed * deltaTime;

            // Keep within bounds
            position.x = MathUtils.clamp(position.x,
                texture.getWidth()/2,
                Gdx.graphics.getWidth() - texture.getWidth()/2);
            position.y = MathUtils.clamp(position.y,
                texture.getHeight()/2,
                Gdx.graphics.getHeight() - texture.getHeight()/2);
        }

        // Only update rotation if we have valid input (targetRotation != -1)
        if (targetRotation >= 0) {
            float rotationSpeed = 360f;
            float angleDiff = ((targetRotation - rotation + 360) % 360);
            if (angleDiff > 180) angleDiff -= 360;
            rotation += Math.signum(angleDiff) *
                Math.min(rotationSpeed * deltaTime, Math.abs(angleDiff));
        }

        rampage.update(deltaTime);

        // Update weapon
        currentWeapon.update(deltaTime, position);

        reticle.update(position.x, position.y, rotation);

        // Calculate aim assist
        if (!enemies.isEmpty()) {
            Vector2 aimDir = new Vector2(1, 0).setAngleDeg(rotation);
            Enemy closestEnemy = null;
            float closestAngle = maxAimAssistAngle;

            for (Enemy enemy : enemies) {
                Vector2 enemyDir = new Vector2(enemy.getPosition()).sub(position).nor();
                float angle = Math.abs(aimDir.angleDeg(enemyDir));

                if (angle < closestAngle && position.dst(enemy.getPosition()) <= aimAssistRadius) {
                    closestAngle = angle;
                    closestEnemy = enemy;
                }
            }

            if (closestEnemy != null) {
                Vector2 enemyDir = new Vector2(closestEnemy.getPosition()).sub(position);
                rotation = enemyDir.angleDeg();
            }
        }
    }

    public void switchToPrimaryWeapon() {
        currentWeapon = primaryWeapon;
    }

    public void switchToSecondaryWeapon() {
        currentWeapon = secondaryWeapon;
    }

    // Method to get current weapon for UI display
    public boolean isPrimaryWeaponActive() {
        return currentWeapon == primaryWeapon;
    }

    public void takeDamage(float damage) {
        if (currentInvincibilityTime <= 0) {
            health -= damage;
            currentInvincibilityTime = invincibilityTime;
        }
    }

    public void restoreHealth(float amount) {
        health = Math.min(health + amount, maxHealth);
    }

    public void addAmmoToWeapons(int amount) {
        if (primaryWeapon != null) {
            primaryWeapon.addReserveAmmo(amount);
        }
        if (secondaryWeapon != null) {
            secondaryWeapon.addReserveAmmo(amount);
        }
    }

    public float getPickupRadius() {
        return 30f; // Radius for picking up items
    }


    public boolean isAlive() {
        return health > 0;
    }


    public Vector2 getPosition() {
        return position;
    }

    public float getHealth() {
        return health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public Rampage getRampage() {
        return rampage;
    }

    public void shoot() {
        if (rampage.isActive()) {
            // Double Tap: Fire two bullets for the price of one
            currentWeapon.shoot(rotation);
            currentWeapon.shoot(rotation); // Fire a second shot without consuming extra ammo
        } else {
            // Normal behavior
            currentWeapon.shoot(rotation);
        }
    }

    public void reload() {
        currentWeapon.startReload();
    }


    public void render(SpriteBatch batch) {
        // Render bullets from current weapon
        for (Bullet bullet : currentWeapon.getBullets()) {
            bullet.render(batch);
        }

        batch.draw(texture,
            position.x - texture.getWidth()/2,
            position.y - texture.getHeight()/2,
            texture.getWidth()/2,
            texture.getHeight()/2,
            texture.getWidth(),
            texture.getHeight(),
            1, 1,
            rotation,
            0, 0,
            texture.getWidth(),
            texture.getHeight(),
            false, false);
    }

    public void renderReticle(ShapeRenderer shapeRenderer) {
        reticle.render(shapeRenderer);
    }


    public ArrayList<Bullet> getBullets() {
        return currentWeapon.getBullets();
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }


    public void dispose() {
        texture.dispose();
        Bullet.dispose();
    }

}
