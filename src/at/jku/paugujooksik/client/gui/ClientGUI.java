package at.jku.paugujooksik.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import at.jku.paugujooksik.client.gui.ValueGenerator.Mode;
import at.jku.paugujooksik.client.gui.ValueGenerator.Type;
import at.jku.paugujooksik.client.sort.SortAlgorithm;

public class ClientGUI {
	private static final Logger DEBUGLOG = Logger.getLogger("DEBUG");
	private static final String DEFAULT_BUTTON_TEXT = "  ";
	private static final int MIN_SIZE = 7;
	private static final int MAX_SIZE = 15;
	private static final int DEFAULT_SIZE = 7;

	private final JFrame frame;
	private final ValueGenerator values;
	private final List<CardPanel> cardBtns;

	private JPanel pnlAbove;
	private DrawPanel pnlBelow;
	private JButton btnExchange;
	private JTextPane txtErrors;
	private JLabel lblTitle;
	private JLabel lblHint;
	private Cards<?> cards;
	private int errorCount = 0;
	private int swapCount = 0;
	private int n = DEFAULT_SIZE;

	/**
	 * Create the application.
	 */
	private ClientGUI() {
		frame = new JFrame();
		values = new ValueGenerator();
		cardBtns = new LinkedList<>();
		setCards();
		initFrame();
		initialize();
	}

	private void reset() {
		errorCount = 0;
		swapCount = 0;
		cards.reset();
		updateComponents();
		updateStats();
	}

	private void clearErrorHint() {
		lblHint.setText("");
	}

	private void reportError(SelectionException ex) {
		lblHint.setForeground(Color.RED);
		lblHint.setText(ex.getMessage());
		errorCount++;
		updateStats();
	}

	private void updateComponents() {
		for (int i = 0; i < n; i++) {
			cardBtns.get(i).setTo(cards.getCard(i));
		}
	
		pnlBelow.removeAll();
		if (cards.two()) {
			btnExchange = new SwapButton();
			btnExchange.setBounds(pnlBelow.getCenter() - 50, 30, 100, 50);
			pnlBelow.add(btnExchange);
		}
		
		lblTitle.setText(cards.sort.getCurrent().toString());
	
		frame.repaint();
		checkFinish();
	}

