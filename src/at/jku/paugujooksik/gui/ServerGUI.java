package at.jku.paugujooksik.gui;

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

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.UIManager;

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
		gblFrame.columnWidths = new int[] { 350, 350, 0 };
		gblFrame.rowHeights = new int[] { 90, 160, 90, 10, 90, 0 };
		gblFrame.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gblFrame.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		frame.getContentPane().setLayout(gblFrame);

		JLabel lblConnTitle = new JLabel("Connection");
		lblConnTitle.setFont(DEFAULT_FONT.deriveFont(24f));
		GridBagConstraints gbcLblConnTitle = new GridBagConstraints(); 
		gbcLblConnTitle.fill = GridBagConstraints.BOTH;
		gbcLblConnTitle.insets = new Insets(0, 5, 5, 5);
		gbcLblConnTitle.gridx = 0;
		gbcLblConnTitle.gridy = 0;
		frame.getContentPane().add(lblConnTitle, gbcLblConnTitle);

		JLabel lblPlayerTitle = new JLabel("Players");
		lblPlayerTitle.setFont(DEFAULT_FONT);
		GridBagConstraints gbcLblPlayerTitle = new GridBagConstraints();
		gbcLblPlayerTitle.fill = GridBagConstraints.BOTH;
		gbcLblPlayerTitle.insets = new Insets(0, 5, 5, 5);
		gbcLblPlayerTitle.gridx = 1;
		gbcLblPlayerTitle.gridy = 0;
		frame.getContentPane().add(lblPlayerTitle, gbcLblPlayerTitle);
		
		txtConnection = new JTextPane();
		txtConnection.setFont(DEFAULT_FONT.deriveFont(24f));
		txtConnection.setOpaque(false);
		txtConnection.setText(printIpConfig());
		GridBagConstraints gbcLblCount = new GridBagConstraints();
		gbcLblCount.insets = new Insets(0, 0, 5, 0);
		gbcLblCount.fill = GridBagConstraints.BOTH;
		gbcLblCount.gridx = 0;
		gbcLblCount.gridy = 1;
		frame.getContentPane().add(txtConnection, gbcLblCount);

		JPanel pnlPlaceholder = new JPanel();
		pnlPlaceholder.setLayout(null);
		GridBagConstraints gbcPnlPlaceholder = new GridBagConstraints();
		gbcPnlPlaceholder.gridwidth = 2;
		gbcPnlPlaceholder.insets = new Insets(0, 0, 0, 0);
		gbcPnlPlaceholder.fill = GridBagConstraints.BOTH;
		gbcPnlPlaceholder.gridx = 0;
		gbcPnlPlaceholder.gridy = 2;
		frame.getContentPane().add(pnlPlaceholder, gbcPnlPlaceholder);

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
