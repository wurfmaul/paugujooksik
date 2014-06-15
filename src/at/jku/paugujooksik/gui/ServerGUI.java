package at.jku.paugujooksik.gui;

import static at.jku.paugujooksik.gui.ResourceLoader.PLAY_ICON;
import static at.jku.paugujooksik.gui.ResourceLoader.loadIcon;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.JSpinner;
import javax.swing.JSlider;

public class ServerGUI extends AbstractGUI {
	private static final String HOST_IP;
	private static final int HOST_PORT = 1099;
	private JTextPane txtConnection;
	
	static {
		String ip = "unknown";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
		} finally {
			HOST_IP = ip;
		}
	}

	private JFrame frame;
	private JList<String> playerList;

	/**
	 * Create the application.
	 */
	public ServerGUI() {
		setupRegistry();
		initialize();
	}

	private void setupRegistry() {
		try {
			@SuppressWarnings("unused")
			Registry reg = LocateRegistry.createRegistry(HOST_PORT);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 500);
		frame.setMinimumSize(new Dimension(500, 450));
		frame.setTitle("Paugujooksik - Presenter Mode");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gblFrame = new GridBagLayout();
		gblFrame.columnWidths = new int[] { 175, 175, 175, 175, 0 };
		gblFrame.rowHeights = new int[] { 50, 150, 50, 40, 40, 40, 40, 0 };
		gblFrame.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gblFrame.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		frame.getContentPane().setLayout(gblFrame);

		JLabel lblConnTitle = new JLabel("Connection");
		lblConnTitle.setFont(DEFAULT_FONT_BOLD);
		GridBagConstraints gbcLblConnTitle = new GridBagConstraints();
		gbcLblConnTitle.gridwidth = 2;
		gbcLblConnTitle.fill = GridBagConstraints.BOTH;
		gbcLblConnTitle.insets = new Insets(0, 5, 5, 5);
		gbcLblConnTitle.gridx = 0;
		gbcLblConnTitle.gridy = 0;
		frame.getContentPane().add(lblConnTitle, gbcLblConnTitle);

		JLabel lblPlayerTitle = new JLabel("Players");
		lblPlayerTitle.setFont(DEFAULT_FONT_BOLD);
		GridBagConstraints gbcLblPlayerTitle = new GridBagConstraints();
		gbcLblPlayerTitle.gridwidth = 2;
		gbcLblPlayerTitle.fill = GridBagConstraints.BOTH;
		gbcLblPlayerTitle.insets = new Insets(0, 5, 5, 0);
		gbcLblPlayerTitle.gridx = 2;
		gbcLblPlayerTitle.gridy = 0;
		frame.getContentPane().add(lblPlayerTitle, gbcLblPlayerTitle);
		
		txtConnection = new JTextPane();
		txtConnection.setFont(DEFAULT_FONT);
		txtConnection.setOpaque(false);
		txtConnection.setText(printIpConfig());
		GridBagConstraints gbcTxtConnection = new GridBagConstraints();
		gbcTxtConnection.gridwidth = 2;
		gbcTxtConnection.insets = new Insets(0, 0, 5, 5);
		gbcTxtConnection.fill = GridBagConstraints.BOTH;
		gbcTxtConnection.gridx = 0;
		gbcTxtConnection.gridy = 1;
		frame.getContentPane().add(txtConnection, gbcTxtConnection);
		
		playerList = new JList<>();
		playerList.setFont(DEFAULT_FONT);
		playerList.setOpaque(false);
		GridBagConstraints gbcPlayerList = new GridBagConstraints();
		gbcPlayerList.gridwidth = 2;
		gbcPlayerList.insets = new Insets(0, 0, 5, 0);
		gbcPlayerList.fill = GridBagConstraints.BOTH;
		gbcPlayerList.gridx = 2;
		gbcPlayerList.gridy = 1;
		frame.getContentPane().add(playerList, gbcPlayerList);

		JLabel lblConfigTitle = new JLabel("Configuration");
		lblConfigTitle.setFont(DEFAULT_FONT_BOLD);
		GridBagConstraints gbcLblConfigTitle = new GridBagConstraints();
		gbcLblConfigTitle.fill = GridBagConstraints.BOTH;
		gbcLblConfigTitle.insets = new Insets(0, 5, 5, 5);
		gbcLblConfigTitle.gridx = 0;
		gbcLblConfigTitle.gridy = 2;
		frame.getContentPane().add(lblConfigTitle, gbcLblConfigTitle);
		
		JLabel lblCfgAlgo = new JLabel("Algorithm: ");
		GridBagConstraints gbcLblCfgAlgo = new GridBagConstraints();
		gbcLblCfgAlgo.insets = new Insets(0, 0, 5, 5);
		gbcLblCfgAlgo.gridx = 0;
		gbcLblCfgAlgo.gridy = 3;
		frame.getContentPane().add(lblCfgAlgo, gbcLblCfgAlgo);
		
		JLabel lblCfgSize = new JLabel("Size: ");
		GridBagConstraints gbcLblCfgSize = new GridBagConstraints();
		gbcLblCfgSize.insets = new Insets(0, 0, 5, 5);
		gbcLblCfgSize.gridx = 0;
		gbcLblCfgSize.gridy = 4;
		frame.getContentPane().add(lblCfgSize, gbcLblCfgSize);
		
		JSlider sldrCfgSize = new JSlider();
		sldrCfgSize.setMajorTickSpacing(1);
		sldrCfgSize.setSnapToTicks(true);
		sldrCfgSize.setPaintTicks(true);
		sldrCfgSize.setValue(DEFAULT_SIZE);
		sldrCfgSize.setMinimum(MIN_SIZE);
		sldrCfgSize.setMaximum(MAX_SIZE);
		GridBagConstraints gbcSldrSize = new GridBagConstraints();
		gbcSldrSize.fill = GridBagConstraints.HORIZONTAL;
		gbcSldrSize.insets = new Insets(0, 0, 5, 5);
		gbcSldrSize.gridx = 1;
		gbcSldrSize.gridy = 4;
		frame.getContentPane().add(sldrCfgSize, gbcSldrSize);
		
		JLabel lblCfgType = new JLabel("Type: ");
		GridBagConstraints gbcLblCfgType = new GridBagConstraints();
		gbcLblCfgType.insets = new Insets(0, 0, 5, 5);
		gbcLblCfgType.gridx = 0;
		gbcLblCfgType.gridy = 5;
		frame.getContentPane().add(lblCfgType, gbcLblCfgType);
		
		JLabel lblCfgMode = new JLabel("Mode: ");
		GridBagConstraints gbcLblCfgMode = new GridBagConstraints();
		gbcLblCfgMode.insets = new Insets(0, 0, 0, 5);
		gbcLblCfgMode.gridx = 0;
		gbcLblCfgMode.gridy = 6;
		frame.getContentPane().add(lblCfgMode, gbcLblCfgMode);

		JButton btnPlay = new JButton(loadIcon(PLAY_ICON));
		btnPlay.setEnabled(false);
		GridBagConstraints gbcBtnPlay = new GridBagConstraints();
		gbcBtnPlay.gridheight = 4;
		gbcBtnPlay.gridx = 3;
		gbcBtnPlay.gridy = 3;
		frame.getContentPane().add(btnPlay, gbcBtnPlay);
		
		initMenuBar();
	}

	private String printIpConfig() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Host IP: ");
		sb.append(HOST_IP);
		sb.append(System.lineSeparator());
		sb.append("Host Port: ");
		sb.append(HOST_PORT);
		return sb.toString();
	}

	private void initMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu mnFile = new JMenu("File");
		{
			JMenuItem mntmExit = new JMenuItem("Exit");
			mntmExit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					DEBUGLOG.info("Exiting server");
					frame.dispose();
				}
			});
			mnFile.add(mntmExit);
		}
		menuBar.add(mnFile);
		frame.setJMenuBar(menuBar);
	}

	@Override
	protected void checkComponents() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void clearErrors() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void reportError(SelectionException ex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateStats() {
		// TODO Auto-generated method stub
		
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
					ServerGUI window = new ServerGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
