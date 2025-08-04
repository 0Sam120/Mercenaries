// Bullet.java
package come.onto.merc.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

public class Bullet {
    private static Texture texture;
    private Vector2 position;
    private Vector2 velocity;
    private float damage;
    private boolean active;
    private Rectangle bounds;

    public static void init() {
        texture = new Texture("bullet.png");
    }

    public static void dispose() {
        texture.dispose();
    }

    public Bullet(float x, float y, float angle, float damage, float speed) {
        position = new Vector2(x, y);
        velocity = new Vector2(1, 0).setAngleDeg(angle).scl(speed);
        this.damage = damage;
        active = true;
        bounds = new Rectangle(x - texture.getWidth()/2,
            y - texture.getHeight()/2,
            texture.getWidth(),
            texture.getHeight());
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.setPosition(position.x - texture.getWidth()/2,
            position.y - texture.getHeight()/2);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture,
            position.x - texture.getWidth()/2,
            position.y - texture.getHeight()/2);
    }

    public boolean checkCollision(Enemy enemy) {
        if (bounds.overlaps(enemy.getBounds())) {
            active = false;
            return true;
        }
        return false;
    }

    public float getDamage() {
        return damage;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isOutOfBounds() {
        return position.x < 0 || position.x > com.badlogic.gdx.Gdx.graphics.getWidth() ||
            position.y < 0 || position.y > com.badlogic.gdx.Gdx.graphics.getHeight();
    }
}

