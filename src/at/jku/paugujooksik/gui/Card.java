package at.jku.paugujooksik.gui;

import static at.jku.paugujooksik.tools.Constants.CARD_LABEL_FONT_ONECHAR;
import static at.jku.paugujooksik.tools.Constants.CARD_LABEL_FONT_THREECHAR;
import static at.jku.paugujooksik.tools.Constants.CARD_LABEL_FONT_TWOCHAR;
import static at.jku.paugujooksik.tools.Constants.DEFAULT_BUTTON_SIZE;
import static at.jku.paugujooksik.tools.Constants.DEFAULT_CARD_TEXT;
import static at.jku.paugujooksik.tools.Constants.DEFAULT_COLOR;
import static at.jku.paugujooksik.tools.Constants.ERROR_COLOR;
import static at.jku.paugujooksik.tools.Constants.MARK_COLOR;
import static at.jku.paugujooksik.tools.Constants.SELECTED_COLOR;
import static at.jku.paugujooksik.tools.ResourceLoader.BLACK_CHECK_ICON;
import static at.jku.paugujooksik.tools.ResourceLoader.BLACK_PIN_ICON;
import static at.jku.paugujooksik.tools.ResourceLoader.GRAY_CHECK_ICON;
import static at.jku.paugujooksik.tools.ResourceLoader.GRAY_PIN_ICON;
import static at.jku.paugujooksik.tools.ResourceLoader.ROTATED_PIN_ICON;
import static at.jku.paugujooksik.tools.ResourceLoader.loadIcon;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.SoftBevelBorder;

import at.jku.paugujooksik.model.CardModel;

//@formatter:off
/**
 * The overall GUI architecture can be imagined as following:
 * 
 * <table style="border: 3px solid green; width:100%;" cellspacing="10px">
 *   <tr><td colspan="2"><p>[{@link CardSetContainer}]</p></td></tr>
 *   <tr>
 *     <td style="width:70%; border:1px dashed black;"><p>&nbsp;[{@link JLabel}] name</p></td>
 *     <td style="width:30%; border:1px solid black;">[{@link JLabel}]s stats</td>
 *   </tr>
 *   <tr>
 *     <td colspan="2" style="border:1px dashed black;">
 *       <p>&nbsp;[{@link CardSet}]</p>
 *       <table cellspacing="10px">
 *         <tr>
 *           <td style="border:2px solid gray; padding:5px;">
 *             <p>[{@link CardSlot}]</p>
 *             <p style="border: 1px solid black; height:100px;">[{@link Card}]</p>
 *           </td>
 *           <td style="border:2px solid gray; padding:5px;">
 *             <p>[{@link CardSlot}]</p>
 *             <p style="border: 1px solid black; height:100px;">[{@link Card}]</p>
 *           </td>
 *           <td style="border:2px solid gray; padding:5px;">
 *             <p>[{@link CardSlot}]</p>
 *             <p style="border: 1px solid black; height:100px;">[{@link Card}]</p>
 *           </td>
 *           <td>...</td>
 *         </tr>
 *       </table>
 *     </td>
 *   </tr>
 * </table>
 * 
 * @author Wolfgang Kuellinger (0955711), 2014
 *
 */
//@formatter:on

public class Card extends AbstractPanel {
	private static final long serialVersionUID = 68959464664105468L;

	/** The component that contains this card. */
	public final CardSetContainer container;

	/** The card's caption. */
	private final JLabel label;
	/** The small button to pin the card. */
	private final JToggleButton pinButton;
	/** The small butten to mark the card. */
	private final JToggleButton markButton;

	private MouseAdapter cardMouseAdapter;
	private MouseAdapter pinMouseAdapter;
	private MouseAdapter finMouseAdapter;

