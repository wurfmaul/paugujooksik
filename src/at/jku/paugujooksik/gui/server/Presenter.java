package at.jku.paugujooksik.gui.server;

import static at.jku.paugujooksik.tools.Constants.DISPLAY_DEVICES;
import static at.jku.paugujooksik.tools.Constants.PLAYER_COLORS;
import static at.jku.paugujooksik.tools.Constants.TITLE_FONT;
import static at.jku.paugujooksik.tools.Constants.USE_ANIMATION;
import static at.jku.paugujooksik.tools.ResourceLoader.ERROR_SND;
import static at.jku.paugujooksik.tools.ResourceLoader.loadClip;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;

import at.jku.paugujooksik.action.Action;
import at.jku.paugujooksik.gui.AnimationListener;
import at.jku.paugujooksik.gui.CardPanel;
import at.jku.paugujooksik.gui.CardSetContainerPanel;
import at.jku.paugujooksik.gui.CardSetHandler;
import at.jku.paugujooksik.gui.SelectionException;
import at.jku.paugujooksik.model.Configuration;

public class Presenter extends Window implements CardSetHandler {
	private static final long serialVersionUID = 8299211278767397214L;

	public final Configuration<?> config;
	
	private final Map<String, Player> players = new LinkedHashMap<>();
	private final Set<JWindow> playerWindows = new LinkedHashSet<>();
	private final Set<String> registeredClients;
	private final int playerCount;
	private JFrame frame;

	/**
	 * Create the application.
	 */
	public Presenter(Frame owner, Configuration<?> config,
			Set<String> registeredClients, int deviceIndex) {
		super(owner);
		this.config = config;
		this.registeredClients = registeredClients;
		this.playerCount = registeredClients.size();

		initialize(deviceIndex);
	}

	public void incErrorCount(String clientId) {
		Player curPlayers = players.get(clientId);
		curPlayers.cards.incErrorCount();
		curPlayers.updateStats();

		Clip clip = loadClip(ERROR_SND);
		if (clip != null)
			clip.start();
	}

	public void queueAction(String clientId, Action action) {
		players.get(clientId).queueAction(clientId, action);
	}

	public void quit() {
		for (JWindow window : playerWindows) {
			window.dispose();
		}
		frame.dispose();
	}

	public void unregister(String clientId) {
		players.get(clientId).panel.setTitle(clientId + " (gone)");
	}

	@Override
	public boolean isProcessing(String clientId) {
		return players.get(clientId).animating;
	}

	@Override
	public void performPin(String clientId, int index) {
		final Player curPlayer = players.get(clientId);
		try {
			curPlayer.cards.togglePin(index);
		} catch (SelectionException ex) {
		}
		curPlayer.updateComponents();
	}

	@Override
	public void performMark(String clientId, int index) {
		final Player curPlayer = players.get(clientId);
		try {
			curPlayer.cards.toggleMark(index);
		} catch (SelectionException ex) {
		}
		curPlayer.updateComponents();
	}

	@Override
	public void performSelect(String clientId, int index) {
		final Player curPlayer = players.get(clientId);
		try {
			curPlayer.cards.select(index);
		} catch (SelectionException ex) {
		}
		curPlayer.updateStats();
		curPlayer.updateComponents();
	}

	@Override
	public void performSwapStart(String clientId) {
		final Player curPlayer = players.get(clientId);
		try {
			curPlayer.cards.swapSelection();
			curPlayer.updateStats();

			int leftIndex = curPlayer.cards.getFirstSelectedIndex();
			int rightIndex = curPlayer.cards.getSecondSelectedIndex();

			// FIXME bring the cards in between to back
			// pnlCards.cardSet.set(size, this, clientId, true, rightIndex);

			CardPanel btnLeft = curPlayer.panel.cardSet.get(leftIndex);
			CardPanel btnRight = curPlayer.panel.cardSet.get(rightIndex);
			if (USE_ANIMATION)
				new AnimationListener(btnLeft, btnRight, this, clientId)
						.start();
			else
				performSwapStop(clientId);
			curPlayer.animating = true;
		} catch (SelectionException ex) {
		}
	}

	@Override
	public void performSwapStop(String clientId) {
		Player curPlayer = players.get(clientId);
		curPlayer.updateComponents();
		curPlayer.animating = false;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(int deviceIndex) {
		int xOffset = 0;
		int yOffset = 60;
		if (deviceIndex != 0) {
			for (int i = 0; i < deviceIndex; i++)
				xOffset += DISPLAY_DEVICES[i].getDisplayMode().getWidth();
		}
	
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { yOffset, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);
	
		JLabel lblConfig = new JLabel(config.toString());
		lblConfig.setFont(TITLE_FONT);
		GridBagConstraints gbcLblConfig = new GridBagConstraints();
		gbcLblConfig.insets = new Insets(10, 10, 10, 10);
		gbcLblConfig.gridx = 0;
		gbcLblConfig.gridy = 0;
		frame.getContentPane().add(lblConfig, gbcLblConfig);
	
		// init background
		frame.setUndecorated(true);
		frame.setVisible(true);
		frame.setLocation(xOffset, 0);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
	
		// init players
		final int displayWidth = DISPLAY_DEVICES[deviceIndex].getDisplayMode()
				.getWidth();
		final int displayHeight = DISPLAY_DEVICES[deviceIndex].getDisplayMode()
				.getHeight();
		final int inset = 10;
		final int maxHeight = (displayHeight - yOffset) / playerCount;
	
		int curRow = 1;
		for (String name : registeredClients) {
			CardSetContainerPanel cardSetPanel = new CardSetContainerPanel(
					this, config.size, name, true, false);
			cardSetPanel.setBackground(PLAYER_COLORS[curRow - 1]);
			cardSetPanel.setTitle("Team '" + name + "'");
			players.put(name, new Player(cardSetPanel, this));
	
			JWindow window = new JWindow();
			{
				window.add(cardSetPanel);
				int x = xOffset + inset;
				int y = yOffset + (curRow - 1) * maxHeight;
				int width = displayWidth - 2 * inset;
				int height = maxHeight - inset;
				window.setBounds(x, y, width, height);
				window.setVisible(true);
				playerWindows.add(window);
			}
			curRow++;
		}
	}
}