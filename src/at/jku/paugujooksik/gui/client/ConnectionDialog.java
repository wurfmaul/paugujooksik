package at.jku.paugujooksik.gui.client;

import static at.jku.paugujooksik.tools.Constants.BINDING_ID;
import static at.jku.paugujooksik.tools.Constants.DEFAULT_FONT;
import static at.jku.paugujooksik.tools.Constants.DEFAULT_FONT_BOLD;
import static at.jku.paugujooksik.tools.Constants.DEFAULT_HOST;
import static at.jku.paugujooksik.tools.Constants.DEFAULT_PORT;
import static at.jku.paugujooksik.tools.Constants.TITLE_FONT;
import static at.jku.paugujooksik.tools.ResourceLoader.PLAY_ICON_SMALL;
import static at.jku.paugujooksik.tools.ResourceLoader.STOP_ICON_SMALL;
import static at.jku.paugujooksik.tools.ResourceLoader.loadIcon;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import at.jku.paugujooksik.network.ServerControl;

/**
 * A small dialog that tries to establish a connection between client and
 * server.
 * 
 * @author Wolfgang Kuellinger (0955711), 2014
 */
public class ConnectionDialog extends JDialog {
	private static final long serialVersionUID = 1513880741540102732L;
	private static final String HISTORY_FILE_DELIM = ";";

	private final JComboBox<String> cbxCfgAddress;
	private final JComboBox<String> cbxCfgPort;
	private final JComboBox<String> cbxCfgName;
	private final JLabel lblTitle;
	private Thread registerThread;

