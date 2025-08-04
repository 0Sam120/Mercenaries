// Pickup.java
package come.onto.merc.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Pickup {
    public enum PickupType {
        HEALTH, AMMO, RAMPAGE
    }

    private Texture texture;
    private Vector2 position;
    private PickupType type;
    private Rectangle bounds;

    public Pickup(float x, float y, PickupType type) {
        this.position = new Vector2(x, y);
        this.type = type;

        // Load appropriate texture based on pickup type
        switch (type) {
            case HEALTH:
                texture = new Texture("pickup_health.png");
                break;
            case AMMO:
                texture = new Texture("pickup_ammo.png");
                break;
            case RAMPAGE:
                texture = new Texture("pickup_rampage.png");
                break;
        }

        bounds = new Rectangle(
            position.x - texture.getWidth() / 2,
            position.y - texture.getHeight() / 2,
            texture.getWidth(),
            texture.getHeight()
        );
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture,
            position.x - texture.getWidth() / 2,
            position.y - texture.getHeight() / 2
        );
    }

    public boolean checkCollision(Vector2 playerPosition, float playerRadius) {
        // Check if the pickup is within the player's pickup radius
        return bounds.contains(playerPosition.x, playerPosition.y);
    }

    public PickupType getType() {
        return type;
    }

    public void dispose() {
        texture.dispose();
    }
}
