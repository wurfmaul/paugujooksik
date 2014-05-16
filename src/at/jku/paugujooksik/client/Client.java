package at.jku.paugujooksik.client;

import java.awt.Color;
import java.awt.EventQueue;
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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

public class Client {
	private static final String DEFAULT_BUTTON_TEXT = "  ";
	private static final int MAX_CARDS = 7;

	private final CardSelector cards;
	private final List<Integer> values;
	private final List<AbstractButton> cardBtns;
	private final List<AbstractButton> pinBtns;
	private final List<AbstractButton> finBtns;

	private JFrame frame;
	private JPanel pnlAbove;
	private JPanel pnlBelow;
	private JButton btnExchange;
	private JLabel lblHint;

	/**
	 * Create the application.
	 */
	public Client() {
		values = new LinkedList<>();
		{
			for (int i = 0; i < MAX_CARDS; i++) {
				values.add(i + 1);
			}
		}
		cardBtns = new LinkedList<>();
		pinBtns = new LinkedList<>();
		finBtns = new LinkedList<>();
		cards = new CardSelector();
		initialize();
		reset();
	}

	private void reset() {
		Collections.shuffle(values);
		cards.reset();
		for (int i = 0; i < MAX_CARDS; i++) {
			cardBtns.get(i).setOpaque(false);
			finBtns.get(i).setSelected(false);
		}
	}

