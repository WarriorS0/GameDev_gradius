package engine.move;

import java.util.LinkedList;
import java.util.List;

import engine.entity.Entity;
import engine.geometry.Grid;
import engine.geometry.ISU;
import engine.shape.Rect;
import game.Game;

public class Model {
	// FIELDS
	private Grid grid;
	public List<Entity> entities;
	public double delta_t;

	// CONSTRUCTOR
	public Model(Grid grid) {
		this.grid = grid;
		entities = new LinkedList<>();
		delta_t = 0;
	}

	// ADD, REMOVE Entity
	public void add(Entity e) {
		entities.add(e);
	}

	void remove(Entity e) {
		entities.remove(e);
	}

	public void tick(double delta_t) {
		this.delta_t = delta_t;
		Physique phy = new Physique();
		for (Entity e : entities) {
			// phy.intersects(e);
			phy.move(e);
		}
	}

	class Physique {

		public Physique() {
		}

		public ISU.Coord dest(Entity e) {
			double x = e.center().x();
			double y = e.center().y();
			double vect_x = e.lSpeed.x();
			double vect_y = e.lSpeed.y();
			double delta_x = vect_x * delta_t;
			double delta_y = vect_y * delta_t;
			return e.isu.new Coord(delta_x, delta_y);
		}

		public Rect superBounding(Entity e) {
			double x = e.center().x();
			double y = e.center().y();
			ISU.Coord coord = dest(e);
			double dest_x = coord.x();
			double dest_y = coord.y();
			double xmin = Math.min(x, dest_x) - e.size().x() / 2;
			double xmax = Math.max(x, dest_x) + e.size().x() / 2;
			double ymin = Math.min(y, dest_y) - e.size().y() / 2;
			double ymax = Math.max(y, dest_y) + e.size().y() / 2;
			Rect r = new Rect(e.isu.new Coord((xmax + xmin) / 2, (ymin + ymax) / 2),
					e.isu.new Dimension(xmax - xmin, ymax - ymin), 0);
			if (Math.abs(dest_x - x) > e.lSpeed.x()) {

			}
			return r;

		}

		

		public void move(Entity e) {
			if(e.aSpeed != 0 ) {
				e.turn(e.aSpeed*delta_t);
			}
			boolean collisionOccurred = false;
			for (Entity en : entities) {
				if (e == en)
					continue;

				Rect r_en = superBounding(en);
				Rect r_e = superBounding(e);

				if (r_en.intersects(r_e)) {

					double vrelx = (e.lSpeed.x() - en.lSpeed.x()) * delta_t;
					double vrely = (e.lSpeed.y() - en.lSpeed.y()) * delta_t;

					double xInvEntry, yInvEntry;
					double xInvExit, yInvExit;

					if (vrelx > 0.0) {
						xInvEntry = (en.center().x() - en.size().x() / 2) - (e.center().x() + e.size().x() / 2);
						xInvExit = (en.center().x() + en.size().x() / 2) - (e.center().x() - e.size().x() / 2);
					} else {
						xInvEntry = (en.center().x() + en.size().x() / 2) - (e.center().x() - e.size().x() / 2);
						xInvExit = (en.center().x() - en.size().x() / 2) - (e.center().x() + e.size().x() / 2);
					}

					if (vrely > 0.0) {
						yInvEntry = (en.center().y() - en.size().y() / 2) - (e.center().y() + e.size().y() / 2);
						yInvExit = (en.center().y() + en.size().y() / 2) - (e.center().y() - e.size().y() / 2);
					} else {
						yInvEntry = (en.center().y() + en.size().y() / 2) - (e.center().y() - e.size().y() / 2);
						yInvExit = (en.center().y() - en.size().y() / 2) - (e.center().y() + e.size().y() / 2);
					}
					double txEntry, tyEntry, txExit, tyExit;

					if (vrelx == 0.0) {
						boolean noOverlapX = (e.center().x() + e.size().x() / 2 <= en.center().x() - en.size().x() / 2)
								|| (e.center().x() - e.size().x() / 2 >= en.center().x() + en.size().x() / 2);
						if (noOverlapX) {
							txEntry = Double.POSITIVE_INFINITY;
							txExit = Double.NEGATIVE_INFINITY;
						} else {
							txEntry = Double.NEGATIVE_INFINITY;
							txExit = Double.POSITIVE_INFINITY;
						}
					} else {
						txEntry = xInvEntry / vrelx;
						txExit = xInvExit / vrelx;
					}

					if (vrely == 0.0) {
						boolean noOverlapY = (e.center().y() + e.size().y() / 2 <= en.center().y() - en.size().y() / 2)
								|| (e.center().y() - e.size().y() / 2 >= en.center().y() + en.size().y() / 2);
						if (noOverlapY) {
							tyEntry = Double.POSITIVE_INFINITY;
							tyExit = Double.NEGATIVE_INFINITY;
						} else {
							tyEntry = Double.NEGATIVE_INFINITY;
							tyExit = Double.POSITIVE_INFINITY;
						}
					} else {
						tyEntry = yInvEntry / vrely;
						tyExit = yInvExit / vrely;
					}

					double entryTime = Math.max(txEntry, tyEntry);
					double exitTime = Math.min(txExit, tyExit);

					if (!(entryTime > exitTime || (txEntry < 0.0 && tyEntry < 0.0) || txEntry > 1.0 || tyEntry > 1.0)) {
						collisionOccurred = true;
						e.collision(en);
					}
				}
			}
			if (!collisionOccurred) {
				ISU.Coord dest = dest(e);
				if(e.lSpeed.x() == 0 && e.lSpeed.y() == 0) {
					e.translate(
							e.isu.new Vector( 0, 0));
				}
				else if(e.lSpeed.x() == 0) {
					e.translate(
							e.isu.new Vector(0, dest.y()));
				}
				else if(e.lSpeed.y() == 0) {
					e.translate(
							e.isu.new Vector(dest.x(), 0));
				}
				else {
					e.translate(
							e.isu.new Vector(dest.x(), dest.y()));
				}
				
			
			}
		}
	}
}
