package at.jku.paugujooksik.client.gui;

import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class AbstractPanel extends JPanel {
	private static final long serialVersionUID = -7222954027988888359L;
	private static final String DIR = "/img/";

	protected static final String SWAP_ICON = "swap.png";
	protected static final String GRAY_PIN_ICON = "pin-grey.png";
	protected static final String BLACK_PIN_ICON = "pin.png";
	protected static final String ROTATED_PIN_ICON = "pin-rot.png";
	protected static final String BLACK_CHECK_ICON = "check.png";
	protected static final String GRAY_CHECK_ICON = "check-gray.png";

	public AbstractPanel(LayoutManager layout) {
		super(layout);
	}

	/**
	 * Provides {@link Icon} resource from the class path.
	 * 
	 * @param filename
	 *            The file name of the image.
	 * @return a new {@link ImageIcon} using the specified file from the
	 *         classpath.
	 */
	protected Icon icon(String filename) {
		return new ImageIcon(AbstractPanel.class.getResource(DIR + filename));
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
