package game.gradius.graphics;

import java.util.ArrayList;
import java.util.List;

import engine.entity.Entity;
import engine.graphics.avatars.SpriteAvatar;
import engine.graphics.hud.PixelCoordinate;
import engine.move.ViewPort;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;

public class TileAvatar extends SpriteAvatar {
	private static final String SPRITE_PATH = "src/game/gradius/graphics/background.png";
	private final int size_bande = 10;
	private BufferedImage spritesheet;
	private List<Bande> l;
	private List<Bande>[] l2;
	private ViewPort vp;
	public List<Tile> tiles;
	private List<Bande> viewBande;

	public TileAvatar(Entity entity, ViewPort vp) {
		super(entity, SPRITE_PATH, 1, 1);
		this.vp = vp;
		l = new ArrayList<Bande>();
		l2 = new ArrayList[5];
		for (int i = 0; i < 5; i++) {
			l2[i] = new ArrayList<Bande>();
		}
		this.tiles = new ArrayList<>();
		this.viewBande = new ArrayList<>();
	}

	@Override
	public void initImage(Graphics g) {

		int floor = (int) vp.height_cm();
		this.spritesheet = g.load(SPRITE_PATH);
		BigTile bgt = new BigTile(size_bande, floor);
		for (int i = 0; i < 102; i++) {
			BufferedImage img = spritesheet.getSubimage(i * size_bande, 0, 2 * size_bande, 320);
			l2[0].add(new Bande(new PixelCoordinate(i * size_bande, 0), img));
		}
		bgt.l = l2[0];
		tiles.add(bgt);
		MediumTile mdt1 = new MediumTile(size_bande, floor);
		for (int i = 0; i < 32; i++) {
			BufferedImage img = spritesheet.getSubimage(10 + i * size_bande, 505, 2 * size_bande, 120);
			l2[1].add(new Bande(new PixelCoordinate(i * size_bande, 0), img));
		}
		mdt1.l = l2[1];
		MediumTile mdt2 = new MediumTile(size_bande, floor);
		for (int i = 0; i < 33; i++) {
			BufferedImage img = spritesheet.getSubimage(360 + i * size_bande, 505, 2 * size_bande, 120);
			l2[2].add(new Bande(new PixelCoordinate(i * size_bande, 0), img));
		}
		mdt2.l = l2[2];
		MediumTile mdt3 = new MediumTile(size_bande, floor);
		for (int i = 0; i < 32; i++) {
			BufferedImage img = spritesheet.getSubimage(715 + i * size_bande, 505, 2 * size_bande, 120);
			l2[3].add(new Bande(new PixelCoordinate(i * size_bande, 0), img));
		}
		mdt3.l = l2[3];
		FlatTile flt = new FlatTile(size_bande, floor);
		for (int i = 0; i < 32; i++) {
			BufferedImage img = spritesheet.getSubimage(40 + i * size_bande, 640, 2 * size_bande, 70);
			l2[4].add(new Bande(new PixelCoordinate(i * size_bande, 0), img));
		}
		flt.l = l2[4];

		tiles.add(bgt);
		tiles.add(mdt1);
		tiles.add(mdt2);
		tiles.add(mdt3);
		tiles.add(flt);
	}

	@Override
	public void paint(Graphics g) {
		if (spritesheet == null || l == null) {
			initImage(g);
		}
		int r = 1;
		l = l2[r];
		if (r == 4) {
			g.scale(1, 0.25);
		}
		for (Bande b : l) {
			int x = b.coord.x;
			int y = b.coord.y;
			if (!b.change) {
				boolean inVP = vp.isVisible(x, y);
				if (b.isInVP) {
					if (!inVP) {
						b.change = true;// on change la bande avec la tile courante (index de la bande de la tile
										// courante)
					}
					b.isInVP = inVP;
				}
				g.drawImage(b.img, b.coord.x / 2, (int) entity.center().y(), b.size_x, 120);
			}
		}
	}

	@Override
	public void updateAnimation(double delta_t) {
		// pas d'animation

	}

}
