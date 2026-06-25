package game.gradius.graphics;

import java.util.ArrayList;
import java.util.List;

import engine.entity.Entity;
import engine.geometry.ISU;
import engine.graphics.Avatar;
import engine.graphics.PixelCoordinate;
import game.Game;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;

public class TileAvatar extends Avatar {
	private static final String SPRITE_PATH = "src/game/gradius/graphics/background.png";
	private final int size_bande = 20;
	private BufferedImage spritesheet;
	private List<Bande> l;
	private Game game;

	public TileAvatar(Entity entity) {
		super(entity);
		l = new ArrayList<Bande>();
		this.game=Game.game();
	}

	@Override
	public void initImage(Graphics g) {
		this.spritesheet = g.load(SPRITE_PATH);
		for (int i = 0; i < 51; i++) {
			BufferedImage img = spritesheet.getSubimage(i * size_bande, 0, size_bande, 320);
			l.add(new Bande(new PixelCoordinate(i* size_bande,0), img));
		}
	}

	@Override
	public void paint(Graphics g) {
		if (spritesheet == null || l == null) {
			initImage(g);
		}
		
		for(Bande b:l) {
			if(!b.change) {
				
				g.drawImage(b.img, 0, 0, 0, 120);
			}
		}
	}

	@Override
	public void updateAnimation(double delta_t) {
		// pas d'animation

	}

}
