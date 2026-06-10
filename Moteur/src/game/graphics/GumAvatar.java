package engine.graphics;

import engine.Entity;
import game.Game;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;

public class GumAvatar extends Avatar {
	private BufferedImage animations;
	private BufferedImage sprite;
	private boolean stop = false;

	public GumAvatar(Entity e) {
		super(e);
	}

	public void initImages(Graphics g) {
		sprite = g.load("src/engine/graphics/pacman_sprite.png");
		animations = sprite.getSubimage(11, 11, 1, 1);
		
	}
	

	@Override
	public void paint(Graphics g) {
		if (animations == null) {
			initImages(g);
		}
		double x_cm = e.center().x();
		double y_cm = e.center().y();
		int xPixel = (int) (x_cm * Game.pixelPerCm);
		int yPixel = (int) (y_cm * Game.pixelPerCm);
		if(stop == false) {
			BufferedImage img = animations;
			double targetWidth = e.box().halfWidth * 2 * Game.pixelPerCm;
	        double targetHeight = e.box().halfHeight * 2 * Game.pixelPerCm;
	        
	        int xTopLeft = xPixel - (int)(targetWidth / 2);
	        int yTopLeft = yPixel - (int)(targetHeight / 2);
			Object savedTransform = g.getTransform();
			g.translate(xTopLeft, yTopLeft);
			g.drawImage(img, -Game.pixelPerCm, -Game.pixelPerCm);
			g.setTransform(savedTransform);
		}
		
	}

	@Override
	public void updateAnimation(double delta_t) {
		// TODO Auto-generated method stub
		
	}
}
