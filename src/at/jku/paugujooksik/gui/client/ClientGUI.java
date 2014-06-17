package at.jku.paugujooksik.gui.client;

import static at.jku.paugujooksik.gui.ResourceLoader.ERROR_SND;
import static at.jku.paugujooksik.gui.ResourceLoader.loadClip;
import static at.jku.paugujooksik.logic.Configuration.MAX_SIZE;
import static at.jku.paugujooksik.logic.Configuration.MIN_SIZE;
import static at.jku.paugujooksik.logic.Toolkit.*;

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
import java.util.logging.Logger;

import javax.sound.sampled.Clip;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import at.jku.paugujooksik.action.Action;
import at.jku.paugujooksik.action.BinaryAction;
import at.jku.paugujooksik.action.UnaryAction;
import at.jku.paugujooksik.gui.CardSet;
import at.jku.paugujooksik.gui.CardSetHandler;
import at.jku.paugujooksik.gui.SelectionException;
import at.jku.paugujooksik.logic.Cards;
import at.jku.paugujooksik.logic.Configuration;
import at.jku.paugujooksik.logic.ValueGenerator.ValueMode;
import at.jku.paugujooksik.logic.ValueGenerator.ValueType;
import at.jku.paugujooksik.server.ServerControl;
import at.jku.paugujooksik.sort.SortAlgorithm;

public class ClientGUI implements CardSetHandler {
	private static final Logger DEBUGLOG = Logger.getLogger("DEBUG");

	private final boolean clientMode;
	private final JFrame frame = new JFrame();
	private final CardSet cardSet;

