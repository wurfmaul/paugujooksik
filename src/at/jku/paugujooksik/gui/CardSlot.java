package at.jku.paugujooksik.gui;

import static at.jku.paugujooksik.tools.Constants.INDEX_COLOR;
import static at.jku.paugujooksik.tools.Constants.INDEX_FONT;

import java.awt.BorderLayout;

import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * This component is basically the border of a card. This is because it is
 * important that the card itself changes location and not the entire slot.
 * 
 * @author Wolfgang Kuellinger (0955711), 2014
 * @see Card details on the GUI architecture
 */
public class CardSlot extends AbstractPanel {
	private static final long serialVersionUID = 4041694836525555796L;

	/**
	 * Creates a new slot. The index is the slot's caption. The indices are
	 * incremented by one in order to get "human" indices [1, 2, 3, ...] instead
	 * of computer's internal ones [0, 1, 2, ...].
	 * 
	 * @param index
	 *            The index that is displayed as part of this slot's border.
	 */
	public CardSlot(int index) {
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
