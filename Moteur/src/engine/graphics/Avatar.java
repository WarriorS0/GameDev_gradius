package engine.graphics;

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
	static {
		logger = LoggerManager.getLogger(Avatar.class.getName());
		LOGGING = logger.getLevel() != Level.OFF;
		INFO = logger.isLoggable(Level.INFO);
	}

	protected static final double pixelPerCm;
	static {
		pixelPerCm = Game.game().pixelPerCm;
	}

	protected Entity entity;
	
	int multX, multY;

	protected Avatar(Entity entity) {
		this.entity = entity;
		if(LOGGING && INFO){
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

	public abstract void paint(Graphics g) ;

	protected int cmToPixel(double value) {
		return (int) Math.round(value * pixelPerCm);
	}

	protected abstract void updateAnimation(double delta_t);
	protected abstract void initImage(Graphics g);
}