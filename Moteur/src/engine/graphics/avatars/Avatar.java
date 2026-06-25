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
	public final static int MIN_ZORDER; // compris
	public final static int MAX_ZORDER;// compris, donc de 0 à 9
	static {
		logger = LoggerManager.getLogger(Avatar.class.getName());
		LOGGING = logger.getLevel() != Level.OFF;
		INFO = logger.isLoggable(Level.INFO);
		pixelPerCm = Game.game().pixelPerCm;
		MIN_ZORDER = 0;
		MAX_ZORDER = 9;
	}

	protected Entity entity;
	private int z_order;

	protected boolean hasBeenInitialized;

	int multX, multY;

	public int z_order() {
		return z_order;
	}

	protected void set_z_order(int z_order) {
		if (z_order > MAX_ZORDER)
			throw new IllegalArgumentException("The maximum Z_order is " + MAX_ZORDER);
		if (z_order < MIN_ZORDER)
			throw new IllegalArgumentException("The minimum Z_order is " + MIN_ZORDER);
		this.z_order = z_order;
	}

	protected Avatar(Entity entity) {
		this.entity = entity;
		this.hasBeenInitialized = false;
		if (LOGGING && INFO) {
			logger.log(Level.INFO, "Created new Avatar");
		}
		z_order = 0;
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
