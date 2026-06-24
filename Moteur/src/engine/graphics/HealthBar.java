package engine.graphics;

import java.util.function.Supplier;

import engine.entity.Entity;
import oop.graphics.Color;
import oop.graphics.Graphics;
import oop.graphics.Graphics.Colors;

public class HealthBar implements HudElement {

	private int x, y, width, height, hp, totalhp;
	private boolean visible = false;
	private PixelCoordinate coord;
	private Entity entity;

	private int scale;
	public HealthBar(PixelCoordinate coord, int scale,Entity e) {
		
		this.coord = coord;
		this.width = width;
		this.height = height;
		this.hp = e.bot().healthPercent();
		this.totalhp = e.bot().healthPercent();
		this.entity = e;
		this.scale = scale;
	}
	
	

	@Override
	public void draw(Graphics g) {
		//int hpwidth = 10;
		hp = entity.bot().healthPercent();
		g.setColor(Colors.white);
		for (int i = 0; i < totalhp; i++) {
			if (hp > i) {
				g.fillOval(coord.x + i * scale, coord.y, scale, scale);
			} else {
				g.drawOval(coord.x + i * scale, coord.y, scale, scale);
			}
		}
		g.setColor(Colors.blue);
		g.drawRect(coord.x, coord.y, width, height);

	}

	public void setCurrentHP(int hp) {
		this.hp = hp;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void setVisibility(boolean shown) {
		visible = shown;
	}

}
