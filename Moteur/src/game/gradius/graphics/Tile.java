package game.gradius.graphics;

import java.util.ArrayList;
import java.util.List;

import engine.entity.Entity;
import engine.shape.Bounding;
import game.Game;
import oop.graphics.BufferedImage;

public abstract class Tile extends Entity{
	List<Bande> l;
	List<Bounding>b;
	BufferedImage spritesheet;
	int index=0;
	private int taille_x;
	protected final int size_bande=10;
	
	public Tile(int max_x) {
		super("Tile");
		l=new ArrayList<Bande>();
		b=new ArrayList<Bounding>();
		this.taille_x=max_x;
	}
	
	public void set_entity_y(int y) {
		super.place(Game.game().isu.new Coord(0,y));
	}
}
