package game.gradius.entity;

import engine.entity.Entity;
import engine.gal.PowerReceiver;
import engine.gal.arguments.Category;
import engine.shape.Rect;
import game.Game;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import engine.geometry.Grid;
import engine.geometry.ISU;

public class Ship extends Entity implements PowerReceiver {

	private final List<Cannon> cannons = new ArrayList<>();
	private boolean powered;
	
	private static final double DEATH_ANIMATION_DURATION_S = 0.6;
	
	private boolean deathAnimationPlaying;
	private double deathAnimationElapsedS;

	public Ship() {
		super("Ship");

		double cell = Game.game().cmPerCell;

		setSize(isu.new Dimension(4.0 * cell, 2.0 * cell));
		setStep(isu.new Dimension(cell, cell));
		category(Category.Team);

		place(grid.new Position(5, grid.height() / 2));
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
		this.powered = true;
	}

	@Override
	public boolean hasPower() {
		return powered;
	}
	
	
}