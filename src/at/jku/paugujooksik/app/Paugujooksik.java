package at.jku.paugujooksik.app;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.jku.paugujooksik.gui.ClientGUI;

public class Paugujooksik {
	private static final Logger DEBUGLOG = Logger.getLogger("DEBUG");
	private static final Level LOGLEVEL = Level.CONFIG;

	public static void main(String[] args) {
		configureLoggers();
		ClientGUI.initAndRun();
	}

	private static void configureLoggers() {
		// deactivate root logger
		final Logger rootLogger = Logger.getLogger("");
		for (Handler h : rootLogger.getHandlers()) {
			rootLogger.removeHandler(h);
		}

		final ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		DEBUGLOG.addHandler(handler);
		DEBUGLOG.setLevel(LOGLEVEL);
	}
}
