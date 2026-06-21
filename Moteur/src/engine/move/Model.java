package engine.move;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import engine.entity.Entity;
import engine.gal.GALStunt;
import engine.geometry.Grid;
import engine.geometry.ISU;
import game.Game;

public class Model {

	// =========================
	// Fields
	// =========================

	// private final Grid grid;
	private final ISU isu;

	public final List<Entity> entities;

	private final Map<Entity, Stunt> stunts;

	public double delta_t;

	// =========================
	// Constructor
	// =========================

	public Model(Grid grid) {
		Game game = Game.game();

		if (game == null) {
			throw new IllegalStateException("No current Game instance");
		}

		// this.grid = grid;
		this.isu = game.isu;

		this.entities = new LinkedList<>();
		this.stunts = new HashMap<>();

		this.delta_t = 0.0;
	}

	// =========================
	// Add / remove entities
	// =========================

	public void add(Entity entity) {
		if (!entities.contains(entity)) {
			entities.add(entity);
		}
	}

	public void add(Entity entity, Stunt stunt) {
		add(entity);
		setStunt(entity, stunt);
	}

	public void remove(Entity entity) {
		entities.remove(entity);
		stunts.remove(entity);
	}

	// =========================
	// Stunt
	// =========================

	public void setStunt(Entity entity, Stunt stunt) {
		ensureKnownEntity(entity);
		stunts.put(entity, stunt);
	}

	public Stunt stunt(Entity entity) {
		ensureKnownEntity(entity);
		return stunts.get(entity);
	}

	private void ensureKnownEntity(Entity entity) {
		if (!entities.contains(entity)) {
			throw new IllegalArgumentException("Entity is not in this model");
		}
	}

	// =========================
	// Tick
	// =========================

	public void tick(double delta_t) {
		this.delta_t = delta_t;

		Physique phy = new Physique();

		for (Entity entity : new LinkedList<>(entities)) {
			if (entity.dead()) {
				continue;
			}

			Stunt stunt = stunts.get(entity);

			if (stunt instanceof GALStunt galStunt) {
				galStunt.tick(delta_t * 1000.0);
			}

			if (entity.dead()) {
				continue;
			}

			phy.move(entity);
		}
	}

	// =========================
	// Physics
	// =========================

	class Physique {

		public ISU.Vector delta(Entity entity) {
			ISU.Vector speed = entity.linearSpeed;

			double delta_x = speed.x() * delta_t;
			double delta_y = speed.y() * delta_t;

			return isu.new Vector(delta_x, delta_y);
		}

		public void move(Entity entity) {
			rotate(entity);
			translate(entity);
		}

		private void rotate(Entity entity) {
			double angularSpeed = entity.angularSpeed;
			double old = entity.orientation();

			if (angularSpeed != 0.0) {
				entity.turn(angularSpeed * delta_t);
				Stunt stunt = stunts.get(entity);
				if (stunt.wantsMoveNotif())
					stunt.rotated(old, entity.orientation());
			}
		}

		private void translate(Entity entity) {
			ISU.Vector d = delta(entity);

			if (d.x() == 0.0 && d.y() == 0.0) {
				return;
			}

			ISU.Coord old = entity.center();

			// Déplacement en X
			if (d.x() != 0.0) {
				entity.translate(isu.new Vector(d.x(), 0));

				Entity other = intersectedEntity(entity);

				if (other != null) {
					entity.translate(isu.new Vector(-d.x(), 0));
					collision(entity, other);
				}
			}

			// Déplacement en Y
			if (d.y() != 0.0) {
				entity.translate(isu.new Vector(0, d.y()));

				Entity other = intersectedEntity(entity);

				if (other != null) {
					entity.translate(isu.new Vector(0, -d.y()));
					collision(entity, other);
				}
			}

			Stunt stunt = stunts.get(entity);
			if (stunt.wantsMoveNotif())
				stunt.moved(old, entity.center());
		}

		private Entity intersectedEntity(Entity entity) {
			for (Entity other : entities) {
				if (entity == other) {
					continue;
				}

				if (other.dead()) {
					continue;
				}

				if (entity.intersects(other)) {
					return other;
				}
			}

			return null;
		}

		private void collision(Entity entity, Entity other) {
			Stunt stunt = stunts.get(entity);

			if (stunt != null) {
				stunt.collision(other);
			}

			Stunt otherStunt = stunts.get(other);

			if (otherStunt != null) {
				otherStunt.collision(entity);
			}
		}
	}
}