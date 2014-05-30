package at.jku.paugujooksik.client.gui;

import java.awt.LayoutManager;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class AbstractPanel extends JPanel {
	private static final long serialVersionUID = -7222954027988888359L;

	protected static final String SWAP_ICON = "/img/swap.png";
	protected static final String GRAY_PIN_ICON = "/img/pin-grey.png";
	protected static final String BLACK_PIN_ICON = "/img/pin.png";
	protected static final String ROTATED_PIN_ICON = "/img/pin-rot.png";
	protected static final String BLACK_CHECK_ICON = "/img/check.png";
	protected static final String GRAY_CHECK_ICON = "/img/check-gray.png";

	public AbstractPanel(LayoutManager layout) {
		super(layout);
	}

	protected Icon icon(String filename) {
		return new ImageIcon(AbstractPanel.class.getResource(filename));
	}

	protected boolean withinSameComponent(MouseEvent e) {
		final int width = e.getComponent().getWidth();
		final int height = e.getComponent().getHeight();
		final int x = e.getPoint().x;
		final int y = e.getPoint().y;
		return 0 <= x && x <= width && 0 <= y && y <= height;
	}
}
