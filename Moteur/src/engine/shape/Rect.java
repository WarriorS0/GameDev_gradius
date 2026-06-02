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
		RectCircleIntersection inter = new RectCircleIntersection(this, circle);
		return inter.intersects();
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

	private class RectCircleIntersection {

		// FIELDS

		private Rect outer;
		private Circle circle;
		
		private ISU.Coord localCircleCenter;
		
		

		// CONSTRUCTOR

		RectCircleIntersection(Rect outer, Circle circle) {
			this.circle = circle;
			this.outer = outer;
			remedy();
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
			ISU.Vector vec = isu.new Vector(-outer.center.x(), -outer.center.y());
			ISU.Coord newCenter = circle.center.mkTranslated(vec);
			newCenter.rotation(-outer.angle_degree);
			this.localCircleCenter = newCenter;
			
			
		}

		// INTERSECTION in the easy case

		boolean intersects() {
			ISU.Coord closestP = this.closestRectpoint();
			double px = closestP.x();
			double py = closestP.y();
			
			double dx = this.localCircleCenter.x() - px;
			double dy = this.localCircleCenter.y() - py;
			
			return dx*dx + dy*dy <= circle.radius*circle.radius;
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
			double px = clamp(localCircleCenter.x(), -outer.halfWidth, outer.halfWidth);
			double py = clamp(localCircleCenter.y(), -outer.halfHeight, outer.halfHeight);

			return isu.new Coord(px, py);
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
	public boolean intersects(Rect rect) {
		RectRectIntersection inter = new RectRectIntersection(this, rect);
		return inter.intersects();
	}

	// === Helping inner class ===

	private class RectRectIntersection {
		
		private Rect rect1;
		private Rect rect2;
		
		RectRectIntersection(Rect rect1, Rect rect2){
			this.rect1 = rect1;
			this.rect2 = rect2;
		}
		
		boolean intersects() {
			ISU.Coord[] corners1 = corners(rect1);
			ISU.Coord[] corners2 = corners(rect2);
			
			return true;
		}
		
		ISU.Coord[] corners(Rect rect){
			ISU.Coord[] corners = new ISU.Coord[4];
			corners[0] = isu.new Coord(-rect.halfWidth, -rect.halfHeight);
			corners[1] = isu.new Coord(-rect.halfWidth, rect.halfHeight);
			corners[2] = isu.new Coord(rect.halfWidth, rect.halfHeight);
			corners[3] = isu.new Coord(rect.halfWidth, -rect.halfHeight);
			return corners;
		}

	}
}
