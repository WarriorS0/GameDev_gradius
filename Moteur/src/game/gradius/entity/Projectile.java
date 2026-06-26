package game.gradius.entity;

import java.io.File;
import java.io.IOException;

import engine.entity.Entity;
import engine.gal.arguments.Category;
import engine.geometry.ISU;
import engine.shape.Circle;
import engine.shape.Rect;
import game.Game;
import oop.utils.SoundPlayer;

public class Projectile extends Entity {

	public enum Type {
		LASER, BLUE_ORB
	}

	private static final double ORB_MIN_DIAMETER_IN_CELL = 0.7;
	private static final double ORB_MAX_DIAMETER_IN_CELL = 2.2;
	private static final double ORB_GROWTH_RATIO = 0.08;
	
	
	private static SoundPlayer laserPlayer;
	static {
		try {
			// Le fichier laser.wav doit se trouver à la racine de votre projet
			laserPlayer = new SoundPlayer(new File("src/game/gradius/entity/laser.wav"));
			laserPlayer.volume(-10F);
		} catch (IOException e) {
			System.err.println("Impossible de charger le fichier audio du laser !");
			e.printStackTrace();
		}
	}
	
	
	private static SoundPlayer BlueOrbPlayer;
	static {
		try {
			// Le fichier laser.wav doit se trouver à la racine de votre projet
			BlueOrbPlayer = new SoundPlayer(new File("src/game/gradius/entity/blue_orb.wav"));
			BlueOrbPlayer.volume(-10F);
		} catch (IOException e) {
			System.err.println("Impossible de charger le fichier audio du laser !");
			e.printStackTrace();
		}
	}
	
	private final Type type;
	private final ISU.Coord spawnCenter;

	public Projectile(ISU.Coord center, ISU.Vector speed) {
		this(center, speed, Type.LASER);
	}

	public Projectile(ISU.Coord center, ISU.Vector speed, Type type) {
		super("Projectile");

		if (type == null) {
			throw new IllegalArgumentException("type cannot be null");
		}

		double cell = Game.game().cmPerCell;

		this.type = type;
		this.spawnCenter = center.mkCopy();

		if (type == Type.BLUE_ORB) {
			double diameter = ORB_MIN_DIAMETER_IN_CELL * cell;
			setSize(isu.new Dimension(diameter, diameter));
		} else {
			setSize(isu.new Dimension(cell, 0.6 * cell));
		}

		setStep(isu.new Dimension(cell, cell));
		category(Category.Projectile);

		place(center);
		setLinearSpeed(speed);
		
		if (type == Type.LASER && laserPlayer != null) {
			laserPlayer.play(1); // Déclenche le son en arrière-plan immédiatement
		}
		if (type == Type.BLUE_ORB && BlueOrbPlayer != null) {
			BlueOrbPlayer.play(1); // Déclenche le son en arrière-plan immédiatement
		}
	}

	public Type type() {
		return type;
	}

	public boolean isBlueOrb() {
		return type == Type.BLUE_ORB;
	}

	public void updatePowerAnimation() {
		if (!isBlueOrb() || center() == null) {
			return;
		}

		double cell = Game.game().cmPerCell;
		double minDiameter = ORB_MIN_DIAMETER_IN_CELL * cell;
		double maxDiameter = ORB_MAX_DIAMETER_IN_CELL * cell;

		double distance = spawnCenter.distanceTo(center());
		double diameter = Math.min(maxDiameter, minDiameter + distance * ORB_GROWTH_RATIO);

		setSize(isu.new Dimension(diameter, diameter));
		setBounding();
		deploy();
	}

	@Override
	protected void setBounding() {
		clearBounding();

		if (isBlueOrb()) {
			addBounding(new Circle(center(), size().x() / 2.0));
		} else {
			addBounding(new Rect(center(), size(), orientation()));
		}
	}
}