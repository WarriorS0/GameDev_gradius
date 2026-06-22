package engine.move;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map;

import engine.entity.Entity;
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

	private ViewPort viewPort;
	private final Set<Entity> cullable = new HashSet<>();

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
	// View port / culling
	// =========================

	/**
	 * Attaches a view port advanced on every tick.
	 *
	 * @param viewPort the camera to drive, or null to detach
	 */
	public void setViewPort(ViewPort viewPort) {
		this.viewPort = viewPort;
	}

	/**
	 * @return the attached view port, or null
	 */
	public ViewPort viewPort() {
		return viewPort;
	}

	/**
	 * Marks an entity to be killed automatically when it leaves the view port,
	 * typically a projectile. The entity must already be in the model.
	 *
	 * @param entity the entity to cull off-screen
	 */
	public void cullOffScreen(Entity entity) {
		ensureKnownEntity(entity);
		cullable.add(entity);
	}

	// =========================
	// Tick
	// =========================

	public void tick(double delta_t) {
		this.delta_t = delta_t;

		Physics phy = new Physics();

		for (Entity entity : new LinkedList<>(entities)) {
			if (entity.dead()) {
				continue;
			}

			Stunt stunt = stunts.get(entity);
			stunt.tick(delta_t * 1000.0);

			if (entity.dead()) {
				continue;
			}

			phy.move(entity);
		}

		if (viewPort != null) {
			viewPort.tick(delta_t);

			if (!cullable.isEmpty()) {
				for (Entity entity : new LinkedList<>(cullable)) {
					if (entity.dead()) {
						cullable.remove(entity);
						continue;
					}

					if (!viewPort.isVisible(entity)) {
						entity.kill();
						cullable.remove(entity);
					}
				}
			}
		}
	}

	// =========================
	// Physics
	// =========================

	class Physics {

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