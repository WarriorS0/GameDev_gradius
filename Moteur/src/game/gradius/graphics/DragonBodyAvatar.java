package game.gradius.graphics;

import engine.entity.Entity;
import engine.geometry.ISU;
import engine.graphics.Avatar;
import game.Game;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;

public class DragonBodyAvatar extends Avatar {
	
	private static final String SPRITE_PATH = "src/game/gradius/graphics/vulture_dragon.png";
	private static final double DEATH_ANIMATION_DURATION_MS = 130.0;
	private BufferedImage sprites;
	private BufferedImage[] orientations;
	private BufferedImage[] death_animation;
	private boolean deathAnimationFinished;
	private BufferedImage current;
	
	public DragonBodyAvatar(Entity entity) {
		super(entity);
		Game.game().view.add(this);
	}
	
	@Override
	public void initImage(Graphics g) {
		sprites = g.load(SPRITE_PATH);
		orientations = new BufferedImage[16];
		orientations[0] = sprites.getSubimage(1, 101,33, 33);
		orientations[1] = sprites.getSubimage(35, 101, 33, 33);
		orientations[2] = sprites.getSubimage(69, 101, 33, 33);
		orientations[3] = sprites.getSubimage(103, 101, 33, 33);
		orientations[4] = sprites.getSubimage(137, 101, 33, 33);
		orientations[5] = sprites.getSubimage(171, 101, 33, 33);
		orientations[6] = sprites.getSubimage(205, 101, 33, 33);
		orientations[7] = sprites.getSubimage(239, 101, 33, 33);
		orientations[8] = sprites.getSubimage(1, 135, 33, 33);
		orientations[9] = sprites.getSubimage(35, 135, 33, 33);
		orientations[10] = sprites.getSubimage(69, 135, 33, 33);
		orientations[11] = sprites.getSubimage(103, 135, 33, 33);
		orientations[12] = sprites.getSubimage(137, 135, 33, 33);
		orientations[13] = sprites.getSubimage(171, 135, 33, 33);
		orientations[14] = sprites.getSubimage(205, 135, 33, 33);
		orientations[15] = sprites.getSubimage(239, 135, 33, 33);
	}
	
	private double normalizeAngle(double angle) {
		double normalized = angle % 360.0;

		if (normalized < 0.0) {
			normalized += 360.0;
		}

		return normalized;
	}
	
	private int getOrientation(double orientation) {
		double angle = normalizeAngle(orientation);
		if(angle >= 11.25 && angle < 33.75)
			return 1;
		if(angle >= 33.75 && angle < 56.25)
			return 2;
		if(angle >= 56.25 && angle < 78.75)
			return 3;
		if(angle >= 78.75 && angle < 101.25)
			return 4;
		if(angle >= 101.25 && angle < 123.75)
			return 5;
		if(angle >= 123.75 && angle < 146.25)
			return 6;
		if(angle >= 146.25 && angle < 168.75)
			return 7;
		if(angle >= 168.75 && angle < 191.25)
			return 8;
		if(angle >= 191.25 && angle < 213.75)
			return 9;
		if(angle >= 213.75 && angle < 236.25)
			return 10;
		if(angle >= 236.25 && angle < 258.75)
			return 11;
		if(angle >= 258.75 && angle < 281.25)
			return 12;
		if(angle >= 281.25 && angle < 303.75)
			return 13;
		if(angle >= 303.75 && angle < 326.25)
			return 14;
		if(angle >= 326.25 && angle < 348.75)
			return 15;
		return 0;
	}

	@Override
	public void updateAnimation(double delta_t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void paint(Graphics g) {
		if (sprites == null || orientations == null) {
			initImage(g);
		}
		if (entity().center() == null) {
			return;
		}
		
		if (dead() && deathAnimationFinished) {
			return;
		}
		
		current = orientations[getOrientation(entity.orientation())];
		Game game = Game.game();
		int pixelPerCm = game.pixelPerCm;
		ISU.Dimension size = entity.size();
		ISU.Coord center = entity.center();
		
		g.drawImage(current, (int) (center.x() - size.x()/2 * pixelPerCm), (int) (center.y() - size.y()/2 * pixelPerCm), (int) (size.x() * pixelPerCm), (int) (size.y() * pixelPerCm));
	}
}