	/**
	 * Create a new Card.
	 * 
	 * @param index
	 *            The index that is displayed above the card.
	 * @param container
	 *            The component that contains this card.
	 * @param enableMouseActions
	 *            If true, the buttons are clickable.
	 */
	public Card(final int index, CardSetContainer container) {
		super(new BorderLayout());
		this.container = container;

		final boolean enableMouseActions = container.mouseActionsEnabled;
		if (enableMouseActions)
			initMouseListeners(index);

		setBackground(DEFAULT_COLOR);
		setBorder(new CardBorder(false));
		addMouseListener(cardMouseAdapter);

		label = new JLabel(DEFAULT_CARD_TEXT, SwingConstants.CENTER);
		{
			label.setFont(CARD_LABEL_FONT_ONECHAR);
			add(label, BorderLayout.CENTER);
		}
		pinButton = new JToggleButton();
		{
			final JPanel pnlPin = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			pinButton.setBorderPainted(false);
			pinButton.setFocusable(false);
			pinButton.setVisible(false);
			pinButton.setOpaque(false);
			pinButton.setIcon(loadIcon(GRAY_PIN_ICON));
			pinButton.setRolloverIcon(loadIcon(BLACK_PIN_ICON));
			pinButton.setSelectedIcon(loadIcon(ROTATED_PIN_ICON));
			pinButton.setPreferredSize(new Dimension(DEFAULT_BUTTON_SIZE, DEFAULT_BUTTON_SIZE));
			pinButton.addMouseListener(pinMouseAdapter);
			pnlPin.setOpaque(false);
			pnlPin.add(pinButton);
			add(pnlPin, BorderLayout.NORTH);
		}
		markButton = new JToggleButton();
		{
			final JPanel pnlMark = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			markButton.setBorderPainted(false);
			markButton.setVisible(enableMouseActions);
			markButton.setFocusable(false);
			markButton.setOpaque(false);
			markButton.setIcon(loadIcon(GRAY_CHECK_ICON));
			markButton.setSelectedIcon(loadIcon(BLACK_CHECK_ICON));
			markButton.setRolloverIcon(loadIcon(BLACK_CHECK_ICON));
			markButton.setPreferredSize(new Dimension(DEFAULT_BUTTON_SIZE, DEFAULT_BUTTON_SIZE));
			markButton.addMouseListener(finMouseAdapter);
			pnlMark.setOpaque(false);
			pnlMark.add(markButton);
			add(pnlMark, BorderLayout.SOUTH);
		}
	}

	/**
	 * Designs the card for it's final appearance.
	 * 
	 * @param hasError
	 *            True if the card is on the wrong position.
	 */
	public void finish(boolean hasError) {
		setBackground(hasError ? ERROR_COLOR : MARK_COLOR);
		removeMouseListener(cardMouseAdapter);
		pinButton.setVisible(false);
		markButton.setVisible(false);
	}

	/**
	 * Design the card's layout in order to match its model.
	 * 
	 * @param card
	 *            The card's model. This is the basis for the update.
	 */
	public void synchronize(CardModel<?>.Card card) {
		setBorder(new CardBorder(card.selected));
		updateText(card);
		pinButton.setVisible(card.selected);
		pinButton.setSelected(card.pinned);
		markButton.setSelected(card.marked);
		setOpaque(true);
		setBackground(card.marked ? MARK_COLOR : card.selected ? SELECTED_COLOR : DEFAULT_COLOR);
	}

	private void initMouseListeners(final int index) {
		final PresentationView view = container.view;
		final String name = container.name;

		cardMouseAdapter = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (!view.isProcessing(name) && staysInsideComponent(e))
					view.performSelect(name, index);
			}
		};
		pinMouseAdapter = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (!view.isProcessing(name) && staysInsideComponent(e))
					view.performPin(name, index);
			}
		};
		finMouseAdapter = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (!view.isProcessing(name) && staysInsideComponent(e))
					view.performMark(name, index);
			}
		};
	}

	private void updateText(CardModel<?>.Card card) {
		Font font = CARD_LABEL_FONT_ONECHAR;
		String text = DEFAULT_CARD_TEXT;
		if (card.selected) {
			text = card.toString();
			if (text.length() == 2)
				font = CARD_LABEL_FONT_TWOCHAR;
			else if (text.length() == 3)
				font = CARD_LABEL_FONT_THREECHAR;
		}
		label.setFont(font);
		label.setText(text);
	}

	private class CardBorder extends SoftBevelBorder {
		private static final long serialVersionUID = 822279921799807495L;

		public CardBorder(boolean selected) {
			super(selected ? LOWERED : RAISED);
		}
	}
}