package game;

import game.entity.PacMan;
import game.entity.Gum;

public class Main {

	public static void main(String args[]) {
		Game game = new Game(12, 12);

		PacMan p = new PacMan();
		Gum g = new Gum();

		g.place(game.grid.new Position(10, 10));

		System.out.println(p.intersects(g));
	}
}
