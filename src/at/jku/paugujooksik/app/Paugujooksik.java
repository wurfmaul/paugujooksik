package at.jku.paugujooksik.app;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.jku.paugujooksik.gui.client.ClientGUI;
import at.jku.paugujooksik.gui.server.ServerGUI;

public class Paugujooksik {
	private static final Logger DEBUGLOG = Logger.getLogger("DEBUG");
	private static final Level LOGLEVEL = Level.CONFIG;

	public static void main(String[] args) {
		configureLoggers();
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("--server")) {
				DEBUGLOG.config("Loading Server mode...");
				ServerGUI.initAndRun();
			} else if (args[0].equalsIgnoreCase("--client")) {
				DEBUGLOG.config("Loading Client mode...");
				ClientGUI.initAndRun(true);
			}
		} else {
			DEBUGLOG.config("Loading Standalone mode...");
			ClientGUI.initAndRun(false);
		}
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
