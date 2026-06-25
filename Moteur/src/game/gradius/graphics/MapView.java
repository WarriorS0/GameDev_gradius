package game.gradius.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import game.Game;
import oop.graphics.BufferedImage;
import oop.graphics.Canvas;
import oop.graphics.Graphics;
import engine.move.Model;
import engine.move.ViewPort;

public class MapView {
	private List<Bande> bandes;
	private final int size_bande = 10;
	private Tile current;
	private Canvas c;
	private Model model;
	private TileAvatar tav;
	private ViewPort vp;
	private boolean isInitialized = false;
	private Random r;

	public MapView(Canvas c, Model m, TileAvatar tav, ViewPort vp) {
		this.c = c;
		this.model = m;
		this.tav = tav;
		this.vp = vp;
		this.r = new Random();
	}

	public void init(Graphics g) {
		tav.initImage(g);
		int length = c.getWidth() / size_bande;
		this.bandes = new ArrayList<>(length);
		while (bandes.size() < length) {
			int n = r.nextInt() % 5;
			if (n < 0) {
				n += 5;
			}
			Tile t = tav.tiles.get(n);
			int i = 0;
			while ((bandes.size() < length) && (i < t.l.size())) {
				bandes.add(t.l.get(i));
			}
			if (i == t.l.size()) {
				int n2 = r.nextInt() % 5; // sans cela la prochaine tile générée serait la même que la dernière tile
											// générée lors de l'initialisation
				if (n2 < 0)
					n2 += 5;
				current = tav.tiles.get(n2);
			} else {
				current = t;
				current.index = i + 1;
			}
		}
		isInitialized = true;

	}

	public void paint(Graphics g) {
		if (this.bandes == null || this.bandes.isEmpty()) {
			init(g);
		}

		Game game = Game.game();
		if (game == null) {
			return;
		}
		
		int start_x=(int)vp.originX();
		
		

	}
}