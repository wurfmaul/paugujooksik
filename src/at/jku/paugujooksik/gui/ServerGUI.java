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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

import at.jku.paugujooksik.logic.ValueGenerator.Mode;
import at.jku.paugujooksik.logic.ValueGenerator.Type;

public class ServerGUI extends AbstractGUI {
	private static final String HOST_IP;
	private static final int HOST_PORT = 1099;
	private JTextArea txtPlayers;
	
	private JFrame frame; // FIXME use super.frame
	
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
		frame.setBounds(100, 100, 700, 420);
		frame.setMinimumSize(new Dimension(500, 420));
		frame.setTitle("Paugujooksik - Presenter Mode");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gblFrame = new GridBagLayout();
		gblFrame.columnWidths = new int[] { 175, 175, 175, 175, 0 };
		gblFrame.rowHeights = new int[] { 50, 80, 0, 50, 40, 40, 40, 40, 0 };
		gblFrame.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE };
		gblFrame.rowWeights = new double[] { 1.0, 1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE };
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
		
		txtPlayers = new JTextArea("Waiting for players...");
		txtPlayers.setFont(DEFAULT_FONT);
		txtPlayers.setOpaque(false);
		txtPlayers.setFocusable(false);
		txtPlayers.setBorder(new BevelBorder(BevelBorder.LOWERED));
		GridBagConstraints gbcTxtPlayers = new GridBagConstraints();
		gbcTxtPlayers.gridheight = 4;
		gbcTxtPlayers.gridwidth = 2;
		gbcTxtPlayers.insets = new Insets(5, 5, 5, 0);
		gbcTxtPlayers.fill = GridBagConstraints.BOTH;
		gbcTxtPlayers.gridx = 2;
		gbcTxtPlayers.gridy = 1;
		frame.getContentPane().add(txtPlayers, gbcTxtPlayers);

		JLabel lblConfigTitle = new JLabel("Configuration");
		lblConfigTitle.setFont(DEFAULT_FONT_BOLD);
		GridBagConstraints gbcLblConfigTitle = new GridBagConstraints();
		gbcLblConfigTitle.gridwidth = 2;
		gbcLblConfigTitle.fill = GridBagConstraints.BOTH;
		gbcLblConfigTitle.insets = new Insets(0, 5, 5, 5);
		gbcLblConfigTitle.gridx = 0;
		gbcLblConfigTitle.gridy = 3;
		frame.getContentPane().add(lblConfigTitle, gbcLblConfigTitle);
		
		JLabel lblCfgAlgo = new JLabel("Algorithm: ");
		GridBagConstraints gbcLblCfgAlgo = new GridBagConstraints();
		gbcLblCfgAlgo.fill = GridBagConstraints.HORIZONTAL;
		gbcLblCfgAlgo.insets = new Insets(0, 10, 5, 5);
		gbcLblCfgAlgo.gridx = 0;
		gbcLblCfgAlgo.gridy = 4;
		frame.getContentPane().add(lblCfgAlgo, gbcLblCfgAlgo);
		
		JComboBox<Object> cbxCfgAlgo = new JComboBox<>(cards.sort.getAll().toArray());
		GridBagConstraints gbcCbxCfgAlgo = new GridBagConstraints();
		gbcCbxCfgAlgo.insets = new Insets(0, 0, 5, 5);
		gbcCbxCfgAlgo.fill = GridBagConstraints.HORIZONTAL;
		gbcCbxCfgAlgo.gridx = 1;
		gbcCbxCfgAlgo.gridy = 4;
		frame.getContentPane().add(cbxCfgAlgo, gbcCbxCfgAlgo);
		
		JLabel lblCfgType = new JLabel("Type: ");
		GridBagConstraints gbcLblCfgType = new GridBagConstraints();
		gbcLblCfgType.fill = GridBagConstraints.HORIZONTAL;
		gbcLblCfgType.insets = new Insets(0, 10, 5, 5);
		gbcLblCfgType.gridx = 0;
		gbcLblCfgType.gridy = 5;
		frame.getContentPane().add(lblCfgType, gbcLblCfgType);
		
		JComboBox<Type> cbxCfgType = new JComboBox<>(); //Type.values());
		GridBagConstraints gbcCbxCfgType = new GridBagConstraints();
		gbcCbxCfgType.insets = new Insets(0, 0, 5, 5);
		gbcCbxCfgType.fill = GridBagConstraints.HORIZONTAL;
		gbcCbxCfgType.gridx = 1;
		gbcCbxCfgType.gridy = 5;
		frame.getContentPane().add(cbxCfgType, gbcCbxCfgType);
		
		JLabel lblCfgMode = new JLabel("Mode: ");
		GridBagConstraints gbcLblCfgMode = new GridBagConstraints();
		gbcLblCfgMode.fill = GridBagConstraints.HORIZONTAL;
		gbcLblCfgMode.insets = new Insets(0, 10, 5, 5);
		gbcLblCfgMode.gridx = 0;
		gbcLblCfgMode.gridy = 6;
		frame.getContentPane().add(lblCfgMode, gbcLblCfgMode);
		
		JComboBox<Mode> cbxCfgMode = new JComboBox<>(); //Mode.values());
		GridBagConstraints gbcCbxCfgMode = new GridBagConstraints();
		gbcCbxCfgMode.insets = new Insets(0, 0, 5, 5);
		gbcCbxCfgMode.fill = GridBagConstraints.HORIZONTAL;
		gbcCbxCfgMode.gridx = 1;
		gbcCbxCfgMode.gridy = 6;
		frame.getContentPane().add(cbxCfgMode, gbcCbxCfgMode);
		

		JLabel lblCfgSize = new JLabel("Size: ");
		GridBagConstraints gbcLblCfgSize = new GridBagConstraints();
		gbcLblCfgSize.fill = GridBagConstraints.HORIZONTAL;
		gbcLblCfgSize.insets = new Insets(0, 10, 0, 5);
		gbcLblCfgSize.gridx = 0;
		gbcLblCfgSize.gridy = 7;
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
		gbcSldrSize.insets = new Insets(0, 0, 0, 5);
		gbcSldrSize.gridx = 1;
		gbcSldrSize.gridy = 7;
		frame.getContentPane().add(sldrCfgSize, gbcSldrSize);

		JButton btnPlay = new JButton(loadIcon(PLAY_ICON));
		btnPlay.setEnabled(false);
		GridBagConstraints gbcBtnPlay = new GridBagConstraints();
		gbcBtnPlay.gridwidth = 2;
		gbcBtnPlay.gridheight = 3;
		gbcBtnPlay.gridx = 2;
		gbcBtnPlay.gridy = 5;
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
