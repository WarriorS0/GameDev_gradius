package game.gradius.graphics;

import engine.entity.Entity;
import engine.gal.arguments.Category;
import engine.shape.Rect;

/**
 * A terrain tile of the tunnel: a solid obstacle of a given {@link TerrainKind},
 * placed against the top or bottom edge of the world.
 *
 * Its width and height come from the kind (height proportional to the sprite so
 * nothing is stretched). Every tile is a real obstacle, but the hitbox shape
 * depends on the kind: flat ground is a single base box, mountains add peak
 * boxes, so the player must fly around mountains while still being blocked from
 * leaving the tunnel.
 *
 * The collision boxes are declared from the anchored edge (the side glued to the
 * world border). For a bottom tile that edge is the bottom; for a top tile the
 * tile is mirrored, so the same declarations describe a peak pointing inward in
 * both rows.
 */
public class Tile extends Entity {

	private final TerrainKind kind;
	private final boolean topRow;

	/**
	 * @param kind   the terrain kind (sprite, width, collision boxes)
	 * @param topRow true if glued to the top edge (mirrored), false for bottom
	 */
	public Tile(TerrainKind kind, boolean topRow) {
		super("Tile");
		this.kind = kind;
		this.topRow = topRow;

		setSize(isu.new Dimension(kind.width_cm, kind.height_cm()));
		category(Category.Obstacle);
	}

	public TerrainKind kind() {
		return kind;
	}

	public boolean isTopRow() {
		return topRow;
	}

	@Override
	protected void setBounding() {
		clearBounding();

		double w = size().x();
		double h = size().y();
		double cx = center().x();
		double cy = center().y();

		double left = cx - w / 2.0;

		for (TerrainKind.Box box : kind.boxes) {
			// Horizontal span [x0, x1] -> center and width.
			double bw = (box.x1 - box.x0) * w;
			double bx = left + (box.x0 + box.x1) / 2.0 * w;

			// Vertical span [y0, y1] measured from the anchored edge inward.
			double bh = (box.y1 - box.y0) * h;
			double midFromEdge = (box.y0 + box.y1) / 2.0 * h; // distance of box center from the anchored edge

			// Bottom tile: anchored edge is the bottom (cy + h/2), inward is up.
			// Top tile: anchored edge is the top (cy - h/2), inward is down.
			double by = topRow ? (cy - h / 2.0) + midFromEdge : (cy + h / 2.0) - midFromEdge;

			addBounding(new Rect(isu.new Coord(bx, by), isu.new Dimension(bw, bh), orientation()));
		}
	}
}