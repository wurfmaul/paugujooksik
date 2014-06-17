package at.jku.paugujooksik.gui;

import static at.jku.paugujooksik.gui.ResourceLoader.BLACK_CHECK_ICON;
import static at.jku.paugujooksik.gui.ResourceLoader.BLACK_PIN_ICON;
import static at.jku.paugujooksik.gui.ResourceLoader.GRAY_CHECK_ICON;
import static at.jku.paugujooksik.gui.ResourceLoader.GRAY_PIN_ICON;
import static at.jku.paugujooksik.gui.ResourceLoader.ROTATED_PIN_ICON;
import static at.jku.paugujooksik.gui.ResourceLoader.loadIcon;
import static at.jku.paugujooksik.logic.Toolkit.DEFAULT_FONT_BOLD;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import at.jku.paugujooksik.logic.Card;

public class CardPanel extends AbstractPanel {
	private static final long serialVersionUID = 68959464664105468L;

	private static final String DEFAULT_CARD_TEXT = "  ";
	private static final int DEFAULT_BUTTON_SIZE = 25;
	private static final float FONT_SIZE_ONECHAR = 36;
	private static final float FONT_SIZE_TWOCHAR = 24;
	private static final float FONT_SIZE_THREECHAR = 16;

	private final Color markColor = Color.GREEN;
	private final Color errorColor = Color.RED;
	private final Color defaultBackground = Color.LIGHT_GRAY;
	private final String originId;
	private final JLabel label;
	private final JToggleButton pin;
	private final JToggleButton fin;
	private final CardSetHandler target;
	private MouseAdapter cardMouseAdapter;
	private MouseAdapter pinMouseAdapter;
	private MouseAdapter finMouseAdapter;

	public CardPanel(final int index, CardSetHandler target, String originId,
			boolean enableMouseActions) {
		super(new BorderLayout());
		this.target = target;
		this.originId = originId;

		if (enableMouseActions)
			initMouseListeners(index);
		setBackground(defaultBackground);
		setBorder(new CardBorder(false));
		addMouseListener(cardMouseAdapter);
		label = new JLabel(DEFAULT_CARD_TEXT, JLabel.CENTER);
		{
			label.setFont(DEFAULT_FONT_BOLD.deriveFont(FONT_SIZE_ONECHAR));
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
		setBackground(hasError ? errorColor : markColor);
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
		setBackground(c.marked ? markColor : defaultBackground);
	}

	private void initMouseListeners(final int index) {
		cardMouseAdapter = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (staysInsideComponent(e))
					target.performSelect(originId, index);
			}
		};
		pinMouseAdapter = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (staysInsideComponent(e))
					target.performPin(originId, index);
			}
		};
		finMouseAdapter = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (staysInsideComponent(e))
					target.performMark(originId, index);
			}
		};
	}

	private void updateText(Card<?> c) {
		float size = FONT_SIZE_ONECHAR;
		String text = DEFAULT_CARD_TEXT;
		if (c.selected) {
			text = c.toString();
			if (text.length() == 2)
				size = FONT_SIZE_TWOCHAR;
			else if (text.length() == 3)
				size = FONT_SIZE_THREECHAR;

		}
		label.setFont(DEFAULT_FONT_BOLD.deriveFont(size));
		label.setText(text);
	}
}