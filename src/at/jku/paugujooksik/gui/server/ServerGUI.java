package at.jku.paugujooksik.gui.server;

import static at.jku.paugujooksik.model.Configuration.DEFAULT_SIZE;
import static at.jku.paugujooksik.model.Configuration.MAX_SIZE;
import static at.jku.paugujooksik.model.Configuration.MIN_SIZE;
import static at.jku.paugujooksik.tools.Constants.BINDING_ID;
import static at.jku.paugujooksik.tools.Constants.DEFAULT_FONT;
import static at.jku.paugujooksik.tools.Constants.DEFAULT_FONT_BOLD;
import static at.jku.paugujooksik.tools.Constants.DEFAULT_PORT;
import static at.jku.paugujooksik.tools.Constants.SHOW_IP6_ADDRESSES;
import static at.jku.paugujooksik.tools.ResourceLoader.PLAY_ICON;
import static at.jku.paugujooksik.tools.ResourceLoader.STOP_ICON;
import static at.jku.paugujooksik.tools.ResourceLoader.loadIcon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

import at.jku.paugujooksik.model.Configuration;
import at.jku.paugujooksik.model.ValueGenerator.ValueMode;
import at.jku.paugujooksik.model.ValueGenerator.ValueType;
import at.jku.paugujooksik.server.ServerControl;
import at.jku.paugujooksik.server.ServerControlImpl;

public class ServerGUI {
	private static final Logger DEBUGLOG = Logger.getLogger("DEBUG");
	private static final List<String> HOST_ADDRESSES = new LinkedList<>();
	private static final List<String> AVAILABLE_DISPLAYS = new LinkedList<>();
	private static final GraphicsDevice[] DISPLAY_DEVICES;

	private final JFrame frame = new JFrame();
	private final Set<String> registeredClients = new LinkedHashSet<>();

	private boolean joinable = true;
	private Presenter presenter;
	private JToggleButton btnPlay;
	private JTextArea txtPlayers;
	private JComboBox<Object> cbxDisplay;
	private JComboBox<Object> cbxCfgHost;
	private JComboBox<Object> cbxCfgAlgo;
	private JComboBox<ValueType> cbxCfgType;
	private JComboBox<ValueMode> cbxCfgMode;
	private JSlider sldrCfgSize;

