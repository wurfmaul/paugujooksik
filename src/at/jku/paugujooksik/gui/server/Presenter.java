package at.jku.paugujooksik.gui.server;

import static at.jku.paugujooksik.tools.Constants.DISPLAY_DEVICES;
import static at.jku.paugujooksik.tools.Constants.PLAYER_COLORS;
import static at.jku.paugujooksik.tools.Constants.TITLE_FONT;
import static at.jku.paugujooksik.tools.Constants.USE_ANIMATION;
import static at.jku.paugujooksik.tools.ResourceLoader.ERROR_SND;
import static at.jku.paugujooksik.tools.ResourceLoader.loadClip;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Window;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.border.EmptyBorder;

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
		final int titleHeight = 60;
		final Rectangle display = DISPLAY_DEVICES[deviceIndex]
				.getDefaultConfiguration().getBounds();

		// init background
		JLabel lblConfig = new JLabel(config.toString());
		lblConfig.setFont(TITLE_FONT);
		lblConfig.setBorder(new EmptyBorder(10, 10, 10, 10));

		frame = new JFrame("Presenter window");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(display); //.x, display.y, display.width, titleHeight);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(lblConfig, BorderLayout.NORTH);
		frame.setUndecorated(true);
		frame.setVisible(true);

		// init players
		final int maxHeight = (display.height - titleHeight) / playerCount;
		final int inset = 10;
		int curRow = 0;

		final GridBagLayout gridBagLayout = new GridBagLayout();
		{
			gridBagLayout.columnWidths = new int[] { 0, 0 };
			gridBagLayout.rowHeights = new int[] { 0, 0 };
			gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
			gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		}

		final GridBagConstraints gridBagConstraints = new GridBagConstraints();
		{
			gridBagConstraints.insets = new Insets(0, inset, inset, inset);
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
		}

		for (String name : registeredClients) {
			final CardSetContainerPanel cardSetPanel = new CardSetContainerPanel(
					this, config.size, name, true, false);
			cardSetPanel.setBackground(PLAYER_COLORS[curRow]);
			cardSetPanel.setTitle(name);
			players.put(name, new Player(cardSetPanel, this));

			final int x = display.x;
			final int y = display.y + titleHeight + curRow * maxHeight;
			final int width = display.width;
			final int height = maxHeight;

			final JWindow borderWindow = new JWindow(frame);
			borderWindow.setBounds(x, y, width, height);
			borderWindow.getContentPane().setLayout(gridBagLayout);
			borderWindow.getContentPane().add(cardSetPanel, gridBagConstraints);
			borderWindow.setAlwaysOnTop(true);
			borderWindow.setVisible(true);

			curRow++;
		}
	}
}