// Weapon.java
package come.onto.merc.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public abstract class Weapon {
    protected float shootCooldown;
    protected float currentShootCooldown;
    protected float damage;
    protected float bulletSpeed;
    protected Vector2 position;
    protected ArrayList<Bullet> bullets;

    protected int clipSize;
    protected int currentClipAmmo;
    protected int maxReserveAmmo;
    protected int currentReserveAmmo;
    protected float reloadTime;
    protected float currentReloadTime;
    protected boolean isReloading;

    public Weapon() {
        bullets = new ArrayList<Bullet>();
        position = new Vector2();
        isReloading = false;
    }

    public void update(float deltaTime, Vector2 position) {
        this.position.set(position);

        if (currentShootCooldown > 0) {
            currentShootCooldown -= deltaTime;
        }

        // Handle reloading
        if (isReloading) {
            currentReloadTime -= deltaTime;
            if (currentReloadTime <= 0) {
                completeReload();
            }
        }

        // Update bullets
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            bullet.update(deltaTime);
            if (bullet.isOutOfBounds() || !bullet.isActive()) {
                bullets.remove(i);
            }
        }
    }

    public void addReserveAmmo(int amount) {
        currentReserveAmmo = Math.min(currentReserveAmmo + amount, maxReserveAmmo);
    }

    public void startReload() {
        if (!isReloading && currentClipAmmo < clipSize && currentReserveAmmo > 0) {
            isReloading = true;
            currentReloadTime = reloadTime;
        }
    }

    protected void completeReload() {
        int ammoNeeded = clipSize - currentClipAmmo;
        int ammoToAdd = Math.min(ammoNeeded, currentReserveAmmo);

        currentClipAmmo += ammoToAdd;
        currentReserveAmmo -= ammoToAdd;
        isReloading = false;
    }

    public abstract void shoot(float rotation);

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void dispose() {
        Bullet.dispose();
    }

    // Getters for UI display
    public int getCurrentClipAmmo() {
        return currentClipAmmo;
    }

    public int getCurrentReserveAmmo() {
        return currentReserveAmmo;
    }

    public boolean isReloading() {
        return isReloading;
    }

    public float getReloadProgress() {
        return isReloading ? 1 - (currentReloadTime / reloadTime) : 1.0f;
    }
}
