package at.jku.paugujooksik.gui;

import static at.jku.paugujooksik.tools.Constants.ANIMATION_SPEED;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class AnimationListener implements ActionListener {
	private final CardSetHandler target;
	private final CardPanel leftCard;
	private final CardPanel rightCard;
	private final int leftDestX;
	private final int rightDestY;
	private final String clientId;
	private final Timer timer;

	public AnimationListener(CardPanel leftCard, CardPanel rightCard,
			CardSetHandler target, String clientId) {
		this.leftCard = leftCard;
		this.rightCard = rightCard;
		this.target = target;
		this.clientId = clientId;

		this.leftDestX = rightCard.getLocation().x;
		this.rightDestY = leftCard.getLocation().x;
		this.timer = new Timer(40, this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int leftX = leftCard.getLocation().x;
		int rightX = rightCard.getLocation().x;
		int leftY = leftCard.getLocation().y;
		int rightY = rightCard.getLocation().y;

		leftCard.setLocation(Math.min(leftX + ANIMATION_SPEED, leftDestX),
				leftY);
		rightCard.setLocation(Math.max(rightX - ANIMATION_SPEED, rightDestY),
				rightY);

		if (leftX == leftDestX || rightX == rightDestY) {
			timer.stop();
			target.performSwapStop(clientId);
			return;
		}
	}

	public void start() {
		timer.start();
	}

}
