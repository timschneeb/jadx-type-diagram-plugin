package jadx.plugins.typediagram.ui.utils;

import javax.swing.ImageIcon;
import static jadx.gui.utils.UiUtils.openSvgIcon;
import static jadx.plugins.typediagram.ui.utils.IconProvider.openBuiltinSvgIcon;

public class Icons {
	public static final ImageIcon METHOD = openSvgIcon("nodes/method");
	public static final ImageIcon FIELD = openSvgIcon("nodes/field");
	public static final ImageIcon CLASS = openSvgIcon("nodes/class");

	public static final ImageIcon CLASS_ABSTRACT = openSvgIcon("nodes/abstractClass");
	public static final ImageIcon CLASS_PUBLIC = openSvgIcon("nodes/publicClass");
	public static final ImageIcon CLASS_PRIVATE = openSvgIcon("nodes/privateClass");
	public static final ImageIcon CLASS_PROTECTED = openSvgIcon("nodes/protectedClass");
	public static final ImageIcon INTERFACE = openSvgIcon("nodes/interface");
	public static final ImageIcon ENUM = openSvgIcon("nodes/enum");
	public static final ImageIcon ANNOTATION = openSvgIcon("nodes/annotationtype");

	public static final ImageIcon RESET_ZOOM = openBuiltinSvgIcon("typediagram/actualZoom");
	public static final ImageIcon ZOOM_IN = openBuiltinSvgIcon("typediagram/zoomIn");
	public static final ImageIcon ZOOM_OUT = openBuiltinSvgIcon("typediagram/zoomOut");
	public static final ImageIcon ZOOM_FIT = openBuiltinSvgIcon("typediagram/fitContent");

	public static final ImageIcon SHOW = openBuiltinSvgIcon("typediagram/show");

	public static final ImageIcon IMAGE = openBuiltinSvgIcon("typediagram/image");

	public static final ImageIcon OPEN_IN_NEW_WINDOW = openBuiltinSvgIcon("typediagram/openInNewWindow");
	public static final ImageIcon HELP = openBuiltinSvgIcon("typediagram/help");
}
