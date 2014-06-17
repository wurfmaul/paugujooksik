package at.jku.paugujooksik.gui.client;

import static at.jku.paugujooksik.gui.ResourceLoader.PLAY_ICON_SMALL;
import static at.jku.paugujooksik.gui.ResourceLoader.STOP_ICON_SMALL;
import static at.jku.paugujooksik.gui.ResourceLoader.loadIcon;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;

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
	private static final Logger DEBUGLOG = Logger.getLogger("DEBUG");

	private final JTextField txtHost;
	private final JTextField txtPort;
	private final JTextField txtName;
	private final ClientGUI target;
	private Thread registerThread;

	/**
	 * Create the dialog.
	 */
	public ConnectionDialog(final JFrame parent, final ClientGUI target) {
		super(parent, true);
		this.target = target;
		setBounds(100, 100, 400, 200);
		// TODO unregister on closing dialog
//		addWindowListener(new WindowAdapter() {
//			@Override
//			public void windowClosed(WindowEvent e) {
//				target.quit();
//			}
//		});
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 120, 120, 60, 0 };
		gridBagLayout.rowHeights = new int[] { 50, 40, 40, 40, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 1.0, 1.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 1.0, 1.0, 1.0,
				Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);
		{
			JLabel lblTitle = new JLabel("Connect to host...");
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
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 0;
			gbc.gridy = 1;
			getContentPane().add(lblHost, gbc);
		}
		{
			txtHost = new JTextField();
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 1;
			gbc.gridy = 1;
			getContentPane().add(txtHost, gbc);
		}
		{
			JLabel lblPort = new JLabel("Port: ");
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
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 1;
			gbc.gridy = 2;
			getContentPane().add(txtPort, gbc);
		}
		{
			JLabel lblName = new JLabel("Your name: ");
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
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					final JToggleButton source = (JToggleButton) e.getSource();
					if (source.isSelected()) {
						final String host = txtHost.getText().trim();
						final int port = Integer.parseInt(txtPort.getText());
						final String name = txtName.getText().trim();
						if (name.equals("")) {
							JOptionPane.showMessageDialog(
									ConnectionDialog.this,
									"Please enter your name!", "Naming error",
									JOptionPane.ERROR_MESSAGE);
							source.setSelected(false);
						} else {
							setupRegistry(host, port, name);
						}
					} else {
						registerThread.interrupt();
					}
				}
			});
		}
		{
			JButton cancelButton = new JButton("Cancel");
			GridBagConstraints gbc_cancelButton = new GridBagConstraints();
			gbc_cancelButton.gridx = 2;
			gbc_cancelButton.gridy = 3;
			getContentPane().add(cancelButton, gbc_cancelButton);
			cancelButton.setActionCommand("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					target.quit();
				}
			});
		}
	}

	private void setupRegistry(String host, int port, final String name) {
		try {
			Registry reg = LocateRegistry.getRegistry(host, port);
			final ServerControl p = (ServerControl) reg.lookup("pres");
			target.setName(name);
			target.setPres(p);

			registerThread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						p.register(name); // FIXME forbid same name!
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
		} catch (RemoteException | NotBoundException e) {
			// TODO timeout
			DEBUGLOG.severe("Could not connect to host!");
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
}