	private void updateStats() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Errors: ");
		sb.append(errorCount);
		sb.append(System.lineSeparator());
		sb.append("Swaps: ");
		sb.append(swapCount);
		txtErrors.setText(sb.toString());
	}

	private void checkFinish() {
		if (cards.allMarked()) {
			if (cards.isSorted()) {
				lblHint.setForeground(Color.GREEN);
				lblHint.setText("Congratulations!");
			} else {
				lblHint.setText("There is something wrong!");
			}
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame.getContentPane().removeAll();

		lblTitle = new JLabel(cards.sort.getCurrent().toString());
		lblTitle.setFont(new Font("Dialog", Font.BOLD, 16));
		GridBagConstraints gbc_lblTitle = new GridBagConstraints();
		gbc_lblTitle.gridwidth = 2;
		gbc_lblTitle.anchor = GridBagConstraints.NORTH;
		gbc_lblTitle.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblTitle.insets = new Insets(0, 0, 5, 0);
		gbc_lblTitle.gridx = 0;
		gbc_lblTitle.gridy = 0;
		frame.getContentPane().add(lblTitle, gbc_lblTitle);

		pnlAbove = new JPanel();
		pnlAbove.setLayout(null);
		GridBagConstraints gbcPnlAbove = new GridBagConstraints();
		gbcPnlAbove.gridwidth = 2;
		gbcPnlAbove.insets = new Insets(0, 0, 5, 0);
		gbcPnlAbove.fill = GridBagConstraints.BOTH;
		gbcPnlAbove.gridx = 0;
		gbcPnlAbove.gridy = 1;
		frame.getContentPane().add(pnlAbove, gbcPnlAbove);

		initCardPanel();

		pnlBelow = new DrawPanel();
		pnlBelow.setLayout(null);
		GridBagConstraints gbcPnlLines = new GridBagConstraints();
		gbcPnlLines.gridwidth = 2;
		gbcPnlLines.insets = new Insets(0, 0, 5, 0);
		gbcPnlLines.fill = GridBagConstraints.BOTH;
		gbcPnlLines.gridx = 0;
		gbcPnlLines.gridy = 3;
		frame.getContentPane().add(pnlBelow, gbcPnlLines);

		lblHint = new JLabel("Click at one of the cards!");
		GridBagConstraints gbcLblHint = new GridBagConstraints();
		gbcLblHint.anchor = GridBagConstraints.WEST;
		gbcLblHint.insets = new Insets(0, 0, 0, 5);
		gbcLblHint.gridx = 0;
		gbcLblHint.gridy = 4;
		frame.getContentPane().add(lblHint, gbcLblHint);

		txtErrors = new JTextPane();
		GridBagConstraints gbcLblCount = new GridBagConstraints();
		gbcLblCount.gridx = 1;
		gbcLblCount.gridy = 4;
		txtErrors.setOpaque(false);
		frame.getContentPane().add(txtErrors, gbcLblCount);
		updateStats();

		frame.setJMenuBar(initMenuBar());
	}
	
	private void initFrame() {
		frame.setBounds(100, 100, 700, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gblFrame = new GridBagLayout();
		gblFrame.columnWidths = new int[] { 350, 40, 0 };
		gblFrame.rowHeights = new int[] { 0, 50, 0, 0, 20, 0 };
		gblFrame.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		gblFrame.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		frame.getContentPane().setLayout(gblFrame);
	}

	private void initCardPanel() {
		final JPanel pnlCards = new JPanel();
		final GridBagConstraints gbcPnLCards = new GridBagConstraints();
		{
			gbcPnLCards.gridwidth = 2;
			gbcPnLCards.insets = new Insets(0, 0, 5, 0);
			gbcPnLCards.fill = GridBagConstraints.BOTH;
			gbcPnLCards.gridx = 0;
			gbcPnLCards.gridy = 2;
		}
		frame.getContentPane().add(pnlCards, gbcPnLCards);

		final GridBagLayout gblPnlCards = new GridBagLayout();
		{
			gblPnlCards.columnWidths = new int[n + 1];
			gblPnlCards.rowHeights = new int[] { 0, 0 };
			double[] weights = new double[n + 1];
			{
				Arrays.fill(weights, 1.0);
				weights[weights.length - 1] = Double.MIN_VALUE;
			}
			gblPnlCards.columnWeights = weights;
			gblPnlCards.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		}
		pnlCards.setLayout(gblPnlCards);

		cardBtns.clear();
		for (int i = 0; i < n; i++) {
			final CardPanel card = new CardPanel(i);
			final GridBagConstraints gbc = new GridBagConstraints();
			{
				gbc.fill = GridBagConstraints.BOTH;
				gbc.insets = new Insets(0, 0, 0, 5);
			}
			pnlCards.add(card, gbc);
			cardBtns.add(card);
		}
	}

	private JMenuBar initMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu mnFile = new JMenu("File");
		{
			JMenuItem mntmRestart = new JMenuItem("Restart");
			mntmRestart.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					DEBUGLOG.fine("Restarting game");
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
							reset();
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
							reset();
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
							reset();
						}
					});
					kindGroup.add(item);
					mnMode.add(item);
				}
			}
			mnConfig.add(mnMode);
		}
		menuBar.add(mnConfig);
		return menuBar;
	}

	private void setCards() {
		final Mode mode = values.mode;
		final Type type = values.type;

		switch (mode) {
		case SMALL:
			if (type == Type.INTEGER) {
				cards = new Cards<>(ValueGenerator.smallIntValues(n));
				return;
			} else if (type == Type.STRING) {
				cards = new Cards<>(ValueGenerator.smallStringValues(n));
				return;
			}
			break;
		case RANDOM:
			if (type == Type.INTEGER) {
				cards = new Cards<>(ValueGenerator.randomIntValues(n));
				return;
			} else if (type == Type.STRING) {
				cards = new Cards<>(ValueGenerator.randomStringValues(n));
				return;
			}
			break;
		}
		DEBUGLOG.severe("Unknown mode: '" + mode + "' or type: '" + type + "'");
	}

	/**
	 * Launch the application.
	 */
	public static <T extends Comparable<T>> void initAndRun() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					System.err.println(e.getLocalizedMessage());
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

	private class SwapButton extends JButton {
		private static final String SWAP_ICON = "/img/swap.png";
		private static final long serialVersionUID = -6368213501613529319L;

		public SwapButton() {
			super(new ImageIcon(SwapButton.class.getResource(SWAP_ICON)));
			
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					try {
						cards.swapSelection();
						swapCount++;
						clearErrorHint();
						updateStats();
					} catch (SelectionException ex) {
						reportError(ex);
					}
					updateComponents();
				}
			});
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			setBounds(pnlBelow.getCenter() - 50, 30, 100, 50);
		}
	}

	private class DrawPanel extends JPanel {
		private static final long serialVersionUID = -7863948686242926432L;

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (cards.two()) {
				final Graphics2D g2d = (Graphics2D) g;
				{
					g2d.drawLine(getLeft(), 20, getRight(), 20);
					g2d.drawLine(getLeft(), 0, getLeft(), 20);
					g2d.drawLine(getRight(), 0, getRight(), 20);
					g2d.drawLine(getCenter(), 20, getCenter(), 30);
				}
			}
		}

		private int getLeft() {
			final CardPanel btn = cardBtns.get(cards.getLeft());
			return btn.getLocation().x + btn.getWidth() / 2;
		}

		private int getRight() {
			final CardPanel btn = cardBtns.get(cards.getRight());
			return btn.getLocation().x + btn.getWidth() / 2;
		}

		private int getCenter() {
			return getLeft() + Math.abs(getRight() - getLeft()) / 2;
		}
	}

	private class CardPanel extends JPanel {
		private static final String GRAY_PIN_ICON = "/img/pin-grey.png";
		private static final String BLACK_PIN_ICON = "/img/pin.png";
		private static final String ROTATED_PIN_ICON = "/img/pin-rot.png";
		private static final String BLACK_CHECK_ICON = "/img/check.png";
		private static final String GRAY_CHECK_ICON = "/img/check-gray.png";
		private static final int DEFAULT_BUTTON_SIZE = 25;
		private static final long serialVersionUID = 68959464664105468L;
		private final JLabel label;
		private final TitledBorder border;
		private final JToggleButton pin;
		private final JToggleButton fin;

		public CardPanel(final int index) {
			super(new BorderLayout(5, 5));
			border = new TitledBorder(new BevelBorder(BevelBorder.RAISED),
					Integer.toString(index + 1));
			setBorder(border);
			setOpaque(false);

			label = new JLabel(DEFAULT_BUTTON_TEXT, JLabel.CENTER);
			{
				label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
				add(label, BorderLayout.CENTER);
			}
			pin = new JToggleButton();
			{
				JPanel pnlPin = new JPanel(new FlowLayout(FlowLayout.RIGHT));
				pin.setEnabled(false);
				pin.setIcon(new ImageIcon(getClass().getResource(GRAY_PIN_ICON)));
				pin.setSelectedIcon(new ImageIcon(getClass().getResource(ROTATED_PIN_ICON)));
				pin.setPreferredSize(new Dimension(DEFAULT_BUTTON_SIZE,
						DEFAULT_BUTTON_SIZE));
				pnlPin.add(pin);
				add(pnlPin, BorderLayout.NORTH);
			}
			fin = new JToggleButton();
			{
				JPanel pnlFin = new JPanel(new FlowLayout(FlowLayout.RIGHT));
				fin.setIcon(new ImageIcon(getClass().getResource(GRAY_CHECK_ICON)));
				fin.setSelectedIcon(new ImageIcon(getClass().getResource(BLACK_CHECK_ICON)));
				fin.setPreferredSize(new Dimension(DEFAULT_BUTTON_SIZE,
						DEFAULT_BUTTON_SIZE));
				pnlFin.add(fin);
				add(pnlFin, BorderLayout.SOUTH);
			}

			initMouseListeners(index);
		}

		private void initMouseListeners(final int index) {
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					try {
						cards.select(index);
						clearErrorHint();
					} catch (SelectionException ex) {
						reportError(ex);
					}
					updateComponents();
				}
			});
			pin.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					pin.setIcon(new ImageIcon(getClass().getResource(BLACK_PIN_ICON)));
				}

				@Override
				public void mouseExited(MouseEvent e) {
					if (!cards.isPinned(index))
						pin.setIcon(new ImageIcon(getClass().getResource(GRAY_PIN_ICON)));
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					if (cards.getCard(index).selected) {
						try {
							cards.togglePin(index);
							clearErrorHint();
						} catch (SelectionException ex) {
							reportError(ex);
						}
						updateComponents();
					}
				}
			});
			fin.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					fin.setIcon(new ImageIcon(getClass().getResource(BLACK_CHECK_ICON)));
				}

				@Override
				public void mouseExited(MouseEvent e) {
					fin.setIcon(new ImageIcon(getClass().getResource(GRAY_CHECK_ICON)));
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					try {
						cards.toggleMark(index);
						clearErrorHint();
					} catch (SelectionException ex) {
						reportError(ex);
					}
					updateComponents();
				}
			});
		}

		public void setTo(Card<?> c) {
			border.setBorder(new BevelBorder(c.selected ? BevelBorder.LOWERED
					: BevelBorder.RAISED));
			label.setText(c.selected ? c.toString() : DEFAULT_BUTTON_TEXT);
			pin.setEnabled(c.selected);
			pin.setSelected(c.pinned);
			pin.setIcon(new ImageIcon(getClass().getResource(c.pinned ? ROTATED_PIN_ICON
					: GRAY_PIN_ICON)));
			fin.setSelected(c.marked);
			setBackground(c.marked ? Color.GREEN : Color.GRAY);
		}
	}
}
