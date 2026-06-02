// = Rect =
package engine.shape;

import engine.geometry.ISU;

public class Rect extends Shape {

	// FIELDS

	double halfWidth, halfHeight;
	int angle_degree;

	// CONSTRUCTOR

	Rect(ISU.Coord center, ISU.Dimension size, int angle_degree) {
		super(center);
		this.halfHeight = size.y() / 2;
		this.halfWidth = size.x() / 2;
		this.angle_degree = angle_degree;
	}

	// TRANSLATION ?

	// ROTATION

	void rotate(int angle_degree) {
		this.angle_degree = ((((this.angle_degree + angle_degree) % 360) + 360) % 360);
	}

	// == INTERSECTION ==
	public boolean intersects(iShape shape) {
		return shape.intersects(this);
	}

	// === Rect/Circle Intersection ===
	public boolean intersects(Circle circle) {
		throw new UnsupportedOperationException("UNIMPLEMENTED METHOD `intersects`");
	}

	// === Helping inner class ===

	/**
	 * @implNote Principe
	 *           <UL>
	 *           <LI>translate le centre du cercle vers le repère formé par les axes
	 *           du rectangle,</LI>
	 *           <LI>redresse le repère du rectangle en annulant la rotation du
	 *           rectangle,</LI>
	 *           <LI>détermine le point <i>P</i> du rectangle le plus proche du
	 *           centre <i>C</i> du cercle de façon efficace car le rectangle est
	 *           aligné sur les axes X,Y.</LI>
	 *           </UL>
	 * @implNote Il y a intersection si distance(P,C) < rayon du cercle</LI>
	 */

	class RectCircleIntersection {

		// FIELDS

		Rect outer;
		Circle circle;

		// CONSTRUCTOR

		RectCircleIntersection(Rect outer, Circle circle) {
			this.circle = circle;
			this.outer = outer;
		}

		// REMEDY means `set right an undesirable situation`

		/**
		 * @apiNote
		 * @implNote Translate virtuellement Rect et Circle dans un repère centré sur le
		 *           centre du rectangle donc les axes sont ceux du rectangle.
		 * @implNote Les coordonnées du centre du rectangle deviennent alors (0,0)
		 * @implNote On translate le centre du cercle
		 * @implNote On déplace par rotation le centre du cercle de -Rect.angle.
		 */
		void remedy() {
			 
		}

		// INTERSECTION in the easy case

		boolean intersects() {
			throw new UnsupportedOperationException("UNIMPLEMENTED METHOD `intersects`");
		}

		/**
		 * @apiNote POINT LE PLUS PROCHE DU CENTRE DU CERCLE
		 * @implNote on projette les coins du rectange (c1,c2) et le centre (c) du
		 *           cercle sur l'axe des <i>x<i>, la coordonnées en x du point le plus
		 *           proche est parmi {c1.x, c2.x, c.x}
		 * @implNote on projette les coins du rectange (c1,c2) et le centre (c) du
		 *           cercle sur l'axe des <i>y<i>, la coordonnées en x du point le plus
		 *           proche est parmi {c1.y, c2.y, c.y}
		 *
		 */

		ISU.Coord closestRectpoint() {
			throw new UnsupportedOperationException("UNIMPLEMENTED METHOD `closestRectpoint`");
		}

		/**
		 * @return &in; {p, l, r}
		 * @implNote la position dans l'interval [l,r] la plus proche de p est :
		 * @implNote p si p &in; [l,r]
		 * @implNote l si p < l
		 * @implNote r si r < p
		 * @param p = position
		 * @param l = borne inférieure de l'interval
		 * @param r = borne supérieure de l'interval
		 */
		double clamp(double p, double l, double r) {
			if(p < l)
				return l;
			if(r<p)
				return r;
			return p;
		}

	}

	// === Rect/Rect Intersection ===
	boolean intersects(Rect rect) {
		throw new UnsupportedOperationException("UNIMPLEMENTED METHOD `intersects`");
	}

	// === Helping inner class ===

	class RectRectIntersection {

	}
}
