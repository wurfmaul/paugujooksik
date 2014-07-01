package at.jku.paugujooksik.tools;

import static at.jku.paugujooksik.tools.Constants.DEBUGLOG;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import at.jku.paugujooksik.gui.AbstractPanel;
import at.jku.paugujooksik.gui.client.ClientGUI;

public class ResourceLoader {
	private static final String FONTDIR = "/font/";
	private static final String ICONDIR = "/img/";
	private static final String SNDDIR = "/snd/";

	public static final String BLACK_CHECK_ICON = "check.png";
	public static final String BLACK_PIN_ICON = "pin.png";
	public static final String FULLSCREEN_ICON = "fullscreen.png";
	public static final String GRAY_PIN_ICON = "pin-grey.png";
	public static final String GRAY_CHECK_ICON = "check-gray.png";
	public static final String PLAY_ICON = "play.png";
	public static final String PLAY_ICON_SMALL = "play_small.png";
	public static final String ROTATED_PIN_ICON = "pin-rot.png";
	public static final String STOP_ICON = "stop.png";
	public static final String STOP_ICON_SMALL = "stop_small.png";

	public static final String SANS_FONT = "DejaVuSans";
	public static final String BOLD_SANS_FONT = "DejaVuSans-Bold";

	public static final String ERROR_SND = "beep.wav";

	public static boolean loadFonts() {
		final String[] fonts = new String[] { SANS_FONT, BOLD_SANS_FONT };
		final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

		for (String s : fonts) {
			try {
				URL res = ClientGUI.class.getResource(FONTDIR + s + ".ttf");
				ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, res.openStream()));
			} catch (FontFormatException | IOException e) {
				DEBUGLOG.severe("Could not load font '" + s + "'!");
				return false;
			}
		}
		return true;
	}

	/**
	 * Provides {@link Icon} resource from the class path.
	 * 
	 * @param filename
	 *            The file name of the image.
	 * @return a new {@link ImageIcon} using the specified file from the
	 *         classpath.
	 */
	public static Icon loadIcon(String filename) {
		final URL res = AbstractPanel.class.getResource(ICONDIR + filename);
		if (res == null) {
			DEBUGLOG.severe("Could not load icon '" + filename + "'!");
		}
		return new ImageIcon(res);
	}

	public static Clip loadClip(String filename) {
		try {
			URL url = ClientGUI.class.getResource(SNDDIR + filename);
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(url));
			return clip;
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			DEBUGLOG.severe("Could not load sound '" + filename + "'!");
			return null;
		}
	}
}
