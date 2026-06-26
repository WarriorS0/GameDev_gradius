package game.gradius.entity;

import engine.entity.Entity;
import engine.gal.PowerReceiver;
import engine.gal.arguments.Category;
import engine.shape.Rect;
import game.Game;
import oop.utils.SoundPlayer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import engine.geometry.Grid;
import engine.geometry.ISU;

public class Ship extends Entity implements PowerReceiver {

	public enum PowerType {
		NONE, SHOOT, SPEED
	}
	
	private static final Random RANDOM = new Random();

	private final List<Cannon> cannons = new ArrayList<>();
	private PowerType currentPower = PowerType.NONE;

	private static final double POWER_DURATION_S = 5.0;
	private double powerRemainingS;

	private static final double DEATH_ANIMATION_DURATION_S = 0.6;

	private boolean deathAnimationPlaying;
	private double deathAnimationElapsedS;
	
	private static SoundPlayer GameOver;
	static {
		try {
			// Le fichier laser.wav doit se trouver à la racine de votre projet
			GameOver = new SoundPlayer(new File("src/game/gradius/entity/Game-Over.wav"));
			GameOver.volume(-5F);
		} catch (IOException e) {
			System.err.println("Impossible de charger le fichier audio du laser !");
			e.printStackTrace();
		}
	}
	
	private static SoundPlayer Music;
	static {
		try {
			// Le fichier laser.wav doit se trouver à la racine de votre projet
			Music = new SoundPlayer(new File("src/game/gradius/entity/Sand-Storm.wav"));
			Music.volume(-5F);
		} catch (IOException e) {
			System.err.println("Impossible de charger le fichier audio du laser !");
			e.printStackTrace();
		}
	}

	public Ship() {
		super("Ship");

		double cell = Game.game().cmPerCell;

		setSize(isu.new Dimension(4.0 * cell, 2.0 * cell));
		setStep(isu.new Dimension(cell, cell));
		category(Category.Team);
		
		place(grid.new Position(5, grid.height() / 2));
		Music.play(1);
	}

	@Override
	protected void setBounding() {
		clearBounding();

		addBounding(new Rect(center(), size(), orientation()));
	}

	public void attachCannon(Cannon cannon) {
		if (cannon == null) {
			throw new IllegalArgumentException("cannon cannot be null");
		}

		if (!cannons.contains(cannon)) {
			cannons.add(cannon);
			cannon.attachTo(this);
			cannon.placeRelativeTo(this);
		}
	}

	public List<Cannon> cannons() {
		return Collections.unmodifiableList(cannons);
	}

	private void syncCannons() {
		for (Cannon cannon : cannons) {
			if (!cannon.dead()) {
				cannon.placeRelativeTo(this);
			}
		}
	}

	// LIFE

	@Override
	public void kill() {
		if (Music != null) {
	        // 1. On baisse le volume à fond d'abord (pendant que Music.m_player n'est pas encore null)
	        Music.volume(-80F); 
	        
	        // 2. Ensuite, on demande l'arrêt du thread
	        Music.stop();
	    }
	    
	    // 3. On lance le son de défaite et l'animation
	    if (GameOver != null) {
	        GameOver.play(1);
	    }
	    
	    startDeathAnimation();
	}

	public boolean deathAnimationPlaying() {
		return deathAnimationPlaying;
	}

	public double deathAnimationProgress() {
		if (!deathAnimationPlaying) {
			return 0.0;
		}

		return Math.min(1.0, deathAnimationElapsedS / DEATH_ANIMATION_DURATION_S);
	}

	public void startDeathAnimation() {
		if (dead() || deathAnimationPlaying) {
			return;
		}

		clearPower();

		deathAnimationPlaying = true;
		deathAnimationElapsedS = 0.0;

		setLinearSpeed(isu.new Vector(0.0, 0.0));
		setAngularSpeed(0.0);

		retract();
		clearBounding();
		category(Category.Void);

		for (Cannon cannon : cannons) {
			if (!cannon.dead()) {
				cannon.kill();
			}
		}
	}

	public void tickDeathAnimation(double elapsed_s) {
		if (!deathAnimationPlaying) {
			return;
		}

		deathAnimationElapsedS += elapsed_s;

		if (deathAnimationElapsedS >= DEATH_ANIMATION_DURATION_S) {
			finishDeathAnimation();
		}
	}

	private void finishDeathAnimation() {
		deathAnimationPlaying = false;
		super.kill();
	}

	// MOVEMENT

	@Override
	public void place(Grid.Position position) {
		super.place(position);
		syncCannons();
	}

	@Override
	public void place(ISU.Coord center) {
		super.place(center);
		syncCannons();
	}

	@Override
	public void translate(ISU.Vector v) {
		super.translate(v);
		syncCannons();
	}

	@Override
	public void translate(Grid.Vector v) {
		super.translate(v);
		syncCannons();
	}

	@Override
	public void activatePower() {
		this.currentPower = randomPowerType();
		this.powerRemainingS = POWER_DURATION_S;
	}

	public boolean hasShootPower() {
		return currentPower == PowerType.SHOOT;
	}

	public boolean hasSpeedPower() {
		return currentPower == PowerType.SPEED;
	}

	public PowerType currentPower() {
		return currentPower;
	}

	public void tickPower(double elapsed_s) {
		if (currentPower == PowerType.NONE) {
			return;
		}

		powerRemainingS -= elapsed_s;

		if (powerRemainingS <= 0.0) {
			clearPower();
		}
	}

	private PowerType randomPowerType() {
		PowerType[] powers = PowerType.values();

		int index = 1 + RANDOM.nextInt(powers.length - 1);

		return powers[index];
	}

	private void clearPower() {
		currentPower = PowerType.NONE;
		powerRemainingS = 0.0;
	}

	@Override
	public boolean hasPower() {
		return currentPower != PowerType.NONE;
	}

}