	private SwapPanel pnlSwap;
	private JTextPane txtStats;
	private JLabel lblTitle;
	private JTextPane txtHint;

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
			} catch (RemoteException e) {
			}
		} else {
			config = Configuration.generateDefault();
		}
		size = config.size;
		cards = new Cards<>(config);
		cardSet = new CardSet(size, this, name, true);
		initFrame();
		initialize();
	}

	private void reset() {
		cards.reset(true);
		checkComponents();
		updateStats();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame.getContentPane().removeAll();

		lblTitle = new JLabel(config.toString());
		lblTitle.setFont(TITLE_FONT);
		GridBagConstraints gbcLblTitle = new GridBagConstraints();
		gbcLblTitle.fill = GridBagConstraints.BOTH;
		gbcLblTitle.insets = new Insets(5, 5, 5, 5);
		gbcLblTitle.gridx = 0;
		gbcLblTitle.gridy = 0;
		frame.getContentPane().add(lblTitle, gbcLblTitle);

		txtStats = new JTextPane();
		txtStats.setFont(DEFAULT_FONT.deriveFont(24f));
		txtStats.setOpaque(false);
		txtStats.setFocusable(false);
		GridBagConstraints gbcLblCount = new GridBagConstraints();
		gbcLblCount.insets = new Insets(0, 0, 5, 0);
		gbcLblCount.fill = GridBagConstraints.BOTH;
		gbcLblCount.gridx = 1;
		gbcLblCount.gridy = 0;
		frame.getContentPane().add(txtStats, gbcLblCount);

		GridBagConstraints gbcPnLCards = new GridBagConstraints();
		gbcPnLCards.gridwidth = 2;
		gbcPnLCards.insets = new Insets(0, 0, 5, 0);
		gbcPnLCards.fill = GridBagConstraints.BOTH;
		gbcPnLCards.gridx = 0;
		gbcPnLCards.gridy = 1;
		frame.getContentPane().add(cardSet, gbcPnLCards);

		pnlSwap = new SwapPanel(this, name);
		GridBagConstraints gbcPnlLines = new GridBagConstraints();
		gbcPnlLines.gridwidth = 2;
		gbcPnlLines.insets = new Insets(0, 0, 5, 0);
		gbcPnlLines.fill = GridBagConstraints.BOTH;
		gbcPnlLines.gridx = 0;
		gbcPnlLines.gridy = 2;
		frame.getContentPane().add(pnlSwap, gbcPnlLines);

		JPanel pnlPlaceholder = new JPanel();
		pnlPlaceholder.setLayout(null);
		GridBagConstraints gbcPnlPlaceholder = new GridBagConstraints();
		gbcPnlPlaceholder.gridwidth = 2;
		gbcPnlPlaceholder.insets = new Insets(0, 0, 0, 0);
		gbcPnlPlaceholder.fill = GridBagConstraints.BOTH;
		gbcPnlPlaceholder.gridx = 0;
		gbcPnlPlaceholder.gridy = 3;
		frame.getContentPane().add(pnlPlaceholder, gbcPnlPlaceholder);

		txtHint = new JTextPane();
		txtHint.setFont(DEFAULT_FONT_BOLD.deriveFont(24f));
		txtHint.setOpaque(false);
		txtHint.setFocusable(false);
		GridBagConstraints gbcLblHint = new GridBagConstraints();
		gbcLblHint.gridwidth = 2;
		gbcLblHint.insets = new Insets(0, 5, 5, 5);
		gbcLblHint.fill = GridBagConstraints.BOTH;
		gbcLblHint.gridx = 0;
		gbcLblHint.gridy = 4;
		frame.getContentPane().add(txtHint, gbcLblHint);

		initMenuBar();
		updateStats();
	}

	private void initFrame() {
		frame.setBounds(100, 100, 700, 500);
		frame.setMinimumSize(new Dimension(500, 450));
		frame.setTitle("Sort the Cards!");
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		GridBagLayout gblFrame = new GridBagLayout();
		gblFrame.columnWidths = new int[] { 508, 190, 0 };
		gblFrame.rowHeights = new int[] { 90, 160, 90, 10, 90, 0 };
		gblFrame.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		gblFrame.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		frame.getContentPane().setLayout(gblFrame);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				quit();
			}
		});
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
						setupCards();
						reset();
						// FIXME when finished, reset fails (e.g. all stays
						// green)
					}
				});
				mnConfig.add(item);
				algorithmGroup.add(item);
			}

			mnConfig.add(new JSeparator());

			JMenu mnSize = new JMenu("Size");
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
							setupCards();
							initialize();
						}
					});
					sizeGroup.add(item);
					mnSize.add(item);
				}
			}
			mnConfig.add(mnSize);

			JMenu mnType = new JMenu("Type");
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
							setupCards();
							initialize();
						}
					});
					typeGroup.add(item);
					mnType.add(item);
				}
			}
			mnConfig.add(mnType);

			JMenu mnMode = new JMenu("Mode");
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
							setupCards();
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

	private void checkFinish() {
		if (cards.allMarked()) {
			if (cards.isSorted()) {
				txtHint.setForeground(Color.GREEN);
				txtHint.setText("Congratulations!");
			} else {
				txtHint.setForeground(Color.RED);
				txtHint.setText("There is something wrong!");
			}
			pnlSwap.removeAll();
			cards.selectAll();
			updateComponents();
			cardSet.finishCards(cards);
		}
	}

	private void checkComponents() {
		updateComponents();
		checkFinish();
	}

	private void clearErrors() {
		txtHint.setText("");
	}

	private void reportError(SelectionException ex) {
		txtHint.setForeground(Color.RED);
		txtHint.setText(ex.getMessage());

		Clip clip = loadClip(ERROR_SND);
		if (clip != null)
			clip.start();

		updateStats();
		if (clientMode)
			reportErrorToPresenter();
	}

	private void updateComponents() {
		cardSet.updateCards(cards);
		pnlSwap.showButton(cards.twoSelected());
		lblTitle.setText(config.toString());
		frame.repaint();
	}

	private void updateStats() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Errors: ");
		sb.append(cards.getErrorCount());
		sb.append(System.lineSeparator());
		sb.append("Swaps: ");
		sb.append(cards.getSwapCount());
		sb.append(System.lineSeparator());
		sb.append("Compares: ");
		sb.append(cards.getCompareCount());
		txtStats.setText(sb.toString());
	}

	private void setupCards() {
		size = config.size;
		cards = new Cards<>(config);
		cardSet.set(size, this, name, true);
	}

	private void reportErrorToPresenter() {
		try {
			controler.incErrorCount(name);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void reportActionToPresenter(Action action) {
		if (clientMode) {
			try { // TODO bad style
				if (action instanceof UnaryAction)
					controler.performAction(name, (UnaryAction) action);
				else if (action instanceof BinaryAction)
					controler.performAction(name, (BinaryAction) action);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void performPin(String originId, int index) {
		if (cards.getCard(index).selected) {
			try {
				Action action = cards.togglePin(index);
				clearErrors();
				reportActionToPresenter(action);
			} catch (SelectionException e) {
				reportError(e);
			}
			checkComponents();
		}
	}

	@Override
	public void performMark(String originId, int index) {
		try {
			Action action = cards.toggleMark(index);
			clearErrors();
			reportActionToPresenter(action);
		} catch (SelectionException ex) {
			reportError(ex);
		}
		checkComponents();
	}

	@Override
	public void performSelect(String originId, int index) {
		try {
			Action action = cards.select(index);
			updateStats();
			clearErrors();
			reportActionToPresenter(action);
		} catch (SelectionException ex) {
			reportError(ex);
		}
		checkComponents();
	}

	@Override
	public void performSwap(String originId) {
		try {
			Action action = cards.swapSelection();
			clearErrors();
			updateStats();
			reportActionToPresenter(action);
		} catch (SelectionException ex) {
			reportError(ex);
		}
		checkComponents();
	}

	public int getLeftReference() {
		final Component comp = cardSet.get(cards.getFirstSelectedIndex())
				.getParent();
		return comp.getLocation().x + comp.getWidth() / 2;
	}

	public int getRightReference() {
		final Component comp = cardSet.get(cards.getSecondSelectedIndex())
				.getParent();
		return comp.getLocation().x + comp.getWidth() / 2;
	}

	public String getName() {
		return name;
	}

	public boolean showSwapButton() {
		return cards.twoSelected();
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPres(ServerControl pres) {
		this.controler = pres;
	}

	public void quit() {
		DEBUGLOG.info("Exiting game...");
		try {
			controler.unregister(name);
		} catch (RemoteException | NullPointerException e) {
		}
		frame.dispose();
		System.exit(0);
	}

	/**
	 * Launch the application.
	 */
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
