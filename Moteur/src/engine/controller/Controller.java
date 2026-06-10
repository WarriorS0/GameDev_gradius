package engine.controller;

import game.move.Stunt;
import oop.graphics.Canvas;
import oop.graphics.VirtualKeyCodes;

public class Controller {
	private Canvas canvas;
	private Stunt stunt;

	public Controller(Canvas canvas, Stunt stunt) {
		this.canvas = canvas;
		this.stunt = stunt;
		canvas.set(new Canvas.KeyListener() {

			@Override
			public void pressed(Canvas canvas, int keyCode, char keyChar) {
				if (keyCode == VirtualKeyCodes.VK_LEFT) {
					stunt.set(180);
				} else if (keyCode == VirtualKeyCodes.VK_UP) {
					stunt.set(270);
				} else if (keyCode == VirtualKeyCodes.VK_RIGHT) {
					stunt.set(0);
				} else if (keyCode == VirtualKeyCodes.VK_DOWN) {
					stunt.set(90);
				}

			}

			@Override
			public void released(Canvas canvas, int keyCode, char keyChar) {
				if (keyCode == VirtualKeyCodes.VK_LEFT) {

				} else if (keyCode == VirtualKeyCodes.VK_UP) {

				} else if (keyCode == VirtualKeyCodes.VK_RIGHT) {

				} else if (keyCode == VirtualKeyCodes.VK_DOWN) {

				}

			}

			@Override
			public void typed(Canvas canvas, char keyChar) {
				// TODO Auto-generated method stub

			}

		});
	}

}
