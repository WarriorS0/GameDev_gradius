package game.gradius.graphics;

import game.Game;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;

public class MapView {

	private static final String SPRITE_PATH = "src/game/gradius/graphics/map_gradius.png";

	private BufferedImage mapImage;
	private boolean initialized;

	public MapView() {
		this.initialized = false;
	}

	public void initImages(Graphics g) {
		BufferedImage sprite = g.load(SPRITE_PATH);
		this.mapImage = sprite.getSubimage(317, 204, 200, 200);
		this.initialized = true;
	}

	public void paint(Graphics g) {
		if (!initialized) {
			initImages(g);
		}

		if (mapImage == null) {
			return;
		}

		Game game = Game.game();

		if (game == null) {
			return;
		}

		int totalWidthPixels = (int) Math.round(game.grid.width() * game.cmPerCell * game.pixelPerCm);
		int totalHeightPixels = (int) Math.round(game.grid.height() * game.cmPerCell * game.pixelPerCm);

		g.drawImage(mapImage, 0, 0, totalWidthPixels, totalHeightPixels);
	}
}