	static {
		// compute host addresses
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface
					.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface iface = interfaces.nextElement();
				if (iface.isLoopback() || !iface.isUp())
					continue;

				for (InterfaceAddress addr : iface.getInterfaceAddresses()) {
					InetAddress address = addr.getAddress();
					if (address instanceof Inet4Address || SHOW_IP6_ADDRESSES) {
						HOST_ADDRESSES.add(address.getHostAddress());
					}
				}
			}
		} catch (SocketException e) {
			DEBUGLOG.severe("Could not retrieve IP address.");
		}

		// list all available monitors
		DISPLAY_DEVICES = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getScreenDevices();

		for (int i = 0; i < DISPLAY_DEVICES.length; i++) {
			GraphicsDevice device = DISPLAY_DEVICES[i];
			DisplayMode mode = device.getDisplayMode();
			AVAILABLE_DISPLAYS.add(String.format("%d: [%dx%d]", i,
					mode.getWidth(), mode.getHeight()));
		}
	}

	/**
	 * Create the application.
	 */
	public ServerGUI() {
		setupRegistry();
		initialize();
	}

	private void initialize() {
		frame.setBounds(100, 100, 700, 520);
		frame.setMinimumSize(new Dimension(500, 520));
		frame.setTitle("Paugujooksik - Presenter Mode");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gblFrame = new GridBagLayout();
		gblFrame.columnWidths = new int[] { 175, 175, 175, 175, 0 };
		gblFrame.rowHeights = new int[] { 50, 40, 40, 50, 40, 50, 40, 40, 40,
				40, 0 };
		gblFrame.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0,
				Double.MIN_VALUE };
		gblFrame.rowWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
				1.0, 1.0, 1.0, Double.MIN_VALUE };
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

		JLabel lblCfgIp = new JLabel("Host address: ");
		GridBagConstraints gbcLblFfgIp = new GridBagConstraints();
		gbcLblFfgIp.anchor = GridBagConstraints.WEST;
		gbcLblFfgIp.insets = new Insets(0, 10, 5, 5);
		gbcLblFfgIp.gridx = 0;
		gbcLblFfgIp.gridy = 1;
		frame.getContentPane().add(lblCfgIp, gbcLblFfgIp);

		cbxCfgHost = new JComboBox<>(HOST_ADDRESSES.toArray());
		GridBagConstraints gbcTxtCfgIp = new GridBagConstraints();
		gbcTxtCfgIp.insets = new Insets(0, 0, 5, 5);
		gbcTxtCfgIp.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtCfgIp.gridx = 1;
		gbcTxtCfgIp.gridy = 1;
		frame.getContentPane().add(cbxCfgHost, gbcTxtCfgIp);

		JLabel lblCfgPort = new JLabel("Port: ");
		GridBagConstraints gbcLblCfgPort = new GridBagConstraints();
		gbcLblCfgPort.anchor = GridBagConstraints.WEST;
		gbcLblCfgPort.insets = new Insets(0, 10, 5, 5);
		gbcLblCfgPort.gridx = 0;
		gbcLblCfgPort.gridy = 2;
		frame.getContentPane().add(lblCfgPort, gbcLblCfgPort);

		JTextField txtCfgPort = new JTextField(Integer.toString(DEFAULT_PORT));
		txtCfgPort.setBackground(Color.WHITE);
		GridBagConstraints gbcTxtCfgPort = new GridBagConstraints();
		gbcTxtCfgPort.insets = new Insets(0, 0, 5, 5);
		gbcTxtCfgPort.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtCfgPort.gridx = 1;
		gbcTxtCfgPort.gridy = 2;
		frame.getContentPane().add(txtCfgPort, gbcTxtCfgPort);
		txtCfgPort.setColumns(10);

		JLabel lblPlayerTitle = new JLabel("Players");
		lblPlayerTitle.setFont(DEFAULT_FONT_BOLD);
		GridBagConstraints gbcLblPlayerTitle = new GridBagConstraints();
		gbcLblPlayerTitle.gridwidth = 2;
		gbcLblPlayerTitle.fill = GridBagConstraints.BOTH;
		gbcLblPlayerTitle.insets = new Insets(0, 5, 5, 0);
		gbcLblPlayerTitle.gridx = 2;
		gbcLblPlayerTitle.gridy = 0;
		frame.getContentPane().add(lblPlayerTitle, gbcLblPlayerTitle);

		txtPlayers = new JTextArea();
		txtPlayers.setFont(DEFAULT_FONT);
		txtPlayers.setOpaque(false);
		txtPlayers.setFocusable(false);
		txtPlayers.setBorder(new BevelBorder(BevelBorder.LOWERED));
		GridBagConstraints gbcTxtPlayers = new GridBagConstraints();
		gbcTxtPlayers.gridheight = 6;
		gbcTxtPlayers.gridwidth = 2;
		gbcTxtPlayers.insets = new Insets(5, 5, 5, 5);
		gbcTxtPlayers.fill = GridBagConstraints.BOTH;
		gbcTxtPlayers.gridx = 2;
		gbcTxtPlayers.gridy = 1;
		frame.getContentPane().add(txtPlayers, gbcTxtPlayers);

		JLabel lblDisplayTitle = new JLabel("Display");
		lblDisplayTitle.setFont(DEFAULT_FONT_BOLD);
		GridBagConstraints gbcLblDisplayTitle = new GridBagConstraints();
		gbcLblDisplayTitle.gridwidth = 2;
		gbcLblDisplayTitle.fill = GridBagConstraints.BOTH;
		gbcLblDisplayTitle.insets = new Insets(0, 5, 5, 5);
		gbcLblDisplayTitle.gridx = 0;
		gbcLblDisplayTitle.gridy = 3;
		frame.getContentPane().add(lblDisplayTitle, gbcLblDisplayTitle);

		JLabel lblDisplay = new JLabel("Presentation screen: ");
		GridBagConstraints gbcLblDisplay = new GridBagConstraints();
		gbcLblDisplay.fill = GridBagConstraints.HORIZONTAL;
		gbcLblDisplay.insets = new Insets(0, 10, 5, 5);
		gbcLblDisplay.gridx = 0;
		gbcLblDisplay.gridy = 4;
		frame.getContentPane().add(lblDisplay, gbcLblDisplay);

		cbxDisplay = new JComboBox<>(AVAILABLE_DISPLAYS.toArray());
		if (cbxDisplay.getItemCount() > 1)
			cbxDisplay.setSelectedIndex(1);
		GridBagConstraints gbcCbxDisplay = new GridBagConstraints();
		gbcCbxDisplay.insets = new Insets(0, 0, 5, 5);
		gbcCbxDisplay.fill = GridBagConstraints.HORIZONTAL;
		gbcCbxDisplay.gridx = 1;
		gbcCbxDisplay.gridy = 4;
		frame.getContentPane().add(cbxDisplay, gbcCbxDisplay);

		JLabel lblConfigTitle = new JLabel("Configuration");
		lblConfigTitle.setFont(DEFAULT_FONT_BOLD);
		GridBagConstraints gbcLblConfigTitle = new GridBagConstraints();
		gbcLblConfigTitle.gridwidth = 2;
		gbcLblConfigTitle.fill = GridBagConstraints.BOTH;
		gbcLblConfigTitle.insets = new Insets(0, 5, 5, 5);
		gbcLblConfigTitle.gridx = 0;
		gbcLblConfigTitle.gridy = 5;
		frame.getContentPane().add(lblConfigTitle, gbcLblConfigTitle);

		JLabel lblCfgAlgo = new JLabel("Algorithm: ");
		GridBagConstraints gbcLblCfgAlgo = new GridBagConstraints();
		gbcLblCfgAlgo.fill = GridBagConstraints.HORIZONTAL;
		gbcLblCfgAlgo.insets = new Insets(0, 10, 5, 5);
		gbcLblCfgAlgo.gridx = 0;
		gbcLblCfgAlgo.gridy = 6;
		frame.getContentPane().add(lblCfgAlgo, gbcLblCfgAlgo);

		cbxCfgAlgo = new JComboBox<>(Configuration.getAlgorithmList());
		GridBagConstraints gbcCbxCfgAlgo = new GridBagConstraints();
		gbcCbxCfgAlgo.insets = new Insets(0, 0, 5, 5);
		gbcCbxCfgAlgo.fill = GridBagConstraints.HORIZONTAL;
		gbcCbxCfgAlgo.gridx = 1;
		gbcCbxCfgAlgo.gridy = 6;
		frame.getContentPane().add(cbxCfgAlgo, gbcCbxCfgAlgo);

		JLabel lblCfgType = new JLabel("Type: ");
		GridBagConstraints gbcLblCfgType = new GridBagConstraints();
		gbcLblCfgType.fill = GridBagConstraints.HORIZONTAL;
		gbcLblCfgType.insets = new Insets(0, 10, 5, 5);
		gbcLblCfgType.gridx = 0;
		gbcLblCfgType.gridy = 7;
		frame.getContentPane().add(lblCfgType, gbcLblCfgType);

		cbxCfgType = new JComboBox<>(ValueType.values());
		GridBagConstraints gbcCbxCfgType = new GridBagConstraints();
		gbcCbxCfgType.insets = new Insets(0, 0, 5, 5);
		gbcCbxCfgType.fill = GridBagConstraints.HORIZONTAL;
		gbcCbxCfgType.gridx = 1;
		gbcCbxCfgType.gridy = 7;
		frame.getContentPane().add(cbxCfgType, gbcCbxCfgType);

		JLabel lblCfgMode = new JLabel("Mode: ");
		GridBagConstraints gbcLblCfgMode = new GridBagConstraints();
		gbcLblCfgMode.fill = GridBagConstraints.HORIZONTAL;
		gbcLblCfgMode.insets = new Insets(0, 10, 5, 5);
		gbcLblCfgMode.gridx = 0;
		gbcLblCfgMode.gridy = 8;
		frame.getContentPane().add(lblCfgMode, gbcLblCfgMode);

		cbxCfgMode = new JComboBox<>(ValueMode.values());
		GridBagConstraints gbcCbxCfgMode = new GridBagConstraints();
		gbcCbxCfgMode.insets = new Insets(0, 0, 5, 5);
		gbcCbxCfgMode.fill = GridBagConstraints.HORIZONTAL;
		gbcCbxCfgMode.gridx = 1;
		gbcCbxCfgMode.gridy = 8;
		frame.getContentPane().add(cbxCfgMode, gbcCbxCfgMode);

		JLabel lblCfgSize = new JLabel("Size: ");
		GridBagConstraints gbcLblCfgSize = new GridBagConstraints();
		gbcLblCfgSize.fill = GridBagConstraints.HORIZONTAL;
		gbcLblCfgSize.insets = new Insets(0, 10, 0, 5);
		gbcLblCfgSize.gridx = 0;
		gbcLblCfgSize.gridy = 9;
		frame.getContentPane().add(lblCfgSize, gbcLblCfgSize);

		sldrCfgSize = new JSlider();
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
		gbcSldrSize.gridy = 9;
		frame.getContentPane().add(sldrCfgSize, gbcSldrSize);

		btnPlay = new JToggleButton(loadIcon(PLAY_ICON));
		btnPlay.setRolloverIcon(loadIcon(PLAY_ICON));
		btnPlay.setSelectedIcon(loadIcon(STOP_ICON));
		btnPlay.setEnabled(false);
		GridBagConstraints gbcBtnPlay = new GridBagConstraints();
		gbcBtnPlay.gridwidth = 2;
		gbcBtnPlay.gridheight = 3;
		gbcBtnPlay.gridx = 2;
		gbcBtnPlay.gridy = 7;
		btnPlay.addActionListener(new PresenterListener());
		frame.getContentPane().add(btnPlay, gbcBtnPlay);

		initMenuBar();
		updatePlayerList();
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

	private void updatePlayerList() {
		final StringBuilder sb = new StringBuilder();
		for (String c : registeredClients) {
			sb.append(c);
			sb.append(System.lineSeparator());
		}

		if (registeredClients.size() == 0) {
			sb.append("Waiting for players...");
			if (!btnPlay.isSelected())
				btnPlay.setEnabled(false);
		} else {
			btnPlay.setEnabled(true);
		}
		txtPlayers.setText(sb.toString());
	}

	private void setupRegistry() {
		try {
			ServerControl p = new ServerControlImpl(this);
			Registry reg = LocateRegistry.createRegistry(DEFAULT_PORT);
			reg.bind(BINDING_ID, p);
		} catch (RemoteException | AlreadyBoundException e) {
			DEBUGLOG.severe("Could not share object!");
		}
	}

	public Presenter getPresenter() {
		return presenter;
	}

	public boolean isJoinable() {
		return joinable ;
	}

	public boolean isRunning() {
		return presenter != null;
	}

	public boolean register(String name) {
		if (registeredClients.contains(name))
			return false;
		registeredClients.add(name);
		updatePlayerList();
		return true;
	}

	public void unregister(String clientId) {
		registeredClients.remove(clientId);
		updatePlayerList();
		if (presenter != null)
			presenter.unregister(clientId);
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

	private class PresenterListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			final JToggleButton sourceBtn = (JToggleButton) e.getSource();
			if (sourceBtn.isSelected()) {
				final ValueMode mode = (ValueMode) cbxCfgMode.getSelectedItem();
				final ValueType type = (ValueType) cbxCfgType.getSelectedItem();
				final int sortIdx = cbxCfgAlgo.getSelectedIndex();
				final int size = sldrCfgSize.getValue();
				final Configuration<?> config = Configuration.generate(mode,
						type, sortIdx, size);

				// compute used screen
				int dspDix = cbxDisplay.getSelectedIndex();
				if (dspDix < 0)
					dspDix = 0;
				final GraphicsDevice device = DISPLAY_DEVICES[dspDix];

				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							presenter = new Presenter(frame, config,
									registeredClients, device);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				joinable = false;
			} else {
				registeredClients.clear();
				updatePlayerList();
				presenter.quit();
				presenter = null;
				joinable = true;
			}
		}
	}
}
