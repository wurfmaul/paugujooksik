package at.jku.paugujooksik.app;

import static at.jku.paugujooksik.tools.Constants.DEBUGLOG;
import at.jku.paugujooksik.gui.client.ClientGUI;
import at.jku.paugujooksik.gui.server.ServerGUI;

/**
 * Starts the application if a command line parameter was chosen, brings up ui
 * for choosing mode otherwise.
 * 
 * @author Wolfgang Kuellinger (0955711), 2014
 */
public class Paugujooksik {
	public static void main(String[] args) {
		if (args.length > 0) {
			switch (args[0]) {
			case "--server":
				DEBUGLOG.config("Loading Server mode...");
				ServerGUI.initAndRun();
				break;
			case "--client":
				DEBUGLOG.config("Loading Client mode...");
				ClientGUI.initAndRun(true);
				break;
			case "--alone":
				DEBUGLOG.config("Loading Standalone mode...");
				ClientGUI.initAndRun(false);
				break;
			default:
				DEBUGLOG.severe("Unknown mode: " + args[0]);
			}
		} else {
			new StarterDialog();
		}
	}
}
