package at.jku.paugujooksik.gui;

import java.util.logging.Logger;

import javax.swing.JFrame;

import at.jku.paugujooksik.logic.Cards;
import at.jku.paugujooksik.logic.Configuration;
import at.jku.paugujooksik.logic.ValueGenerator;

public abstract class AbstractGUI {
	protected static final Logger DEBUGLOG = Logger.getLogger("DEBUG");
	protected static final boolean GROW_CARDS = false;
	
	protected final JFrame frame = new JFrame();
	protected final ValueGenerator values = new ValueGenerator();
	
	protected Configuration<?> config;
	protected Cards<?> cards;
	
	protected AbstractGUI() {
		config = Configuration.generateDefault();
		cards = new Cards<>(config.values, config.algorithmIndex);
	}
}
