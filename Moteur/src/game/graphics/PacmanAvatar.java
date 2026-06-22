package game.graphics;

import java.util.ArrayList;

import engine.entity.Entity;
import engine.graphics.Avatar;
import game.Game;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;

public class PacmanAvatar extends Avatar {

	private static final String SPRITE_PATH = "src/game/graphics/pacman_sprite.png";
	private static final double ANIMATION_DURATION_MS = 0.13;

	private BufferedImage sprite;
	private BufferedImage[] animations;

	private double time;
	private int frameIndex;

	private Direction currentDirection;
	private boolean currentDeadState;
	private boolean deathAnimationFinished;
	
	private ArrayList<BufferedImage> listAnimations;
	private BufferedImage[] animeLeft;
	private BufferedImage[] animeRight;
	private BufferedImage[] animeUp;
	private BufferedImage[] animeDown;

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

		initListAnimations();
		if (currentDeadState) {
			setDeathAnimation();
		} else {
			setNormalAnimation(currentDirection);
		}

		time = 0.0;
		frameIndex = 0;
		deathAnimationFinished = false;
	}
	
	private void initListAnimations() {
		this.listAnimations = new ArrayList<BufferedImage>();
		listAnimations.add(sprite.getSubimage(453, 0, 15, 15));
		listAnimations.add(sprite.getSubimage(470, 0, 15, 15));
		listAnimations.add(sprite.getSubimage(487, 0, 15, 15));
		
		listAnimations.add(sprite.getSubimage(453, 47, 20, 15));
		listAnimations.add(sprite.getSubimage(470, 47, 20, 15));
		listAnimations.add(sprite.getSubimage(487, 0, 15, 15));
		
		listAnimations.add(sprite.getSubimage(453, 15, 17, 15));
		listAnimations.add(sprite.getSubimage(470, 15, 17, 15));
		listAnimations.add(sprite.getSubimage(487, 0, 15, 15));
		
		listAnimations.add(sprite.getSubimage(453, 35, 17, 12));
		listAnimations.add(sprite.getSubimage(470, 35, 17, 12));
		listAnimations.add(sprite.getSubimage(487, 0, 15, 15));
		
		listAnimations.add(sprite.getSubimage(503, 0, 15, 15));
		listAnimations.add(sprite.getSubimage(519, 0, 15, 15));
		listAnimations.add(sprite.getSubimage(535, 0, 15, 15));
		listAnimations.add(sprite.getSubimage(551, 0, 15, 15));
		listAnimations.add(sprite.getSubimage(567, 0, 15, 15));
		listAnimations.add(sprite.getSubimage(583, 0, 15, 15));
		listAnimations.add(sprite.getSubimage(599, 0, 15, 15));
		listAnimations.add(sprite.getSubimage(617, 0, 15, 15));
		listAnimations.add(sprite.getSubimage(634, 0, 15, 15));
		listAnimations.add(sprite.getSubimage(648, 0, 15, 15));
		listAnimations.add(sprite.getSubimage(661, 0, 15, 15));
		
	}

	private void setNormalAnimation(Direction direction) {
		animations = new BufferedImage[3];

		switch (direction) {
		case RIGHT:
			animations[0] = listAnimations.get(0);
			animations[1] = listAnimations.get(1);
			animations[2] = listAnimations.get(2);
			break;

		case DOWN:
			animations[0] = listAnimations.get(3);
			animations[1] = listAnimations.get(4);
			animations[2] = listAnimations.get(5);
			break;

		case LEFT:
			animations[0] = listAnimations.get(6);
			animations[1] = listAnimations.get(7);
			animations[2] = listAnimations.get(8);
			break;

		case UP:
			animations[0] = listAnimations.get(9);
			animations[1] = listAnimations.get(10);
			animations[2] = listAnimations.get(11);
			break;
		}
	}

	private void setDeathAnimation() {
		animations = new BufferedImage[11];

		animations[0] = listAnimations.get(0);
		animations[1] = listAnimations.get(0);
		animations[2] = listAnimations.get(0);
		animations[3] = listAnimations.get(0);
		animations[4] = listAnimations.get(0);
		animations[5] = listAnimations.get(0);
		animations[6] = listAnimations.get(0);
		animations[7] = listAnimations.get(0);
		animations[8] = listAnimations.get(0);
		animations[9] = listAnimations.get(0);
		animations[10] = listAnimations.get(0);
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