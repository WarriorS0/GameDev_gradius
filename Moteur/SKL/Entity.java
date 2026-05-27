// == ENTITY ==

 class Entity {

	// FIELDS

	 Grid grid;
	 ISU isu;
	 String name;

	// FIELDS

	 ISU.Dimension size; // dimension de l'entité
	 ISU.Dimension step; // dimension d'un pas de déplacement
	 Grid.Position position; // position dans la grille
	 ISU.Coord center; // coordonnées en cm du centre de l'entité

	// FIELDS

	 int orientation_degree; // orientation par rapport à l'axe des x

	// CONSTRUCTOR

	 Entity(String name) { throw new UnsupportedOperationException("Unimplemented method"); }

	// SETTER

	 void setPosition(Grid.Position position) { throw new UnsupportedOperationException("Unimplemented method"); }

	 void setCoord(ISU.Coord center) { throw new UnsupportedOperationException("Unimplemented method"); }

	 void setSize(Grid.Dimension dimension) { throw new UnsupportedOperationException("Unimplemented method"); }

	 void setSize(ISU.Dimension dimension) { throw new UnsupportedOperationException("Unimplemented method"); }

	// GETTER

	 ISU.Coord center() { return null; }

	 Grid.Position position() { return null; }

	 int orientation() { return 0; }

	// TRANSLATION

	 void translate(Grid.Vector v) { throw new UnsupportedOperationException("Unimplemented method"); }

	 void translate(ISU.Vector v) { throw new UnsupportedOperationException("Unimplemented method"); }

	// TURN

	/**
	 * @apiNote turn is a rotation around the center of the entity.
	 * @param angle_degree
	 */
	 void turn(int angle_degree) { throw new UnsupportedOperationException("Unimplemented method"); }

	// SHOW

	void show(PrintStream ps) { throw new UnsupportedOperationException("Unimplemented method"); }

	// === MOVE ===

	/**
	 * @apiNote déplacement vers le nord en nombre de pas
	 * @param nStep
	 */
	 void moveNorth(int nStep) { throw new UnsupportedOperationException("Unimplemented method"); }

	 void moveSouth(int nStep) { throw new UnsupportedOperationException("Unimplemented method"); }

	/**
	 * @apiNote déplacement vers l'est en cm
	 * @param length_cm
	 */
	 void moveEast(double length_cm) { throw new UnsupportedOperationException("Unimplemented method"); }

	 void moveWest(double length_cm) { throw new UnsupportedOperationException("Unimplemented method"); }

}
