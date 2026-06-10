// == ENTITY ==

package engine.entity;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import engine.geometry.Grid;
import engine.geometry.Grid.Cell;
import engine.shape.Bounding;
import engine.shape.iShape;
import engine.geometry.ISU;
import game.Game;

public class Entity {

	// FIELDS

	protected Grid grid;
	protected ISU isu;
	protected String name;

	// FIELDS

	private ISU.Dimension size; // dimension de l'entité
	private ISU.Dimension step; // dimension d'un pas de déplacement
	private Grid.Position position; // position dans la grille
	private ISU.Coord center; // coordonnées en cm du centre de l'entité

	// FIELDS

	private int orientation_degree; // orientation par rapport à l'axe des x

	private Bounding hitbox;

	// CONSTRUCTOR

	protected Entity(String name) {
		Game game = Game.game();
		this.name = name;
		this.grid = game.grid;
		this.isu = game.isu;
		this.hitbox = new Bounding();
		this.occupied = new HashSet<Cell>();

	}

	// SETTER

	protected void setPosition(Grid.Position position) {
		this.position = position;
		center = position.toISUCoord();
	}

	void setCoord(ISU.Coord center) {
		this.center = center;
		position = center.toGridPosition();
	}

	void setSize(Grid.Dimension dimension) {
		this.size = dimension.toISUDimension();
	}

	protected void setSize(ISU.Dimension dimension) {
		this.size = dimension;
	}

	void setStep(ISU.Dimension dimension) {
		this.step = dimension;
	}

	// GETTER

	protected ISU.Coord center() {
		return this.center;
	}

	Grid.Position position() {
		return position;
	}
	
	protected ISU.Dimension size() {
		return size;
	}

	protected int orientation() {
		return this.orientation_degree;
	}

	// TRANSLATION

	void translate(Grid.Vector v) {
		if (position == null) {
			throw new IllegalStateException("Entity position is not set");
		}
		position.translate(v);
		this.setPosition(position);
		this.setBounding();
	}

	void translate(ISU.Vector v) {
		if (center == null) {
			throw new IllegalStateException("Entity center is not set");
		}
		center.translate(v);
		this.setCoord(center);
		this.setBounding();
	}

	// TURN

	/**
	 * @apiNote turn is a rotation around the center of the entity.
	 * @param angle_degree
	 */
	public void turn(int angle_degree) {
		this.orientation_degree = ((((this.orientation_degree + angle_degree) % 360) + 360) % 360);
		this.setBounding();
	}

	// SHOW

	void show(PrintStream ps) {
		ps.printf("Entity = %s in (%d,%d) cell\n", this.name, this.position.x(), this.position.y());
	}

	// === MOVE ===

	/**
	 * @apiNote déplacement vers le nord en nombre de pas
	 * @param nStep
	 */
	void moveNorth(int nStep) {
		this.moveNorth(nStep * step.y());
	}

	void moveSouth(int nStep) {
		this.moveSouth(nStep * step.y());
	}

	void moveEast(int nStep) {
		this.moveEast(nStep * step.x());
	}

	void moveWest(int nStep) {
		this.moveWest(nStep * step.x());
	}

	/**
	 * @apiNote déplacement vers l'est en cm
	 * @param length_cm
	 */
	void moveEast(double length_cm) {
		if (step == null) {
			throw new IllegalStateException("Entity step is not set");
		}
		this.translate(isu.new Vector(length_cm, 0));
	}

	void moveWest(double length_cm) {
		if (step == null) {
			throw new IllegalStateException("Entity step is not set");
		}
		this.translate(isu.new Vector(-length_cm, 0));
	}

	void moveNorth(double length_cm) {
		if (step == null) {
			throw new IllegalStateException("Entity step is not set");
		}
		this.translate(isu.new Vector(0, -length_cm));
	}

	void moveSouth(double length_cm) {
		if (step == null) {
			throw new IllegalStateException("Entity step is not set");
		}
		this.translate(isu.new Vector(0, length_cm));
	}
	
	// === COLLISION ===

	// INTERSECTION

	public boolean intersects(Entity e) {
		return this.hitbox.intersects(e.hitbox);
	}


	protected void setBounding() {
		this.hitbox = new Bounding();
	}
	
	protected void addBounding(iShape shape) {
		this.hitbox.add(shape);
	}


	double distanceCenterToCenter(Entity e) {
		return e.center().distanceTo(this.center());
	}

	// DEPLOY in the Grid according to the BOUNDING
	
	Set<Cell> occupied;

	void deploy() {
		this.retract();

		for (iShape.Box box : this.hitbox.boundingBoxes()) {
			int minX = toCellIndex(box.minX());
			int maxX = toCellIndex(box.maxX());
			int minY = toCellIndex(box.minY());
			int maxY = toCellIndex(box.maxY());

			for (int x = minX; x <= maxX; x++) {
				for (int y = minY; y <= maxY; y++) {
					this.occupy(grid.new Position(x, y));
				}
			}
		}
	}

	private int toCellIndex(double coord_cm) {
		return (int) Math.floor(coord_cm / Game.game().cmPerCell);
	}

	void occupy(Grid.Position position) {
		Cell cell = this.grid.occupy(this, position);
		this.occupied.add(cell);
	}

	void retract() {
		for (Cell cell : this.occupied) {
			this.grid.retract(this, cell);
		}

		this.occupied.clear();
	}
	
	public void place(Grid.Position position) {
		this.setPosition(position);
		this.setBounding();
	}

}
