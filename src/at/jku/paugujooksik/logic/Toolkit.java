package at.jku.paugujooksik.logic;

import static at.jku.paugujooksik.gui.ResourceLoader.BOLD_SANS_FONT;
import static at.jku.paugujooksik.gui.ResourceLoader.SANS_FONT;
import static at.jku.paugujooksik.gui.ResourceLoader.loadFonts;

import java.awt.Font;

public class Toolkit {
	public static final Font DEFAULT_FONT;
	public static final Font DEFAULT_FONT_BOLD;
	
	static {
		if (loadFonts()) {
			DEFAULT_FONT = new Font(SANS_FONT, Font.PLAIN, 16);
			DEFAULT_FONT_BOLD = new Font(BOLD_SANS_FONT, Font.BOLD, 16);
		} else {
			DEFAULT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
			DEFAULT_FONT_BOLD = new Font(Font.SANS_SERIF, Font.BOLD, 16);
		}
	}
}
