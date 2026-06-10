package engine.graphics;

import java.util.ArrayList;
import java.util.List;

import engine.Entity;
import engine.shape.Rect;
import game.Game;
import oop.graphics.Graphics;
import oop.graphics.Graphics.Colors;

public class View {
	private List<Avatar> l_avatar;

	// Ajout : variable pour mémoriser l'heure du dernier affichage
	private long lastTime;

	View() {
		this.l_avatar = new ArrayList<>();

		// Initialisation du chrono
		this.lastTime = System.currentTimeMillis();
	}

	public List<Avatar> getAvatars() {
		return l_avatar;
	}

	public void add(Avatar a) {
		l_avatar.add(a);
	}

	public void remove(Avatar a) {
		l_avatar.remove(a);
	}

	void paint(Graphics g) {
		long currentTime = System.currentTimeMillis();
		long delta_t = currentTime - lastTime;
		lastTime = currentTime;
		g.drawRect(2, 2, 3, 2);
		for (int i = 0; i < l_avatar.size(); i++) {
			Avatar a = l_avatar.get(i);
//			if (true) {
//				Entity entity = a.e;
//				g.setColor(Colors.red);
//				Rect boundingBox = entity.box();
//				g.drawRect((int) (boundingBox.center.x() - boundingBox.halfWidth) * Game.pixelPerCm,
//						(int) (boundingBox.center.y() - boundingBox.halfHeight) * Game.pixelPerCm,
//						(int) boundingBox.halfWidth * 2 * Game.pixelPerCm,
//						(int) boundingBox.halfHeight * 2 * Game.pixelPerCm);
//			}

			a.updateAnimation(delta_t);
			a.paint(g);
		}
	}
}