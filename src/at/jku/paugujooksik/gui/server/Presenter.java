package at.jku.paugujooksik.gui.server;

import static at.jku.paugujooksik.tools.Constants.DEBUGLOG;
import static at.jku.paugujooksik.tools.Constants.DISPLAY_DEVICES;
import static at.jku.paugujooksik.tools.Constants.PLAYER_COLORS;
import static at.jku.paugujooksik.tools.Constants.TITLE_FONT;
import static at.jku.paugujooksik.tools.Constants.USE_ANIMATION;
import static at.jku.paugujooksik.tools.ResourceLoader.ERROR_SND;
import static at.jku.paugujooksik.tools.ResourceLoader.FULLSCREEN_ICON;
import static at.jku.paugujooksik.tools.ResourceLoader.loadClip;
import static at.jku.paugujooksik.tools.ResourceLoader.loadIcon;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.WindowConstants;

import at.jku.paugujooksik.gui.Animator;
import at.jku.paugujooksik.gui.Card;
import at.jku.paugujooksik.gui.CardSetContainer;
import at.jku.paugujooksik.gui.PresentationView;
import at.jku.paugujooksik.model.Action;
import at.jku.paugujooksik.model.Configuration;
import at.jku.paugujooksik.model.SelectionException;

/**
 * This represents the server's presenter window.
 * 
 * @author Wolfgang Kuellinger (0955711), 2014
 */
public class Presenter extends Window implements PresentationView {
	private static final long serialVersionUID = 8299211278767397214L;

	/** The basic configuration of the game parameters. */
	public final Configuration<?> config;

	/** A mapping of player by its name. */
	private final Map<String, Player> players = new LinkedHashMap<>();
	/** A set of all the clients that have registered at the server. */
	private final Set<String> registeredClients;

	private JFrame frame;

	/**
	 * Create a new presenter object.
	 * 
	 * @param owner
	 *            The owning frame, basically the server GUI
	 * @param config
	 *            See {@link #config}.
	 * @param registeredClients
	 *            See {@link #registeredClients}.
	 * @param deviceIndex
	 *            The index of the used display.
	 */
	public Presenter(Frame owner, Configuration<?> config, Set<String> registeredClients, int deviceIndex) {
		super(owner);
		this.config = config;
		this.registeredClients = registeredClients;

		initialize(deviceIndex, true);
	}

	/**
	 * Increment the error counter of the specified player.
	 * 
	 * @param clientId
	 *            The player who's error counter is to be incremented.
	 */
	public void incErrorCount(String clientId) {
		Player curPlayers = players.get(clientId);
		curPlayers.cardModel.incErrorCount();
		curPlayers.synchronizeStats();

		try {
			// play a sound
			final Clip clip = loadClip(ERROR_SND);
			if (clip != null)
				clip.start();
		} catch (Exception e) {
			DEBUGLOG.warning(e.getMessage());
		}
	}

	/**
	 * Perform an action. Basically a delegation to the player's queue.
	 * 
	 * @param clientId
	 *            The players name.
	 * @param action
	 *            The performed action.
	 */
	public void queueAction(String clientId, Action action) {
		players.get(clientId).queueAction(clientId, action);
	}

	/**
	 * Exit this application. On dispose, the owner becomes active again.
	 */
	public void quit() {
		frame.dispose();
	}

	/**
	 * Unregister a player from the presenter. Removes the player from the list.
	 * 
	 * @param clientId
	 *            The name of the player.
	 */
	public void unregister(String clientId) {
		players.get(clientId).getPanel().setTitle(clientId + " (gone)");
	}

	@Override
	public boolean isProcessing(String clientId) {
		return players.get(clientId).animating;
	}

	@Override
	public void performPin(String clientId, int index) {
		final Player curPlayer = players.get(clientId);
		try {
			curPlayer.cardModel.togglePin(index);
		} catch (SelectionException ex) {
		}
		curPlayer.synchronizeComponents();
	}

	@Override
	public void performMark(String clientId, int index) {
		final Player curPlayer = players.get(clientId);
		try {
			curPlayer.cardModel.toggleMark(index);
		} catch (SelectionException ex) {
		}
		curPlayer.synchronizeComponents();
	}

	@Override
	public void performSelect(String clientId, int index) {
		final Player curPlayer = players.get(clientId);
		try {
			curPlayer.cardModel.select(index);
		} catch (SelectionException ex) {
		}
		curPlayer.synchronizeStats();
		curPlayer.synchronizeComponents();
	}

