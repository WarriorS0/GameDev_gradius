package engine.graphics.avatars;

import java.util.logging.Level;

import engine.entity.Entity;
import engine.geometry.ISU;
import oop.graphics.Color;
import oop.graphics.Graphics;
import oop.graphics.Graphics.Colors;

public class ShapeAvatar extends Avatar {

	public static final Color DEFAULT_BOX_COLOR = Colors.red;

	protected ShapeAvatar(Entity entity, int multX, int multY) {
		super(entity);
		this.multX = multX;
		this.multY = multY;
		this.colorShape = DEFAULT_BOX_COLOR; // default Color
	}

	private final Color colorShape;

	@Override
	public void paint(Graphics g) {

		ISU.Coord coord = entity.center();

		int xCenter = this.cmToPixel(coord.x());
		int yCenter = this.cmToPixel(coord.y());

		ISU.Dimension size = entity.size();

		int width = Math.max(1, this.cmToPixel(size.x())) * this.multX;
		int height = Math.max(1, this.cmToPixel(size.y())) * this.multY;

		int xTopLeft = xCenter - width / 2;
		int yTopLeft = yCenter - height / 2;

		g.setColor(colorShape);
		g.drawRect(xTopLeft, yTopLeft, width, height);
	}

	@Override
	public void updateAnimation(double delta_t) {
		// no animation
	}

	@Override
	public void initImage(Graphics g) {
		if (hasBeenInitialized)
			return;
		hasBeenInitialized = true;
		if (LOGGING && INFO) {
			logger.log(Level.INFO, "Init shape avatar");
		}
		// no image to init...
	}

}
