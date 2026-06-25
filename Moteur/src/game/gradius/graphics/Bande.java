package game.gradius.graphics;

import engine.geometry.ISU;
import engine.graphics.hud.PixelCoordinate;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;

public class Bande {
	PixelCoordinate coord;
	boolean change,isInVP;
	final int size_x=10;
	BufferedImage img;
	
	public Bande(PixelCoordinate x_deb,BufferedImage img) {
		this.coord=x_deb;
		change=false;
		this.img=img;
	}
	
	
	public void change_img(BufferedImage img){
		this.img=img;
	}
}
