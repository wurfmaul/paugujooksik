package at.jku.paugujooksik.tools;

import static at.jku.paugujooksik.tools.ResourceLoader.BOLD_SANS_FONT;
import static at.jku.paugujooksik.tools.ResourceLoader.SANS_FONT;
import static at.jku.paugujooksik.tools.ResourceLoader.loadFonts;

import java.awt.Color;
import java.awt.Font;

public class Constants {
	public static final Color[] PLAYER_COLORS;

	public static final Font DEFAULT_FONT;
	public static final Font DEFAULT_FONT_BOLD;
	public static final Font INDEX_FONT;
	public static final Font CARD_LABEL_FONT_ONECHAR;
	public static final Font CARD_LABEL_FONT_TWOCHAR;
	public static final Font CARD_LABEL_FONT_THREECHAR;
	public static final Font TITLE_FONT;
	public static final Font STAT_FONT;
	public static final Font HINT_FONT;

	public static final Color INDEX_COLOR = Color.GRAY;
	public static final Color MARK_COLOR = new Color(60, 179, 113);
	public static final Color ERROR_COLOR = Color.RED;
	public static final Color SELECTED_COLOR = new Color(211, 211, 211);
	public static final Color DEFAULT_COLOR = Color.LIGHT_GRAY;
	public static final Color STAT_LINE_COLOR = new Color(0, 128, 0);
	public static final Color STAT_BACKGROUND_COLOR = new Color(204, 255, 153);
	public static final Color ERROR_LINE_COLOR = new Color(128, 0, 0);
	public static final Color ERROR_BACKGROUND_COLOR = new Color(255, 153, 153);
	public static final Color PLAYER_BORDER_COLOR = Color.LIGHT_GRAY;
	public static final Color POSITIVE_HINT_COLOR = new Color(60, 179, 113);
	public static final Color NEGATIVE_HINT_COLOR = new Color(255, 153, 153);

	public static final String DEFAULT_CARD_TEXT = "  ";
	public static final String ERROR_LABEL = "≠ ";
	public static final String SWAP_LABEL = "↔ ";
	public static final String COMPARE_LABEL = "≶ ";

	public static final int PLAYER_BORDER_THICKNESS = 3;
	public static final int DEFAULT_BUTTON_SIZE = 25;

	public static final boolean PLAYER_BORDER_ISROUNDED = true;

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

		PLAYER_COLORS = new Color[] { new Color(153, 204, 204),
				new Color(204, 153, 204), new Color(204, 204, 153),
				new Color(204, 153, 153), new Color(153, 204, 153),
				new Color(153, 153, 204), new Color(255, 153, 153),
				new Color(153, 255, 153), new Color(153, 153, 255) };
	}
}
