package game.graphics;

import engine.entity.Entity;
import engine.graphics.Avatar;
import game.Game;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;

public class PacmanAvatar extends Avatar {

	private static final String SPRITE_PATH = "src/game/graphics/pacman_sprite.png";
	private static final double ANIMATION_DURATION_MS = 130.0;

	private BufferedImage sprite;
	private BufferedImage[] animations;

	private double time;
	private int frameIndex;

	private Direction currentDirection;
	private boolean currentDeadState;
	private boolean deathAnimationFinished;

	private enum Direction {
		RIGHT, DOWN, LEFT, UP
	}

	public PacmanAvatar(Entity entity) {
		super(entity);
	}

	@Override
	public void initImages(Graphics g) {
		sprite = g.load(SPRITE_PATH);

		currentDirection = directionFromOrientation(entity().orientation());
		currentDeadState = dead();

		if (currentDeadState) {
			setDeathAnimation();
		} else {
			setNormalAnimation(currentDirection);
		}

		time = 0.0;
		frameIndex = 0;
		deathAnimationFinished = false;
	}

	private void setNormalAnimation(Direction direction) {
		animations = new BufferedImage[3];

		switch (direction) {
		case RIGHT:
			animations[0] = sprite.getSubimage(453, 0, 15, 15);
			animations[1] = sprite.getSubimage(470, 0, 15, 15);
			animations[2] = sprite.getSubimage(487, 0, 15, 15);
			break;

		case DOWN:
			animations[0] = sprite.getSubimage(453, 47, 20, 15);
			animations[1] = sprite.getSubimage(470, 47, 20, 15);
			animations[2] = sprite.getSubimage(487, 0, 15, 15);
			break;

		case LEFT:
			animations[0] = sprite.getSubimage(453, 15, 17, 15);
			animations[1] = sprite.getSubimage(470, 15, 17, 15);
			animations[2] = sprite.getSubimage(487, 0, 15, 15);
			break;

		case UP:
			animations[0] = sprite.getSubimage(453, 35, 17, 12);
			animations[1] = sprite.getSubimage(470, 35, 17, 12);
			animations[2] = sprite.getSubimage(487, 0, 15, 15);
			break;
		}
	}

	private void setDeathAnimation() {
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
	}

	private void updateCurrentAnimationIfNeeded() {
		boolean newDeadState = dead();
		Direction newDirection = directionFromOrientation(entity().orientation());

		boolean deathStateChanged = newDeadState != currentDeadState;
		boolean directionChanged = !newDeadState && newDirection != currentDirection;

		if (!deathStateChanged && !directionChanged) {
			return;
		}

		currentDeadState = newDeadState;
		currentDirection = newDirection;

		time = 0.0;
		frameIndex = 0;
		deathAnimationFinished = false;

		if (currentDeadState) {
			setDeathAnimation();
		} else {
			setNormalAnimation(currentDirection);
		}
	}

	private Direction directionFromOrientation(double orientation) {
		double angle = normalizeAngle(orientation);

		if (angle >= 45.0 && angle < 135.0) {
			return Direction.DOWN;
		}

		if (angle >= 135.0 && angle < 225.0) {
			return Direction.LEFT;
		}

		if (angle >= 225.0 && angle < 315.0) {
			return Direction.UP;
		}

		return Direction.RIGHT;
	}

	private double normalizeAngle(double angle) {
		double normalized = angle % 360.0;

		if (normalized < 0.0) {
			normalized += 360.0;
		}

		return normalized;
	}

	@Override
	public void updateAnimation(double delta_t) {
		if (sprite == null || animations == null) {
			return;
		}

		updateCurrentAnimationIfNeeded();

		if (dead() && deathAnimationFinished) {
			return;
		}

		time += delta_t;

		while (time >= ANIMATION_DURATION_MS) {
			time -= ANIMATION_DURATION_MS;
			frameIndex++;

			if (frameIndex < animations.length) {
				continue;
			}

			if (dead()) {
				frameIndex = animations.length - 1;
				deathAnimationFinished = true;
				return;
			}

			frameIndex = 0;
		}
	}

	@Override
	public void paint(Graphics g) {
		if (sprite == null || animations == null) {
			initImages(g);
		}

		if (entity().center() == null) {
			return;
		}
		
		if (dead() && deathAnimationFinished) {
			return;
		}

		updateCurrentAnimationIfNeeded();

		BufferedImage img = animations[frameIndex];

		double cell = Game.game().cmPerCell;
		double pixelPerCm = Game.game().pixelPerCm;

		int size = Math.max(1, (int) Math.round(cell * pixelPerCm));

		int xCenter = (int) Math.round((entity().center().x() + cell / 2.0) * pixelPerCm);
		int yCenter = (int) Math.round((entity().center().y() + cell / 2.0) * pixelPerCm);

		int xTopLeft = xCenter - size / 2;
		int yTopLeft = yCenter - size / 2;

		g.drawImage(img, xTopLeft, yTopLeft, size, size);
	}
}