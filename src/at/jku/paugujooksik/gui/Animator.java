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
	private final int rightDestX;
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
		// compute destinations
		this.leftDestX = rightCard.getLocation().x;
		this.rightDestX = leftCard.getLocation().x;
		// setup timer
		this.timer = new Timer(40, this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final int leftX = leftCard.getLocation().x;
		final int rightX = rightCard.getLocation().x;
		final int leftY = leftCard.getLocation().y;
		final int rightY = rightCard.getLocation().y;

		if (leftX == leftDestX || rightX == rightDestX) {
			// if the destinations are reached, stop animation
			timer.stop();
			// tell presenter that animation is done
			final PresentationView view = leftCard.container.view;
			final String name = leftCard.container.name;
			view.performSwapStop(name);
		} else {
			// if there is still a way to go
			leftCard.setLocation(Math.min(leftX + ANIMATION_SPEED, leftDestX), leftY);
			rightCard.setLocation(Math.max(rightX - ANIMATION_SPEED, rightDestX), rightY);
		}
	}

	/**
	 * Start the animation.
	 */
	public void start() {
		timer.start();
	}

}
