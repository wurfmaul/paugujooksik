package at.jku.paugujooksik.gui;

import static at.jku.paugujooksik.tools.Constants.ANIMATION_SPEED;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * The task of this class is to perform the animation once two cards are
 * swapped.
 * 
 * @author Wolfgang Kuellinger (0955711), 2014
 *
 */
public class Animator implements ActionListener {
	private final Card leftCard;
	private final Card rightCard;
	private final int leftDestX;
	private final int rightDestY;
	private final Timer timer;

	/**
	 * Creates a new Animator.
	 * 
	 * @param leftCard
	 *            The first card that is to be swapped.
	 * @param rightCard
	 *            The second card that is to be swapped.
	 */
	public Animator(Card leftCard, Card rightCard) {
		this.leftCard = leftCard;
		this.rightCard = rightCard;

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

		leftCard.setLocation(Math.min(leftX + ANIMATION_SPEED, leftDestX), leftY);
		rightCard.setLocation(Math.max(rightX - ANIMATION_SPEED, rightDestY), rightY);

		if (leftX == leftDestX || rightX == rightDestY) {
			timer.stop();
			// tell presenter that animation is done
			leftCard.getView().performSwapStop(leftCard.getClientId());
			return;
		}
	}

	/**
	 * Start the animation.
	 */
	public void start() {
		timer.start();
	}

}
