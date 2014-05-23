package at.jku.paugujooksik.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import at.jku.paugujooksik.client.gui.ClientGUI;
import at.jku.paugujooksik.client.gui.ValueGenerator;

public class Client {
	private static final Logger DEBUG = Logger.getLogger("DEBUG");

	public static void main(String[] args) {
		DEBUG.setLevel(Level.ALL);
		ClientGUI.initAndRun(ValueGenerator.intValues(7));
	}
}
