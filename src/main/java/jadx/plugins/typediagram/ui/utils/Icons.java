package jadx.plugins.typediagram.ui.utils;

import javax.swing.ImageIcon;
import static jadx.plugins.typediagram.ui.utils.IconProvider.openSvgIcon;

public class Icons {
	public static final ImageIcon METHOD = openSvgIcon("nodes/method");
	public static final ImageIcon FIELD = openSvgIcon("nodes/field");

	public static final ImageIcon RESET_ZOOM = openSvgIcon("typediagram/actualZoom");
	public static final ImageIcon ZOOM_IN = openSvgIcon("typediagram/zoomIn");
	public static final ImageIcon ZOOM_OUT = openSvgIcon("typediagram/zoomOut");
	public static final ImageIcon ZOOM_FIT = openSvgIcon("typediagram/fitContent");

	public static final ImageIcon SHOW = openSvgIcon("typediagram/show");

	public static final ImageIcon IMAGE = openSvgIcon("typediagram/image");

	public static final ImageIcon OPEN_IN_NEW_WINDOW = openSvgIcon("typediagram/openInNewWindow");
	public static final ImageIcon HELP = openSvgIcon("typediagram/help");
}
