package at.jku.paugujooksik.gui.client;

import static at.jku.paugujooksik.tools.Constants.DEBUGLOG;
import static at.jku.paugujooksik.tools.Constants.HINT_FONT;
import static at.jku.paugujooksik.tools.Constants.INSET;
import static at.jku.paugujooksik.tools.Constants.MAX_SIZE;
import static at.jku.paugujooksik.tools.Constants.MIN_SIZE;
import static at.jku.paugujooksik.tools.Constants.NEGATIVE_HINT_COLOR;
import static at.jku.paugujooksik.tools.Constants.POSITIVE_HINT_COLOR;
import static at.jku.paugujooksik.tools.Constants.USE_ANIMATION;
import static at.jku.paugujooksik.tools.ResourceLoader.ERROR_SND;
import static at.jku.paugujooksik.tools.ResourceLoader.loadClip;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.sound.sampled.Clip;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import at.jku.paugujooksik.action.Action;
import at.jku.paugujooksik.gui.AnimationListener;
import at.jku.paugujooksik.gui.CardPanel;
import at.jku.paugujooksik.gui.CardSetContainerPanel;
import at.jku.paugujooksik.gui.CardSetHandler;
import at.jku.paugujooksik.gui.SelectionException;
import at.jku.paugujooksik.model.Cards;
import at.jku.paugujooksik.model.Configuration;
import at.jku.paugujooksik.model.ValueGenerator.ValueMode;
import at.jku.paugujooksik.model.ValueGenerator.ValueType;
import at.jku.paugujooksik.server.ServerControl;
import at.jku.paugujooksik.sort.SortAlgorithm;

public class ClientGUI implements CardSetHandler {
	private final boolean clientMode;
	private final JFrame frame = new JFrame();
	private boolean animating;
	private CardSetContainerPanel pnlCards;
	private SwapPanel pnlSwap;
	private JLabel lblHint;
	private Configuration<?> config;
	private String name;
	private ServerControl controler;
	private Cards<?> cards;
	private int size;

	private ClientGUI(boolean clientMode) {
		this.clientMode = clientMode;

		if (clientMode) {
			ConnectionDialog.init(frame, this);
			try {
				config = controler.getConfig();
			} catch (RemoteException | NullPointerException e) {
				quit();
			}
		} else {
			config = Configuration.generateDefault();
		}
		
		if (config != null) {
		size = config.size;
		cards = new Cards<>(config);
		initFrame();
		initialize();
		}
	}

	public int getLeftReference() {
		final JComponent comp = pnlCards.cardSet.get(cards
				.getFirstSelectedIndex());
		return comp.getLocation().x + comp.getWidth() / 2 + INSET;
	}

	public String getName() {
		return name;
	}

	public int getRightReference() {
		final Component comp = pnlCards.cardSet.get(cards
				.getSecondSelectedIndex());
		return comp.getLocation().x + comp.getWidth() / 2 + INSET;
	}

	public void quit() {
		DEBUGLOG.info("Exiting game...");
		if (controler != null) {
			try {
				controler.unregister(name);
				UnicastRemoteObject.unexportObject(controler, true);
			} catch (RemoteException e) {
			}
		}
		frame.dispose();
		System.exit(0);
	}

