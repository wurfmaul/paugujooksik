package at.jku.paugujooksik.gui;

import static at.jku.paugujooksik.tools.Constants.ANIMATION_SPEED;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class AnimationListener implements ActionListener {

	private final CardSetHandler target;
	private final CardPanel leftCard;
	private final CardPanel rightCard;
	private final Point leftDest;
	private final Point rightDest;
	private final String clientId;
	private final Timer timer;

	public AnimationListener(CardPanel leftCard, CardPanel rightCard,
			CardSetHandler target, String clientId) {
		this.leftCard = leftCard;
		this.rightCard = rightCard;
		this.target = target;
		this.clientId = clientId;
		timer = new Timer(40, this);
		leftDest = rightCard.getLocation();
		rightDest = leftCard.getLocation();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Point leftLoc = leftCard.getLocation();
		Point rightLoc = rightCard.getLocation();
		if (leftLoc.equals(leftDest) || rightLoc.equals(rightDest)) {
			timer.stop();
			target.finishSwap(clientId);
			return;
		}

		leftCard.setLocation(Math.min(leftLoc.x + ANIMATION_SPEED, leftDest.x),
				leftLoc.y);
		rightCard
				.setLocation(
						Math.max(rightLoc.x - ANIMATION_SPEED, rightDest.x),
						rightLoc.y);

		leftCard.repaint();
		rightCard.repaint();
	}

	public void start() {
		timer.start();
	}

}
