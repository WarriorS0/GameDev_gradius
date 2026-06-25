package engine.graphics.hud;

import engine.entity.Entity;
import oop.graphics.Color;
import oop.graphics.Graphics;

public class HealthBar extends FollowerLabel implements IHealthBar, IAnchored {

	private enum TYPE {
		ABSOLUTE, ANCHORED, FOLLOWING
	}

	private int hp, totalhp;
	private int sizeOfOneHp;

	private final Anchor anchor;
	private final TYPE type;

	public Color colorPts;
	public boolean drawText;

	public HealthBar(Entity entity, Anchor anchor, PixelCoordinate offset, int sizeOfOneHp, Color colorText,
			Color colorPts) {
		this(sizeOfOneHp, true, new PixelCoordinate(0, 0), offset, entity, anchor, TYPE.ANCHORED, colorText, colorPts,
				true);
	}

	public HealthBar(Entity entity, PixelCoordinate position, PixelCoordinate offset, int sizeOfOneHp, Color colorText,
			Color colorPts) {
		this(sizeOfOneHp, true, position, offset, entity, null, TYPE.ABSOLUTE, colorText, colorPts, false);
	}

	public HealthBar(Entity entity, PixelCoordinate offset, int sizeOfOneHp, Color colorText, Color colorPts) {
		this(sizeOfOneHp, true, new PixelCoordinate(0, 0), offset, entity, null, TYPE.FOLLOWING, colorText, colorPts,
				false); // we use new PixelCoordinate(0,0) here instead of
						// FollowerLabel.view.worldToScreen(entity.center()) because view isn't
						// initialized yet at this stade of the execution
	}

	public HealthBar(int sizeOfOneHp, boolean visible, PixelCoordinate position, PixelCoordinate offset, Entity entity,
			Anchor anchor, TYPE type, Color colorText, Color colorPts, boolean drawText) {
		super("", entity, offset, colorText);
		this.position = position;
		this.anchor = anchor;
		this.type = type;
		this.sizeOfOneHp = sizeOfOneHp;
		this.colorPts = colorPts;
		this.drawText = drawText;
		this.totalhp = target.bot().life();
		this.hp = this.totalhp;
		this.set(() -> this.toString());
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(colorPts);
		for (int i = 0; i < totalhp; i++) {
			if (hp > i) {
				g.fillOval(position.x + (i * sizeOfOneHp) - (totalhp * sizeOfOneHp) / 2, position.y - sizeOfOneHp / 2,
						sizeOfOneHp, sizeOfOneHp);
			} else {
				g.drawOval(position.x + i * sizeOfOneHp - (totalhp * sizeOfOneHp) / 2, position.y - sizeOfOneHp / 2,
						sizeOfOneHp, sizeOfOneHp);
			}
		}
		if (drawText)
			super.draw(g);
	}

	@Override
	public void update(int canvasWidth, int canvasHeigh) {
		hp = target.bot().life(); // we don't put the bot in a local var because bot can be switch around mid game
		switch (this.type) {
		case ANCHORED: {
			this.position.x = anchor.calculateX(canvasWidth) + offset.x;
			this.position.y = anchor.calculateY(canvasHeigh) + offset.y;
			return;
		}
		case FOLLOWING: {
			super.update(canvasWidth, canvasHeigh);
			return;
		}
		case ABSOLUTE: {
			return;
		}
		}

	}

	@Override
	public Anchor anchor() {
		return this.anchor;
	}

	@Override
	public int getCurrentHP() {
		return this.hp;
	}

	@Override
	public void setCurrentHP(int hp) {
		this.hp = hp;
	}

	@Override
	public int getTotalHP() {
		return this.totalhp;
	}

	@Override
	public void setTotalHP(int hp) {
		this.totalhp = hp;
	}

	@Override
	public String toString() {
		return String.format("%d/%d", hp, totalhp);
	}

}