	public void setController(ServerControl controller) {
		this.controler = controller;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean showSwapButton() {
		return cards.twoSelected();
	}

	@Override
	public boolean isProcessing(String clientId) {
		return animating;
	}

	@Override
	public void performMark(String clientId, int index) {
		try {
			Action action = cards.toggleMark(index);
			clearErrorMessage();
			reportActionToPresenter(action);
		} catch (SelectionException ex) {
			showErrorMessage(ex);
		}
		updateAllComponents();
	}

	@Override
	public void performPin(String clientId, int index) {
		if (cards.getCard(index).selected) {
			try {
				Action action = cards.togglePin(index);
				clearErrorMessage();
				reportActionToPresenter(action);
			} catch (SelectionException e) {
				showErrorMessage(e);
			}
			updateAllComponents();
		}
	}

	@Override
	public void performSelect(String clientId, int index) {
		try {
			Action action = cards.select(index);
			updateStats();
			clearErrorMessage();
			reportActionToPresenter(action);
		} catch (SelectionException ex) {
			showErrorMessage(ex);
		}
		updateAllComponents();
	}

	@Override
	public void performSwapStart(String clientId) {
		try {
			Action action = cards.swapSelection();
			reportActionToPresenter(action);
			updateStats();

			int leftIndex = cards.getFirstSelectedIndex();
			int rightIndex = cards.getSecondSelectedIndex();
			CardPanel cardLeft = pnlCards.cardSet.get(leftIndex);
			CardPanel cardRight = pnlCards.cardSet.get(rightIndex);

			if (USE_ANIMATION)
				new AnimationListener(cardLeft, cardRight, this, clientId)
						.start();
			else
				performSwapStop(clientId);
			animating = true;
		} catch (SelectionException ex) {
			showErrorMessage(ex);
		}
	}

	@Override
	public void performSwapStop(String clientId) {
		pnlCards.cardSet.updateCards(cards);
		animating = false;
	}

	private void clearErrorMessage() {
		lblHint.setText("");
	}

	private void displayErrorMessageDialog() {
		DEBUGLOG.severe("Connection to server lost!");
		JOptionPane.showMessageDialog(frame,
				"Connection to server lost! Shutting down.", "Network error",
				JOptionPane.ERROR_MESSAGE);
		quit();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame.getContentPane().removeAll();

		pnlCards = new CardSetContainerPanel(this, size, name, false, true);
		{
			pnlCards.setTitle(config.toString());
			GridBagConstraints gbcPnlCards = new GridBagConstraints();
			gbcPnlCards.fill = GridBagConstraints.BOTH;
			gbcPnlCards.insets = new Insets(5, 5, 5, 5);
			gbcPnlCards.gridx = 0;
			gbcPnlCards.gridy = 0;
			frame.getContentPane().add(pnlCards, gbcPnlCards);
		}

		pnlSwap = new SwapPanel(this, name);
		{
			GridBagConstraints gbcPnlLines = new GridBagConstraints();
			gbcPnlLines.insets = new Insets(0, 0, 5, 0);
			gbcPnlLines.fill = GridBagConstraints.BOTH;
			gbcPnlLines.gridx = 0;
			gbcPnlLines.gridy = 1;
			frame.getContentPane().add(pnlSwap, gbcPnlLines);
		}

		lblHint = new JLabel();
		{
			lblHint.setFont(HINT_FONT);
			lblHint.setHorizontalAlignment(SwingConstants.CENTER);
			lblHint.setVerticalAlignment(SwingConstants.TOP);
			GridBagConstraints gbcLblHint = new GridBagConstraints();
			gbcLblHint.insets = new Insets(0, 5, 5, 5);
			gbcLblHint.fill = GridBagConstraints.BOTH;
			gbcLblHint.gridx = 0;
			gbcLblHint.gridy = 2;
			frame.getContentPane().add(lblHint, gbcLblHint);
		}

		initMenuBar();
		updateStats();
	}

	private void initCards() {
		size = config.size;
		cards = new Cards<>(config);
		pnlCards.cardSet.set(size, this, name, true);
	}

	private void initFrame() {
		frame.setBounds(100, 100, 700, 450);
		frame.setMinimumSize(new Dimension(500, 450));
		frame.setTitle("Sort the Cards!");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				quit();
			}
		});
		GridBagLayout gblFrame = new GridBagLayout();
		gblFrame.columnWidths = new int[] { 0, 0 };
		gblFrame.rowHeights = new int[] { 260, 90, 0, 0 };
		gblFrame.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gblFrame.rowWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		frame.getContentPane().setLayout(gblFrame);
	}

	private void initMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu mnFile = new JMenu("File");
		{
			JMenuItem mntmRestart = new JMenuItem("Restart");
			mntmRestart.setEnabled(!clientMode);
			mntmRestart.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					DEBUGLOG.fine("Restarting game");
					initialize();
					reset();
				}
			});
			mnFile.add(mntmRestart);

			JMenuItem mntmExit = new JMenuItem("Exit");
			mntmExit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					quit();
				}
			});
			mnFile.add(mntmExit);
		}
		menuBar.add(mnFile);

		JMenu mnConfig = new JMenu("Config");
		{
			mnConfig.setEnabled(!clientMode);

			ButtonGroup algorithmGroup = new ButtonGroup();
			int idx = 0;
			for (SortAlgorithm<?> sort : config.getAllAlgorithms()) {
				final int index = idx++;
				final JMenuItem item = new JRadioButtonMenuItem(sort.toString());
				item.setSelected(config.getAlgorithm().equals(sort));
				item.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						DEBUGLOG.config("Set algorithm to "
								+ config.getAllAlgorithms().get(index));

						config = Configuration.deriveWithNewSortIdx(config,
								index);
						initCards();
						reset();
					}
				});
				mnConfig.add(item);
				algorithmGroup.add(item);
			}

			mnConfig.add(new JSeparator());

			JMenu mnSize = new JMenu("Number of cards");
			{
				ButtonGroup sizeGroup = new ButtonGroup();
				for (int i = MIN_SIZE; i <= MAX_SIZE; i++) {
					final int newSize = i;
					JMenuItem item = new JRadioButtonMenuItem(
							Integer.toString(newSize));
					item.setSelected(i == size);
					item.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							DEBUGLOG.config("Changed size to " + newSize);
							config = Configuration.deriveWithNewSize(config,
									newSize);
							initCards();
							initialize();
						}
					});
					sizeGroup.add(item);
					mnSize.add(item);
				}
			}
			mnConfig.add(mnSize);

			JMenu mnType = new JMenu("Value type");
			{
				final ButtonGroup typeGroup = new ButtonGroup();
				final ValueType[] types = ValueType.values();

				for (int i = 0; i < types.length; i++) {
					final ValueType type = types[i];
					JMenuItem item = new JRadioButtonMenuItem(type.toString());
					item.setSelected(type == config.type);
					item.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							DEBUGLOG.config("Changed type to " + type);
							config = Configuration.deriveWithNewType(config,
									type);
							initCards();
							initialize();
						}
					});
					typeGroup.add(item);
					mnType.add(item);
				}
			}
			mnConfig.add(mnType);

			JMenu mnMode = new JMenu("Values");
			{
				final ButtonGroup kindGroup = new ButtonGroup();
				final ValueMode[] modes = ValueMode.values();

				for (int i = 0; i < modes.length; i++) {
					final ValueMode mode = modes[i];
					JMenuItem item = new JRadioButtonMenuItem(mode.toString());
					item.setSelected(mode == config.mode);
					item.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							DEBUGLOG.config("Changed mode to " + mode);
							config = Configuration.deriveWithNewMode(config,
									mode);
							initCards();
							initialize();
						}

					});
					kindGroup.add(item);
					mnMode.add(item);
				}
			}
			mnConfig.add(mnMode);
		}
		menuBar.add(mnConfig);
		frame.setJMenuBar(menuBar);
	}

	private void reportActionToPresenter(Action action) {
		if (clientMode) {
			try {
				controler.performAction(name, action);
			} catch (RemoteException | NullPointerException e) {
				displayErrorMessageDialog();
			}
		}
	}

	private void reportErrorToPresenter() {
		try {
			controler.incErrorCount(name);
		} catch (RemoteException | NullPointerException e) {
			displayErrorMessageDialog();
		}
	}

	private void reset() {
		clearErrorMessage();
		cards.reset(true);
		updateAllComponents();
		updateStats();
	}

	private void showErrorMessage(SelectionException e) {
		lblHint.setForeground(Color.RED);
		lblHint.setText(e.getMessage());

		if (!e.isDuplicate()) {
			if (!clientMode) {
				Clip clip = loadClip(ERROR_SND);
				if (clip != null)
					clip.start();
			}

			updateStats();
			if (clientMode)
				reportErrorToPresenter();
		}
	}

	private void updateAllComponents() {
		updateComponents();
		updateCardsIfFinished();
	}

	private void updateCardsIfFinished() {
		if (cards.allMarked()) {
			if (cards.isSorted()) {
				lblHint.setForeground(POSITIVE_HINT_COLOR);
				lblHint.setText("Congratulations!");
			} else {
				lblHint.setForeground(NEGATIVE_HINT_COLOR);
				lblHint.setText("There is something wrong!");
			}
			pnlSwap.removeAll();
			cards.selectAll();
			updateComponents();
			pnlCards.cardSet.finishCards(cards);
		}
	}

	private void updateComponents() {
		pnlCards.cardSet.updateCards(cards);
		pnlSwap.updateButton(cards.twoSelected());
		pnlCards.setTitle(config.toString());
	}

	private void updateStats() {
		pnlCards.setStats(cards.getCompareCount(), cards.getSwapCount(),
				cards.getErrorCount());
	}

	public static void initAndRun(final boolean remoteConfig) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (Exception e) {
				}
				try {
					ClientGUI window = new ClientGUI(remoteConfig);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
