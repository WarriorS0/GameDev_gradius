package engine.graphics.hud;

import java.util.function.Supplier;

import engine.entity.Entity;
import engine.geometry.ISU;
import engine.graphics.View;
import game.Game;
import oop.graphics.Color;

public class FollowerLabel extends Label implements IFollower {

	protected static final double cmPerCell;
	protected static final View view;
	protected static final ISU isu;
	static {
		Game game = Game.game();
		cmPerCell = game.cmPerCell;
		view = game.view;
		isu = game.isu;
	}

	protected Entity target;
	protected boolean mustBeDeleted;
	protected PixelCoordinate lastPos;

	public FollowerLabel(String text, Entity target, PixelCoordinate offset, Color color) {
		this(() -> text, target, offset, color);
	}

	public FollowerLabel(Supplier<String> text, Entity target, PixelCoordinate offset, Color color) {
		this(text, target, offset, true, Label.DEFAULT_FONT_NAME, Label.DEFAULT_FONT_SIZE, Label.DEFAULT_FONT_STYLE,
				color, true);
	}

	public FollowerLabel(Supplier<String> text, Entity target, PixelCoordinate offset, boolean visible, String fontName,
			int fontSize, int fontStyle, Color color, boolean centeredText) {
		super(text, new PixelCoordinate(0, 0), new PixelCoordinate(0, 0), visible, fontName, fontSize, fontStyle, color,
				centeredText);
		this.target = target;
		this.offset = offset;
		this.lastPos = new PixelCoordinate(0, 0);
		this.mustBeDeleted = false;
	}

	public static ISU.Coord center(ISU.Coord c) {
		return isu.new Coord(c.x() + cmPerCell / 2.0, c.y() + cmPerCell / 2.0);
	}

	@Override
	public void update(int canvasWidth, int canvasHeigh) {
		if (target == null || target.dead()) {
			// setVisibility(false); // label will be deleted if targe is dead.
			// target shouldn't be null in the first place
			this.mustBeDeleted = true;
			return;
		}

		// On vise le centre de l'entité (entities are placed by their top-left
		// corner, so add half a cell to reach the middle).
		ISU.Coord eCenter = target.center();
		ISU.Coord center = center(eCenter);

		PixelCoordinate screen = view.worldToScreen(center);

		if (screen == null) {
			if (!FOLLOWING_LABEL_SHOULD_STAY) {
				// Target is outside the view port: do not draw.
				// TODO DUE TO THE BUG WITH THE GAME SIZE, LABEL DON'T SHOW CORRECTLY
				// NOT A LABEL BUG, it's a viewport bug
				setVisibility(false);
			}
			return;
		} else {
			lastPos = screen;
		}

		this.position = IHudElement.getNewPositionFromCurrentPositionAndOffset(lastPos, offset);
	}

	/**
	 * getter to check if the target entity is still alive
	 * 
	 * @return true if target entity is still alive
	 */
	@Override
	public boolean isTargetEntityStillAlive() {
		return this.target.alive();
	}

	@Override
	public boolean mustBeDeleted() {
		return this.mustBeDeleted;
	}

	@Override
	public Entity getTarget() {
		return this.target;
	}
}