package game.gradius.graphics;

import engine.entity.Entity;
import engine.graphics.Avatar;
import oop.graphics.Graphics;
import oop.graphics.Graphics.Colors;

public class ProjectileAvatar extends Avatar {

	public ProjectileAvatar(Entity entity) {
		super(entity);
	}

	@Override
	public void initImages(Graphics g) {
		// Rien à charger pour ce test.
	}

	@Override
	public void updateAnimation(double delta_t) {
		// Pas d'animation pour l'instant.
	}

	@Override
	public void paint(Graphics g) {
		if (dead() || entity().center() == null) {
			return;
		}

		int width = Math.max(1, this.cmToPixel(entity().size().x()));
		int height = Math.max(1, this.cmToPixel(entity().size().y()));

		int xCenter = this.cmToPixel(entity().center().x());
		int yCenter = this.cmToPixel(entity().center().y());

		int xTopLeft = xCenter - width / 2;
		int yTopLeft = yCenter - height / 2;

		g.setColor(Colors.white);
		g.fillRect(xTopLeft, yTopLeft, width, height);
	}
}