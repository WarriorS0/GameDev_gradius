package engine.geometry;

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
		this.xAxis = new Axis(game.torusOnXaxis, game.width_cm);
		this.yAxis = new Axis(game.torusOnYaxis, game.height_cm);
		// mettre à la fin du constructeur de grid set pour finir d init isu
	}

	// SETTER

	void set(Grid grid) {
		this.grid = grid;
	}

	// == DIMENSION (cm) ==

	 public class Dimension {
		double x_cm, y_cm;

		// CONSTRUCTOR

		public Dimension(double x_cm, double y_cm) {
			this.x_cm = x_cm;
			this.y_cm = y_cm;
			this.normalize();
		}

		// GEOMETRY

		void normalize() {
			if (xAxis == null || yAxis == null) {
				throw new IllegalStateException("Axis not init");
			}
			this.x_cm = xAxis.normalize(x_cm);
			this.y_cm = yAxis.normalize(y_cm);
		}

		// SETTER

		void setxy(double x_cm, double y_cm) {
			this.x_cm = x_cm;
			this.y_cm = y_cm;
			this.normalize();
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
			if ((this instanceof Vector && d instanceof Vector) || (this instanceof Coord && d instanceof Coord)) {
				double epsilon =1e-9;
				double deltaX = Math.abs( this.x_cm - d.x_cm);
				double deltaY = Math.abs(this.y_cm - d.y_cm);
				return (deltaX < epsilon) && (deltaY < epsilon);
			}
			return false;
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
			ps.printf(" x = %f cm \n y = %f cm\n", this.x_cm, this.y_cm);
		}

	}

	// == POINT ==

	public class Coord extends Dimension {

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

		public Grid.Position toGridPosition() {
			if (Game.game() == null || grid == null) {
			    throw new IllegalStateException("ISU not linked to game/grid");
			}
			
			int x_cell = (int) (this.x_cm / Game.game().cmPerCell);
			int y_cell = (int) (this.y_cm / Game.game().cmPerCell);
			return isu().grid.new Position(x_cell, y_cell);
		}

		// TRANSLATION

		public void translate(ISU.Vector v) {
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
			this.rotateAround(new Coord(0, 0), angle_degree);
		}

		/**
		 * @apiNote rotation around the given center
		 * @param center
		 * @param angle_degree
		 */
		void rotateAround(Coord center, int angle_degree) {
			double tempX = this.x_cm;
			double tempY = this.y_cm;
			this.x_cm = center.x_cm + (tempX - center.x_cm) * Math.cos(Math.toRadians(angle_degree))
					- (tempY - center.y_cm) * Math.sin(Math.toRadians(angle_degree));
			this.y_cm = center.y_cm + (tempX - center.x_cm) * Math.sin(Math.toRadians(angle_degree))
					+ (tempY - center.y_cm) * Math.cos(Math.toRadians(angle_degree));
			this.normalize();
		}

		// DISTANCE

		double distanceTo(Coord pt) {
			double distX = xAxis.distance(this.x_cm, pt.x_cm);
			double distY = yAxis.distance(this.y_cm, pt.y_cm);
			return Math.sqrt(distX * distX + distY * distY);
		}

	}

	// == VECTOR ==

	/**
	 * @apiNote The Vector class defines canonical vectors with origin in (0,0)
	 *          poiting at a target coordinate.
	 * @apiNote Canonocal vectors are defined by their target Coord.
	 */
	public class Vector extends Dimension {

		// CONSTRUCTOR

		public Vector(double targetX_cm, double targetY_cm) {
			super(targetX_cm, targetY_cm);
		}

		// OPERATOR

		void add(Vector v) {
			this.x_cm += v.x_cm;
			this.y_cm += v.y_cm;
			this.normalize();
		}

		void scale(double factor) {
			this.x_cm *= factor;
			this.y_cm *= factor;
			this.normalize();
		}

		void scale(double xFactor, double yFactor) {
			this.x_cm *= xFactor;
			this.y_cm *= yFactor;
			this.normalize();
		}

		/**
		 * @apiNote produit scalaire
		 * @param v
		 * @return le produit scalaire de `this` et du vecteur v
		 */
		double dot(ISU.Vector v) {
			return (this.x_cm * v.x_cm) + (this.y_cm * v.y_cm);
		}

		double norm() {
			return Math.sqrt(this.dot(this));
		}

		/**
		 * @apiNote rend le vecteur unitaire, ie. de norme = 1
		 */
		void unity() {
			double norm = this.norm();
			if (norm == 0) {
				throw new IllegalStateException("Cannot normalize zero vector");
			}
			this.x_cm /= norm;
			this.y_cm /= norm;
			this.normalize();
		}

		// TURN

		/**
		 * @apiNote turn the vector itself
		 * @implNote the center of the rotation is the origin of the vector
		 * @param angle_degree
		 */
		void turn(int angle_degree) {
			double tempX = this.x_cm;
			double tempY = this.y_cm;
			this.x_cm = tempX * Math.cos(Math.toRadians(angle_degree)) - tempY * Math.sin(Math.toRadians(angle_degree));
			this.y_cm = tempX * Math.sin(Math.toRadians(angle_degree)) + tempY * Math.cos(Math.toRadians(angle_degree));
			this.normalize();
		}

	}

}
