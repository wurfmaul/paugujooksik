package at.jku.paugujooksik.client;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
	private static final int ACTIVE_CARDS = 2;
	private static final int MAX_CARDS = 7;
	
	private List<Integer> values;
	private List<AbstractButton> cardBtns;
	private JFrame frame;
	
	private int[] actCards;
	private int actCardIdx;
	private JPanel pnlLines;
	private JButton btnExchange;
	

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
		actCards = new int[ACTIVE_CARDS];
		cardBtns = new LinkedList<>();
		reset();
		initialize();
	}
	
	private void nextCard(int index) {
		actCardIdx = ++actCardIdx % ACTIVE_CARDS;
		actCards[actCardIdx] = index;
	}
	
	private void reset() {
		Collections.shuffle(values);
		Arrays.fill(actCards, -1);
		actCardIdx = 0;
	}
	
	private boolean exchangeEnabled() {
		return actCards.length == 2 && actCards[0] > -1 && actCards[1] > -1;
	}
	
	private void updateButtons() {
		for (int i = 0; i < MAX_CARDS; i++) {
			boolean active = false;
			for (int j = 0; j < ACTIVE_CARDS; j++) {
				if (i == actCards[j]) {
					active = true;
					break;
				}
			}
			final AbstractButton btn = cardBtns.get(i);
			btn.setText(active ? Integer.toString(values.get(i)) : DEFAULT_BUTTON_TEXT);
			btn.setSelected(active);
		}
		pnlLines.repaint();

		if (exchangeEnabled()) {
			pnlLines.removeAll();
			btnExchange = new JButton("<->");
			btnExchange.setLayout(null);
//			int x = ((LinesPanel) pnlLines).getCenter();
//			btnExchange.setLocation(300, 100);
			btnExchange.setBounds(200, 300, 100, 200);
			pnlLines.add(btnExchange);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 385);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JLabel lblTitle = new JLabel("Task: Sort the following cards!");
		lblTitle.setFont(new Font("Dialog", Font.BOLD, 16));
		frame.getContentPane().add(lblTitle, BorderLayout.NORTH);
		
		JPanel pnlCenter = new JPanel();
		frame.getContentPane().add(pnlCenter, BorderLayout.CENTER);
		GridBagLayout gbl_pnlCenter = new GridBagLayout();
		gbl_pnlCenter.columnWidths = new int[]{448, 0};
		gbl_pnlCenter.rowHeights = new int[]{100, 0, 0};
		gbl_pnlCenter.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_pnlCenter.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		pnlCenter.setLayout(gbl_pnlCenter);
		
		JPanel pnlCards = new JPanel();
		GridBagConstraints gbc_pnlCards = new GridBagConstraints();
		gbc_pnlCards.insets = new Insets(0, 0, 5, 0);
		gbc_pnlCards.fill = GridBagConstraints.BOTH;
		gbc_pnlCards.gridx = 0;
		gbc_pnlCards.gridy = 0;
		pnlCenter.add(pnlCards, gbc_pnlCards);
		GridBagLayout gbl_pnlCards = new GridBagLayout();
		gbl_pnlCards.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_pnlCards.rowHeights = new int[]{0, 0};
		gbl_pnlCards.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_pnlCards.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		pnlCards.setLayout(gbl_pnlCards);
		
		JToggleButton btnCard0 = new JToggleButton(DEFAULT_BUTTON_TEXT);
		GridBagConstraints gbcBtnCard0 = new GridBagConstraints();
		gbcBtnCard0.fill = GridBagConstraints.BOTH;
		gbcBtnCard0.insets = new Insets(0, 0, 0, 5);
		gbcBtnCard0.gridx = 0;
		gbcBtnCard0.gridy = 0;
		pnlCards.add(btnCard0, gbcBtnCard0);
		cardBtns.add(btnCard0);
		
		JToggleButton btnCard1 = new JToggleButton(DEFAULT_BUTTON_TEXT);
		GridBagConstraints gbcBtnCard1 = new GridBagConstraints();
		gbcBtnCard1.fill = GridBagConstraints.BOTH;
		gbcBtnCard1.insets = new Insets(0, 0, 0, 5);
		gbcBtnCard1.gridx = 1;
		gbcBtnCard1.gridy = 0;
		pnlCards.add(btnCard1, gbcBtnCard1);
		cardBtns.add(btnCard1);
		
		JToggleButton btnCard2 = new JToggleButton(DEFAULT_BUTTON_TEXT);
		GridBagConstraints gbcBtnCard2 = new GridBagConstraints();
		gbcBtnCard2.fill = GridBagConstraints.BOTH;
		gbcBtnCard2.insets = new Insets(0, 0, 0, 5);
		gbcBtnCard2.gridx = 2;
		gbcBtnCard2.gridy = 0;
		pnlCards.add(btnCard2, gbcBtnCard2);
		cardBtns.add(btnCard2);
		
		JToggleButton btnCard3 = new JToggleButton(DEFAULT_BUTTON_TEXT);
		GridBagConstraints gbcBtnCard3 = new GridBagConstraints();
		gbcBtnCard3.fill = GridBagConstraints.BOTH;
		gbcBtnCard3.insets = new Insets(0, 0, 0, 5);
		gbcBtnCard3.gridx = 3;
		gbcBtnCard3.gridy = 0;
		pnlCards.add(btnCard3, gbcBtnCard3);
		cardBtns.add(btnCard3);
		
		JToggleButton btnCard4 = new JToggleButton(DEFAULT_BUTTON_TEXT);
		GridBagConstraints gbcBtnCard4 = new GridBagConstraints();
		gbcBtnCard4.fill = GridBagConstraints.BOTH;
		gbcBtnCard4.insets = new Insets(0, 0, 0, 5);
		gbcBtnCard4.gridx = 4;
		gbcBtnCard4.gridy = 0;
		pnlCards.add(btnCard4, gbcBtnCard4);
		cardBtns.add(btnCard4);
		
		JToggleButton btnCard5 = new JToggleButton(DEFAULT_BUTTON_TEXT);
		GridBagConstraints gbcBtnCard5 = new GridBagConstraints();
		gbcBtnCard5.fill = GridBagConstraints.BOTH;
		gbcBtnCard5.insets = new Insets(0, 0, 0, 5);
		gbcBtnCard5.gridx = 5;
		gbcBtnCard5.gridy = 0;
		pnlCards.add(btnCard5, gbcBtnCard5);
		cardBtns.add(btnCard5);
		
		JToggleButton btnCard6 = new JToggleButton(DEFAULT_BUTTON_TEXT);
		GridBagConstraints gbcBtnCard6 = new GridBagConstraints();
		gbcBtnCard6.fill = GridBagConstraints.BOTH;
		gbcBtnCard6.gridx = 6;
		gbcBtnCard6.gridy = 0;
		pnlCards.add(btnCard6, gbcBtnCard6);
		cardBtns.add(btnCard6);
		
		pnlLines = new LinesPanel();
		GridBagConstraints gbc_pnlLines = new GridBagConstraints();
		gbc_pnlLines.fill = GridBagConstraints.BOTH;
		gbc_pnlLines.gridx = 0;
		gbc_pnlLines.gridy = 1;
		pnlCenter.add(pnlLines, gbc_pnlLines);
		
		JPanel pnlToolbar = new JPanel();
		frame.getContentPane().add(pnlToolbar, BorderLayout.SOUTH);
		pnlToolbar.setLayout(new BorderLayout(0, 0));
		
		JLabel lblHint = new JLabel("Click at one of the cards!");
		pnlToolbar.add(lblHint, BorderLayout.CENTER);
		
		JLabel lblCount = new JLabel("0");
		pnlToolbar.add(lblCount, BorderLayout.EAST);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmRestart = new JMenuItem("Restart");
		mntmRestart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("click");
				reset();
				updateButtons();
			}
		});
		mnFile.add(mntmRestart);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.setVisible(false);
				frame.dispose();
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);
		
		JMenuItem mntmHelp = new JMenuItem("Help");
		menuBar.add(mntmHelp);
		
		// Action Handlers
		for (int i = 0; i < cardBtns.size(); i++) {
			final int index = i;
			final AbstractButton btn = cardBtns.get(index);
			btn.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					nextCard(index);
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
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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
	
	public class LinesPanel extends JPanel {
		private static final long serialVersionUID = -7863948686242926432L;
		private int center;

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (exchangeEnabled()) {
				final Graphics2D g2d = (Graphics2D) g;
				{
					final AbstractButton btn1 = cardBtns.get(actCards[0]);
					final AbstractButton btn2 = cardBtns.get(actCards[1]);
					final int x1 = btn1.getLocation().x + btn1.getWidth() / 2;
					final int x2 = btn2.getLocation().x + btn2.getWidth() / 2;
					center = Math.min(x1, x2) + Math.abs(x2 - x1) / 2;
					
					g2d.drawLine(x1, 20, x2, 20);
					g2d.drawLine(x1, 0, x1, 20);
					g2d.drawLine(x2, 0, x2, 20);
					g2d.drawLine(center, 20, center, 30);
				}
			}
		}

		public int getCenter() {
			return center;
		}
	}
}