	private ConnectionDialog(final JFrame parent, final ClientGUI client) {
		super(parent, true);
		setBounds(100, 100, 400, 200);
		setTitle("Connection Assistant");
		setResizable(false);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 120, 120, 60, 0 };
		gridBagLayout.rowHeights = new int[] { 50, 40, 40, 40, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 1.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);
		{
			lblTitle = new JLabel("Connect to host...");
			lblTitle.setFont(TITLE_FONT);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridwidth = 3;
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 0;
			gbc.gridy = 0;
			getContentPane().add(lblTitle, gbc);
		}
		{
			JLabel lblHost = new JLabel("Host: ");
			lblHost.setFont(DEFAULT_FONT_BOLD);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 0;
			gbc.gridy = 1;
			getContentPane().add(lblHost, gbc);
		}
		{
			cbxCfgAddress = new JComboBox<>(ConfigHistory.getRecentAddresses());
			if (cbxCfgAddress.getItemCount() == 0)
				cbxCfgAddress.addItem(DEFAULT_HOST);
			cbxCfgAddress.setFont(DEFAULT_FONT);
			cbxCfgAddress.setEditable(true);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 1;
			gbc.gridy = 1;
			getContentPane().add(cbxCfgAddress, gbc);
		}
		{
			JLabel lblPort = new JLabel("Port: ");
			lblPort.setFont(DEFAULT_FONT_BOLD);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 0;
			gbc.gridy = 2;
			getContentPane().add(lblPort, gbc);
		}
		{
			cbxCfgPort = new JComboBox<>(ConfigHistory.getRecentPorts());
			if (cbxCfgPort.getItemCount() == 0)
				cbxCfgPort.addItem(Integer.toString(DEFAULT_PORT));
			cbxCfgPort.setFont(DEFAULT_FONT);
			cbxCfgPort.setEditable(true);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 1;
			gbc.gridy = 2;
			getContentPane().add(cbxCfgPort, gbc);
		}
		{
			JLabel lblName = new JLabel("Your name: ");
			lblName.setFont(DEFAULT_FONT_BOLD);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 0;
			gbc.gridy = 3;
			getContentPane().add(lblName, gbc);
		}
		{
			cbxCfgName = new JComboBox<>(ConfigHistory.getRecentNames());
			cbxCfgName.setFont(DEFAULT_FONT);
			cbxCfgName.setEditable(true);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 1;
			gbc.gridy = 3;
			getContentPane().add(cbxCfgName, gbc);
		}
		{
			JToggleButton okButton = new JToggleButton();
			GridBagConstraints gbc_okButton = new GridBagConstraints();
			gbc_okButton.gridheight = 2;
			gbc_okButton.insets = new Insets(0, 0, 5, 0);
			gbc_okButton.gridx = 2;
			gbc_okButton.gridy = 1;
			getContentPane().add(okButton, gbc_okButton);
			okButton.setActionCommand("OK");
			okButton.setIcon(loadIcon(PLAY_ICON_SMALL));
			okButton.setRolloverIcon(loadIcon(PLAY_ICON_SMALL));
			okButton.setSelectedIcon(loadIcon(STOP_ICON_SMALL));
			okButton.setFocusable(false);
			okButton.addActionListener(new ConnectionListener(client));
		}
		{
			JButton cancelButton = new JButton("Cancel");
			GridBagConstraints gbc_cancelButton = new GridBagConstraints();
			gbc_cancelButton.gridx = 2;
			gbc_cancelButton.gridy = 3;
			getContentPane().add(cancelButton, gbc_cancelButton);
			cancelButton.setFont(DEFAULT_FONT);
			cancelButton.setActionCommand("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					client.quit();
				}
			});
		}
	}

	/**
	 * Create and initialize a new connection dialog.
	 * 
	 * @param parent
	 *            The parent frame.
	 * @param target
	 *            The client that is to be notified about the connection status.
	 */
	public static void init(JFrame parent, ClientGUI target) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		try {
			ConnectionDialog dialog = new ConnectionDialog(parent, target);
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This listener is activated once the "connect" button is pressed.
	 */
	private class ConnectionListener implements ActionListener {
		private ClientGUI target;

		public ConnectionListener(ClientGUI target) {
			this.target = target;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			final JToggleButton sourceBtn = (JToggleButton) e.getSource();
			if (sourceBtn.isSelected()) {
				// if a connection should be established
				final String host = cbxCfgAddress.getEditor().getItem().toString().trim();
				final String port = cbxCfgPort.getEditor().getItem().toString().trim();
				final String name = cbxCfgName.getEditor().getItem().toString().trim();

				// save the current selection to the history
				ConfigHistory.addEntry(host, port, name);
				cbxCfgAddress.addItem(host);
				cbxCfgPort.addItem(port);
				cbxCfgName.addItem(name);

				try {
					// try to connect
					final Registry reg = LocateRegistry.getRegistry(host, Integer.parseInt(port));
					final ServerControl remoteControl = (ServerControl) reg.lookup(BINDING_ID);

					if (name.equals("")) {
						JOptionPane.showMessageDialog(ConnectionDialog.this, "Please enter your name!", "Naming error",
								JOptionPane.ERROR_MESSAGE);
						sourceBtn.setSelected(false);
					} else if (!remoteControl.isJoinable()) {
						// if the server does not want more connections
						JOptionPane.showMessageDialog(ConnectionDialog.this, "The selected server is busy!",
								"Network error", JOptionPane.ERROR_MESSAGE);
						sourceBtn.setSelected(false);
					} else if (!remoteControl.register(name)) {
						// if the name was already chosen by somebody else
						JOptionPane.showMessageDialog(ConnectionDialog.this, "Please choose another name!",
								"Naming error", JOptionPane.ERROR_MESSAGE);
						sourceBtn.setSelected(false);
					} else {
						// if everything worked correctly
						cbxCfgAddress.setEnabled(false);
						cbxCfgName.setEnabled(false);
						cbxCfgPort.setEnabled(false);
						lblTitle.setText("Waiting for host...");

						// notify client about the connection details
						target.setName(name);
						target.setController(remoteControl);

						// wait for server to start game
						registerThread = new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									while (!remoteControl.isRunning()) {
										Thread.sleep(500);
									}
									ConnectionDialog.this.dispose();
								} catch (InterruptedException | RemoteException e) {
									try {
										remoteControl.unregister(name);
									} catch (Exception e1) {
									}
								}
							}
						});
						registerThread.start();
					}
				} catch (RemoteException | NotBoundException ex) {
					JOptionPane.showMessageDialog(ConnectionDialog.this, "Host cannot be found!", "Network error",
							JOptionPane.ERROR_MESSAGE);
					sourceBtn.setSelected(false);
				}
			} else {
				// if the waiting for a connection should be discontinued
				registerThread.interrupt();
				cbxCfgAddress.setEnabled(true);
				cbxCfgName.setEnabled(true);
				cbxCfgPort.setEnabled(true);
				lblTitle.setText("Connect to host...");
			}
		}
	}

	/**
	 * A very primitive history of entered data. Creates a text file that saves
	 * the strings in comma separated lists.
	 */
	private static class ConfigHistory {
		public static final File file = new File("ConnectionHistory.lst");

		private static final int ADDRESS_LINE = 0;
		private static final int PORT_LINE = 1;
		private static final int NAME_LINE = 2;

		public static void addEntry(String address, String port, String name) {
			final Map<Integer, String> map = new LinkedHashMap<>();

			map.put(ADDRESS_LINE, createLine(ADDRESS_LINE, address));
			map.put(PORT_LINE, createLine(PORT_LINE, port));
			map.put(NAME_LINE, createLine(NAME_LINE, name));

			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new FileWriter(file));
				writer.write(map.get(0));
				writer.write(map.get(1));
				writer.write(map.get(2));
			} catch (IOException e) {
			} finally {
				try {
					writer.close();
				} catch (Exception e) {
				}
			}
		}

		public static String[] getRecentAddresses() {
			return getRecentEntries(ADDRESS_LINE).toArray(new String[0]);
		}

		public static String[] getRecentNames() {
			return getRecentEntries(NAME_LINE).toArray(new String[0]);
		}

		public static String[] getRecentPorts() {
			return getRecentEntries(PORT_LINE).toArray(new String[0]);
		}

		private static String createLine(int line, String entry) {
			List<String> addressList = getRecentEntries(line);
			addressList.remove(entry);
			addressList.add(0, entry);
			StringBuilder sb = new StringBuilder();
			Iterator<String> iterator = addressList.iterator();
			while (iterator.hasNext()) {
				sb.append(iterator.next());
				if (iterator.hasNext())
					sb.append(HISTORY_FILE_DELIM);
			}
			sb.append(System.lineSeparator());
			return sb.toString();
		}

		private static List<String> getRecentEntries(int lineNumber) {
			ArrayList<String> list = new ArrayList<>();

			String line = readLine(lineNumber);
			StringTokenizer t = new StringTokenizer(line, HISTORY_FILE_DELIM);

			while (t.hasMoreTokens()) {
				list.add(t.nextToken());
			}

			return list;
		}

		private static String readLine(int lineNumber) {
			BufferedReader reader = null;
			String line = "";
			try {
				reader = new BufferedReader(new FileReader(file));
				int i = 0;
				for (String curLine; (curLine = reader.readLine()) != null; i++) {
					if (i == lineNumber)
						line = curLine;
				}
			} catch (IOException e) {
			} finally {
				try {
					reader.close();
				} catch (Exception e) {
				}
			}
			return line;
		}
	}
}
