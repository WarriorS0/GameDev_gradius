package engine.graphics;

import engine.Entity;
import engine.Grid;
import game.Game;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;

public class PacmanAvatar extends Avatar {
	private BufferedImage[] animations;
	private BufferedImage sprite;
	private double time;
	private int frameIndex;
	private final double animation_duree = 130;
	public boolean stop = false;

	public PacmanAvatar(Entity e) {
		super(e);
	}

	public void initImages(Graphics g) {
		sprite = g.load("src/engine/graphics/pacman_sprite.png");
		animations = new BufferedImage[3];
		change_animation();
		
	}
	
	public void change_animation() {
		double orientation = e.orientation();
		if(dead) {
			animations = new BufferedImage[11];
			animations[0] = sprite.getSubimage(503, 0, 15, 15);
			animations[1] = sprite.getSubimage(519, 0, 15, 15);
			animations[2] = sprite.getSubimage(535, 0, 15, 15);
			animations[3] = sprite.getSubimage(551, 0, 15, 15);
			animations[4] = sprite.getSubimage(567, 0, 15, 15);
			animations[5] = sprite.getSubimage(583, 0, 15, 15);
			animations[6] = sprite.getSubimage(599, 0, 15, 15);
			animations[7] = sprite.getSubimage(617, 0, 15, 15);
			animations[8] = sprite.getSubimage(634, 0, 15, 15);
			animations[9] = sprite.getSubimage(648, 0, 15, 15);
			animations[10] = sprite.getSubimage(661, 0, 15, 15);
		} else {
			if (orientation >= 0 && orientation < 90) {
				animations[0] = sprite.getSubimage(453, 0, 15, 15);
				animations[1] = sprite.getSubimage(470, 0, 15, 15);
				animations[2] = sprite.getSubimage(487, 0, 15, 15);
			} else if (orientation >= 90 && orientation < 180) {
				animations[0] = sprite.getSubimage(453, 47, 20, 15);
				animations[1] = sprite.getSubimage(470, 47, 20, 15);
				animations[2] = sprite.getSubimage(487, 0, 15, 15);
			} else if (orientation >= 180 && orientation < 270) {
				animations[0] = sprite.getSubimage(453, 15, 17, 15);
				animations[1] = sprite.getSubimage(470, 15, 17, 15);
				animations[2] = sprite.getSubimage(487, 0, 15, 15);
			} else {
				animations[0] = sprite.getSubimage(453, 35, 17, 12);
				animations[1] = sprite.getSubimage(470, 35, 17, 12);
				animations[2] = sprite.getSubimage(487, 0, 15, 15);
			}
		}
		
	}

	public void updateAnimation(double delta_t) {
		if (animations != null) {
			change_animation();
			time += delta_t;
			if (time >= animation_duree) {
				frameIndex++;
				if (frameIndex >= animations.length) {
					if(!dead) {
						frameIndex = 0;
					} else {
						stop = true;
					}
						
				}
				time -= animation_duree;
			}
		}
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
			BufferedImage img = animations[frameIndex];
			double targetWidth = e.box().halfWidth * 2 * Game.pixelPerCm;
	        double targetHeight = e.box().halfHeight * 2 * Game.pixelPerCm;
	        
	        int xTopLeft = xPixel - (int)(targetWidth / 2);
	        int yTopLeft = yPixel - (int)(targetHeight / 2);
			Object savedTransform = g.getTransform();
			g.translate(xTopLeft, yTopLeft);
			g.drawImage(img, -Game.pixelPerCm*3, -Game.pixelPerCm*2);
			g.setTransform(savedTransform);
		}
		
	}
}
