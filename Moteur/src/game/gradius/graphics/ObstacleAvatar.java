package game.gradius.graphics;

import engine.entity.Entity;
import engine.graphics.Avatar;
import game.Game;
import oop.graphics.Graphics;
import oop.graphics.Graphics.Colors;

public class ObstacleAvatar extends Avatar {

	public ObstacleAvatar(Entity entity) {
		super(entity);
	}

	@Override
	public void initImages(Graphics g) {
		// Rien à charger pour ce test.
	}

	@Override
	public void updateAnimation(double delta_t) {
		// Pas d'animation.
	}

	@Override
	public void paint(Graphics g) {
		if (dead() || entity().center() == null) {
			return;
		}

		double pixelPerCm = Game.game().pixelPerCm;

		int width = Math.max(1, (int) Math.round(entity().size().x() * pixelPerCm));
		int height = Math.max(1, (int) Math.round(entity().size().y() * pixelPerCm));

		int xCenter = (int) Math.round(entity().center().x() * pixelPerCm);
		int yCenter = (int) Math.round(entity().center().y() * pixelPerCm);

		int xTopLeft = xCenter - width / 2;
		int yTopLeft = yCenter - height / 2;

		g.setColor(Colors.red);
		g.fillRect(xTopLeft, yTopLeft, width, height);

		g.setColor(Colors.white);
		g.drawRect(xTopLeft, yTopLeft, width, height);
	}
}