	private void updateButtons() {
		for (int i = 0; i < MAX_CARDS; i++) {
			final boolean active = cards.isSelected(i);
			final AbstractButton btnCard = cardBtns.get(i);
			{
				btnCard.setText(active ? Integer.toString(values.get(i))
					: DEFAULT_BUTTON_TEXT);
				btnCard.setSelected(active);
			}
			pinBtns.get(i).setSelected(cards.isPinned(i));
		}
		pnlAbove.removeAll();
		pnlBelow.removeAll();
		
		for (int i = 0; i<MAX_CARDS; i++) {
			pinBtns.get(i).setEnabled(false);
		}

		if (cards.one()) {
			pinBtns.get(cards.getOne()).setEnabled(true);
		} else if (cards.two()) {
			pinBtns.get(cards.getLeft()).setEnabled(true);
			pinBtns.get(cards.getRight()).setEnabled(true);
			
			btnExchange = new JButton("<->");
			{
				final int center = ((DrawPanel) pnlBelow).getCenter();
				btnExchange.setBounds(center - 50, 30, 100, 50);
				btnExchange.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						// exchange values
						final int i = cards.getLeft();
						final int j = cards.getRight();
						final int tmp = values.get(i);
						values.set(i, values.get(j));
						values.set(j, tmp);

						updateButtons();
					}
				});
			}
			pnlBelow.add(btnExchange);
		}
		frame.getContentPane().repaint();
		checkFinish();
	}

	private void checkFinish() {
		for (int i = 0; i<MAX_CARDS; i++) {
			if (!finBtns.get(i).isSelected()) {
				lblHint.setText("");
				return;
			}
		}
		Object[] act = values.toArray();
		Object[] exp = values.toArray();
		Arrays.sort(exp);
		if (Arrays.equals(act, exp)) {
			lblHint.setText("Congratulations!");
		} else {
			lblHint.setText("There is something wrong!");
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 350, 37, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 50, 150, 0, 21, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);

		JLabel lblTitle = new JLabel("Task: Sort the following cards!");
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

		JPanel pnlCards = new JPanel();
		GridBagConstraints gbcPnLCards = new GridBagConstraints();
		gbcPnLCards.gridwidth = 2;
		gbcPnLCards.insets = new Insets(0, 0, 5, 0);
		gbcPnLCards.fill = GridBagConstraints.BOTH;
		gbcPnLCards.gridx = 0;
		gbcPnLCards.gridy = 2;
		frame.getContentPane().add(pnlCards, gbcPnLCards);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
				1.0, 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		pnlCards.setLayout(gbl_panel);
		
		/*********
		 * CARDS *
		 *********/
		
		JToggleButton btnCard0Pin = new JToggleButton("!");
		GridBagConstraints gbcBtnCard0Pin = new GridBagConstraints();
		gbcBtnCard0Pin.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnCard0Pin.insets = new Insets(0, 0, 5, 5);
		gbcBtnCard0Pin.gridx = 0;
		gbcBtnCard0Pin.gridy = 0;
		btnCard0Pin.setEnabled(false);
		pnlCards.add(btnCard0Pin, gbcBtnCard0Pin);
		pinBtns.add(btnCard0Pin);
		
		JToggleButton btnCard0Fin = new JToggleButton("#");
		GridBagConstraints gbcBtnCard0Fin = new GridBagConstraints();
		gbcBtnCard0Fin.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnCard0Fin.insets = new Insets(0, 0, 5, 5);
		gbcBtnCard0Fin.gridx = 1;
		gbcBtnCard0Fin.gridy = 0;
		pnlCards.add(btnCard0Fin, gbcBtnCard0Fin);
		finBtns.add(btnCard0Fin);
		
		JToggleButton btnCard1Pin = new JToggleButton("!");
		GridBagConstraints gbcBtnCard1Pin = new GridBagConstraints();
		gbcBtnCard1Pin.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnCard1Pin.insets = new Insets(0, 0, 5, 5);
		gbcBtnCard1Pin.gridx = 2;
		gbcBtnCard1Pin.gridy = 0;
		btnCard1Pin.setEnabled(false);
		pnlCards.add(btnCard1Pin, gbcBtnCard1Pin);
		pinBtns.add(btnCard1Pin);
		
		JToggleButton btnCard1Fin = new JToggleButton("#");
		GridBagConstraints gbcBtnCard1Fin = new GridBagConstraints();
		gbcBtnCard1Fin.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnCard1Fin.insets = new Insets(0, 0, 5, 5);
		gbcBtnCard1Fin.gridx = 3;
		gbcBtnCard1Fin.gridy = 0;
		pnlCards.add(btnCard1Fin, gbcBtnCard1Fin);
		finBtns.add(btnCard1Fin);
		
		JToggleButton btnCard2Pin = new JToggleButton("!");
		GridBagConstraints gbcBtnCard2Pin = new GridBagConstraints();
		gbcBtnCard2Pin.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnCard2Pin.insets = new Insets(0, 0, 5, 5);
		gbcBtnCard2Pin.gridx = 4;
		gbcBtnCard2Pin.gridy = 0;
		btnCard2Pin.setEnabled(false);
		pnlCards.add(btnCard2Pin, gbcBtnCard2Pin);
		pinBtns.add(btnCard2Pin);
		
		JToggleButton btnCard2Fin = new JToggleButton("#");
		GridBagConstraints gbcBtnCard2Fin = new GridBagConstraints();
		gbcBtnCard2Fin.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnCard2Fin.insets = new Insets(0, 0, 5, 5);
		gbcBtnCard2Fin.gridx = 5;
		gbcBtnCard2Fin.gridy = 0;
		pnlCards.add(btnCard2Fin, gbcBtnCard2Fin);
		finBtns.add(btnCard2Fin);

		JToggleButton btnCard3Pin = new JToggleButton("!");
		GridBagConstraints gbcBtnCard3Pin = new GridBagConstraints();
		gbcBtnCard3Pin.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnCard3Pin.insets = new Insets(0, 0, 5, 5);
		gbcBtnCard3Pin.gridx = 6;
		gbcBtnCard3Pin.gridy = 0;
		btnCard3Pin.setEnabled(false);
		pnlCards.add(btnCard3Pin, gbcBtnCard3Pin);
		pinBtns.add(btnCard3Pin);
		
		JToggleButton btnCard3Fin = new JToggleButton("#");
		GridBagConstraints gbcBtnCard3Fin = new GridBagConstraints();
		gbcBtnCard3Fin.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnCard3Fin.insets = new Insets(0, 0, 5, 5);
		gbcBtnCard3Fin.gridx = 7;
		gbcBtnCard3Fin.gridy = 0;
		pnlCards.add(btnCard3Fin, gbcBtnCard3Fin);
		finBtns.add(btnCard3Fin);
		
		JToggleButton btnCard4Pin = new JToggleButton("!");
		GridBagConstraints gbcBtnCard4Pin = new GridBagConstraints();
		gbcBtnCard4Pin.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnCard4Pin.insets = new Insets(0, 0, 5, 5);
		gbcBtnCard4Pin.gridx = 8;
		gbcBtnCard4Pin.gridy = 0;
		btnCard4Pin.setEnabled(false);
		pnlCards.add(btnCard4Pin, gbcBtnCard4Pin);
		pinBtns.add(btnCard4Pin);
		
		JToggleButton btnCard4Fin = new JToggleButton("#");
		GridBagConstraints gbcBtnCard4Fin = new GridBagConstraints();
		gbcBtnCard4Fin.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnCard4Fin.insets = new Insets(0, 0, 5, 5);
		gbcBtnCard4Fin.gridx = 9;
		gbcBtnCard4Fin.gridy = 0;
		pnlCards.add(btnCard4Fin, gbcBtnCard4Fin);
		finBtns.add(btnCard4Fin);
		
		JToggleButton btnCard5Pin = new JToggleButton("!");
		GridBagConstraints gbcBtnCard5Pin = new GridBagConstraints();
		gbcBtnCard5Pin.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnCard5Pin.insets = new Insets(0, 0, 5, 5);
		gbcBtnCard5Pin.gridx = 10;
		gbcBtnCard5Pin.gridy = 0;
		btnCard5Pin.setEnabled(false);
		pnlCards.add(btnCard5Pin, gbcBtnCard5Pin);
		pinBtns.add(btnCard5Pin);
		
		JToggleButton btnCard5Fin = new JToggleButton("#");
		GridBagConstraints gbcBtnCard5Fin = new GridBagConstraints();
		gbcBtnCard5Fin.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnCard5Fin.insets = new Insets(0, 0, 5, 5);
		gbcBtnCard5Fin.gridx = 11;
		gbcBtnCard5Fin.gridy = 0;
		pnlCards.add(btnCard5Fin, gbcBtnCard5Fin);
		finBtns.add(btnCard5Fin);
		
		JToggleButton btnCard6Pin = new JToggleButton("!");
		GridBagConstraints gbcBtnCard6Pin = new GridBagConstraints();
		gbcBtnCard6Pin.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnCard6Pin.insets = new Insets(0, 0, 5, 5);
		gbcBtnCard6Pin.gridx = 12;
		gbcBtnCard6Pin.gridy = 0;
		btnCard6Pin.setEnabled(false);
		pnlCards.add(btnCard6Pin, gbcBtnCard6Pin);
		pinBtns.add(btnCard6Pin);
		
		JToggleButton btnCard6Fin = new JToggleButton("#");
		GridBagConstraints gbcBtnCard6Fin = new GridBagConstraints();
		gbcBtnCard6Fin.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnCard6Fin.insets = new Insets(0, 0, 5, 5);
		gbcBtnCard6Fin.gridx = 13;
		gbcBtnCard6Fin.gridy = 0;
		pnlCards.add(btnCard6Fin, gbcBtnCard6Fin);
		finBtns.add(btnCard6Fin);
		
		JToggleButton btnCard0 = new JToggleButton(DEFAULT_BUTTON_TEXT);
		GridBagConstraints gbc_btnCard0 = new GridBagConstraints();
		gbc_btnCard0.gridwidth = 2;
		gbc_btnCard0.fill = GridBagConstraints.BOTH;
		gbc_btnCard0.insets = new Insets(0, 0, 0, 5);
		gbc_btnCard0.gridx = 0;
		gbc_btnCard0.gridy = 1;
		pnlCards.add(btnCard0, gbc_btnCard0);
		cardBtns.add(btnCard0);

		JToggleButton btnCard1 = new JToggleButton(DEFAULT_BUTTON_TEXT);
		GridBagConstraints gbc_btnCard1 = new GridBagConstraints();
		gbc_btnCard1.gridwidth = 2;
		gbc_btnCard1.fill = GridBagConstraints.BOTH;
		gbc_btnCard1.insets = new Insets(0, 0, 0, 5);
		gbc_btnCard1.gridx = 2;
		gbc_btnCard1.gridy = 1;
		pnlCards.add(btnCard1, gbc_btnCard1);
		cardBtns.add(btnCard1);

		JToggleButton btnCard2 = new JToggleButton(DEFAULT_BUTTON_TEXT);
		GridBagConstraints gbc_btnCard2 = new GridBagConstraints();
		gbc_btnCard2.gridwidth = 2;
		gbc_btnCard2.fill = GridBagConstraints.BOTH;
		gbc_btnCard2.insets = new Insets(0, 0, 0, 5);
		gbc_btnCard2.gridx = 4;
		gbc_btnCard2.gridy = 1;
		pnlCards.add(btnCard2, gbc_btnCard2);
		cardBtns.add(btnCard2);

		JToggleButton btnCard3 = new JToggleButton(DEFAULT_BUTTON_TEXT);
		GridBagConstraints gbc_btnCard3 = new GridBagConstraints();
		gbc_btnCard3.gridwidth = 2;
		gbc_btnCard3.fill = GridBagConstraints.BOTH;
		gbc_btnCard3.insets = new Insets(0, 0, 0, 5);
		gbc_btnCard3.gridx = 6;
		gbc_btnCard3.gridy = 1;
		pnlCards.add(btnCard3, gbc_btnCard3);
		cardBtns.add(btnCard3);

		JToggleButton btnCard4 = new JToggleButton(DEFAULT_BUTTON_TEXT);
		GridBagConstraints gbc_btnCard4 = new GridBagConstraints();
		gbc_btnCard4.gridwidth = 2;
		gbc_btnCard4.fill = GridBagConstraints.BOTH;
		gbc_btnCard4.insets = new Insets(0, 0, 0, 5);
		gbc_btnCard4.gridx = 8;
		gbc_btnCard4.gridy = 1;
		pnlCards.add(btnCard4, gbc_btnCard4);
		cardBtns.add(btnCard4);

		JToggleButton btnCard5 = new JToggleButton(DEFAULT_BUTTON_TEXT);
		GridBagConstraints gbc_btnCard5 = new GridBagConstraints();
		gbc_btnCard5.gridwidth = 2;
		gbc_btnCard5.fill = GridBagConstraints.BOTH;
		gbc_btnCard5.insets = new Insets(0, 0, 0, 5);
		gbc_btnCard5.gridx = 10;
		gbc_btnCard5.gridy = 1;
		pnlCards.add(btnCard5, gbc_btnCard5);
		cardBtns.add(btnCard5);

		JToggleButton btnCard6 = new JToggleButton(DEFAULT_BUTTON_TEXT);
		GridBagConstraints gbc_btnCard6 = new GridBagConstraints();
		gbc_btnCard6.gridwidth = 2;
		gbc_btnCard6.fill = GridBagConstraints.BOTH;
		gbc_btnCard6.gridx = 12;
		gbc_btnCard6.gridy = 1;
		pnlCards.add(btnCard6, gbc_btnCard6);
		cardBtns.add(btnCard6);

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

		JLabel lblCount = new JLabel("0");
		GridBagConstraints gbcLblCount = new GridBagConstraints();
		gbcLblCount.gridx = 1;
		gbcLblCount.gridy = 4;
		frame.getContentPane().add(lblCount, gbcLblCount);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmRestart = new JMenuItem("Restart");
		mntmRestart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reset();
				updateButtons();
			}
		});
		mnFile.add(mntmRestart);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		mnFile.add(mntmExit);

		JMenuItem mntmHelp = new JMenuItem("Help");
		menuBar.add(mntmHelp);

		// Action Handlers
		for (int i = 0; i < MAX_CARDS; i++) {
			final int index = i;
			final AbstractButton btnCard = cardBtns.get(index);
			final AbstractButton btnPin = pinBtns.get(index);
			final AbstractButton btnFin = finBtns.get(index);
			btnCard.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (!cards.select(index))
						btnCard.setSelected(false);
					updateButtons();
				}
			});
			btnPin.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					cards.togglePin(index);
					updateButtons();
				}
			});
			btnFin.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					btnCard.setOpaque(!btnCard.isOpaque());
					btnCard.setBackground(Color.GREEN);
					updateButtons();
				}
			});
		}
		
		
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					System.err.println(e.getLocalizedMessage());
				}
				try {
					Client window = new Client();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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
			final AbstractButton btn = cardBtns.get(cards.getLeft());
			return btn.getLocation().x + btn.getWidth() / 2;
		}

		private int getRight() {
			final AbstractButton btn = cardBtns.get(cards.getRight());
			return btn.getLocation().x + btn.getWidth() / 2;
		}

		private int getCenter() {
			return getLeft() + Math.abs(getRight() - getLeft()) / 2;
		}
	}
}
