package engine.graphics;

import engine.Entity;
import game.Game;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;

public class InkyAvatar extends Avatar{
	private BufferedImage[] animations;
	private BufferedImage sprite;
	private double time;
	private int frameIndex;
	private final double animation_duree = 130;

	public boolean alive = true;
	public boolean weak = false;
	public boolean weak_over = false;
	public boolean dead = false;
	public boolean stop = false;

	public InkyAvatar(Entity e) {
		super(e);
	}

	public void initImages(Graphics g) {
		sprite = g.load("src/engine/graphics/pacman_sprite.png");
		animations = change_animation();
	}

	public BufferedImage[] change_animation() {
		double orientation = e.orientation();
		if (weak) {
			animations = new BufferedImage[2];
			animations[0] = sprite.getSubimage(583, 64, 16, 18);
			animations[1] = sprite.getSubimage(600, 64, 16, 18);
		} else if (weak_over) {
			animations = new BufferedImage[4];
			animations[0] = sprite.getSubimage(583, 64, 16, 18);
			animations[1] = sprite.getSubimage(616, 64, 16, 18);
			animations[2] = sprite.getSubimage(600, 64, 16, 18);
			animations[3] = sprite.getSubimage(631, 64, 16, 18);
		} else if (dead) {
			animations = new BufferedImage[1];
			if (orientation >= 0 && orientation < 90) {
				animations[0] = sprite.getSubimage(584, 82, 16, 18);
			} else if (orientation >= 90 && orientation < 180) {
				animations[0] = sprite.getSubimage(632, 82, 16, 20);
			} else if (orientation >= 180 && orientation < 270) {
				animations[0] = sprite.getSubimage(600, 82, 16, 18);
			} else {
				animations[0] = sprite.getSubimage(615, 80, 16, 18);
			}
		} else {
			animations = new BufferedImage[2];
			if (orientation >= 0 && orientation < 90) {
				animations[0] = sprite.getSubimage(455, 95, 16, 16);
				animations[1] = sprite.getSubimage(471, 95, 16, 16);
			} else if (orientation >= 90 && orientation < 180) {
				animations[0] = sprite.getSubimage(551, 95, 16, 16);
				animations[1] = sprite.getSubimage(567, 95, 16, 16);
			} else if (orientation >= 180 && orientation < 270) {
				animations[0] = sprite.getSubimage(487, 95, 16, 16);
				animations[1] = sprite.getSubimage(503, 95, 16, 16);
			} else {
				animations[0] = sprite.getSubimage(519, 95, 16, 16);
				animations[1] = sprite.getSubimage(536, 95, 16, 16);
			}
		}
		return animations;

	}

	public void updateAnimation(double delta_t) {
		if (sprite != null) {
			animations = change_animation();
			if (animations != null) {
				time += delta_t;
				if (time >= animation_duree) {
					frameIndex++;
					if (frameIndex >= animations.length) {
						frameIndex = 0;
					}
					time -= animation_duree;
				}
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
