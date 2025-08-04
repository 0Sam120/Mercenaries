// Enemy.java
package come.onto.merc.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy {
    private Texture texture;
    private Vector2 position;
    private Rectangle bounds;
    private float health;
    private float rotation;
    private float speed;
    private float damage;
    private float attackRange;
    private float attackCooldown;
    private float currentAttackCooldown;
    private float hitFlashTime;
    private static final float HIT_FLASH_DURATION = 0.1f;// Duration of the flash effect
    private static final float DROP_CHANCE = 0.3f;

    public Enemy(float x, float y) {
        texture = new Texture("enemy.png"); // Triangle with red tip
        position = new Vector2(x, y);
        rotation = 0;
        speed = 150; // Slightly slower than player
        damage = 10;
        attackRange = 50;
        attackCooldown = 1.0f; // Attack once per second
        currentAttackCooldown = 0;
        bounds = new Rectangle(x - texture.getWidth()/2,
            y - texture.getHeight()/2,
            texture.getWidth(),
            texture.getHeight());
        health = 100;
        hitFlashTime = 0;
    }

    public void update(float deltaTime, Player player) {
        // Update attack cooldown
        if (currentAttackCooldown > 0) {
            currentAttackCooldown -= deltaTime;
        }

        // Only chase if player is alive
        if (player.isAlive()) {
            // Get direction to player
            Vector2 directionToPlayer = new Vector2(player.getPosition()).sub(position).nor();

            // Move towards player
            position.x += directionToPlayer.x * speed * deltaTime;
            position.y += directionToPlayer.y * speed * deltaTime;

            // Update rotation to face player
            rotation = directionToPlayer.angle();

            // Check if in range to attack
            float distanceToPlayer = position.dst(player.getPosition());
            if (distanceToPlayer <= attackRange && currentAttackCooldown <= 0) {
                player.takeDamage(damage);
                currentAttackCooldown = attackCooldown;
            }
        }

        // Update bounds position - Moved outside of cooldown check
        bounds.setPosition(position.x - texture.getWidth()/2,
            position.y - texture.getHeight()/2);

        // Update hit flash
        if (hitFlashTime > 0) {
            hitFlashTime -= deltaTime;
        }
    }

    public void render(SpriteBatch batch) {
        Color oldColor = batch.getColor();
        if (hitFlashTime > 0) {
            // Flash white when hit
            batch.setColor(1, 1, 1, 1);
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

        batch.setColor(oldColor);
    }

    public Pickup dropPickup() {
        if (Math.random() < DROP_CHANCE) {
            Pickup.PickupType type = determinePickupType();
            return new Pickup(position.x, position.y, type);
        }
        return null;
    }

    private Pickup.PickupType determinePickupType() {
        float random = MathUtils.random();
        if (random < 0.4f) return Pickup.PickupType.HEALTH; // 40% chance for health
        else if (random < 0.7f) return Pickup.PickupType.AMMO; // 30% chance for ammo
        else return Pickup.PickupType.RAMPAGE; // 30% chance for Rampage
    }

    public void takeDamage(float damage) {
        health -= damage;
        hitFlashTime = HIT_FLASH_DURATION;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void dispose() {
        texture.dispose();
    }
}
