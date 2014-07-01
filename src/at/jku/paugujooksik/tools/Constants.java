package at.jku.paugujooksik.tools;

import static at.jku.paugujooksik.tools.ResourceLoader.BOLD_SANS_FONT;
import static at.jku.paugujooksik.tools.ResourceLoader.SANS_FONT;
import static at.jku.paugujooksik.tools.ResourceLoader.loadFonts;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.jku.paugujooksik.model.ValueGenerator.ValueMode;
import at.jku.paugujooksik.model.ValueGenerator.ValueType;

public class Constants {
	// GLOBAL CONSTANTS

	/** The global logger */
	public static final Logger DEBUGLOG = Logger.getLogger("DEBUG");
	/** The {@link Level} of the global logger. */
	public static final Level LOGLEVEL = Level.SEVERE;
	/** The default font that should be used everywhere. */
	public static final Font DEFAULT_FONT;
	/** The default font in bold version. */
	public static final Font DEFAULT_FONT_BOLD;
	/** The default bold font that should be used for every title. */
	public static final Font TITLE_FONT;
	/** An array of all available graphic devices (e.g. monitors). */
	public static final GraphicsDevice[] DISPLAY_DEVICES;
	/** If true, animations are used for swapping. */
	public static final boolean USE_ANIMATION = true;
	/** The speed defines how fast the card is moved. The higher, the faster. */
	public static final int ANIMATION_SPEED = 20;
	/** The minimum specifiable amount of cards. */
	public static final int MIN_SIZE = 7;
	/** The maximum specifiable amount of cards. */
	public static final int MAX_SIZE = 15;
	/** The default amount of cards. */
	public static final int DEFAULT_SIZE = 7;
	/** The default value type of the cards. */
	public static final ValueType DEFAULT_TYPE = ValueType.NUMBERS;
	/** The default value range of the cards. */
	public static final ValueMode DEFAULT_MODE = ValueMode.SMALL;

	// CARD CONSTANTS

	/** The font that is used if the card's label length is 1. */
	public static final Font CARD_LABEL_FONT_ONECHAR;
	/** The font that is used if the card's label length is 2. */
	public static final Font CARD_LABEL_FONT_TWOCHAR;
	/** The font that is used if the card's label length is 3. */
	public static final Font CARD_LABEL_FONT_THREECHAR;
	/** The font that is used for the card's index. */
	public static final Font INDEX_FONT;
	/** The font that is used for the hints. */
	public static final Font HINT_FONT;
	/** The default color of the hint text. */
	public static final Color DEFAULT_HINT_COLOR = Color.BLACK;
	/** The used color, when the hint is positive (e.g. game won). */
	public static final Color POSITIVE_HINT_COLOR = new Color(60, 179, 113);
	/** The used color, when the hint is negative (e.g. errors). */
	public static final Color NEGATIVE_HINT_COLOR = new Color(255, 153, 153);
	/** The color of the card's index. */
	public static final Color INDEX_COLOR = Color.GRAY;
	/** The card's color once it is marked. */
	public static final Color MARK_COLOR = new Color(60, 179, 113);
	/** The card's color once it is selected. */
	public static final Color SELECTED_COLOR = new Color(211, 211, 211);
	/** The card's color once it was wrongly sorted. */
	public static final Color ERROR_COLOR = Color.RED;
	/** The card's default color. */
	public static final Color DEFAULT_COLOR = Color.LIGHT_GRAY;
	/** The insets used for the card's border. */
	public static final int INSET = 10;
	/** The size of the pin/mark buttons. */
	public static final int DEFAULT_BUTTON_SIZE = 25;
	/** The card's default label (e.g. "?") */
	public static final String DEFAULT_CARD_TEXT = "  ";
	/** If true, the cards are pinned, otherwise their slots. */
	public static final boolean MOVE_PIN_ON_SWAP = true;
	/** If true, the cards are marked, otherwise their slots. */
	public static final boolean MOVE_MARK_ON_SWAP = true;
	/** The font for the swap button label. */
	public static final Font SWAP_FONT;

	// STATISTICS CONSTANTS

	/** The font that is used for the statistics. */
	public static final Font STAT_FONT;
	/** The border color of the statistics panels (except for errors). */
	public static final Color STAT_LINE_COLOR = new Color(0, 128, 0);
	/** The background color of the statistics panels (except for errors). */
	public static final Color STAT_BACKGROUND_COLOR = new Color(204, 255, 153);
	/** The border color of the error statistics panels. */
	public static final Color ERROR_LINE_COLOR = new Color(128, 0, 0);
	/** The background color of the error statistics panels. */
	public static final Color ERROR_BACKGROUND_COLOR = new Color(255, 153, 153);
	/** The label of the error statistics panel. */
	public static final String ERROR_LABEL = "≠";
	/** The label of the swap statistics panel. */
	public static final String SWAP_LABEL = "↔";
	/** The label of the compare statistics panel. */
	public static final String COMPARE_LABEL = "≶";

	// PRESENTATION CONSTANTS

	/** The background colors of the player's panels. */
	public static final Color[] PLAYER_COLORS;
	/** The color of the border around each player. */
	public static final Color PLAYER_BORDER_COLOR = Color.LIGHT_GRAY;
	/** The thickness of the border around each player. */
	public static final int PLAYER_BORDER_THICKNESS = 3;
	/** The minimum delay between two actions. */
	public static final int QUEUE_DELAY = 300;

	// CONNECTION CONSTANTS

	/** The proposed host address. */
	public static final String DEFAULT_HOST = "localhost";
	/** The proposed host port. */
	public static final int DEFAULT_PORT = 1099;
	/** ID for shared objects, basically it should simply be unique. */
	public static final String BINDING_ID = "x98873543132";
	/** If true, show IPv6 addresses in connection dialog. */
	public static final boolean SHOW_IP6_ADDRESSES = false;

	static {
		if (loadFonts()) {
			DEFAULT_FONT = new Font(SANS_FONT, Font.PLAIN, 16);
			DEFAULT_FONT_BOLD = new Font(BOLD_SANS_FONT, Font.BOLD, 16);
		} else {
			DEFAULT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
			DEFAULT_FONT_BOLD = new Font(Font.SANS_SERIF, Font.BOLD, 16);
		}

		INDEX_FONT = DEFAULT_FONT_BOLD.deriveFont(18f);
		CARD_LABEL_FONT_ONECHAR = DEFAULT_FONT_BOLD.deriveFont(36f);
		CARD_LABEL_FONT_TWOCHAR = DEFAULT_FONT_BOLD.deriveFont(24f);
		CARD_LABEL_FONT_THREECHAR = DEFAULT_FONT_BOLD.deriveFont(16f);
		TITLE_FONT = DEFAULT_FONT.deriveFont(30f);
		STAT_FONT = DEFAULT_FONT_BOLD.deriveFont(28f);
		HINT_FONT = DEFAULT_FONT_BOLD.deriveFont(24f);
		SWAP_FONT = DEFAULT_FONT.deriveFont(36f);

		PLAYER_COLORS = new Color[] { new Color(153, 204, 204), new Color(204, 153, 204), new Color(204, 204, 153),
				new Color(204, 153, 153), new Color(153, 204, 153), new Color(153, 153, 204), new Color(255, 153, 153),
				new Color(153, 255, 153), new Color(153, 153, 255) };

		DISPLAY_DEVICES = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

		// deactivate root logger
		final Logger rootLogger = Logger.getLogger("");
		for (Handler h : rootLogger.getHandlers()) {
			rootLogger.removeHandler(h);
		}

		final ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		DEBUGLOG.addHandler(handler);
	}
}
