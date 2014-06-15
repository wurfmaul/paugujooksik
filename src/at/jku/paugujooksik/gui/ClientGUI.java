package at.jku.paugujooksik.gui;

import static at.jku.paugujooksik.gui.ResourceLoader.ERROR_SND;
import static at.jku.paugujooksik.gui.ResourceLoader.loadClip;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

import at.jku.paugujooksik.logic.Cards;
import at.jku.paugujooksik.logic.ValueGenerator;
import at.jku.paugujooksik.logic.ValueGenerator.Mode;
import at.jku.paugujooksik.logic.ValueGenerator.Type;
import at.jku.paugujooksik.sort.SortAlgorithm;

public class ClientGUI extends AbstractGUI {
	private SwapPanel pnlSwap;
	private JTextPane txtStats;
	private JLabel lblTitle;
	private JTextPane txtHint;

	private ClientGUI() {
		setCards();
		initFrame();
		initialize();
	}

	private void reset() {
		cards.reset();
		checkComponents();
		updateStats();
	}

	protected void clearErrors() {
		txtHint.setText("");
	}

	protected void reportError(SelectionException ex) {
		txtHint.setForeground(Color.RED);
		txtHint.setText(ex.getMessage());

		Clip clip = loadClip(ERROR_SND);
		if (clip != null)
			clip.start();

		updateStats();
	}

	protected void checkComponents() {
		updateComponents();
		checkFinish();
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
			for (int i = 0; i < n; i++) {
				cardBtns.get(i).finish(!cards.isOnRightPosition(i));
			}
		}
	}

	private void updateComponents() {
		for (int i = 0; i < n; i++) {
			cardBtns.get(i).updateCard(cards.getCard(i));
		}

		pnlSwap.showButton(cards.twoSelected());
		lblTitle.setText(printConfig());
		frame.repaint();
	}

	private String printConfig() {
		final StringBuilder sb = new StringBuilder();
		sb.append(cards.sort.getCurrent().toString());
		sb.append(" (");
		sb.append(n);
		sb.append(" ");
		sb.append(values.mode);
		sb.append(" ");
		sb.append(values.type);
		sb.append(")");
		return sb.toString();
	}

	protected void updateStats() {
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

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame.getContentPane().removeAll();

		lblTitle = new JLabel(printConfig());
		lblTitle.setFont(DEFAULT_FONT.deriveFont(30f));
		GridBagConstraints gbcLblTitle = new GridBagConstraints();
		gbcLblTitle.fill = GridBagConstraints.BOTH;
		gbcLblTitle.insets = new Insets(0, 5, 5, 5);
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

		initCardPanel();

		pnlSwap = new SwapPanel(this);
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
		gbcPnlLines.gridwidth = 2;
		gbcPnlLines.insets = new Insets(0, 0, 0, 0);
		gbcPnlLines.fill = GridBagConstraints.BOTH;
		gbcPnlLines.gridx = 0;
		gbcPnlLines.gridy = 3;
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
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gblFrame = new GridBagLayout();
		gblFrame.columnWidths = new int[] { 508, 190, 0 };
		gblFrame.rowHeights = new int[] { 90, 160, 90, 10, 90, 0 };
		gblFrame.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		if (GROW_CARDS)
			gblFrame.rowWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 0.0,
					Double.MIN_VALUE };
		else
			gblFrame.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0,
					Double.MIN_VALUE };
		frame.getContentPane().setLayout(gblFrame);
	}

	private void initMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu mnFile = new JMenu("File");
		{
			JMenuItem mntmRestart = new JMenuItem("Restart");
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
					DEBUGLOG.info("Exiting game");
					frame.dispose();
				}
			});
			mnFile.add(mntmExit);
		}
		menuBar.add(mnFile);

		JMenu mnConfig = new JMenu("Config");
		{
			ButtonGroup algorithmGroup = new ButtonGroup();
			int idx = 0;
			for (SortAlgorithm<?> sort : cards.sort.getAll()) {
				final int index = idx++;
				final JMenuItem item = new JRadioButtonMenuItem(sort.toString());
				item.setSelected(cards.sort.getCurrent().equals(sort));
				item.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						DEBUGLOG.config("Set algorithm to "
								+ cards.sort.getAll().get(index));
						cards.sort.setCurrent(index);
						reset();
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
					final int size = i;
					JMenuItem item = new JRadioButtonMenuItem(
							Integer.toString(size));
					item.setSelected(i == n);
					item.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							DEBUGLOG.config("Changed size to " + size);
							n = size;
							setCards();
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
				final Type[] types = Type.values();

				for (int i = 0; i < types.length; i++) {
					final Type type = types[i];
					JMenuItem item = new JRadioButtonMenuItem(type.toString());
					item.setSelected(type == values.type);
					item.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							DEBUGLOG.config("Changed type to " + type);
							values.type = type;
							setCards();
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
				final Mode[] modes = Mode.values();

				for (int i = 0; i < modes.length; i++) {
					final Mode mode = modes[i];
					JMenuItem item = new JRadioButtonMenuItem(mode.toString());
					item.setSelected(mode == values.mode);
					item.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							DEBUGLOG.config("Changed mode to " + mode);
							values.mode = mode;
							setCards();
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

	private void setCards() {
		final Mode mode = values.mode;
		final Type type = values.type;

		int sort = 0;
		if (cards != null)
			sort = cards.sort.getCurrentIndex();

		switch (mode) {
		case SMALL:
			if (type == Type.INTEGER) {
				cards = new Cards<>(ValueGenerator.smallIntValues(n), sort);
				return;
			} else if (type == Type.STRING) {
				cards = new Cards<>(ValueGenerator.smallStringValues(n), sort);
				return;
			}
			break;
		case RANDOM:
			if (type == Type.INTEGER) {
				cards = new Cards<>(ValueGenerator.randomIntValues(n), sort);
				return;
			} else if (type == Type.STRING) {
				cards = new Cards<>(ValueGenerator.randomStringValues(n), sort);
				return;
			}
			break;
		}
		DEBUGLOG.severe("Unknown mode: '" + mode + "' or type: '" + type + "'");
	}

	public int getLeftReference() {
		final Component comp = cardBtns.get(cards.getFirstSelectedIndex())
				.getParent();
		return comp.getLocation().x + comp.getWidth() / 2;
	}

	public int getRightReference() {
		final Component comp = cardBtns.get(cards.getSecondSelectedIndex())
				.getParent();
		return comp.getLocation().x + comp.getWidth() / 2;
	}

	public boolean showSwapButton() {
		return cards.twoSelected();
	}

	/**
	 * Launch the application.
	 */
	public static void initAndRun() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (Exception e) {
				}
				try {
					ClientGUI window = new ClientGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
