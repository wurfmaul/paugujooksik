package at.jku.paugujooksik.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnimationListener implements ActionListener {

	private final CardPanel leftCard;
	private final CardPanel rightCard;

	public AnimationListener(CardPanel leftCard, CardPanel rightCard) {
		this.leftCard = leftCard;
		this.rightCard = rightCard;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("keep moving");
		leftCard.setLocation(leftCard.getLocation().x + 10, 0);
		leftCard.repaint();
		rightCard.repaint();
	}

}
