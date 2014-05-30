package at.jku.paugujooksik.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import at.jku.paugujooksik.client.logic.Card;

public class CardPanel extends JPanel {
	private static final long serialVersionUID = 68959464664105468L;
	
	private static final String GRAY_PIN_ICON = "/img/pin-grey.png";
	private static final String BLACK_PIN_ICON = "/img/pin.png";
	private static final String ROTATED_PIN_ICON = "/img/pin-rot.png";
	private static final String BLACK_CHECK_ICON = "/img/check.png";
	private static final String GRAY_CHECK_ICON = "/img/check-gray.png";
	private static final String DEFAULT_CARD_TEXT = "  ";
	private static final int DEFAULT_BUTTON_SIZE = 25;

	private final Color markColor = Color.GREEN;
	private final Color errorColor = Color.RED;
	private final Color defaultBackground = Color.LIGHT_GRAY;
	private final JLabel label;
	private final JToggleButton pin;
	private final JToggleButton fin;
	private final ClientGUI target;
	private MouseAdapter cardMouseAdapter;
	private MouseAdapter pinMouseAdapter;
	private MouseAdapter finMouseAdapter;

	public CardPanel(final int index, ClientGUI target) {
		super(new BorderLayout());
		this.target = target;
		initMouseListeners(index);
		{
			setBackground(defaultBackground);
			setBorder(new CardBorder(false));
			addMouseListener(cardMouseAdapter);
		}
		label = new JLabel(DEFAULT_CARD_TEXT, JLabel.CENTER);
		{
			label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 36)); //TODO adapt size
			add(label, BorderLayout.CENTER);
		}
		pin = new JToggleButton();
		{
			JPanel pnlPin = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			pin.setBorderPainted(false);
			pin.setFocusable(false);
			pin.setVisible(false);
			pin.setIcon(icon(GRAY_PIN_ICON));
			pin.setRolloverIcon(icon(BLACK_PIN_ICON));
			pin.setSelectedIcon(icon(ROTATED_PIN_ICON));
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
			fin.setIcon(icon(GRAY_CHECK_ICON));
			fin.setSelectedIcon(icon(BLACK_CHECK_ICON));
			fin.setRolloverIcon(icon(BLACK_CHECK_ICON));
			fin.setPreferredSize(new Dimension(DEFAULT_BUTTON_SIZE,
					DEFAULT_BUTTON_SIZE));
			fin.addMouseListener(finMouseAdapter);
			pnlFin.setOpaque(false);
			pnlFin.add(fin);
			add(pnlFin, BorderLayout.SOUTH);
		}
	}

	private Icon icon(String name) {
		return new ImageIcon(getClass().getResource(name));
	}

	private void initMouseListeners(final int index) {
		cardMouseAdapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				target.performSelect(index);
			}
		};
		pinMouseAdapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				target.performPin(index);
			}
		};
		finMouseAdapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				target.performMark(index);
			}
		};
	}

	public void finish(boolean hasError) {
		setBackground(hasError ? errorColor : markColor);
		removeMouseListener(cardMouseAdapter);
		pin.setEnabled(false);
		pin.removeMouseListener(pinMouseAdapter);
		fin.setEnabled(false);
		fin.removeMouseListener(finMouseAdapter);
	}

	public void updateCard(Card<?> c) {
		setBorder(new CardBorder(c.selected));
		label.setText(c.selected ? c.toString() : DEFAULT_CARD_TEXT);
		pin.setVisible(c.selected);
		pin.setSelected(c.pinned);
		fin.setSelected(c.marked);
		setOpaque(true);
		setBackground(c.marked ? markColor : defaultBackground);
	}
}