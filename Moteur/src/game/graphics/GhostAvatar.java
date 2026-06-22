package game.graphics;

import engine.entity.Entity;
import engine.graphics.Avatar;
import game.Game;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;

public abstract class GhostAvatar extends Avatar {

	private static final String SPRITE_PATH = "src/game/graphics/pacman_sprite.png";
	private static final double ANIMATION_DURATION_MS = 0.1;

	public boolean weak = false;
	public boolean weak_over = false;
	public boolean stop = false;

	private BufferedImage sprite;
	private BufferedImage[] animations;

	private double time;
	private int frameIndex;

	private Direction currentDirection;
	private State currentState;

	private enum Direction {
		RIGHT, DOWN, LEFT, UP
	}

	private enum State {
		NORMAL, WEAK, WEAK_OVER, DEAD
	}

	protected GhostAvatar(Entity entity) {
		super(entity);
	}

	protected abstract int normalY();

	protected int normalDownY() {
		return normalY();
	}

	protected int normalHeight() {
		return 16;
	}

	@Override
	public void initImages(Graphics g) {
		this.sprite = g.load(SPRITE_PATH);
		this.currentDirection = directionFromOrientation(entity().orientation());
		this.currentState = state();

		this.animations = buildAnimation(currentState, currentDirection);
		this.time = 0.0;
		this.frameIndex = 0;
	}

	@Override
	public void updateAnimation(double delta_t) {
		if (sprite == null || animations == null || stop) {
			return;
		}

		updateAnimationIfNeeded();

		if (animations.length <= 1) {
			frameIndex = 0;
			return;
		}

		time += delta_t;

		while (time >= ANIMATION_DURATION_MS) {
			time -= ANIMATION_DURATION_MS;
			frameIndex++;

			if (frameIndex >= animations.length) {
				frameIndex = 0;
			}
		}
	}

	private void updateAnimationIfNeeded() {
		Direction newDirection = directionFromOrientation(entity().orientation());
		State newState = state();

		if (newDirection == currentDirection && newState == currentState) {
			return;
		}

		currentDirection = newDirection;
		currentState = newState;

		animations = buildAnimation(currentState, currentDirection);
		frameIndex = 0;
		time = 0.0;
	}

	private State state() {
		if (dead()) {
			return State.DEAD;
		}

		if (weak_over) {
			return State.WEAK_OVER;
		}

		if (weak) {
			return State.WEAK;
		}

		return State.NORMAL;
	}

	private BufferedImage[] buildAnimation(State state, Direction direction) {
		switch (state) {
		case WEAK:
			return weakAnimation();

		case WEAK_OVER:
			return weakOverAnimation();

		case DEAD:
			return deadAnimation(direction);

		case NORMAL:
		default:
			return normalAnimation(direction);
		}
	}

	private BufferedImage[] normalAnimation(Direction direction) {
		BufferedImage[] frames = new BufferedImage[2];

		switch (direction) {
		case RIGHT:
			frames[0] = sprite.getSubimage(455, normalY(), 16, normalHeight());
			frames[1] = sprite.getSubimage(471, normalY(), 16, normalHeight());
			break;

		case LEFT:
			frames[0] = sprite.getSubimage(487, normalY(), 16, normalHeight());
			frames[1] = sprite.getSubimage(503, normalY(), 16, normalHeight());
			break;

		case UP:
			frames[0] = sprite.getSubimage(519, normalY(), 16, normalHeight());
			frames[1] = sprite.getSubimage(536, normalY(), 16, normalHeight());
			break;

		case DOWN:
			frames[0] = sprite.getSubimage(551, normalDownY(), 16, normalHeight());
			frames[1] = sprite.getSubimage(567, normalDownY(), 16, normalHeight());
			break;
		}

		return frames;
	}

	private BufferedImage[] weakAnimation() {
		BufferedImage[] frames = new BufferedImage[2];

		frames[0] = sprite.getSubimage(583, 64, 16, 18);
		frames[1] = sprite.getSubimage(600, 64, 16, 18);

		return frames;
	}

	private BufferedImage[] weakOverAnimation() {
		BufferedImage[] frames = new BufferedImage[4];

		frames[0] = sprite.getSubimage(583, 64, 16, 18);
		frames[1] = sprite.getSubimage(616, 64, 16, 18);
		frames[2] = sprite.getSubimage(600, 64, 16, 18);
		frames[3] = sprite.getSubimage(631, 64, 16, 18);

		return frames;
	}

	private BufferedImage[] deadAnimation(Direction direction) {
		BufferedImage[] frames = new BufferedImage[1];

		switch (direction) {
		case RIGHT:
			frames[0] = sprite.getSubimage(584, 82, 16, 18);
			break;

		case LEFT:
			frames[0] = sprite.getSubimage(600, 82, 16, 18);
			break;

		case UP:
			frames[0] = sprite.getSubimage(615, 80, 16, 18);
			break;

		case DOWN:
			frames[0] = sprite.getSubimage(632, 82, 16, 20);
			break;
		}

		return frames;
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
	public void paint(Graphics g) {
		if (sprite == null || animations == null) {
			initImages(g);
		}

		if (stop || entity().center() == null) {
			return;
		}

		updateAnimationIfNeeded();

		BufferedImage img = animations[frameIndex];

		Game game = Game.game();

		double cell = game.cmPerCell;
		double pixelPerCm = game.pixelPerCm;

		int size = Math.max(1, (int) Math.round(cell * pixelPerCm));

		int xCenter = (int) Math.round((entity().center().x() + cell / 2.0) * pixelPerCm);
		int yCenter = (int) Math.round((entity().center().y() + cell / 2.0) * pixelPerCm);

		int xTopLeft = xCenter - size / 2;
		int yTopLeft = yCenter - size / 2;

		g.drawImage(img, xTopLeft, yTopLeft, size, size);
	}
}