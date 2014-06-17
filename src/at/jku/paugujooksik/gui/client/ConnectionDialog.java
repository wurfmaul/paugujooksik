package at.jku.paugujooksik.gui.client;

import static at.jku.paugujooksik.tools.Constants.*;
import static at.jku.paugujooksik.tools.ResourceLoader.PLAY_ICON_SMALL;
import static at.jku.paugujooksik.tools.ResourceLoader.STOP_ICON_SMALL;
import static at.jku.paugujooksik.tools.ResourceLoader.loadIcon;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

import at.jku.paugujooksik.server.ServerControl;

public class ConnectionDialog extends JDialog {
	private static final long serialVersionUID = 1513880741540102732L;

	private final JTextField txtHost;
	private final JTextField txtPort;
	private final JTextField txtName;
	private Thread registerThread;
	private JLabel lblTitle;

	/**
	 * Create the dialog.
	 */
	public ConnectionDialog(final JFrame parent, final ClientGUI target) {
		super(parent, true);
		setBounds(100, 100, 400, 200);
		setTitle("Connection Assistant");
		setResizable(false);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 120, 120, 60, 0 };
		gridBagLayout.rowHeights = new int[] { 50, 40, 40, 40, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 1.0, 1.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 1.0, 1.0, 1.0,
				Double.MIN_VALUE };
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
			txtHost = new JTextField("localhost");
			txtHost.setFont(DEFAULT_FONT);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 1;
			gbc.gridy = 1;
			getContentPane().add(txtHost, gbc);
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
			txtPort = new JTextField("1099");
			txtPort.setFont(DEFAULT_FONT);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 1;
			gbc.gridy = 2;
			getContentPane().add(txtPort, gbc);
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
			txtName = new JTextField();
			txtName.setFont(DEFAULT_FONT);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 1;
			gbc.gridy = 3;
			getContentPane().add(txtName, gbc);
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
			okButton.addActionListener(new ConnectionListener(target));
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
					target.quit();
				}
			});
		}
	}

	/**
	 * Launch the application.
	 */
	public static void init(JFrame parent, ClientGUI target) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		try {
			ConnectionDialog dialog = new ConnectionDialog(parent, target);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class ConnectionListener implements ActionListener {
		private ClientGUI target;

		public ConnectionListener(ClientGUI target) {
			this.target = target;
		}

		public void actionPerformed(ActionEvent e) {
			final JToggleButton sourceBtn = (JToggleButton) e.getSource();
			if (sourceBtn.isSelected()) {
				final String host = txtHost.getText().trim();
				final int port = Integer.parseInt(txtPort.getText());
				final String name = txtName.getText().trim();
				try {
					Registry reg = LocateRegistry.getRegistry(host, port);
					final ServerControl p = (ServerControl) reg.lookup("pres");
					if (name.equals("")) {
						JOptionPane.showMessageDialog(ConnectionDialog.this,
								"Please enter your name!", "Naming error",
								JOptionPane.ERROR_MESSAGE);
						sourceBtn.setSelected(false);
					} else if (!p.register(name)) {
						JOptionPane.showMessageDialog(ConnectionDialog.this,
								"Please choose another name!", "Naming error",
								JOptionPane.ERROR_MESSAGE);
						sourceBtn.setSelected(false);
					} else {
						txtHost.setEnabled(false);
						txtName.setEnabled(false);
						txtPort.setEnabled(false);
						lblTitle.setText("Waiting for host...");

						target.setName(name);
						target.setPres(p);

						registerThread = new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									while (!p.isRunning()) {
										Thread.sleep(500);
									}
									ConnectionDialog.this.dispose();
								} catch (InterruptedException | RemoteException e) {
									try {
										p.unregister(name);
									} catch (RemoteException e1) {
									}
								}
							}
						});
						registerThread.start();
					}
				} catch (RemoteException | NotBoundException ex) {
					JOptionPane.showMessageDialog(ConnectionDialog.this,
							"Host cannot be found!", "Network error",
							JOptionPane.ERROR_MESSAGE);
					sourceBtn.setSelected(false);
				}
			} else {
				registerThread.interrupt();
				txtHost.setEnabled(true);
				txtName.setEnabled(true);
				txtPort.setEnabled(true);
				lblTitle.setText("Connect to host...");
			}
		}
	}
}
