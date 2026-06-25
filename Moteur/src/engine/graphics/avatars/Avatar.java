package engine.graphics.avatars;

import java.util.logging.Level;
import java.util.logging.Logger;

import engine.entity.Entity;
import engine.logs.LoggerManager;
import game.Game;
import oop.graphics.Graphics;

public abstract class Avatar {

	protected static final boolean LOGGING;
	protected static final boolean INFO;
	protected static Logger logger;
	protected static final double pixelPerCm;
	static {
		logger = LoggerManager.getLogger(Avatar.class.getName());
		LOGGING = logger.getLevel() != Level.OFF;
		INFO = logger.isLoggable(Level.INFO);
		pixelPerCm = Game.game().pixelPerCm;
	}

	protected boolean hasBeenInitialized;
	protected Entity entity;

	int multX, multY;

	protected Avatar(Entity entity) {
		this.entity = entity;
		this.hasBeenInitialized=false;
		if (LOGGING && INFO) {
			logger.log(Level.INFO, "Created new Avatar");
		}
	}

	public Entity entity() {
		return entity;
	}

	public boolean dead() {
		return entity != null && entity.dead();
	}

	public void kill() {
		if (entity != null) {
			entity.kill();
		}
	}

	public void revive() {
		if (entity != null) {
			entity.revive();
		}
	}

	public abstract void paint(Graphics g);

	protected int cmToPixel(double value) {
		return (int) Math.round(value * pixelPerCm);
	}

	public abstract void updateAnimation(double delta_t);

	public abstract void initImage(Graphics g);

	public boolean hasBeenInitialized() {
		return this.hasBeenInitialized;
	}
}