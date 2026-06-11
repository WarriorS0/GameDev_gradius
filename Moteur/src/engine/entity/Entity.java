// == ENTITY ==

package engine.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import engine.geometry.Grid;
import engine.geometry.Grid.Cell;
import engine.geometry.ISU;
import engine.shape.Bounding;
import engine.shape.iShape;
import game.Game;

public abstract class Entity {

	// =========================
	// Core fields
	// =========================

	protected final Grid grid;
	protected final ISU isu;
	protected final String name;

	private ISU.Dimension size; // dimension de l'entité en cm
	private ISU.Dimension step; // dimension d'un pas de déplacement en cm
	private Grid.Position position; // position dans la grille
	private ISU.Coord center; // coordonnées en cm du centre de l'entité

	private double orientation_degree;

	private Bounding hitbox;
	private final Set<Cell> occupied;

	private static final double EPSILON = 1e-9;

	// =========================
	// Constructor
	// =========================

	protected Entity(String name) {
		Game game = Game.game();

		if (game == null) {
			throw new IllegalStateException("No current Game instance");
		}

		this.name = Objects.requireNonNull(name, "name cannot be null");// masterclass
		this.grid = game.grid;
		this.isu = game.isu;

		this.hitbox = new Bounding();
		this.occupied = new HashSet<>();
	}

	// =========================
	// Setters / configuration
	// =========================

	public void setSize(Grid.Dimension dimension) {
		Objects.requireNonNull(dimension, "dimension cannot be null");
		this.size = dimension.toISUDimension();
	}

	public void setSize(ISU.Dimension dimension) {
		this.size = Objects.requireNonNull(dimension, "dimension cannot be null");
	}

	public void setStep(ISU.Dimension step) {
		this.step = Objects.requireNonNull(step, "step cannot be null");
	}

	public void place(Grid.Position position) {
		setPosition(position);
		setBounding();
		deploy();
	}

	public void place(ISU.Coord center) {
		setCoord(center);
		setBounding();
		deploy();
	}

	protected void setPosition(Grid.Position position) {
		this.position = Objects.requireNonNull(position, "position cannot be null");
		this.center = position.toISUCoord();
	}

	protected void setCoord(ISU.Coord center) {
		this.center = Objects.requireNonNull(center, "center cannot be null");
		this.position = center.toGridPosition();
	}

	// =========================
	// Getters
	// =========================

	public String name() {
		return name;
	}

	public ISU.Coord center() {
		return center;
	}

	public Grid.Position position() {
		return position;
	}

	public ISU.Dimension size() {
		return size;
	}

	public ISU.Dimension step() {
		return step;
	}

	public double orientation() {
		return orientation_degree;
	}

	public Set<Cell> occupied() {
		return occupied;
	}

	protected Bounding hitbox() {
		return hitbox;
	}

	// =========================
	// Translation
	// =========================

	public void translate(Grid.Vector v) {
		Objects.requireNonNull(v, "vector cannot be null");
		ensurePositionIsSet();

		position.translate(v);
		setPosition(position);

		setBounding();
		deploy();
	}

	public void translate(ISU.Vector v) {
		Objects.requireNonNull(v, "vector cannot be null");
		ensureCenterIsSet();

		center.translate(v);
		setCoord(center);

		setBounding();
		deploy();
	}

	// =========================
	// Rotation
	// =========================

	/**
	 * Rotation autour du centre de l'entité.
	 *
	 * @param angle_degree angle ajouté à l'orientation actuelle
	 */
	public void turn(double angle_degree) {
		this.orientation_degree = normalizeAngle(this.orientation_degree + angle_degree);

		setBounding();
		deploy();
	}

	private double normalizeAngle(double angle_degree) {
		double angle = angle_degree % 360.0;

		if (angle < 0) {
			angle += 360.0;
		}

		return angle;
	}

	// =========================
	// Move
	// =========================

	public void moveNorth(int nStep) {
		ensureStepIsSet();
		moveNorth(nStep * step.y());
	}

	public void moveSouth(int nStep) {
		ensureStepIsSet();
		moveSouth(nStep * step.y());
	}

	public void moveEast(int nStep) {
		ensureStepIsSet();
		moveEast(nStep * step.x());
	}

	public void moveWest(int nStep) {
		ensureStepIsSet();
		moveWest(nStep * step.x());
	}

	/**
	 * Déplacement vers l'est en cm.
	 */
	public void moveEast(double length_cm) {
		translate(isu.new Vector(length_cm, 0));
	}

	public void moveWest(double length_cm) {
		translate(isu.new Vector(-length_cm, 0));
	}

	public void moveNorth(double length_cm) {
		translate(isu.new Vector(0, -length_cm));
	}

	public void moveSouth(double length_cm) {
		translate(isu.new Vector(0, length_cm));
	}

	// =========================
	// Bounding / collision
	// =========================

	public boolean intersects(Entity e) {
		Objects.requireNonNull(e, "entity cannot be null");
		return this.hitbox.intersects(e.hitbox);
	}

	public double distanceCenterToCenter(Entity e) {
		Objects.requireNonNull(e, "entity cannot be null");
		ensureCenterIsSet();
		e.ensureCenterIsSet();

		return this.center.distanceTo(e.center);
	}

	/**
	 * Les sous-classes doivent définir leur propre hitbox ici.
	 */
	protected abstract void setBounding();

	protected void clearBounding() {
		this.hitbox = new Bounding();
	}

	protected void addBounding(iShape shape) {
		Objects.requireNonNull(shape, "shape cannot be null");
		this.hitbox.add(shape);
	}

	// =========================
	// Deploy in grid
	// =========================

	public void deploy() {
		retract();

		for (iShape.Box box : this.hitbox.boundingBoxes()) {
			int minX = toCellIndexMin(box.minX());
			int maxX = toCellIndexMax(box.maxX());
			int minY = toCellIndexMin(box.minY());
			int maxY = toCellIndexMax(box.maxY());

			for (int x = minX; x <= maxX; x++) {
				for (int y = minY; y <= maxY; y++) {
					occupy(grid.new Position(x, y));
				}
			}
		}
	}

	private int toCellIndexMin(double coord_cm) {
		double cmPerCell = Game.game().cmPerCell;
		return (int) Math.floor(coord_cm / cmPerCell + 0.5 + EPSILON);
	}

	private int toCellIndexMax(double coord_cm) {
		double cmPerCell = Game.game().cmPerCell;
		return (int) Math.floor(coord_cm / cmPerCell + 0.5 - EPSILON);
	}

	private void occupy(Grid.Position position) {
		Cell cell = this.grid.occupy(this, position);
		this.occupied.add(cell);
	}

	public void retract() {
		for (Cell cell : this.occupied) {
			this.grid.retract(this, cell);
		}

		this.occupied.clear();
	}

	// =========================
	// Guards
	// =========================

	private void ensurePositionIsSet() {
		if (position == null) {
			throw new IllegalStateException("Entity position is not set");
		}
	}

	private void ensureCenterIsSet() {
		if (center == null) {
			throw new IllegalStateException("Entity center is not set");
		}
	}

	private void ensureStepIsSet() {
		if (step == null) {
			throw new IllegalStateException("Entity step is not set");
		}
	}
}