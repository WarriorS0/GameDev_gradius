// == ENTITY ==

package engine;

import java.io.PrintStream;

class Entity {

	// FIELDS

	private Grid grid;
	private ISU isu;
	private String name;

	// FIELDS

	private ISU.Dimension size; // dimension de l'entité
	private ISU.Dimension step; // dimension d'un pas de déplacement
	private Grid.Position position; // position dans la grille
	private ISU.Coord center; // coordonnées en cm du centre de l'entité

	// FIELDS

	private int orientation_degree; // orientation par rapport à l'axe des x

	// CONSTRUCTOR

	Entity(String name) {
		this.name = name;

	}

	// SETTER

	void setPosition(Grid.Position position) {
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

	void setSize(ISU.Dimension dimension) {
		this.size = dimension;
	}

	// GETTER

	ISU.Coord center() {
		return this.center;
	}

	Grid.Position position() {
		return position;
	}

	int orientation() {
		return this.orientation_degree;
	}

	// TRANSLATION

	void translate(Grid.Vector v) {
		position.translate(v);
		this.setPosition(position);
	}

	void translate(ISU.Vector v) {
		center.translate(v);
		this.setCoord(center);
	}

	// TURN

	/**
	 * @apiNote turn is a rotation around the center of the entity.
	 * @param angle_degree
	 */
	void turn(int angle_degree) {
		this.orientation_degree = (((this.orientation_degree % 360) + 360) % 360);
	}

	// SHOW

	void show(PrintStream ps) {
		ps.printf("Entity = %s in (%d,%d) cell\n", this.name, this.position.x_ncell, this.position.y_ncell);
	}

	// === MOVE ===

	/**
	 * @apiNote déplacement vers le nord en nombre de pas
	 * @param nStep
	 */
	void moveNorth(int nStep) {
		Grid.Vector = ()
		this.translate(grid.new Vector(0, -nStep));
	}

	void moveSouth(int nStep) {
		this.translate(grid.new Vector(0, nStep));
	}

	void moveEast(int nStep) {
		this.translate(grid.new Vector(nStep, 0));
	}

	void moveWest(int nStep) {
		this.translate(grid.new Vector(-nStep, 0));
	}

	/**
	 * @apiNote déplacement vers l'est en cm
	 * @param length_cm
	 */
	void moveEast(double length_cm) {
		this.translate(isu.new Vector(length_cm, 0));
	}

	void moveWest(double length_cm) {
		this.translate(isu.new Vector(-length_cm, 0));
	}

	void moveNorth(double length_cm) {
		this.translate(isu.new Vector(0, -length_cm));
	}

	void moveSouth(double length_cm) {
		this.translate(isu.new Vector(0, length_cm));
	}

}
