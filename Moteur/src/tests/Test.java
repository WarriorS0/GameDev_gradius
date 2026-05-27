package tests;

import java.io.PrintStream;

import game.Game;

public class Test {
	
	private PrintStream ps;

	private Test(){
		this.ps = System.out;
	}
	
	public static void main(String[] args) {
		Test test =new Test();
		int w_cell = 12;
		int h_cell = 12;
		Game game = new Game(w_cell, h_cell);
		game.show(test.ps);
	}
}
