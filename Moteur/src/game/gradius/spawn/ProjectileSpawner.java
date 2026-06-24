package game.gradius.spawn;

import engine.graphics.View;
import engine.geometry.ISU;
import engine.move.Model;
import game.gradius.entity.Projectile;
import game.gradius.graphics.ProjectileAvatar;
import game.gradius.stunt.ProjectileStunt;

public class ProjectileSpawner {

	private final Model model;
	private final View view;

	public ProjectileSpawner(Model model, View view) {
		if (model == null) {
			throw new IllegalArgumentException("model cannot be null");
		}

		if (view == null) {
			throw new IllegalArgumentException("view cannot be null");
		}

		this.model = model;
		this.view = view;
	}

	public Projectile spawn(ISU.Coord center, ISU.Vector speed) {
		Projectile projectile = new Projectile(center, speed);

		model.add(projectile);
		model.setStunt(projectile, new ProjectileStunt(model, projectile));
		model.cullOffScreen(projectile);

		view.add(new ProjectileAvatar(projectile));

		return projectile;
	}
}