package engine;

import java.io.PrintStream;

import game.Game;

public class ISU {

	// FIELDS

	private Axis xAxis, yAxis;
	private ISU isu;
	private Grid grid;

	// CONSTRUCTOR

	public ISU(Game game) {
		this.isu = this;
		// mettre à la fin du constructeur de grid set pour finir d init isu
	}

	// SETTER

	void set(Grid grid) {
		this.grid = grid;
		this.xAxis = grid.xAxis;
		this.yAxis = grid.yAxis;
	}

	// == DIMENSION (cm) ==

	class Dimension {
		double x_cm, y_cm;

		// CONSTRUCTOR

		Dimension(double x_cm, double y_cm) {
			this.x_cm = x_cm;
			this.y_cm = y_cm;
			this.normalize();
		}

		// GEOMETRY

		void normalize() {
			this.x_cm = xAxis.normalize(x_cm);
			this.y_cm = yAxis.normalize(y_cm);
		}

		// SETTER

		void setxy(double x_cm, double y_cm) {
			this.x_cm = xAxis.normalize(x_cm);
			this.y_cm = yAxis.normalize(y_cm);
		}

		// GETTER

		ISU isu() {
			return isu;
		}

		// EQUALS / EQUIV
		@Override
		public boolean equals(Object o) {
			if (o instanceof Dimension) {
				return this.equiv((Dimension) o);
			}
			return false;
		}

		protected boolean equiv(Dimension d) {
			return (this.x_cm == d.x_cm) && (this.y_cm == d.y_cm);
		}

		// GETTER

		public double x() {
			return this.x_cm;
		}

		public double y() {
			return this.y_cm;
		}

		// FACTORY

		ISU.Vector mkScaledVector(double factor) {
			return new Vector(this.x_cm * factor, this.y_cm * factor);
		}

		ISU.Vector mkScaledVector(double xFactor, double yFactor) {
			return new Vector(this.x_cm * xFactor, this.y_cm * yFactor);
		}

		ISU.Vector mkVector() {
			return new Vector(this.x_cm, this.y_cm);
		}

		// SHOW

		void show(PrintStream ps) {
			ps.printf(" x = %d cm \n y = %d cm\n", this.x_cm, this.y_cm);
		}

	}

	// == POINT ==

	class Coord extends Dimension {

		// CONSTRUCTOR

		Coord(double x_cm, double y_cm) {
			super(x_cm, y_cm);
		}

		// SHOW

		void show(PrintStream ps) {
			ps.print(this.toString());
			super.show(ps);
		}

		// EQUALS
		@Override
		public boolean equals(Object o) {
			if (o instanceof Coord) {
				return super.equals(o);
			}
			return false;
		}

		// FACTORY

		ISU.Vector mkVectorToward(Coord target) {
			return new Vector(target.x_cm - this.x_cm, target.y_cm - this.y_cm);
		}

		// CONVERSION

		Grid.Position toGridPosition() {
			return null;
		}

		// TRANSLATION

		void translate(ISU.Vector v) {
			this.x_cm += v.x_cm;
			this.y_cm += v.y_cm;
			this.normalize();
		}

		ISU.Coord mkTranslated(ISU.Vector v) {
			return new Coord(this.x_cm + v.x_cm, this.y_cm + v.y_cm);
		}

		// COPY

		ISU.Coord mkCopy() {
			return new Coord(this.x_cm, this.y_cm);
		}

		// ROTATION

		/**
		 * @apiNote rotation around the origin (0,0)
		 * @param angle_degree
		 */
		void rotation(int angle_degree) {
			throw new UnsupportedOperationException("Unimplemented method");
		}

		/**
		 * @apiNote rotation around the given center
		 * @param center
		 * @param angle_degree
		 */
		void rotateAround(Coord center, int angle_degree) {
			throw new UnsupportedOperationException("Unimplemented method");
		}

		// DISTANCE

		double distanceTo(Coord pt) {
			return 0.0;
		}

	}

	// == VECTOR ==

	/**
	 * @apiNote The Vector class defines canonical vectors with origin in (0,0)
	 *          poiting at a target coordinate.
	 * @apiNote Canonocal vectors are defined by their target Coord.
	 */
	class Vector extends Dimension {

		// CONSTRUCTOR

		Vector(double targetX_cm, double targetY_cm) {
			super(targetX_cm, targetY_cm);
		}

		// OPERATOR

		void add(Vector v) {
			throw new UnsupportedOperationException("Unimplemented method");
		}

		void scale(double factor) {
			throw new UnsupportedOperationException("Unimplemented method");
		}

		void scale(double xFactor, double yFactor) {
			throw new UnsupportedOperationException("Unimplemented method");
		}

		/**
		 * @apiNote produit scalaire
		 * @param v
		 * @return le produit scalaire de `this` et du vecteur v
		 */
		double dot(ISU.Vector v) {
			return 0.0;
		}

		double norm() {
			return 0.0;
		}

		/**
		 * @apiNote rend le vecteur unitaire, ie. de norme = 1
		 */
		void unity() {
			throw new UnsupportedOperationException("Unimplemented method");
		}

		// TURN

		/**
		 * @apiNote turn the vector itself
		 * @implNote the center of the rotation is the origin of the vector
		 * @param angle_degree
		 */
		void turn(int angle_degree) {
			throw new UnsupportedOperationException("Unimplemented method");
		}

	}

}
