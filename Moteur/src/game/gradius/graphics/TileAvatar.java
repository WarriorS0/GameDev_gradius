package game.gradius.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import engine.entity.Entity;
import engine.graphics.Avatar;
import engine.graphics.PixelCoordinate;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;

public class TileAvatar extends Avatar {
	private static final String SPRITE_PATH = "src/game/gradius/graphics/background.png";
	private final int size_bande = 20;
	private BufferedImage spritesheet;
	private List<Bande> l;

	public TileAvatar(Entity entity) {
		super(entity);
		l = new ArrayList<Bande>();
	}

	@Override
	public void initImage(Graphics g) {
		
		this.spritesheet = g.load(SPRITE_PATH);
		Random rand=new Random();
		int n=rand.nextInt(5);
		if(n==0) {
			for (int i = 0; i < 51; i++) {
				BufferedImage img = spritesheet.getSubimage(i * size_bande, 0, size_bande, 320);
				l.add(new Bande(new PixelCoordinate(i* size_bande,0), img));
			}
		}
		
	}

	@Override
	public void paint(Graphics g) {
		if (spritesheet == null || l == null) {
			initImage(g);
		}
		
		for(Bande b:l) {
			if(!b.change) {
				
				g.drawImage(b.img, b.coord.x/2, (int)entity.center().y(), b.size_x, 120);
			}
		}
	}

	@Override
	public void updateAnimation(double delta_t) {
		// pas d'animation

	}

}
