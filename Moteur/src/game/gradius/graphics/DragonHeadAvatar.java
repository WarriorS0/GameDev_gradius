package game.gradius.graphics;

import engine.entity.Entity;
import engine.geometry.ISU;
import engine.graphics.avatars.AnimationAvatar;
import engine.graphics.avatars.ShapeAvatar;
import game.Game;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;

// DragonHeadAvatar devrait avoir sa propre classe qui implémente RessourceAvatar
public class DragonHeadAvatar extends ShapeAvatar {
	
	private static final String SPRITE_PATH = "src/game/gradius/graphics/vulture_dragon.png";
	private static final int   DEATH_FRAMES        = 5;
    private static final double DEATH_FRAME_DURATION_S = 0.13;
	private BufferedImage sprites;
	private BufferedImage[] orientations;
	private BufferedImage[] death_animation;
	private boolean deathStarted          = false;
    private boolean deathAnimationFinished = false;
    private double  deathTimer            = 0.0;
    private int     deathFrameIndex       = 0;
	private BufferedImage current;
	
	public DragonHeadAvatar(Entity entity) {
		super(entity, 1, 1);
		Game.game().view.add(this);
	}
	
	@Override
	public void initImage(Graphics g) {
		sprites = g.load(SPRITE_PATH);
		orientations = new BufferedImage[16];
		orientations[0] = sprites.getSubimage(1, 1, 49, 49);
		orientations[1] = sprites.getSubimage(51, 1, 49, 49);
		orientations[2] = sprites.getSubimage(101, 1, 49, 49);
		orientations[3] = sprites.getSubimage(151, 1, 49, 49);
		orientations[4] = sprites.getSubimage(201, 1, 49, 49);
		orientations[5] = sprites.getSubimage(251, 1, 49, 49);
		orientations[6] = sprites.getSubimage(301, 1, 49, 49);
		orientations[7] = sprites.getSubimage(351, 1, 49, 49);
		orientations[8] = sprites.getSubimage(1, 51, 49, 49);
		orientations[9] = sprites.getSubimage(51, 51, 49, 49);
		orientations[10] = sprites.getSubimage(101, 51, 49, 49);
		orientations[11] = sprites.getSubimage(151, 51, 49, 49);
		orientations[12] = sprites.getSubimage(201, 51, 49, 49);
		orientations[13] = sprites.getSubimage(251, 51, 49, 49);
		orientations[14] = sprites.getSubimage(301, 51, 49, 49);
		orientations[15] = sprites.getSubimage(351, 51, 49, 49);
		
		death_animation = new BufferedImage[5];
		death_animation[0] = sprites.getSubimage(99, 169, 48, 48);
		death_animation[1] = sprites.getSubimage(148, 169, 48, 48);
		death_animation[2] = sprites.getSubimage(197, 169, 48, 48);
		death_animation[3] = sprites.getSubimage(246, 169, 48, 48);	
		death_animation[4] = sprites.getSubimage(295, 169, 48, 48);
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
		if (!dead() || deathAnimationFinished) return;
		 
        if (!deathStarted) deathStarted = true;
 
        deathTimer += delta_t;
        while (deathTimer >= DEATH_FRAME_DURATION_S) {
            deathTimer -= DEATH_FRAME_DURATION_S;
            deathFrameIndex++;
            if (deathFrameIndex >= DEATH_FRAMES) {
                deathFrameIndex       = DEATH_FRAMES - 1;
                deathAnimationFinished = true;
                return;
            }
        }
	}

	@Override
	public void paint(Graphics g) {
		if (sprites == null || orientations == null) {
			initImage(g);
		}
		if (entity().center() == null) {
			return;
		}
		
		if (deathAnimationFinished) {
			return;
		}
		
		if(AnimationAvatar.debugCollision)
			super.paint(g);
		
		if (dead()) {
			current = death_animation[deathFrameIndex];
		} else {
			current = orientations[getOrientation(entity.orientation())];
		}
		
		ISU.Coord coord = entity().center();
		ISU.Dimension size = entity().size();

		int width = Math.max(1, this.cmToPixel(size.x()));
		int height = Math.max(1, this.cmToPixel(size.y()));

		int xCenter = this.cmToPixel(coord.x());
		int yCenter = this.cmToPixel(coord.y());

		int xTopLeft = xCenter - width / 2;
		int yTopLeft = yCenter - height / 2;

		g.drawImage(current, xTopLeft, yTopLeft, width, height);
		}
}
