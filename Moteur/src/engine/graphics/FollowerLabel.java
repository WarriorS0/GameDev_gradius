package engine.graphics;

import java.util.function.Supplier;

import engine.entity.Entity;
import engine.geometry.ISU;
import game.Game;
import oop.graphics.Color;

/**
 * A label anchored to an entity in the world: it tracks the entity through the
 * camera so it stays above it on screen, whatever the view port does (follow,
 * rail, toric wrap). The label hides itself when the entity is dead or scrolls
 * out of the view port.
 */
public class FollowerLabel extends Label {

	protected Entity target;

	/**
	 * Offset added in screen pixels, after projection (e.g. a few pixels up).
	 */
	protected PixelCoordinate offset;

	/**
	 * Half a cell in cm, used to anchor on the entity center.
	 */
	private final double cell;

	/**
	 * The view providing the world -> screen projection. May be null until set,
	 * in which case the label keeps its last screen position.
	 */
	private View view;

	public FollowerLabel(Supplier<String> text, Color color, Entity target, int offX, int offY) {
		super(text, new PixelCoordinate(0, 0), color, true);
		this.target = target;
		this.offset = new PixelCoordinate(offX, offY);
		this.cell = Game.game().cmPerCell;
	}

	/**
	 * Binds the view used to project the target position. Call once after building
	 * the view (e.g. right after view.setHUD(...)).
	 *
	 * @param view the view driving the projection
	 */
	public void setView(View view) {
		this.view = view;
	}

	/**
	 * Recomputes the screen position from the target's world coordinates, going
	 * through the camera. Hides the label if the target is gone or off-screen.
	 */
	public void update() {
		if (target == null || target.dead()) {
			setVisibility(false);
			return;
		}

		if (view == null) {
			// No projection available yet: nothing reliable to draw.
			setVisibility(false);
			return;
		}

		// Anchor on the entity center (entities are placed by their top-left
		// corner, so add half a cell to reach the middle).
		Game game = Game.game();
		ISU.Coord center = game.isu.new Coord(target.center().x() + cell / 2.0, target.center().y() + cell / 2.0);

		PixelCoordinate screen = view.worldToScreen(center);

		if (screen == null) {
			// Target is outside the view port: do not draw.
			setVisibility(false);
			return;
		}

		setVisibility(true);
		this.pc.x = (int) (target.center().x() + offset.x)+screen.x;
		this.pc.y = (int) (target.center().y()+offset.y)+screen.y;
	}

	public static PixelCoordinate getPosFromCoordAndOffset(PixelCoordinate position, PixelCoordinate offset) {
		return new PixelCoordinate(position.x + offset.x, position.y + offset.y);
	}
}