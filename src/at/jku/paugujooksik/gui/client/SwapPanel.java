package at.jku.paugujooksik.gui.client;

import static at.jku.paugujooksik.tools.Constants.*;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import at.jku.paugujooksik.gui.AbstractPanel;

/**
 * This panel includes the swap button of the client GUI. The lines to the
 * selected cards are drawn here.
 * 
 * @author Wolfgang Kuellinger (0955711), 2014
 */
public class SwapPanel extends AbstractPanel {
	private static final long serialVersionUID = -7863948686242926432L;

	private final ClientGUI client;
	private final String clientId;

	/**
	 * Create a new panel.
	 * 
	 * @param client
	 *            The current client GUI.
	 */
	public SwapPanel(ClientGUI client) {
		super(null);
		this.client = client;
		this.clientId = client.getName();
	}

	/**
	 * Specify if the button should be shown and the lines be drawn.
	 * 
	 * @param show
	 *            If true, draw the lines and show the button.
	 */
	public void updateButton(boolean show) {
		removeAll();
		if (show) {
			add(new SwapButton());
		}
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (client.showSwapButton()) {
			final Graphics2D g2d = (Graphics2D) g;
			{
				final int left = client.getLeftReference();
				final int right = client.getRightReference();
				final int yOff = 20;
				g2d.drawLine(left, yOff, right, yOff);
				g2d.drawLine(left, 0, left, yOff);
				g2d.drawLine(right, 0, right, yOff);
				g2d.drawLine(getCenter(), yOff, getCenter(), 30);
			}
		}
	}

	/**
	 * @return the center x coordinate of the two selected cards.
	 */
	private int getCenter() {
		final int left = client.getLeftReference();
		final int right = client.getRightReference();
		return Math.min(left, right) + Math.abs(right - left) / 2;
	}

	private class SwapButton extends JButton {
		private static final long serialVersionUID = -6368213501613529319L;
		private static final int OFFSET_Y = 30;
		private static final int WIDTH = 100;
		private static final int HEIGHT = 50;

		public SwapButton() {
			super(SWAP_LABEL);
			setFont(SWAP_FONT);
			setBounds(getCenter() - WIDTH / 2, OFFSET_Y, WIDTH, HEIGHT);
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					if (!client.isProcessing(clientId) && staysInsideComponent(e))
						client.performSwapStart(clientId);
				}
			});
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			setBounds(getCenter() - WIDTH / 2, OFFSET_Y, WIDTH, HEIGHT);
		}
	}
}