	@Override
	public void performSwapStart(String clientId) {
		final Player curPlayer = players.get(clientId);
		try {
			curPlayer.cardModel.swapSelection();
			curPlayer.synchronizeStats();

			int leftIndex = curPlayer.cardModel.getFirstSelectedIndex();
			int rightIndex = curPlayer.cardModel.getSecondSelectedIndex();
			Card btnLeft = curPlayer.getPanel().cardSet.get(leftIndex);
			Card btnRight = curPlayer.getPanel().cardSet.get(rightIndex);

			if (USE_ANIMATION)
				new Animator(btnLeft, btnRight).start();
			else
				performSwapStop(clientId);
			curPlayer.animating = true;
		} catch (SelectionException ex) {
		}
	}

	@Override
	public void performSwapStop(String clientId) {
		Player curPlayer = players.get(clientId);
		curPlayer.animating = false;
		curPlayer.synchronizeComponents();
	}

	private void initialize(final int deviceIndex, boolean fullscreen) {
		// compute the bounds of the current display
		final Rectangle display = DISPLAY_DEVICES[deviceIndex].getDefaultConfiguration().getBounds();
		final int inset = 10;
		final Insets insets = new Insets(inset, inset, inset, inset);
		final int totalRows = registeredClients.size();

		frame = new JFrame("Presenter window");
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				quit();
			}
		});
		frame.setBounds(display);

		// init layout
		final GridBagLayout gridBagLayout = new GridBagLayout();
		{
			int[] colHeights = new int[totalRows + 2];
			double[] colWeights = new double[totalRows + 2];
			{
				colHeights[0] = 60;
				Arrays.fill(colWeights, 1.0);
				colWeights[0] = 0.0;
				colWeights[totalRows + 1] = Double.MIN_VALUE;
			}
			gridBagLayout.columnWidths = new int[] { 0, 60, 0 };
			gridBagLayout.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
			gridBagLayout.rowHeights = colHeights;
			gridBagLayout.rowWeights = colWeights;
		}
		frame.getContentPane().setLayout(gridBagLayout);

		// init title row
		JLabel lblConfig = new JLabel(config.toString());
		{
			lblConfig.setFont(TITLE_FONT);
			GridBagConstraints titleConstraints = new GridBagConstraints();
			{
				titleConstraints.insets = insets;
				titleConstraints.fill = GridBagConstraints.BOTH;
				titleConstraints.gridx = 0;
				titleConstraints.gridy = 0;
			}
			frame.getContentPane().add(lblConfig, titleConstraints);
		}

		JToggleButton btnFullscreen = new JToggleButton();
		{
			btnFullscreen.setIcon(loadIcon(FULLSCREEN_ICON));
			btnFullscreen.setSelected(fullscreen);
			btnFullscreen.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JToggleButton source = (JToggleButton) e.getSource();
					frame.dispose();
					initialize(deviceIndex, source.isSelected());
					frame.setBounds(display);
					for (Player p : players.values()) {
						p.synchronizeComponents();
						p.synchronizeStats();
					}
				}
			});

			GridBagConstraints constraints = new GridBagConstraints();
			{
				constraints.insets = insets;
				constraints.fill = GridBagConstraints.BOTH;
				constraints.gridx = 1;
				constraints.gridy = 0;
			}

			frame.getContentPane().add(btnFullscreen, constraints);
		}

		// init players
		int curRow = 0;

		for (String name : registeredClients) {
			final CardSetContainer cardSetPanel = new CardSetContainer(this, config.size, name, true, false);

			final GridBagConstraints gridBagConstraints = new GridBagConstraints();
			{
				gridBagConstraints.gridwidth = 2;
				gridBagConstraints.insets = new Insets(0, inset, inset, inset);
				gridBagConstraints.fill = GridBagConstraints.BOTH;
				gridBagConstraints.gridx = 0;
				gridBagConstraints.gridy = curRow + 1;
			}

			cardSetPanel.setBackground(PLAYER_COLORS[curRow % totalRows]);
			cardSetPanel.setTitle(name);

			// establish the mapping of the player's name to its delegate
			if (players.containsKey(name)) {
				players.get(name).setPanel(cardSetPanel);
			} else {
				players.put(name, new Player(cardSetPanel, this));
			}

			frame.getContentPane().add(cardSetPanel, gridBagConstraints);
			curRow++;
		}

		frame.setUndecorated(fullscreen);
		frame.setVisible(true);
	}
}