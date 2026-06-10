package engine.graphics;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import engine.Entity;
import engine.controller.Controller;
import game.Game;
import game.entity.Ghost;
import game.entity.Gum;
import game.entity.PacMan;
import game.entity.Wall;
import game.move.BasicStunt;
import game.move.GhostStunt;
import game.move.Model;
import game.move.PacManStunt;
import game.move.Ticker;
import oop.graphics.Canvas;
import oop.graphics.Graphics;
import oop.graphics.Graphics.Colors;
import oop.tasks.Runnable;
import oop.tasks.Runtime;
import oop.tasks.Task;

public class MainPaint implements Runnable {

	public static void main(String[] args) throws Exception {
		Runnable r = new MainPaint();
		Dimension tailleFenetre = new Dimension(1600, 900);
		Runtime.boot(tailleFenetre, r);
	}

	@Override
	public void run() throws Exception {
		Task task = Runtime.task();
		Canvas canvas = (Canvas) task.find("canvas");

		Game game = new Game(38, 41);
		Model model = new Model(game.grid);
		Ticker tick = new Ticker(model);

		PacMan p = new PacMan();
		p.setBounding();
		p.setPosition(game.grid.new Position(2, 2));

		Ghost blinky = new Ghost();
		blinky.setBounding();
		blinky.setPosition(game.grid.new Position(7, 7));

		Ghost pinky = new Ghost();
		pinky.setBounding();
		pinky.setPosition(game.grid.new Position(5, 11));

		Ghost inky = new Ghost();
		inky.setBounding();
		inky.setPosition(game.grid.new Position(11, 11));

		Ghost clyde = new Ghost();
		clyde.setBounding();
		clyde.setPosition(game.grid.new Position(15, 15));

		Ghost weak = new Ghost();
		weak.setBounding();
		weak.setPosition(game.grid.new Position(19, 19));

		Ghost weak_over = new Ghost();
		weak_over.setBounding();
		weak_over.setPosition(game.grid.new Position(10, 19));

		Ghost dead = new Ghost();
		dead.setBounding();
		dead.setPosition(game.grid.new Position(23, 23));

		PacMan p_dead = new PacMan();
		p_dead.setBounding();
		p_dead.setPosition(game.grid.new Position(10, 25));

		for (int x = 0; x < game.grid.width(); x++) {
			Wall wall = new Wall();
			wall.setBounding();
			wall.setPosition(game.grid.new Position(x, 1));
			model.add(wall);
			BasicStunt stunt_wall = new BasicStunt(model, wall);
			stunt_wall.set(game.isu.new Vector(0, 0));
			stunt_wall.set_aSpeed(0);
		}

		for (int y = 0; y < game.grid.height(); y++) {
			if (y != 19 && y != 18) {
				Wall wall = new Wall();
				wall.setBounding();
				wall.setPosition(game.grid.new Position(1, y));
				model.add(wall);
				BasicStunt stunt_wall = new BasicStunt(model, wall);
				stunt_wall.set(game.isu.new Vector(0, 0));
				stunt_wall.set_aSpeed(0);
			}

		}

		for (int y = 0; y < game.grid.height(); y++) {
			if (y != 19 && y != 18) {
				Wall wall = new Wall();
				wall.setBounding();
				wall.setPosition(game.grid.new Position(Game.width_ncell - 2, y));
				model.add(wall);
				BasicStunt stunt_wall = new BasicStunt(model, wall);
				stunt_wall.set(game.isu.new Vector(0, 0));
				stunt_wall.set_aSpeed(0);
			}
		}

		for (int x = 0; x < game.grid.width(); x++) {
			Wall wall = new Wall();
			wall.setBounding();
			wall.setPosition(game.grid.new Position(x, Game.height_ncell - 2));
			model.add(wall);
			BasicStunt stunt_wall = new BasicStunt(model, wall);
			stunt_wall.set(game.isu.new Vector(0, 0));
			stunt_wall.set_aSpeed(0);
		}

		model.add(p);
		PacManStunt stunt_p = new PacManStunt(model, p);
		stunt_p.set(game.isu.new Vector(20, 0));
		stunt_p.set_aSpeed(0);

		model.add(blinky);
		GhostStunt stunt_blinky = new GhostStunt(model, blinky);
		stunt_blinky.set(game.isu.new Vector(10, 0));
		stunt_blinky.set_aSpeed(50);

		model.add(pinky);
		GhostStunt stunt_pinky = new GhostStunt(model, pinky);
		stunt_pinky.set(game.isu.new Vector(10, 0));
		stunt_pinky.set_aSpeed(50);

		model.add(inky);
		GhostStunt stunt_inky = new GhostStunt(model, inky);
		stunt_inky.set(game.isu.new Vector(10, 0));
		stunt_inky.set_aSpeed(50);

		model.add(clyde);
		GhostStunt stunt_clyde = new GhostStunt(model, clyde);
		stunt_clyde.set(game.isu.new Vector(10, 0));
		stunt_clyde.set_aSpeed(50);

		model.add(weak);
		GhostStunt stunt_weak = new GhostStunt(model, weak);
		stunt_weak.set(game.isu.new Vector(10, 0));
		stunt_weak.set_aSpeed(50);

		model.add(weak_over);
		GhostStunt stunt_weak_over = new GhostStunt(model, weak_over);
		stunt_weak_over.set(game.isu.new Vector(10, 0));
		stunt_weak_over.set_aSpeed(50);

		model.add(dead);
		GhostStunt stunt_dead = new GhostStunt(model, dead);
		stunt_dead.set(game.isu.new Vector(10, 0));
		stunt_dead.set_aSpeed(50);

		model.add(p_dead);
		BasicStunt stunt_pdead = new BasicStunt(model, p_dead);
		stunt_pdead.set(game.isu.new Vector(0, 0));
		stunt_pdead.set_aSpeed(0);

		tick.run();

		View view = new View();
		PacmanAvatar av_pac = new PacmanAvatar(p);
		BlinkyAvatar av_blink = new BlinkyAvatar(blinky);
		PinkyAvatar av_pink = new PinkyAvatar(pinky);
		InkyAvatar av_ink = new InkyAvatar(inky);
		ClydeAvatar av_cly = new ClydeAvatar(clyde);
		ClydeAvatar av_weak = new ClydeAvatar(weak);
		av_weak.weak = true;
		ClydeAvatar av_weak_over = new ClydeAvatar(weak_over);
		av_weak_over.weak_over = true;
		ClydeAvatar av_dead = new ClydeAvatar(dead);
		av_dead.dead = true;
		Entity gum = new Gum();
		PacmanAvatar av_pdead = new PacmanAvatar(p_dead);
		av_pdead.dead = true;

		view.add(av_pac);
		view.add(av_blink);
		view.add(av_pink);
		view.add(av_ink);
		view.add(av_cly);
		view.add(av_weak);
		view.add(av_weak_over);
		view.add(av_dead);
		view.add(av_pdead);
		MapView mapView = new MapView();
		Paint paint = new Paint(canvas);

		canvas.set(new Canvas.PaintListener() {
			@Override
			public void visible(Canvas canvas) {
				paint.start();
			}

			@Override
			public void paint(Canvas canvas, Graphics g) {
				int w = canvas.getWidth();
				int h = canvas.getHeight();
				g.setColor(Colors.black);
				g.fillRect(0, 0, w, h);
				double totalWidthPixels = Game.width_ncell * Game.cmPerCell * Game.pixelPerCm;
				double totalHeightPixels = Game.height_ncell * Game.cmPerCell * Game.pixelPerCm;
				double scale = Math.min(w / totalWidthPixels, h / totalHeightPixels);
				int offsetX = (int) ((w - (totalWidthPixels * scale)) / 2);
				int offsetY = (int) ((h - (totalHeightPixels * scale)) / 2);
				g.translate(offsetX, offsetY);
				g.scale(scale, scale);
				mapView.paint(g);
				view.paint(g);
			}

			@Override
			public void revoked(Canvas canvas) {
			}
		});
		Controller control = new Controller(canvas, stunt_p);
	}
}