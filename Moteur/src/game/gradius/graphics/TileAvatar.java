package game.gradius.graphics;

import engine.geometry.ISU;
import engine.graphics.avatars.SpriteAvatar;
import engine.move.ViewPort;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;

/**
 * Draws a single {@link Tile} from its {@link TerrainKind} sprite, exactly once
 * per frame, and only when the tile is inside the view port.
 *
 * Bottom-row tiles are drawn as-is; top-row tiles are mirrored vertically (the
 * engine has no flip primitive, so the graphics are scaled by (1, -1) around the
 * tile). The shared sub-image is cut once at init and reused.
 */
public class TileAvatar extends SpriteAvatar {

	private static final String SPRITE_PATH = "src/game/gradius/graphics/background.png";

	/**
	 * Shared cut sprites, one per kind, built once and reused by every tile of
	 * that kind. Avoids re-cutting (and the parent re-loading) the sheet per tile.
	 */
	private static final java.util.EnumMap<TerrainKind, BufferedImage> SPRITES = new java.util.EnumMap<>(
			TerrainKind.class);

	private final Tile tile;
	private final ViewPort vp;
	private BufferedImage tileSprite;

	public TileAvatar(Tile tile, ViewPort vp) {
		super(tile, SPRITE_PATH, 1, 1);
		this.tile = tile;
		this.vp = vp;
	}

	@Override
	public void initImage(Graphics g) {
		if (tileSprite != null) {
			return; // already initialized for this avatar
		}

		TerrainKind k = tile.kind();

		// Cut each kind's sprite once, globally, and share it.
		tileSprite = SPRITES.get(k);
		if (tileSprite == null) {
			super.initImage(g, k.srcX, k.srcY, k.srcW, k.srcH); // loads sheet once for this avatar
			tileSprite = image.getSubimage(k.srcX, k.srcY, k.srcW, k.srcH);
			SPRITES.put(k, tileSprite);
		} else {
			// Mark parent initialized so its paint guard never reloads.
			super.initImage(g, k.srcX, k.srcY, k.srcW, k.srcH);
		}
	}

	@Override
	public void paint(Graphics g) {
		if (tileSprite == null) {
			initImage(g);
		}

		ISU.Coord coord = tile.center();

		if (coord == null) {
			return;
		}

		// Cull: skip tiles whose box does not overlap the view port. This is the
		// main optimization — only on-screen terrain is drawn.
		if (!overlapsViewPort(coord)) {
			return;
		}

		ISU.Dimension size = tile.size();

		int width = Math.max(1, cmToPixel(size.x()));
		int height = Math.max(1, cmToPixel(size.y()));

		int xCenter = cmToPixel(coord.x());
		int yCenter = cmToPixel(coord.y());

		int xTopLeft = xCenter - width / 2;
		int yTopLeft = yCenter - height / 2;

		if (!tile.isTopRow()) {
			g.drawImage(tileSprite, xTopLeft, yTopLeft, width, height);
		} else {
			// Mirror vertically around the tile center, draw once, restore.
			Object saved = g.getTransform();
			g.translate(0, yCenter);
			g.scale(1, -1);
			g.translate(0, -yCenter);
			g.drawImage(tileSprite, xTopLeft, yTopLeft, width, height);
			g.setTransform(saved);
		}

		// Optional collision-box overlay (cheap; only when debug is on).
		if (debugCollision) {
			g.drawRect(xTopLeft, yTopLeft, width, height);
		}
	}

	/**
	 * @return true if the tile's box overlaps the view port on both axes, taking
	 *         the toric geometry into account
	 */
	private boolean overlapsViewPort(ISU.Coord coord) {
		ISU.Dimension size = tile.size();
		double halfW = size.x() / 2.0;
		double halfH = size.y() / 2.0;

		// Test the tile's left/right and top/bottom extents; a tile is visible if
		// any of its spanning points falls inside the view port.
		return vp.isVisible(coord.x() - halfW, coord.y()) || vp.isVisible(coord.x() + halfW, coord.y())
				|| vp.isVisible(coord.x(), coord.y() - halfH) || vp.isVisible(coord.x(), coord.y() + halfH)
				|| vp.isVisible(coord.x(), coord.y());
	}
}