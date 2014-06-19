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
import javax.swing.border.SoftBevelBorder;

import at.jku.paugujooksik.model.Card;

public class CardPanel extends AbstractPanel {
	private static final long serialVersionUID = 68959464664105468L;

	private final String clientId;
	private final JLabel label;
	private final JToggleButton pin;
	private final JToggleButton fin;
	private final CardSetHandler target;
	private MouseAdapter cardMouseAdapter;
	private MouseAdapter pinMouseAdapter;
	private MouseAdapter finMouseAdapter;

	public CardPanel(final int index, CardSetHandler target, String clientId,
			boolean enableMouseActions) {
		super(new BorderLayout());
		this.target = target;
		this.clientId = clientId;

		if (enableMouseActions)
			initMouseListeners(index);
		setBackground(DEFAULT_COLOR);
		setBorder(new CardBorder(false));
		addMouseListener(cardMouseAdapter);
		label = new JLabel(DEFAULT_CARD_TEXT, JLabel.CENTER);
		{
			label.setFont(CARD_LABEL_FONT_ONECHAR);
			add(label, BorderLayout.CENTER);
		}
		pin = new JToggleButton();
		{
			JPanel pnlPin = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			pin.setBorderPainted(false);
			pin.setFocusable(false);
			pin.setVisible(false);
			pin.setOpaque(false);
			pin.setIcon(loadIcon(GRAY_PIN_ICON));
			pin.setRolloverIcon(loadIcon(BLACK_PIN_ICON));
			pin.setSelectedIcon(loadIcon(ROTATED_PIN_ICON));
			pin.setPreferredSize(new Dimension(DEFAULT_BUTTON_SIZE,
					DEFAULT_BUTTON_SIZE));
			pin.addMouseListener(pinMouseAdapter);
			pnlPin.setOpaque(false);
			pnlPin.add(pin);
			add(pnlPin, BorderLayout.NORTH);
		}
		fin = new JToggleButton();
		{
			JPanel pnlFin = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			fin.setBorderPainted(false);
			fin.setFocusable(false);
			fin.setOpaque(false);
			fin.setIcon(loadIcon(GRAY_CHECK_ICON));
			fin.setSelectedIcon(loadIcon(BLACK_CHECK_ICON));
			fin.setRolloverIcon(loadIcon(BLACK_CHECK_ICON));
			fin.setPreferredSize(new Dimension(DEFAULT_BUTTON_SIZE,
					DEFAULT_BUTTON_SIZE));
			fin.addMouseListener(finMouseAdapter);
			pnlFin.setOpaque(false);
			pnlFin.add(fin);
			add(pnlFin, BorderLayout.SOUTH);
		}
	}

	public void finish(boolean hasError) {
		setBackground(hasError ? ERROR_COLOR : MARK_COLOR);
		removeMouseListener(cardMouseAdapter);
		pin.setVisible(false);
		fin.setVisible(false);
	}

	public void updateCard(Card<?> c) {
		setBorder(new CardBorder(c.selected));
		updateText(c);
		pin.setVisible(c.selected);
		pin.setSelected(c.pinned);
		fin.setSelected(c.marked);
		setOpaque(true);
		setBackground(c.marked ? MARK_COLOR : c.selected ? SELECTED_COLOR
				: DEFAULT_COLOR);
	}

	private void initMouseListeners(final int index) {
		cardMouseAdapter = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (!target.isProcessing(clientId) && staysInsideComponent(e))
					target.performSelect(clientId, index);
			}
		};
		pinMouseAdapter = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (!target.isProcessing(clientId) && staysInsideComponent(e))
					target.performPin(clientId, index);
			}
		};
		finMouseAdapter = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (!target.isProcessing(clientId) && staysInsideComponent(e))
					target.performMark(clientId, index);
			}
		};
	}

	private void updateText(Card<?> c) {
		Font font = CARD_LABEL_FONT_ONECHAR;
		String text = DEFAULT_CARD_TEXT;
		if (c.selected) {
			text = c.toString();
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