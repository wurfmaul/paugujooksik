package at.jku.paugujooksik.gui;

import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class AbstractPanel extends JPanel {
	private static final long serialVersionUID = -7222954027988888359L;

	public AbstractPanel(LayoutManager layout) {
		super(layout);
	}

	/**
	 * Computes whether or not the specified {@link MouseEvent} started and
	 * ended within the same {@link Component}.
	 * 
	 * @param e
	 *            The Event that triggered the {@link MouseListener}.
	 * @return true if mouse click started and ended within same component.
	 */
	protected boolean staysInsideComponent(MouseEvent e) {
		final int width = e.getComponent().getWidth();
		final int height = e.getComponent().getHeight();
		final int x = e.getPoint().x;
		final int y = e.getPoint().y;
		return 0 <= x && x <= width && 0 <= y && y <= height;
	}
}
