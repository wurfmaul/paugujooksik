package at.jku.paugujooksik.app;

import static at.jku.paugujooksik.tools.Constants.DEFAULT_FONT;
import static at.jku.paugujooksik.tools.Constants.TITLE_FONT;
import static at.jku.paugujooksik.tools.ResourceLoader.PLAY_ICON;
import static at.jku.paugujooksik.tools.ResourceLoader.loadIcon;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import at.jku.paugujooksik.gui.client.ClientGUI;
import at.jku.paugujooksik.gui.server.ServerGUI;

public class StarterDialog extends JDialog {
	private static final long serialVersionUID = 7372737145331671762L;

	public StarterDialog() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setTitle("Choose a mode!");
		setResizable(false);

		getContentPane().setLayout(new BorderLayout());

		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 120, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JButton btnStandalone = new JButton(loadIcon(PLAY_ICON));
			btnStandalone.setFont(TITLE_FONT);
			btnStandalone.setActionCommand("OK");
			btnStandalone.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ClientGUI.initAndRun(false);
					dispose();
				}
			});
			GridBagConstraints gbcBtnStandalone = new GridBagConstraints();
			gbcBtnStandalone.gridheight = 2;
			gbcBtnStandalone.fill = GridBagConstraints.BOTH;
			gbcBtnStandalone.insets = new Insets(5, 5, 5, 5);
			gbcBtnStandalone.gridx = 0;
			gbcBtnStandalone.gridy = 0;
			contentPanel.add(btnStandalone, gbcBtnStandalone);
			getRootPane().setDefaultButton(btnStandalone);
		}
		{
			JButton btnClient = new JButton("Client Mode");
			btnClient.setFont(DEFAULT_FONT);
			btnClient.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ClientGUI.initAndRun(true);
					dispose();
				}
			});
			GridBagConstraints gbcBtnClient = new GridBagConstraints();
			gbcBtnClient.fill = GridBagConstraints.BOTH;
			gbcBtnClient.insets = new Insets(5, 5, 5, 5);
			gbcBtnClient.gridx = 1;
			gbcBtnClient.gridy = 0;
			contentPanel.add(btnClient, gbcBtnClient);
		}
		{
			JButton btnServer = new JButton("Server Mode");
			btnServer.setFont(DEFAULT_FONT);
			btnServer.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ServerGUI.initAndRun();
					dispose();
				}
			});
			GridBagConstraints gbcBtnServer = new GridBagConstraints();
			gbcBtnServer.fill = GridBagConstraints.BOTH;
			gbcBtnServer.insets = new Insets(5, 5, 5, 5);
			gbcBtnServer.gridx = 1;
			gbcBtnServer.gridy = 1;
			contentPanel.add(btnServer, gbcBtnServer);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.setFont(DEFAULT_FONT);
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}

		setVisible(true);
	}

}
