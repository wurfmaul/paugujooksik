package at.jku.paugujooksik.gui;

import static at.jku.paugujooksik.gui.ResourceLoader.SWAP_ICON;
import static at.jku.paugujooksik.gui.ResourceLoader.loadIcon;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

public class SwapPanel extends AbstractPanel {
	private static final long serialVersionUID = -7863948686242926432L;
	private final ClientGUI target;

	public SwapPanel(ClientGUI target) {
		super(null);
		this.target = target;
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
			super(loadIcon(SWAP_ICON));

			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					if (staysInsideComponent(e))
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