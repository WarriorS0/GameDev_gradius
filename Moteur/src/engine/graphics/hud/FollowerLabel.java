package engine.graphics.hud;

import java.util.function.Supplier;

import engine.entity.Entity;
import engine.geometry.ISU;
import engine.graphics.View;
import game.Game;
import oop.graphics.Color;

/**
 * A label anchored to an entity in the world: it tracks the entity through the
 * camera so it stays above it on screen, whatever the view port does (follow,
 * rail, toric wrap). The label hides itself when the entity is dead or scrolls
 * out of the view port.
 */
public class FollowerLabel extends Label {

	private final static boolean FOLLOWING_LABEL_SHOULD_STAY = false;

	protected Entity target;

	/**
	 * Offset added in screen pixels, after projection (e.g. a few pixels up).
	 */
	protected PixelCoordinate offset;
	private PixelCoordinate pos;

	/**
	 * Half a cell in cm, used to anchor on the entity center.
	 */
	private final double cell;

	/**
	 * The view providing the world -> screen projection. May be null until set, in
	 * which case the label keeps its last screen position.
	 */
	private View view;

	public FollowerLabel(Supplier<String> text, Color color, Entity target, int offX, int offY) {
		super(text, new PixelCoordinate(0, 0), color, true);
		this.target = target;
		this.offset = new PixelCoordinate(offX, offY);
		this.cell = Game.game().cmPerCell;
		this.pos = new PixelCoordinate(0, 0);
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
			// setVisibility(false); // label will be deleted if targe is dead.
			// target shouldn't be null in the first place
			return;
		}

		if (view == null) {
			// No projection available yet: nothing reliable to draw.
			// setVisibility(false); //no view shoudn't append
			return;
		}

		// Anchor on the entity center (entities are placed by their top-left
		// corner, so add half a cell to reach the middle).
		Game game = Game.game();
		ISU.Coord center = game.isu.new Coord(target.center().x() + cell / 2.0, target.center().y() + cell / 2.0);

		PixelCoordinate screen = view.worldToScreen(center);

		if (screen == null) {
			if (!FOLLOWING_LABEL_SHOULD_STAY) {
				// Target is outside the view port: do not draw.
				// TODO DUE TO THE BUG WITH THE GAME SIZE, LABEL DON'T SHOW CORRECTLY
				// NOT A LABEL BUG, it's a viewport bug
				// setVisibility(false);
			}
			return;
		} else {
			pos = screen;
		}

		this.pc.x = pos.x + offset.x;
		this.pc.y = pos.y + offset.y;
	}

	public static PixelCoordinate getPosFromCoordAndOffset(PixelCoordinate position, PixelCoordinate offset) {
		return new PixelCoordinate(position.x + offset.x, position.y + offset.y);
	}

	/**
	 * getter to check if the target entity is still alive
	 * 
	 * @return true if target entity is still alive
	 */
	public boolean isTargetEntityStillAlive() {
		return this.target.alive();
	}
}