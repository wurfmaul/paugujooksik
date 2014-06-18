package at.jku.paugujooksik.gui.server;

import static at.jku.paugujooksik.tools.Constants.PLAYER_COLORS;
import static at.jku.paugujooksik.tools.Constants.TITLE_FONT;
import static at.jku.paugujooksik.tools.ResourceLoader.ERROR_SND;
import static at.jku.paugujooksik.tools.ResourceLoader.loadClip;

import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

import at.jku.paugujooksik.action.BinaryAction;
import at.jku.paugujooksik.action.UnaryAction;
import at.jku.paugujooksik.gui.AnimationListener;
import at.jku.paugujooksik.gui.CardPanel;
import at.jku.paugujooksik.gui.CardSetContainerPanel;
import at.jku.paugujooksik.gui.CardSetHandler;
import at.jku.paugujooksik.gui.SelectionException;
import at.jku.paugujooksik.model.Cards;
import at.jku.paugujooksik.model.Configuration;

public class Presenter extends Window implements CardSetHandler {
	private static final long serialVersionUID = 8299211278767397214L;
	private static final Logger DEBUGLOG = Logger.getLogger("DEBUG");

	private final Map<String, Player> players = new LinkedHashMap<>();

	private JFrame frame;
	private Configuration<?> config;
	private Set<String> registeredClients;

	/**
	 * Create the application.
	 * 
	 * @param registeredClients
	 */
	public Presenter(Frame owner, Configuration<?> config,
			Set<String> registeredClients) {
		super(owner);
		this.config = config;
		this.registeredClients = registeredClients;
		initialize();
		setToFullScreen();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		final int rows = registeredClients.size();
		int curRow = 1;

		frame = new JFrame();
		frame.setBounds(100, 100, 600, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		int[] heights = new int[rows + 2];
		double[] weights = new double[rows + 2];
		{
			heights[0] = 60;
			Arrays.fill(weights, 1.0);
			weights[0] = 0.0;
			weights[rows + 1] = Double.MIN_VALUE;
		}
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = heights;
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = weights;
		frame.getContentPane().setLayout(gridBagLayout);

		JLabel lblConfig = new JLabel(config.toString());
		lblConfig.setFont(TITLE_FONT);
		GridBagConstraints gbcLblConfig = new GridBagConstraints();
		gbcLblConfig.insets = new Insets(10, 10, 10, 10);
		gbcLblConfig.gridx = 0;
		gbcLblConfig.gridy = 0;
		frame.getContentPane().add(lblConfig, gbcLblConfig);

		for (String name : registeredClients) {
			GridBagConstraints gbcPnlRow = new GridBagConstraints();
			gbcPnlRow.insets = new Insets(10, 10, 10, 10);
			gbcPnlRow.fill = GridBagConstraints.BOTH;
			gbcPnlRow.gridx = 0;
			gbcPnlRow.gridy = curRow;
			CardSetContainerPanel cardSetPanel = new CardSetContainerPanel(
					this, config.size, name, true, false);
			cardSetPanel.setBackground(PLAYER_COLORS[curRow - 1]);
			cardSetPanel.setTitle("Team '" + name + "'");
			players.put(name, new Player(cardSetPanel));
			frame.getContentPane().add(cardSetPanel, gbcPnlRow);
			curRow++;
		}
	}

	private void setToFullScreen() {
		// use secondary monitor if available
		GraphicsDevice[] devices = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getScreenDevices();
		GraphicsDevice monitor = devices.length > 1 ? devices[1] : devices[0];

		frame.dispose();
		frame.setUndecorated(true);
		frame.setVisible(true);
		monitor.setFullScreenWindow(frame);
	}

	public Configuration<?> getConfig() {
		return config;
	}

	public void incErrorCount(String clientId) {
		Player curPlayers = players.get(clientId);
		curPlayers.cards.incErrorCount();
		curPlayers.updateStats();

		Clip clip = loadClip(ERROR_SND);
		if (clip != null)
			clip.start();
	}

	public void performAction(String clientId, UnaryAction action) {
		switch (action.type) {
		case OPEN:
			performSelect(clientId, action.index);
			break;
		case MARK:
		case UNMARK:
			performMark(clientId, action.index);
			break;
		case PIN:
			performPin(clientId, action.index);
			break;
		default:
			DEBUGLOG.severe("Cannot perform action: '" + action + "'");
		}
	}

	public void performAction(String clientId, BinaryAction action) {
		final Player curPlayer = players.get(clientId);
		switch (action.type) {
		case OPEN:
			if (curPlayer.cards.isSelected(action.indexLeft))
				performSelect(clientId, action.indexRight);
			else
				performSelect(clientId, action.indexLeft);
			break;
		case SWAP:
			performSwap(clientId);
			break;
		default:
			DEBUGLOG.severe("Cannot perform action: '" + action + "'");
		}
	}

	public void quit() {
		frame.dispose();
	}

	public void unregister(String clientId) {
		players.get(clientId).panel.setTitle("<gone>");
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
	public void performSwap(String clientId) {
		final Player curPlayer = players.get(clientId);
		try {
			curPlayer.cards.swapSelection(true); // FIXME animation also here!

			int leftIndex = curPlayer.cards.getFirstSelectedIndex();
			int rightIndex = curPlayer.cards.getSecondSelectedIndex();

			// FIXME bring the cards in between to back
			// pnlCards.cardSet.set(size, this, clientId, true, rightIndex);

			CardPanel btnLeft = curPlayer.panel.cardSet.get(leftIndex);
			CardPanel btnRight = curPlayer.panel.cardSet.get(rightIndex);
			AnimationListener listener = new AnimationListener(btnLeft,
					btnRight, this, clientId);
			Timer timer = new Timer(40, listener);
			listener.setTimer(timer);
			timer.start();
		} catch (SelectionException ex) {
		}
		curPlayer.updateStats();
		curPlayer.updateComponents();
	}

	@Override
	public void finishSwap(String clientId) {
		final Player curPlayer = players.get(clientId);
		try {
			curPlayer.cards.swapSelection(false);
		} catch (SelectionException ex) {
		}
		curPlayer.updateStats();
		curPlayer.updateComponents();
	}

	private class Player {
		public final Cards<?> cards;
		public final CardSetContainerPanel panel;

		public Player(CardSetContainerPanel panel) {
			this.cards = new Cards<>(config);
			this.panel = panel;
		}

		public void updateComponents() {
			panel.cardSet.updateCards(cards);
			if (cards.allMarked()) {
				cards.selectAll();
				panel.cardSet.finishCards(cards);
			}
		}

		public void updateStats() {
			panel.setStats(cards.getCompareCount(), cards.getSwapCount(),
					cards.getErrorCount());
		}
	}
}