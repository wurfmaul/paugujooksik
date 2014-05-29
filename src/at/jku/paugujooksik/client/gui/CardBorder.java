package at.jku.paugujooksik.client.gui;

import javax.swing.border.SoftBevelBorder;

class CardBorder extends SoftBevelBorder {
	private static final long serialVersionUID = 822279921799807495L;

	public CardBorder(boolean selected) {
		super(selected ? LOWERED : RAISED);
	}
}
