package at.jku.paugujooksik.client.gui;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.border.SoftBevelBorder;

class CardBorder extends SoftBevelBorder {
	private static final long serialVersionUID = 822279921799807495L;

	public CardBorder(boolean selected) {
		super(selected ? LOWERED : RAISED);
	}
	
	@Override
	protected void paintLoweredBevel(Component c, Graphics g, int x, int y,
			int width, int height) {
		// TODO rounded corners
		super.paintLoweredBevel(c, g, x, y, width, height);
//		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}
}
