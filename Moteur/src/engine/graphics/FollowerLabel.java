package engine.graphics;

import java.util.function.Supplier;

import engine.entity.Entity;
import game.Game;
import oop.graphics.Color;

public class FollowerLabel extends Label {
	protected Entity target;
	protected PixelCoordinate offset;
	double cell;
	double pixelPerCm;
	int size;

	public FollowerLabel(Supplier<String> text, Color color, Entity target, int offX, int offY) {
		super(text, new PixelCoordinate((int) target.center().x(), (int) target.center().y()), color, true);
		this.target = target;
		this.offset = new PixelCoordinate(offX, offY);
		this.update();

		Game game = Game.game();

		cell = game.cmPerCell;
		pixelPerCm = game.pixelPerCm;
	}

	public void update() {
		if (this.target == null || this.target.dead())
			this.setVisibility(false);
		else {
			int xCenter = (int) Math.round((target.center().x() + cell / 2.0) * pixelPerCm);
			int yCenter = (int) Math.round((target.center().y() + cell / 2.0) * pixelPerCm);

			this.pc.x = xCenter + offset.x;
			this.pc.y = yCenter + offset.y;
		}

	}

	public static PixelCoordinate getPosFromCoordAndOffset(PixelCoordinate position, PixelCoordinate offset) {
		return new PixelCoordinate(position.x + offset.x, position.y + offset.y);
	}

}