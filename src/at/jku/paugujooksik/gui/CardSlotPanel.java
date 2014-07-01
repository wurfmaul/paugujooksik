package at.jku.paugujooksik.gui;

import static at.jku.paugujooksik.tools.Constants.INDEX_COLOR;
import static at.jku.paugujooksik.tools.Constants.INDEX_FONT;

import java.awt.BorderLayout;

import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class CardSlotPanel extends AbstractPanel {
	private static final long serialVersionUID = 4041694836525555796L;

	public CardSlotPanel(int index) {
		super(new BorderLayout());
		setOpaque(false);
		final TitledBorder border = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED));
		{
			border.setTitle(Integer.toString(index + 1));
			border.setTitlePosition(TitledBorder.TOP);
			border.setTitleJustification(TitledBorder.CENTER);
			border.setTitleColor(INDEX_COLOR);
			border.setTitleFont(INDEX_FONT);
		}
		setBorder(border);
	}
}
