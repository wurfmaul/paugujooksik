package at.jku.paugujooksik.client.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class CardSlotPanel extends JPanel {
	private static final long serialVersionUID = 4041694836525555796L;

	public CardSlotPanel(int index) {
		super(new BorderLayout());
		setOpaque(false);
		final TitledBorder border = new TitledBorder(new EtchedBorder(
				EtchedBorder.RAISED));
		{
			border.setTitle(Integer.toString(index + 1));
			border.setTitlePosition(TitledBorder.TOP);
			border.setTitleJustification(TitledBorder.CENTER);
		}
		setBorder(border);
	}
}
