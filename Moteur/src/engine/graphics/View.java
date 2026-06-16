package engine.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import oop.graphics.Graphics;

public class View {

	private List<Avatar> avatars;
	private long lastTime;
	
	private Hud hud; //optionnal
	

	public View() {
		this.avatars = new ArrayList<>();
		this.lastTime = System.currentTimeMillis();
	}

	public List<Avatar> avatars() {
		return avatars;
	}

	public void add(Avatar avatar) {
		avatars.add(avatar);
	}

	public void remove(Avatar avatar) {
		avatars.remove(avatar);
	}

	public void paint(Graphics g) {
		Objects.requireNonNull(g, "graphics cannot be null");

		long currentTime = System.currentTimeMillis();
		double delta_t = currentTime - lastTime;
		lastTime = currentTime;

		for (Avatar avatar : avatars) {
			avatar.updateAnimation(delta_t);
			avatar.paint(g);
		}
		
		// après les autres paints pour que ça dessine au dessus
		if (hud != null) {
			hud.draw(g);
		}
	}
	
	/**
	 * Attache un HUD à la vue ; il sera dessiné par-dessus la scène.
	 *
	 * @param hud le HUD à dessiner, ou null pour aucun
	 */
	public void setHUD(Hud hud) {
		this.hud = hud;
	}
}