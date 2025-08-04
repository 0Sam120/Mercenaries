// UIBar.java
package come.onto.merc.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class UIBar {
    private float x, y, width, height;
    private int segments;
    private float segmentGap;
    private Color backgroundColor;
    private Color foregroundColor;

    public UIBar(float x, float y, float width, float height, int segments, float segmentGap, Color backgroundColor, Color foregroundColor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.segments = segments;
        this.segmentGap = segmentGap;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
    }

    // Render the bar
    public void render(ShapeRenderer shapeRenderer, float progress) {
        // Draw background bar
        shapeRenderer.setColor(backgroundColor);
        shapeRenderer.rect(x, y, width, height);

        // Draw foreground (progress) bar
        shapeRenderer.setColor(foregroundColor);
        shapeRenderer.rect(x, y, width * progress, height);
    }

    public void render(ShapeRenderer shapeRenderer, int activeSegments, int maxSegments) {
        float segmentWidth = (width - (maxSegments - 1) * segmentGap) / maxSegments;

        for (int i = 0; i < maxSegments; i++) {
            float segmentX = x + i * (segmentWidth + segmentGap);
            shapeRenderer.setColor(i < activeSegments ? foregroundColor : backgroundColor);
            shapeRenderer.rect(segmentX, y, segmentWidth, height);
        }
    }
}
