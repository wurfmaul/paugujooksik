package at.jku.paugujooksik.client.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class SwapPanel extends JPanel {
	private static final long serialVersionUID = -7863948686242926432L;
	private static final String SWAP_ICON = "/img/swap.png";

	private final ClientGUI target;

	public SwapPanel(ClientGUI target) {
		super();
		this.target = target;
		setLayout(null);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (target.showSwapButton()) {
			final Graphics2D g2d = (Graphics2D) g;
			{
				final int left = target.getLeftReference();
				final int right = target.getRightReference();
				g2d.drawLine(left, 20, right, 20);
				g2d.drawLine(left, 0, left, 20);
				g2d.drawLine(right, 0, right, 20);
				g2d.drawLine(getCenter(), 20, getCenter(), 30);
			}
		}
	}

	public void showButton(boolean show) {
		removeAll();
		if (show) {
			SwapButton btn = new SwapButton();
			btn.setBounds(getCenter() - 50, 30, 100, 50);
			add(btn);
		}
	}

	private int getCenter() {
		final int left = target.getLeftReference();
		final int right = target.getRightReference();
		return left + Math.abs(right - left) / 2;
	}

	private class SwapButton extends JButton {
		private static final long serialVersionUID = -6368213501613529319L;

		public SwapButton() {
			super(new ImageIcon(SwapButton.class.getResource(SWAP_ICON)));

			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					target.performSwap();
				}
			});
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			setBounds(getCenter() - 50, 30, 100, 50);
		}
	}
}