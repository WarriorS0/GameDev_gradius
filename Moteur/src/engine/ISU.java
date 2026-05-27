package engine;

import game.Game;

public class ISU {

	// FIELDS

	 Axis xAxis, yAxis;
	 ISU isu;
	 Grid grid;

	// CONSTRUCTOR

	 public ISU(Game game) { throw new UnsupportedOperationException("Unimplemented method"); }

	// SETTER

	 void set(Grid grid) { throw new UnsupportedOperationException("Unimplemented method"); }

	// == DIMENSION (cm) ==

	 class Dimension {
		 double x_cm, y_cm;

		// CONSTRUCTOR

		 Dimension(double x_cm, double y_cm) { throw new UnsupportedOperationException("Unimplemented method"); }

		// GEOMETRY

		 void normalize() { throw new UnsupportedOperationException("Unimplemented method"); }

		// SETTER

		 void setxy(double x_cm, double y_cm) { throw new UnsupportedOperationException("Unimplemented method"); }

		// GETTER

		 ISU isu() { return null; }

		// EQUALS / EQUIV
		 boolean equals(Object o) { return false; }

		 boolean equiv(Dimension d) { return false; }

		// GETTER

		 double x() { return 0.0;   }

		 double y() { return 0.0;   }

		// FACTORY

		 ISU.Vector mkScaledVector(double factor) { return null; }

		 ISU.Vector mkScaledVector(double xFactor, double yFactor) { return null; }

		 ISU.Vector mkVector() { return null; }

		// SHOW

		void show(PrintStream ps) { throw new UnsupportedOperationException("Unimplemented method"); }

	}

	// == POINT ==

	 class Coord  {

		// CONSTRUCTOR

		 Coord(double x_cm, double y_cm) { throw new UnsupportedOperationException("Unimplemented method"); }

		// SHOW

		void show(PrintStream ps) { throw new UnsupportedOperationException("Unimplemented method"); }

		// EQUALS
		 boolean equals(Object o) { return false; }

		// FACTORY

		 ISU.Vector mkVectorToward(Coord target) { return null; }

		// CONVERSION

		 Grid.Position toGridPosition() { return null; }

		// TRANSLATION

		 void translate(ISU.Vector v) { throw new UnsupportedOperationException("Unimplemented method"); }

		 ISU.Coord mkTranslated(ISU.Vector v) { return null; }

		// COPY

		 ISU.Coord mkCopy() { return null; }

		// ROTATION

		/**
		 * @apiNote rotation around the origin (0,0)
		 * @param angle_degree
		 */
		 void rotation(int angle_degree) { throw new UnsupportedOperationException("Unimplemented method"); }

		/**
		 * @apiNote rotation around the given center
		 * @param center
		 * @param angle_degree
		 */
		 void rotateAround(Coord center, int angle_degree) { throw new UnsupportedOperationException("Unimplemented method"); }

		// DISTANCE

		 double distanceTo(Coord pt) { return 0.0;   }

	}

	// == VECTOR ==

	/**
	 * @apiNote The Vector class defines canonical vectors with origin in (0,0)
	 *          poiting at a target coordinate.
	 * @apiNote Canonocal vectors are defined by their target Coord.
	 */
	 class Vector  {

		// CONSTRUCTOR

		 Vector(double targetX_cm, double targetY_cm) { throw new UnsupportedOperationException("Unimplemented method"); }

		// OPERATOR

		 void add(Vector v) { throw new UnsupportedOperationException("Unimplemented method"); }

		 void scale(double factor) { throw new UnsupportedOperationException("Unimplemented method"); }

		 void scale(double xFactor, double yFactor) { throw new UnsupportedOperationException("Unimplemented method"); }

		/**
		 * @apiNote produit scalaire
		 * @param v
		 * @return le produit scalaire de `this` et du vecteur v
		 */
		 double dot(ISU.Vector v) { return 0.0;   }

		 double norm() { return 0.0;   }

		/**
		 * @apiNote rend le vecteur unitaire, ie. de norme = 1
		 */
		 void unity() { throw new UnsupportedOperationException("Unimplemented method"); }

		// TURN

		/**
		 * @apiNote turn the vector itself
		 * @implNote the center of the rotation is the origin of the vector
		 * @param angle_degree
		 */
		 void turn(int angle_degree) { throw new UnsupportedOperationException("Unimplemented method"); }

	}

}
