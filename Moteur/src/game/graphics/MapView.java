package engine.graphics;

import game.Game;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;

public class MapView {
    private BufferedImage mapImage;
    private boolean isInitialized = false;

    public void initImages(Graphics g) {
        BufferedImage sprite = g.load("src/engine/graphics/pacman_sprite.png");
        mapImage = sprite.getSubimage(225, 0, 230, 248);
        isInitialized = true;
    }

    public void paint(Graphics g) {
        if (!isInitialized) {
            initImages(g);
        }
        
        if (mapImage != null) {
            int totalWidthPixels = (int) (Game.width_ncell * Game.cmPerCell * Game.pixelPerCm);
            int totalHeightPixels = (int) (Game.height_ncell * Game.cmPerCell * Game.pixelPerCm);
            g.drawImage(mapImage, 0, 0, totalWidthPixels, totalHeightPixels);
        }
    }
}