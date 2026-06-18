package engine.graphics;

import java.util.function.Supplier;

import oop.graphics.Color;
import oop.graphics.Font;
import oop.graphics.Graphics;

public class Label implements HudElement {

	private final static String DEFAULT_FONT = "SansSerif";
	private final static int DEFAULT_FONT_SIZE = 16;
	private final static int DEFAULT_FONT_STYLE = Font.PLAIN;

	protected Supplier<String> text;
	private boolean visible;
	protected String fontName;
	protected int fontStyle;
	protected int fontSize;
	
	/**
	 * pixel coordinate
	 */
	protected int x, y;
	
	public Color color;

	public Label(String text, int x, int y, Color color) {
		this(text, x, y, true, DEFAULT_FONT, DEFAULT_FONT_SIZE, DEFAULT_FONT_STYLE, color);
	}

	public Label(Supplier<String> text, int x, int y, Color color) {
		this(text, x, y, true, DEFAULT_FONT, DEFAULT_FONT_SIZE, DEFAULT_FONT_STYLE, color);
	}

	public Label(String text, int x, int y, boolean visible, String fontName, int fontSize, int fontStyle,
			Color color) {
		this(() -> text, x, y, visible, fontName, fontSize, fontStyle, color);
	}

	public Label(Supplier<String> text, int x, int y, boolean visible, String fontName, int fontSize, int fontStyle,
			Color color) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.visible = visible;
		this.fontName = fontName;
		this.fontSize = fontSize;
		this.fontStyle = fontStyle;
		this.color = color;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.setFont(g.getFont(fontName, fontStyle, fontSize));

		g.drawString(text.get(), x, y);
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void setVisibility(boolean shown) {
		this.visible = shown;
	}

}
