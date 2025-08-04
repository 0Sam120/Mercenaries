//Reticle
package come.onto.merc.ui;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Reticle {
    private float size;
    private Vector2 position;
    private float distanceFromPlayer;  // Added this field

    public Reticle() {
        size = 15f;  // Increased from 8f to 15f for better visibility
        position = new Vector2();
        distanceFromPlayer = 100f;  // Increased from 50f to 100f to place it further ahead
    }

    public void update(float playerX, float playerY, float playerRotation) {
        float angleRad = (float)Math.toRadians(playerRotation);
        position.x = playerX + (float)Math.cos(angleRad) * distanceFromPlayer;
        position.y = playerY + (float)Math.sin(angleRad) * distanceFromPlayer;
    }

    public void render(ShapeRenderer shapeRenderer) {
        // Make the reticle more visible with a thicker line
        shapeRenderer.setColor(1, 1, 1, 0.8f);  // Increased opacity from 0.5f to 0.8f

        // Draw outer circle
        shapeRenderer.circle(position.x, position.y, size);

        // Draw crosshair lines
        shapeRenderer.line(position.x - size * 1.5f, position.y, position.x + size * 1.5f, position.y);
        shapeRenderer.line(position.x, position.y - size * 1.5f, position.x, position.y + size * 1.5f);
    }

    public Vector2 getPosition() {
        return position;
    }
}
