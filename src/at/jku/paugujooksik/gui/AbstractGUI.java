package at.jku.paugujooksik.gui;

import static at.jku.paugujooksik.gui.ResourceLoader.BOLD_SANS_FONT;
import static at.jku.paugujooksik.gui.ResourceLoader.SANS_FONT;
import static at.jku.paugujooksik.gui.ResourceLoader.loadFonts;

import java.awt.Font;
import java.util.logging.Logger;

import javax.swing.JFrame;

import at.jku.paugujooksik.logic.Cards;
import at.jku.paugujooksik.logic.Configuration;
import at.jku.paugujooksik.logic.ValueGenerator;

public abstract class AbstractGUI {
	protected static final Logger DEBUGLOG = Logger.getLogger("DEBUG");
	protected static final Font DEFAULT_FONT;
	protected static final Font DEFAULT_FONT_BOLD;
	protected static final boolean GROW_CARDS = false;
	
	protected final JFrame frame = new JFrame();
	protected final ValueGenerator values = new ValueGenerator();
	
	protected Configuration<?> config;
	protected Cards<?> cards;
	
	static {
		if (loadFonts()) {
			DEFAULT_FONT = new Font(SANS_FONT, Font.PLAIN, 16);
			DEFAULT_FONT_BOLD = new Font(BOLD_SANS_FONT, Font.BOLD, 16);
		} else {
			DEFAULT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
			DEFAULT_FONT_BOLD = new Font(Font.SANS_SERIF, Font.BOLD, 16);
		}
	}
	
	protected AbstractGUI() {
		config = Configuration.generateDefault();
		cards = new Cards<>(config.values, config.algorithmIndex);
	}
}
